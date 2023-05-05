import { useQuery } from "@tanstack/react-query";
import React, { useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getProduct } from "../api/apiRequests";
import { Product } from "../api/apiTypes";
import { Button, Container } from "react-bootstrap";
import Form from "react-bootstrap/Form";
import ProductImage from "./ProductImage";
import ProductReview from "./ProductReview";
import displayStars from "../utils/StarUtils";
import { BsStarFill } from "react-icons/bs";
import ReviewSubmitStars from "./ReviewSubmitStars";

const ProductPage = () => {
  const { id } = useParams();
  const [reviewRating, setReviewRating] = useState<number>(0);
  const navigate = useNavigate();
  const intl = Intl.NumberFormat("ru-RU", {
    style: "currency",
    currency: "RUB",
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

  const onSubmit = () => {
    console.log("amogus");
  };

  const product: Product = productQuery.data?.data;

  return (
    <>
      <Container className="display-3 pb-3">{product?.name}</Container>

      <Container className="d-flex flex-column flex-lg-row">
        <ProductImage
          className="img-responsive"
          imageId={product.images[0]}
          productId={product.id}
          size="BIG"
        />

        <Container className="d-flex flex-column gap-1 gap-lg-0">
          <div className="pt-2 pt-lg-0">
            <b>Производитель</b> {product.user.username}
          </div>
          <div>{displayStars(product.rating)}</div>
          {!product.isInStock && <div>Нет в наличии</div>}

          <div className="mt-2">
            <div className="pb-2">
              <b>Описание</b>
            </div>

            <Container>{product?.description}</Container>
          </div>
        </Container>

        <Container className="pt-2 pt-lg-0 d-flex flex-column">
          <Button disabled={!product.isInStock}>В корзину</Button>
          <div className="align-self-center">
            <b>{intl.format(product?.price)}</b>
          </div>
        </Container>
      </Container>

      <div className="mt-3">
        <div className="display-6 pb-2">Отзывы о товаре</div>

        <Container>
          <form
            onSubmit={onSubmit}
            className="d-flex align-items-lg-center flex-column flex-lg-row pb-3"
          >
            <div className="pb-1">
              <ReviewSubmitStars setRating={setReviewRating} />
            </div>

            <Form className="flex-grow-1 py-2 px-lg-2">
              <Form.Control placeholder="Ваш отзыв" />
            </Form>

            <Button type="submit">Оставить отзыв</Button>
          </form>

          {product?.reviews?.map((review) => (
            <ProductReview key={review.id} review={review} />
          ))}
        </Container>
      </div>
    </>
  );
};

export default ProductPage;
