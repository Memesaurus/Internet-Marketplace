import { createSlice, PayloadAction } from "@reduxjs/toolkit";

export interface UserState {
  username: null | string;
}

const initialState: UserState = {
  username: null,
};

export const userSlice = createSlice({
  name: "user",
  initialState,
  reducers: {
    setUser: (state, action: PayloadAction<UserState>) => {
      const { username } = action.payload;
      state.username = username;
    },
    clearUser: () => initialState,
  },
});

export const { setUser, clearUser } = userSlice.actions;
export default userSlice.reducer;
