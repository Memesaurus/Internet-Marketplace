import React, { useEffect } from "react";
import AppRoutes from "./AppRoutes";
import Header from "./components/Header"
import { Container } from "react-bootstrap";
import { setUser } from "./redux/userSlice";
import { getCurrentState } from "./api/apiRequests";
import { useAppDispatch } from "./redux/hooks";

const App = () => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    getCurrentState()
    .then((response) => {      
      if (response.status === 200) {
        dispatch(setUser(response.data))
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
