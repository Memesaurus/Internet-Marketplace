import React, { useEffect, useState } from "react";
import { getAllProducts } from "../api/apiRequests";
import { Product } from "../api/apiTypes";
import ProductCard from "./ProductCard";
import { Container } from "react-bootstrap";
import { Link } from "react-router-dom";

const HomePage = () => {
  const [products, setProducts] = useState<Product[]>();

  useEffect(() => {
    getAllProducts().then((res) => setProducts(res.data));
  }, []);

  return (
    <Container className="d-flex flex-wrap gap-4 justify-content-center">
      {products?.map((product) => 
      <Link className="text-decoration-none" key={product.id} to={`/product/${product.id}`}>
        <ProductCard product={product}/>
      </Link>
      )}
    </Container>
  );
};

export default HomePage;
