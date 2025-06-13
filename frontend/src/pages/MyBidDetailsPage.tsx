import React, { useEffect, useState } from 'react';
import { Typography, Container, Box, CircularProgress, Alert, Paper, Grid } from '@mui/material';
import api from '../utils/api';
import axios from 'axios';
import { useParams } from 'react-router-dom';

interface BidDetails {
  id: string;
  title: string;
  description: string;
  budget: number;
  deadline: string;
  status: string;
  // 다른 필요한 필드들을 여기에 추가
}

function MyBidDetailsPage() {
  const { bidId } = useParams<{ bidId: string }>();
  const [bid, setBid] = useState<BidDetails | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (bidId) {
      const fetchBidDetails = async () => {
        try {
          const response = await api.get(`/advertisers/bids/${bidId}`);
          setBid(response.data);
        } catch (err) {
          if (axios.isAxiosError(err) && err.response) {
            setError(err.response.data.message || '입찰 상세 정보를 불러오는 데 실패했습니다.');
          } else {
            setError('알 수 없는 오류가 발생했습니다.');
          }
          console.error('입찰 상세 정보 불러오기 오류:', err);
        } finally {
          setLoading(false);
        }
      };
      fetchBidDetails();
    }
  }, [bidId]);

  if (loading) {
    return (
      <Container maxWidth="md" sx={{ mt: 8, textAlign: 'center' }}>
        <CircularProgress />
        <Typography>입찰 상세 정보를 불러오는 중...</Typography>
      </Container>
    );
  }

  if (error) {
    return (
      <Container maxWidth="md" sx={{ mt: 8 }}>
        <Alert severity="error">{error}</Alert>
      </Container>
    );
  }

  if (!bid) {
    return (
      <Container maxWidth="md" sx={{ mt: 8 }}>
        <Alert severity="info">입찰 정보를 찾을 수 없습니다.</Alert>
      </Container>
    );
  }

  return (
    <Container maxWidth="md">
      <Box sx={{ mt: 8, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
        <Typography component="h1" variant="h5" gutterBottom>
          입찰 상세 정보
        </Typography>
        <Paper elevation={3} sx={{ p: 4, width: '100%' }}>
          <Grid container spacing={2}>
            <Grid item xs={12}>
              <Typography variant="h6">제목: {bid.title}</Typography>
            </Grid>
            <Grid item xs={12}>
              <Typography variant="body1">설명: {bid.description}</Typography>
            </Grid>
            <Grid item xs={12}>
              <Typography variant="body1">예산: {bid.budget}원</Typography>
            </Grid>
            <Grid item xs={12}>
              <Typography variant="body1">마감일: {new Date(bid.deadline).toLocaleDateString()}</Typography>
            </Grid>
            <Grid item xs={12}>
              <Typography variant="body1">상태: {bid.status}</Typography>
            </Grid>
            {/* 여기에 추가적인 입찰 상세 정보를 표시할 수 있습니다 */}
          </Grid>
        </Paper>
      </Box>
    </Container>
  );
}

export default MyBidDetailsPage; 