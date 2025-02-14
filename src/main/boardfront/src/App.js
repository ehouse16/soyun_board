import {BrowserRouter as Router, Routes, Route, Link, Navigate} from "react-router-dom";
import PostCreate from "./packages/PostCreate";
import PostList from "./packages/PostList";
import PostDetail from "./packages/PostDetail";
import PostEdit from "./packages/PostEdit";
import Login from "./components/Login";
import Signup from "./components/Signup";
import { useState, useEffect } from "react";

function App() {
    const [isAuthenticated, setIsAuthenticated] = useState(false);

    // Check if user is authenticated on component mount
    useEffect(() => {
        const token = localStorage.getItem('accessToken');
        setIsAuthenticated(!!token);
    }, []);

    // Helper function to create protected routes
    const ProtectedRoute = ({ children }) => {
        return isAuthenticated ? children : <Navigate to="/login" />;
    };

    return (
        <Router>
            <div className="App">
                <nav className="app-nav">
                    <h1>게시판 애플리케이션</h1>
                    <div className="nav-links">
                        {isAuthenticated ? (
                            <>
                                <Link to="/posts/create">
                                    <button>게시글 작성하기</button>
                                </Link>
                                <Link to="/posts">
                                    <button>게시글 목록 보기</button>
                                </Link>
                                <button onClick={() => {
                                    localStorage.removeItem('accessToken');
                                    localStorage.removeItem('refreshToken');
                                    setIsAuthenticated(false);
                                }}>로그아웃</button>
                            </>
                        ) : (
                            <>
                                <Link to="/login">
                                    <button>로그인</button>
                                </Link>
                                <Link to="/signup">
                                    <button>회원가입</button>
                                </Link>
                            </>
                        )}
                    </div>
                </nav>

                <Routes>
                    {/* Public routes */}
                    <Route path="/" element={<div className="home-page">게시판 홈페이지에 오신 것을 환영합니다</div>} />
                    <Route path="/login" element={<Login setIsAuthenticated={setIsAuthenticated} />} />
                    <Route path="/signup" element={<Signup />} />

                    {/* Protected routes */}
                    <Route path="/posts/create" element={
                        <ProtectedRoute>
                            <PostCreate />
                        </ProtectedRoute>
                    } />
                    <Route path="/posts" element={<PostList />} />
                    <Route path="/posts/:id" element={<PostDetail />} />
                    <Route path="/posts/edit/:id" element={
                        <ProtectedRoute>
                            <PostEdit />
                        </ProtectedRoute>
                    } />
                </Routes>
            </div>
        </Router>
    );
}

export default App;