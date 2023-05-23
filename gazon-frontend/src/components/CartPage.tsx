import { useQuery } from "@tanstack/react-query";
import React from "react";
import { getCart, placeOrder } from "../api/apiRequests";
import { Button, Spinner } from "react-bootstrap";
import { CartItem } from "../api/apiTypes";
import CartPageItem from "./CartPageItem";
import { useNavigate } from "react-router-dom";
import { useAppDispatch } from "../redux/hooks";
import { clearCart } from "../redux/userSlice";

const CartPage = () => {
  const { data, isLoading } = useQuery<CartItem[]>({
    queryKey: ["cart"],
    queryFn: () => getCart().then((response) => response.data),
  });
  const navigate = useNavigate();
  const dispatch = useAppDispatch();

  const handlePlaceOrder = () => {
    placeOrder().then(() => {
      dispatch(clearCart());
      navigate("/");
    })
  }

  if (isLoading) {
    return <Spinner />;
  }

  return (
    <>
      <div className="w-75 mx-auto">
        {data?.map((item) => (
          <CartPageItem
            key={item.product.id}
            product={item.product}
            quantity={item.quantity}
          />
        ))}
      </div>

      <Button onClick={handlePlaceOrder}> Заказать </Button>
    </>
  );
};

export default CartPage;
