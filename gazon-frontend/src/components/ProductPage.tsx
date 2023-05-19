import { useQuery } from "@tanstack/react-query";
import React from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getProduct } from "../api/apiRequests";
import { Product, UserRole } from "../api/apiTypes";
import { Button, Carousel, CarouselItem, Container } from "react-bootstrap";
import ProductImage from "./ProductImage";
import ProductReview from "./ProductReview";
import displayStars from "../utils/StarUtils";
import WithLogin from "./WithLogin";
import LoginReviewForm from "./LoginReviewForm";
import { useAppSelector } from "../redux/hooks";

const ProductPage = () => {
  const { id } = useParams<string>();
  const isLoggedIn = useAppSelector<UserRole | null>(
    (state) => state.user.role
  );
  const navigate = useNavigate();
  const intl = Intl.NumberFormat("ru-RU", {
    style: "currency",
    currency: "RUB",
  });

  const handleAddToCart = () => {
    console.log("amogus");
  };

  const getCompanyName = () => {
    if (product.user.name) {
      return product.user.name;
    }

    return product.user.username;
  };

  const WithLoginReviewForm = WithLogin({
    MemberComponent: LoginReviewForm,
    adminDisplayBoth: true,
  });

  const productQuery = useQuery({
    queryKey: ["product", id],
    queryFn: () => getProduct(id),
  });

  if (productQuery.isLoading) {
    return <div>Loading...</div>;
  }

  if (!productQuery.data?.data) {
    navigate("/404");
    return <div>404</div>;
  }

  const product: Product = productQuery.data?.data;

  return (
    <>
      <Container className="display-3 mb-3">{product?.name}</Container>

      <Container className="d-flex flex-column flex-lg-row">
        <Carousel>
          {product.images.map((image) => (
            <CarouselItem key={image}>
              <ProductImage imageId={image} productId={product.id} size="BIG" />
            </CarouselItem>
          ))}
        </Carousel>

        <Container className="d-flex flex-column gap-1 gap-lg-0">
          <div className="mt-2 mt-lg-0">
            <b>Производитель</b>
            <div>{getCompanyName()}</div>
          </div>

          <div>{displayStars(product.rating)}</div>

          {!product.isInStock && <div>Нет в наличии</div>}

          <div className="mt-2">
            <div className="mb-2">
              <b>Описание</b>
            </div>

            <div>{product?.description}</div>
          </div>
        </Container>

        <Container className="mt-2 mt-lg-0 d-flex flex-column">
          <Button
            onClick={handleAddToCart}
            disabled={!product.isInStock || isLoggedIn === null || isLoggedIn === UserRole.COMPANY}
          >
            В корзину
          </Button>
          <div className="align-self-center">
            <b>{intl.format(product?.price)}</b>
          </div>
        </Container>
      </Container>

      <div className="mt-3">
        <div className="display-6 mb-2">Отзывы о товаре</div>

        <Container>
          <WithLoginReviewForm />

          {product?.reviews?.map((review) => (
            <ProductReview key={review.id} review={review} />
          ))}
        </Container>
      </div>
    </>
  );
};

export default ProductPage;
