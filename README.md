# API REST para Gestión de Productos y Pedidos 🛒

## Reto Cineplanet 2024 🪐🎥

## Descripción 📃
Este proyecto es una API REST desarrollada con Spring Boot que permite gestionar productos y pedidos.

## Tecnologías Utilizadas

- **Spring Boot 2.7⚙️♨️**
- **Java 11☕** 
- **JWT (Json Web Token)🔐**
- **Swagger (Springdoc OpenAPI 3.0)📃**
- **PostgreSQL💾**
- **Docker🐳**
- **GitHub😺**

## Características del Proyecto📜

### CRUD de Productos
- **Productos**
  - Código Numérico
  - Nombre (Cadena de Texto)
  - Precio (Decimal)

### CRUD de Pedidos
- **Pedidos**
  - Nro de Pedido (Numérico)
  - Código de Producto (Numérico)
  - Cantidad (Numérico)
  - Cliente (Objeto)
    - Nombres (Cadena de Texto)
    - Apellidos (Cadena de Texto)
    - Dirección (Cadena de Texto)

### Otras características
- Implementación de Búsqueda y Filtrado 🔎
- Paginación y Ordenamiento 🗄️
- Autenticación y Autorización con JWT 🔒
- Validaciones ✅
- Relaciones entre Entidades ⬅️⬇️➡️
- Pruebas Unitarias y de Integración 🧪
- Manejo de Errores y Logging ❌🪵
- Documentación de API con Swagger 📜
- Diseño y Patrones de Arquitectura ⚡

## Deployment🌍
Se podra acceder a los recursos del api en el siguiente dominio
```https://retocp2024-nqug.onrender.com```

## Endpoints 📃

Las operaciones apuntan principalmente a estos dos endpoints
- **```/productos```**
- **```/pedidos```**

Se podra revisar la especificacion de dichos endpoints en la siguiente URL de documentacion [Swagger OpenApi 3.0](https://retocp2024-nqug.onrender.com/swagger-ui/index.html)

Adicionalmente se adjunta el [POSTMAN DEL PROYECTO](retoCP-2024.postman_collection.json)‍🚀📜






