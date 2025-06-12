import React, { useState } from 'react';
import { TextField, Button, Typography, Container, Box, MenuItem, Select, InputLabel, FormControl } from '@mui/material';

function CreateBidPage() {
  const [placeLink, setPlaceLink] = useState<string>('');
  const [desiredRank, setDesiredRank] = useState<number>(1);
  const [keywords, setKeywords] = useState<string>('');

  const handleSubmit = (event: React.FormEvent) => {
    event.preventDefault();
    console.log({
      placeLink,
      desiredRank,
      keywords,
    });
    // TODO: 여기에 API 호출 로직을 추가합니다.
  };

  return (
    <Container maxWidth="sm">
      <Box sx={{ mt: 8, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
        <Typography component="h1" variant="h5">
          입찰 요청 등록
        </Typography>
        <Box component="form" noValidate sx={{ mt: 3 }} onSubmit={handleSubmit}>
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
          />
          <Button
            type="submit"
            fullWidth
            variant="contained"
            sx={{ mt: 3, mb: 2 }}
          >
            입찰 요청 등록
          </Button>
        </Box>
      </Box>
    </Container>
  );
}

export default CreateBidPage; 