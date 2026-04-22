import React from 'react';
import { AlertTriangle, X } from 'lucide-react';
import { Part } from '../types/inventory';
import { Link } from 'react-router-dom';
import { useState } from 'react';

interface Props {
  parts: Part[];
}

// A part is "low stock" when its approved quantity is below or equal to its reorder level
// Both values come from PartResponseDTO — quantity (live from StockBalance) and reorderLevel (from Part master)

export const LowStockAlert = ({ parts }: Props) => {
  const [dismissed, setDismissed] = useState(false);

  const lowStockParts = parts.filter(p =>
    p.reorderLevel !== null &&
    p.quantity !== null &&
    p.quantity <= p.reorderLevel &&
    p.status === 'APPROVED' // only alert on approved stock — pending/rejected are separate concerns
  );

  if (lowStockParts.length === 0 || dismissed) return null;

  return (
    <div className="bg-amber-50 border border-amber-200 rounded-2xl p-5 animate-in slide-in-from-top-2 duration-500">
      <div className="flex items-start justify-between gap-4">
        <div className="flex items-start gap-3">
          <div className="p-2 bg-amber-100 rounded-xl mt-0.5">
            <AlertTriangle size={18} className="text-amber-600" />
          </div>
          <div>
            <p className="text-sm font-black text-amber-800 uppercase tracking-wide">
              {lowStockParts.length} Material{lowStockParts.length > 1 ? 's' : ''} Below Reorder Level
            </p>
            <p className="text-xs text-amber-600 font-medium mt-0.5">
              Raise procurement requests to avoid production stoppage.
            </p>
            <div className="flex flex-wrap gap-2 mt-3">
              {lowStockParts.map(p => (
                <Link
                  key={p.id}
                  to={`/inventory/${p.id}/transactions`}
                  className="flex items-center gap-2 bg-white border border-amber-200 px-3 py-1.5 rounded-xl hover:bg-amber-100 transition-colors group"
                >
                  <span className="text-[10px] font-black text-slate-600 uppercase tracking-wider">{p.name}</span>
                  <span className="text-[10px] font-mono text-red-500 font-black">
                    {p.quantity} / {p.reorderLevel}
                  </span>
                </Link>
              ))}
            </div>
          </div>
        </div>
        <button
          onClick={() => setDismissed(true)}
          className="text-amber-400 hover:text-amber-700 transition-colors mt-0.5 shrink-0"
          title="Dismiss"
        >
          <X size={16} />
        </button>
      </div>
    </div>
  );
};
