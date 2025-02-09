import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import PostCreate from "./packages/PostCreate"; // PostCreate 컴포넌트 가져오기
import PostList from "./packages/PostList"; // PostList 컴포넌트 가져오기
import PostDetail from "./packages/PostDetail";
import PostEdit from "./packages/PostEdit"; // PostDetail 컴포넌트 가져오기

function App() {
    return (
        <Router>
            <div className="App">
                <h1>홈페이지</h1>
                <Link to={"/post/create"}>
                    <button>게시글 작성하기</button>
                </Link>
                <Link to={"/posts"}>
                    <button>게시글 목록 보기</button>
                </Link>
                <Routes>
                    <Route path="/" element={<div>홈페이지</div>} />
                    <Route path="/post/create" element={<PostCreate />} />
                    <Route path="/posts" element={<PostList />} />
                    <Route path="/posts/:id" element={<PostDetail />} /> {/* 상세 페이지를 위한 Route 추가 */}
                    <Route path="/posts/edit/:id" element={<PostEdit />} /> {/* 수정 페이지 경로 */}
                </Routes>
            </div>
        </Router>
    );
}

export default App;
