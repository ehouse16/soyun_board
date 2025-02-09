import { useState } from "react";
import { TextField, Button } from "@mui/material";
import { useNavigate } from "react-router-dom"; // ✅ 페이지 이동을 위한 훅
import axios from "axios";

const PostForm = () => {
    const [post, setPost] = useState({ title: "", content: "", author: "" });
    const [errors, setErrors] = useState({});
    const navigate = useNavigate(); // ✅ 페이지 이동을 위한 훅

    const handleChange = (e) => {
        setPost({ ...post, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await axios.post("/api/post/write", post);
            alert("게시글이 성공적으로 작성되었습니다!"); // ✅ 성공 메시지 표시
            navigate("/"); // ✅ 게시글 목록 페이지로 이동
        } catch (error) {
            if (error.response && error.response.data) {
                setErrors(error.response.data);
            } else {
                alert("오류가 발생했습니다.");
            }
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <TextField
                fullWidth
                label="제목"
                name="title"
                value={post.title}
                onChange={handleChange}
                required
                inputProps={{ maxLength: 15 }}
                margin="normal"
                error={!!errors.title}
                helperText={errors.title}
            />
            <TextField
                fullWidth
                label="내용"
                name="content"
                value={post.content}
                onChange={handleChange}
                required
                multiline
                rows={4}
                inputProps={{ maxLength: 1000 }}
                margin="normal"
                error={!!errors.content}
                helperText={errors.content}
            />
            <TextField
                fullWidth
                label="글쓴이"
                name="author"
                value={post.author}
                onChange={handleChange}
                required
                margin="normal"
                error={!!errors.author}
                helperText={errors.author}
            />
            <Button type="submit" variant="contained" color="primary" fullWidth sx={{ mt: 2 }}>
                작성
            </Button>
        </form>
    );
};

export default PostForm;
