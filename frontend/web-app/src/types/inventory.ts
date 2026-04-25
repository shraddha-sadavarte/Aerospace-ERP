// ============================================================
// Matches: PartResponseDTO.java
// ============================================================
export interface Part {
  id: number;
  name: string;
  serialNumber: string;
  batchNumber: string | null;
  quantity: number | null;
  location: string | null;
  partType: string | null;
  criticality: string | null;
  reorderLevel: number | null;
  status: string | null;
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
  transactionType: string;
  quantity: number;
  referenceDoc: string | null;
  remarks: string | null;
  timestamp: string;
}

// ============================================================
// Matches: InspectionRequestDTO.java in qa-service (port 8082)
// ============================================================
export interface InspectionRequest {
  partId: number;
  batchNumber: string;
  decision: 'APPROVED' | 'REJECTED';
  remarks: string;
  certificateNumber?: string;
  inspectorName?: string;
  location?: string;
}

// ============================================================
// Matches: InspectionResponseDTO.java in qa-service
// ============================================================
export interface InspectionReport {
  id: number;
  partId: number;
  batchNumber: string;
  decision: 'APPROVED' | 'REJECTED';
  remarks: string;
  certificateNumber: string | null;
  inspectorName: string | null;
  inspectedAt: string;
  publishedToInventory: boolean;
}

// ============================================================
// Matches: ApiResponse.java — used by BOTH inventory (8081) and qa (8082)
// ============================================================
export interface ApiResponse<T> {
  message: string;
  data: T;
}

export type StockStatus = 'PENDING_QA' | 'APPROVED' | 'REJECTED' | 'RESERVED' | 'EMPTY';