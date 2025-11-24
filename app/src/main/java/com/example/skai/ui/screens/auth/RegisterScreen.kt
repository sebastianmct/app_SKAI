package com.example.skai.ui.screens.auth

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.skai.R
import com.example.skai.data.model.User
import com.example.skai.ui.viewmodel.AuthViewModel
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    
    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var addressError by remember { mutableStateOf<String?>(null) }
    
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    
    fun validateName(name: String): String? {
        return when {
            name.isBlank() -> "El nombre es requerido"
            name.length < 2 -> "El nombre debe tener al menos 2 caracteres"
            else -> null
        }
    }
    
    fun validateEmail(email: String): String? {
        return when {
            email.isBlank() -> "El email es requerido"
            !isValidEmail(email) -> "El formato del email no es válido"
            else -> null
        }
    }
    
    fun validatePassword(password: String): String? {
        return when {
            password.isBlank() -> "La contraseña es requerida"
            password.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
            !password.any { it.isDigit() } -> "La contraseña debe contener al menos un número"
            !password.any { it.isLetter() } -> "La contraseña debe contener al menos una letra"
            else -> null
        }
    }
    
    fun validateConfirmPassword(password: String, confirmPassword: String): String? {
        return when {
            confirmPassword.isBlank() -> "Confirma tu contraseña"
            password != confirmPassword -> "Las contraseñas no coinciden"
            else -> null
        }
    }
    
    fun validatePhone(phone: String): String? {
        return when {
            phone.isBlank() -> "El teléfono es requerido"
            phone.length < 8 -> "El teléfono debe tener al menos 8 dígitos"
            else -> null
        }
    }
    
    fun validateAddress(address: String): String? {
        return when {
            address.isBlank() -> "La dirección es requerida"
            address.length < 5 -> "La dirección debe tener al menos 5 caracteres"
            else -> null
        }
    }

    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            onRegisterSuccess()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF6A5ACD),
                        Color(0xFF9370DB)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.skaifondo),
                contentDescription = "SKAI Logo",
                modifier = Modifier
                    .size(100.dp)
                    .alpha(animateFloatAsState(
                        targetValue = if (isLoading) 0.5f else 1f,
                        animationSpec = tween(1000)
                    ).value)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Crear Cuenta",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))


            OutlinedTextField(
                value = name,
                onValueChange = { 
                    name = it
                    nameError = validateName(it)
                },
                label = { Text("Nombre Completo") },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = "Name")
                },
                modifier = Modifier.fillMaxWidth(),
                isError = nameError != null,
                supportingText = nameError?.let { { Text(it, color = Color.Red) } },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (nameError != null) Color.Red else Color.White,
                    unfocusedBorderColor = if (nameError != null) Color.Red else Color.White.copy(alpha = 0.7f),
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(12.dp))


            OutlinedTextField(
                value = email,
                onValueChange = { 
                    email = it
                    emailError = validateEmail(it)
                },
                label = { Text("Email") },
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = "Email")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                isError = emailError != null,
                supportingText = emailError?.let { { Text(it, color = Color.Red) } },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (emailError != null) Color.Red else Color.White,
                    unfocusedBorderColor = if (emailError != null) Color.Red else Color.White.copy(alpha = 0.7f),
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(12.dp))


            OutlinedTextField(
                value = phone,
                onValueChange = { 
                    phone = it
                    phoneError = validatePhone(it)
                },
                label = { Text("Teléfono") },
                leadingIcon = {
                    Icon(Icons.Default.Phone, contentDescription = "Phone")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth(),
                isError = phoneError != null,
                supportingText = phoneError?.let { { Text(it, color = Color.Red) } },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (phoneError != null) Color.Red else Color.White,
                    unfocusedBorderColor = if (phoneError != null) Color.Red else Color.White.copy(alpha = 0.7f),
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(12.dp))


            OutlinedTextField(
                value = address,
                onValueChange = { 
                    address = it
                    addressError = validateAddress(it)
                },
                label = { Text("Dirección") },
                leadingIcon = {
                    Icon(Icons.Default.LocationOn, contentDescription = "Address")
                },
                modifier = Modifier.fillMaxWidth(),
                isError = addressError != null,
                supportingText = addressError?.let { { Text(it, color = Color.Red) } },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (addressError != null) Color.Red else Color.White,
                    unfocusedBorderColor = if (addressError != null) Color.Red else Color.White.copy(alpha = 0.7f),
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(12.dp))


            OutlinedTextField(
                value = password,
                onValueChange = { 
                    password = it
                    passwordError = validatePassword(it)
                    confirmPasswordError = validateConfirmPassword(it, confirmPassword)
                },
                label = { Text("Contraseña") },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = "Password")
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = painterResource(
                                if (passwordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off
                            ),
                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                isError = passwordError != null,
                supportingText = passwordError?.let { { Text(it, color = Color.Red) } },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (passwordError != null) Color.Red else Color.White,
                    unfocusedBorderColor = if (passwordError != null) Color.Red else Color.White.copy(alpha = 0.7f),
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(12.dp))


            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { 
                    confirmPassword = it
                    confirmPasswordError = validateConfirmPassword(password, it)
                },
                label = { Text("Confirmar Contraseña") },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = "Confirm Password")
                },
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            painter = painterResource(
                                if (confirmPasswordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off
                            ),
                            contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                isError = confirmPasswordError != null,
                supportingText = confirmPasswordError?.let { { Text(it, color = Color.Red) } },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (confirmPasswordError != null) Color.Red else Color.White,
                    unfocusedBorderColor = if (confirmPasswordError != null) Color.Red else Color.White.copy(alpha = 0.7f),
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(24.dp))


            errorMessage?.let { error ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.1f))
                ) {
                    Text(
                        text = error,
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }


            Button(
                onClick = {
                    nameError = validateName(name)
                    emailError = validateEmail(email)
                    passwordError = validatePassword(password)
                    confirmPasswordError = validateConfirmPassword(password, confirmPassword)
                    phoneError = validatePhone(phone)
                    addressError = validateAddress(address)
                    
                    if (nameError == null && emailError == null && passwordError == null && 
                        confirmPasswordError == null && phoneError == null && addressError == null) {
                        viewModel.clearError()
                        val user = User(
                            id = UUID.randomUUID().toString(),
                            email = email,
                            password = password,
                            name = name,
                            phone = phone,
                            address = address,
                            isAdmin = false
                        )
                        viewModel.register(user)
                    }
                },
                enabled = !isLoading && name.isNotBlank() && email.isNotBlank() && 
                         password.isNotBlank() && confirmPassword.isNotBlank() && 
                         phone.isNotBlank() && address.isNotBlank() &&
                         nameError == null && emailError == null && passwordError == null &&
                         confirmPasswordError == null && phoneError == null && addressError == null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF6A5ACD)
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color(0xFF6A5ACD)
                    )
                } else {
                    Text(
                        text = "Registrarse",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))


            TextButton(
                onClick = onNavigateToLogin,
                enabled = !isLoading
            ) {
                Text(
                    text = "¿Ya tienes cuenta? Inicia Sesión",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }
    }
}
