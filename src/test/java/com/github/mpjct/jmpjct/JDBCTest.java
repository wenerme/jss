package com.github.mpjct.jmpjct;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
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
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost:5050/test", "root", "root");
        System.out.println("建立连接");
        Statement stmt = c.createStatement();
        System.out.println("创建语句");
        stmt.execute("drop table test");
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


        c.setAutoCommit(false);
        System.out.println("打开事务");
        stmt.execute("insert into test values(2,'223')");
        System.out.println("执行SQL");
        c.commit();
        System.out.println("提交事务");

        stmt.close();
        System.out.println("关闭语句");
        c.close();
        System.out.println("关闭连接");
    }
}
