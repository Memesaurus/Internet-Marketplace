import React, { ReactElement } from "react";
import { BsStar, BsStarFill, BsStarHalf } from "react-icons/bs";

const displayStars = (rating: number): ReactElement => {
    const stars: ReactElement[] = [];

    while (rating >= 0.5 && stars.length < 5) {
        if (rating >= 1) {
            rating--;
            stars.push(<BsStarFill key={stars.length} />);
        } else {
            rating -= 0.5;
            stars.push(<BsStarHalf key={stars.length} />)
        }
    }
    
    while (stars.length < 5) {
        stars.push(<BsStar key={stars.length} />);
    }
    
    return (
        <>
         {stars}
        </>
    );
}

export default displayStars;