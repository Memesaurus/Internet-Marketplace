import React, { useEffect, useState } from 'react'
import { getProductImage as getProductImageUrl } from '../api/apiRequests'
import defaultImage from '../assets/default.png'
import { Image } from 'react-bootstrap';

type Props = {
    imageId: string | undefined;
    productId: string;
    className?: string;
    size?: 'BIG' | 'SMALL';
}

const ProductImage = ({ imageId, productId, className, size }: Props) => {
    const [imageUrl, setImageUrl] = useState<string>()
    
  const sizes = {
    'BIG': {width: 600, height: 600},
    'SMALL': {width: 200, height: 200}
  }

    const setDefaultImage = () => {
        setImageUrl(defaultImage)
    }

    useEffect(() => {
      imageId ? setImageUrl(getProductImageUrl(productId, imageId)) : 
        setDefaultImage()
    }, []);
    

  return(
    <Image className={className} src={imageUrl} onError={setDefaultImage} style={size && sizes[size]} />
  ) 
}

export default ProductImage