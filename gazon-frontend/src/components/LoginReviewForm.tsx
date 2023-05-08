import React, { useState } from "react";
import { Button, FormControl } from "react-bootstrap";
import ReviewSubmitStars from "./ReviewSubmitStars";
import { useForm } from "react-hook-form";
import { useMutation } from "@tanstack/react-query";
import { addReviewToProduct } from "../api/apiRequests";
import { ReviewRequest } from "../api/apiTypes";
import { useNavigate, useParams } from "react-router-dom";
import { AxiosError, AxiosResponse } from "axios";


const LoginReviewForm = () => {
  const [reviewRating, setReviewRating] = useState<number>(0);
  const [showErrorMessage, setShowErrorMessage] = useState<boolean>(false);
  const { id } = useParams<string>();
  const navigate = useNavigate();

  if (!id) {
    navigate('/404')
    return (<></>);
  }

  const axiosPostWrapper = async (data: ReviewRequest) => {
    const request: AxiosResponse = await addReviewToProduct(id, data);
    if (request.status === 200) {
      return;
    } else {
      throw new Error();
    }
  }
  
  const reviewMutate = useMutation({
    mutationFn: (data: ReviewRequest) => axiosPostWrapper(data),
    onError: () => setShowErrorMessage(true)
  })

  const {
    register,
    handleSubmit,
    formState: { isDirty, isSubmitted, isValid },
  } = useForm();

  const onSubmit = handleSubmit((data) => {
    const review: ReviewRequest = {
      body: data.body,
      rating: reviewRating
    };

    reviewMutate.mutate(review);
  });

  return (
    <form
      onSubmit={onSubmit}
      className="d-flex align-items-lg-center flex-column flex-lg-row pb-3"
    >
      <div className="pb-1">
        <ReviewSubmitStars setRating={setReviewRating} />
      </div>

      <div className="flex-grow-1 py-2 px-lg-2">
        <FormControl autoComplete="false" required placeholder="Ваш отзыв" {...register("body")} />
      </div>

      <div hidden={!showErrorMessage} className="text-danger mb-2">
        Вы уже оставили отзыв на этот продукт!
      </div>

      <Button disabled={isSubmitted || !isDirty || !isValid} type="submit">
        Оставить отзыв
      </Button>
    </form>
  );
}

export default LoginReviewForm;
