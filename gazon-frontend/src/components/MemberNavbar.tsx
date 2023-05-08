import React from "react";
import LoggedInNavbar from "./LoggedInNavbar";
import { Nav } from "react-bootstrap";
import { BsCart } from "react-icons/bs";
import { NavLink } from "react-router-dom";

const MemberNavbar = () => {
  return (
    <Nav className="mx-2 ms-auto">
      <Nav.Link as={NavLink} to={'/cart'} >
        <BsCart className="d-md-block d-none text-white m" style={{width: '27px', height: '27px'}} />
        <div className=" text-white d-block d-md-none">Корзина</div>
      </Nav.Link>
      <LoggedInNavbar />
    </Nav>
  );
};

export default MemberNavbar;
