import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import axios from 'axios';
import { ArrowLeft, ArrowUpCircle, ArrowDownCircle, Clock } from 'lucide-react';
import { InventoryTransaction, ApiResponse } from '../types/inventory';

const TransactionHistory = () => {
  const { id } = useParams<{ id: string }>();
  const [transactions, setTransactions] = useState<InventoryTransaction[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    axios.get<ApiResponse<InventoryTransaction[]>>(`http://localhost:8081/api/v1/inventory/${id}/transactions`)
      .then(res => {
        setTransactions(res.data.data);
        setLoading(false);
      })
      .catch(err => console.error("Error fetching history", err));
  }, [id]);

  return (
    <div className="p-6 bg-gray-50 min-h-screen">
      <div className="max-w-4xl mx-auto">
        <Link to="/inventory" className="flex items-center text-blue-600 mb-6 hover:underline gap-2">
          <ArrowLeft size={18} /> Back to Inventory
        </Link>

        <div className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
          <div className="p-6 border-b border-gray-100">
            <h2 className="text-xl font-bold text-gray-800 flex items-center gap-2">
              <Clock className="text-gray-400" /> Stock Movement History
            </h2>
            <p className="text-sm text-gray-500 mt-1">Detailed log of entries and removals for Part #{id}</p>
          </div>

          {loading ? (
            <div className="p-12 text-center text-gray-400">Loading history...</div>
          ) : (
            <table className="w-full text-left">
              <thead>
                <tr className="bg-gray-50 text-gray-600 text-xs uppercase">
                  <th className="p-4">Type</th>
                  <th className="p-4">Quantity</th>
                  <th className="p-4">Date & Time</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-100">
                {transactions.length > 0 ? transactions.map(t => (
                  <tr key={t.id} className="hover:bg-gray-50">
                    <td className="p-4">
                      <span className={`flex items-center gap-2 font-medium ${
                        t.type === 'IN' ? 'text-green-600' : 'text-red-600'
                      }`}>
                        {t.type === 'IN' ? <ArrowUpCircle size={16} /> : <ArrowDownCircle size={16} />}
                        Stock {t.type}
                      </span>
                    </td>
                    <td className="p-4 font-mono font-bold text-gray-700">
                      {t.type === 'IN' ? '+' : '-'}{t.quantity}
                    </td>
                    <td className="p-4 text-gray-500 text-sm">
                      {new Date(t.timestamp).toLocaleString()}
                    </td>
                  </tr>
                )) : (
                  <tr><td colSpan={3} className="p-10 text-center text-gray-400">No transactions recorded for this part.</td></tr>
                )}
              </tbody>
            </table>
          )}
        </div>
      </div>
    </div>
  );
};

export default TransactionHistory;
