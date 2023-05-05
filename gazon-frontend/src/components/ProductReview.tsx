import React from "react";
import { Review } from "../api/apiTypes";
import { OverlayTrigger, Tooltip, TooltipProps } from "react-bootstrap";
import displayStars from "../utils/StarUtils";

type Props = {
  review: Review;
};

const ProductReview = ({ review }: Props) => {
  const intl = new Intl.RelativeTimeFormat("ru", {
    numeric: "auto",
    style: "short",
  });
  const reviewTime = new Date(review.createdAt);
  const timeDiffirence: number = Date.now() - reviewTime.getTime();

  const getRelativeTime = (time: number): string => {
    let diff = Math.round(time / (1000 * 60 * 60 * 24));
    let unit: Intl.RelativeTimeFormatUnit = "day";

    if (diff >= 30) {
      unit = "month";
      diff = Math.round(diff / 30);
    }

    if (unit === "month" && diff >= 12) {
      unit = "year";
      diff = Math.round(diff / 12);
    }

    return intl.format(-diff, unit);
  };

  const renderDateTooltip = (props: TooltipProps) => {
    return (
      <Tooltip id="date-tooltip" {...props}>
        {Intl.DateTimeFormat("ru", { dateStyle: "long" }).format(reviewTime)}
      </Tooltip>
    );
  };

  return (
    <div className="pb-3">
      <div className="d-flex align-items-center">
        <div className="display-lg-6 flex-fill">
          <b>{review.user.username}</b>
        </div>

        <OverlayTrigger placement="top" overlay={renderDateTooltip}>
          <div>{getRelativeTime(timeDiffirence)}</div>
        </OverlayTrigger>

        <div className="mx-3 mb-1">{displayStars(review.rating)}</div>
      </div>

      <div className="p-1">{review.body}</div>
    </div>
  );
};

export default ProductReview;
