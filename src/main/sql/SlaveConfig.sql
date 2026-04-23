-- 配置连接到 Master
CHANGE REPLICATION SOURCE TO
    SOURCE_HOST='host.docker.internal',
    SOURCE_USER='repl',
    SOURCE_PASSWORD='Root@123456',
    SOURCE_LOG_FILE='mysql-bin.000001', -- 填入刚才记录的 File
    SOURCE_LOG_POS=900;                 -- 填入刚才记录的 Position

-- 启动复制线程

START REPLICA;
SHOW REPLICA STATUS;

SHOW DATABASES;
SHOW DATABASES;
SELECT * FROM replication_test.users;

SHOW REPLICA STATUS;
SELECT VERSION();

stop replica ;
change replication source to GET_SOURCE_PUBLIC_KEY = 1;
START REPLICA;
SHOW REPLICA STATUS ;
STOP REPLICA;
SET GLOBAL server_id = 2;
start replica ;
SET GLOBAL ENFORCE_GTID_CONSISTENCY = ON;
SET GLOBAL GTID_MODE = OFF_PERMISSIVE;
SET GLOBAL GTID_MODE = ON_PERMISSIVE;
SET GLOBAL GTID_MODE = ON;
CHANGE REPLICATION SOURCE TO SOURCE_AUTO_POSITION = 1;
START REPLICA;
show replica status ;
STOP REPLICA;
RESET REPLICA;
RESET MASTER;
SET GLOBAL gtid_purged = '55b852d4-2ff8-11f1-87ee-ad9c095590d5:1-204';


CHANGE REPLICATION SOURCE TO
    SOURCE_HOST='host.docker.internal',
    SOURCE_USER='repl',
    SOURCE_PORT=3306,
    GET_SOURCE_PUBLIC_KEY=1,
    SOURCE_AUTO_POSITION=1;
START REPLICA;
show replica status ;
# #1. 密码加密认证阻断 (Authentication Plugin Restrictions)
# 问题核心： MySQL 8.0 及以上版本默认使用了安全性更高的 caching_sha2_password 认证插件。该插件强制要求在网络中传输密码时必须使用 SSL/TLS 加密。而你的本地 Docker 环境尝试使用明文进行连接，因此直接被 Master 拒绝。
#
# 解决思路： 我们开启了 GET_SOURCE_PUBLIC_KEY=1，允许 Slave 在不配置复杂 SSL 证书的情况下，自动向 Master 请求 RSA 公钥（Public Key）来加密密码，从而通过了身份验证。
#
# 2. 节点标识符冲突 (Server ID Collision)
# 问题核心： 在分布式数据库的 Replication topology（复制拓扑结构） 中，为了防止二进制日志在节点间发生死循环同步，每个实例都必须有一个全局唯一的标识。你的 Mac 主机和 Docker 容器默认的 server_id 都是 1。
#
# 解决思路： 我们将 Docker 容器内 Slave 的 server_id 修改为了 2，解决了身份冲突。
#
# 3. 事务追踪机制不匹配 (GTID Mode Incompatibility)
# 问题核心： Master 端开启了 Global Transaction Identifiers（GTID，全局事务标识符），这是一种高级的复制跟踪机制；但 Slave 端默认是关闭的 (GTID_MODE = OFF)。两边的同步协议不一致，导致 Receiver 线程无法启动。
#
# 解决思路： 我们在 Slave 上强制开启了 GTID 一致性 (ENFORCE_GTID_CONSISTENCY = ON) 和 GTID 模式，并启用了 Auto-Positioning（自动定位机制），让主从节点能够智能协商同步位点。
#
# 4. 历史日志缺失触发保护机制 (Purged Binary Logs / Error 1236)
# 问题核心： Slave 作为“新兵”，向 Master 索要从第 1 个事务开始的所有历史记录。但 Master 出于空间管理的需要，已经将早期的 Binary Logs（二进制日志） 给 purged（清理） 掉了（缺失了 1-204 号事务）。为了防止出现严重的数据不一致（Data Consistency 异常），MySQL 触发了致命错误（Fatal error）并强制中断同步。
#
# 解决思路： 我们利用 SET GLOBAL gtid_purged 命令，手动将这 204 个缺失的事务注入到 Slave 的已执行记录中，成功“欺骗”了 Slave，让它直接从第 205 个事务开始同步。
#
# 5. 凭证存储的安全拦截 (Connection Metadata Security)
# 问题核心： MySQL 认为将明文的用户名和密码持久化存储在系统表（如 mysql.slave_master_info）中存在安全隐患。
#
# 解决思路： 我们放弃了在 CHANGE REPLICATION SOURCE 中写死密码，转而使用 START REPLICA USER='...', PASSWORD='...' 的方式，在内存中动态传递凭证（Credentials）。


SHOW DATABASES LIKE 'replication%';
-- 或者
