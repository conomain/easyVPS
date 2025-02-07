INSERT INTO users (id_user, email, password, username)
VALUES(nextval('users_seq'), 'test001@gmail.com', 'pass1', 'testuser001');

INSERT INTO users (id_user, email, password, username)
VALUES(nextval('users_seq'), 'test002@gmail.com', 'pass2', 'testuser002');

INSERT INTO users (id_user, email, password, username)
VALUES(nextval('users_seq'), 'test003@gmail.com', 'pass3', 'testuser003');

INSERT INTO users (id_user, email, password, username)
VALUES(nextval('users_seq'), 'test004@gmail.com', 'pass4', 'testuser004');

INSERT INTO users (id_user, email, password, username)
VALUES(nextval('users_seq'), 'test005@gmail.com', 'pass5', 'testuser005');

INSERT INTO users (id_user, email, password, username)
VALUES(nextval('users_seq'), 'test006@gmail.com', 'pass6', 'testuser006');

INSERT INTO users (id_user, email, password, username)
VALUES(nextval('users_seq'), 'test007@gmail.com', 'pass7', 'testuser007');

INSERT INTO users (id_user, email, password, username)
VALUES(nextval('users_seq'), 'test008@gmail.com', 'pass8', 'testuser008');



INSERT INTO configurations (id_configuration, configuration_cpu_cores, configuration_price, configuration_ram, configuration_storage, configuration_name)
VALUES(nextval('configurations_seq'), 1, 5, 1, 40, 'small');

INSERT INTO configurations (id_configuration, configuration_cpu_cores, configuration_price, configuration_ram, configuration_storage, configuration_name)
VALUES(nextval('configurations_seq'), 2, 10, 2, 80, 'medium');

INSERT INTO configurations (id_configuration, configuration_cpu_cores, configuration_price, configuration_ram, configuration_storage, configuration_name)
VALUES(nextval('configurations_seq'), 4, 20, 8, 150, 'large');

INSERT INTO configurations (id_configuration, configuration_cpu_cores, configuration_price, configuration_ram, configuration_storage, configuration_name)
VALUES(nextval('configurations_seq'), 8, 40, 16, 300, 'xlarge');



INSERT INTO server (id_server, server_cpu_cores, server_ram, server_storage)
VALUES(nextval('server_seq'), 16, 64, 512);

INSERT INTO server (id_server, server_cpu_cores, server_ram, server_storage)
VALUES(nextval('server_seq'), 64, 256, 2048);

INSERT INTO server (id_server, server_cpu_cores, server_ram, server_storage)
VALUES(nextval('server_seq'), 128, 512, 4096);

