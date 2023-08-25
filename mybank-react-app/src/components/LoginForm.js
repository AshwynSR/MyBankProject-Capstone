import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "./Auth";

const LoginForm = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [loginError, setLoginError] = useState('');
  
  const auth = useAuth()

  const navigate = useNavigate();

  const validUsers = [
    { username: "Ashwin", password: "1234" },
    { username: "Harshit", password: "4321" }
  ];

  const handleUsernameChange = (event) => {
    setUsername(event.target.value);
  };

  const handlePasswordChange = (event) => {
    setPassword(event.target.value);
  };

  const handleLogin = (event) => {
    event.preventDefault();

    const user = validUsers.find(user => user.username === username && user.password === password);

    if (user) {
      console.log('Login successful!');
      auth.login(user)
      navigate('/home', {replace: true});
    } else {
      console.log('Login failed. Please check your credentials.');
      setLoginError('Invalid username or password');
      alert('Invalid username or password');
    }
  };

  return (
    <>
      <h1>Please Login !!</h1>
      <form onSubmit={handleLogin}>
        <label>
          Username:
          <input type="text" value={username} onChange={handleUsernameChange} />
        </label>
        <br />
        <label>
          Password:
          <input type="password" value={password} onChange={handlePasswordChange} />
        </label>
        <br />
        <button type="submit">Login</button>
      </form>
    </>
  );
};

export default LoginForm;

