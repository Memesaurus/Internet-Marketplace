import React, { useState } from "react";
import { Form, Button, Container, Spinner } from "react-bootstrap";
import { useForm } from "react-hook-form";
import { UserLoginRequest } from "../api/apiTypes";
import { getCurrentState, login } from "../api/apiRequests";
import { setUser } from "../redux/userSlice";
import { Link, useNavigate } from "react-router-dom";
import { useAppDispatch } from "../redux/hooks";
import { AxiosError } from "axios";

const LoginPage = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const [error, setError] = useState<string>();
  const {
    register,
    handleSubmit,
    formState: { isDirty, isSubmitted, isValid },
  } = useForm<UserLoginRequest>();

  const onSubmit = handleSubmit((data) => {
    login(data).then((response) => {
      if (response instanceof AxiosError) {
        if (
          response.response?.status === 401 ||
          response.response?.status === 401
        ) {
          setError("Неверный Логин/Пароль");
          return;
        }

        setError("Внутренняя ошибка сервера");
        return;
      }

      getCurrentState().then((response) => {
        dispatch(
          setUser(response.data)
        );
        navigate("/");
      });
    });
  });

  return (
    <Container className="w-50">
      <Form onSubmit={onSubmit}>
        <Form.Group className="mb-3" controlId="formBasicEmail">
          <Form.Label>Логин</Form.Label>
          <Form.Control
            type="username"
            required
            placeholder="Enter username"
            {...register("username", {required: true})}
          />
        </Form.Group>

        <Form.Group className="mb-3" controlId="formBasicPassword">
          <Form.Label>Пароль</Form.Label>
          <Form.Control
            type="password"
            required
            placeholder="Password"
            {...register("password", {required: true})}
          />
        </Form.Group>

        <Form.Group className="mb-3 d-grid gap-2" controlId="formBasicCheckbox">
          <Form.Check
            type="checkbox"
            label="Запомнить меня"
            {...register("rememberMe")}
          />

          {error !== null && <div className="text-danger">{error}</div>}

          <Button
            disabled={!isDirty || !isValid}
            variant="primary"
            size="lg"
            type="submit"
          >
            {isSubmitted && error === null ? (
              <Spinner animation="border" />
            ) : (
              "Войти"
            )}
          </Button>
        </Form.Group>

        <Form.Group className="mb-3">
          <span>
            Нет аккаунта? <Link to={"/register"}>Зарегистрироваться</Link>
          </span>
        </Form.Group>
      </Form>
    </Container>
  );
};

export default LoginPage;
