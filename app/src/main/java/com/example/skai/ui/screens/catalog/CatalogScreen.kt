package com.example.skai.ui.screens.catalog

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.ui.draw.scale


import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.List

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.skai.R
import com.example.skai.data.model.Product
import com.example.skai.ui.viewmodel.AuthViewModel
import com.example.skai.ui.viewmodel.CartViewModel
import com.example.skai.ui.viewmodel.ProductViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    onNavigateToProductDetail: (String) -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToAdmin: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onLogout: () -> Unit,
    productViewModel: ProductViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val products by productViewModel.filteredProducts.collectAsStateWithLifecycle()
    val categories by remember { mutableStateOf(productViewModel.getCategories()) }
    val selectedCategory by productViewModel.selectedCategory.collectAsStateWithLifecycle()
    val searchQuery by productViewModel.searchQuery.collectAsStateWithLifecycle()
    val isLoading by productViewModel.isLoading.collectAsStateWithLifecycle()
    val currentUser by authViewModel.currentUser.collectAsStateWithLifecycle()
    var cartItemCount by remember { mutableStateOf(0) }

    LaunchedEffect(currentUser) {
        currentUser?.let { user ->
            cartItemCount = cartViewModel.getCartItemCount(user.id)
        } ?: run {
            cartItemCount = 0
        }
    }

    LaunchedEffect(currentUser) {
        currentUser?.let { user ->
            cartViewModel.loadCartItems(user.id)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {

        TopAppBar(
            title = {
                Text(
                    text = "SKAI",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            },
            actions = {

                BadgedBox(
                    badge = {
                        if (cartItemCount > 0) {
                            Badge {
                                Text(cartItemCount.toString(), color = Color.White, fontSize = 12.sp)
                            }
                        }
                    }
                ) {
                    IconButton(onClick = onNavigateToCart) {
                        Icon(
                            Icons.Default.ShoppingCart,
                            contentDescription = "Carrito",
                            tint = Color.White
                        )
                    }
                }


                if (currentUser?.isAdmin == true) {
                    IconButton(onClick = onNavigateToAdmin) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = "Admin",
                            tint = Color.White
                        )
                    }
                }


                IconButton(onClick = onNavigateToOrders) {
                    Icon(Icons.Default.List, contentDescription = "Pedidos", tint = Color.White)
                }


                var showMenu by remember { mutableStateOf(false) }

                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(
                            Icons.Filled.Person,
                            contentDescription = "Perfil",
                            tint = Color.White
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        currentUser?.let { user ->

                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Filled.Person, contentDescription = "Usuario", modifier = Modifier.size(20.dp))
                                        Spacer(Modifier.width(8.dp))
                                        Text(user.name)
                                    }
                                },
                                onClick = {
                                    showMenu = false
                                    onNavigateToProfile()
                                }
                            )

                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Filled.Email, contentDescription = "Correo", modifier = Modifier.size(20.dp))
                                        Spacer(Modifier.width(8.dp))
                                        Text(user.email)
                                    }
                                },
                                onClick = { showMenu = false }
                            )
                        }

                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Filled.Logout, contentDescription = "Cerrar sesión", modifier = Modifier.size(20.dp), tint = Color.Red)
                                    Spacer(Modifier.width(8.dp))
                                    Text("Cerrar Sesión", color = Color.Red)
                                }
                            },
                            onClick = {
                                showMenu = false
                                authViewModel.logout()
                                onLogout()
                            }
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF6A5ACD)
            )
        )

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { productViewModel.setSearchQuery(it) },
            placeholder = { Text("Buscar productos...") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Buscar")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp)
        )


        LazyRow(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            item {
                FilterChip(
                    onClick = { productViewModel.setCategory(null) },
                    label = { Text("Todos") },
                    selected = selectedCategory == null
                )
            }

            items(categories) { category ->
                FilterChip(
                    onClick = { productViewModel.setCategory(category) },
                    label = { Text(category) },
                    selected = selectedCategory == category
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))


        AnimatedVisibility(
            visible = isLoading,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF6A5ACD))
            }
        }


        AnimatedVisibility(
            visible = !isLoading,
            enter = fadeIn(animationSpec = tween(300)),
            exit = fadeOut(animationSpec = tween(300))
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = products,
                    key = { it.id }
                ) { product ->
                    ProductCard(
                        product = product,
                        onProductClick = { onNavigateToProductDetail(product.id) },
                        onAddToCart = { size ->
                            currentUser?.let { user ->
                                val cartItem = com.example.skai.data.model.CartItem(
                                    id = UUID.randomUUID().toString(),
                                    userId = user.id,
                                    productId = product.id,
                                    productName = product.name,
                                    productPrice = product.price,
                                    productImage = product.images.firstOrNull() ?: "",
                                    selectedSize = size,
                                    quantity = 1
                                )
                                cartViewModel.addToCart(cartItem)
                            }
                        }
                    )
                }
            }
        }
    }
}



@Composable
fun ProductCard(
    product: Product,
    onProductClick: () -> Unit,
    onAddToCart: (String) -> Unit
) {
    var showSizeSelector by remember { mutableStateOf(false) }
    var selectedSize by remember { mutableStateOf("") }
    

    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ), label = "scale"
    )

    fun formatPrice(price: Double): String {
        val priceClp = price * 1000
        return String.format("CLP $%,.0f", priceClp).replace(",", ".")
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable(
                onClick = {
                    isPressed = true
                    onProductClick()
                },
                indication = null,
                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                AsyncImage(
                    model = product.images.firstOrNull() ?: "",
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.ic_launcher_foreground),
                    error = painterResource(R.drawable.ic_launcher_foreground)
                )


                if (product.stock <= 0) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "SIN STOCK",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }


            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = product.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))


                Text(
                    text = formatPrice(product.price),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF6A5ACD)
                )

                Spacer(modifier = Modifier.height(8.dp))


                Button(
                    onClick = {
                        if (product.sizes.size == 1) {
                            onAddToCart(product.sizes.first())
                        } else {
                            showSizeSelector = true
                        }
                    },
                    enabled = product.stock > 0,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6A5ACD)
                    )
                ) {
                    Text(
                        text = if (product.stock > 0) "Agregar" else "Sin Stock",
                        fontSize = 12.sp
                    )
                }
            }
        }
    }



    if (showSizeSelector) {
        AlertDialog(
            onDismissRequest = { showSizeSelector = false },
            title = { Text("Seleccionar Talla") },
            text = {
                Column {
                    product.sizes.forEach { size ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedSize = size
                                    onAddToCart(size)
                                    showSizeSelector = false
                                }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedSize == size,
                                onClick = {
                                    selectedSize = size
                                    onAddToCart(size)
                                    showSizeSelector = false
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = size)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { showSizeSelector = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}
