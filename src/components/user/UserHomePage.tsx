import React, { useState, useEffect } from 'react';
import {
  Box,
  Container,
  Typography,
  Paper,
  TextField,
  Button,
  Grid,
  Card,
  CardContent,
  CardMedia,
  CardActions,
  Chip,
  InputAdornment,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  AppBar,
  Toolbar,
  IconButton,
  Avatar,
  Menu,
  Divider,
  ListItemIcon,
  ListItemText
} from '@mui/material';
import {
  Search,
  LocationOn,
  AttachMoney,
  Business,
  Home,
  AccountCircle,
  Login,
  Logout,
  FilterList,
  Phone,
  Person
} from '@mui/icons-material';
import { BuildingDTO } from '../../types';
import { useAuth } from '../../context/AuthContext';
import { useNavigate } from 'react-router-dom';

const UserHomePage: React.FC = () => {
  const [buildings, setBuildings] = useState<BuildingDTO[]>([]);
  const [filteredBuildings, setFilteredBuildings] = useState<BuildingDTO[]>([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedDistrict, setSelectedDistrict] = useState('');
  const [priceRange, setPriceRange] = useState('');
  const [buildingType, setBuildingType] = useState('');
  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
  
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    loadBuildings();
  }, []);

  useEffect(() => {
    filterBuildings();
  }, [searchTerm, selectedDistrict, priceRange, buildingType, buildings]);

  const loadBuildings = () => {
    // Mock data
    const mockBuildings: BuildingDTO[] = [
      {
        name: 'Chung cư Vinhomes Central Park',
        floorArea: 120,
        districtName: 'Quận Bình Thạnh',
        ward: 'Phường 22',
        street: 'Đường Nguyễn Hữu Cảnh',
        numberFloor: 45,
        direction: 'Đông Nam',
        level: 'Cao cấp',
        rentAria: 120,
        rentPrice: 25000000,
        managerName: 'Nguyễn Văn A',
        managerPhoneNumber: '0901234567',
        buildingRentType: 'Chung cư'
      },
      {
        name: 'Văn phòng Bitexco Financial Tower',
        floorArea: 200,
        districtName: 'Quận 1',
        ward: 'Phường Bến Nghé',
        street: 'Đường Hải Triều',
        numberFloor: 68,
        direction: 'Nam',
        level: 'Hạng A',
        rentAria: 200,
        rentPrice: 50000000,
        managerName: 'Trần Thị B',
        managerPhoneNumber: '0907654321',
        buildingRentType: 'Văn phòng'
      },
      {
        name: 'Nhà phố Thảo Điền',
        floorArea: 300,
        districtName: 'Quận 2',
        ward: 'Phường Thảo Điền',
        street: 'Đường Nguyễn Văn Hưởng',
        numberFloor: 4,
        direction: 'Đông',
        level: 'Cao cấp',
        rentAria: 300,
        rentPrice: 40000000,
        managerName: 'Lê Văn C',
        managerPhoneNumber: '0912345678',
        buildingRentType: 'Nhà phố'
      },
      {
        name: 'Căn hộ dịch vụ Quận 7',
        floorArea: 80,
        districtName: 'Quận 7',
        ward: 'Phường Tân Thuận Đông',
        street: 'Đường Nguyễn Thị Thập',
        numberFloor: 20,
        direction: 'Tây Nam',
        level: 'Trung cấp',
        rentAria: 80,
        rentPrice: 15000000,
        managerName: 'Phạm Văn D',
        managerPhoneNumber: '0908765432',
        buildingRentType: 'Căn hộ dịch vụ'
      },
      {
        name: 'Mặt bằng kinh doanh Quận 3',
        floorArea: 150,
        districtName: 'Quận 3',
        ward: 'Phường Võ Thị Sáu',
        street: 'Đường Cách Mạng Tháng 8',
        numberFloor: 2,
        direction: 'Bắc',
        level: 'Thương mại',
        rentAria: 150,
        rentPrice: 35000000,
        managerName: 'Hoàng Thị E',
        managerPhoneNumber: '0913456789',
        buildingRentType: 'Mặt bằng'
      },
      {
        name: 'Studio Quận 10',
        floorArea: 45,
        districtName: 'Quận 10',
        ward: 'Phường 12',
        street: 'Đường Sư Vạn Hạnh',
        numberFloor: 15,
        direction: 'Đông Bắc',
        level: 'Bình dân',
        rentAria: 45,
        rentPrice: 8000000,
        managerName: 'Ngô Văn F',
        managerPhoneNumber: '0914567890',
        buildingRentType: 'Studio'
      }
    ];
    setBuildings(mockBuildings);
  };

  const filterBuildings = () => {
    let filtered = buildings;

    if (searchTerm) {
      filtered = filtered.filter(building => 
        building.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
        building.districtName.toLowerCase().includes(searchTerm.toLowerCase()) ||
        building.ward.toLowerCase().includes(searchTerm.toLowerCase()) ||
        building.street.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }

    if (selectedDistrict) {
      filtered = filtered.filter(building => building.districtName === selectedDistrict);
    }

    if (priceRange) {
      filtered = filtered.filter(building => {
        const price = building.rentPrice;
        switch (priceRange) {
          case 'under-10':
            return price < 10000000;
          case '10-20':
            return price >= 10000000 && price < 20000000;
          case '20-30':
            return price >= 20000000 && price < 30000000;
          case '30-50':
            return price >= 30000000 && price < 50000000;
          case 'over-50':
            return price >= 50000000;
          default:
            return true;
        }
      });
    }

    if (buildingType) {
      filtered = filtered.filter(building => building.buildingRentType === buildingType);
    }

    setFilteredBuildings(filtered);
  };

  const formatPrice = (price: number) => {
    return new Intl.NumberFormat('vi-VN', {
      style: 'currency',
      currency: 'VND'
    }).format(price);
  };

  const handleMenuClick = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  const handleLogin = () => {
    navigate('/login');
    handleMenuClose();
  };

  const handleLogout = () => {
    logout();
    handleMenuClose();
  };

  const handleAdminPanel = () => {
    navigate('/admin');
    handleMenuClose();
  };

  const districts = ['Quận 1', 'Quận 2', 'Quận 3', 'Quận 4', 'Quận 5', 'Quận 7', 'Quận 10', 'Quận Bình Thạnh'];
  const buildingTypes = ['Chung cư', 'Văn phòng', 'Nhà phố', 'Căn hộ dịch vụ', 'Mặt bằng', 'Studio'];

  return (
    <Box>
      {/* Header */}
      <AppBar position="static" color="primary">
        <Toolbar>
          <Business sx={{ mr: 2 }} />
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            Hệ thống Bất động sản
          </Typography>
          
          <IconButton
            size="large"
            aria-label="account menu"
            aria-controls="account-menu"
            aria-haspopup="true"
            onClick={handleMenuClick}
            color="inherit"
          >
            {user ? (
              <Avatar sx={{ width: 32, height: 32 }}>
                {user.firstName?.charAt(0) || 'U'}
              </Avatar>
            ) : (
              <AccountCircle />
            )}
          </IconButton>
          
          <Menu
            id="account-menu"
            anchorEl={anchorEl}
            open={Boolean(anchorEl)}
            onClose={handleMenuClose}
            onClick={handleMenuClose}
          >
            {user ? (
              [
                <MenuItem key="profile" onClick={handleMenuClose}>
                  <ListItemIcon>
                    <Person fontSize="small" />
                  </ListItemIcon>
                  <ListItemText>{user.username}</ListItemText>
                </MenuItem>,
                user.role === 'ADMIN' && (
                  <MenuItem key="admin" onClick={handleAdminPanel}>
                    <ListItemIcon>
                      <Business fontSize="small" />
                    </ListItemIcon>
                    <ListItemText>Quản trị</ListItemText>
                  </MenuItem>
                ),
                <Divider key="divider" />,
                <MenuItem key="logout" onClick={handleLogout}>
                  <ListItemIcon>
                    <Logout fontSize="small" />
                  </ListItemIcon>
                  <ListItemText>Đăng xuất</ListItemText>
                </MenuItem>
              ]
            ) : (
              <MenuItem onClick={handleLogin}>
                <ListItemIcon>
                  <Login fontSize="small" />
                </ListItemIcon>
                <ListItemText>Đăng nhập</ListItemText>
              </MenuItem>
            )}
          </Menu>
        </Toolbar>
      </AppBar>

      <Container maxWidth="lg" sx={{ py: 4 }}>
        {/* Hero Section */}
        <Box sx={{ textAlign: 'center', mb: 6 }}>
          <Typography variant="h3" component="h1" gutterBottom>
            Tìm kiếm Bất động sản
          </Typography>
          <Typography variant="h6" color="text.secondary" sx={{ mb: 4 }}>
            Khám phá các bất động sản cho thuê tốt nhất tại TP.HCM
          </Typography>
        </Box>

        {/* Search & Filter */}
        <Paper sx={{ p: 3, mb: 4 }}>
          <Grid container spacing={2}>
            <Grid item xs={12} md={4}>
              <TextField
                fullWidth
                placeholder="Tìm kiếm theo tên, địa chỉ..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                InputProps={{
                  startAdornment: (
                    <InputAdornment position="start">
                      <Search />
                    </InputAdornment>
                  ),
                }}
              />
            </Grid>
            <Grid item xs={12} md={2}>
              <FormControl fullWidth>
                <InputLabel>Quận</InputLabel>
                <Select
                  value={selectedDistrict}
                  onChange={(e) => setSelectedDistrict(e.target.value)}
                  label="Quận"
                >
                  <MenuItem value="">Tất cả</MenuItem>
                  {districts.map((district) => (
                    <MenuItem key={district} value={district}>
                      {district}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Grid>
            <Grid item xs={12} md={2}>
              <FormControl fullWidth>
                <InputLabel>Giá thuê</InputLabel>
                <Select
                  value={priceRange}
                  onChange={(e) => setPriceRange(e.target.value)}
                  label="Giá thuê"
                >
                  <MenuItem value="">Tất cả</MenuItem>
                  <MenuItem value="under-10">Dưới 10 triệu</MenuItem>
                  <MenuItem value="10-20">10-20 triệu</MenuItem>
                  <MenuItem value="20-30">20-30 triệu</MenuItem>
                  <MenuItem value="30-50">30-50 triệu</MenuItem>
                  <MenuItem value="over-50">Trên 50 triệu</MenuItem>
                </Select>
              </FormControl>
            </Grid>
            <Grid item xs={12} md={2}>
              <FormControl fullWidth>
                <InputLabel>Loại hình</InputLabel>
                <Select
                  value={buildingType}
                  onChange={(e) => setBuildingType(e.target.value)}
                  label="Loại hình"
                >
                  <MenuItem value="">Tất cả</MenuItem>
                  {buildingTypes.map((type) => (
                    <MenuItem key={type} value={type}>
                      {type}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Grid>
            <Grid item xs={12} md={2}>
              <Button
                fullWidth
                variant="outlined"
                startIcon={<FilterList />}
                sx={{ height: '56px' }}
                onClick={() => {
                  setSearchTerm('');
                  setSelectedDistrict('');
                  setPriceRange('');
                  setBuildingType('');
                }}
              >
                Xóa bộ lọc
              </Button>
            </Grid>
          </Grid>
        </Paper>

        {/* Results */}
        <Typography variant="h5" gutterBottom>
          Kết quả tìm kiếm ({filteredBuildings.length} bất động sản)
        </Typography>

        <Grid container spacing={3}>
          {filteredBuildings.map((building, index) => (
            <Grid item xs={12} sm={6} md={4} key={index}>
              <Card sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
                <CardMedia
                  component="div"
                  sx={{
                    height: 200,
                    background: 'linear-gradient(45deg, #FE6B8B 30%, #FF8E53 90%)',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center'
                  }}
                >
                  <Home sx={{ fontSize: 60, color: 'white' }} />
                </CardMedia>
                <CardContent sx={{ flexGrow: 1 }}>
                  <Typography gutterBottom variant="h6" component="div">
                    {building.name}
                  </Typography>
                  
                  <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
                    <LocationOn sx={{ mr: 1, color: 'text.secondary', fontSize: 16 }} />
                    <Typography variant="body2" color="text.secondary">
                      {building.street}, {building.ward}, {building.districtName}
                    </Typography>
                  </Box>

                  <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
                    <Business sx={{ mr: 1, color: 'text.secondary', fontSize: 16 }} />
                    <Typography variant="body2" color="text.secondary">
                      {building.floorArea} m² • {building.numberFloor} tầng • {building.direction}
                    </Typography>
                  </Box>

                  <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                    <AttachMoney sx={{ mr: 1, color: 'success.main', fontSize: 16 }} />
                    <Typography variant="h6" color="success.main" fontWeight="bold">
                      {formatPrice(building.rentPrice)}
                    </Typography>
                  </Box>

                  <Box sx={{ mb: 2 }}>
                    <Chip 
                      label={building.buildingRentType} 
                      size="small" 
                      color="primary" 
                      variant="outlined"
                      sx={{ mr: 1 }}
                    />
                    <Chip 
                      label={building.level} 
                      size="small" 
                      color="secondary" 
                      variant="outlined"
                    />
                  </Box>

                  <Box sx={{ display: 'flex', alignItems: 'center' }}>
                    <Phone sx={{ mr: 1, color: 'text.secondary', fontSize: 16 }} />
                    <Box>
                      <Typography variant="body2" fontWeight="bold">
                        {building.managerName}
                      </Typography>
                      <Typography variant="caption" color="text.secondary">
                        {building.managerPhoneNumber}
                      </Typography>
                    </Box>
                  </Box>
                </CardContent>
                <CardActions>
                  <Button size="small" color="primary">
                    Xem chi tiết
                  </Button>
                  <Button size="small" color="primary">
                    Liên hệ
                  </Button>
                </CardActions>
              </Card>
            </Grid>
          ))}
        </Grid>

        {filteredBuildings.length === 0 && (
          <Box sx={{ textAlign: 'center', py: 8 }}>
            <Business sx={{ fontSize: 80, color: 'text.secondary', mb: 2 }} />
            <Typography variant="h6" color="text.secondary">
              Không tìm thấy bất động sản phù hợp
            </Typography>
            <Typography variant="body2" color="text.secondary">
              Hãy thử thay đổi điều kiện tìm kiếm
            </Typography>
          </Box>
        )}
      </Container>
    </Box>
  );
};

export default UserHomePage;
