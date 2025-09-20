import React, { useState } from 'react';
import {
  Box,
  Typography,
  Card,
  CardContent,
  Button,
  IconButton,
  TextField,
  Divider,
  Paper,
  Grid,
  Alert,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Snackbar,
} from '@mui/material';
import {
  Add,
  Remove,
  Delete,
  ShoppingCartCheckout,
  ArrowBack,
} from '@mui/icons-material';
import { Link, useNavigate } from 'react-router-dom';
import { useCart } from '../contexts/CartContext';
import axios from 'axios';

const Cart = () => {
  const {
    cartItems,
    updateQuantity,
    removeFromCart,
    clearCart,
    getTotalPrice,
    getTotalItems,
  } = useCart();
  
  const navigate = useNavigate();
  const [checkoutLoading, setCheckoutLoading] = useState(false);
  const [checkoutDialog, setCheckoutDialog] = useState(false);
  const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' });

  const handleQuantityChange = (itemId, newQuantity) => {
    if (newQuantity <= 0) {
      removeFromCart(itemId);
    } else {
      updateQuantity(itemId, newQuantity);
    }
  };

  const handleCheckout = async () => {
    setCheckoutLoading(true);
    try {
      // Process each item in the cart
      for (const item of cartItems) {
        await axios.post(`/api/sweets/${item.id}/purchase`, {
          quantity: item.quantity,
        });
      }

      clearCart();
      setCheckoutDialog(false);
      setSnackbar({
        open: true,
        message: 'Order placed successfully! Thank you for your purchase.',
        severity: 'success',
      });
      
      setTimeout(() => {
        navigate('/dashboard');
      }, 2000);
    } catch (error) {
      setSnackbar({
        open: true,
        message: error.response?.data?.message || 'Checkout failed. Please try again.',
        severity: 'error',
      });
    } finally {
      setCheckoutLoading(false);
    }
  };

  if (cartItems.length === 0) {
    return (
      <Box textAlign="center" py={8}>
        <Typography variant="h4" gutterBottom>
          Your Cart is Empty
        </Typography>
        <Typography variant="body1" color="text.secondary" sx={{ mb: 4 }}>
          Add some delicious sweets to your cart to get started!
        </Typography>
        <Button
          variant="contained"
          component={Link}
          to="/"
          startIcon={<ArrowBack />}
          size="large"
        >
          Continue Shopping
        </Button>
      </Box>
    );
  }

  return (
    <Box>
      <Typography variant="h4" gutterBottom sx={{ fontWeight: 600 }}>
        Shopping Cart ({getTotalItems()} items)
      </Typography>

      <Grid container spacing={3}>
        {/* Cart Items */}
        <Grid item xs={12} md={8}>
          <Paper sx={{ p: 2 }}>
            {cartItems.map((item, index) => (
              <Box key={item.id}>
                <Card variant="outlined" sx={{ mb: 2 }}>
                  <CardContent>
                    <Grid container spacing={2} alignItems="center">
                      <Grid item xs={12} sm={4}>
                        <Typography variant="h6" sx={{ fontWeight: 600 }}>
                          {item.name}
                        </Typography>
                        <Typography variant="body2" color="text.secondary">
                          {item.category.name}
                        </Typography>
                        <Typography variant="body2" color="primary" sx={{ fontWeight: 600 }}>
                          ₹{item.price.toFixed(2)} each
                        </Typography>
                      </Grid>

                      <Grid item xs={12} sm={4}>
                        <Box display="flex" alignItems="center" justifyContent="center" gap={1}>
                          <IconButton
                            onClick={() => handleQuantityChange(item.id, item.quantity - 1)}
                            size="small"
                          >
                            <Remove />
                          </IconButton>
                          <TextField
                            size="small"
                            type="number"
                            value={item.quantity}
                            onChange={(e) =>
                              handleQuantityChange(item.id, parseInt(e.target.value) || 1)
                            }
                            inputProps={{
                              min: 1,
                              max: item.quantity + item.quantity, // Allow current + available
                              style: { textAlign: 'center', width: '60px' },
                            }}
                          />
                          <IconButton
                            onClick={() => handleQuantityChange(item.id, item.quantity + 1)}
                            size="small"
                          >
                            <Add />
                          </IconButton>
                        </Box>
                      </Grid>

                      <Grid item xs={12} sm={3}>
                        <Box textAlign="right">
                          <Typography variant="h6" sx={{ fontWeight: 600 }}>
                            ${(item.price * item.quantity).toFixed(2)}
                          </Typography>
                          <IconButton
                            onClick={() => removeFromCart(item.id)}
                            color="error"
                            size="small"
                          >
                            <Delete />
                          </IconButton>
                        </Box>
                      </Grid>
                    </Grid>
                  </CardContent>
                </Card>
                {index < cartItems.length - 1 && <Divider sx={{ my: 2 }} />}
              </Box>
            ))}
          </Paper>
        </Grid>

        {/* Order Summary */}
        <Grid item xs={12} md={4}>
          <Paper sx={{ p: 3, position: 'sticky', top: 100 }}>
            <Typography variant="h6" gutterBottom sx={{ fontWeight: 600 }}>
              Order Summary
            </Typography>

            <Box display="flex" justifyContent="space-between" sx={{ mb: 1 }}>
              <Typography variant="body2">Items ({getTotalItems()})</Typography>
              <Typography variant="body2">${getTotalPrice().toFixed(2)}</Typography>
            </Box>

            <Box display="flex" justifyContent="space-between" sx={{ mb: 1 }}>
              <Typography variant="body2">Shipping</Typography>
              <Typography variant="body2">Free</Typography>
            </Box>

            <Box display="flex" justifyContent="space-between" sx={{ mb: 1 }}>
              <Typography variant="body2">Tax</Typography>
              <Typography variant="body2">${(getTotalPrice() * 0.08).toFixed(2)}</Typography>
            </Box>

            <Divider sx={{ my: 2 }} />

            <Box display="flex" justifyContent="space-between" sx={{ mb: 3 }}>
              <Typography variant="h6" sx={{ fontWeight: 600 }}>
                Total
              </Typography>
              <Typography variant="h6" sx={{ fontWeight: 600 }}>
                ${(getTotalPrice() * 1.08).toFixed(2)}
              </Typography>
            </Box>

            <Button
              variant="contained"
              fullWidth
              size="large"
              startIcon={<ShoppingCartCheckout />}
              onClick={() => setCheckoutDialog(true)}
              sx={{ mb: 2 }}
            >
              Proceed to Checkout
            </Button>

            <Button
              variant="outlined"
              fullWidth
              component={Link}
              to="/"
              startIcon={<ArrowBack />}
            >
              Continue Shopping
            </Button>
          </Paper>
        </Grid>
      </Grid>

      {/* Checkout Confirmation Dialog */}
      <Dialog open={checkoutDialog} onClose={() => setCheckoutDialog(false)} maxWidth="sm" fullWidth>
        <DialogTitle>Confirm Your Order</DialogTitle>
        <DialogContent>
          <Typography variant="body2" gutterBottom>
            You are about to place an order for {getTotalItems()} items totaling ₹{(getTotalPrice() * 1.08).toFixed(2)}.
          </Typography>
          <Alert severity="info" sx={{ mt: 2 }}>
            This action will immediately process your order and update the inventory.
          </Alert>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setCheckoutDialog(false)}>
            Cancel
          </Button>
          <Button
            onClick={handleCheckout}
            variant="contained"
            disabled={checkoutLoading}
          >
            {checkoutLoading ? 'Processing...' : 'Confirm Order'}
          </Button>
        </DialogActions>
      </Dialog>

      {/* Snackbar for notifications */}
      <Snackbar
        open={snackbar.open}
        autoHideDuration={6000}
        onClose={() => setSnackbar({ ...snackbar, open: false })}
      >
        <Alert severity={snackbar.severity} onClose={() => setSnackbar({ ...snackbar, open: false })}>
          {snackbar.message}
        </Alert>
      </Snackbar>
    </Box>
  );
};

export default Cart;