import React from 'react';
import {
  Grid,
  Paper,
  Typography,
  Box,
  Card,
  CardContent,
  Avatar,
  List,
  ListItem,
  ListItemText,
  ListItemAvatar,
  Divider
} from '@mui/material';
import {
  Business,
  People,
  TrendingUp,
  AttachMoney,
  Home,
  Apartment
} from '@mui/icons-material';

interface StatCardProps {
  title: string;
  value: string | number;
  icon: React.ReactElement;
  color: string;
  change?: string;
}

const StatCard: React.FC<StatCardProps> = ({ title, value, icon, color, change }) => (
  <Card sx={{ height: '100%' }}>
    <CardContent>
      <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <Box>
          <Typography color="textSecondary" gutterBottom variant="overline">
            {title}
          </Typography>
          <Typography variant="h4" component="div">
            {value}
          </Typography>
          {change && (
            <Typography variant="body2" sx={{ color: 'success.main', mt: 1 }}>
              {change}
            </Typography>
          )}
        </Box>
        <Avatar sx={{ backgroundColor: color, width: 56, height: 56 }}>
          {icon}
        </Avatar>
      </Box>
    </CardContent>
  </Card>
);

const Dashboard: React.FC = () => {
  const stats = [
    {
      title: 'Tổng số bất động sản',
      value: 124,
      icon: <Business />,
      color: '#1976d2',
      change: '+12% so với tháng trước'
    },
    {
      title: 'Bất động sản đã thuê',
      value: 89,
      icon: <Home />,
      color: '#388e3c',
      change: '+8% so với tháng trước'
    },
    {
      title: 'Doanh thu tháng này',
      value: '2.5 tỷ VND',
      icon: <AttachMoney />,
      color: '#f57c00',
      change: '+15% so với tháng trước'
    },
    {
      title: 'Người dùng',
      value: 456,
      icon: <People />,
      color: '#7b1fa2',
      change: '+5% so với tháng trước'
    }
  ];

  const recentActivities = [
    { action: 'Bất động sản mới được thêm', detail: 'Chung cư Vinhomes Central Park', time: '2 giờ trước' },
    { action: 'Hợp đồng thuê mới', detail: 'Căn hộ tại Quận 1', time: '4 giờ trước' },
    { action: 'Người dùng mới đăng ký', detail: 'Nguyễn Văn A', time: '6 giờ trước' },
    { action: 'Cập nhật giá thuê', detail: 'Văn phòng tại Quận 3', time: '1 ngày trước' },
  ];

  return (
    <Box>
      <Typography variant="h4" gutterBottom>
        Dashboard
      </Typography>
      <Typography variant="body1" color="text.secondary" sx={{ mb: 4 }}>
        Tổng quan hệ thống quản lý bất động sản
      </Typography>

      <Grid container spacing={3}>
        {/* Thống kê */}
        {stats.map((stat, index) => (
          <Grid item xs={12} sm={6} md={3} key={index}>
            <StatCard {...stat} />
          </Grid>
        ))}

        {/* Biểu đồ hoạt động */}
        <Grid item xs={12} md={8}>
          <Paper sx={{ p: 3, height: '400px' }}>
            <Typography variant="h6" gutterBottom>
              Thống kê theo tháng
            </Typography>
            <Box sx={{ 
              height: '300px', 
              display: 'flex', 
              alignItems: 'center', 
              justifyContent: 'center',
              backgroundColor: 'grey.50',
              borderRadius: 1
            }}>
              <Typography variant="body1" color="text.secondary">
                Biểu đồ sẽ được thêm vào sau
              </Typography>
            </Box>
          </Paper>
        </Grid>

        {/* Hoạt động gần đây */}
        <Grid item xs={12} md={4}>
          <Paper sx={{ p: 3, height: '400px' }}>
            <Typography variant="h6" gutterBottom>
              Hoạt động gần đây
            </Typography>
            <List>
              {recentActivities.map((activity, index) => (
                <React.Fragment key={index}>
                  <ListItem alignItems="flex-start">
                    <ListItemAvatar>
                      <Avatar sx={{ backgroundColor: 'primary.main' }}>
                        <TrendingUp />
                      </Avatar>
                    </ListItemAvatar>
                    <ListItemText
                      primary={activity.action}
                      secondary={
                        <>
                          <Typography component="span" variant="body2" color="text.primary">
                            {activity.detail}
                          </Typography>
                          <br />
                          {activity.time}
                        </>
                      }
                    />
                  </ListItem>
                  {index < recentActivities.length - 1 && <Divider variant="inset" component="li" />}
                </React.Fragment>
              ))}
            </List>
          </Paper>
        </Grid>

        {/* Bất động sản nổi bật */}
        <Grid item xs={12}>
          <Paper sx={{ p: 3 }}>
            <Typography variant="h6" gutterBottom>
              Bất động sản được quan tâm nhiều nhất
            </Typography>
            <Grid container spacing={2}>
              {[1, 2, 3, 4].map((item) => (
                <Grid item xs={12} sm={6} md={3} key={item}>
                  <Card variant="outlined">
                    <CardContent>
                      <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                        <Avatar sx={{ mr: 2 }}>
                          <Apartment />
                        </Avatar>
                        <Box>
                          <Typography variant="subtitle1">
                            Chung cư cao cấp {item}
                          </Typography>
                          <Typography variant="body2" color="text.secondary">
                            Quận {item}, TP.HCM
                          </Typography>
                        </Box>
                      </Box>
                      <Typography variant="h6" color="primary">
                        {15 + item * 5} triệu/tháng
                      </Typography>
                      <Typography variant="body2" color="text.secondary">
                        {120 + item * 10} lượt xem
                      </Typography>
                    </CardContent>
                  </Card>
                </Grid>
              ))}
            </Grid>
          </Paper>
        </Grid>
      </Grid>
    </Box>
  );
};

export default Dashboard;
