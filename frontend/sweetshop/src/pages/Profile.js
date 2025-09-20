import React, { useState } from 'react';
import {
  Box,
  Typography,
  Card,
  CardContent,
  Button,
  Grid,
  Divider,
  Chip,
  Paper,
  List,
  ListItem,
  ListItemIcon,
  ListItemText,
} from '@mui/material';
import {
  Person,
  Email,
  CalendarToday,
  Security,
  Edit,
  Verified,
} from '@mui/icons-material';
import { useAuth } from '../contexts/AuthContext';

const Profile = () => {
  const { user, isAdmin } = useAuth();
  const [editing, setEditing] = useState(false);

  const userInfo = [
    {
      icon: <Person color="primary" />,
      label: 'Full Name',
      value: `${user?.firstName} ${user?.lastName}`,
    },
    {
      icon: <Email color="primary" />,
      label: 'Email Address',
      value: user?.email,
    },
    {
      icon: <CalendarToday color="primary" />,
      label: 'Member Since',
      value: new Date(user?.createdAt).toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
      }),
    },
    {
      icon: <Security color="primary" />,
      label: 'Account Status',
      value: user?.enabled ? 'Active' : 'Inactive',
    },
  ];

  return (
    <Box>
      <Typography variant="h4" gutterBottom sx={{ fontWeight: 600 }}>
        My Profile
      </Typography>

      <Grid container spacing={3}>
        {/* Profile Information */}
        <Grid item xs={12} md={8}>
          <Card>
            <CardContent sx={{ p: 3 }}>
              <Box display="flex" justifyContent="space-between" alignItems="center" sx={{ mb: 3 }}>
                <Typography variant="h6" sx={{ fontWeight: 600 }}>
                  Personal Information
                </Typography>
                <Button
                  variant="outlined"
                  startIcon={<Edit />}
                  onClick={() => setEditing(!editing)}
                >
                  {editing ? 'Cancel' : 'Edit Profile'}
                </Button>
              </Box>

              <List disablePadding>
                {userInfo.map((info, index) => (
                  <React.Fragment key={index}>
                    <ListItem disableGutters sx={{ py: 2 }}>
                      <ListItemIcon sx={{ minWidth: 40 }}>
                        {info.icon}
                      </ListItemIcon>
                      <ListItemText
                        primary={
                          <Typography variant="body2" color="text.secondary">
                            {info.label}
                          </Typography>
                        }
                        secondary={
                          <Typography variant="body1" sx={{ fontWeight: 500 }}>
                            {info.value}
                            {info.label === 'Account Status' && user?.enabled && (
                              <Verified color="success" sx={{ ml: 1, fontSize: 16 }} />
                            )}
                          </Typography>
                        }
                      />
                    </ListItem>
                    {index < userInfo.length - 1 && <Divider />}
                  </React.Fragment>
                ))}
              </List>
            </CardContent>
          </Card>
        </Grid>

        {/* Account Summary */}
        <Grid item xs={12} md={4}>
          <Card>
            <CardContent sx={{ p: 3 }}>
              <Typography variant="h6" gutterBottom sx={{ fontWeight: 600 }}>
                Account Summary
              </Typography>

              <Box sx={{ mb: 2 }}>
                <Typography variant="body2" color="text.secondary" gutterBottom>
                  Account Type
                </Typography>
                <Chip
                  label={isAdmin() ? 'Administrator' : 'Customer'}
                  color={isAdmin() ? 'error' : 'primary'}
                  variant="outlined"
                />
              </Box>

              <Box sx={{ mb: 2 }}>
                <Typography variant="body2" color="text.secondary" gutterBottom>
                  Roles
                </Typography>
                <Box display="flex" flexWrap="wrap" gap={1}>
                  {user?.roles?.map((role) => (
                    <Chip
                      key={role.id}
                      label={role.name}
                      size="small"
                      color="secondary"
                    />
                  ))}
                </Box>
              </Box>

              <Divider sx={{ my: 2 }} />

              <Paper sx={{ p: 2, backgroundColor: 'grey.50' }}>
                <Typography variant="caption" color="text.secondary" display="block" gutterBottom>
                  Quick Stats
                </Typography>
                <Typography variant="body2">
                  • Total Orders: 0
                </Typography>
                <Typography variant="body2">
                  • Total Spent: ₹0.00
                </Typography>
                <Typography variant="body2">
                  • Favorite Items: 0
                </Typography>
              </Paper>
            </CardContent>
          </Card>
        </Grid>
      </Grid>
    </Box>
  );
};

export default Profile;