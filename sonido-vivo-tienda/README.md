# Sonido Vivo - Backend

Spring Boot 4 + MySQL. API REST en `/api/v1`.

## Cómo levantarlo
1. Tener MySQL en marcha y crear la base y un usuario (como administrador):
   ```sql
   CREATE DATABASE sonidovivo CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   CREATE USER 'sonidovivo_app'@'localhost' IDENTIFIED BY 'ELIGE_UNA_CLAVE';
   GRANT ALL PRIVILEGES ON sonidovivo.* TO 'sonidovivo_app'@'localhost';
   ```
2. En PowerShell, dentro del proyecto (la clave NO va en el código):
   ```powershell
   $env:DB_USER="sonidovivo_app"
   $env:DB_PASSWORD="ELIGE_UNA_CLAVE"
   .\mvnw.cmd spring-boot:run
   ```
3. Swagger: http://localhost:8080/swagger-ui/index.html

## Convenciones
- Paquetes: `cl.duoc.sonidovivo.tienda.{entities, repositories, services, controllers, exceptions}`
- Rutas versionadas: `/api/v1/...`
- Ramas: `feature/<tema>` → Pull Request a `develop`. Nadie sube directo a `main`.
