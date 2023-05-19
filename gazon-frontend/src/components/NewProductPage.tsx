import { AxiosError } from 'axios';
import React, { useState } from 'react'
import { Container, Button, Form } from 'react-bootstrap';
import { useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
import { addImageToProduct, addProduct, registerUser } from '../api/apiRequests';
import { ProductRequest } from '../api/apiTypes';

const NewProductPage = () => {
  const [error, setError] = useState<string>();
  const {
    handleSubmit,
    formState: { isDirty, isValid, isSubmitted },
    register,
  } = useForm<ProductRequest>();
  const navigate = useNavigate();

  const handleCancel = () => {
    navigate("/");
  };

  const onSubmit = handleSubmit((data) => {
    console.log(data);
    data.tags = ['1', '2', '3'];
    addProduct(data).then((productId) => {
      const formData = new FormData();
      formData.append("image", data.images[0]);
      console.log(formData);
      
      addImageToProduct(productId.data, formData);
    });
  });

  return (
    <Container className="px-lg-5">
      <Container className="display-6 mb-3 px-lg-5">New Product</Container>

      <Form className="d-flex mx-lg-5 gap-2 flex-column align-items-center">

        <Form.Group className="w-100">
          <Form.Label className="align-self-start">
            Name <span className="h5 text-danger">*</span>{" "}
          </Form.Label>
          <Form.Control
            required
            autoComplete="off"
            placeholder="product name"
            {...register("name", { required: true })}
          />
        </Form.Group>

        <Form.Group className="w-100">
          <Form.Label className="align-self-start">
            Description <span className="h5 text-danger">*</span>{" "}
          </Form.Label>
          <Form.Control
            required
            autoComplete="off"
            placeholder="desc"
            {...register("description", { required: true })}
          />
        </Form.Group>

        <Form.Group className="w-100">
          <Form.Label className="align-self-start">
            Price <span className="h5 text-danger">*</span>{" "}
          </Form.Label>
          <Form.Control
            required
            autoComplete="off"
            placeholder="price"
            type="number"
            {...register("price", { required: true })}
          />
        </Form.Group>

        <Form.Group className="w-100">
          <Form.Label className="align-self-start">
            Tags <span className="h5 text-danger">*</span>
          </Form.Label>
          <Form.Control
            autoComplete="off"
            placeholder="tags placeholder"
            {...register("tags", { required: true })}
          />
        </Form.Group>

        <Form.Group className="w-100">
          <Form.Label className="align-self-start">
            Images
          </Form.Label>
          <Form.Control
            autoComplete="off"
            placeholder="tags placeholder"
            type="file"
            {...register("images", { required: true })}
          />
        </Form.Group>

        {error !== null && <div className="text-danger">{error}</div>}

        <Container className="d-flex mt-2 gap-3 justify-content-center">
          <Button disabled={!isValid || !isDirty} onClick={onSubmit} size="lg">
            Post product
          </Button>

          <Button
            disabled={isSubmitted}
            variant="danger"
            size="lg"
            onClick={handleCancel}
          >
            Отмена
          </Button>
        </Container>
      </Form>
    </Container>
  );
}

export default NewProductPage