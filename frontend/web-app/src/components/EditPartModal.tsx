import React, { useState, useEffect } from 'react'; 
import axios from 'axios'; 
import { X } from 'lucide-react'; 
import { Part } from '../types/inventory'; 

interface Props { 
  isOpen: boolean; 
  part: Part | null; 
  onClose: () => void; 
  onSuccess: () => void; 
} 

export const EditPartModal = ({ isOpen, part, onClose, onSuccess }: Props) => { 
  const [formData, setFormData] = useState({ 
    name: '', 
    quantity: 0, 
    location: '', 
  }); 

  useEffect(() => { 
    if (part) { 
      setFormData({ 
        name: part.name, 
        quantity: part.quantity, 
        location: part.location || '', 
      }); 
    } 
  }, [part]); 

  if (!isOpen || !part) return null; 

  const handleSubmit = async (e: React.FormEvent) => { 
    e.preventDefault(); 
    try { 
      // We spread the original 'part' first to include serialNumber, batchNumber, etc.
      // Then we overwrite with the modified 'formData'.
      const updatedPayload = {
        ...part,        // Includes original serialNumber, batchNumber, partType, etc.
        ...formData     // Overwrites name, quantity, and location with user changes
      };

      await axios.put(`http://localhost:8081/api/v1/inventory/${part.id}`, updatedPayload); 
      onSuccess(); 
      onClose(); 
    } catch (err: any) { 
      console.error("Error updating part", err);
      alert(err.response?.data?.message || "Failed to update part. Check backend logs.");
    } 
  }; 

  return ( 
    <div className="fixed inset-0 bg-black/50 flex items-center justify-center p-4 z-50 backdrop-blur-sm"> 
      <div className="bg-white rounded-xl shadow-xl w-full max-w-md"> 
        <div className="flex justify-between items-center p-4 border-b"> 
          <h2 className="text-xl font-bold text-gray-800">Edit {part.name}</h2> 
          <button onClick={onClose} className="text-gray-500 hover:text-black"><X /></button> 
        </div> 

        <form onSubmit={handleSubmit} className="p-6 space-y-4"> 
          <div> 
            <label className="block text-sm font-medium text-gray-700 mb-1">Part Name</label> 
            <input 
              type="text" 
              value={formData.name} 
              required 
              className="w-full p-2 border rounded-lg" 
              onChange={e => setFormData({...formData, name: e.target.value})} 
            /> 
          </div> 
          <div> 
            <label className="block text-sm font-medium text-gray-700 mb-1">Quantity</label> 
            <input 
              type="number" 
              value={formData.quantity} 
              className="w-full p-2 border rounded-lg" 
              // Safety check: prevents sending NaN if input is empty
              onChange={e => setFormData({...formData, quantity: e.target.value === '' ? 0 : parseInt(e.target.value)})} 
            /> 
          </div> 
          <div> 
            <label className="block text-sm font-medium text-gray-700 mb-1">Location</label> 
            <input 
              type="text" 
              value={formData.location} 
              className="w-full p-2 border rounded-lg" 
              onChange={e => setFormData({...formData, location: e.target.value})} 
            /> 
          </div> 
          <div className="flex gap-3 pt-2"> 
            <button type="button" onClick={onClose} className="flex-1 px-4 py-2 border rounded-lg hover:bg-gray-50">Cancel</button> 
            <button type="submit" className="flex-1 bg-blue-600 text-white py-2 rounded-lg font-bold hover:bg-blue-700 shadow-md"> 
              Save Changes 
            </button> 
          </div> 
        </form> 
      </div> 
    </div> 
  ); 
};
