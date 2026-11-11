import type { NotificationEvent, EntityType } from '@/types/common';

export type EventHandler = (event: NotificationEvent) => void;

class SseClient {
  private eventSource: EventSource | null = null;
  private url = '/api/notifications/events';
  private handlers = new Map<EntityType, Set<EventHandler>>();
  private reconnectTimer: number | null = null;
  private reconnectDelay = 2000;
  private maxReconnectDelay = 30000;
  private isExplicitlyClosed = false;

  public connect(): void {
    if (this.eventSource) return;
    this.isExplicitlyClosed = false;

    try {
      this.eventSource = new EventSource(this.url);

      this.eventSource.onopen = () => {
        this.reconnectDelay = 2000;
      };

      this.eventSource.addEventListener('entity-change', (e: MessageEvent) => {
        try {
          const parsed: NotificationEvent = JSON.parse(e.data);
          this.handlers.get(parsed.entityType)?.forEach((handler) => handler(parsed));
        } catch (err) {
          console.error('Failed to parse SSE entity-change data:', err);
        }
      });

      this.eventSource.onerror = (err) => {
        console.warn('SSE connection error:', err);
        this.cleanup();
        this.scheduleReconnect();
      };
    } catch (err) {
      console.error('Error instantiating EventSource:', err);
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
  }

  public on(entityType: EntityType, handler: EventHandler): () => void {
    let set = this.handlers.get(entityType);
    if (!set) {
      set = new Set();
      this.handlers.set(entityType, set);
    }
    set.add(handler);
    return () => {
      set?.delete(handler);
    };
  }

  private cleanup(): void {
    if (this.eventSource) {
      this.eventSource.close();
      this.eventSource = null;
    }
  }

  private scheduleReconnect(): void {
    if (this.isExplicitlyClosed || this.reconnectTimer !== null) return;

    this.reconnectTimer = window.setTimeout(() => {
      this.reconnectTimer = null;
      this.reconnectDelay = Math.min(this.reconnectDelay * 1.5, this.maxReconnectDelay);
      this.connect();
    }, this.reconnectDelay);
  }
}

export const sseClient = new SseClient();
