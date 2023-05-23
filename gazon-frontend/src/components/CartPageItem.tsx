import React, { useState } from 'react'
import { Product } from '../api/apiTypes'
import ProductImage from './ProductImage'
import { Button } from 'react-bootstrap'
import { addToCart, removeFromCart } from '../api/apiRequests'
import { useAppDispatch } from '../redux/hooks'
import { decreaseCartSize } from '../redux/userSlice'

type Props = {
    product: Product,
    quantity: number
}

const CartPageItem = ({ product, quantity }: Props) => {
  const dispatch = useAppDispatch();
  const [d, setD] = useState<string>('d-flex');
  const [quant, setQuant] = useState<number>(quantity);

  const reduceItemHandler = () => {
    removeFromCart(product.id).then(() => {
      setQuant(() => quant - 1);

      if (quant === 1) {
        dispatch(decreaseCartSize());
        setD('d-none')
      }
    })
  }

  const increaseItemHandler = () => {
    addToCart(product.id).then(() => {
      setQuant(() => quant + 1);
    })
  }

  return (
    <div className={`${d} pb-2 justify-content-between align-items-center`}>
        <ProductImage imageId={product.images[0]} productId={product.id} size='MINI' />

        <div>
          {product.name}
        </div>

        <div className='d-flex gap-2 align-items-center'>
          <Button onClick={reduceItemHandler}>
            {"<"}
          </Button>
            {quant}
          <Button onClick={increaseItemHandler}>
            {">"}
          </Button>
        </div>
    </div>
  )
}

export default CartPageItem