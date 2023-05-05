import React, { useEffect } from "react";
import AppRoutes from "./AppRoutes";
import Header from "./components/Header"
import { Container } from "react-bootstrap";
import { useDispatch } from "react-redux";
import { setUser } from "./redux/userSlice";
import { getCurrentState } from "./api/apiRequests";

const App = () => {
  const dispatch = useDispatch();

  useEffect(() => {
    getCurrentState()
    .then((response) => {      
      if (response.status === 200) {
        dispatch(setUser({username: response.data.username}))
      }
    })

  }, []);

  return (
    <>
    <Header />
    <Container className="mt-3">
      <AppRoutes />
    </Container>
    </>
  );
}

export default App;
