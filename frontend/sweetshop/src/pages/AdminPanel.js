// File: frontend/src/pages/AdminPanel.js
import React, { useState, useEffect } from 'react';
import {
  Box,
  Typography,
  Tabs,
  Tab,
  Paper,
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
  Grid,
  Card,
  CardContent,
  IconButton,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Chip,
  Alert,
  Snackbar,
  CircularProgress,
  Fab,
  Tooltip,
  Avatar,
  ListItemText,
  ListItemAvatar,
  List,
  ListItem,
} from '@mui/material';
import {
  Add,
  Edit,
  Delete,
  Inventory,
  Category,
  People,
  Assessment,
  Store,
  TrendingUp,
  Warning,
  CheckCircle,
  Info,
  Refresh,
} from '@mui/icons-material';
import axios from 'axios';

const AdminPanel = () => {
  const [activeTab, setActiveTab] = useState(0);
  const [sweets, setSweets] = useState([]);
  const [categories, setCategories] = useState([]);
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(false);
  
  // Dialog states
  const [sweetDialog, setSweetDialog] = useState({ open: false, mode: 'add', sweet: null });
  const [categoryDialog, setCategoryDialog] = useState({ open: false, mode: 'add', category: null });
  const [restockDialog, setRestockDialog] = useState({ open: false, sweet: null });
  
  // Form states
  const [sweetForm, setSweetForm] = useState({
    name: '',
    description: '',
    price: '',
    quantity: '',
    categoryId: '',
    imageUrl: '',
  });
  const [categoryForm, setCategoryForm] = useState({
    name: '',
    description: '',
  });
  const [restockQuantity, setRestockQuantity] = useState('');
  
  const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' });

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    setLoading(true);
    try {
      const [sweetsRes, categoriesRes, usersRes] = await Promise.all([
        axios.get('/api/sweets'),
        axios.get('/api/categories'),
        axios.get('/api/users').catch(() => ({ data: { data: [] } })) // Handle if users endpoint fails
      ]);
      setSweets(sweetsRes.data.data || sweetsRes.data);
      setCategories(categoriesRes.data.data || categoriesRes.data);
      setUsers(usersRes.data.data || usersRes.data);
    } catch (error) {
      showSnackbar('Failed to fetch data: ' + (error.response?.data?.message || error.message), 'error');
    } finally {
      setLoading(false);
    }
  };

  const showSnackbar = (message, severity = 'success') => {
    setSnackbar({ open: true, message, severity });
  };

  // Sweet Management Functions
  const handleSweetSubmit = async () => {
    try {
      const data = {
        ...sweetForm,
        price: parseFloat(sweetForm.price),
        quantity: parseInt(sweetForm.quantity),
        categoryId: parseInt(sweetForm.categoryId),
      };

      if (sweetDialog.mode === 'add') {
        await axios.post('/api/sweets', data);
        showSnackbar('Sweet added successfully!');
      } else {
        await axios.put(`/api/sweets/${sweetDialog.sweet.id}`, data);
        showSnackbar('Sweet updated successfully!');
      }
      
      closeSweetDialog();
      fetchData();
    } catch (error) {
      showSnackbar(error.response?.data?.message || 'Operation failed', 'error');
    }
  };

  const handleDeleteSweet = async (id, name) => {
    if (window.confirm(`Are you sure you want to delete "${name}"?`)) {
      try {
        await axios.delete(`/api/sweets/${id}`);
        showSnackbar('Sweet deleted successfully!');
        fetchData();
      } catch (error) {
        showSnackbar('Failed to delete sweet: ' + (error.response?.data?.message || error.message), 'error');
      }
    }
  };

  const handleRestock = async () => {
    try {
      await axios.post(`/api/sweets/${restockDialog.sweet.id}/restock`, {
        quantity: parseInt(restockQuantity),
      });
      showSnackbar(`Successfully restocked ${restockDialog.sweet.name}!`);
      setRestockDialog({ open: false, sweet: null });
      setRestockQuantity('');
      fetchData();
    } catch (error) {
      showSnackbar('Failed to restock: ' + (error.response?.data?.message || error.message), 'error');
    }
  };

  // Category Management Functions
  const handleCategorySubmit = async () => {
    try {
      if (categoryDialog.mode === 'add') {
        await axios.post('/api/categories', categoryForm);
        showSnackbar('Category added successfully!');
      } else {
        await axios.put(`/api/categories/${categoryDialog.category.id}`, categoryForm);
        showSnackbar('Category updated successfully!');
      }
      
      closeCategoryDialog();
      fetchData();
    } catch (error) {
      showSnackbar(error.response?.data?.message || 'Operation failed', 'error');
    }
  };

  const handleDeleteCategory = async (id, name) => {
    if (window.confirm(`Are you sure you want to delete "${name}" category?`)) {
      try {
        await axios.delete(`/api/categories/${id}`);
        showSnackbar('Category deleted successfully!');
        fetchData();
      } catch (error) {
        showSnackbar('Failed to delete category: ' + (error.response?.data?.message || error.message), 'error');
      }
    }
  };

  // Dialog Management Functions
  const openSweetDialog = (mode, sweet = null) => {
    setSweetDialog({ open: true, mode, sweet });
    if (sweet) {
      setSweetForm({
        name: sweet.name,
        description: sweet.description || '',
        price: sweet.price.toString(),
        quantity: sweet.quantity.toString(),
        categoryId: sweet.category?.id?.toString() || '',
        imageUrl: sweet.imageUrl || '',
      });
    } else {
      setSweetForm({
        name: '', description: '', price: '', quantity: '', categoryId: '', imageUrl: ''
      });
    }
  };

  const closeSweetDialog = () => {
    setSweetDialog({ open: false, mode: 'add', sweet: null });
    setSweetForm({ name: '', description: '', price: '', quantity: '', categoryId: '', imageUrl: '' });
  };

  const openCategoryDialog = (mode, category = null) => {
    setCategoryDialog({ open: true, mode, category });
    if (category) {
      setCategoryForm({
        name: category.name,
        description: category.description || '',
      });
    } else {
      setCategoryForm({ name: '', description: '' });
    }
  };

  const closeCategoryDialog = () => {
    setCategoryDialog({ open: false, mode: 'add', category: null });
    setCategoryForm({ name: '', description: '' });
  };

  // Analytics Calculations
  const analytics = {
    totalSweets: sweets.length,
    totalCategories: categories.length,
    totalUsers: users.length,
    lowStockItems: sweets.filter(s => s.quantity < 10).length,
    outOfStockItems: sweets.filter(s => s.quantity === 0).length,
    totalValue: sweets.reduce((sum, sweet) => sum + (sweet.price * sweet.quantity), 0),
  };

  return (
    <Box sx={{ minHeight: '100vh', bgcolor: 'background.default' }}>
      {/* Header */}
      <Paper sx={{ p: 3, mb: 3, background: 'linear-gradient(135deg, #f8bbd9 0%, #fce4ec 100%)' }}>
        <Box display="flex" justifyContent="space-between" alignItems="center">
          <Box>
            <Typography variant="h4" sx={{ fontWeight: 600, color: 'text.primary' }}>
              Admin Panel
            </Typography>
            <Typography variant="body1" color="text.secondary">
              Manage your sweet shop inventory, categories, and users
            </Typography>
          </Box>
          <Box display="flex" gap={1}>
            <Tooltip title="Refresh Data">
              <IconButton onClick={fetchData} disabled={loading}>
                <Refresh />
              </IconButton>
            </Tooltip>
          </Box>
        </Box>
      </Paper>

      {/* Tabs */}
      <Paper sx={{ mb: 3 }}>
        <Tabs 
          value={activeTab} 
          onChange={(e, v) => setActiveTab(v)}
          variant="scrollable"
          scrollButtons="auto"
        >
          <Tab icon={<Store />} label="Sweet Management" />
          <Tab icon={<Category />} label="Categories" />
          <Tab icon={<People />} label="Users" />
          <Tab icon={<Assessment />} label="Analytics" />
        </Tabs>
      </Paper>

      {loading && (
        <Box display="flex" justifyContent="center" my={4}>
          <CircularProgress size={60} />
        </Box>
      )}

      {/* Sweet Management Tab */}
      {activeTab === 0 && !loading && (
        <Box>
          <Box display="flex" justifyContent="space-between" alignItems="center" sx={{ mb: 3 }}>
            <Typography variant="h6" sx={{ fontWeight: 600 }}>
              Sweet Inventory ({sweets.length} items)
            </Typography>
            <Button
              variant="contained"
              startIcon={<Add />}
              onClick={() => openSweetDialog('add')}
              size="large"
            >
              Add New Sweet
            </Button>
          </Box>

          {/* Quick Stats */}
          <Grid container spacing={2} sx={{ mb: 3 }}>
            <Grid item xs={6} sm={3}>
              <Card sx={{ textAlign: 'center', p: 2 }}>
                <Typography color="success.main" variant="h4" sx={{ fontWeight: 600 }}>
                  {sweets.filter(s => s.quantity > 10).length}
                </Typography>
                <Typography variant="body2" color="text.secondary">In Stock</Typography>
              </Card>
            </Grid>
            <Grid item xs={6} sm={3}>
              <Card sx={{ textAlign: 'center', p: 2 }}>
                <Typography color="warning.main" variant="h4" sx={{ fontWeight: 600 }}>
                  {analytics.lowStockItems}
                </Typography>
                <Typography variant="body2" color="text.secondary">Low Stock</Typography>
              </Card>
            </Grid>
            <Grid item xs={6} sm={3}>
              <Card sx={{ textAlign: 'center', p: 2 }}>
                <Typography color="error.main" variant="h4" sx={{ fontWeight: 600 }}>
                  {analytics.outOfStockItems}
                </Typography>
                <Typography variant="body2" color="text.secondary">Out of Stock</Typography>
              </Card>
            </Grid>
            <Grid item xs={6} sm={3}>
              <Card sx={{ textAlign: 'center', p: 2 }}>
                <Typography color="primary.main" variant="h4" sx={{ fontWeight: 600 }}>
                  ₹{analytics.totalValue.toFixed(2)}
                </Typography>
                <Typography variant="body2" color="text.secondary">Total Value</Typography>
              </Card>
            </Grid>
          </Grid>

          <TableContainer component={Paper} sx={{ maxHeight: 600 }}>
            <Table stickyHeader>
              <TableHead>
                <TableRow>
                  <TableCell sx={{ fontWeight: 600 }}>Sweet</TableCell>
                  <TableCell sx={{ fontWeight: 600 }}>Category</TableCell>
                  <TableCell sx={{ fontWeight: 600 }}>Price</TableCell>
                  <TableCell sx={{ fontWeight: 600 }}>Stock</TableCell>
                  <TableCell sx={{ fontWeight: 600 }}>Status</TableCell>
                  <TableCell sx={{ fontWeight: 600 }}>Actions</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {sweets.map((sweet) => (
                  <TableRow key={sweet.id} hover>
                    <TableCell>
                      <Box>
                        <Typography variant="body1" sx={{ fontWeight: 500 }}>
                          {sweet.name}
                        </Typography>
                        <Typography variant="caption" color="text.secondary" noWrap>
                          {sweet.description}
                        </Typography>
                      </Box>
                    </TableCell>
                    <TableCell>
                      <Chip 
                        label={sweet.category?.name || 'N/A'} 
                        size="small" 
                        color="primary" 
                        variant="outlined" 
                      />
                    </TableCell>
                    <TableCell>
                      <Typography variant="body1" sx={{ fontWeight: 600 }}>
                        ₹{sweet.price?.toFixed(2)}
                      </Typography>
                    </TableCell>
                    <TableCell>
                      <Typography 
                        variant="body1" 
                        color={sweet.quantity < 10 ? 'warning.main' : 'text.primary'}
                        sx={{ fontWeight: 500 }}
                      >
                        {sweet.quantity}
                      </Typography>
                    </TableCell>
                    <TableCell>
                      <Chip
                        label={
                          sweet.quantity === 0 ? 'Out of Stock' : 
                          sweet.quantity < 10 ? 'Low Stock' : 'In Stock'
                        }
                        color={
                          sweet.quantity === 0 ? 'error' : 
                          sweet.quantity < 10 ? 'warning' : 'success'
                        }
                        size="small"
                      />
                    </TableCell>
                    <TableCell>
                      <Box display="flex" gap={1}>
                        <Tooltip title="Edit Sweet">
                          <IconButton
                            onClick={() => openSweetDialog('edit', sweet)}
                            color="primary"
                            size="small"
                          >
                            <Edit />
                          </IconButton>
                        </Tooltip>
                        <Tooltip title="Restock">
                          <IconButton
                            onClick={() => setRestockDialog({ open: true, sweet })}
                            color="info"
                            size="small"
                          >
                            <Inventory />
                          </IconButton>
                        </Tooltip>
                        <Tooltip title="Delete Sweet">
                          <IconButton
                            onClick={() => handleDeleteSweet(sweet.id, sweet.name)}
                            color="error"
                            size="small"
                          >
                            <Delete />
                          </IconButton>
                        </Tooltip>
                      </Box>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>

          {sweets.length === 0 && (
            <Paper sx={{ p: 4, textAlign: 'center', mt: 2 }}>
              <Store sx={{ fontSize: 60, color: 'text.secondary', mb: 2 }} />
              <Typography variant="h6" gutterBottom>
                No Sweets Found
              </Typography>
              <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                Start by adding your first sweet to the inventory
              </Typography>
              <Button 
                variant="contained" 
                startIcon={<Add />} 
                onClick={() => openSweetDialog('add')}
              >
                Add Your First Sweet
              </Button>
            </Paper>
          )}
        </Box>
      )}

      {/* Categories Tab */}
      {activeTab === 1 && !loading && (
        <Box>
          <Box display="flex" justifyContent="space-between" alignItems="center" sx={{ mb: 3 }}>
            <Typography variant="h6" sx={{ fontWeight: 600 }}>
              Category Management ({categories.length} categories)
            </Typography>
            <Button
              variant="contained"
              startIcon={<Add />}
              onClick={() => openCategoryDialog('add')}
              size="large"
            >
              Add New Category
            </Button>
          </Box>

          <Grid container spacing={3}>
            {categories.map((category) => (
              <Grid item xs={12} sm={6} md={4} key={category.id}>
                <Card sx={{ height: '100%' }}>
                  <CardContent>
                    <Box display="flex" justifyContent="space-between" alignItems="flex-start" sx={{ mb: 2 }}>
                      <Avatar sx={{ bgcolor: 'primary.main', mb: 1 }}>
                        <Category />
                      </Avatar>
                      <Typography variant="caption" color="text.secondary">
                        {sweets.filter(s => s.category?.id === category.id).length} items
                      </Typography>
                    </Box>
                    
                    <Typography variant="h6" gutterBottom sx={{ fontWeight: 600 }}>
                      {category.name}
                    </Typography>
                    <Typography 
                      variant="body2" 
                      color="text.secondary" 
                      sx={{ mb: 2, minHeight: 40 }}
                    >
                      {category.description || 'No description available'}
                    </Typography>
                    
                    <Box display="flex" gap={1} justifyContent="flex-end">
                      <Button
                        size="small"
                        startIcon={<Edit />}
                        onClick={() => openCategoryDialog('edit', category)}
                      >
                        Edit
                      </Button>
                      <Button
                        size="small"
                        color="error"
                        startIcon={<Delete />}
                        onClick={() => handleDeleteCategory(category.id, category.name)}
                      >
                        Delete
                      </Button>
                    </Box>
                  </CardContent>
                </Card>
              </Grid>
            ))}
          </Grid>

          {categories.length === 0 && (
            <Paper sx={{ p: 4, textAlign: 'center' }}>
              <Category sx={{ fontSize: 60, color: 'text.secondary', mb: 2 }} />
              <Typography variant="h6" gutterBottom>
                No Categories Found
              </Typography>
              <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                Create categories to organize your sweets
              </Typography>
              <Button 
                variant="contained" 
                startIcon={<Add />} 
                onClick={() => openCategoryDialog('add')}
              >
                Add Your First Category
              </Button>
            </Paper>
          )}
        </Box>
      )}

      {/* Users Tab */}
      {activeTab === 2 && !loading && (
        <Box>
          <Typography variant="h6" sx={{ mb: 3, fontWeight: 600 }}>
            User Management ({users.length} users)
          </Typography>
          
          {users.length > 0 ? (
            <TableContainer component={Paper}>
              <Table>
                <TableHead>
                  <TableRow>
                    <TableCell sx={{ fontWeight: 600 }}>User</TableCell>
                    <TableCell sx={{ fontWeight: 600 }}>Email</TableCell>
                    <TableCell sx={{ fontWeight: 600 }}>Roles</TableCell>
                    <TableCell sx={{ fontWeight: 600 }}>Status</TableCell>
                    <TableCell sx={{ fontWeight: 600 }}>Joined</TableCell>
                    <TableCell sx={{ fontWeight: 600 }}>Actions</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {users.map((user) => (
                    <TableRow key={user.id} hover>
                      <TableCell>
                        <Box display="flex" alignItems="center" gap={2}>
                          <Avatar sx={{ bgcolor: 'primary.main' }}>
                            {user.firstName?.charAt(0)}{user.lastName?.charAt(0)}
                          </Avatar>
                          <Box>
                            <Typography variant="body1" sx={{ fontWeight: 500 }}>
                              {user.firstName} {user.lastName}
                            </Typography>
                          </Box>
                        </Box>
                      </TableCell>
                      <TableCell>{user.email}</TableCell>
                      <TableCell>
                        <Box display="flex" gap={0.5}>
                          {user.roles?.map((role) => (
                            <Chip
                              key={role.id}
                              label={role.name}
                              size="small"
                              color={role.name === 'ADMIN' ? 'error' : 'primary'}
                            />
                          )) || <Chip label="USER" size="small" color="primary" />}
                        </Box>
                      </TableCell>
                      <TableCell>
                        <Chip
                          label={user.enabled ? 'Active' : 'Inactive'}
                          color={user.enabled ? 'success' : 'default'}
                          size="small"
                        />
                      </TableCell>
                      <TableCell>
                        {user.createdAt ? new Date(user.createdAt).toLocaleDateString() : 'N/A'}
                      </TableCell>
                      <TableCell>
                        <Button size="small" disabled>
                          View Details
                        </Button>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </TableContainer>
          ) : (
            <Paper sx={{ p: 4, textAlign: 'center' }}>
              <People sx={{ fontSize: 60, color: 'text.secondary', mb: 2 }} />
              <Typography variant="h6" gutterBottom>
                No Users Found
              </Typography>
              <Typography variant="body2" color="text.secondary">
                Users will appear here as they register
              </Typography>
            </Paper>
          )}
        </Box>
      )}

      {/* Analytics Tab */}
      {activeTab === 3 && !loading && (
        <Box>
          <Typography variant="h6" sx={{ mb: 3, fontWeight: 600 }}>
            Analytics Dashboard
          </Typography>
          
          <Grid container spacing={3} sx={{ mb: 4 }}>
            <Grid item xs={12} sm={6} md={3}>
              <Card sx={{ textAlign: 'center', p: 3 }}>
                <Store sx={{ fontSize: 40, color: 'primary.main', mb: 1 }} />
                <Typography variant="h4" sx={{ fontWeight: 600, color: 'primary.main' }}>
                  {analytics.totalSweets}
                </Typography>
                <Typography color="textSecondary" variant="body2">
                  Total Sweets
                </Typography>
              </Card>
            </Grid>
            <Grid item xs={12} sm={6} md={3}>
              <Card sx={{ textAlign: 'center', p: 3 }}>
                <Category sx={{ fontSize: 40, color: 'secondary.main', mb: 1 }} />
                <Typography variant="h4" sx={{ fontWeight: 600, color: 'secondary.main' }}>
                  {analytics.totalCategories}
                </Typography>
                <Typography color="textSecondary" variant="body2">
                  Categories
                </Typography>
              </Card>
            </Grid>
            <Grid item xs={12} sm={6} md={3}>
              <Card sx={{ textAlign: 'center', p: 3 }}>
                <People sx={{ fontSize: 40, color: 'info.main', mb: 1 }} />
                <Typography variant="h4" sx={{ fontWeight: 600, color: 'info.main' }}>
                  {analytics.totalUsers}
                </Typography>
                <Typography color="textSecondary" variant="body2">
                  Total Users
                </Typography>
              </Card>
            </Grid>
            <Grid item xs={12} sm={6} md={3}>
              <Card sx={{ textAlign: 'center', p: 3 }}>
                <Warning sx={{ fontSize: 40, color: 'warning.main', mb: 1 }} />
                <Typography variant="h4" sx={{ fontWeight: 600, color: 'warning.main' }}>
                  {analytics.lowStockItems}
                </Typography>
                <Typography color="textSecondary" variant="body2">
                  Low Stock Items
                </Typography>
              </Card>
            </Grid>
          </Grid>

          {/* Low Stock Alert */}
          {analytics.lowStockItems > 0 && (
            <Alert severity="warning" sx={{ mb: 3 }}>
              <Typography variant="body1">
                <strong>{analytics.lowStockItems}</strong> items are running low on stock. 
                Consider restocking soon!
              </Typography>
            </Alert>
          )}

          {/* Out of Stock Alert */}
          {analytics.outOfStockItems > 0 && (
            <Alert severity="error" sx={{ mb: 3 }}>
              <Typography variant="body1">
                <strong>{analytics.outOfStockItems}</strong> items are out of stock and 
                unavailable for purchase.
              </Typography>
            </Alert>
          )}

          {/* Recent Activity */}
          <Paper sx={{ p: 3 }}>
            <Typography variant="h6" gutterBottom sx={{ fontWeight: 600 }}>
              Inventory Status
            </Typography>
            <List>
              {sweets.slice(0, 5).map((sweet) => (
                <ListItem key={sweet.id}>
                  <ListItemAvatar>
                    <Avatar sx={{ 
                      bgcolor: sweet.quantity === 0 ? 'error.main' : 
                              sweet.quantity < 10 ? 'warning.main' : 'success.main' 
                    }}>
                      {sweet.quantity === 0 ? <Warning /> : 
                      sweet.quantity < 10 ? <Info /> : <CheckCircle />}
                    </Avatar>
                  </ListItemAvatar>
                  <ListItemText
                    primary={sweet.name}
                    secondary={`Stock: ${sweet.quantity} | Price: ₹${sweet.price?.toFixed(2)}`}
                  />
                </ListItem>
              ))}
            </List>
          </Paper>
        </Box>
      )}

      {/* Sweet Management Dialog */}
      <Dialog open={sweetDialog.open} onClose={closeSweetDialog} maxWidth="md" fullWidth>
        <DialogTitle>
          {sweetDialog.mode === 'add' ? 'Add New Sweet' : 'Edit Sweet'}
        </DialogTitle>
        <DialogContent>
          <Grid container spacing={2} sx={{ mt: 1 }}>
            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                label="Sweet Name"
                value={sweetForm.name}
                onChange={(e) => setSweetForm({ ...sweetForm, name: e.target.value })}
                required
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <FormControl fullWidth required>
                <InputLabel>Category</InputLabel>
                <Select
                  value={sweetForm.categoryId}
                  label="Category"
                  onChange={(e) => setSweetForm({ ...sweetForm, categoryId: e.target.value })}
                >
                  {categories.map((category) => (
                    <MenuItem key={category.id} value={category.id}>
                      {category.name}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Grid>
            <Grid item xs={12}>
              <TextField
                fullWidth
                label="Description"
                multiline
                rows={3}
                value={sweetForm.description}
                onChange={(e) => setSweetForm({ ...sweetForm, description: e.target.value })}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                label="Price (₹)"
                type="number"
                value={sweetForm.price}
                onChange={(e) => setSweetForm({ ...sweetForm, price: e.target.value })}
                inputProps={{ min: 0, step: 0.01 }}
                required
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                label="Initial Quantity"
                type="number"
                value={sweetForm.quantity}
                onChange={(e) => setSweetForm({ ...sweetForm, quantity: e.target.value })}
                inputProps={{ min: 0 }}
                required
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                fullWidth
                label="Image URL (optional)"
                value={sweetForm.imageUrl}
                onChange={(e) => setSweetForm({ ...sweetForm, imageUrl: e.target.value })}
                placeholder="https://example.com/image.jpg"
              />
            </Grid>
          </Grid>
        </DialogContent>
        <DialogActions sx={{ p: 2 }}>
          <Button onClick={closeSweetDialog}>Cancel</Button>
          <Button 
            onClick={handleSweetSubmit} 
            variant="contained"
            disabled={!sweetForm.name || !sweetForm.price || !sweetForm.quantity || !sweetForm.categoryId}
          >
            {sweetDialog.mode === 'add' ? 'Add Sweet' : 'Update Sweet'}
          </Button>
        </DialogActions>
      </Dialog>

      {/* Category Management Dialog */}
      <Dialog open={categoryDialog.open} onClose={closeCategoryDialog} maxWidth="sm" fullWidth>
        <DialogTitle>
          {categoryDialog.mode === 'add' ? 'Add New Category' : 'Edit Category'}
        </DialogTitle>
        <DialogContent>
          <TextField
            fullWidth
            label="Category Name"
            value={categoryForm.name}
            onChange={(e) => setCategoryForm({ ...categoryForm, name: e.target.value })}
            sx={{ mt: 2, mb: 2 }}
            required
          />
          <TextField
            fullWidth
            label="Description"
            multiline
            rows={3}
            value={categoryForm.description}
            onChange={(e) => setCategoryForm({ ...categoryForm, description: e.target.value })}
          />
        </DialogContent>
        <DialogActions sx={{ p: 2 }}>
    
          <Button onClick={closeCategoryDialog}>Cancel</Button>
          <Button 
            onClick={handleCategorySubmit} 
            variant="contained"
            disabled={!categoryForm.name}
          >
            {categoryDialog.mode === 'add' ? 'Add Category' : 'Update Category'}
          </Button>
        </DialogActions>
      </Dialog>

      {/* Restock Dialog */}
      <Dialog open={restockDialog.open} onClose={() => setRestockDialog({ open: false, sweet: null })}>
        <DialogTitle>Restock {restockDialog.sweet?.name}</DialogTitle>
        <DialogContent>
          <TextField
            fullWidth
            label="Quantity to Add"
            type="number"
            value={restockQuantity}
            onChange={(e) => setRestockQuantity(e.target.value)}
            inputProps={{ min: 1 }}
            sx={{ mt: 2 }}
          />
        </DialogContent>
        <DialogActions sx={{ p: 2 }}>
          <Button onClick={() => setRestockDialog({ open: false, sweet: null })}>Cancel</Button>
          <Button 
            onClick={handleRestock} 
            variant="contained" 
            disabled={!restockQuantity || parseInt(restockQuantity) <= 0}
          >
            Restock
          </Button>
        </DialogActions>
      </Dialog>

      {/* Snackbar */}
      <Snackbar
        open={snackbar.open}
        autoHideDuration={4000}
        onClose={() => setSnackbar({ ...snackbar, open: false })}
        anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
      >
        <Alert 
          onClose={() => setSnackbar({ ...snackbar, open: false })} 
          severity={snackbar.severity} 
          sx={{ width: '100%' }}
        >
          {snackbar.message}
        </Alert>
      </Snackbar>
    </Box>
  );
};

export default AdminPanel;
