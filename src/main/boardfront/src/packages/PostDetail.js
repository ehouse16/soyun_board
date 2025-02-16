import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom"; // useNavigate 추가
import axios from "axios";
import { Container, Typography, Box, Card, CardContent, Button } from "@mui/material";
import axiosInstance from './axiosConfig';

const PostDetail = () => {
    const { id } = useParams();
    const [post, setPost] = useState(null);
    const navigate = useNavigate(); // 페이지 이동을 위한 훅

    useEffect(() => {
        const fetchPost = async () => {
            try {
                const response = await axiosInstance.get(`/api/posts/${id}`);
                setPost(response.data);
            } catch (error) {
                console.error("게시글을 불러오는 중 오류 발생", error);
            }
        };
        fetchPost();
    }, [id]);

    // 수정 버튼 클릭 시 PostEdit.js로 이동
    const handleEditClick = () => {
        navigate(`/posts/edit/${id}`); // 수정 페이지로 이동
    };

    // 게시글 삭제 처리
    const handleDeleteClick = async () => {
        const confirmDelete = window.confirm("정말로 이 게시글을 삭제하시겠습니까?");
        if (confirmDelete) {
            try {
                await axiosInstance.delete(`/api/posts/${id}`);
                alert("게시글이 삭제되었습니다!");
                navigate("/posts"); // 게시글 목록 페이지로 이동
            } catch (error) {
                console.error("게시글 삭제 중 오류 발생", error);
                alert("게시글 삭제에 실패했습니다.");
            }
        }
    };

    return (
        <Container maxWidth="md">
            <Box sx={{ mt: 5 }}>
                {post ? (
                    <Card>
                        <CardContent>
                            <Typography variant="h4" gutterBottom>
                                {post.title}
                            </Typography>
                            <Typography variant="body1" gutterBottom>
                                <strong>작성자:</strong> {post.author}
                            </Typography>
                            <Typography variant="body2" color="text.secondary" gutterBottom>
                                {post.content}
                            </Typography>
                            <Button
                                variant="contained"
                                color="primary"
                                onClick={handleEditClick} // 수정 버튼 클릭 시 호출
                                sx={{ mr: 2 }}
                            >
                                수정
                            </Button>
                            <Button
                                variant="contained"
                                color="secondary"
                                onClick={handleDeleteClick} // 삭제 버튼 클릭 시 호출
                            >
                                삭제
                            </Button>
                        </CardContent>
                    </Card>
                ) : (
                    <Typography>게시글을 불러오는 중입니다...</Typography>
                )}
            </Box>
        </Container>
    );
};

export default PostDetail;
