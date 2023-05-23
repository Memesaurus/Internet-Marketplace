import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import { UserRole } from "../api/apiTypes";

export interface UserState {
  username: string | null;
  role: UserRole | null;
  cartSize: number | null;
}

const initialState: UserState = {
  username: null,
  role: null,
  cartSize: null,
};

export const userSlice = createSlice({
  name: "user",
  initialState,
  reducers: {
    setUser: (state, action: PayloadAction<UserState>) => {
      const { username, role, cartSize } = action.payload;
      state.username = username;
      state.role = role;
      state.cartSize = cartSize;
    },
    clearUser: () => initialState,
    decreaseCartSize: (state) => {
      if (state.cartSize) {
        state.cartSize = state.cartSize - 1;
      }
    }, 
    increaseCartSize: (state) => {
      if (state.cartSize) {
        state.cartSize = state.cartSize + 1;
      } else {
        state.cartSize = 1;
      }
    },
    clearCart: (state) => {
      state.cartSize = 0;
    }
  },
});

export const { setUser, clearUser, decreaseCartSize, increaseCartSize, clearCart } = userSlice.actions;
export default userSlice.reducer;
