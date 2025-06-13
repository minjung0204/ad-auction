import React, { useEffect, useState } from 'react';
import { Typography, Container, Box, CircularProgress, Alert, Paper, Grid, TextField, Button, List } from '@mui/material';
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
  advertiserCompanyName: string;
  proposals: Proposal[]; // 대행사 제안 목록
}

interface Proposal {
  id: string;
  agencyId: string;
  agencyCompanyName: string;
  proposedAmount: number;
  message: string;
  status: string; // 예: PENDING, ACCEPTED, REJECTED
  createdAt: string;
}

function AgencyBidDetailsPage() {
  const { bidId } = useParams<{ bidId: string }>();
  const [bid, setBid] = useState<BidDetails | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [proposalAmount, setProposalAmount] = useState<number | ''>('');
  const [proposalMessage, setProposalMessage] = useState<string>('');
  const [submittingProposal, setSubmittingProposal] = useState<boolean>(false);
  const [proposalError, setProposalError] = useState<string | null>(null);
  const [proposalSuccess, setProposalSuccess] = useState<string | null>(null);
  const [existingProposal, setExistingProposal] = useState<Proposal | null>(null); // 기존 제안 상태

  const fetchBidDetails = async () => {
    if (bidId) {
      try {
        const response = await api.get(`/agencies/bids/${bidId}`);
        setBid(response.data);
        const currentUserType = localStorage.getItem('userType');
        const currentUserId = localStorage.getItem('userId'); // localStorage에서 userId 가져오기

        if (currentUserType === 'AGENCY' && response.data.proposals) {
          const foundProposal = response.data.proposals.find(
            (prop: Proposal) => prop.agencyId === currentUserId // 대행사 ID 비교
          );
          if (foundProposal) {
            setExistingProposal(foundProposal);
            setProposalAmount(foundProposal.proposedAmount);
            setProposalMessage(foundProposal.message);
          }
        }

      } catch (err) {
        if (axios.isAxiosError(err) && err.response) {
          setError(err.response.data.message || '입찰 상세 정보를 불러오는 데 실패했습니다.');
        } else {
          setError('알 수 없는 오류가 발생했습니다.');
        }
        console.error('대행사 입찰 상세 정보 불러오기 오류:', err);
      } finally {
        setLoading(false);
      }
    }
  };

  useEffect(() => {
    fetchBidDetails();
  }, [bidId]);

  const handleProposalSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    setSubmittingProposal(true);
    setProposalError(null);
    setProposalSuccess(null);

    if (!proposalAmount || !proposalMessage) {
      setProposalError('제안 금액과 메시지를 입력해주세요.');
      setSubmittingProposal(false);
      return;
    }

    try {
      const url = existingProposal
        ? `/agencies/bids/${bidId}/proposals/${existingProposal.id}` // 수정 API
        : `/agencies/bids/${bidId}/proposals`; // 제출 API
      const method = existingProposal ? api.put : api.post; // PUT 또는 POST 선택

      const response = await method(url, {
        proposedAmount: Number(proposalAmount),
        message: proposalMessage,
      });

      setProposalSuccess(
        existingProposal ? '제안이 성공적으로 수정되었습니다!' : '제안이 성공적으로 제출되었습니다!'
      );
      // 수정 후에는 폼 초기화하지 않고 현재 값 유지, 제출 후에는 초기화
      if (!existingProposal) {
        setProposalAmount('');
        setProposalMessage('');
      }
      console.log(existingProposal ? '제안 수정 성공:' : '제안 제출 성공:', response.data);
      fetchBidDetails(); // 제안 제출/수정 후 최신 정보 다시 불러오기
    } catch (err) {
      if (axios.isAxiosError(err) && err.response) {
        setProposalError(err.response.data.message || '제안 제출 실패.');
      } else {
        setProposalError('알 수 없는 오류가 발생했습니다.');
      }
      console.error('제안 제출 오류:', err);
    } finally {
      setSubmittingProposal(false);
    }
  };

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
          입찰 상세 정보 (대행사)
        </Typography>
        <Paper elevation={3} sx={{ p: 4, width: '100%', mb: 4 }}>
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
            <Grid item xs={12}>
              <Typography variant="body1">광고주: {bid.advertiserCompanyName}</Typography>
            </Grid>
          </Grid>
        </Paper>

        <Typography component="h2" variant="h6" gutterBottom sx={{ mt: 2 }}>
          제안 제출
        </Typography>
        <Box component="form" onSubmit={handleProposalSubmit} noValidate sx={{ mt: 1, width: '100%' }}>
          {proposalError && <Alert severity="error" sx={{ width: '100%', mb: 2 }}>{proposalError}</Alert>}
          {proposalSuccess && <Alert severity="success" sx={{ width: '100%', mb: 2 }}>{proposalSuccess}</Alert>}
          <TextField
            margin="normal"
            required
            fullWidth
            id="proposalAmount"
            label="제안 금액"
            name="proposalAmount"
            type="number"
            value={proposalAmount}
            onChange={(e) => setProposalAmount(e.target.value === '' ? '' : Number(e.target.value))}
          />
          <TextField
            margin="normal"
            required
            fullWidth
            id="proposalMessage"
            label="제안 메시지"
            name="proposalMessage"
            multiline
            rows={4}
            value={proposalMessage}
            onChange={(e) => setProposalMessage(e.target.value)}
          />
          <Button
            type="submit"
            fullWidth
            variant="contained"
            sx={{ mt: 3, mb: 2 }}
            disabled={submittingProposal}
          >
            {submittingProposal
              ? <CircularProgress size={24} color="inherit" />
              : existingProposal ? '제안 수정' : '제안 제출'}
          </Button>
        </Box>

        <Typography component="h2" variant="h6" gutterBottom sx={{ mt: 4 }}>
          {existingProposal ? '나의 제안' : '기존 제안'}
        </Typography>
        {bid.proposals.length === 0 ? (
          <Typography>아직 제출된 제안이 없습니다.</Typography>
        ) : (
          <List sx={{ width: '100%', bgcolor: 'background.paper' }}>
            {existingProposal ? (
              <Paper key={existingProposal.id} elevation={2} sx={{ p: 2, mb: 2 }}>
                <Typography variant="subtitle1">제안 금액: {existingProposal.proposedAmount}원</Typography>
                <Typography variant="body2">메시지: {existingProposal.message}</Typography>
                <Typography variant="body2">상태: {existingProposal.status}</Typography>
                <Typography variant="caption" color="text.secondary">
                  제출일: {new Date(existingProposal.createdAt).toLocaleDateString()}
                </Typography>
              </Paper>
            ) : (
              <Typography>아직 제출된 제안이 없습니다.</Typography>
            )}
          </List>
        )}
      </Box>
    </Container>
  );
}

export default AgencyBidDetailsPage; 