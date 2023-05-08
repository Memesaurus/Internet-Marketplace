import React from "react";
import { Nav } from "react-bootstrap";
import { NavLink } from "react-router-dom";
import { useAppDispatch, useAppSelector } from "../redux/hooks";
import { logout } from "../api/apiRequests";
import { clearUser } from "../redux/userSlice";

const LoggedInNavbar = () => {
  const dispatch = useAppDispatch();
  const username = useAppSelector((state) => state.user.username);

  const handleLogout = () => {
    logout().then(() => {
      dispatch(clearUser());
    });
  };

  return (
    <>
      <Nav.Link className="text-white">
        Текущий пользователь {username}
      </Nav.Link>
      <Nav.Link
        className="text-white"
        as={NavLink}
        onClick={handleLogout}
        to="/"
      >
        Выйти
      </Nav.Link>
    </>
  );
};

export default LoggedInNavbar;
