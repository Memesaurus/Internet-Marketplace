import axios from "axios";
import React from "react";
import { useState } from "react";
import { addProduct, login, logout, placeOrder } from "./api/apiRequests";
import { OrderRequest, UserAddress, UserLoginRequest } from "./api/apiTypes";
import api from "./api/axiosConfiguration";

function App() {
  const [count, setCount] = useState(0);

  const logIn = () => {
    login({username: 'member', password: 'member', rememberMe: true} as UserLoginRequest)
  };

  const post = () => {
    addProduct({
      description: 'test',
      price: 100,
      name: 'test',
      tags: ['test', 'test2']
    }).then(response => console.log(response))
  }
  
  return (
    <div className="App">
      
      <div className="card">
        <button onClick={logIn}>login</button>
        <button onClick={logout}>logout</button>
        <button onClick={post}>post</button>
      </div>

    </div>
  );
}

export default App;
