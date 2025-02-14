import React, { useState } from 'react';
import axios from 'axios';

function Signup() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [name, setName] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('http://localhost:8080/api/auth/signup', {
                email,
                password,
                name
            });
            alert(response.data); // 회원가입 성공 메세지
        } catch (error) {
            alert(error.response.data.message);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <h2>Signup</h2>
            <label>Email:</label>
            <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
            <label>Password:</label>
            <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
            <label>Name:</label>
            <input type="text" value={name} onChange={(e) => setName(e.target.value)} required />
            <button type="submit">Signup</button>
        </form>
    );
}

export default Signup;
