import React, { FormEvent, useState } from "react";
import { Container, Button, Form } from "react-bootstrap";
import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import { addImageToProduct, addProduct } from "../api/apiRequests";
import { ProductRequest } from "../api/apiTypes";
import { WithContext as ReactTags, Tag } from "react-tag-input";
import './reactTagsStyling.css'

const NewProductPage = () => {
  const [error, setError] = useState<string>();
  const [tags, setTags] = useState<Tag[]>([]);
  const {
    handleSubmit,
    formState: { isDirty, isValid, isSubmitted },
    register,
  } = useForm<ProductRequest>();
  const navigate = useNavigate();
  const [isButton, setIsButton] = useState<boolean>(true);
  const handleCancel = () => {
    navigate("/");
  };

  const checkForMaxImages = (event: FormEvent<HTMLInputElement>) => {
    const input = event.currentTarget;

    if (!input.files) {
      return;
    }

    const keys = Object.keys(input.files);
    if (keys.length > 5) {
      setError("Слишком много изображений, максимум 5");
      setIsButton(false);
    } else {
      setError("");
      setIsButton(true);
    }
  };

  const onSubmit = handleSubmit((data) => {
    if (tags.length === 0) {
      return;
    }

    data.tags = tags.map((tag) => tag.text);

    addProduct(data).then(async (productId) => {
      for (const i in data.images) {
        if (['length', 'item'].includes(i)) {
          continue;
        }

        const formData = new FormData();
        formData.append("image", data.images[i]);
        
        await addImageToProduct(productId.data, formData);
      }
    });
  });

  const handleDelete = (i: number) => {
    setTags(tags.filter((tag, index) => index !== i));
  };

  const handleAddition = (tag: Tag) => {
    setTags([...tags, tag]);
  };

  const handleDrag = (tag: Tag, currPos: number, newPos: number) => {
    const newTags = tags.slice();

    newTags.splice(currPos, 1);
    newTags.splice(newPos, 0, tag);

    setTags(newTags);
  };

  return (
    <Container className="px-lg-5">
      <Container className="display-6 mb-3 px-lg-5">New Product</Container>

      <Form className="d-flex mx-lg-5 gap-2 flex-column align-items-center">
        <Form.Group className="w-100">
          <Form.Label className="align-self-start">
            Наименование товара <span className="h5 text-danger">*</span>{" "}
          </Form.Label>
          <Form.Control
            required
            autoComplete="off"
            placeholder="наименование товара"
            {...register("name", { required: true })}
          />
        </Form.Group>

        <Form.Group className="w-100">
          <Form.Label className="align-self-start">
            Описание товара <span className="h5 text-danger">*</span>{" "}
          </Form.Label>
          <Form.Control
            required
            autoComplete="off"
            placeholder="описание товара"
            {...register("description", { required: true })}
          />
        </Form.Group>

        <Form.Group className="w-100">
          <Form.Label className="align-self-start">
            Цена <span className="h5 text-danger">*</span>{" "}
          </Form.Label>
          <Form.Control
            required
            autoComplete="off"
            placeholder="цена"
            type="number"
            {...register("price", { required: true })}
          />
        </Form.Group>

        <Form.Group className="w-100">
          <Form.Label className="align-self-start">
            Теги <span className="h5 text-danger">*</span>
          </Form.Label>

          <ReactTags
            tags={tags}
            placeholder="теги"
            delimiters={[188, 13]}
            handleDelete={handleDelete}
            handleAddition={handleAddition}
            handleDrag={handleDrag}
            inputFieldPosition="bottom"
            autocomplete
            {...register("tags")}
          />
        </Form.Group>

        {error !== null && <div className="text-danger">{error}</div>}

        <Form.Group className="w-100">
          <Form.Label className="align-self-start">Изображения</Form.Label>
          <Form.Control
            autoComplete="off"
            placeholder="изображения"
            type="file"
            accept="image/*"
            multiple
            onInput={checkForMaxImages}
            {...register("images", { required: true })}
          />
        </Form.Group>

        <Container className="d-flex mt-2 gap-3 justify-content-center">
          <Button
            disabled={!isValid || !isDirty || !isButton}
            onClick={onSubmit}
            size="lg"
          >
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
};

export default NewProductPage;
