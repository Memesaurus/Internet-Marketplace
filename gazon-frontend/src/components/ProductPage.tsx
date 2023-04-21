import { QueryFunction, UseQueryResult, useQuery, useQueryClient } from '@tanstack/react-query'
import React, { useState } from 'react'
import { useParams } from 'react-router-dom'
import { getProduct } from '../api/apiRequests'
import { Product } from '../api/apiTypes'
import { Carousel, CarouselItem, Col, Container } from 'react-bootstrap'
import ProductImage from './ProductImage'

const ProductPage = () => {
  const { id } = useParams()

  const productQuery = useQuery({
    queryKey: ['product', id],
    queryFn: () => getProduct(id)
  })

  
  if (productQuery.isLoading) {
    return <div>Loading...</div>
  }

  const product: Product = productQuery.data?.data
  
  return (
    <>
      <h1>
        {product.name}
      </h1>

      <Container className='d-flex'>

        <ProductImage imageId={product.images[0]} productId={product.id} size='BIG' />

        <Container className='d-flex flex-column'>
            <div>
              Производитель амогус
            </div>
            <div>
              Рейтинг {product.rating}
            </div>
            {product.isInStock ? 
            <div>
              Есть в наличии
            </div> :
            <div>
              Нет в наличии
            </div>}
        </Container>

        <Container>
          КУПИТЬ
          <div>
              Цена {product.price}
          </div>
        </Container>
      </Container>
      
      <Container>
        {product.description}
      </Container>

      <Container>
        {product.reviews?.map((review) => (
          <div key={review.id}>
            {JSON.stringify(review)}
          </div>
        ))}
      </Container>
    </>
  )
}

export default ProductPage