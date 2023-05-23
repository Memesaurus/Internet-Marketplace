import { useQuery } from "@tanstack/react-query";
import React from "react";
import { useParams, useNavigate } from "react-router-dom";
import { addToCart, getProduct } from "../api/apiRequests";
import { Product, UserRole } from "../api/apiTypes";
import { Button, Carousel, CarouselItem, Container } from "react-bootstrap";
import ProductImage from "./ProductImage";
import ProductReview from "./ProductReview";
import displayStars from "../utils/StarUtils";
import WithLogin from "./WithLogin";
import LoginReviewForm from "./LoginReviewForm";
import { useAppDispatch, useAppSelector } from "../redux/hooks";
import { increaseCartSize } from "../redux/userSlice";

const ProductPage = () => {
  const { id } = useParams<string>();
  const isLoggedIn = useAppSelector<UserRole | null>(
    (state) => state.user.role
  );
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const intl = Intl.NumberFormat("ru-RU", {
    style: "currency",
    currency: "RUB",
  });

  const handleAddToCart = () => {
    if (id) {
      addToCart(id).then(() => {
        dispatch(increaseCartSize())
      });
    }
  };

  const getCompanyName = () => {
    if (data?.user.name) {
      return data?.user.name;
    }

    return data?.user.username;
  };

  const WithLoginReviewForm = WithLogin({
    MemberComponent: LoginReviewForm,
    adminDisplayBoth: true,
  });

  const { data, isLoading } = useQuery<Product>({
    queryKey: ["product", id],
    queryFn: () => getProduct(id).then((response) => response.data),
  });

  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (!data) {
    navigate("/404");
    return <div>404</div>;
  }


  return (
    <>
      <Container className="display-3 mb-3">{data?.name}</Container>

      <Container className="d-flex flex-column flex-lg-row">
        <Carousel>
          {data.images.map((image) => (
            <CarouselItem key={image}>
              <ProductImage imageId={image} productId={data.id} size="BIG" />
            </CarouselItem>
          ))}
        </Carousel>

        <Container className="d-flex flex-column gap-1 gap-lg-0">
          <div className="mt-2 mt-lg-0">
            <b>Производитель</b>
            <div>{getCompanyName()}</div>
          </div>

          <div>{displayStars(data.rating)}</div>

          {!data.isInStock && <div>Нет в наличии</div>}

          <div className="mt-2">
            <div className="mb-2">
              <b>Описание</b>
            </div>

            <div>{data?.description}</div>
          </div>
        </Container>

        <Container className="mt-2 mt-lg-0 d-flex flex-column">
          <Button
            onClick={handleAddToCart}
            disabled={!data.isInStock || isLoggedIn === null || isLoggedIn === UserRole.COMPANY}
          >
            В корзину
          </Button>
          <div className="align-self-center">
            <b>{intl.format(data?.price)}</b>
          </div>
        </Container>
      </Container>

      <div className="mt-3">
        <div className="display-6 mb-2">Отзывы о товаре</div>

        <Container>
          <WithLoginReviewForm />

          {data?.reviews?.map((review) => (
            <ProductReview key={review.id} review={review} />
          ))}
        </Container>
      </div>
    </>
  );
};

export default ProductPage;
