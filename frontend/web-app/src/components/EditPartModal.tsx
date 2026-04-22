import React, { useState, useEffect } from 'react';
import api from '../api/axios';
import { X, Edit3, ShieldAlert } from 'lucide-react';
import { Part } from '../types/inventory';
import toast from 'react-hot-toast';

interface Props {
  isOpen: boolean;
  part: Part | null;
  onClose: () => void;
  onSuccess: () => void;
}

// Matches backend updatePart() which only accepts master fields:
// name, partType, criticality, reorderLevel, serialNumber
// Stock/batch/location are managed via StockAdjustmentModal

export const EditPartModal = ({ isOpen, part, onClose, onSuccess }: Props) => {
  const [formData, setFormData] = useState({
    name: '',
    partType: '',
    criticality: 'MEDIUM',
    reorderLevel: 0,
  });

  useEffect(() => {
    if (part) {
      setFormData({
        name: part.name,
        partType: part.partType || '',
        criticality: part.criticality || 'MEDIUM',
        reorderLevel: part.reorderLevel || 0,
      });
    }
  }, [part]);

  if (!isOpen || !part) return null;

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    // Only master fields — serialNumber sent to maintain SKU identity
    // Backend: part.setSkuCode(dto.getSerialNumber()) — must not be changed here
    const payload = {
      name: formData.name,
      partType: formData.partType,
      criticality: formData.criticality,
      reorderLevel: formData.reorderLevel,
      serialNumber: part.serialNumber, // preserve existing SKU — never let user change SKU
    };

    try {
      await toast.promise(
        api.put(`/inventory/${part.id}`, payload),
        {
          loading: 'Updating material master...',
          success: 'Master record updated successfully.',
          error: (err) => err.response?.data?.message || 'Update failed.',
        }
      );
      onSuccess();
      onClose();
    } catch (err) {
      // toast.promise handles display
    }
  };

  return (
    <div className="fixed inset-0 bg-slate-900/60 backdrop-blur-md flex items-center justify-center p-4 z-50 animate-in fade-in zoom-in-95 duration-200">
      <div className="bg-white rounded-2xl shadow-2xl w-full max-w-md border border-slate-200">

        <div className="bg-slate-50 px-6 py-4 border-b flex justify-between items-center rounded-t-2xl">
          <div className="flex items-center gap-3">
            <div className="p-2 bg-indigo-600 rounded-lg text-white">
              <Edit3 size={18} />
            </div>
            <div>
              <h2 className="text-lg font-bold text-slate-800 tracking-tight">Edit Material Master</h2>
              <p className="text-[10px] text-slate-500 font-bold uppercase tracking-widest">
                SKU: {part.serialNumber} · ID: #{part.id}
              </p>
            </div>
          </div>
          <button onClick={onClose} className="text-slate-400 hover:text-black transition-colors">
            <X size={20} />
          </button>
        </div>

        <form onSubmit={handleSubmit} className="p-6 space-y-5">
          <div>
            <label className="text-xs font-bold text-slate-500 uppercase mb-1.5 block tracking-wider">Part Name *</label>
            <input
              type="text"
              value={formData.name}
              required
              className="w-full px-4 py-2.5 bg-slate-50 border border-slate-200 rounded-xl focus:ring-4 focus:ring-indigo-500/10 focus:border-indigo-500 outline-none transition-all font-medium"
              onChange={e => setFormData({ ...formData, name: e.target.value })}
            />
          </div>

          <div>
            <label className="text-xs font-bold text-slate-500 uppercase mb-1.5 block tracking-wider">Part Type / Category</label>
            <input
              type="text"
              value={formData.partType}
              placeholder="RAW_MATERIAL, CONSUMABLE, SPARE_PART..."
              className="w-full px-4 py-2.5 bg-slate-50 border border-slate-200 rounded-xl focus:ring-4 focus:ring-indigo-500/10 focus:border-indigo-500 outline-none transition-all"
              onChange={e => setFormData({ ...formData, partType: e.target.value })}
            />
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="text-xs font-bold text-slate-500 uppercase mb-1.5 block tracking-wider">Criticality</label>
              <select
                value={formData.criticality}
                className="w-full px-4 py-2.5 bg-slate-50 border border-slate-200 rounded-xl outline-none focus:border-indigo-500 font-bold"
                onChange={e => setFormData({ ...formData, criticality: e.target.value })}
              >
                <option value="LOW">LOW</option>
                <option value="MEDIUM">MEDIUM</option>
                <option value="HIGH">HIGH</option>
              </select>
            </div>
            <div>
              <label className="text-xs font-bold text-slate-500 uppercase mb-1.5 block tracking-wider">Reorder Point</label>
              <input
                type="number"
                value={formData.reorderLevel}
                min={0}
                className="w-full px-4 py-2.5 bg-slate-50 border border-slate-200 rounded-xl outline-none focus:border-indigo-500 font-bold"
                onChange={e => setFormData({ ...formData, reorderLevel: parseInt(e.target.value) || 0 })}
              />
            </div>
          </div>

          {/* ERP notice — explain why stock fields are not here */}
          <div className="p-4 bg-amber-50 rounded-xl border border-amber-100 flex gap-3">
            <ShieldAlert size={18} className="text-amber-500 mt-0.5 shrink-0" />
            <p className="text-[10px] leading-relaxed text-amber-700 font-medium">
              Physical quantities, batch assignments, and QA statuses are managed via
              <strong> Stock Adjustments</strong> and <strong>QA Decisions</strong> to maintain ledger integrity.
            </p>
          </div>

          <div className="flex gap-3 pt-2">
            <button type="button" onClick={onClose} className="flex-1 py-3 text-slate-500 font-bold text-sm hover:bg-slate-50 rounded-xl transition-all">
              Cancel
            </button>
            <button type="submit" className="flex-[2] bg-slate-900 text-white py-3 rounded-xl font-bold hover:bg-black transition-all active:scale-[0.98] shadow-lg">
              Update Master Record
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};
