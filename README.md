# API REST para Gestión de Productos y Pedidos

## Descripción

Este proyecto es una API REST desarrollada con Spring Boot que permite gestionar productos y pedidos.

## Tecnologías Utilizadas

- **Spring Boot 2.7**
- **Java 11**
- **JWT (Json Web Token)**
- **Swagger (Springdoc OpenAPI 3.0)**
- **Base de datos**: PostgreSQL
- **Docker**
- **GitHub**

## Características del Proyecto

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
- Implementación de Búsqueda y Filtrado
- Paginación y Ordenamiento
- Autenticación y Autorización con JWT
- Validaciones
- Relaciones entre Entidades
- Pruebas Unitarias y de Integración
- Manejo de Errores y Logging
- Documentación de API con Swagger
- Diseño y Patrones de Arquitectura
