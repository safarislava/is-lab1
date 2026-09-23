export type ActionType = 'CREATE' | 'UPDATE' | 'DELETE';
export type EntityType = 'BOOK_CREATURE' | 'MAGIC_CITY' | 'RING';

export interface NotificationEvent {
  action: ActionType;
  entityType: EntityType;
  entityId: number;
  timestamp: string;
}

export interface PageResponse<T> {
  content: T[];
  total: number;
  page: number;
  size: number;
}

export interface ErrorResponse {
  message: string;
  status: number;
  timestamp: string;
  details?: string[];
}

export type SortDirection = 'ASC' | 'DESC';
