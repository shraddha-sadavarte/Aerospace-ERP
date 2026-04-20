export interface ApiResponse<T> {
  status: string;
  data: T;
}

export interface DashboardStats {
  totalParts: number;
  totalQuantity: number;
  lowStockItems: number;
  outOfStockItems: number;
  partsByType: Record<string, number>;
}

export interface Part {
  id: number;
  name: string;
  serialNumber: string;
  quantity: number;
  location: string | null;
  reorderLevel: number;
}

export interface InventoryTransaction {
  id: number;
  partId: number;
  type: 'IN' | 'OUT';
  quantity: number;
  timestamp: string;
}
