import React, { useState, useEffect } from 'react';
import {
  Box,
  Paper,
  Typography,
  Button,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  IconButton,
  Chip,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Grid,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Alert,
  Fab,
  InputAdornment,
  TablePagination
} from '@mui/material';
import {
  Edit,
  Delete,
  Add,
  Search,
  Visibility,
  Business,
  LocationOn,
  AttachMoney
} from '@mui/icons-material';
import { BuildingDTO, Building } from '../../types';
import { buildingService } from '../../services/buildingService';

const BuildingManagement: React.FC = () => {
  const [buildings, setBuildings] = useState<BuildingDTO[]>([]);
  const [filteredBuildings, setFilteredBuildings] = useState<BuildingDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [searchTerm, setSearchTerm] = useState('');
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);

  // Dialog states
  const [openDialog, setOpenDialog] = useState(false);
  const [selectedBuilding, setSelectedBuilding] = useState<Building | null>(null);
  const [isEditing, setIsEditing] = useState(false);

  // Form data
  const [formData, setFormData] = useState<Partial<Building>>({
    name: '',
    street: '',
    ward: '',
    districtId: 1,
    structure: '',
    numberFloor: 1,
    floorArea: 0,
    direction: '',
    level: '',
    rentPrice: 0,
    rentPriceDescription: '',
    managerName: '',
    managerPhoneNumber: ''
  });

  useEffect(() => {
    loadBuildings();
  }, []);

  useEffect(() => {
    filterBuildings();
  }, [searchTerm, buildings]);

  const loadBuildings = async () => {
    try {
      setLoading(true);
      // Tạo dữ liệu mock vì API chưa hoàn chỉnh
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
        }
      ];
      setBuildings(mockBuildings);
    } catch (err: any) {
      setError('Không thể tải dữ liệu bất động sản');
    } finally {
      setLoading(false);
    }
  };

  const filterBuildings = () => {
    if (!searchTerm) {
      setFilteredBuildings(buildings);
    } else {
      const filtered = buildings.filter(building => 
        building.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
        building.districtName.toLowerCase().includes(searchTerm.toLowerCase()) ||
        building.ward.toLowerCase().includes(searchTerm.toLowerCase()) ||
        building.street.toLowerCase().includes(searchTerm.toLowerCase())
      );
      setFilteredBuildings(filtered);
    }
  };

  const handleOpenDialog = (building?: Building) => {
    if (building) {
      setSelectedBuilding(building);
      setFormData(building);
      setIsEditing(true);
    } else {
      setSelectedBuilding(null);
      setFormData({
        name: '',
        street: '',
        ward: '',
        districtId: 1,
        structure: '',
        numberFloor: 1,
        floorArea: 0,
        direction: '',
        level: '',
        rentPrice: 0,
        rentPriceDescription: '',
        managerName: '',
        managerPhoneNumber: ''
      });
      setIsEditing(false);
    }
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setSelectedBuilding(null);
    setFormData({});
  };

  const handleInputChange = (field: keyof Building, value: any) => {
    setFormData(prev => ({
      ...prev,
      [field]: value
    }));
  };

  const handleSave = async () => {
    try {
      if (isEditing && selectedBuilding) {
        // Update building
        console.log('Updating building:', formData);
      } else {
        // Create new building
        console.log('Creating building:', formData);
      }
      handleCloseDialog();
      loadBuildings();
    } catch (err: any) {
      setError('Không thể lưu thông tin bất động sản');
    }
  };

  const handleDelete = async (building: BuildingDTO) => {
    if (window.confirm(`Bạn có chắc chắn muốn xóa "${building.name}"?`)) {
      try {
        console.log('Deleting building:', building.name);
        loadBuildings();
      } catch (err: any) {
        setError('Không thể xóa bất động sản');
      }
    }
  };

  const formatPrice = (price: number) => {
    return new Intl.NumberFormat('vi-VN', {
      style: 'currency',
      currency: 'VND'
    }).format(price);
  };

  const handleChangePage = (event: unknown, newPage: number) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event: React.ChangeEvent<HTMLInputElement>) => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0);
  };

  const paginatedBuildings = filteredBuildings.slice(
    page * rowsPerPage,
    page * rowsPerPage + rowsPerPage
  );

  return (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
        <Typography variant="h4" gutterBottom>
          Quản lý Bất động sản
        </Typography>
        <Button
          variant="contained"
          startIcon={<Add />}
          onClick={() => handleOpenDialog()}
        >
          Thêm bất động sản
        </Button>
      </Box>

      {error && (
        <Alert severity="error" sx={{ mb: 2 }}>
          {error}
        </Alert>
      )}

      {/* Search */}
      <Paper sx={{ p: 2, mb: 3 }}>
        <TextField
          fullWidth
          placeholder="Tìm kiếm theo tên, quận, phường, đường..."
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
      </Paper>

      {/* Table */}
      <Paper>
        <TableContainer>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Tên bất động sản</TableCell>
                <TableCell>Địa chỉ</TableCell>
                <TableCell>Diện tích</TableCell>
                <TableCell>Số tầng</TableCell>
                <TableCell>Giá thuê</TableCell>
                <TableCell>Loại hình</TableCell>
                <TableCell>Quản lý</TableCell>
                <TableCell align="center">Hành động</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {paginatedBuildings.map((building, index) => (
                <TableRow key={index} hover>
                  <TableCell>
                    <Box sx={{ display: 'flex', alignItems: 'center' }}>
                      <Business sx={{ mr: 1, color: 'primary.main' }} />
                      <Typography variant="subtitle2">
                        {building.name}
                      </Typography>
                    </Box>
                  </TableCell>
                  <TableCell>
                    <Box sx={{ display: 'flex', alignItems: 'center' }}>
                      <LocationOn sx={{ mr: 1, color: 'text.secondary', fontSize: 16 }} />
                      <Box>
                        <Typography variant="body2">
                          {building.street}, {building.ward}
                        </Typography>
                        <Typography variant="caption" color="text.secondary">
                          {building.districtName}
                        </Typography>
                      </Box>
                    </Box>
                  </TableCell>
                  <TableCell>{building.floorArea} m²</TableCell>
                  <TableCell>{building.numberFloor} tầng</TableCell>
                  <TableCell>
                    <Box sx={{ display: 'flex', alignItems: 'center' }}>
                      <AttachMoney sx={{ mr: 1, color: 'success.main', fontSize: 16 }} />
                      <Typography variant="body2" color="success.main" fontWeight="bold">
                        {formatPrice(building.rentPrice)}
                      </Typography>
                    </Box>
                  </TableCell>
                  <TableCell>
                    <Chip 
                      label={building.buildingRentType} 
                      size="small" 
                      color="primary" 
                      variant="outlined"
                    />
                  </TableCell>
                  <TableCell>
                    <Typography variant="body2">{building.managerName}</Typography>
                    <Typography variant="caption" color="text.secondary">
                      {building.managerPhoneNumber}
                    </Typography>
                  </TableCell>
                  <TableCell align="center">
                    <IconButton size="small" color="primary">
                      <Visibility />
                    </IconButton>
                    <IconButton 
                      size="small" 
                      color="primary"
                      onClick={() => handleOpenDialog(building as Building)}
                    >
                      <Edit />
                    </IconButton>
                    <IconButton 
                      size="small" 
                      color="error"
                      onClick={() => handleDelete(building)}
                    >
                      <Delete />
                    </IconButton>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
        <TablePagination
          rowsPerPageOptions={[5, 10, 25]}
          component="div"
          count={filteredBuildings.length}
          rowsPerPage={rowsPerPage}
          page={page}
          onPageChange={handleChangePage}
          onRowsPerPageChange={handleChangeRowsPerPage}
          labelRowsPerPage="Số hàng mỗi trang:"
          labelDisplayedRows={({ from, to, count }) => `${from}-${to} của ${count}`}
        />
      </Paper>

      {/* Add/Edit Dialog */}
      <Dialog open={openDialog} onClose={handleCloseDialog} maxWidth="md" fullWidth>
        <DialogTitle>
          {isEditing ? 'Chỉnh sửa bất động sản' : 'Thêm bất động sản mới'}
        </DialogTitle>
        <DialogContent>
          <Grid container spacing={2} sx={{ mt: 1 }}>
            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                label="Tên bất động sản"
                value={formData.name || ''}
                onChange={(e) => handleInputChange('name', e.target.value)}
              />
            </Grid>
            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                label="Đường"
                value={formData.street || ''}
                onChange={(e) => handleInputChange('street', e.target.value)}
              />
            </Grid>
            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                label="Phường"
                value={formData.ward || ''}
                onChange={(e) => handleInputChange('ward', e.target.value)}
              />
            </Grid>
            <Grid item xs={12} md={6}>
              <FormControl fullWidth>
                <InputLabel>Quận</InputLabel>
                <Select
                  value={formData.districtId || 1}
                  onChange={(e) => handleInputChange('districtId', e.target.value)}
                  label="Quận"
                >
                  <MenuItem value={1}>Quận 1</MenuItem>
                  <MenuItem value={2}>Quận 2</MenuItem>
                  <MenuItem value={3}>Quận 3</MenuItem>
                  <MenuItem value={4}>Quận 4</MenuItem>
                  <MenuItem value={5}>Quận 5</MenuItem>
                </Select>
              </FormControl>
            </Grid>
            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                label="Diện tích (m²)"
                type="number"
                value={formData.floorArea || ''}
                onChange={(e) => handleInputChange('floorArea', parseFloat(e.target.value))}
              />
            </Grid>
            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                label="Số tầng"
                type="number"
                value={formData.numberFloor || ''}
                onChange={(e) => handleInputChange('numberFloor', parseInt(e.target.value))}
              />
            </Grid>
            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                label="Hướng"
                value={formData.direction || ''}
                onChange={(e) => handleInputChange('direction', e.target.value)}
              />
            </Grid>
            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                label="Hạng/Cấp độ"
                value={formData.level || ''}
                onChange={(e) => handleInputChange('level', e.target.value)}
              />
            </Grid>
            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                label="Giá thuê (VND)"
                type="number"
                value={formData.rentPrice || ''}
                onChange={(e) => handleInputChange('rentPrice', parseFloat(e.target.value))}
              />
            </Grid>
            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                label="Tên người quản lý"
                value={formData.managerName || ''}
                onChange={(e) => handleInputChange('managerName', e.target.value)}
              />
            </Grid>
            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                label="Số điện thoại quản lý"
                value={formData.managerPhoneNumber || ''}
                onChange={(e) => handleInputChange('managerPhoneNumber', e.target.value)}
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                fullWidth
                multiline
                rows={3}
                label="Mô tả giá thuê"
                value={formData.rentPriceDescription || ''}
                onChange={(e) => handleInputChange('rentPriceDescription', e.target.value)}
              />
            </Grid>
          </Grid>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog}>Hủy</Button>
          <Button onClick={handleSave} variant="contained">
            {isEditing ? 'Cập nhật' : 'Thêm mới'}
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default BuildingManagement;
