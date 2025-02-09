import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom"; // useNavigate 추가
import axios from "axios";
import { Container, Typography, Box, Card, CardContent, Button } from "@mui/material";

const PostDetail = () => {
    const { id } = useParams();
    const [post, setPost] = useState(null);
    const navigate = useNavigate(); // 페이지 이동을 위한 훅

    useEffect(() => {
        const fetchPost = async () => {
            try {
                const response = await axios.get(`/posts/${id}`);
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
                            >
                                수정
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
