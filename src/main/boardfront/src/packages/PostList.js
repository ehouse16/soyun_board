import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { Container, Typography, Box, Card, CardContent, CardActionArea } from "@mui/material";
import axiosInstance from './axiosConfig';

const PostList = () => {
    const [posts, setPosts] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchPosts = async () => {
            try {
                const response = await axiosInstance.get("/api/posts");
                setPosts(response.data);
            } catch (error) {
                console.error("게시글을 불러오는 중 오류 발생", error);
            }
        };
        fetchPosts();
    }, []);

    return (
        <Container maxWidth="md">
            <Box sx={{ mt: 5 }}>
                <Typography variant="h4" gutterBottom>
                    게시글 목록
                </Typography>
                {posts.length === 0 ? (
                    <Typography>게시글이 없습니다.</Typography>
                ) : (
                    posts.map((post) => (
                        <Card key={post.id} sx={{ mb: 2 }}>
                            <CardActionArea onClick={() => navigate(`/posts/${post.id}`)}> {/* post.id 사용 */}
                                <CardContent>
                                    <Typography variant="h6" gutterBottom>
                                        {post.title}
                                    </Typography>
                                    <Typography variant="body2" color="text.secondary">
                                        {post.author}
                                    </Typography>
                                    <Typography variant="body2" color="text.secondary">
                                        {post.content}
                                    </Typography>
                                </CardContent>
                            </CardActionArea>
                        </Card>
                    ))
                )}
            </Box>
        </Container>
    );
};

export default PostList;
