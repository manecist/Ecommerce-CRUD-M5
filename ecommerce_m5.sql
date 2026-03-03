-- Crear el Schema (Base de Datos)
CREATE DATABASE ecommerce_m5; 

-- Usuarios 
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_completo VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE, 
    telefono VARCHAR(20),
    email VARCHAR(100) NOT NULL UNIQUE,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol ENUM('admin', 'usuario') DEFAULT 'usuario'
);

-- Categorías 
CREATE TABLE categorias (
    idCategoria INT AUTO_INCREMENT PRIMARY KEY,
    nombreCategoria VARCHAR(100) NOT NULL,
    imagenBanner VARCHAR(255) DEFAULT 'default-banner.jpg'
);

-- Subcategorías 
CREATE TABLE subcategorias (
    idSubcategoria INT AUTO_INCREMENT PRIMARY KEY,
    idCategoriaAsociada INT NOT NULL,
    nombreSubcategoria VARCHAR(100) NOT NULL,
    CONSTRAINT fk_sub_cat FOREIGN KEY (idCategoriaAsociada) 
        REFERENCES categorias(idCategoria) ON DELETE CASCADE
);

-- Productos 
CREATE TABLE productos (
    idProducto INT AUTO_INCREMENT PRIMARY KEY,
    idSubcategoriaAsociada INT NOT NULL,
    nombreProducto VARCHAR(150) NOT NULL,
    descripcionProducto TEXT,
    precioProducto DOUBLE NOT NULL DEFAULT 0.0,
    stockProducto INT NOT NULL DEFAULT 0,
    imagenProducto VARCHAR(255) DEFAULT 'default-prod.jpg',
    CONSTRAINT fk_prod_sub FOREIGN KEY (idSubcategoriaAsociada) 
        REFERENCES subcategorias(idSubcategoria) ON DELETE RESTRICT
);

-- Clientes solo creada no instanciada en intellij no se utilizan aun
CREATE TABLE customers (
    idCustomer INT AUTO_INCREMENT PRIMARY KEY,
    idUsuario INT NOT NULL,
    direccion_envio TEXT,
    comuna VARCHAR(50),
    ciudad VARCHAR(50),
    CONSTRAINT fk_cust_user FOREIGN KEY (idUsuario) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- Pedidos (inicio) creada no instanciada en intellij no se utilizan aun
CREATE TABLE orders (
    idOrder INT AUTO_INCREMENT PRIMARY KEY,
    idCustomer INT NOT NULL,
    fecha_pedido TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_pedido DOUBLE NOT NULL,
    estado_pedido ENUM('pendiente', 'pagado', 'enviado', 'cancelado') DEFAULT 'pendiente',
    CONSTRAINT fk_order_cust FOREIGN KEY (idCustomer) REFERENCES customers(idCustomer)
);

--  Detalles del Pedido (Productos comprados) creada no instanciada en intellij no se utilizan aun
CREATE TABLE order_items (
    idOrderItem INT AUTO_INCREMENT PRIMARY KEY,
    idOrder INT NOT NULL,
    idProducto INT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DOUBLE NOT NULL,
    subtotal DOUBLE AS (cantidad * precio_unitario), 
    CONSTRAINT fk_item_order FOREIGN KEY (idOrder) REFERENCES orders(idOrder) ON DELETE CASCADE,
    CONSTRAINT fk_item_prod FOREIGN KEY (idProducto) REFERENCES productos(idProducto)
);

-- 6. Insertar Admin de prueba (Password: Admin.2026!)
INSERT INTO usuarios (nombre_completo, email, username, password, rol) 
VALUES ('Administrador Mágico', 'admin@magic.cl', 'admin', 'Admin.2026!', 'admin');
SELECT * FROM usuarios;
SELECT * FROM categorias;
SELECT * FROM subcategorias;

SELECT 
    c.nombreCategoria AS Categoria,
    s.nombreSubcategoria AS Subcategoria,
    p.nombreProducto AS Producto,
    p.stockProducto AS Stock
FROM productos p
INNER JOIN subcategorias s ON p.idSubcategoriaAsociada = s.idSubcategoria
INNER JOIN categorias c ON s.idCategoriaAsociada = c.idCategoria;

SELECT 
    c.nombreCategoria AS Categoria,
    s.nombreSubcategoria AS Subcategoria,
    p.nombreProducto AS Producto,
    p.precioProducto AS Precio,
    p.stockProducto AS Stock
FROM productos p
INNER JOIN subcategorias s ON p.idSubcategoriaAsociada = s.idSubcategoria
INNER JOIN categorias c ON s.idCategoriaAsociada = c.idCategoria
ORDER BY c.nombreCategoria, p.nombreProducto;

SELECT 
    nombreProducto AS Producto,
    precioProducto AS Precio,
    stockProducto AS Stock
FROM productos
ORDER BY precioProducto ASC;

