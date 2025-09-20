import React, { useState, useEffect } from 'react';
import {
  Box,
  Typography,
  Grid,
  Card,
  CardContent,
  Button,
  Tabs,
  Tab,
  Paper,
  CircularProgress,
} from '@mui/material';
import { ShoppingCart, Favorite, History, Person } from '@mui/icons-material';
import { useAuth } from '../contexts/AuthContext';
import SweetList from '../components/Sweet/SweetList';
import axios from 'axios';

const Dashboard = () => {
  const { user } = useAuth();
  const [activeTab, setActiveTab] = useState(0);
  const [userStats, setUserStats] = useState({
    totalOrders: 0,
    favoriteItems: 0,
    totalSpent: 0,
    loading: true
  });

  useEffect(() => {
    fetchUserStats();
  }, []);

  const fetchUserStats = async () => {
    try {
      // These endpoints would need to be implemented in your backend
      const [ordersRes, favoritesRes] = await Promise.all([
        axios.get('/api/users/orders').catch(() => ({ data: { data: [] } })),
        axios.get('/api/users/favorites').catch(() => ({ data: { data: [] } }))
      ]);

      const orders = ordersRes.data.data || [];
      const favorites = favoritesRes.data.data || [];
      
      // Calculate total spent from orders
      const totalSpent = orders.reduce((sum, order) => sum + (order.totalAmount || 0), 0);

      setUserStats({
        totalOrders: orders.length,
        favoriteItems: favorites.length,
        totalSpent: totalSpent,
        loading: false
      });
    } catch (error) {
      console.error('Failed to fetch user stats:', error);
      // Set default values if API calls fail
      setUserStats({
        totalOrders: 0,
        favoriteItems: 0,
        totalSpent: 0,
        loading: false
      });
    }
  };

  const handleTabChange = (event, newValue) => {
    setActiveTab(newValue);
  };

  const stats = [
    {
      title: 'Total Orders',
      value: userStats.loading ? '-' : userStats.totalOrders.toString(),
      icon: <ShoppingCart sx={{ fontSize: 40, color: 'primary.main' }} />,
      color: 'primary.main',
    },
    {
      title: 'Favorite Items',
      value: userStats.loading ? '-' : userStats.favoriteItems.toString(),
      icon: <Favorite sx={{ fontSize: 40, color: 'error.main' }} />,
      color: 'error.main',
    },
    {
      title: 'Total Spent',
      value: userStats.loading ? '-' : `₹${userStats.totalSpent.toFixed(2)}`,
      icon: <History sx={{ fontSize: 40, color: 'success.main' }} />,
      color: 'success.main',
    },
    {
      title: 'Member Since',
      value: user?.createdAt ? new Date(user.createdAt).toLocaleDateString() : 'Today',
      icon: <Person sx={{ fontSize: 40, color: 'info.main' }} />,
      color: 'info.main',
    },
  ];

  return (
    <Box>
      {/* Welcome Section */}
      <Paper sx={{ p: 4, mb: 4, background: 'linear-gradient(135deg, #fce4ec 0%, #f8bbd9 100%)' }}>
        <Typography variant="h4" gutterBottom sx={{ fontWeight: 600 }}>
          Welcome back, {user?.firstName}!
        </Typography>
        <Typography variant="body1" color="text.secondary">
          Explore our delicious collection of sweets and place your orders.
        </Typography>
      </Paper>

      {/* Stats Cards */}
      <Grid container spacing={3} sx={{ mb: 4 }}>
        {stats.map((stat, index) => (
          <Grid item xs={12} sm={6} md={3} key={index}>
            <Card
              sx={{
                height: '100%',
                display: 'flex',
                alignItems: 'center',
                p: 2,
                transition: 'transform 0.2s ease-in-out',
                '&:hover': {
                  transform: 'translateY(-2px)',
                },
              }}
            >
              <Box sx={{ mr: 2 }}>
                {userStats.loading ? <CircularProgress size={40} /> : stat.icon}
              </Box>
              <CardContent sx={{ p: 0 }}>
                <Typography variant="h6" sx={{ fontWeight: 600 }}>
                  {stat.value}
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  {stat.title}
                </Typography>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>

      {/* Rest of your component remains the same */}
      <Box sx={{ borderBottom: 1, borderColor: 'divider', mb: 3 }}>
        <Tabs value={activeTab} onChange={handleTabChange}>
          <Tab label="Browse Sweets" />
          <Tab label="Order History" />
          <Tab label="Favorites" />
        </Tabs>
      </Box>

      {activeTab === 0 && (
        <Box>
          <SweetList />
        </Box>
      )}

      {activeTab === 1 && (
        <Paper sx={{ p: 4, textAlign: 'center' }}>
          <Typography variant="h6" gutterBottom>
            Order History
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Your order history will appear here once you make your first purchase.
          </Typography>
          <Button variant="contained" sx={{ mt: 2 }} onClick={() => setActiveTab(0)}>
            Start Shopping
          </Button>
        </Paper>
      )}

      {activeTab === 2 && (
        <Paper sx={{ p: 4, textAlign: 'center' }}>
          <Typography variant="h6" gutterBottom>
            Favorite Items
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Your favorite sweets will be saved here for quick access.
          </Typography>
          <Button variant="contained" sx={{ mt: 2 }} onClick={() => setActiveTab(0)}>
            Browse Sweets
          </Button>
        </Paper>
      )}
    </Box>
  );
};

export default Dashboard;