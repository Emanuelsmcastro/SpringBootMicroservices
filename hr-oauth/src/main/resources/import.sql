INSERT INTO tb_user (username, password) VALUES ('Nina',  '$2a$12$3Dgw2Das6q0wl4U5unXvTOd7k1isAf9nEEKdYJiVUgrBEMgI/vjQm');
INSERT INTO tb_user (username, password) VALUES ('Carl',  '$2a$12$3Dgw2Das6q0wl4U5unXvTOd7k1isAf9nEEKdYJiVUgrBEMgI/vjQm');

INSERT INTO tb_role (name) VALUES ('ROLE_OPERATOR');
INSERT INTO tb_role (name) VALUES ('ROLE_ADMIN');

INSERT INTO tb_user_role (user_id, role_id) VALUES (1, 1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (2, 1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (2, 2);