import React, { useState } from 'react';
import { TextField, Button, Typography, Container, Box, MenuItem, Select, InputLabel, FormControl, CircularProgress, Alert } from '@mui/material';
import api from '../utils/api';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function CreateBidPage() {
  const [placeLink, setPlaceLink] = useState<string>('');
  const [desiredRank, setDesiredRank] = useState<number>(1);
  const [keywords, setKeywords] = useState<string>('');
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState<string | null>(null);
  const navigate = useNavigate();

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    setLoading(true);
    setError(null);
    setSuccess(null);

    if (!placeLink || !keywords) {
      setError('모든 필드를 입력해주세요.');
      setLoading(false);
      return;
    }

    // 플레이스 링크 유효성 검증 (모바일 네이버 플레이스 URL 형식)
    if (!placeLink.startsWith('https://m.place.naver.com/')) {
      setError('유효한 모바일 네이버 플레이스 링크를 입력해주세요. (예: https://m.place.naver.com/...)');
      setLoading(false);
      return;
    }

    try {
      const response = await api.post('/advertisers/bids', {
        placeLink,
        desiredRank,
        keywords,
      });
      setSuccess('입찰 요청이 성공적으로 등록되었습니다!');
      console.log('입찰 등록 성공:', response.data);
      setTimeout(() => {
        navigate('/'); // 성공 시 홈 페이지로 이동
      }, 2000); // 2초 후 이동
    } catch (err) {
      if (axios.isAxiosError(err) && err.response) {
        setError(err.response.data.message || '입찰 등록 실패.');
      } else {
        setError('알 수 없는 오류가 발생했습니다.');
      }
      console.error('입찰 등록 오류:', err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container maxWidth="sm">
      <Box sx={{ mt: 8, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
        <Typography component="h1" variant="h5">
          입찰 요청 등록
        </Typography>
        <Box component="form" noValidate sx={{ mt: 3 }} onSubmit={handleSubmit}>
          {error && <Alert severity="error" sx={{ width: '100%', mb: 2 }}>{error}</Alert>}
          {success && <Alert severity="success" sx={{ width: '100%', mb: 2 }}>{success}</Alert>}
          <TextField
            margin="normal"
            required
            fullWidth
            id="placeLink"
            label="플레이스 링크"
            name="placeLink"
            autoFocus
            value={placeLink}
            onChange={(e) => setPlaceLink(e.target.value)}
            error={!!error && (!placeLink || !placeLink.startsWith('https://m.place.naver.com/'))}
            helperText={
              !!error && !placeLink
                ? '플레이스 링크를 입력해주세요.'
                : !!error && !placeLink.startsWith('https://m.place.naver.com/')
                ? '유효한 모바일 네이버 플레이스 링크를 입력해주세요.'
                : ''
            }
          />
          <FormControl fullWidth margin="normal" required>
            <InputLabel id="desired-rank-label">희망 노출 순위</InputLabel>
            <Select
              labelId="desired-rank-label"
              id="desiredRank"
              name="desiredRank"
              label="희망 노출 순위"
              value={desiredRank}
              onChange={(e) => setDesiredRank(e.target.value as number)}
            >
              <MenuItem value={1}>1위</MenuItem>
              <MenuItem value={2}>2위</MenuItem>
              <MenuItem value={3}>3위</MenuItem>
              <MenuItem value={4}>4위</MenuItem>
              <MenuItem value={5}>5위</MenuItem>
            </Select>
          </FormControl>
          <TextField
            margin="normal"
            required
            fullWidth
            id="keywords"
            label="키워드 (쉼표로 구분)"
            name="keywords"
            multiline
            rows={3}
            value={keywords}
            onChange={(e) => setKeywords(e.target.value)}
            error={!!error && !keywords}
            helperText={!!error && !keywords ? '키워드를 입력해주세요.' : ''}
          />
          <Button
            type="submit"
            fullWidth
            variant="contained"
            sx={{ mt: 3, mb: 2 }}
            disabled={loading}
          >
            {loading ? <CircularProgress size={24} color="inherit" /> : '입찰 요청 등록'}
          </Button>
        </Box>
      </Box>
    </Container>
  );
}

export default CreateBidPage; 