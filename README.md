# SKAI - E-commerce Mobile App

Aplicación móvil de e-commerce desarrollada en Android con Kotlin, utilizando Jetpack Compose para la interfaz de usuario, arquitectura MVVM y backend Spring Boot conectado a **Oracle Cloud Infrastructure (OCI)**.

## Integrantes

- **Sebastian Caamaño**
- **Vicente Ordenes**

## Arquitectura del Sistema

```
┌─────────────────┐     HTTP/REST      ┌─────────────────┐      JDBC       ┌─────────────────┐
│   Android App   │ ◄──────────────► │  Spring Boot    │ ◄─────────────► │   Oracle DB     │
│  (Kotlin/Compose)│                   │    Backend      │                  │     (OCI)       │
└─────────────────┘                    └─────────────────┘                  └─────────────────┘
```

### Tecnología de Base de Datos

**Oracle Cloud Infrastructure (OCI) - Autonomous Database**

La aplicación utiliza una base de datos Oracle en la nube, NO almacenamiento local (Room/SQLite). Esto proporciona:

- ✅ **Persistencia real en la nube**: Los datos se guardan en Oracle Autonomous Database
- ✅ **Sincronización multi-dispositivo**: Cualquier dispositivo accede a los mismos datos
- ✅ **Escalabilidad empresarial**: Base de datos Oracle de nivel producción
- ✅ **Seguridad**: Conexión cifrada con Oracle Wallet

## Funcionalidades

### Funcionalidades Principales
- **Autenticación de usuarios**: Login y registro con validación contra BD Oracle
- **Catálogo de productos**: Productos almacenados en Oracle, con búsqueda y filtrado
- **Carrito de compras**: Gestión completa persistida en la nube
- **Pedidos**: Creación y seguimiento de pedidos en tiempo real
- **Notificaciones push locales**: Eventos importantes (pedido confirmado, etc.)
- **Integración con API externa**: Consumo de productos desde Fake Store API
- **Panel de administración**: CRUD completo de productos (solo admin)

### Funcionalidades de Hardware
- **Cámara**: Captura de fotos para perfil/productos
- **Galería**: Selección de imágenes del dispositivo
- **Notificaciones Push Locales**: Alertas de pedidos y productos

## Backend - Spring Boot

El backend está desarrollado en **Kotlin con Spring Boot** y se conecta a Oracle OCI.

### Estructura del Backend

```
skai-backend/
├── src/main/kotlin/com/example/skai/
│   ├── config/
│   │   ├── CorsConfig.kt          # Configuración CORS
│   │   └── DataInitializer.kt     # Datos iniciales (usuarios/productos)
│   ├── controller/
│   │   ├── CartController.kt      # API del carrito
│   │   ├── OrderController.kt     # API de pedidos
│   │   ├── ProductController.kt   # API de productos
│   │   └── UserController.kt      # API de usuarios
│   ├── model/
│   │   ├── CartItem.kt
│   │   ├── Order.kt
│   │   ├── Product.kt
│   │   └── User.kt
│   ├── repository/                # JPA Repositories (Oracle)
│   ├── service/                   # Lógica de negocio
│   └── SkaiApplication.kt
└── src/main/resources/
    ├── application.properties     # Configuración Oracle
    └── Wallet_*/                  # Oracle Wallet (credenciales)
```

### Conexión a Oracle OCI

```properties
# application.properties
spring.datasource.url=jdbc:oracle:thin:@skai_high?TNS_ADMIN=src/main/resources/Wallet_SKAI
spring.datasource.username=ADMIN
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.OracleDialect
```

## Endpoints REST (Backend Real)

**Base URL**: `http://10.0.2.2:8080/api/` (emulador) o `http://<IP>:8080/api/`

### Usuarios
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/users/login` | Autenticación |
| POST | `/users/register` | Registro |
| PUT | `/users/{id}` | Actualizar usuario |

### Productos
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/products` | Listar todos |
| GET | `/products/{id}` | Obtener por ID |
| GET | `/products?category=X` | Filtrar por categoría |
| POST | `/products` | Crear producto |
| PUT | `/products/{id}` | Actualizar |
| DELETE | `/products/{id}` | Eliminar |

### Carrito
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/cart/{userId}` | Obtener carrito |
| POST | `/cart` | Agregar item |
| PUT | `/cart/{id}` | Actualizar cantidad |
| DELETE | `/cart/{id}` | Eliminar item |

### Pedidos
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/orders/user/{userId}` | Pedidos del usuario |
| POST | `/orders` | Crear pedido |
| PUT | `/orders/{id}/status` | Actualizar estado |

## Tecnologías Utilizadas

### App Android
| Tecnología | Uso |
|------------|-----|
| Kotlin | Lenguaje principal |
| Jetpack Compose | UI declarativa |
| Material Design 3 | Sistema de diseño |
| Hilt | Inyección de dependencias |
| Retrofit + OkHttp | Cliente HTTP |
| Coroutines + StateFlow | Asincronía y estado reactivo |
| Coil | Carga de imágenes |
| Accompanist | Permisos de cámara |

### Backend
| Tecnología | Uso |
|------------|-----|
| Spring Boot 3 | Framework backend |
| Kotlin | Lenguaje |
| Spring Data JPA | ORM |
| Oracle JDBC | Driver de BD |
| Oracle Wallet | Autenticación segura |

### Base de Datos
| Tecnología | Uso |
|------------|-----|
| Oracle Autonomous DB | Base de datos en OCI |
| JPA/Hibernate | Mapeo objeto-relacional |

### Testing
| Tecnología | Uso |
|------------|-----|
| JUnit 5 | Framework base |
| Kotest | Tests en estilo BDD |
| MockK | Mocking para Kotlin |
| Coroutines Test | Testing asíncrono |

## Requisitos

### App Android
- Android Studio Hedgehog 2023.1.1+
- JDK 17+
- Android SDK 24 (mínimo) - 34 (target)

### Backend
- JDK 17+
- Gradle 8+
- Cuenta Oracle Cloud (OCI)
- Oracle Wallet configurado

## Pasos para Ejecutar

### 1. Configurar y Ejecutar el Backend

```bash
cd skai-backend

# Asegúrate de tener el Wallet de Oracle en src/main/resources/
# Configura las credenciales en application.properties

# Ejecutar
./gradlew bootRun
```

El backend iniciará en `http://localhost:8080` y:
- Creará las tablas automáticamente en Oracle
- Insertará usuarios y productos iniciales

### 2. Ejecutar la App Android

```bash
cd app_SKAI

# Abrir en Android Studio y ejecutar en emulador/dispositivo
```

**Nota**: El emulador usa `10.0.2.2` para conectar a localhost del host.

### 3. Probar la Aplicación

**Usuario Administrador**:
- Email: `admin@skai.com`
- Password: `admin123`

**Usuario Cliente**:
- Email: `cliente@skai.com`
- Password: `cliente123`

## Estructura del Proyecto Android

```
app/src/main/java/com/example/skai/
├── data/
│   ├── api/              # Interfaces Retrofit
│   │   ├── ProductApiService.kt
│   │   ├── UserApiService.kt
│   │   ├── CartApiService.kt
│   │   └── OrderApiService.kt
│   ├── model/            # Entidades
│   │   ├── Product.kt
│   │   ├── User.kt
│   │   ├── CartItem.kt
│   │   └── Order.kt
│   └── repository/       # Repositorios (acceso a API)
├── di/
│   └── NetworkModule.kt  # Configuración Retrofit/Hilt
├── ui/
│   ├── screens/          # Pantallas Compose
│   │   ├── auth/         # Login, Register
│   │   ├── catalog/      # Catálogo
│   │   ├── product/      # Detalle producto
│   │   ├── cart/         # Carrito
│   │   ├── orders/       # Historial
│   │   ├── profile/      # Perfil
│   │   └── admin/        # Panel admin
│   ├── viewmodel/        # ViewModels
│   ├── components/       # Componentes reutilizables
│   └── navigation/       # Navegación
└── utils/                # Utilidades
```

## Testing

```bash
# Ejecutar todos los tests
./gradlew test

# Tests específicos
./gradlew test --tests "AuthViewModelTest"
./gradlew test --tests "ProductViewModelTest"
```

### Tests Incluidos
- `AuthViewModelTest` - Login, registro, logout
- `ProductViewModelTest` - CRUD productos
- `CartViewModelTest` - Gestión carrito
- `OrderViewModelTest` - Creación pedidos
- `ExternalProductRepositoryTest` - API externa

## Datos Iniciales

Al iniciar el backend, se crean automáticamente:

### Usuarios
| Email | Password | Rol |
|-------|----------|-----|
| admin@skai.com | admin123 | Administrador |
| cliente@skai.com | cliente123 | Cliente |

### Productos
| Nombre | Categoría | Precio |
|--------|-----------|--------|
| Camisa Casual Azul | Camisas | $29.990 |
| Pantalón Jeans Clásico | Pantalones | $49.990 |
| Vestido Elegante Negro | Vestidos | $79.990 |
| Zapatos Deportivos Blancos | Zapatos | $89.990 |
| Cinturón de Cuero Marrón | Accesorios | $24.990 |
| Chaqueta de Cuero Negra | Chaquetas | $129.990 |

## Diferencias con Almacenamiento Local

| Aspecto | Room (Local) | Oracle OCI (Nube) |
|---------|--------------|-------------------|
| Persistencia | Solo en dispositivo | En la nube |
| Sincronización | No | Multi-dispositivo |
| Escalabilidad | Limitada | Empresarial |
| Backup | Manual | Automático |
| Acceso | Offline | Requiere conexión |

## Autores

- **Sebastian Caamaño**
- **Vicente Ordenes**

---

**Nota**: Esta aplicación utiliza Oracle Cloud Infrastructure para la base de datos, proporcionando una arquitectura cliente-servidor real en lugar de almacenamiento local.
