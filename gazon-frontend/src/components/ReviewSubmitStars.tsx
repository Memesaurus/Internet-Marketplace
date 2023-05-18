import React, { useState } from "react";
import { SetStateAction } from "react";
import { BsStar, BsStarFill, BsStarHalf } from "react-icons/bs";

type Props = {
  setRating: React.Dispatch<SetStateAction<number>>;
};

const ReviewSubmitStars = (props: Props) => {
  const initializeStarArray = (): JSX.Element[] => {
    const array: JSX.Element[] = [];

    for (let i = 0; i < 5; i++) {
      array.push(<BsStar key={i} />);
    }

    return array;
  };

  const [stars, setStars] = useState<JSX.Element[]>(initializeStarArray());

  const handleClick = (
    event: any,
    key: number
  ) => {
    const newStars = [...stars];
    let currentRating = key + 1;

    for (let i = 0; i < newStars.length; i++) {
      const props = newStars[i].props;
      if (i <= key) {
        newStars[i] = (<BsStarFill {...props} />);
      } else {
        newStars[i] = (<BsStar {...props} />);
      }
    }

    const clickedDivXY: DOMRect = event.target.getBoundingClientRect();

    const divX = clickedDivXY.left + clickedDivXY.width / 2;
    const mouseX = event.clientX;

    const deltaX = divX - mouseX;
    const clickedElProps = newStars[key].props;
    
    if (deltaX >= 0) {
      newStars[key] = <BsStarHalf {...clickedElProps} />;
      currentRating -= 0.5
    }

    setStars(newStars);
    props.setRating(currentRating);
  };

  return (
    <div className="d-flex">
      {stars.map((star, key) => (
        <div key={key} onClick={(event) => handleClick(event, key)}>
          {star}
        </div>
      ))}
    </div>
  );
};

export default ReviewSubmitStars;
