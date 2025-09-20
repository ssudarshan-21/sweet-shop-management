import React, { useState } from 'react';
import {
  Card,
  CardContent,
  CardActions,
  CardMedia,
  Typography,
  Button,
  Chip,
  Box,
  TextField,
  Alert,
  Snackbar,
  IconButton,
} from '@mui/material';
import { Add, Remove, ShoppingCart } from '@mui/icons-material';
import { useAuth } from '../../contexts/AuthContext';
import { useCart } from '../../contexts/CartContext';
import axios from 'axios';

const SweetCard = ({ sweet, onPurchase }) => {
  const { user, isAdmin } = useAuth();
  const { addToCart } = useCart();
  const [quantity, setQuantity] = useState(1);
  const [loading, setLoading] = useState(false);
  const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' });

  const handlePurchase = async () => {
    setLoading(true);
    try {
      await axios.post(`/api/sweets/${sweet.id}/purchase`, { quantity });
      setSnackbar({
        open: true,
        message: `Successfully purchased ${quantity} ${sweet.name}(s)!`,
        severity: 'success'
      });
      if (onPurchase) {
        onPurchase();
      }
    } catch (error) {
      setSnackbar({
        open: true,
        message: error.response?.data?.message || 'Purchase failed',
        severity: 'error'
      });
    } finally {
      setLoading(false);
    }
  };

  const handleAddToCart = () => {
    addToCart(sweet, quantity);
    setSnackbar({
      open: true,
      message: `Added ${quantity} ${sweet.name}(s) to cart!`,
      severity: 'success'
    });
  };

  const isOutOfStock = sweet.quantity === 0;
  const maxQuantity = Math.min(sweet.quantity, 10);

  return (
    <>
      <Card
        sx={{
          height: '100%',
          display: 'flex',
          flexDirection: 'column',
          transition: 'transform 0.2s ease-in-out, box-shadow 0.2s ease-in-out',
          '&:hover': {
            transform: 'translateY(-4px)',
            boxShadow: '0 8px 25px rgba(0,0,0,0.15)',
          },
        }}
      >
        {/* Image Section */}
        {sweet.imageUrl && (
          <CardMedia
            component="img"
            height="180"
            image={sweet.imageUrl}
            alt={sweet.name}
            sx={{ objectFit: 'cover', width: '100%' }}
          />
        )}

        <CardContent sx={{ flexGrow: 1, pb: 1 }}>
          <Box display="flex" justifyContent="space-between" alignItems="flex-start" mb={1}>
            <Typography variant="h6" component="h2" sx={{ fontWeight: 600, fontSize: '1.1rem' }}>
              {sweet.name}
            </Typography>
            <Chip
              label={sweet.category?.name || "uncategorized"}
              size="small"
              color="primary"
              variant="outlined"
            />
          </Box>
          
          <Typography 
            variant="body2" 
            color="text.secondary" 
            sx={{ 
              mb: 2, 
              overflow: 'hidden',
              textOverflow: 'ellipsis',
              display: '-webkit-box',
              WebkitLineClamp: 2,
              WebkitBoxOrient: 'vertical',
            }}
          >
            {sweet.description}
          </Typography>
          
          <Box display="flex" justifyContent="space-between" alignItems="center" mb={1}>
            <Typography variant="h6" color="primary" sx={{ fontWeight: 600 }}>
              ₹{sweet.price.toFixed(2)}
            </Typography>
            <Chip
              label={isOutOfStock ? 'Out of Stock' : `${sweet.quantity} in stock`}
              color={isOutOfStock ? 'error' : sweet.quantity < 10 ? 'warning' : 'success'}
              size="small"
            />
          </Box>
        </CardContent>

        {user && !isAdmin() && (
          <CardActions sx={{ p: 2, pt: 0, flexDirection: 'column', gap: 1 }}>
            {/* Quantity Selector */}
            <Box display="flex" alignItems="center" justifyContent="center" gap={1} width="100%">
              <IconButton
                size="small"
                onClick={() => setQuantity(Math.max(1, quantity - 1))}
                disabled={quantity <= 1 || isOutOfStock}
                sx={{ minWidth: 32, height: 32 }}
              >
                <Remove fontSize="small" />
              </IconButton>
              
              <TextField
                size="small"
                type="number"
                value={quantity}
                onChange={(e) =>
                  setQuantity(Math.max(1, Math.min(maxQuantity, parseInt(e.target.value) || 1)))
                }
                inputProps={{ 
                  min: 1, 
                  max: maxQuantity, 
                  style: { 
                    textAlign: 'center', 
                    width: '50px',
                    padding: '8px 4px'
                  } 
                }}
                disabled={isOutOfStock}
                sx={{ 
                  '& .MuiOutlinedInput-root': { 
                    minHeight: '32px',
                    '& fieldset': { borderColor: 'divider' }
                  }
                }}
              />
              
              <IconButton
                size="small"
                onClick={() => setQuantity(Math.min(maxQuantity, quantity + 1))}
                disabled={quantity >= maxQuantity || isOutOfStock}
                sx={{ minWidth: 32, height: 32 }}
              >
                <Add fontSize="small" />
              </IconButton>
            </Box>
            
            {/* Action Buttons */}
            <Box display="flex" gap={1} width="100%">
              <Button
                variant="outlined"
                size="small"
                startIcon={<ShoppingCart />}
                onClick={handleAddToCart}
                disabled={isOutOfStock}
                fullWidth
                sx={{ 
                  fontSize: '0.75rem',
                  py: 1,
                  minHeight: '36px'
                }}
              >
                Add to Cart
              </Button>
              <Button
                variant="contained"
                size="small"
                onClick={handlePurchase}
                disabled={isOutOfStock || loading}
                fullWidth
                sx={{ 
                  fontSize: '0.75rem',
                  py: 1,
                  minHeight: '36px'
                }}
              >
                {loading ? 'Processing...' : 'Buy Now'}
              </Button>
            </Box>
          </CardActions>
        )}
      </Card>

      <Snackbar
        open={snackbar.open}
        autoHideDuration={4000}
        onClose={() => setSnackbar({ ...snackbar, open: false })}
      >
        <Alert severity={snackbar.severity} onClose={() => setSnackbar({ ...snackbar, open: false })}>
          {snackbar.message}
        </Alert>
      </Snackbar>
    </>
  );
};

export default SweetCard;