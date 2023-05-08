import React, { FC, useState } from "react";
import { useAppSelector } from "../redux/hooks";
import { UserRole } from "../api/apiTypes";

type Props = {
  NoLoginComponent?: React.FC;
  MemberComponent?: React.FC;
  CompanyComponent?: React.FC;
  adminDisplayBoth?: boolean; 
};

const WithLogin = ({
  NoLoginComponent,
  MemberComponent,
  CompanyComponent,
  adminDisplayBoth
}: Props) => {
  const WithLoginWrapper = (elementProps: any) => {
    const components: { [key in UserRole]: any } = {
      [UserRole.ADMIN]: [
        CompanyComponent && <CompanyComponent key={"Company"} {...elementProps} />,
        MemberComponent && <MemberComponent key={"member"} {...elementProps} />,
      ],
      [UserRole.COMPANY]: CompanyComponent && <CompanyComponent {...elementProps} />,
      [UserRole.MEMBER]: MemberComponent && <MemberComponent {...elementProps} />,
    };

    const userRole = useAppSelector((state) => state.user.role);

    if (userRole) {
      if (userRole === UserRole.ADMIN && !adminDisplayBoth) {
        return components[userRole][1];
      }

      return components[userRole];
    } else {
      return NoLoginComponent && <NoLoginComponent {...elementProps} />;
    }
  };

  return WithLoginWrapper;
};

export default WithLogin;
