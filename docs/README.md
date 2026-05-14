# 📚 Tutorial: Sistema de Gestión de Adopción de Mascotas con Spring Boot y JPA

## Índice de contenidos

| Archivo | Tema |
|---------|------|
| [Pruebas-logica-de-negocio.md](./Pruebas-logica-de-negocio.md) | Pruebas paso a paso de la lógica de negocio |

---

## 🐶 Sobre el proyecto

Este proyecto es un ejemplo básico de una API REST para gestionar solicitudes de adopción de mascotas. Incluye las siguientes funcionalidades clave:

- **Mascotas**: Gestión de mascotas con diferentes estados (Disponible, En Proceso, Adoptado) y tamaños.
- **Adoptantes**: Gestión de adoptantes con validaciones de edad y requisitos de vivienda (como tener patio para mascotas grandes).
- **Solicitudes**: Proceso de creación de solicitudes de adopción con lógica de negocio integrada en la capa de servicio.

## 🛠️ Tecnologías

- **Spring Boot 3.x**
- **Spring Data JPA**
- **PostgreSQL** (para persistencia)
- **H2** (para pruebas)
- **OpenAPI 3 / Swagger UI** (para documentación interactiva)

## ⚖️ Manejo de Errores

El proyecto cuenta con un manejador global de excepciones ([GlobalExceptionHandler.java](../src/main/java/com/example/demo_basic/exception/GlobalExceptionHandler.java)) que intercepta errores comunes como `IllegalArgumentException` y los traduce en respuestas HTTP claras (ej. 400 Bad Request) para el cliente, evitando los errores 500 genéricos.

---

> 💡 **Recomendación:** Comienza revisando la [Guía de Pruebas de Lógica de Negocio](./Pruebas-logica-de-negocio.md) para entender cómo interactuar con los endpoints de la API.
