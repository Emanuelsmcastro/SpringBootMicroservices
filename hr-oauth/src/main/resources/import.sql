INSERT INTO tb_user (username, password) VALUES ('Nina',  '$2a$10$A9L2v6rd1Ic3BtbOJ6AGAeg.rkBKo7Kq4qGdA5Lx8V4PgQZnEsreC');
INSERT INTO tb_user (username, password) VALUES ('Carl',  '$2a$10$A9L2v6rd1Ic3BtbOJ6AGAeg.rkBKo7Kq4qGdA5Lx8V4PgQZnEsreC');

INSERT INTO tb_role (name) VALUES ('ROLE_OPERATOR');
INSERT INTO tb_role (name) VALUES ('ROLE_ADMIN');

INSERT INTO tb_user_role (user_id, role_id) VALUES (1, 1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (2, 1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (2, 2);