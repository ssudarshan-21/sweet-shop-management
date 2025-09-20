import React, { useState, useEffect } from 'react';
import {
  Grid,
  Typography,
  CircularProgress,
  Box,
  Alert,
  TextField,
  MenuItem,
  FormControl,
  InputLabel,
  Select,
  Paper,
  InputAdornment,
} from '@mui/material';
import { Search } from '@mui/icons-material';
import SweetCard from './SweetCard';
import axios from 'axios';

const SweetList = ({ refreshTrigger }) => {
  const [sweets, setSweets] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedCategory, setSelectedCategory] = useState('');
  const [minPrice, setMinPrice] = useState('');
  const [maxPrice, setMaxPrice] = useState('');

  useEffect(() => {
    fetchSweets();
    fetchCategories();
  }, [refreshTrigger]);

  useEffect(() => {
    const delayedSearch = setTimeout(() => {
      searchSweets();
    }, 500);

    return () => clearTimeout(delayedSearch);
  }, [searchTerm, selectedCategory, minPrice, maxPrice]);

  const fetchSweets = async () => {
    try {
      setLoading(true);
      const response = await axios.get('/api/sweets');
      setSweets(response.data.data);
      setError(null);
    } catch (err) {
      setError('Failed to fetch sweets');
      console.error('Error fetching sweets:', err);
    } finally {
      setLoading(false);
    }
  };

  const fetchCategories = async () => {
    try {
      const response = await axios.get('/api/categories');
      setCategories(response.data.data);
    } catch (err) {
      console.error('Error fetching categories:', err);
    }
  };

  const searchSweets = async () => {
    try {
      setLoading(true);
      const params = new URLSearchParams();
      
      if (searchTerm) params.append('name', searchTerm);
      if (selectedCategory) params.append('categoryId', selectedCategory);
      if (minPrice) params.append('minPrice', minPrice);
      if (maxPrice) params.append('maxPrice', maxPrice);
      params.append('onlyAvailable', 'true');

      const response = await axios.get(`/api/sweets/search?${params.toString()}`);
      setSweets(response.data.data);
      setError(null);
    } catch (err) {
      setError('Failed to search sweets');
      console.error('Error searching sweets:', err);
    } finally {
      setLoading(false);
    }
  };

  const clearFilters = () => {
    setSearchTerm('');
    setSelectedCategory('');
    setMinPrice('');
    setMaxPrice('');
  };

  if (loading && sweets.length === 0) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="400px">
        <CircularProgress size={60} />
      </Box>
    );
  }

  if (error) {
    return (
      <Alert severity="error" sx={{ mb: 2 }}>
        {error}
      </Alert>
    );
  }

  return (
    <Box>
      {/* Search and Filter Controls */}
      <Paper sx={{ p: 3, mb: 3 }}>
        <Typography variant="h6" gutterBottom>
          Search & Filter Sweets
        </Typography>
        <Grid container spacing={2} alignItems="center">
          <Grid item xs={12} sm={6} md={3}>
            <TextField
              fullWidth
              label="Search sweets"
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
          <Grid item xs={12} sm={6} md={3}>
            <FormControl fullWidth>
              <InputLabel>Category</InputLabel>
              <Select
                value={selectedCategory}
                label="Category"
                onChange={(e) => setSelectedCategory(e.target.value)}
              >
                <MenuItem value="">All Categories</MenuItem>
                {categories.map((category) => (
                  <MenuItem key={category.id} value={category.id}>
                    {category.name}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
          </Grid>
          <Grid item xs={12} sm={6} md={2}>
            <TextField
              fullWidth
              label="Min Price"
              type="number"
              value={minPrice}
              onChange={(e) => setMinPrice(e.target.value)}
              inputProps={{ min: 0, step: 0.01 }}
            />
          </Grid>
          <Grid item xs={12} sm={6} md={2}>
            <TextField
              fullWidth
              label="Max Price"
              type="number"
              value={maxPrice}
              onChange={(e) => setMaxPrice(e.target.value)}
              inputProps={{ min: 0, step: 0.01 }}
            />
          </Grid>
          <Grid item xs={12} md={2}>
            <Box display="flex" justifyContent="center">
              <Typography
                variant="body2"
                color="primary"
                sx={{ cursor: 'pointer', textDecoration: 'underline' }}
                onClick={clearFilters}
              >
                Clear Filters
              </Typography>
            </Box>
          </Grid>
        </Grid>
      </Paper>

      {/* Results */}
      <Typography variant="h6" gutterBottom>
        {sweets.length} sweets found
      </Typography>

      {loading ? (
        <Box display="flex" justifyContent="center" mt={4}>
          <CircularProgress />
        </Box>
      ) : sweets.length === 0 ? (
        <Box textAlign="center" py={8}>
          <Typography variant="h6" color="text.secondary">
            No sweets found matching your criteria
          </Typography>
          <Typography variant="body2" color="text.secondary" mt={1}>
            Try adjusting your search filters
          </Typography>
        </Box>
      ) : (
        <Grid container spacing={3}>
          {sweets.map((sweet) => (
            <Grid item xs={12} sm={6} md={4} lg={3} key={sweet.id}>
              <SweetCard sweet={sweet} onPurchase={fetchSweets} />
            </Grid>
          ))}
        </Grid>
      )}
    </Box>
  );
};

export default SweetList;