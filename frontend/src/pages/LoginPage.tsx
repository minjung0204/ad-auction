import React, { useState } from 'react';
import { TextField, Button, Typography, Container, Box, CircularProgress, Alert } from '@mui/material';
import axios from 'axios';
import api from '../utils/api';
import { useNavigate } from 'react-router-dom';

interface LoginPageProps {
  setIsLoggedIn: (isLoggedIn: boolean) => void;
  setUserType: (userType: string | null) => void;
}

function LoginPage({ setIsLoggedIn, setUserType }: LoginPageProps) {
  const [email, setEmail] = useState<string>('');
  const [password, setPassword] = useState<string>('');
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    setLoading(true);
    setError(null);

    try {
      const response = await api.post('/users/login', {
        email,
        password,
      });
      // 로그인 성공 시 JWT 토큰 저장 (예: localStorage)
      localStorage.setItem('accessToken', response.data.accessToken);
      localStorage.setItem('refreshToken', response.data.refreshToken);
      localStorage.setItem('userType', response.data.user.userType);
      setIsLoggedIn(true); // 로그인 상태 업데이트
      setUserType(response.data.user.userType); // 사용자 타입 업데이트
      alert('로그인 성공!');
      navigate('/'); // 로그인 성공 시 홈 페이지로 이동
    } catch (err) {
      if (axios.isAxiosError(err) && err.response) {
        setError(err.response.data.message || '로그인 실패.');
      } else {
        setError('알 수 없는 오류가 발생했습니다.');
      }
      console.error('로그인 오류:', err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container maxWidth="xs">
      <Box sx={{ mt: 8, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
        <Typography component="h1" variant="h5">
          로그인
        </Typography>
        <Box component="form" noValidate sx={{ mt: 1 }} onSubmit={handleSubmit}>
          {error && <Alert severity="error" sx={{ width: '100%', mb: 2 }}>{error}</Alert>}
          <TextField
            margin="normal"
            required
            fullWidth
            id="email"
            label="이메일 주소"
            name="email"
            autoComplete="email"
            autoFocus
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          <TextField
            margin="normal"
            required
            fullWidth
            name="password"
            label="비밀번호"
            type="password"
            id="password"
            autoComplete="current-password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
          <Button
            type="submit"
            fullWidth
            variant="contained"
            sx={{ mt: 3, mb: 2 }}
            disabled={loading}
          >
            {loading ? <CircularProgress size={24} color="inherit" /> : '로그인'}
          </Button>
          <Box sx={{ mt: 2, width: '100%', textAlign: 'center' }}>
            <Typography variant="body2" color="textSecondary">또는 소셜 계정으로 로그인</Typography>
            <Button
              fullWidth
              variant="outlined"
              sx={{ mt: 2 }}
              onClick={() => alert('네이버 로그인 기능 준비 중')}
            >
              네이버로 로그인
            </Button>
            <Button
              fullWidth
              variant="outlined"
              sx={{ mt: 1 }}
              onClick={() => alert('카카오 로그인 기능 준비 중')}
            >
              카카오로 로그인
            </Button>
          </Box>
        </Box>
      </Box>
    </Container>
  );
}

export default LoginPage; 