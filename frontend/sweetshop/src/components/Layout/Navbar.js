import React, { useState } from 'react';
import {
  AppBar,
  Toolbar,
  Typography,
  Button,
  IconButton,
  Badge,
  Menu,
  MenuItem,
  Box,
} from '@mui/material';
import {
  ShoppingCart,
  AccountCircle,
  Store,
  Dashboard,
  AdminPanelSettings,
  Logout,
} from '@mui/icons-material';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import { useCart } from '../../contexts/CartContext';

const Navbar = () => {
  const { user, logout, isAdmin } = useAuth();
  const { getTotalItems } = useCart();
  const navigate = useNavigate();
  const [anchorEl, setAnchorEl] = useState(null);

  const handleMenu = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null);
  };

  const handleLogout = () => {
    logout();
    handleClose();
    navigate('/');
  };

  const handleNavigation = (path) => {
    navigate(path);
    handleClose();
  };

  return (
    <AppBar position="sticky" elevation={2}>
      <Toolbar>
        <Store sx={{ mr: 2 }} />
        <Typography
          variant="h6"
          component={Link}
          to="/"
          sx={{
            flexGrow: 1,
            textDecoration: 'none',
            color: 'inherit',
            fontWeight: 600,
          }}
        >
          Sweet Shop
        </Typography>

        {user ? (
          <Box display="flex" alignItems="center" gap={1}>
            {!isAdmin() && (
              <IconButton
                color="inherit"
                component={Link}
                to="/cart"
                aria-label="shopping cart"
              >
                <Badge badgeContent={getTotalItems()} color="secondary">
                  <ShoppingCart />
                </Badge>
              </IconButton>
            )}

            <IconButton
              size="large"
              aria-label="account menu"
              aria-controls="menu-appbar"
              aria-haspopup="true"
              onClick={handleMenu}
              color="inherit"
            >
              <AccountCircle />
            </IconButton>
            <Menu
              id="menu-appbar"
              anchorEl={anchorEl}
              anchorOrigin={{
                vertical: 'bottom',
                horizontal: 'right',
              }}
              keepMounted
              transformOrigin={{
                vertical: 'top',
                horizontal: 'right',
              }}
              open={Boolean(anchorEl)}
              onClose={handleClose}
            >
              <MenuItem onClick={() => handleNavigation('/dashboard')}>
                <Dashboard sx={{ mr: 1 }} />
                Dashboard
              </MenuItem>
              {isAdmin() && (
                <MenuItem onClick={() => handleNavigation('/admin')}>
                  <AdminPanelSettings sx={{ mr: 1 }} />
                  Admin Panel
                </MenuItem>
              )}
              <MenuItem onClick={() => handleNavigation('/profile')}>
                <AccountCircle sx={{ mr: 1 }} />
                Profile
              </MenuItem>
              <MenuItem onClick={handleLogout}>
                <Logout sx={{ mr: 1 }} />
                Logout
              </MenuItem>
            </Menu>
          </Box>
        ) : (
          <Box>
            <Button color="inherit" component={Link} to="/login" sx={{ mr: 1 }}>
              Login
            </Button>
            <Button
              variant="outlined"
              color="inherit"
              component={Link}
              to="/register"
              sx={{
                borderColor: 'currentColor',
                '&:hover': {
                  borderColor: 'currentColor',
                  backgroundColor: 'rgba(255, 255, 255, 0.1)',
                },
              }}
            >
              Register
            </Button>
          </Box>
        )}
      </Toolbar>
    </AppBar>
  );
};

export default Navbar;