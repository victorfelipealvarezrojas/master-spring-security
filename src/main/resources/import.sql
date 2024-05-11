INSERT INTO "user" (username, name, password, role) VALUES ('lmarquez', 'luis márquez', '$2a$10$SkCYmtcOCn5wzb6Ef.Hs0uS2DNTXjw7e1vtebPKvgcoh9bYx3LXuC', 'ROLE_CUSTOMER');
INSERT INTO "user" (username, name, password, role) VALUES ('fperez', 'fulano pérez', '$2a$10$lRYCmeRpC/oV8oO1RqlmqOVGi0q5pRODbr8O9B2xE16./QkhrFkFy', 'ROLE_ASSISTANT_ADMINISTRATOR');
INSERT INTO "user" (username, name, password, role) VALUES ('mhernandez', 'mengano hernández', '$2a$10$SkCYmtcOCn5wzb6Ef.Hs0uS2DNTXjw7e1vtebPKvgcoh9bYx3LXuC', 'ROLE_ADMINISTRATOR');



INSERT INTO category (name, status) VALUES ('Electrónica', 'ENABLED');
INSERT INTO category (name, status) VALUES ('Ropa', 'ENABLED');
INSERT INTO category (name, status) VALUES ('Deportes', 'ENABLED');
INSERT INTO category (name, status) VALUES ('Hogar', 'ENABLED');

INSERT INTO product (name, price, status, category_id) VALUES ('Smartphone', 500.00, 'ENABLED', 1);
INSERT INTO product (name, price, status, category_id) VALUES ('Auriculares Bluetooth', 50.00, 'DISABLED', 1);
INSERT INTO product (name, price, status, category_id) VALUES ('Tablet', 300.00, 'ENABLED', 1);

INSERT INTO product (name, price, status, category_id) VALUES ('Camiseta', 25.00, 'ENABLED', 2);
INSERT INTO product (name, price, status, category_id) VALUES ('Pantalones', 35.00, 'ENABLED', 2);
INSERT INTO product (name, price, status, category_id) VALUES ('Zapatos', 45.00, 'ENABLED', 2);

INSERT INTO product (name, price, status, category_id) VALUES ('Balón de Fútbol', 20.00, 'ENABLED', 3);
INSERT INTO product (name, price, status, category_id) VALUES ('Raqueta de Tenis', 80.00, 'DISABLED', 3);

INSERT INTO product (name, price, status, category_id) VALUES ('Aspiradora', 120.00, 'ENABLED', 4);
INSERT INTO product (name, price, status, category_id) VALUES ('Licuadora', 50.00, 'ENABLED', 4);
