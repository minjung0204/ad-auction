import React, { useEffect, useState } from 'react';
import { Typography, Container, Box, CircularProgress, Alert, List, ListItem, ListItemText, Button } from '@mui/material';
import api from '../utils/api';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

interface Bid {
  id: string;
  title: string;
  description: string;
  budget: number;
  deadline: string;
  status: string;
  advertiserCompanyName: string;
}

function AgencyBidListPage() {
  const [bids, setBids] = useState<Bid[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchBids = async () => {
      try {
        const response = await api.get('/agencies/bids');
        setBids(response.data);
      } catch (err) {
        if (axios.isAxiosError(err) && err.response) {
          setError(err.response.data.message || '입찰 목록을 불러오는 데 실패했습니다.');
        } else {
          setError('알 수 없는 오류가 발생했습니다.');
        }
        console.error('대행사 입찰 목록 불러오기 오류:', err);
      } finally {
        setLoading(false);
      }
    };

    fetchBids();
  }, []);

  const handleBidClick = (bidId: string) => {
    navigate(`/agency/bids/${bidId}`);
  };

  if (loading) {
    return (
      <Container maxWidth="md" sx={{ mt: 8, textAlign: 'center' }}>
        <CircularProgress />
        <Typography>입찰 목록을 불러오는 중...</Typography>
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

  return (
    <Container maxWidth="md">
      <Box sx={{ mt: 8, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
        <Typography component="h1" variant="h5" gutterBottom>
          대행사 입찰 목록
        </Typography>
        {bids.length === 0 ? (
          <Typography>등록된 입찰 요청이 없습니다.</Typography>
        ) : (
          <List sx={{ width: '100%', bgcolor: 'background.paper' }}>
            {bids.map((bid) => (
              <ListItem
                key={bid.id}
                secondaryAction={
                  <Button variant="outlined" onClick={() => handleBidClick(bid.id)}>
                    상세보기
                  </Button>
                }
              >
                <ListItemText
                  primary={`제목: ${bid.title}`}
                  secondary={
                    <React.Fragment>
                      <Typography
                        sx={{ display: 'inline' }}
                        component="span"
                        variant="body2"
                        color="text.primary"
                      >
                        예산: {bid.budget}원
                      </Typography>
                      {` — 마감일: ${new Date(bid.deadline).toLocaleDateString()} / 상태: ${bid.status} / 광고주: 익명`}
                    </React.Fragment>
                  }
                />
              </ListItem>
            ))}
          </List>
        )}
      </Box>
    </Container>
  );
}

export default AgencyBidListPage; 