import React, { createContext, useContext, useState, useEffect } from 'react';
import axios from 'axios';

const AuthContext = createContext();

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [token, setToken] = useState(() => localStorage.getItem('accessToken'));

  useEffect(() => {
    const initAuth = async () => {
      const storedToken = localStorage.getItem('accessToken');
      if (storedToken) {
        try {
          // Set default authorization header
          axios.defaults.headers.common['Authorization'] = `Bearer ${storedToken}`;
          
          // Get user profile
          const response = await axios.get('/api/users/profile');
          setUser(response.data.data);
          setToken(storedToken);
        } catch (error) {
          console.error('Token validation failed:', error);
          logout();
        }
      }
      setLoading(false);
    };

    initAuth();
  }, []);

  useEffect(() => {
  if (user) {
    console.log("Logged-in user:", user);
  }
}, [user]);

  const login = async (email, password) => {
    try {
      const response = await axios.post('/api/auth/login', { email, password });
      const { accessToken, refreshToken } = response.data.data;
      
      localStorage.setItem('accessToken', accessToken);
      localStorage.setItem('refreshToken', refreshToken);
      
      axios.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`;
      setToken(accessToken);
      
      // Get user profile
      const profileResponse = await axios.get('/api/users/profile');
      setUser(profileResponse.data.data);
      
      return { success: true };
    } catch (error) {
      console.error('Login error:', error);
      return { 
        success: false, 
        message: error.response?.data?.message || 'Login failed' 
      };
    }
  };

  const register = async (userData) => {
    try {
      await axios.post('/api/auth/register', userData);
      return { success: true };
    } catch (error) {
      console.error('Registration error:', error);
      return { 
        success: false, 
        message: error.response?.data?.message || 'Registration failed' 
      };
    }
  };

  const logout = () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    delete axios.defaults.headers.common['Authorization'];
    setUser(null);
    setToken(null);
  };

  const isAdmin = () => {
    return user?.roles?.some(role => role.name === 'ADMIN') || false;
  };

  const value = {
    user,
    token,
    loading,
    login,
    register,
    logout,
    isAdmin,
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};

