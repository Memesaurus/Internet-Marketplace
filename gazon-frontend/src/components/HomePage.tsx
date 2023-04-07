import React, { useEffect, useState } from "react";
import { getAllProducts } from "../api/apiRequests";
import { Product } from "../api/apiTypes";
import ProductCard from "./ProductCard";
import { Container } from "react-bootstrap";

const HomePage = () => {
  const [products, setProducts] = useState<Product[]>();

  useEffect(() => {
    getAllProducts().then((res) => setProducts(res.data));

  }, []);

  return (
    <Container className="d-flex flex-wrap gap-4 justify-content-center">
      {products?.map((product) => 
      <ProductCard key={product.id} product={product}/>)}
    </Container>
  );
};

export default HomePage;
