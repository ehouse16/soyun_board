import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { Container, Typography, Box } from "@mui/material";
import PostForm from "../components/PostForm"; // 입력 폼 컴포넌트 가져오기

const PostCreate = () => {
    const navigate = useNavigate();
    const [post, setPost] = useState({ title: "", content: "", author: "" });
    const [error, setError] = useState("");

    const handleChange = (e) => {
        const { name, value } = e.target;
        setPost({ ...post, [name]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError("");

        try {
            await axios.post("/api/posts/write", post);
            navigate("/posts"); // 게시글 목록 페이지로 이동
        } catch (err) {
            setError("게시글 작성에 실패했습니다. 다시 시도해주세요.");
        }
    };

    return (
        <Container maxWidth="sm">
            <Box sx={{ mt: 5 }}>
                <Typography variant="h4" gutterBottom>
                    게시글 작성
                </Typography>
                {error && (
                    <Typography color="error" sx={{ mb: 2 }}>
                        {error}
                    </Typography>
                )}
                <PostForm post={post} handleChange={handleChange} handleSubmit={handleSubmit} />
            </Box>
        </Container>
    );
};

export default PostCreate;
