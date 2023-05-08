import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import { UserRole } from "../api/apiTypes";

export interface UserState {
  username: null | string;
  role: UserRole | null;
}

const initialState: UserState = {
  username: null,
  role: null
};

export const userSlice = createSlice({
  name: "user",
  initialState,
  reducers: {
    setUser: (state, action: PayloadAction<UserState>) => {
      const { username, role } = action.payload;
      state.username = username;
      state.role = role;
    },
    clearUser: () => initialState,
  },
});

export const { setUser, clearUser } = userSlice.actions;
export default userSlice.reducer;
