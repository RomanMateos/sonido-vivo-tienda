-- ============================================================================
-- PROYECTO: SONIDO VIVO
-- MOTOR: MySQL
-- ============================================================================

-- ?. ELIMINADO DE TABLAS
DROP TABLE IF EXISTS usuarios;
DROP TABLE IF EXISTS regiones;
DROP TABLE IF EXISTS comunas;
DROP TABLE IF EXISTS registro_usuarios;
DROP TABLE IF EXISTS categorias;
DROP TABLE IF EXISTS marcas;
DROP TABLE IF EXISTS productos;
DROP TABLE IF EXISTS ventas;
DROP TABLE IF EXISTS detalle_ventas;
DROP TABLE IF EXISTS blog;
DROP TABLE IF EXISTS contacto;

-- ----------------------------------------------------------------------------
-- 0. BASES DE DATOS Y USUARIOS INDEPENDIENTES
-- ----------------------------------------------------------------------------
CREATE DATABASE IF NOT EXISTS sonido_autenticacion
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'autenticacion_app'@'localhost' IDENTIFIED BY 'ClaveAuth2026!';
GRANT ALL PRIVILEGES ON sonido_autenticacion.* TO 'autenticacion_app'@'localhost';

CREATE DATABASE IF NOT EXISTS sonido_usuarios
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'usuarios_app'@'localhost' IDENTIFIED BY 'ClaveUsuarios2026!';
GRANT ALL PRIVILEGES ON sonido_usuarios.* TO 'usuarios_app'@'localhost';

CREATE DATABASE IF NOT EXISTS sonido_productos
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'productos_app'@'localhost' IDENTIFIED BY 'ClaveProductos2026!';
GRANT ALL PRIVILEGES ON sonido_productos.* TO 'productos_app'@'localhost';

CREATE DATABASE IF NOT EXISTS sonido_ventas
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'ventas_app'@'localhost' IDENTIFIED BY 'ClaveVentas2026!';
GRANT ALL PRIVILEGES ON sonido_ventas.* TO 'ventas_app'@'localhost';

CREATE DATABASE IF NOT EXISTS sonido_blog
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'blog_app'@'localhost' IDENTIFIED BY 'ClaveBlog2026!';
GRANT ALL PRIVILEGES ON sonido_blog.* TO 'blog_app'@'localhost';

CREATE DATABASE IF NOT EXISTS sonido_contacto
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'contacto_app'@'localhost' IDENTIFIED BY 'ClaveContacto2026!';
GRANT ALL PRIVILEGES ON sonido_contacto.* TO 'contacto_app'@'localhost';

FLUSH PRIVILEGES;

-- ----------------------------------------------------------------------------
-- 1. BASE DE DATOS: sonido_autenticacion (Puerto 8081)
-- ----------------------------------------------------------------------------
USE sonido_autenticacion;

CREATE TABLE IF NOT EXISTS usuarios (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        email VARCHAR(120) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    rol VARCHAR(30) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE
    ) ENGINE=InnoDB;

-- ----------------------------------------------------------------------------
-- 2. BASE DE DATOS: sonido_usuarios (Puerto 8082)
-- ----------------------------------------------------------------------------
USE sonido_usuarios;

CREATE TABLE IF NOT EXISTS regiones (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        nombre VARCHAR(100) NOT NULL UNIQUE
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS comunas (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       nombre VARCHAR(100) NOT NULL,
    region_id BIGINT NOT NULL,
    CONSTRAINT fk_comunas_region FOREIGN KEY (region_id) REFERENCES regiones(id),
    CONSTRAINT uk_comuna_region UNIQUE (nombre, region_id)
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS registro_usuarios (
                                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                 run VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(120) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    telefono VARCHAR(30) NULL,
    direccion VARCHAR(200) NOT NULL,
    comuna_id BIGINT NOT NULL,
    rol VARCHAR(30) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_usuario_comuna FOREIGN KEY (comuna_id) REFERENCES comunas(id)
    ) ENGINE=InnoDB;

-- ----------------------------------------------------------------------------
-- 3. BASE DE DATOS: sonido_productos (Puerto 8083)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS categorias (
                                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                          nombre VARCHAR(100) NOT NULL UNIQUE
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS marcas (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      nombre VARCHAR(100) NOT NULL UNIQUE
    ) ENGINE=InnoDB;

CREATE TABLE productos (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           nombre VARCHAR(150) NOT NULL,
                           categoria_id BIGINT NOT NULL,
                           marca_id BIGINT NOT NULL,
                           precio INT NOT NULL,
                           descripcion VARCHAR(500) NOT NULL,
                           imagen VARCHAR(500) NOT NULL,
                           en_oferta BOOLEAN NOT NULL DEFAULT FALSE,
                           stock INT NOT NULL DEFAULT 0,
                           CONSTRAINT fk_productos_categoria FOREIGN KEY (categoria_id) REFERENCES categorias(id),
                           CONSTRAINT fk_productos_marca FOREIGN KEY (marca_id) REFERENCES marcas(id)
) ENGINE=InnoDB AUTO_INCREMENT=1000;
-- ----------------------------------------------------------------------------
-- 4. BASE DE DATOS: sonido_ventas (Puerto 8084)
-- ----------------------------------------------------------------------------
USE sonido_ventas;

CREATE TABLE IF NOT EXISTS ventas (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      comprador_nombre VARCHAR(100) NOT NULL,
    comprador_apellidos VARCHAR(100) NOT NULL,
    comprador_email VARCHAR(120) NOT NULL,
    comprador_region VARCHAR(100) NOT NULL,
    comprador_comuna VARCHAR(100) NOT NULL,
    comprador_direccion VARCHAR(200) NOT NULL,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tipo_despacho VARCHAR(30) NOT NULL,
    lugar_despacho VARCHAR(250) NOT NULL,
    total INT NOT NULL,
    estado VARCHAR(20) NOT NULL
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS detalle_ventas (
                                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                              venta_id BIGINT NOT NULL,
                                              producto_id VARCHAR(20) NOT NULL,
    nombre_producto VARCHAR(150) NOT NULL,
    precio_unitario INT NOT NULL,
    cantidad INT NOT NULL,
    subtotal INT NOT NULL,
    CONSTRAINT fk_detalle_ventas_venta FOREIGN KEY (venta_id) REFERENCES ventas(id) ON DELETE CASCADE
    ) ENGINE=InnoDB;

-- ----------------------------------------------------------------------------
-- 5. BASE DE DATOS: sonido_blog (Puerto 8085)
-- ----------------------------------------------------------------------------
USE sonido_blog;

CREATE TABLE IF NOT EXISTS blog (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    titulo VARCHAR(200) NOT NULL,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    resumen VARCHAR(500) NOT NULL,
    parrafo_1 TEXT NOT NULL,
    subtitulo_1 VARCHAR(200) NOT NULL,
    parrafo_2 TEXT NOT NULL,
    subtitulo_2 VARCHAR(200) NOT NULL,
    parrafo_3 TEXT NOT NULL,
    imagen VARCHAR(500) NOT NULL,
    cita VARCHAR(500) NOT NULL
    ) ENGINE=InnoDB;

-- ----------------------------------------------------------------------------
-- 6. BASE DE DATOS: sonido_contacto (Puerto 8086)
-- ----------------------------------------------------------------------------
USE sonido_contacto;

CREATE TABLE IF NOT EXISTS contacto (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    email VARCHAR(120) NOT NULL,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    mensaje TEXT NOT NULL
    ) ENGINE=InnoDB;


