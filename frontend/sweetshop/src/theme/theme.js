import { createTheme } from '@mui/material/styles';

const theme = createTheme({
  palette: {
    mode: 'light',
    primary: {
      main: '#f8bbd9', // Pastel pink
      light: '#fce4ec',
      dark: '#f48fb1',
      contrastText: '#4a4a4a',
    },
    secondary: {
      main: '#8d6e63', // Deep brown
      light: '#bcaaa4',
      dark: '#5d4037',
      contrastText: '#ffffff',
    },
    background: {
      default: '#fef7f0', // Cream background
      paper: '#ffffff',
    },
    text: {
      primary: '#3e2723', // Deep brown for text
      secondary: '#6d4c41',
    },
    success: {
      main: '#81c784',
    },
    error: {
      main: '#e57373',
    },
    warning: {
      main: '#ffb74d',
    },
    info: {
      main: '#64b5f6',
    },
  },
  typography: {
    fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
    h1: {
      fontSize: '2.5rem',
      fontWeight: 500,
      color: '#3e2723',
    },
    h2: {
      fontSize: '2rem',
      fontWeight: 500,
      color: '#3e2723',
    },
    h3: {
      fontSize: '1.75rem',
      fontWeight: 500,
      color: '#3e2723',
    },
    h4: {
      fontSize: '1.5rem',
      fontWeight: 500,
      color: '#3e2723',
    },
    h5: {
      fontSize: '1.25rem',
      fontWeight: 500,
      color: '#3e2723',
    },
    h6: {
      fontSize: '1rem',
      fontWeight: 500,
      color: '#3e2723',
    },
  },
  components: {
    MuiButton: {
      styleOverrides: {
        root: {
          borderRadius: 8,
          textTransform: 'none',
          fontWeight: 500,
        },
        contained: {
          boxShadow: '0 2px 8px rgba(248, 187, 217, 0.3)',
          '&:hover': {
            boxShadow: '0 4px 12px rgba(248, 187, 217, 0.4)',
          },
        },
      },
    },
    MuiCard: {
      styleOverrides: {
        root: {
          borderRadius: 12,
          boxShadow: '0 2px 12px rgba(0,0,0,0.08)',
        },
      },
    },
    MuiAppBar: {
      styleOverrides: {
        root: {
          backgroundColor: '#ffffff',
          color: '#3e2723',
          boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
        },
      },
    },
  },
});

export default theme;
