import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom"; // URL 파라미터와 페이지 이동을 위한 훅
import axios from "axios";
import { TextField, Button } from "@mui/material";

const PostEdit = () => {
    const { id } = useParams(); // URL에서 게시글 id 추출
    const [post, setPost] = useState({ title: "", content: "", author: "" });
    const [errors, setErrors] = useState({});
    const navigate = useNavigate(); // 페이지 이동을 위한 훅

    // 게시글 정보를 불러오기 위한 useEffect
    useEffect(() => {
        const fetchPost = async () => {
            try {
                const response = await axios.get(`/posts/${id}`);
                setPost({
                    title: response.data.title,
                    content: response.data.content,
                    author: response.data.author,
                });
            } catch (error) {
                console.error("게시글을 불러오는 중 오류 발생", error);
            }
        };
        fetchPost();
    }, [id]); // id가 변경되면 다시 실행

    // 입력값 변경 시 상태 업데이트
    const handleChange = (e) => {
        setPost({ ...post, [e.target.name]: e.target.value });
    };

    // 폼 제출 시 게시글 수정 처리
    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await axios.put(`/posts/${id}`, {
                title: post.title,
                content: post.content,
            });
            alert("게시글이 성공적으로 수정되었습니다!");
            navigate(`/posts/${id}`); // 수정된 게시글 상세 페이지로 이동
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
                margin="normal"
                disabled // 글쓴이 필드는 수정할 수 없게 막음
            />
            <Button type="submit" variant="contained" color="primary" fullWidth sx={{ mt: 2 }}>
                수정
            </Button>
        </form>
    );
};

export default PostEdit;
