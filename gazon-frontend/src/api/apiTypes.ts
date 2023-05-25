import { Tag } from "react-tag-input";

interface UserAuthRequest {
    username: string;
    password: string;
}

export interface UserStateResponse {
    username: string;
    role: UserRole;
    cartSize: number;
}

export interface UserLoginRequest extends UserAuthRequest {
    rememberMe: boolean;
}

interface UserRegisterRequest extends UserAuthRequest{
    email: string;
    additionalInfo: UserAdditionalInfo;
    role: UserRole;
    name: string;
}

export interface CompanyUserRegisterRequest extends UserRegisterRequest {
    description: string;
}

export interface MemberUserRegisterRequest extends UserLoginRequest {
    middlename: string;
    surname: string;
    age: number;
}

export enum UserRole {
    ADMIN = "ADMIN",
    MEMBER = "MEMBER",
    COMPANY = "COMPANY"
}

export interface OrderRequest {
    products: Map<string, number>;
    address: UserAddress;
}

export interface ProductRequest {
    name: string;
    description: string;
    price: number;
    tags: string[];
    images: FileList;
}

export interface ReviewRequest {
    rating: number;
    body: string;
}

export interface User {
    username: string;
    email: string;
    additionalInfo: UserAdditionalInfo;
    name: string;
}

export interface UserAdditionalInfo {
    address: UserAddress;
    phone: string;
}

export interface UserAddress {
    country: string;
    city: string;
    street: string;
    house: string;
}

export interface Order {
    id: string;
    createdAt: Date;
    cancellableUntil: Date;
    deliveryAddress: string;
    price: number;
    user: User;
    productOrders: CartItem[];
    status: boolean;
}

export interface CartItem {
    product: Product;
    quantity: number;
}

export interface Product {
    id: string;
    name: string;
    description: string;
    price: number;
    isInStock: boolean;
    rating: number;
    user: User;
    reviewCount?: number;
    images: string[];
    tags: string[];
    reviews?: Review[];
}

export interface Review {
    id: string;
    createdAt: Date;
    rating: number;
    body: string;
    user: User;
}