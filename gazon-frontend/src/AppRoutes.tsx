import React from 'react'
import { Route, Routes } from 'react-router-dom'
import NotFoundPage from './components/NotFoundPage'
import HomePage from './components/HomePage'
import LoginPage from './components/LoginPage'
import RegisterPage from './components/RegisterPage'
import ProductPage from './components/ProductPage'
import NewProductPage from './components/NewProductPage'

const AppRoutes = () => {
  return (
    <Routes>
      <Route path='/' element={<HomePage />} />
      <Route path='/login' element={<LoginPage />} />
      <Route path='/register' element={<RegisterPage />} />
      <Route path='/product/:id' element={<ProductPage />} />
      <Route path='/product/new' element={<NewProductPage/>} />
      <Route path='*' element={<NotFoundPage />} />
    </Routes>
  )
}

export default AppRoutes;
