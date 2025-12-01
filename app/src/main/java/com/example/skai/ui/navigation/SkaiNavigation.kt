package com.example.skai.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.skai.ui.screens.auth.LoginScreen
import com.example.skai.ui.screens.auth.RegisterScreen
import com.example.skai.ui.screens.catalog.CatalogScreen
import com.example.skai.ui.screens.cart.CartScreen
import com.example.skai.ui.screens.orders.OrderHistoryScreen
import com.example.skai.ui.screens.admin.AdminScreen
import com.example.skai.ui.screens.product.ProductDetailScreen
import com.example.skai.ui.screens.checkout.CheckoutScreen
import com.example.skai.ui.screens.profile.ProfileScreen

@Composable
fun SkaiNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
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
                }
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
                }
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
                }
            )
        }
        
        composable("profile") {
            ProfileScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
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
                }
            )
        }
        
        composable("cart") {
            CartScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToCheckout = {
                    navController.navigate("checkout")
                }
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
                }
            )
        }
        
        composable("orders") {
            OrderHistoryScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
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
