import React, { useState } from 'react';
import { TextField, Button, Container, Typography, Paper, Box } from '@mui/material';

const Signup = () => {
    const [formData, setFormData] = useState({
        email: '',
        password: '',
        name: '',
    });

    const handleSignup = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch(
                //'http://13.124.180.241:8080/api/auth/signup', {
                'http://localhost:8080/api/auth/signup', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(formData),
            });

            if (!response.ok) {
                throw new Error('Signup failed');
            }

            alert("회원가입 성공! 로그인 페이지로 이동합니다.");
            window.location.href = '/login';
        } catch (err) {
            alert("회원가입 실패! 다시 시도해 주세요.");
        }
    };

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value,
        });
    };

    return (
        <Container component="main" maxWidth="xs">
            <Paper elevation={6} sx={{ padding: 4, marginTop: 8, borderRadius: 2 }}>
                <Typography variant="h5" align="center" gutterBottom>
                    회원가입
                </Typography>
                <Box component="form" onSubmit={handleSignup} sx={{ mt: 3 }}>
                    <TextField
                        fullWidth
                        margin="normal"
                        label="이메일"
                        name="email"
                        type="email"
                        required
                        variant="outlined"
                        onChange={handleChange}
                    />
                    <TextField
                        fullWidth
                        margin="normal"
                        label="비밀번호"
                        name="password"
                        type="password"
                        required
                        variant="outlined"
                        onChange={handleChange}
                    />
                    <TextField
                        fullWidth
                        margin="normal"
                        label="이름"
                        name="name"
                        type="text"
                        required
                        variant="outlined"
                        onChange={handleChange}
                    />
                    <Button
                        type="submit"
                        fullWidth
                        variant="contained"
                        color="primary"
                        sx={{ mt: 3, mb: 2 }}
                    >
                        회원가입
                    </Button>
                </Box>
            </Paper>
        </Container>
    );
};

export default Signup;
