import React from 'react'
import ProductImage from './ProductImage';
import { Product } from '../api/apiTypes';
import { AiFillStar } from "react-icons/ai";

type Props = {
    product: Product
}

const ProductCard = ({ product }: Props) => {
    const intl = Intl.NumberFormat('ru-RU', {style: 'currency', currency: 'RUB'});
    
    const counterStyle = {marginTop: '2px', marginRight: '5px'}

  return (
    <div className='h5'>
        <ProductImage imageId={product.images?.at(0)} productId={product.id} size='SMALL' />

        <div className='pt-1'>
            {intl.format(product.price)}   
        </div>

        <div className='text-truncate' style={{width: "200px"}}>
            {product.name}
        </div>

        {
            !product.isInStock &&
            <div className='text-danger'>
                Нет в наличии
            </div>
        }
        
        <div className='d-flex gap-3 pt-1'>
            <div className='d-flex align-items-center'>
                <AiFillStar style={counterStyle} />
                {product.rating.toFixed(2)}
            </div>

        </div>
    </div>
  )
}

export default ProductCard