import React, { useState } from 'react';
import { TextField, Button, Typography, Container, Box, Alert, CircularProgress, FormControl, InputLabel, Select, MenuItem } from '@mui/material';
import api from '../utils/api';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function RegisterPage() {
  const [email, setEmail] = useState<string>('');
  const [password, setPassword] = useState<string>('');
  const [confirmPassword, setConfirmPassword] = useState<string>('');
  const [userType, setUserType] = useState<'advertiser' | 'agency' | ''>('');
  const [name, setName] = useState<string>(''); // 이름 (개인명 또는 담당자명)
  const [companyName, setCompanyName] = useState<string>(''); // 회사명 (백엔드 요구사항)
  const [registrationNumber, setRegistrationNumber] = useState<string>(''); // 사업자등록번호 또는 생년월일
  const [phoneNumber, setPhoneNumber] = useState<string>(''); // 전화번호
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState<string | null>(null);
  const navigate = useNavigate();

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    setLoading(true);
    setError(null);
    setSuccess(null);

    if (password !== confirmPassword) {
      setError('비밀번호가 일치하지 않습니다.');
      setLoading(false);
      return;
    }
    if (!email || !password || !userType || !name || !companyName || !registrationNumber || !phoneNumber) {
        setError('모든 필드를 입력해주세요.');
        setLoading(false);
        return;
    }

    try {
      const response = await api.post('/users/register', {
        email,
        password,
        userType: userType.toUpperCase(),
        name,
        companyName, // 다시 추가
        registrationNumber,
        phoneNumber,
      });
      setSuccess('회원가입이 성공적으로 완료되었습니다!');
      console.log('회원가입 성공:', response.data);
      setTimeout(() => {
        navigate('/login');
      }, 2000);
    } catch (err) {
      if (axios.isAxiosError(err) && err.response) {
        setError(err.response.data.message || '회원가입 실패.');
      } else {
        setError('알 수 없는 오류가 발생했습니다.');
      }
      console.error('회원가입 오류:', err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container maxWidth="xs">
      <Box sx={{ mt: 8, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
        <Typography component="h1" variant="h5">
          회원가입
        </Typography>
        <Box component="form" noValidate sx={{ mt: 3 }} onSubmit={handleSubmit}>
          {error && <Alert severity="error" sx={{ width: '100%', mb: 2 }}>{error}</Alert>}
          {success && <Alert severity="success" sx={{ width: '100%', mb: 2 }}>{success}</Alert>}

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
            error={!!error && !email}
            helperText={!!error && !email ? '이메일을 입력해주세요.' : ''}
          />
          <TextField
            margin="normal"
            required
            fullWidth
            name="password"
            label="비밀번호"
            type="password"
            id="password"
            autoComplete="new-password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            error={!!error && !password}
            helperText={!!error && !password ? '비밀번호를 입력해주세요.' : ''}
          />
          <TextField
            margin="normal"
            required
            fullWidth
            name="confirmPassword"
            label="비밀번호 확인"
            type="password"
            id="confirmPassword"
            autoComplete="new-password"
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
            error={!!error && password !== confirmPassword}
            helperText={!!error && password !== confirmPassword ? '비밀번호가 일치하지 않습니다.' : ''}
          />
          
          <FormControl fullWidth margin="normal" required error={!!error && !userType}>
            <InputLabel id="user-type-label">회원 유형</InputLabel>
            <Select
              labelId="user-type-label"
              id="userType"
              value={userType}
              label="회원 유형"
              onChange={(e) => setUserType(e.target.value as 'advertiser' | 'agency')}
            >
              <MenuItem value="advertiser">광고주</MenuItem>
              <MenuItem value="agency">대행사</MenuItem>
            </Select>
            {!!error && !userType && <Typography color="error" variant="caption">회원 유형을 선택해주세요.</Typography>}
          </FormControl>

          <TextField
            margin="normal"
            required
            fullWidth
            id="name"
            label="이름 (개인 또는 담당자명)"
            name="name"
            value={name}
            onChange={(e) => setName(e.target.value)}
            error={!!error && !name}
            helperText={!!error && !name ? '이름을 입력해주세요.' : ''}
          />
          <TextField
            margin="normal"
            required
            fullWidth
            id="companyName"
            label="회사명 (상호명)"
            name="companyName"
            value={companyName}
            onChange={(e) => setCompanyName(e.target.value)}
            error={!!error && !companyName}
            helperText={!!error && !companyName ? '회사명을 입력해주세요.' : ''}
          />
          <TextField
            margin="normal"
            required
            fullWidth
            id="registrationNumber"
            label="사업자등록번호 (또는 생년월일)"
            name="registrationNumber"
            value={registrationNumber}
            onChange={(e) => setRegistrationNumber(e.target.value)}
            error={!!error && !registrationNumber}
            helperText={!!error && !registrationNumber ? '사업자등록번호를 입력해주세요.' : ''}
          />
          <TextField
            margin="normal"
            required
            fullWidth
            id="phoneNumber"
            label="전화번호"
            name="phoneNumber"
            value={phoneNumber}
            onChange={(e) => setPhoneNumber(e.target.value)}
            error={!!error && !phoneNumber}
            helperText={!!error && !phoneNumber ? '전화번호를 입력해주세요.' : ''}
          />

          <Button
            type="submit"
            fullWidth
            variant="contained"
            sx={{ mt: 3, mb: 2 }}
            disabled={loading}
          >
            {loading ? <CircularProgress size={24} color="inherit" /> : '회원가입'}
          </Button>
        </Box>
      </Box>
    </Container>
  );
}

export default RegisterPage; 