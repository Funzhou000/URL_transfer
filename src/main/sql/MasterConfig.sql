CREATE USER 'repl'@'%' IDENTIFIED BY 'Root@123456';
-- 授予复制权限
GRANT REPLICATION SLAVE ON *.* TO 'repl'@'%';
FLUSH PRIVILEGES;
SHOW MASTER STATUS;
SHOW BINARY LOG STATUS;
SHOW BINARY LOGS;
CREATE DATABASE replication_test3;
USE replication_test3;
CREATE TABLE users (id INT PRIMARY KEY, name VARCHAR(20));
INSERT INTO users VALUES (1, 'Li Fangzhou');

SELECT VERSION();

-- 修改 repl 用户的加密插件为 mysql_native_password
ALTER USER 'repl'@'%' IDENTIFIED WITH mysql_native_password BY 'Root@123456';

-- 刷新权限确保生效
FLUSH PRIVILEGES;
STOP REPLICA;
CHANGE REPLICATION SOURCE TO
    SOURCE_HOST='host.docker.internal',
    SOURCE_USER='repl',
    SOURCE_PASSWORD='Root@123456',
    -- 核心参数：允许 Slave 自动从 Master 获取 RSA 公钥进行加密
    GET_SOURCE_PUBLIC_KEY=1;
START REPLICA;
CREATE DATABASE replication_test;
