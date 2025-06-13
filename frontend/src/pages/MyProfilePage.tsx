import React, { useState, useEffect } from 'react';
import { Box, TextField, Button, Typography, Paper, Alert } from '@mui/material';
import api from '../utils/api';
import axios, { AxiosError } from 'axios';
import { useNavigate } from 'react-router-dom';

interface UserProfile {
  id: number;
  email: string;
  name: string;
  socialType: string;
  userType: string;
  phoneNumber: string;
  companyName: string;
  registrationNumber: string;
  accountNumber: string | null;
  bankName: string | null;
}

const MyProfilePage: React.FC = () => {
  const [profile, setProfile] = useState<UserProfile | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState<string | null>(null);
  const [isEditing, setIsEditing] = useState<boolean>(false);
  const [currentPassword, setCurrentPassword] = useState<string>('');
  const [newPassword, setNewPassword] = useState<string>('');
  const navigate = useNavigate();

  // 전화번호 자동 하이픈 추가 유틸리티 함수
  const formatPhoneNumber = (value: string) => {
    if (!value) return value;
    const digitsOnly = value.replace(/\D/g, ''); // 숫자만 추출
    if (digitsOnly.length <= 3) return digitsOnly;
    if (digitsOnly.length <= 7) return `${digitsOnly.substring(0, 3)}-${digitsOnly.substring(3)}`;
    if (digitsOnly.length <= 11) return `${digitsOnly.substring(0, 3)}-${digitsOnly.substring(3, 7)}-${digitsOnly.substring(7)}`;
    return `${digitsOnly.substring(0, 3)}-${digitsOnly.substring(3, 7)}-${digitsOnly.substring(7, 11)}`;
  };

  useEffect(() => {
    // 사용자 정보를 가져오는 함수
    const fetchUserProfile = async () => {
      try {
        setLoading(true);
        setError(null);
        setSuccess(null);
        const response = await api.get<UserProfile>('/users/me');
        // 전화번호를 받아올 때 포맷 적용
        setProfile({
          ...response.data,
          phoneNumber: formatPhoneNumber(response.data.phoneNumber),
        });
      } catch (err) {
        if (axios.isAxiosError(err)) {
          setError(err.response?.data?.message || '사용자 정보를 불러오는 데 실패했습니다.');
        } else {
          setError('알 수 없는 오류가 발생했습니다.');
        }
      } finally {
        setLoading(false);
      }
    };

    fetchUserProfile();
  }, []);

  // 입력 필드 변경 핸들러
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    if (profile) {
      if (name === 'phoneNumber') {
        setProfile({ ...profile, [name]: formatPhoneNumber(value) }); // 전화번호 입력 시 포맷 적용
      } else {
        setProfile({ ...profile, [name]: value });
      }
    }
  };

  // 정보 수정 제출 핸들러
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!profile) return;

    try {
      setLoading(true);
      setError(null);
      setSuccess(null);

      const updatePayload: any = {
        name: profile.name,
        phoneNumber: profile.phoneNumber,
        companyName: profile.companyName,
        registrationNumber: profile.registrationNumber,
        accountNumber: profile.accountNumber === '' ? null : profile.accountNumber,
        bankName: profile.bankName === '' ? null : profile.bankName,
      };

      if (newPassword) {
        if (!currentPassword) {
          setError('새 비밀번호를 설정하려면 현재 비밀번호를 입력해야 합니다.');
          setLoading(false);
          return;
        }
        updatePayload.currentPassword = currentPassword;
        updatePayload.newPassword = newPassword;
      }

      console.log('Update Payload:', updatePayload); // 전송될 페이로드 로깅

      await api.put('/users/me', updatePayload);
      setSuccess('사용자 정보가 성공적으로 업데이트되었습니다.');
      setIsEditing(false);
      setCurrentPassword('');
      setNewPassword('');
    } catch (err) {
      if (axios.isAxiosError(err)) {
        // 백엔드에서 전달된 구체적인 오류 메시지 파싱 시도 (예: err.response.data.errors)
        const errorMessage = err.response?.data?.message || '사용자 정보 업데이트에 실패했습니다.';
        setError(errorMessage);
      } else {
        setError('알 수 없는 오류가 발생했습니다.');
      }
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <Box sx={{ p: 3 }}>
        <Typography variant="h5">내 정보</Typography>
        <Typography>로딩 중...</Typography>
      </Box>
    );
  }

  if (error && !profile) { // 초기 로딩 시 에러가 나면 메시지 표시
    return (
      <Box sx={{ p: 3 }}>
        <Typography variant="h5">내 정보</Typography>
        <Alert severity="error">{error}</Alert>
        <Button variant="contained" onClick={() => navigate('/login')} sx={{ mt: 2 }}>
          로그인 페이지로 이동
        </Button>
      </Box>
    );
  }

  return (
    <Box sx={{ p: 3, maxWidth: 600, mx: 'auto' }}>
      <Typography variant="h4" component="h1" gutterBottom>
        내 정보
      </Typography>

      {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
      {success && <Alert severity="success" sx={{ mb: 2 }}>{success}</Alert>}

      <Paper elevation={3} sx={{ p: 3 }}>
        {!isEditing ? (
          <Box>
            <Typography variant="h6">ID: {profile?.id}</Typography>
            <Typography variant="h6">이메일: {profile?.email}</Typography>
            <Typography variant="h6">이름: {profile?.name}</Typography>
            <Typography variant="h6">소셜 타입: {profile?.socialType || 'N/A'}</Typography>
            <Typography variant="h6">사용자 타입: {profile?.userType || 'N/A'}</Typography>
            <Typography variant="h6">전화번호: {profile?.phoneNumber}</Typography>
            <Typography variant="h6">회사명: {profile?.companyName}</Typography>
            <Typography variant="h6">사업자등록번호: {profile?.registrationNumber}</Typography>
            <Typography variant="h6">계좌번호: {profile?.accountNumber || 'N/A'}</Typography>
            <Typography variant="h6">은행명: {profile?.bankName || 'N/A'}</Typography>
            <Button variant="contained" onClick={() => setIsEditing(true)} sx={{ mt: 2 }}>
              정보 수정
            </Button>
          </Box>
        ) : (
          <form onSubmit={handleSubmit}>
            <TextField
              label="ID"
              name="id"
              value={profile?.id || ''}
              fullWidth
              margin="normal"
              disabled
            />
            <TextField
              label="이메일"
              name="email"
              value={profile?.email || ''}
              onChange={handleChange}
              fullWidth
              margin="normal"
              disabled
            />
            <TextField
              label="이름"
              name="name"
              value={profile?.name || ''}
              onChange={handleChange}
              fullWidth
              margin="normal"
            />
            <TextField
              label="전화번호"
              name="phoneNumber"
              value={profile?.phoneNumber || ''}
              onChange={handleChange}
              fullWidth
              margin="normal"
              // TODO: 전화번호 형식 유효성 검사 힌트 추가
            />
            <TextField
              label="회사명"
              name="companyName"
              value={profile?.companyName || ''}
              onChange={handleChange}
              fullWidth
              margin="normal"
            />
            <TextField
              label="사업자등록번호"
              name="registrationNumber"
              value={profile?.registrationNumber || ''}
              onChange={handleChange}
              fullWidth
              margin="normal"
              // TODO: 사업자등록번호 10자리 유효성 검사 힌트 추가
            />
            <TextField
              label="계좌번호"
              name="accountNumber"
              value={profile?.accountNumber || ''}
              onChange={handleChange}
              fullWidth
              margin="normal"
            />
            <TextField
              label="은행명"
              name="bankName"
              value={profile?.bankName || ''}
              onChange={handleChange}
              fullWidth
              margin="normal"
            />
            <Typography variant="h6" sx={{ mt: 3, mb: 1 }}>비밀번호 변경 (선택 사항)</Typography>
            <TextField
              label="현재 비밀번호"
              type="password"
              name="currentPassword"
              value={currentPassword}
              onChange={(e) => setCurrentPassword(e.target.value)}
              fullWidth
              margin="normal"
            />
            <TextField
              label="새 비밀번호"
              type="password"
              name="newPassword"
              value={newPassword}
              onChange={(e) => setNewPassword(e.target.value)}
              fullWidth
              margin="normal"
            />
            <Box sx={{ mt: 2, display: 'flex', gap: 1 }}>
              <Button type="submit" variant="contained" color="primary" disabled={loading}>
                수정 완료
              </Button>
              <Button type="button" variant="outlined" onClick={() => setIsEditing(false)} disabled={loading}>
                취소
              </Button>
            </Box>
          </form>
        )}
      </Paper>
    </Box>
  );
};

export default MyProfilePage; 