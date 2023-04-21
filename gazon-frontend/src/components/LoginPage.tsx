import React from "react";
import { Form, Button, Container, Spinner } from "react-bootstrap";
import { useForm } from "react-hook-form";
import { useDispatch } from "react-redux";
import { UserLoginRequest } from "../api/apiTypes";
import { login } from "../api/apiRequests";
import { setUser } from "../redux/userSlice";
import { useNavigate } from "react-router-dom";

const LoginPage = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const {
    register,
    handleSubmit,
    formState: { isDirty, isSubmitted, isValid },
  } = useForm<UserLoginRequest>();

  const onSubmit = handleSubmit((data) => {
    login(data).then(() => {
      dispatch(setUser({ username: data.username }));
      navigate("/");
    });
  });

  return (
    <Container className="w-50">
      <Form onSubmit={onSubmit}>
        <Form.Group className="mb-3" controlId="formBasicEmail">
          <Form.Label>Логин</Form.Label>
          <Form.Control
            type="username"
            placeholder="Enter username"
            {...register("username")}
          />
        </Form.Group>

        <Form.Group className="mb-3" controlId="formBasicPassword">
          <Form.Label>Пароль</Form.Label>
          <Form.Control
            type="password"
            placeholder="Password"
            {...register("password")}
          />
        </Form.Group>

        <Form.Group className="mb-3 d-grid gap-2" controlId="formBasicCheckbox">
          <Form.Check
            type="checkbox"
            label="Запомнить меня"
            {...register("rememberMe")}
          />

          <Button
            disabled={isSubmitted || !isDirty || !isValid}
            variant="primary"
            size="lg"
            type="submit"
          >
            {isSubmitted ? <Spinner animation="border" /> : "Войти"}
          </Button>
        </Form.Group>

        <Form.Group className="mb-3">
          <span>Нет аккаунта? Иди нахуй пока что</span>
        </Form.Group>
      </Form>
    </Container>
  );
};

export default LoginPage;