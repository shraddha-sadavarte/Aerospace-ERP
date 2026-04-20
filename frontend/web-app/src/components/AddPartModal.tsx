import React, { useState } from 'react';
import axios from 'axios';
import { X } from 'lucide-react';
import { ApiResponse, Part } from '../types/inventory';

interface Props {
  isOpen: boolean;
  onClose: () => void;
  onSuccess: () => void;
}

export const AddPartModal = ({ isOpen, onClose, onSuccess }: Props) => {
  const [formData, setFormData] = useState({
    name: '',
    serialNumber: '',
    batchNumber: '',
    quantity: 0,
    location: '',
    partType: '',
    criticality: 'MEDIUM',
    reorderLevel: 5
  });

  if (!isOpen) return null;

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await axios.post<ApiResponse<Part>>('http://localhost:8081/api/v1/inventory', formData);
      onSuccess(); // Refresh the list
      onClose();   // Close modal
    } catch (err) {
      console.error("Error creating part", err);
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
      <div className="bg-white rounded-xl shadow-lg w-full max-w-md overflow-hidden">
        <div className="flex justify-between items-center p-4 border-b">
          <h2 className="text-xl font-bold">Add New Part</h2>
          <button onClick={onClose} className="text-gray-500 hover:text-black"><X /></button>
        </div>

        <form onSubmit={handleSubmit} className="p-4 space-y-4">
          <input 
            type="text" placeholder="Part Name" required className="w-full p-2 border rounded"
            onChange={e => setFormData({...formData, name: e.target.value})}
          />
          <div className="grid grid-cols-2 gap-4">
            <input 
              type="text" placeholder="Serial #" required className="w-full p-2 border rounded"
              onChange={e => setFormData({...formData, serialNumber: e.target.value})}
            />
            <input 
              type="text" placeholder="Batch #" className="w-full p-2 border rounded"
              onChange={e => setFormData({...formData, batchNumber: e.target.value})}
            />
          </div>
          <div className="grid grid-cols-2 gap-4">
            <input 
              type="number" placeholder="Quantity" className="w-full p-2 border rounded"
              onChange={e => setFormData({...formData, quantity: parseInt(e.target.value)})}
            />
            <input 
              type="text" placeholder="Part Type (e.g. Engine)" className="w-full p-2 border rounded"
              onChange={e => setFormData({...formData, partType: e.target.value})}
            />
          </div>
          <input 
            type="text" placeholder="Location" className="w-full p-2 border rounded"
            onChange={e => setFormData({...formData, location: e.target.value})}
          />
          <button type="submit" className="w-full bg-blue-600 text-white py-2 rounded-lg font-bold hover:bg-blue-700">
            Create Part
          </button>
        </form>
      </div>
    </div>
  );
};
