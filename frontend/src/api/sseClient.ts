import type { NotificationEvent } from '@/types/common';

export type SseConnectionStatus = 'CONNECTING' | 'CONNECTED' | 'DISCONNECTED' | 'ERROR';

type EventHandler = (event: NotificationEvent) => void;
type StatusHandler = (status: SseConnectionStatus) => void;

class SseClient {
  private eventSource: EventSource | null = null;
  private url = '/api/notifications/events';
  private eventHandlers: Set<EventHandler> = new Set();
  private statusHandlers: Set<StatusHandler> = new Set();
  private reconnectTimer: number | null = null;
  private reconnectDelay = 2000;
  private maxReconnectDelay = 30000;
  private isExplicitlyClosed = false;

  public connect(): void {
    if (this.eventSource) {
      return;
    }
    this.isExplicitlyClosed = false;
    this.updateStatus('CONNECTING');

    try {
      this.eventSource = new EventSource(this.url);

      this.eventSource.onopen = () => {
        this.reconnectDelay = 2000;
        this.updateStatus('CONNECTED');
      };

      this.eventSource.addEventListener('entity-change', (e: MessageEvent) => {
        try {
          const parsed: NotificationEvent = JSON.parse(e.data);
          this.eventHandlers.forEach((handler) => handler(parsed));
        } catch (err) {
          console.error('Failed to parse SSE entity-change data:', err);
        }
      });

      this.eventSource.onerror = (err) => {
        console.warn('SSE connection error:', err);
        this.updateStatus('ERROR');
        this.cleanup();
        this.scheduleReconnect();
      };
    } catch (err) {
      console.error('Error instantiating EventSource:', err);
      this.updateStatus('ERROR');
      this.scheduleReconnect();
    }
  }

  public disconnect(): void {
    this.isExplicitlyClosed = true;
    if (this.reconnectTimer !== null) {
      window.clearTimeout(this.reconnectTimer);
      this.reconnectTimer = null;
    }
    this.cleanup();
    this.updateStatus('DISCONNECTED');
  }

  public onEvent(handler: EventHandler): () => void {
    this.eventHandlers.add(handler);
    return () => this.eventHandlers.delete(handler);
  }

  public onStatusChange(handler: StatusHandler): () => void {
    this.statusHandlers.add(handler);
    return () => this.statusHandlers.delete(handler);
  }

  private cleanup(): void {
    if (this.eventSource) {
      this.eventSource.close();
      this.eventSource = null;
    }
  }

  private updateStatus(status: SseConnectionStatus): void {
    this.statusHandlers.forEach((handler) => handler(status));
  }

  private scheduleReconnect(): void {
    if (this.isExplicitlyClosed) return;
    if (this.reconnectTimer !== null) return;

    this.reconnectTimer = window.setTimeout(() => {
      this.reconnectTimer = null;
      this.reconnectDelay = Math.min(this.reconnectDelay * 1.5, this.maxReconnectDelay);
      this.connect();
    }, this.reconnectDelay);
  }
}

export const sseClient = new SseClient();
