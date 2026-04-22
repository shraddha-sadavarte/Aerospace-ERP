import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import api from '../api/axios';
import {
  ArrowLeft, ArrowUpRight, ArrowDownLeft,
  Clock, ShieldCheck, Settings, History, Bookmark, ArrowLeftRight,
  CalendarX2
} from 'lucide-react';
import { InventoryTransaction, ApiResponse } from '../types/inventory';

// Transaction types match backend TransactionServiceImpl:
// GRN_RECEIPT, PRODUCTION_ISSUE, QA_DECISION, STOCK_RESERVATION, LOCATION_TRANSFER

const getTransactionStyle = (type: string) => {
  switch (type) {
    case 'GRN_RECEIPT':
      return { color: 'text-emerald-600', bg: 'bg-emerald-50', icon: <ArrowUpRight size={16} />, label: 'Inward (GRN)' };
    case 'PRODUCTION_ISSUE':
      return { color: 'text-orange-600', bg: 'bg-orange-50', icon: <ArrowDownLeft size={16} />, label: 'Production Issue' };
    case 'QA_DECISION':
      return { color: 'text-blue-600', bg: 'bg-blue-50', icon: <ShieldCheck size={16} />, label: 'QA Decision' };
    case 'STOCK_RESERVATION':
      return { color: 'text-violet-600', bg: 'bg-violet-50', icon: <Bookmark size={16} />, label: 'Reserved' };
    case 'LOCATION_TRANSFER':
      return { color: 'text-indigo-600', bg: 'bg-indigo-50', icon: <ArrowLeftRight size={16} />, label: 'Bin Transfer' };
    case 'EXPIRY_REJECTION':
      return { color: 'text-red-600', bg: 'bg-red-50', icon: <CalendarX2 size={16} />, label: 'Expired - Auto Rejected' }
    default:
      return { color: 'text-slate-500', bg: 'bg-slate-50', icon: <Settings size={16} />, label: type };
  }
};

const TransactionHistory = () => {
  const { id } = useParams<{ id: string }>();
  const [transactions, setTransactions] = useState<InventoryTransaction[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.get<ApiResponse<InventoryTransaction[]>>(`/inventory/${id}/transactions`)
      .then(res => {
        setTransactions(res.data.data);
        setLoading(false);
      })
      .catch(err => {
        console.error('Error fetching transaction history', err);
        setLoading(false);
      });
  }, [id]);

  return (
    <div className="space-y-6 animate-in fade-in duration-500">
      <div className="max-w-5xl mx-auto">
        <Link
          to="/inventory"
          className="inline-flex items-center text-slate-500 mb-6 hover:text-blue-600 transition-colors gap-2 font-bold text-sm uppercase tracking-wider"
        >
          <ArrowLeft size={16} /> Back to Inventory Ledger
        </Link>

        <div className="bg-white rounded-3xl shadow-xl shadow-slate-200/50 border border-slate-100 overflow-hidden">

          {/* Header */}
          <div className="p-8 bg-slate-900 text-white">
            <div className="flex justify-between items-start">
              <div>
                <h2 className="text-2xl font-black tracking-tight flex items-center gap-3">
                  <History className="text-blue-400" /> Stock Movement Ledger
                </h2>
                <p className="text-slate-400 text-sm mt-1 font-medium italic">
                  Complete audit trail for Material Resource ID: #{id}
                </p>
              </div>
              <div className="bg-white/10 px-4 py-2 rounded-xl border border-white/10">
                <span className="text-[10px] font-black uppercase tracking-widest text-blue-300">Compliance Verified</span>
              </div>
            </div>
          </div>

          {/* Table */}
          <div className="overflow-x-auto">
            {loading ? (
              <div className="p-20 text-center text-slate-400 font-bold uppercase tracking-widest text-xs animate-pulse">
                Loading Ledger Records...
              </div>
            ) : (
              <table className="w-full text-left border-collapse">
                <thead>
                  <tr className="bg-slate-50 border-b border-slate-100">
                    <th className="p-5 text-[10px] font-black text-slate-400 uppercase tracking-widest">Event Type</th>
                    <th className="p-5 text-[10px] font-black text-slate-400 uppercase tracking-widest text-center">Qty</th>
                    <th className="p-5 text-[10px] font-black text-slate-400 uppercase tracking-widest">Batch</th>
                    {/* referenceDoc — populated once GRN/WO integration is live */}
                    <th className="p-5 text-[10px] font-black text-slate-400 uppercase tracking-widest">Reference Doc</th>
                    <th className="p-5 text-[10px] font-black text-slate-400 uppercase tracking-widest">Remarks</th>
                    <th className="p-5 text-[10px] font-black text-slate-400 uppercase tracking-widest text-right">Timestamp</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-50">
                  {transactions.length > 0 ? transactions.map(t => {
                    const style = getTransactionStyle(t.transactionType);
                    return (
                      <tr key={t.id} className="hover:bg-slate-50/50 transition-colors">
                        <td className="p-5">
                          <div className={`inline-flex items-center gap-2 px-3 py-1.5 rounded-lg font-bold text-xs ${style.bg} ${style.color}`}>
                            {style.icon}
                            {style.label}
                          </div>
                        </td>
                        <td className="p-5 text-center">
                          <span className={`font-mono font-black text-sm ${t.quantity > 0 ? 'text-emerald-600' : t.quantity < 0 ? 'text-orange-600' : 'text-slate-400'}`}>
                            {t.quantity > 0 ? `+${t.quantity}` : t.quantity === 0 ? '—' : t.quantity}
                          </span>
                        </td>
                        <td className="p-5">
                          <span className="bg-slate-100 text-slate-600 px-2 py-1 rounded text-[10px] font-black tracking-wider uppercase border border-slate-200">
                            {t.batchNumber || 'N/A'}
                          </span>
                        </td>
                        <td className="p-5">
                          {t.referenceDoc ? (
                            <span className="bg-blue-50 text-blue-600 px-2 py-1 rounded text-[10px] font-black tracking-wider uppercase border border-blue-100">
                              {t.referenceDoc}
                            </span>
                          ) : (
                            <span className="text-[10px] text-slate-300 italic">—</span>
                          )}
                        </td>
                        <td className="p-5 text-xs text-slate-500 font-medium italic max-w-xs truncate">
                          {t.remarks || 'No remarks.'}
                        </td>
                        <td className="p-5 text-right text-slate-400 text-[11px] font-bold whitespace-nowrap">
                          {new Date(t.timestamp).toLocaleString()}
                        </td>
                      </tr>
                    );
                  }) : (
                    <tr>
                      <td colSpan={6} className="p-20 text-center text-slate-300">
                        <div className="flex flex-col items-center gap-2">
                          <Clock size={48} className="opacity-20 mb-2" />
                          <p className="font-bold uppercase tracking-widest text-xs">No movements registered in this ledger</p>
                        </div>
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            )}
          </div>

        </div>
      </div>
    </div>
  );
};

export default TransactionHistory;
