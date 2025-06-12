import React from 'react';
import { Typography, Container, Box } from '@mui/material';

function HomePage() {
  return (
    <Container maxWidth="md">
      <Box sx={{ mt: 8, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
        <Typography component="h1" variant="h4">
          환영합니다!
        </Typography>
        <Typography variant="body1" sx={{ mt: 2 }}>
          네이버 플레이스 상위노출 입찰 사이트에 오신 것을 환영합니다.
        </Typography>
      </Box>
    </Container>
  );
}

export default HomePage; 