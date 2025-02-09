import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import { Container, Typography, Box, Card, CardContent } from "@mui/material";

const PostDetail = () => {
    const { id } = useParams();
    const [post, setPost] = useState(null);

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
                            <Typography variant="body2" color="text.secondary">
                                {post.content}
                            </Typography>
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
