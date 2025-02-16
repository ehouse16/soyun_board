import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate, Link } from 'react-router-dom';
import { AppBar, Toolbar, Button, Typography, Container, Box, Paper, TextField } from '@mui/material';
import PostCreate from './packages/PostCreate';
import PostList from './packages/PostList';
import PostDetail from './packages/PostDetail';
import PostEdit from './packages/PostEdit';
import Login from './components/Login';
import Signup from './components/Signup';

function App() {
    const [isAuthenticated, setIsAuthenticated] = useState(false);

    useEffect(() => {
        const token = localStorage.getItem('accessToken');
        setIsAuthenticated(!!token);
    }, []);

    const handleLogout = () => {
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        setIsAuthenticated(false);
    };

    const ProtectedRoute = ({ children }) => {
        return isAuthenticated ? children : <Navigate to="/login" />;
    };

    return (
        <Router>
            <AppBar position="static">
                <Toolbar>
                    <Typography variant="h6" sx={{ flexGrow: 1 }}>게시판 애플리케이션</Typography>
                    {isAuthenticated ? (
                        <>
                            <Button color="inherit" component={Link} to="/posts/create">게시글 작성</Button>
                            <Button color="inherit" component={Link} to="/posts">게시글 목록</Button>
                            <Button color="inherit" onClick={handleLogout}>로그아웃</Button>
                        </>
                    ) : (
                        <>
                            <Button color="inherit" component={Link} to="/login">로그인</Button>
                            <Button color="inherit" component={Link} to="/signup">회원가입</Button>
                        </>
                    )}
                </Toolbar>
            </AppBar>
            <Container sx={{ mt: 4 }}>
                <Routes>
                    <Route path="/" element={<Typography variant="h4" align="center">게시판 홈페이지에 오신 것을 환영합니다</Typography>} />
                    <Route path="/login" element={<Login setIsAuthenticated={setIsAuthenticated} />} />
                    <Route path="/signup" element={<Signup />} />
                    <Route path="/posts/create" element={<ProtectedRoute><PostCreate /></ProtectedRoute>} />
                    <Route path="/posts" element={<PostList />} />
                    <Route path="/posts/:id" element={<PostDetail />} />
                    <Route path="/posts/edit/:id" element={<ProtectedRoute><PostEdit /></ProtectedRoute>} />
                </Routes>
            </Container>
        </Router>
    );
}

export default App;
