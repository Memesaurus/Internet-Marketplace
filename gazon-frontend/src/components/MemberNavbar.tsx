import React from "react";
import LoggedInNavbar from "./LoggedInNavbar";
import { Nav } from "react-bootstrap";
import { BsCart } from "react-icons/bs";
import { NavLink } from "react-router-dom";
import { useAppSelector } from "../redux/hooks";

const MemberNavbar = () => {
  const cartSize = useAppSelector((state) => state.user.cartSize);

  return (
    <Nav className="mx-2 ms-auto">
      <Nav.Link className="d-flex align-items-center gap-2 text-white" as={NavLink} to={'/cart'} >
        <BsCart className="d-md-block d-none text-white m" style={{width: '27px', height: '27px'}} />
        <div className=" text-white d-block d-md-none">Корзина</div>
        {cartSize}
      </Nav.Link>
      <LoggedInNavbar />
    </Nav>
  );
};

export default MemberNavbar;
