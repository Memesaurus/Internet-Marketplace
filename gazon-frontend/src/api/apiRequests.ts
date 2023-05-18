import { AxiosError } from "axios"
import { CompanyUserRegisterRequest, MemberUserRegisterRequest, Order, OrderRequest, Product, ProductRequest, Review, ReviewRequest, UserLoginRequest, UserStateResponse } from "./apiTypes"
import api from "./axiosConfiguration"

export const getCurrentState = async () => {
    return api.get<UserStateResponse>("/auth/current", {headers: {
        "STATE-REQUEST": true
    }})
}

export const login = async (data: UserLoginRequest) => {
    return api.post<UserStateResponse | AxiosError>("/auth/login", data);
}

export const logout = async () => {
    return api.post("/auth/logout");
}

export const registerUser = async (data: MemberUserRegisterRequest | CompanyUserRegisterRequest) => {
    return api.post("/auth/register", data);
}

export const placeOrder = async (data: OrderRequest) => {
    return api.post("/orders", data);
}

export const getUserOrders = async () => {
    return api.get<Order[]>("/orders/user");
}

export const cancelOrder = async (orderId: string) => {
    return api.post(`/orders/${orderId}/cancel`);
}

export const deliverOrder = async (orderId: string) => {
    return api.post(`/orders/${orderId}/deliver`);
}

export const getOrder = async (orderId: string) => {
    return api.get<Order>(`/orders/${orderId}`);
}

export const getAllProducts = async () => {
    return api.get<Product[]>("/products/home");
}

export const getProductsOfCompany = async (companyName: string) => {
    return api.get<Product[]>(`/products/${companyName}`);
}

export const getProduct = async (productId: string | undefined) => {
    return api.get<Product>(`/products/${productId}`);
}

export const addProduct = async (data: ProductRequest) => {
    return api.post("/products", data);
}

export const addImageToProduct = async (productId: string, image: ImageData) => {
    return api.post(`/products/${productId}/images`, image);
}

export const getProductImage = (productId: string, imageId: string): string => {
    const baseUrl = api.defaults.baseURL;
    
    return `${baseUrl}/products/${productId}/images/${imageId}`;
}

export const getReviewsOfProduct = async (productId: string) => {
    return api.get<Review[]>(`/products/${productId}/reviews`);
}

export const addReviewToProduct = async (productId: string, data: ReviewRequest) => {
    return api.post(`/products/${productId}/reviews`, data);
}

export const patchReview = async (productId: string, data: ReviewRequest) => {
    return api.patch(`/products/${productId}/reviews`, data);
}