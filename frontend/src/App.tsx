import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import HomePage from './pages/HomePage';
import CreateBidPage from './pages/CreateBidPage';
import { Button, AppBar, Toolbar, Typography, Box } from '@mui/material';

function App() {
  return (
    <Router>
      <AppBar position="static">
        <Toolbar>
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            네이버 플레이스 입찰
          </Typography>
          <Button color="inherit" component={Link} to="/">홈</Button>
          <Button color="inherit" component={Link} to="/login">로그인</Button>
          <Button color="inherit" component={Link} to="/create-bid">입찰 요청 등록</Button>
        </Toolbar>
      </AppBar>
      <Box sx={{ mt: 2 }}>
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/" element={<HomePage />} />
          <Route path="/create-bid" element={<CreateBidPage />} />
        </Routes>
      </Box>
    </Router>
  );
}

export default App; 