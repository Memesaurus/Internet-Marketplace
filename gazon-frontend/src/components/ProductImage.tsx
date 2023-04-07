import React, { useEffect, useState } from 'react'
import { getProductImage as getProductImageUrl } from '../api/apiRequests'
import defaultImage from '../assets/default.png'
import { Image } from 'react-bootstrap';

type Props = {
    imageId: string | undefined;
    productId: string;
}

const ProductImage = ({ imageId, productId }: Props) => {
    const [imageUrl, setImageUrl] = useState<string>()
    
    const setDefaultImage = () => {
        setImageUrl(defaultImage)
    }

    useEffect(() => {
      imageId ? setImageUrl(getProductImageUrl(productId, imageId)) : 
        setDefaultImage()
    }, []);
    

  return(
    <Image src={imageUrl} onError={setDefaultImage} style={{width: 200, height: 200}} />
  ) 
}

export default ProductImage