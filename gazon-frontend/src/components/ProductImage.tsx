import React, { useEffect, useState } from 'react'
import { getProductImage as getProductImageUrl } from '../api/apiRequests'
import defaultImage from '../assets/default.png'
import { Image } from 'react-bootstrap';

type Props = {
    imageId: string | undefined;
    productId: string;
    className?: string;
    size?: 'BIG' | 'SMALL' | 'MINI' | 'MOBILE';
}

type dimensions = {
  width: number | string;
  height: number | string;
}

const ProductImage = ({ imageId, productId, className, size }: Props) => {
    const [imageUrl, setImageUrl] = useState<string>()

    const viewport = {
      width: window.innerWidth, 
      height: window.innerHeight
    } as dimensions;

    const sizes = {
      'BIG': {width: 500, height: 500} as dimensions,
      'SMALL': {width: 200, height: 200} as dimensions,
      'MOBILE': {width: '50vh', height: 300} as dimensions,
      'MINI': {width: 75 , height: 75} as dimensions
    }

    const areDimensionsBiggerThanScreen = ({ width, height }: dimensions): boolean => {
      return width >= viewport.width || height >= viewport.height;
    }

    if (size && areDimensionsBiggerThanScreen(sizes[size])) {
      size = 'MOBILE';
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