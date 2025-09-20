import React from 'react';
import {
  Box,
  Typography,
  Container,
  Button,
  Grid,
  Paper,
  Card,
  CardContent,
} from '@mui/material';
import { Store, Star, LocalShipping, Security } from '@mui/icons-material';
import { Link } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import SweetList from '../components/Sweet/SweetList';

const Home = () => {
  const { user } = useAuth();

  const features = [
    {
      icon: <Store sx={{ fontSize: 40, color: 'primary.main' }} />,
      title: 'Wide Selection',
      description: 'Choose from our extensive collection of traditional and modern sweets',
    },
    {
      icon: <Star sx={{ fontSize: 40, color: 'primary.main' }} />,
      title: 'Premium Quality',
      description: 'All our sweets are made with the finest ingredients and traditional recipes',
    },
    {
      icon: <LocalShipping sx={{ fontSize: 40, color: 'primary.main' }} />,
      title: 'Fast Delivery',
      description: 'Quick and reliable delivery to your doorstep with proper packaging',
    },
    {
      icon: <Security sx={{ fontSize: 40, color: 'primary.main' }} />,
      title: 'Secure Shopping',
      description: 'Safe and secure payment processing with buyer protection',
    },
  ];

  return (
    <Box>
      {/* Hero Section */}
      <Paper
        sx={{
          background: 'linear-gradient(135deg, #fce4ec 0%, #f8bbd9 100%)',
          borderRadius: 3,
          p: { xs: 4, md: 6 },
          mb: 6,
          textAlign: 'center',
        }}
      >
        <Container maxWidth="md">
          <Typography
            variant="h2"
            component="h1"
            gutterBottom
            sx={{
              fontWeight: 700,
              color: 'text.primary',
              fontSize: { xs: '2rem', md: '3rem' },
            }}
          >
            Welcome to Sweet Shop
          </Typography>
          <Typography
            variant="h5"
            color="text.secondary"
            paragraph
            sx={{ mb: 4, fontSize: { xs: '1.1rem', md: '1.5rem' } }}
          >
            Discover the finest collection of traditional and modern sweets, 
            made with love and the best ingredients.
          </Typography>
          {!user && (
            <Box display="flex" gap={2} justifyContent="center" flexWrap="wrap">
              <Button
                variant="contained"
                size="large"
                component={Link}
                to="/register"
                sx={{ px: 4, py: 1.5 }}
              >
                Get Started
              </Button>
              <Button
                variant="outlined"
                size="large"
                component={Link}
                to="/login"
                sx={{ px: 4, py: 1.5 }}
              >
                Sign In
              </Button>
            </Box>
          )}
        </Container>
      </Paper>

      {/* Features Section */}
      <Container maxWidth="lg" sx={{ mb: 6 }}>
        <Typography variant="h4" component="h2" textAlign="center" gutterBottom sx={{ mb: 4 }}>
          Why Choose Our Sweet Shop?
        </Typography>
        <Grid container spacing={4}>
          {features.map((feature, index) => (
            <Grid item xs={12} sm={6} md={3} key={index}>
              <Card
                sx={{
                  height: '100%',
                  textAlign: 'center',
                  p: 2,
                  transition: 'transform 0.2s ease-in-out',
                  '&:hover': {
                    transform: 'translateY(-4px)',
                  },
                }}
              >
                <CardContent>
                  <Box mb={2}>
                    {feature.icon}
                  </Box>
                  <Typography variant="h6" component="h3" gutterBottom sx={{ fontWeight: 600 }}>
                    {feature.title}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    {feature.description}
                  </Typography>
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>
      </Container>

      {/* Sweet List Section */}
      <Container maxWidth="lg">
        <Typography variant="h4" component="h2" textAlign="center" gutterBottom sx={{ mb: 4 }}>
          Our Sweet Collection
        </Typography>
        <SweetList />
      </Container>
    </Box>
  );
};

export default Home;