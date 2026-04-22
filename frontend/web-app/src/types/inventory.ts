// ============================================================
// Matches: PartResponseDTO.java
// ============================================================
export interface Part {
  id: number;
  name: string;
  serialNumber: string;   // maps to skuCode in backend entity
  batchNumber: string | null;
  quantity: number | null;
  location: string | null;
  partType: string | null;
  criticality: string | null;
  reorderLevel: number | null;
  status: string | null;  // PENDING_QA | APPROVED | REJECTED | RESERVED | EMPTY
  qaRemarks: string | null;
  expiryDate: string | null;
}

// ============================================================
// Matches: PartRequestDTO.java
// ============================================================
export interface PartRequest {
  name: string;
  serialNumber: string;
  batchNumber?: string;
  quantity?: number;
  location?: string;
  partType?: string;
  criticality?: string;
  reorderLevel?: number;
  status?: string;
  qaRemarks?: string;
}

// ============================================================
// Matches: DashboardResponseDTO.java
// ============================================================
export interface DashboardStats {
  totalParts: number;
  totalQuantity: number;
  lowStockItems: number;
  outOfStockItems: number;
  pendingQAItems: number;
  rejectedItems: number;
  inventoryValue: number;
  partsByType: Record<string, number>;
}

// ============================================================
// Matches: InventoryTransaction.java entity
// ============================================================
export interface InventoryTransaction {
  id: number;
  partId: number;
  batchNumber: string | null;
  transactionType: string; // GRN_RECEIPT | PRODUCTION_ISSUE | QA_DECISION | STOCK_RESERVATION | LOCATION_TRANSFER
  quantity: number;        // positive = IN, negative = OUT
  referenceDoc: string | null;
  remarks: string | null;
  timestamp: string;       // ISO date string from LocalDateTime
}

// ============================================================
// Matches: ApiResponse.java common wrapper
// Every backend endpoint returns: { message: string, data: T }
// ============================================================
export interface ApiResponse<T> {
  message: string;
  data: T;
}

// ============================================================
// Stock status enum — matches StockStatus.java
// ============================================================
export type StockStatus = 'PENDING_QA' | 'APPROVED' | 'REJECTED' | 'RESERVED' | 'EMPTY';