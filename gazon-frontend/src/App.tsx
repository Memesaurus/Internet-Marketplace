import axios from "axios";
import React from "react";
import { useState } from "react";
import "./App.css";

function App() {
  const [count, setCount] = useState(0);

  const logIn = () => {
    axios
      .post("http://localhost:8080/api/auth/login", {
        username: "admin",
        password: "admin",
      })
      .then(
        (response) =>
          (axios.defaults.headers.common["Authorization"] = `Bearer ${response.data}`)
      );
  };

  const postData = () => {
    axios.post(
      "http://localhost:8080/api/product/6414d0c1d50083306a0fe6a5/reviews",
      {
        rating: 2.5,
        body: "Жестко"
      }
    );
  };

  return (
    <div className="App">
      <div className="card">
        <button onClick={logIn}></button>
        <button onClick={postData}></button>
        <p>
          Edit <code>src/App.tsx</code> and save to test HMR
        </p>
      </div>
      <p className="read-the-docs">
        Click on the Vite and React logos to learn more
      </p>
    </div>
  );
}

export default App;
