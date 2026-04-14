-- 用户表
use malllearning;
CREATE TABLE user (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      username VARCHAR(50) NOT NULL UNIQUE,
                      password VARCHAR(255) NOT NULL,
                      email VARCHAR(100),
                      balance DECIMAL(10,2) DEFAULT 0,
                      created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                      updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 商品表
CREATE TABLE product (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(100) NOT NULL,
                         description TEXT,
                         price DECIMAL(10,2) NOT NULL,
                         stock INT NOT NULL DEFAULT 0,
                         created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                         updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 购物车表
CREATE TABLE cart (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      user_id BIGINT NOT NULL,
                      created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                      updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                      FOREIGN KEY (user_id) REFERENCES user(id)
);

-- 购物车明细表
CREATE TABLE cart_item (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           cart_id BIGINT NOT NULL,
                           product_id BIGINT NOT NULL,
                           quantity INT NOT NULL DEFAULT 1,
                           created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                           updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           FOREIGN KEY (cart_id) REFERENCES cart(id),
                           FOREIGN KEY (product_id) REFERENCES product(id)
);

-- 优惠券表
CREATE TABLE coupon (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(50) NOT NULL,
                        type VARCHAR(20) NOT NULL,
                        amount DECIMAL(10,2) NOT NULL,
                        min_order DECIMAL(10,2),
                        quantity INT NOT NULL,
                        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                        updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 用户优惠券表
CREATE TABLE user_coupon (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             user_id BIGINT NOT NULL,
                             coupon_id BIGINT NOT NULL,
                             status VARCHAR(20) NOT NULL DEFAULT 'UNUSED',
                             created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                             updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                             FOREIGN KEY (user_id) REFERENCES user(id),
                             FOREIGN KEY (coupon_id) REFERENCES coupon(id)
);

-- 订单表
CREATE TABLE `order` (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         user_id BIGINT NOT NULL,
                         total_amount DECIMAL(10,2) NOT NULL,
                         discount DECIMAL(10,2) DEFAULT 0,
                         final_amount DECIMAL(10,2) NOT NULL,
                         status VARCHAR(20) DEFAULT 'PENDING',
                         coupon_id BIGINT,
                         created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                         updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         FOREIGN KEY (user_id) REFERENCES user(id),
                         FOREIGN KEY (coupon_id) REFERENCES coupon(id)
);

-- 订单明细表
CREATE TABLE order_item (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            order_id BIGINT NOT NULL,
                            product_id BIGINT NOT NULL,
                            quantity INT NOT NULL,
                            price DECIMAL(10,2) NOT NULL,
                            created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                            updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            FOREIGN KEY (order_id) REFERENCES `order`(id),
                            FOREIGN KEY (product_id) REFERENCES product(id)
);
-- 给 user 表添加 role 字段
ALTER TABLE `user` ADD COLUMN `role` VARCHAR(20) NOT NULL DEFAULT 'USER' COMMENT '角色：USER-普通用户，ADMIN-管理员';

-- 插入一个管理员账号（密码后续需要改成加密的）
INSERT INTO `user` (username, password, email, balance, role)
VALUES ('admin', '123456', 'admin@example.com', 0.00, 'ADMIN');
