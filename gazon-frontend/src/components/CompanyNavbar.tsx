import React from "react";
import { Nav } from "react-bootstrap";
import { NavLink } from "react-router-dom";
import LoggedInNavbar from "./LoggedInNavbar";

const CompanyNavbar = () => {
  return (
    <Nav className="mx-2 ms-auto">
      <Nav.Link className="text-white" as={NavLink} to={"/product/new"}>
         Разместить товар на платформе
      </Nav.Link>
        <LoggedInNavbar />
    </Nav>
  );
};

export default CompanyNavbar;
