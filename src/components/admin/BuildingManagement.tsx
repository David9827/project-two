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
  InputAdornment,
  TablePagination,
  CircularProgress,
  Snackbar
} from '@mui/material';
import {
  Edit,
  Delete,
  Add,
  Search,
  Visibility,
  Business,
  LocationOn,
  AttachMoney,
  Refresh
} from '@mui/icons-material';

// API Configuration
const API_BASE_URL = 'http://localhost:8080/api';

// Types - cập nhật theo API response
interface BuildingDTO {
  id?: number;
  name: string;
  street?: string;
  ward?: string;
  district?: string;
  districtName?: string;
  structure?: string;
  numberOfBasement?: number;
  floorArea?: number;
  direction?: string;
  level?: string;
  rentPrice?: number;
  rentPriceDescription?: string;
  serviceFee?: string;
  carFee?: string;
  motoFee?: string;
  overtimeFee?: string;
  waterFee?: string;
  electricityFee?: string;
  deposit?: string;
  payment?: string;
  rentTime?: string;
  decorationTime?: string;
  brokerageFee?: number;
  note?: string;
  linkOfBuilding?: string;
  map?: string;
  avatar?: string;
  managerName?: string;
  managerPhone?: string;
  createdDate?: string;
  modifiedDate?: string;
  createdBy?: string;
  modifiedBy?: string;
}

interface BuildingSearchCriteria {
  name?: string;
  street?: string;
  ward?: string;
  district?: string;
  floorArea?: number;
  numberOfBasement?: number;
  rentPrice?: number;
  managerName?: string;
  managerPhone?: string;
}

// API Service
const buildingAPI = {
  // Get all buildings
  getAll: async (): Promise<{ success: boolean; data: BuildingDTO[]; count: number; message?: string }> => {
    try {
      console.log('🔄 API Call: GET /api/building');
      const response = await fetch(`${API_BASE_URL}/building`);

      if (!response.ok) {
        throw new Error(`HTTP ${response.status}: ${response.statusText}`);
      }

      const result = await response.json();
      console.log('✅ API Response:', result);
      return result;
    } catch (error) {
      console.error('❌ API Error:', error);
      throw error;
    }
  },

  // Search buildings
  search: async (criteria: BuildingSearchCriteria): Promise<{ success: boolean; data: BuildingDTO[]; count: number; message?: string }> => {
    try {
      console.log('🔍 API Call: POST /api/building/search', criteria);
      const response = await fetch(`${API_BASE_URL}/building/search`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(criteria)
      });

      if (!response.ok) {
        throw new Error(`HTTP ${response.status}: ${response.statusText}`);
      }

      const result = await response.json();
      console.log('🔍 Search Response:', result);
      return result;
    } catch (error) {
      console.error('❌ Search Error:', error);
      throw error;
    }
  },

  // Get building by ID
  getById: async (id: number): Promise<{ success: boolean; data: BuildingDTO; message?: string }> => {
    try {
      console.log(`🔍 API Call: GET /api/building/${id}`);
      const response = await fetch(`${API_BASE_URL}/building/${id}`);

      if (!response.ok) {
        throw new Error(`HTTP ${response.status}: ${response.statusText}`);
      }

      const result = await response.json();
      console.log('📦 Building Details:', result);
      return result;
    } catch (error) {
      console.error('❌ Get Building Error:', error);
      throw error;
    }
  },

  // Create building
  create: async (building: Omit<BuildingDTO, 'id'>): Promise<{ success: boolean; data: BuildingDTO; message?: string }> => {
    try {
      console.log('➕ API Call: POST /api/building', building);
      const response = await fetch(`${API_BASE_URL}/building`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(building)
      });

      if (!response.ok) {
        throw new Error(`HTTP ${response.status}: ${response.statusText}`);
      }

      const result = await response.json();
      console.log('✅ Created Building:', result);
      return result;
    } catch (error) {
      console.error('❌ Create Error:', error);
      throw error;
    }
  },

  // Update building
  update: async (id: number, building: BuildingDTO): Promise<{ success: boolean; data: BuildingDTO; message?: string }> => {
    try {
      console.log(`✏️ API Call: PUT /api/building/${id}`, building);
      const response = await fetch(`${API_BASE_URL}/building/${id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ ...building, id })
      });

      if (!response.ok) {
        throw new Error(`HTTP ${response.status}: ${response.statusText}`);
      }

      const result = await response.json();
      console.log('✅ Updated Building:', result);
      return result;
    } catch (error) {
      console.error('❌ Update Error:', error);
      throw error;
    }
  },

  // Delete building
  delete: async (id: number): Promise<{ success: boolean; message?: string }> => {
    try {
      console.log(`🗑️ API Call: DELETE /api/building/${id}`);
      const response = await fetch(`${API_BASE_URL}/building/${id}`, {
        method: 'DELETE',
      });

      if (!response.ok) {
        throw new Error(`HTTP ${response.status}: ${response.statusText}`);
      }

      const result = await response.json();
      console.log('🗑️ Deleted Building:', result);
      return result;
    } catch (error) {
      console.error('❌ Delete Error:', error);
      throw error;
    }
  },

  // Health check
  healthCheck: async (): Promise<any> => {
    try {
      console.log('🏥 API Call: GET /api/health');
      const response = await fetch(`${API_BASE_URL}/health`);
      const result = await response.json();
      console.log('🏥 Health Status:', result);
      return result;
    } catch (error) {
      console.error('❌ Health Check Error:', error);
      throw error;
    }
  }
};

const BuildingManagement: React.FC = () => {
  const [buildings, setBuildings] = useState<BuildingDTO[]>([]);
  const [filteredBuildings, setFilteredBuildings] = useState<BuildingDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [searchTerm, setSearchTerm] = useState('');
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);

  // Dialog states
  const [openDialog, setOpenDialog] = useState(false);
  const [selectedBuilding, setSelectedBuilding] = useState<BuildingDTO | null>(null);
  const [isEditing, setIsEditing] = useState(false);
  const [dialogLoading, setDialogLoading] = useState(false);

  // Form data
  const [formData, setFormData] = useState<Partial<BuildingDTO>>({
    name: '',
    street: '',
    ward: '',
    district: '',
    structure: '',
    numberOfBasement: 0,
    floorArea: 0,
    direction: '',
    level: '',
    rentPrice: 0,
    rentPriceDescription: '',
    managerName: '',
    managerPhone: '',
    note: ''
  });

  useEffect(() => {
    loadBuildings();
    testConnection();
  }, []);

  useEffect(() => {
    filterBuildings();
  }, [searchTerm, buildings]);

  // Test backend connection
  const testConnection = async () => {
    try {
      const health = await buildingAPI.healthCheck();
      console.log('✅ Backend connection successful:', health);
    } catch (error) {
      console.error('❌ Backend connection failed:', error);
      setError('Không thể kết nối tới server. Vui lòng kiểm tra Backend có đang chạy không.');
    }
  };

  const loadBuildings = async () => {
    try {
      setLoading(true);
      setError('');

      const result = await buildingAPI.getAll();

      if (result.success) {
        setBuildings(result.data || []);
        console.log(`✅ Loaded ${result.count} buildings from database`);
      } else {
        setError(result.message || 'Không thể tải dữ liệu bất động sản');
      }
    } catch (err: any) {
      console.error('❌ Load buildings error:', err);
      setError(`Lỗi kết nối: ${err.message}. Kiểm tra Backend có chạy trên http://localhost:8080 không?`);
    } finally {
      setLoading(false);
    }
  };

  const filterBuildings = () => {
    if (!searchTerm) {
      setFilteredBuildings(buildings);
    } else {
      const filtered = buildings.filter(building =>
          (building.name?.toLowerCase().includes(searchTerm.toLowerCase())) ||
          (building.districtName?.toLowerCase().includes(searchTerm.toLowerCase())) ||
          (building.district?.toLowerCase().includes(searchTerm.toLowerCase())) ||
          (building.ward?.toLowerCase().includes(searchTerm.toLowerCase())) ||
          (building.street?.toLowerCase().includes(searchTerm.toLowerCase())) ||
          (building.managerName?.toLowerCase().includes(searchTerm.toLowerCase()))
      );
      setFilteredBuildings(filtered);
    }
  };

  const handleOpenDialog = async (building?: BuildingDTO) => {
    if (building && building.id) {
      try {
        setDialogLoading(true);
        // Get full building details
        const result = await buildingAPI.getById(building.id);

        if (result.success) {
          setSelectedBuilding(result.data);
          setFormData(result.data);
          setIsEditing(true);
        } else {
          setError(result.message || 'Không thể tải thông tin bất động sản');
          return;
        }
      } catch (err: any) {
        setError(`Lỗi khi tải thông tin: ${err.message}`);
        return;
      } finally {
        setDialogLoading(false);
      }
    } else {
      setSelectedBuilding(null);
      setFormData({
        name: '',
        street: '',
        ward: '',
        district: '',
        structure: '',
        numberOfBasement: 0,
        floorArea: 0,
        direction: '',
        level: '',
        rentPrice: 0,
        rentPriceDescription: '',
        managerName: '',
        managerPhone: '',
        note: ''
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

  const handleInputChange = (field: keyof BuildingDTO, value: any) => {
    setFormData(prev => ({
      ...prev,
      [field]: value
    }));
  };

  const handleSave = async () => {
    try {
      setDialogLoading(true);
      setError('');

      // Validate required fields
      if (!formData.name?.trim()) {
        setError('Tên bất động sản là bắt buộc');
        return;
      }

      if (isEditing && selectedBuilding?.id) {
        // Update building
        const result = await buildingAPI.update(selectedBuilding.id, formData as BuildingDTO);

        if (result.success) {
          setSuccess('Cập nhật bất động sản thành công!');
          handleCloseDialog();
          loadBuildings();
        } else {
          setError(result.message || 'Không thể cập nhật bất động sản');
        }
      } else {
        // Create new building
        const result = await buildingAPI.create(formData as Omit<BuildingDTO, 'id'>);

        if (result.success) {
          setSuccess('Thêm bất động sản thành công!');
          handleCloseDialog();
          loadBuildings();
        } else {
          setError(result.message || 'Không thể thêm bất động sản');
        }
      }
    } catch (err: any) {
      setError(`Lỗi khi lưu: ${err.message}`);
    } finally {
      setDialogLoading(false);
    }
  };

  const handleDelete = async (building: BuildingDTO) => {
    if (!building.id) {
      setError('Không thể xóa: Thiếu ID bất động sản');
      return;
    }

    if (window.confirm(`Bạn có chắc chắn muốn xóa "${building.name}"?`)) {
      try {
        const result = await buildingAPI.delete(building.id);

        if (result.success) {
          setSuccess('Xóa bất động sản thành công!');
          loadBuildings();
        } else {
          setError(result.message || 'Không thể xóa bất động sản');
        }
      } catch (err: any) {
        setError(`Lỗi khi xóa: ${err.message}`);
      }
    }
  };

  const formatPrice = (price?: number) => {
    if (!price) return 'Liên hệ';
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
            {buildings.length > 0 && (
                <Typography variant="subtitle1" color="text.secondary">
                  ({buildings.length} bất động sản)
                </Typography>
            )}
          </Typography>
          <Box>
            <Button
                variant="outlined"
                startIcon={<Refresh />}
                onClick={loadBuildings}
                sx={{ mr: 1 }}
                disabled={loading}
            >
              Làm mới
            </Button>
            <Button
                variant="contained"
                startIcon={<Add />}
                onClick={() => handleOpenDialog()}
                disabled={loading}
            >
              Thêm bất động sản
            </Button>
          </Box>
        </Box>

        {/* Connection Status */}
        <Paper sx={{ p: 2, mb: 2, bgcolor: 'info.50' }}>
          <Typography variant="body2" color="info.main">
            🔗 Kết nối với Backend: {API_BASE_URL}
          </Typography>
        </Paper>

        {error && (
            <Alert severity="error" sx={{ mb: 2 }}>
              {error}
            </Alert>
        )}

        {/* Search */}
        <Paper sx={{ p: 2, mb: 3 }}>
          <TextField
              fullWidth
              placeholder="Tìm kiếm theo tên, quận, phường, đường, người quản lý..."
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

        {/* Loading */}
        {loading && (
            <Box sx={{ display: 'flex', justifyContent: 'center', py: 4 }}>
              <CircularProgress />
              <Typography sx={{ ml: 2 }}>Đang tải dữ liệu từ database...</Typography>
            </Box>
        )}

        {/* Empty State */}
        {!loading && buildings.length === 0 && !error && (
            <Paper sx={{ p: 4, textAlign: 'center' }}>
              <Business sx={{ fontSize: 64, color: 'text.disabled', mb: 2 }} />
              <Typography variant="h6" color="text.secondary" gutterBottom>
                Chưa có dữ liệu bất động sản
              </Typography>
              <Typography color="text.secondary" sx={{ mb: 2 }}>
                Thêm bất động sản đầu tiên hoặc kiểm tra kết nối database
              </Typography>
              <Button variant="outlined" onClick={testConnection}>
                Kiểm tra kết nối
              </Button>
            </Paper>
        )}

        {/* Table */}
        {!loading && buildings.length > 0 && (
            <Paper>
              <TableContainer>
                <Table>
                  <TableHead>
                    <TableRow>
                      <TableCell>ID</TableCell>
                      <TableCell>Tên bất động sản</TableCell>
                      <TableCell>Địa chỉ</TableCell>
                      <TableCell>Diện tích</TableCell>
                      <TableCell>Tầng hầm</TableCell>
                      <TableCell>Giá thuê</TableCell>
                      <TableCell>Quản lý</TableCell>
                      <TableCell align="center">Hành động</TableCell>
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    {paginatedBuildings.map((building, index) => (
                        <TableRow key={building.id || index} hover>
                          <TableCell>
                            <Chip label={building.id || 'N/A'} size="small" variant="outlined" />
                          </TableCell>
                          <TableCell>
                            <Box sx={{ display: 'flex', alignItems: 'center' }}>
                              <Business sx={{ mr: 1, color: 'primary.main' }} />
                              <Typography variant="subtitle2">
                                {building.name || 'Chưa có tên'}
                              </Typography>
                            </Box>
                          </TableCell>
                          <TableCell>
                            <Box sx={{ display: 'flex', alignItems: 'center' }}>
                              <LocationOn sx={{ mr: 1, color: 'text.secondary', fontSize: 16 }} />
                              <Box>
                                <Typography variant="body2">
                                  {[building.street, building.ward].filter(Boolean).join(', ') || 'Chưa có địa chỉ'}
                                </Typography>
                                <Typography variant="caption" color="text.secondary">
                                  {building.districtName || building.district || 'Chưa có quận'}
                                </Typography>
                              </Box>
                            </Box>
                          </TableCell>
                          <TableCell>{building.floorArea ? `${building.floorArea} m²` : 'N/A'}</TableCell>
                          <TableCell>{building.numberOfBasement !== undefined ? `${building.numberOfBasement} tầng` : 'N/A'}</TableCell>
                          <TableCell>
                            <Box sx={{ display: 'flex', alignItems: 'center' }}>
                              <AttachMoney sx={{ mr: 1, color: 'success.main', fontSize: 16 }} />
                              <Typography variant="body2" color="success.main" fontWeight="bold">
                                {formatPrice(building.rentPrice)}
                              </Typography>
                            </Box>
                          </TableCell>
                          <TableCell>
                            {building.managerName && (
                                <Box>
                                  <Typography variant="body2">{building.managerName}</Typography>
                                  {building.managerPhone && (
                                      <Typography variant="caption" color="text.secondary">
                                        {building.managerPhone}
                                      </Typography>
                                  )}
                                </Box>
                            )}
                          </TableCell>
                          <TableCell align="center">
                            <IconButton
                                size="small"
                                color="primary"
                                onClick={() => handleOpenDialog(building)}
                                title="Xem chi tiết"
                            >
                              <Visibility />
                            </IconButton>
                            <IconButton
                                size="small"
                                color="primary"
                                onClick={() => handleOpenDialog(building)}
                                title="Chỉnh sửa"
                            >
                              <Edit />
                            </IconButton>
                            <IconButton
                                size="small"
                                color="error"
                                onClick={() => handleDelete(building)}
                                title="Xóa"
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
        )}

        {/* Add/Edit Dialog */}
        <Dialog open={openDialog} onClose={handleCloseDialog} maxWidth="md" fullWidth>
          <DialogTitle>
            {isEditing ? 'Chỉnh sửa bất động sản' : 'Thêm bất động sản mới'}
          </DialogTitle>
          <DialogContent>
            {dialogLoading && (
                <Box sx={{ display: 'flex', justifyContent: 'center', py: 2 }}>
                  <CircularProgress size={24} />
                  <Typography sx={{ ml: 2 }}>Đang tải...</Typography>
                </Box>
            )}
            <Grid container spacing={2} sx={{ mt: 1 }}>
              <Grid item xs={12} md={6}>
                <TextField
                    fullWidth
                    label="Id bất động sản *"
                    value={formData.id || ''}
                    onChange={(e) => handleInputChange('id', e.target.value)}
                    required
                />
              </Grid>
              <Grid item xs={12} md={6}>
                <TextField
                    fullWidth
                    label="Tên bất động sản *"
                    value={formData.name || ''}
                    onChange={(e) => handleInputChange('name', e.target.value)}
                    required
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
                <TextField
                    fullWidth
                    label="Quận"
                    value={formData.district || ''}
                    onChange={(e) => handleInputChange('district', e.target.value)}
                />
              </Grid>
              <Grid item xs={12} md={6}>
                <TextField
                    fullWidth
                    label="Diện tích (m²)"
                    type="number"
                    value={formData.floorArea || ''}
                    onChange={(e) => handleInputChange('floorArea', parseFloat(e.target.value) || 0)}
                />
              </Grid>
              <Grid item xs={12} md={6}>
                <TextField
                    fullWidth
                    label="Số tầng hầm"
                    type="number"
                    value={formData.numberOfBasement || ''}
                    onChange={(e) => handleInputChange('numberOfBasement', parseInt(e.target.value) || 0)}
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
                    onChange={(e) => handleInputChange('rentPrice', parseFloat(e.target.value) || 0)}
                />
              </Grid>
              <Grid item xs={12} md={6}>
                <TextField
                    fullWidth
                    label="Cấu trúc"
                    value={formData.structure || ''}
                    onChange={(e) => handleInputChange('structure', e.target.value)}
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
                    value={formData.managerPhone || ''}
                    onChange={(e) => handleInputChange('managerPhone', e.target.value)}
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
              <Grid item xs={12}>
                <TextField
                    fullWidth
                    multiline
                    rows={2}
                    label="Ghi chú"
                    value={formData.note || ''}
                    onChange={(e) => handleInputChange('note', e.target.value)}
                />
              </Grid>
            </Grid>
          </DialogContent>
          <DialogActions>
            <Button onClick={handleCloseDialog}>Hủy</Button>
            <Button
                onClick={handleSave}
                variant="contained"
                disabled={dialogLoading}
                startIcon={dialogLoading ? <CircularProgress size={16} /> : undefined}
            >
              {isEditing ? 'Cập nhật' : 'Thêm mới'}
            </Button>
          </DialogActions>
        </Dialog>

        {/* Success Snackbar */}
        <Snackbar
            open={!!success}
            autoHideDuration={6000}
            onClose={() => setSuccess('')}
            anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
        >
          <Alert onClose={() => setSuccess('')} severity="success" sx={{ width: '100%' }}>
            {success}
          </Alert>
        </Snackbar>
      </Box>
  );
};

export default BuildingManagement;