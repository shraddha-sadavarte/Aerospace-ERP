import React from 'react';
import { AlertOctagon, ArrowRight } from 'lucide-react';
import { Link } from 'react-router-dom';
import { Part } from '../types/inventory';

interface Props {
  parts: Part[];
}

export const LowStockAlert = ({ parts }: Props) => {
  // Filter logic: quantity <= reorderLevel
  const lowStockItems = parts.filter(p => p.quantity <= (p.reorderLevel || 0));

  if (lowStockItems.length === 0) return null;

  return (
    <div className="mb-6 bg-red-50 border-l-4 border-red-500 p-4 rounded-r-xl shadow-sm">
      <div className="flex items-start gap-3">
        <AlertOctagon className="text-red-500 mt-1 flex-shrink-0" size={20} />
        <div className="flex-1">
          <h3 className="text-red-800 font-bold">Critical Stock Alert</h3>
          <p className="text-red-700 text-sm mb-2">
            The following {lowStockItems.length} items are at or below reorder levels:
          </p>
          <div className="flex flex-wrap gap-2">
            {lowStockItems.map(item => (
              <span key={item.id} className="bg-white px-2 py-1 rounded border border-red-200 text-xs font-semibold text-red-600">
                {item.name} ({item.quantity} left)
              </span>
            ))}
          </div>
        </div>
        <Link to="/inventory" className="text-red-600 hover:text-red-800 flex items-center gap-1 text-sm font-bold whitespace-nowrap">
          Restock Now <ArrowRight size={16} />
        </Link>
      </div>
    </div>
  );
};
