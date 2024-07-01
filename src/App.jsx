import React from "react";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import Home from "./pages/home";
import Login from "./pages/login";
import About from "./pages/about";
import Designs from "./pages/designs";
import Collections from "./pages/collections";
import Blog from "./pages/blog";
import Register from "./pages/register";
import Layout from "./components/Layout";
import Error from "./pages/error";
import Admin from "./pages/admin";
import ProductDetails from "./pages/product-details";
import ManagerRequest from "./pages/manager/request";
import ManagerOrder from "./pages/manager/order";
import ProtectedRoute from "./components/ProtectedRoute";
import authService from "./services/authService";
import Designer from "./pages/designer";
import ProductionStaff from "./pages/production-staff";
import ProcessRequests from "./pages/saler/process_requests";
import ReceiveRequests from "./pages/saler/receive_requests";
import CartRequest from "./pages/cart/request";
import CartOrder from "./pages/cart/order";
import Dashboard from "./pages/dashboard";

const getCurrentUser = () => {
  return authService.getCurrentUser();
};

const isAuthenticated = () => {
  const user = getCurrentUser();
  return user && user.token;
};

const isAdmin = () => {
  const user = getCurrentUser();
  return user && user.title === "ADMIN";
};

const isCustomer = () => {
  const user = getCurrentUser();
  return user && user.title === "CUSTOMER";
};

function App() {
  const router = createBrowserRouter([
    {
      path: "/",
      element: <Layout />,

      children: [
        {
          path: "/",
          element: <Home />,
        },
        {
          path: "/about",
          element: <About />,
        },
        {
          path: "/designs",
          element: <Designs />,
        },
        {
          path: "/collections",
          element: <Collections />,
        },
        {
          path: "/blog",
          element: <Blog />,
        },
        {
          path: "/register",
          element: <Register />,
        },
        {
          path: "/login",
          element: <Login />,
        },
        {
          path: "/product-details/:productId",
          element: <ProductDetails />,
        },
        {
          path: "/cart/request",
          element: <CartRequest />,
        },
        {
          path: "/cart/order/:requestID",
          element: <CartOrder />,
        },
      ],
    },
    {
      path: "/error",
      element: <Error />,
    },
    {
      path: "/admin",
      element: <ProtectedRoute element={<Admin />} isAllowed={isAdmin()} />,
    },
    {
      path: "/saler/receive_requests",
      element: (
        <ProtectedRoute
          element={<ReceiveRequests />}
          isAllowed={isAuthenticated()}
        />
      ),
    },
    {
      path: "/saler/process_requests",
      element: (
        <ProtectedRoute
          element={<ProcessRequests />}
          isAllowed={isAuthenticated()}
        />
      ),
    },
    {
      path: "/manager/request",
      element: (
        <ProtectedRoute
          element={<ManagerRequest />}
          isAllowed={isAuthenticated()}
        />
      ),
    },
    {
      path: "/manager/order",
      element: (
        <ProtectedRoute
          element={<ManagerOrder />}
          isAllowed={isAuthenticated()}
        />
      ),
    },
    {
      path: "/designer",
      element: (
        <ProtectedRoute element={<Designer />} isAllowed={isAuthenticated()} />
      ),
    },
    {
      path: "/production-staff",
      element: (
        <ProtectedRoute
          element={<ProductionStaff />}
          isAllowed={isAuthenticated()}
        />
      ),
    },
    {
      path: "/dashboard",
      element: (
        <ProtectedRoute element={<Dashboard />} isAllowed={isAuthenticated()} />
      ),
    },
  ]);

  return <RouterProvider router={router} />;
}

export default App;