import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Package, AlertTriangle, Database, Activity } from 'lucide-react';
import { DashboardStats, ApiResponse } from '../types/inventory';

const Dashboard = () => {
  const [stats, setStats] = useState<DashboardStats | null>(null);

  useEffect(() => {
    axios.get<ApiResponse<DashboardStats>>('http://localhost:8081/api/v1/dashboard/stats')
      .then(res => setStats(res.data.data))
      .catch(err => console.error("Error fetching stats", err));
  }, []);

  if (!stats) return <div className="p-8 text-center text-gray-500">Loading Dashboard...</div>;

  const cards = [
    { title: 'Total Parts', val: stats.totalParts, icon: <Database />, color: 'bg-blue-500' },
    { title: 'Total Quantity', val: stats.totalQuantity, icon: <Activity />, color: 'bg-green-500' },
    { title: 'Low Stock', val: stats.lowStockItems, icon: <AlertTriangle />, color: 'bg-orange-500' },
    { title: 'Out of Stock', val: stats.outOfStockItems, icon: <Package />, color: 'bg-red-500' },
  ];

  return (
    <div className="p-6 bg-gray-50 min-h-screen">
      <h1 className="text-2xl font-bold mb-6 text-gray-800">Inventory Overview</h1>
      
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        {cards.map((card, i) => (
          <div key={i} className="bg-white p-6 rounded-xl shadow-sm border border-gray-100 flex items-center space-x-4">
            <div className={`${card.color} p-3 rounded-lg text-white`}>{card.icon}</div>
            <div>
              <p className="text-sm text-gray-500 uppercase font-semibold">{card.title}</p>
              <p className="text-2xl font-bold text-gray-900">{card.val}</p>
            </div>
          </div>
        ))}
      </div>

      <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-100">
        <h2 className="text-lg font-semibold mb-4">Parts by Category</h2>
        <div className="flex flex-wrap gap-3">
          {Object.entries(stats.partsByType).map(([type, count]) => (
            <span key={type} className="px-4 py-2 bg-blue-50 text-blue-700 rounded-full text-sm font-medium border border-blue-100">
              {type}: {count}
            </span>
          ))}
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
