CREATE DATABASE ComputerStoreManagement;
USE ComputerStoreManagement;
GO

-- Bảng categories
CREATE TABLE categories (
    category_id INT PRIMARY KEY IDENTITY(1,1), 
    name NVARCHAR(100) NOT NULL
);
GO

-- Bảng suppliers
CREATE TABLE suppliers (
    supplier_id INT PRIMARY KEY IDENTITY(1,1),
    name NVARCHAR(100) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(15),
    address NVARCHAR(MAX)
);
GO

-- Bảng products
CREATE TABLE products (
    product_id INT PRIMARY KEY IDENTITY(1,1),
    name NVARCHAR(100) NOT NULL,
    description NVARCHAR(MAX),
    price FLOAT(53) NOT NULL CHECK (price >= 0),
    category_id INT,
    supplier_id INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(category_id),
    FOREIGN KEY (supplier_id) REFERENCES suppliers(supplier_id)
);
GO

-- Bảng customers
CREATE TABLE customers (
    customer_id INT PRIMARY KEY IDENTITY(1,1),
    name NVARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(15),
    address NVARCHAR(MAX),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
GO

CREATE TABLE user_credentials (
    username VARCHAR(15) PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);
GO

-- Bảng employees
CREATE TABLE employees (
    employee_id INT PRIMARY KEY IDENTITY(1,1),
    name NVARCHAR(100) NOT NULL,
    username VARCHAR(15) NOT NULL UNIQUE,
    is_admin BIT NOT NULL DEFAULT 0, -- 1: admin, 0: nhân viên thường
    position VARCHAR(50),
    salary FLOAT(53),
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(15),
    hired_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (username) REFERENCES user_credentials(username)
);
GO

-- Bảng orders
CREATE TABLE orders (
    order_id INT PRIMARY KEY IDENTITY(1,1),
    customer_id INT,
    employee_id INT,
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) NOT NULL DEFAULT 'Pending',
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id)
);
GO

-- Bảng order_details
CREATE TABLE order_details (
    order_detail_id INT PRIMARY KEY IDENTITY(1,1),
    order_id INT,
    product_id INT,
    quantity INT NOT NULL CHECK (quantity > 0),
    price FLOAT(53) NOT NULL CHECK (price >= 0),
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id),
    CONSTRAINT unique_order_product UNIQUE (order_id, product_id)
);
GO

-- Bảng inventory
CREATE TABLE inventory (
    inventory_id INT PRIMARY KEY IDENTITY(1,1),
    product_id INT,
    quantity INT NOT NULL DEFAULT 0 CHECK (quantity >= 0),
    inbound INT DEFAULT 0 CHECK (inbound >= 0),
    outbound INT DEFAULT 0 CHECK (outbound >= 0),
    last_updated DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);
GO

-- Bảng payments
CREATE TABLE payments (
    payment_id INT PRIMARY KEY IDENTITY(1,1),
    order_id INT,
    payment_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    amount DECIMAL(10,2) NOT NULL CHECK (amount >= 0),
    method NVARCHAR(50) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id)
);
GO

-- Lấy tổng tiền của đơn hàng (tính từ bảng order_details)
CREATE PROCEDURE GetTotalAmountByOrderId (@orderId INT)
AS
BEGIN
    SELECT SUM(quantity * price) AS total_amount
    FROM order_details
    WHERE order_id = @orderId
    GROUP BY order_id;
END;
GO

INSERT INTO categories (name) VALUES 
(N'Laptop'),
(N'Máy tính để bàn'),
(N'Màn hình'),
(N'Bàn phím'),
(N'Chuột');
GO 

INSERT INTO suppliers (name, email, phone, address) VALUES
(N'FPT Trading', 'fpt@fpt.vn', '0281234567', N'456 Nguyễn Văn Cừ, Quận 5, TP.HCM'),
(N'An Phát Computer', 'anphat@gmail.com', '0249876543', N'789 Bạch Đằng, Quận 2, TP.HCM'),
(N'Phong Vũ', 'phongvu@store.com', '0287654321', N'321 Hai Bà Trưng, Quận 1, TP.HCM');
GO

INSERT INTO products (name, description, price, category_id, supplier_id, created_at) VALUES
(N'Dell Inspiron 15', N'Laptop với RAM 8GB, SSD 256GB', 15000000, 1, 1, '2024-01-15 10:30:00'),
(N'HP Pavilion 14', N'Laptop với RAM 16GB, SSD 512GB', 20000000, 1, 1, '2024-02-20 14:00:00'),
(N'Acer Aspire TC', N'Máy tính để bàn với bộ xử lý i5, RAM 8GB', 12000000, 2, 2, '2024-03-05 09:45:00'),
(N'Samsung 24-inch Monitor', N'Màn hình Full HD', 3000000, 3, 2, '2024-04-12 16:15:00'),
(N'Logitech K120', N'Bàn phím có dây tiêu chuẩn', 250000, 4, 3, '2024-05-07 13:50:00'),
(N'Logitech MX Master 3', N'Chuột không dây với thiết kế công thái học', 2000000, 5, 1, '2024-06-18 11:25:00');
GO

INSERT INTO customers (name, email, phone, address, created_at) VALUES
(N'Nguyễn Văn A', 'nva@gmail.com', '0901234567', N'123 Lê Lợi, Quận 1, TP.HCM', '2024-01-10 08:00:00'),
(N'Trần Thị B', 'ttb@yahoo.com', '0902345678', N'456 Trần Hưng Đạo, Quận 5, TP.HCM', '2024-02-15 09:15:00'),
(N'Lê Hoàng C', 'lhc@hotmail.com', '0903456789', N'789 Nguyễn Trãi, Quận 3, TP.HCM', '2024-03-20 14:45:00');
GO

INSERT INTO user_credentials (username, password, role) VALUES
('nte', 'password456', 'Staff'),
('pvd', 'password123', 'Admin'),
('tvf', 'password789', 'Staff');
GO

INSERT INTO employees (name, username, is_admin, position, salary, email, phone, hired_date) VALUES
(N'Phạm Văn D', 'pvd', 1, 'Admin', 20000000, 'pvd@store.com', '0904567890', '2023-12-01 09:00:00'),
(N'Nguyễn Thị E', 'nte', 0, 'Staff', 10000000, 'nte@store.com', '0905678901', '2024-01-15 10:30:00'),
(N'Trần Văn F', 'tvf', 0, 'Staff', 12000000, 'tvf@store.com', '0906789012', '2024-02-01 08:45:00');
GO

INSERT INTO orders (customer_id, employee_id, order_date, status) VALUES
(1, 1, '2024-06-01 10:00:00', 'Completed'),
(2, 2, '2024-06-05 14:30:00', 'Pending'),
(3, 3, '2024-06-10 16:15:00', 'Cancelled'),
(1, 2, '2024-06-15 12:45:00', 'Completed');
GO

INSERT INTO order_details (order_id, product_id, quantity, price) VALUES
(1, 1, 2, 15000000),
(1, 5, 1, 2000000),
(2, 3, 1, 12000000),
(3, 4, 2, 3000000);
GO

INSERT INTO inventory (product_id, quantity, inbound, outbound, last_updated) VALUES
(1, 50, 100, 50, '2024-06-01 09:30:00'),
(2, 30, 50, 20, '2024-06-03 14:00:00'),
(3, 20, 30, 10, '2024-06-05 11:45:00'),
(4, 100, 150, 50, '2024-06-10 08:15:00'),
(5, 70, 100, 30, '2024-06-15 13:20:00');
GO

INSERT INTO payments (order_id, payment_date, amount, method) VALUES
(1, '2024-06-01 10:30:00', 32000000, N'Tiền mặt'),
(2, '2024-06-05 14:45:00', 12000000, N'Thẻ'),
(3, '2024-06-10 16:00:00', 6000000, N'Chuyển khoản'),
(4, '2024-06-15 12:00:00', 20000000, N'Tiền mặt');
GO