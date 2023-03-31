import { CompanyUserRegisterRequest, MemberUserRegisterRequest, OrderRequest, ProductRequest, ReviewRequest, UserLoginRequest } from "./apiTypes"
import api from "./axiosConfiguration"

export const login = async (data: UserLoginRequest) => {
    return await api.post("/auth/login", data);
}

export const logout = async () => {
    return await api.post("/auth/logout");
}

export const register = async (data: MemberUserRegisterRequest | CompanyUserRegisterRequest) => {
    return await api.post("/auth/register", data);
}

export const placeOrder = async (data: OrderRequest) => {
    return await api.post("/orders", data);
}

export const getUserOrders = async () => {
    return await api.get("/orders/user");
}

export const cancelOrder = async (orderId: string) => {
    return await api.post(`/orders/${orderId}/cancel`);
}

export const deliverOrder = async (orderId: string) => {
    return await api.post(`/orders/${orderId}/deliver`);
}

export const getOrder = async (orderId: string) => {
    return await api.get(`/orders/${orderId}`);
}

export const getAllProducts = async () => {
    return await api.get("/products");
}

export const getProductsOfCompany = async (companyName: string) => {
    return await api.get(`/products/${companyName}`);
}

export const addProduct = async (data: ProductRequest) => {
    return await api.post("/products", data);
}

export const addImageToProduct = async (productId: string, image: ImageData) => {
    return await api.post(`/products/${productId}/images`, image);
}

export const getProductImage = async (productId: string, imageId: string) => {
    return await api.get(`/products/${productId}/images/${imageId}`);
}

export const getReviewsOfProduct = async (productId: string) => {
    return await api.get(`/products/${productId}/reviews`);
}

export const addReviewToProduct = async (productId: string, data: ReviewRequest) => {
    return await api.post(`/products/${productId}/reviews`, data);
}

export const patchReview = async (productId: string, data: ReviewRequest) => {
    return await api.patch(`/products/${productId}/reviews`, data);
}