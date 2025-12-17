package com.example.skai.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.skai.ui.screens.auth.LoginScreen
import com.example.skai.ui.screens.auth.RegisterScreen
import com.example.skai.ui.screens.catalog.CatalogScreen
import com.example.skai.ui.screens.cart.CartScreen
import com.example.skai.ui.screens.orders.OrderHistoryScreen
import com.example.skai.ui.screens.admin.AdminScreen
import com.example.skai.ui.screens.product.ProductDetailScreen
import com.example.skai.ui.screens.checkout.CheckoutScreen
import com.example.skai.ui.screens.profile.ProfileScreen
import com.example.skai.ui.viewmodel.AuthViewModel
import com.example.skai.ui.viewmodel.CartViewModel

@Composable
fun SkaiNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel()
) {
    NavHost(
        navController = navController,
        startDestination = "login",
        modifier = modifier
    ) {
        composable("login") {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate("register")
                },
                onLoginSuccess = {
                    navController.navigate("catalog") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                viewModel = authViewModel
            )
        }
        
        composable("register") {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.navigate("login")
                },
                onRegisterSuccess = {
                    navController.navigate("catalog") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                viewModel = authViewModel
            )
        }
        
        composable("catalog") {
            CatalogScreen(
                onNavigateToProductDetail = { productId ->
                    navController.navigate("product_detail/$productId")
                },
                onNavigateToCart = {
                    navController.navigate("cart")
                },
                onNavigateToOrders = {
                    navController.navigate("orders")
                },
                onNavigateToAdmin = {
                    navController.navigate("admin")
                },
                onNavigateToProfile = {
                    navController.navigate("profile")
                },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                authViewModel = authViewModel,
                cartViewModel = cartViewModel
            )
        }
        
        composable("profile") {
            ProfileScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                authViewModel = authViewModel
            )
        }
        
        composable("product_detail/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            ProductDetailScreen(
                productId = productId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToCart = {
                    navController.navigate("cart")
                },
                authViewModel = authViewModel,
                cartViewModel = cartViewModel
            )
        }
        
        composable("cart") {
            CartScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToCheckout = {
                    navController.navigate("checkout")
                },
                authViewModel = authViewModel,
                cartViewModel = cartViewModel
            )
        }
        
        composable("checkout") {
            CheckoutScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onOrderConfirmed = {
                    navController.navigate("catalog") {
                        popUpTo("catalog") { inclusive = true }
                    }
                },
                authViewModel = authViewModel,
                cartViewModel = cartViewModel
            )
        }
        
        composable("orders") {
            OrderHistoryScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                authViewModel = authViewModel
            )
        }
        
        composable("admin") {
            AdminScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
