import React from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { Box, CircularProgress, Typography } from '@mui/material';

interface ProtectedRouteProps {
  children: React.ReactNode;
  requiredRole?: 'ADMIN' | 'USER';
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ 
  children, 
  requiredRole 
}) => {
  const { user, isLoading } = useAuth();
  const location = useLocation();

  // Hiển thị loading khi đang kiểm tra authentication
  if (isLoading) {
    return (
      <Box
        sx={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          justifyContent: 'center',
          height: '100vh',
          gap: 2
        }}
      >
        <CircularProgress />
        <Typography variant="body1" color="text.secondary">
          Đang kiểm tra quyền truy cập...
        </Typography>
      </Box>
    );
  }

  // Nếu chưa đăng nhập, chuyển hướng đến trang login
  if (!user) {
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  // Nếu có yêu cầu role và user không có role phù hợp
  if (requiredRole && user.role !== requiredRole) {
    // Admin có thể truy cập tất cả, USER chỉ truy cập được USER routes
    if (requiredRole === 'ADMIN' && user.role !== 'ADMIN') {
      return <Navigate to="/" replace />;
    }
  }

  return <>{children}</>;
};

export default ProtectedRoute;
