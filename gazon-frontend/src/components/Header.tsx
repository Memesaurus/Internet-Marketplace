import React from "react";
import { Container, Nav, NavbarBrand } from "react-bootstrap";
import Navbar from "react-bootstrap/Navbar";
import { Link, NavLink } from "react-router-dom";
import logo from "../assets/logo.png";
import Image from "react-bootstrap/Image"
import { useAppSelector } from "../redux/hooks";
import SearchBar from "./SearchBar";

const Header = () => {
    const username = useAppSelector((state) => state.user.username);

  return (
    <Navbar className="border-bottom border-primary" expand="md">
      <Container fluid>
        <NavbarBrand className="flex-grow-1 d-none d-md-block">
            <Link to="/"> 
                <Image style={{width: 120, height: 50}} src={logo} alt="Gazon" /> 
            </Link>
        </NavbarBrand>
        <div className="flex-grow-1">
          <SearchBar />
        </div>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="mx-2 ms-auto">
            <Nav.Link className="text-white">
              Logged in as {username}
            </Nav.Link>
            <Nav.Link className="text-white" as={NavLink} to="/login"> 
              Войти 
            </Nav.Link>
            <Nav.Link className="text-white" as={NavLink} to="/logout"> 
              Выйти
            </Nav.Link>
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default Header;
