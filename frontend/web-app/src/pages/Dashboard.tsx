import React, { useEffect, useState, useRef } from 'react';
import api from '../api/axios';
import { Package, AlertTriangle, Database, Activity, ShieldAlert, Ban, TrendingUp, RefreshCw } from 'lucide-react';
import { DashboardStats, ApiResponse, Part } from '../types/inventory';
import { LowStockAlert } from '../components/LowStockAlert';

// FIX: Math.random() in render causes a new ID on every re-render.
// Generate it once with useRef so it stays stable for the session.

const Dashboard = () => {
  const [stats, setStats] = useState<DashboardStats | null>(null);
  const [parts, setParts] = useState<Part[]>([]);
  const [loading, setLoading] = useState(true);

  // Stable session terminal ID — generated once, never changes
  const terminalId = useRef(Math.random().toString(36).substring(2, 9).toUpperCase());

  const fetchAll = async () => {
    setLoading(true);
    try {
      const [statsRes, partsRes] = await Promise.all([
        api.get<ApiResponse<DashboardStats>>('/dashboard/stats'),
        api.get<ApiResponse<Part[]>>('/inventory'),
      ]);
      setStats(statsRes.data.data);
      setParts(partsRes.data.data);
    } catch {
      // Individual errors handled by axios interceptor
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { fetchAll(); }, []);

  if (loading || !stats) return (
    <div className="flex items-center justify-center h-screen bg-slate-50">
      <div className="text-center animate-pulse">
        <Activity className="mx-auto text-blue-600 mb-4" size={48} />
        <p className="text-slate-500 font-bold tracking-widest uppercase text-xs">Syncing Ledger...</p>
      </div>
    </div>
  );

  const cards = [
    {
      title: 'SKU Records',
      val: stats.totalParts.toLocaleString(),
      icon: <Database size={20} />,
      color: 'from-blue-600 to-blue-700',
      note: 'Registered parts'
    },
    {
      title: 'Total Approved Units',
      val: stats.totalQuantity.toLocaleString(),
      icon: <Activity size={20} />,
      color: 'from-slate-800 to-slate-900',
      note: 'Available for production'
    },
    {
      title: 'In QA Inspection',
      val: stats.pendingQAItems.toLocaleString(),
      icon: <ShieldAlert size={20} />,
      color: 'from-amber-500 to-amber-600',
      note: 'Awaiting QA decision'
    },
    {
      title: 'Rejected Lots',
      val: stats.rejectedItems.toLocaleString(),
      icon: <Ban size={20} />,
      color: 'from-red-600 to-red-700',
      note: 'Quarantined stock'
    },
    {
      title: 'Low Stock Alerts',
      // lowStockItems comes from backend — currently 0 until backend logic added
      val: stats.lowStockItems.toLocaleString(),
      icon: <AlertTriangle size={20} />,
      color: 'from-orange-500 to-orange-600',
      note: 'Below reorder level'
    },
    {
      title: 'Inventory Value',
      // inventoryValue is 0.0 until Finance service provides pricing
      val: stats.inventoryValue > 0 ? `$${stats.inventoryValue.toLocaleString()}` : 'Pending Pricing',
      icon: <TrendingUp size={20} />,
      color: 'from-indigo-600 to-indigo-700',
      note: 'Finance integration pending'
    },
  ];

  return (
    <div className="space-y-8 animate-in fade-in slide-in-from-bottom-4 duration-700">

      {/* Low stock banner — only shows when parts are below reorder level */}
      <LowStockAlert parts={parts} />

      <section>
        <div className="flex justify-between items-end mb-6">
          <div>
            <h1 className="text-3xl font-black text-slate-900 tracking-tight">Executive Overview</h1>
            <p className="text-slate-500 text-sm font-medium">Real-time material balances and quality compliance status.</p>
          </div>
          <div className="flex items-center gap-3">
            <button
              onClick={fetchAll}
              className="p-2 hover:bg-slate-100 rounded-xl transition-colors text-slate-400 hover:text-slate-700"
              title="Refresh dashboard"
            >
              <RefreshCw size={16} />
            </button>
            {/* FIX: useRef ensures this ID is stable — was Math.random() directly in JSX */}
            <span className="text-[10px] font-black text-slate-400 uppercase tracking-widest bg-slate-100 px-3 py-1 rounded-full">
              Terminal: {terminalId.current}
            </span>
          </div>
        </div>

        {/* KPI Cards */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {cards.map((card, i) => (
            <div key={i} className="group bg-white rounded-3xl p-6 shadow-sm border border-slate-100 hover:shadow-xl hover:-translate-y-1 transition-all duration-300">
              <div className="flex justify-between items-start">
                <div className={`p-4 rounded-2xl bg-gradient-to-br ${card.color} text-white shadow-lg`}>
                  {card.icon}
                </div>
              </div>
              <div className="mt-6">
                <p className="text-[11px] font-black text-slate-400 uppercase tracking-[0.15em] mb-1">{card.title}</p>
                <p className="text-3xl font-black text-slate-900 tracking-tight">{card.val}</p>
                <p className="text-[10px] text-slate-400 mt-1 font-medium">{card.note}</p>
              </div>
            </div>
          ))}
        </div>
      </section>

      {/* Material Categorization — partsByType from backend */}
      <section className="bg-white p-8 rounded-3xl shadow-sm border border-slate-100">
        <div className="flex items-center gap-2 mb-6">
          <div className="w-1.5 h-6 bg-blue-600 rounded-full" />
          <h2 className="text-xl font-black text-slate-800">Material Categorization</h2>
        </div>

        {Object.keys(stats.partsByType).length === 0 ? (
          <p className="text-slate-400 text-sm font-medium">No categorized parts yet. Set part types when registering materials.</p>
        ) : (
          <div className="flex flex-wrap gap-4">
            {Object.entries(stats.partsByType).map(([type, count]) => (
              <div key={type} className="flex items-center gap-3 px-6 py-4 bg-slate-50 rounded-2xl border border-slate-100 hover:bg-blue-50 transition-colors group">
                <div className="w-2 h-2 rounded-full bg-blue-500 group-hover:scale-125 transition-transform" />
                <span className="text-xs font-bold text-slate-500 uppercase tracking-wide">{type}</span>
                <span className="text-sm font-black text-slate-900">{count}</span>
              </div>
            ))}
          </div>
        )}
      </section>

    </div>
  );
};

export default Dashboard;
