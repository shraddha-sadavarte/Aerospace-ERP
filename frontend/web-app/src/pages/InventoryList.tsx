import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import { 
  Trash2, 
  Edit3, 
  Search, 
  Loader2, 
  Clock, 
  MinusCircle, 
  PlusCircle, 
  Package 
} from 'lucide-react';

// Import local components and types
import { Part, ApiResponse } from '../types/inventory';
import { AddPartModal } from '../components/AddPartModal';
import { EditPartModal } from '../components/EditPartModal';

const InventoryList = () => {
  // --- State Management ---
  const [parts, setParts] = useState<Part[]>([]);
  const [loading, setLoading] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');
  
  // Modal States
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [selectedPart, setSelectedPart] = useState<Part | null>(null);

  // --- API Actions ---

  // Fetch all parts or search by name
  const fetchParts = async (query = '') => {
    setLoading(true);
    try {
      const url = query 
        ? `http://localhost:8081/api/v1/inventory/search?name=${query}`
        : 'http://localhost:8081/api/v1/inventory';
      
      const res = await axios.get<ApiResponse<Part[]>>(url);
      setParts(res.data.data);
    } catch (err) {
      console.error("Error fetching parts:", err);
    } finally {
      setLoading(false);
    }
  };

  // Delete a part
  const handleDelete = async (id: number) => {
    if (!window.confirm("Are you sure you want to delete this part? This action cannot be undone.")) return;
    try {
      await axios.delete(`http://localhost:8081/api/v1/inventory/${id}`);
      fetchParts(searchTerm);
    } catch (err) {
      alert("Failed to delete part. It might be referenced in transactions.");
    }
  };

  // Quick Stock Consumption (OUT)
  const handleConsume = async (id: number) => {
    const qty = window.prompt("Enter quantity to consume (OUT):");
    if (!qty || isNaN(parseInt(qty)) || parseInt(qty) <= 0) return;

    try {
      await axios.post(`http://localhost:8081/api/v1/inventory/${id}/consume?qty=${qty}`);
      fetchParts(searchTerm);
    } catch (err: any) {
      alert(err.response?.data?.message || "Insufficient stock or error.");
    }
  };

  // Open Edit Modal
  const openEditModal = (part: Part) => {
    setSelectedPart(part);
    setIsEditModalOpen(true);
  };

  // --- Lifecycle & Effects ---

  useEffect(() => {
    // Debounce search input (waits 300ms after last keystroke)
    const delayDebounceFn = setTimeout(() => {
      fetchParts(searchTerm);
    }, 300);

    return () => clearTimeout(delayDebounceFn);
  }, [searchTerm]);

  return (
    <div className="p-6 bg-gray-50 min-h-screen">
      {/* Modals */}
      <AddPartModal 
        isOpen={isModalOpen} 
        onClose={() => setIsModalOpen(false)} 
        onSuccess={() => fetchParts(searchTerm)} 
      />
      
      <EditPartModal 
        isOpen={isEditModalOpen} 
        part={selectedPart} 
        onClose={() => setIsEditModalOpen(false)} 
        onSuccess={() => fetchParts(searchTerm)} 
      />

      <div className="max-w-6xl mx-auto space-y-6">
        
        {/* Top Action Bar */}
        <div className="flex flex-col md:flex-row justify-between items-center gap-4">
          <div className="relative w-full md:w-96">
            <Search className="absolute left-3 top-2.5 text-gray-400 w-5 h-5" />
            <input
              type="text"
              placeholder="Search by part name..."
              className="w-full pl-10 pr-4 py-2 border border-gray-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-blue-500 bg-white shadow-sm transition-all"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
          </div>
          
          <button
            onClick={() => setIsModalOpen(true)}
            className="w-full md:w-auto bg-blue-600 text-white px-6 py-2.5 rounded-xl hover:bg-blue-700 transition shadow-md flex items-center justify-center gap-2 font-semibold"
          >
            <PlusCircle size={20} />
            <span>Add New Part</span>
          </button>
        </div>

        {/* Inventory Table Card */}
        <div className="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">
          <div className="p-4 bg-white border-b border-gray-50">
            <h2 className="text-lg font-bold text-gray-800 flex items-center gap-2">
              <Package className="text-blue-500" size={20} /> Inventory Stock
            </h2>
          </div>

          <div className="overflow-x-auto">
            <table className="w-full text-left border-collapse">
              <thead>
                <tr className="bg-gray-50/50 text-gray-500 text-xs uppercase tracking-widest font-bold">
                  <th className="p-4">ID</th>
                  <th className="p-4">Part Details</th>
                  <th className="p-4">Serial Number</th>
                  <th className="p-4">Quantity</th>
                  <th className="p-4">Location</th>
                  <th className="p-4 text-center">Actions</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-50">
                {loading ? (
                  <tr>
                    <td colSpan={6} className="p-20 text-center">
                      <div className="flex flex-col items-center gap-2 text-gray-400">
                        <Loader2 className="animate-spin text-blue-500" size={32} />
                        <span className="text-sm font-medium">Syncing with warehouse...</span>
                      </div>
                    </td>
                  </tr>
                ) : parts.length > 0 ? (
                  parts.map(part => (
                    <tr key={part.id} className="hover:bg-blue-50/30 transition group">
                      <td className="p-4 text-gray-400 font-mono text-xs font-medium">#{part.id}</td>
                      <td className="p-4">
                        <div className="font-bold text-gray-900">{part.name}</div>
                      </td>
                      <td className="p-4 text-gray-600 font-medium">{part.serialNumber}</td>
                      <td className="p-4">
                        <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-bold border ${
                          part.quantity < 5 
                            ? 'bg-red-50 text-red-600 border-red-100' 
                            : 'bg-green-50 text-green-600 border-green-100'
                        }`}>
                          {part.quantity} units
                        </span>
                      </td>
                      <td className="p-4 text-gray-500 text-sm italic">{part.location || 'Unassigned'}</td>
                      <td className="p-4">
                        <div className="flex justify-center items-center gap-1">
                          
                          {/* History Link */}
                          <Link 
                            to={`/inventory/${part.id}/transactions`} 
                            className="p-2 text-gray-400 hover:text-purple-600 hover:bg-purple-50 rounded-lg transition"
                            title="Transaction History"
                          >
                            <Clock size={18} />
                          </Link>

                          {/* Quick Consume */}
                          <button 
                            onClick={() => handleConsume(part.id)}
                            className="p-2 text-gray-400 hover:text-orange-600 hover:bg-orange-50 rounded-lg transition"
                            title="Consume Stock"
                          >
                            <MinusCircle size={18} />
                          </button>

                          {/* Edit */}
                          <button 
                            onClick={() => openEditModal(part)}
                            className="p-2 text-gray-400 hover:text-blue-600 hover:bg-blue-50 rounded-lg transition"
                            title="Edit Details"
                          >
                            <Edit3 size={18} />
                          </button>
                          
                          {/* Delete */}
                          <button 
                            onClick={() => handleDelete(part.id)}
                            className="p-2 text-gray-400 hover:text-red-600 hover:bg-red-50 rounded-lg transition"
                            title="Remove Part"
                          >
                            <Trash2 size={18} />
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan={6} className="p-20 text-center text-gray-400">
                      <Package size={48} className="mx-auto mb-2 opacity-20" />
                      <p className="italic">No parts found matching "{searchTerm}"</p>
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
};

export default InventoryList;
