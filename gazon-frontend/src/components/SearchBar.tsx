import React from "react";
import {Button, Form, FormControl } from "react-bootstrap";
import { BsSearch } from "react-icons/bs"

const SearchBar = () => {
  return (
    <Form className="d-flex">
      <FormControl className="col-12" type="search" placeholder="Поиск" />
    
      <Button style={{marginLeft:'-42px' }}>
        <BsSearch className="mb-1" />
      </Button>
    </Form>
  );
};

export default SearchBar;
