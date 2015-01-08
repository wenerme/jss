package com.mysql.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.junit.Test;

public class JDBCConnectionAcquire
{
    @Test
    public void test() throws SQLException, NoSuchFieldException, IllegalAccessException
    {
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost/test", "root", "root");
        ConnectionImpl impl = (ConnectionImpl) c;
        MysqlIO io = impl.getIO();
        // 与该数据库的Socket连接
        // io.mysqlConnection;
    }
}
