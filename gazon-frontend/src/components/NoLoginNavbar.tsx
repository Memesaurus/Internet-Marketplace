import React from 'react'
import { Nav } from 'react-bootstrap'
import { NavLink } from 'react-router-dom'


const NoLoginNavbar = () => {
  return (
    <Nav className="mx-2 ms-auto">
        <Nav.Link className="text-white" as={NavLink} to="/login">
            Войти
        </Nav.Link>
        <Nav.Link className="text-white" as={NavLink} to="/register">
            Зарегистрироваться
        </Nav.Link>
    </Nav>
  )
}

export default NoLoginNavbar