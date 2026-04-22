import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { Layout } from './components/Layout';
import Dashboard from './pages/Dashboard';
import InventoryList from './pages/InventoryList';
import TransactionHistory from './pages/TransactionHistory';
import { Toaster } from 'react-hot-toast';

function App() {
  return (
    <Router>
      <Toaster 
        position="top-right" 
        reverseOrder={false} 
        toastOptions={{
          // Professional ERP styling for toasts
          duration: 4000,
          style: {
            background: '#1e293b', // Slate-800
            color: '#fff',
            borderRadius: '12px',
            fontSize: '14px',
            fontWeight: '600'
          },
        }}
      />
      <Layout>
        <Routes>
          <Route path="/" element={<Dashboard />} />
          <Route path="/inventory" element={<InventoryList />} />
          {/* Added the missing leading slash here */}
          <Route path="/inventory/:id/transactions" element={<TransactionHistory />} />
        </Routes>
      </Layout>
    </Router>
  );
}

export default App;
