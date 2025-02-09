import { BrowserRouter as Router, Route, Routes, Link } from "react-router-dom";
import PostCreate from "./packages/PostCreate"; // PostCreate 컴포넌트 가져오기

function App() {
    return (
        <Router>
            <div className="App">
                <h1>홈페이지</h1>
                <Link to={"/post/create"}>
                    <button>
                        move to create
                    </button>
                </Link>
                <Routes>
                    <Route path="/" element={<div>홈페이지</div>} />
                    <Route path="/post/create" element={<PostCreate />} />
                </Routes>
            </div>
        </Router>
    );
}

export default App;
