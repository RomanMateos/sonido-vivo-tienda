# Microservicio Tienda / Catalogo (sonido-vivo-tienda)

Backend desarrollado en Spring Boot para la gestion del catalogo de productos, categorias y marcas de la tienda Sonido Vivo (Evaluacion Parcial 3 - DSY1104 Desarrollo FullStack II).

## Tecnologias y Arquitectura
- Java 17+ / Spring Boot 4
- Spring Data JPA + MySQL 8 (base de datos sonido_productos normalizada en 3FN)
- Spring Security + JWT (HMAC-SHA256) para autenticacion stateless y control de acceso por roles (ADMINISTRADOR, VENDEDOR, CLIENTE)
- Springdoc OpenAPI / Swagger UI con autenticacion Bearer JWT integrada
- JUnit 5 + Mockito para pruebas unitarias en la capa de servicios

## Requisitos y Variables de Entorno
- DB_USER: Usuario de conexion a MySQL (ej. sonidovivo_app)
- DB_PASSWORD: Contrasena de MySQL
- JWT_SECRET: Clave secreta en Base64 compartida con autenticacion

## Ejecucion Local y Pruebas
- Ejecutar pruebas unitarias (10 tests): ./mvnw test
- Iniciar servidor (puerto 8083): ./mvnw spring-boot:run
- Documentacion Swagger UI: http://localhost:8083/swagger-ui.html

## Endpoints Principales (/api/v1)
- /productos: GET (publico), POST, PUT, DELETE (rol ADMINISTRADOR)
- /categorias: GET (publico), POST, PUT, DELETE (rol ADMINISTRADOR)
- /marcas: GET (publico), POST, PUT, DELETE (rol ADMINISTRADOR)
