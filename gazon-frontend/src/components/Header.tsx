import React from "react";
import { Container, NavbarBrand } from "react-bootstrap";
import Navbar from "react-bootstrap/Navbar";
import { Link } from "react-router-dom";
import logo from "../assets/logo.png";
import logo_mini from "../assets/logo_mini.png";
import Image from "react-bootstrap/Image"
import WithLogin from "./WithLogin";
import NoLoginNavbar from "./NoLoginNavbar";
import CompanyNavbar from "./CompanyNavbar";
import MemberNavbar from "./MemberNavbar";

const Header = () => {
    const WithLoginNavbar = WithLogin({
      NoLoginComponent: NoLoginNavbar,
      CompanyComponent: CompanyNavbar,
      MemberComponent: MemberNavbar,
    })

  return (
    <Navbar className="border-bottom border-primary" variant="dark" expand="md">
      <Container fluid>
        
        <NavbarBrand className="flex-grow-1 d-none d-md-block">
            <Link to="/"> 
                <Image style={{width: 120, height: 50}} src={logo} alt="Gazon" /> 
            </Link>
        </NavbarBrand>
        <NavbarBrand className="d-block d-md-none">
            <Link to="/"> 
                <Image style={{width: 40, height: 40}} src={logo_mini} alt="Gazon" /> 
            </Link>
        </NavbarBrand>

        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <WithLoginNavbar />
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default Header;
