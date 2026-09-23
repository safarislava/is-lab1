export interface RingResponse {
  id: number;
  name: string;
  power: number;
}

export interface RingCreateRequest {
  name: string;
  power: number;
}

export interface RingUpdateRequest {
  name: string;
  power: number;
}
