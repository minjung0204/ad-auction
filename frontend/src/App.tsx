import React, { useState, useEffect } from 'react';
import { Routes, Route, Link, useNavigate } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import HomePage from './pages/HomePage';
import CreateBidPage from './pages/CreateBidPage';
import MyBidsPage from './pages/MyBidsPage';
import MyBidDetailsPage from './pages/MyBidDetailsPage';
import AgencyBidListPage from './pages/AgencyBidListPage';
import AgencyBidDetailsPage from './pages/AgencyBidDetailsPage';
import RegisterPage from './pages/RegisterPage';
import MyProfilePage from './pages/MyProfilePage';
import { Button, AppBar, Toolbar, Typography, Box } from '@mui/material';

function App() {
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [userType, setUserType] = useState<string | null>(null);
    const navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem('accessToken');
        const storedUserType = localStorage.getItem('userType');
        setIsLoggedIn(!!token);
        setUserType(storedUserType);
        console.log('App Init - Access Token:', token ? ' vorhanden' : ' nicht vorhanden');
        console.log('App Init - User Type:', storedUserType);
        console.log('App Init - Is Logged In:', !!token);
    }, []);

    useEffect(() => {
        const handleStorageChange = () => {
            const token = localStorage.getItem('accessToken');
            const storedUserType = localStorage.getItem('userType');
            setIsLoggedIn(!!token);
            setUserType(storedUserType);
            console.log('Storage Change - Access Token:', token ? ' vorhanden' : ' nicht vorhanden');
            console.log('Storage Change - User Type:', storedUserType);
            console.log('Storage Change - Is Logged In:', !!token);
        };
        window.addEventListener('storage', handleStorageChange);
        return () => window.removeEventListener('storage', handleStorageChange);
    }, []);

    const handleLogout = () => {
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        localStorage.removeItem('userType');
        setIsLoggedIn(false);
        setUserType(null);
        navigate('/');
    };

    return (
        <>
        <AppBar position="static">
            <Toolbar>
            <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
                네이버 플레이스 입찰
            </Typography>
            <Button color="inherit" component={Link} to="/">홈</Button>
            {isLoggedIn ? (
                <>
                <Button color="inherit" component={Link} to="/my-profile">내 정보</Button>
                {userType === 'ADVERTISER' && (
                    <>
                    <Button color="inherit" component={Link} to="/create-bid">입찰 요청 등록</Button>
                    <Button color="inherit" component={Link} to="/my-bids">내 입찰 목록</Button>
                    </>
                )}
                {userType === 'AGENCY' && (
                    <>
                    <Button color="inherit" component={Link} to="/agency/bids">대행사 입찰 목록</Button>
                    </>
                )}
                <Button color="inherit" onClick={handleLogout}>로그아웃</Button>
                </>
            ) : (
                <>
                <Button color="inherit" component={Link} to="/login">로그인</Button>
                <Button color="inherit" component={Link} to="/register">회원가입</Button>
                </>
            )}
            </Toolbar>
        </AppBar>
        <Box sx={{ mt: 2 }}>
            <Routes>
            <Route path="/login" element={<LoginPage setIsLoggedIn={setIsLoggedIn} setUserType={setUserType} />} />
            <Route path="/register" element={<RegisterPage />} />
            <Route path="/" element={<HomePage />} />
            <Route path="/my-profile" element={<MyProfilePage />} />
            <Route path="/create-bid" element={<CreateBidPage />} />
            <Route path="/my-bids" element={<MyBidsPage />} />
            <Route path="/my-bids/:bidId" element={<MyBidDetailsPage />} />
            <Route path="/agency/bids" element={<AgencyBidListPage />} />
            <Route path="/agency/bids/:bidId" element={<AgencyBidDetailsPage />} />
            </Routes>
        </Box>
        </>
    );
}

export default App; 