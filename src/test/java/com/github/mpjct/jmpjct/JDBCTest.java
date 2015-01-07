package com.github.mpjct.jmpjct;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import org.junit.Test;

public class JDBCTest
{
    @Test
    public void test() throws Exception
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    JMP.main(null);
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
        Thread.sleep(1000);
        System.out.println("初始化完成");
//        Connection c = DriverManager.getConnection("jdbc:mysql://localhost:5050/test?useServerPrepStmts=true", "root", "");
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost:5050/test", "root", "");
        System.out.println("建立连接");
        Statement stmt = c.createStatement();
        System.out.println("创建语句");
        stmt.execute("drop table if EXISTS test");
        System.out.println("执行SQL");
        stmt.execute("create table if not exists test(id int, value  varchar(50))");
        System.out.println("执行SQL");
        stmt.execute("insert into test values(1,'123')");
        System.out.println("执行SQL");
        stmt.execute("select * from test");
        System.out.println("执行SQL");
        {
            ResultSet rs = stmt.getResultSet();
            rs.next();
            int id = rs.getInt(1);
            String value = rs.getString(2);
            System.out.printf("ID: %s VALUE: %s%n", id, value);
        }


        System.out.println("打开事务");
        c.setAutoCommit(false);
        System.out.println("执行SQL");
        stmt.execute("insert into test values(2,'223')");
        System.out.println("提交事务");
        c.commit();

        System.out.println("获取 Catalog");
        System.out.println(c.getCatalog());

        System.out.println("Prepare 语句");
        PreparedStatement statement = c.prepareStatement("select * from test where id=?");
        System.out.println("设置参数");
        statement.setInt(1, 2);
        System.out.println("执行 Prepare");
        statement.executeQuery();

        System.out.println("执行 Prepare");
        statement.setInt(1, 1);
        statement.executeQuery();

        System.out.println("关闭语句");
        stmt.close();
        System.out.println("关闭连接");
        try (Connection connection = c)
        {
            System.out.println("Nothing");
        }
        System.out.println(c.isClosed());
        c.close();
    }
}
