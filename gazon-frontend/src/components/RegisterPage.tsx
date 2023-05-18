import React, { useState } from "react";
import { Button, Container, Form } from "react-bootstrap";
import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import {
  CompanyUserRegisterRequest,
  MemberUserRegisterRequest,
  UserRole,
} from "../api/apiTypes";
import { registerUser } from "../api/apiRequests";
import { AxiosError } from "axios";

const RegisterPage = () => {
  const [role, setRole] = useState<UserRole | null>(null);
  const [error, setError] = useState<string>();
  const {
    handleSubmit,
    formState: { isDirty, isValid, isSubmitted },
    register,
  } = useForm<MemberUserRegisterRequest | CompanyUserRegisterRequest>();
  const navigate = useNavigate();

  const handleCancel = () => {
    navigate("/");
  };

  const onSubmit = handleSubmit((data) => {
    registerUser(data).then((response: unknown) => {
      if (!(response instanceof AxiosError)) {
        navigate("/");
      } else {
        if (response.response?.status === 400) {
          setError("Пользователь с заданным логином или почтой уже существует");
        } else {
          setError("Ошибка во время выполнения запроса");
        }
      }
    });
  });

  return (
    <Container className="px-lg-5">
      <Container className="display-6 mb-3 px-lg-5">Регистрация</Container>

      <Form className="d-flex mx-lg-5 gap-2 flex-column align-items-center">
        <Form.Group className="w-100">
          <Form.Label className="align-self-start">
            Почта <span className="h5 text-danger">*</span>{" "}
          </Form.Label>
          <Form.Control
            required
            autoComplete="off"
            placeholder="почта"
            type="email"
            {...register("email", { required: true })}
          />
        </Form.Group>

        <Form.Group className="w-100">
          <Form.Label className="align-self-start">
            Логин <span className="h5 text-danger">*</span>{" "}
          </Form.Label>
          <Form.Control
            required
            autoComplete="off"
            placeholder="логин"
            {...register("username", { required: true })}
          />
        </Form.Group>

        <Form.Group className="w-100">
          <Form.Label className="align-self-start">
            Пароль <span className="h5 text-danger">*</span>{" "}
          </Form.Label>
          <Form.Control
            required
            autoComplete="off"
            placeholder="пароль"
            type="password"
            {...register("password", { required: true })}
          />
        </Form.Group>

        <Form.Group className="w-100">
          <Form.Label className="align-self-start">
            Страна <span className="h5 text-danger">*</span>
          </Form.Label>
          <Form.Control
            autoComplete="off"
            placeholder="Страна"
            {...register("additionalInfo.address.country", { required: true })}
          />
        </Form.Group>

        <Form.Group className="w-100">
          <Form.Label required className="align-self-start">
            Город <span className="h5 text-danger">*</span>
          </Form.Label>
          <Form.Control
            autoComplete="off"
            placeholder="Город"
            {...register("additionalInfo.address.country", { required: true })}
          />
        </Form.Group>

        <Form.Group className="w-100">
          <Form.Label required className="align-self-start">
            Улица <span className="h5 text-danger">*</span>
          </Form.Label>
          <Form.Control
            autoComplete="off"
            placeholder="Улица"
            {...register("additionalInfo.address.street", { required: true })}
          />
        </Form.Group>

        <Form.Group className="w-100">
          <Form.Label required className="align-self-start">
            Дом <span className="h5 text-danger">*</span>
          </Form.Label>
          <Form.Control
            autoComplete="off"
            placeholder="Дом"
            {...register("additionalInfo.address.city", { required: true })}
          />
        </Form.Group>

        <Form.Group className="w-100">
          <Form.Label required className="align-self-start">
            Телефон <span className="h5 text-danger">*</span>
          </Form.Label>
          <Form.Control
            placeholder="123-45-678"
            required
            autoComplete="off"
            type="tel"
            {...register("additionalInfo.phone", { required: true })}
          />
        </Form.Group>

        <Form.Group className="d-flex">
          <Form.Check
            inline
            type="radio"
            label="Частное лицо"
            value={UserRole.MEMBER}
            {...register("role", { required: true })}
            onClick={() => setRole(UserRole.MEMBER)}
          />
          <Form.Check
            inline
            type="radio"
            label="Компания"
            value={UserRole.COMPANY}
            {...register("role", { required: true })}
            onClick={() => setRole(UserRole.COMPANY)}
          />
        </Form.Group>

        {role === UserRole.MEMBER && (
          <Form.Group className="d-flex flex-column w-100 gap-2">
            <Form.Group className="w-100">
              <Form.Label required className="align-self-start">
                Фамилия
              </Form.Label>
              <Form.Control
                autoComplete="off"
                placeholder="фамилия"
                {...register("surname", { required: true })}
              />
            </Form.Group>
            <Form.Group className="w-100">
              <Form.Label required className="align-self-start">
                Имя
              </Form.Label>
              <Form.Control
                autoComplete="off"
                placeholder="имя"
                {...register("name", { required: true })}
              />
            </Form.Group>

            <Form.Group>
              <Form.Label required className="align-self-start">
                Отчество
              </Form.Label>
              <Form.Control
                autoComplete="off"
                placeholder="отчество"
                {...register("middlename", { required: true })}
              />
            </Form.Group>

            <Form.Group className="w-100">
              <Form.Label required className="align-self-start">
                Возраст
              </Form.Label>
              <Form.Control
                autoComplete="off"
                placeholder="Возраст"
                {...register("age", { required: true })}
              />
            </Form.Group>
          </Form.Group>
        )}

        {role === UserRole.COMPANY && (
          <Form.Group className="d-flex flex-column w-100 gap-2">
            <Form.Group className="w-100">
              <Form.Label required className="align-self-start">
                Наименование компании
              </Form.Label>
              <Form.Control
                autoComplete="off"
                placeholder="название"
                {...register("name", { required: true })}
              />
            </Form.Group>

            <Form.Group className="w-100">
              <Form.Label required className="align-self-start">
                Описание
              </Form.Label>
              <Form.Control
                placeholder="описание"
                autoComplete="off"
                {...register("description", { required: true })}
              />
            </Form.Group>
          </Form.Group>
        )}

        {error !== null && <div className="text-danger">{error}</div>}

        <Container className="d-flex mt-2 gap-3 justify-content-center">
          <Button disabled={!isValid || !isDirty} onClick={onSubmit} size="lg">
            Зарегистрироваться
          </Button>

          <Button
            disabled={isSubmitted}
            variant="danger"
            size="lg"
            onClick={handleCancel}
          >
            Отмена
          </Button>
        </Container>
      </Form>
    </Container>
  );
};

export default RegisterPage;
