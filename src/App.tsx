import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import { AuthProvider } from './context/AuthContext';
import ProtectedRoute from './components/auth/ProtectedRoute';
import LoginForm from './components/auth/LoginForm';
import AdminLayout from './components/layout/AdminLayout';
import Dashboard from './components/admin/Dashboard';
import BuildingManagement from './components/admin/BuildingManagement';
import UserHomePage from './components/user/UserHomePage';

// Tạo theme cho ứng dụng
const theme = createTheme({
  palette: {
    primary: {
      main: '#1976d2',
    },
    secondary: {
      main: '#dc004e',
    },
    background: {
      default: '#f5f5f5',
    },
  },
  typography: {
    fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
    h4: {
      fontWeight: 600,
    },
    h6: {
      fontWeight: 600,
    },
  },
  components: {
    MuiButton: {
      styleOverrides: {
        root: {
          textTransform: 'none',
        },
      },
    },
    MuiCard: {
      styleOverrides: {
        root: {
          boxShadow: '0 2px 4px rgba(0,0,0,0.1)',
          '&:hover': {
            boxShadow: '0 4px 8px rgba(0,0,0,0.15)',
          },
        },
      },
    },
  },
});

// Component cho Users Management (tạm thời)
const UserManagement: React.FC = () => (
  <div>
    <h2>Quản lý Người dùng</h2>
    <p>Tính năng này sẽ được phát triển trong phiên bản tiếp theo.</p>
  </div>
);

function App() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <AuthProvider>
        <Router>
          <div className="App">
            <Routes>
              {/* Trang chủ công cộng */}
              <Route path="/" element={<UserHomePage />} />
              
              {/* Trang đăng nhập */}
              <Route path="/login" element={<LoginForm />} />
              
              {/* Admin routes - yêu cầu đăng nhập và role ADMIN */}
              <Route 
                path="/admin" 
                element={
                  <ProtectedRoute requiredRole="ADMIN">
                    <AdminLayout />
                  </ProtectedRoute>
                } 
              >
                {/* Nested routes cho admin */}
                <Route index element={<Dashboard />} />
                <Route path="buildings" element={<BuildingManagement />} />
                <Route path="users" element={<UserManagement />} />
              </Route>
              
              {/* Redirect any unknown routes */}
              <Route path="*" element={<Navigate to="/" replace />} />
            </Routes>
          </div>
        </Router>
      </AuthProvider>
    </ThemeProvider>
  );
}

export default App;
