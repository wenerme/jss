package com.github.mpjct.jmpjct;

import static jss.util.jdbc.ResultSets.print;

import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import org.junit.Ignore;
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

    @Test
    public void testDBMetaData() throws Exception
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
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost:5050/information_schema", "root", "");
        print(c.getMetaData().getCatalogs());
        System.out.println("getSuperTables");
        print(c.getMetaData().getSuperTables(null, "information_schema", "SCHEMATA"));
        System.out.println("getClientInfoProperties");
        print(c.getMetaData().getClientInfoProperties());
        System.out.println("getMetaData().getColumns");
        print(c.getMetaData().getColumns(null, "mysql", null, null));
        System.out.println("c.getMetaData().getPrimaryKeys");
        print(c.getMetaData().getPrimaryKeys(null, "information_schema", "tables"));
        c.close();
    }

    @Test
    public void testRSMetaData() throws Exception
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
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost:5050/information_schema", "root", "");

        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("select * from mysql.user");
        ResultSetMetaData md = rs.getMetaData();
        int count = md.getColumnCount();
        for (int i = 1; i <= count; i++)
        {
            System.out.printf("ReadOnly: %s\tAutoIncrement: %s\tCaseSensitive: %s\n" +
                    "Searchable: %s\tCurrency: %s\tNullable: %s\n" +
                    "Signed: %s\tColumnDisplaySize: %s\tColumnLabel: %s\n" +
                    "ColumnName: %s\tSchemaName: %s\tPrecision: %s\n" +
                    "Scale: %s\tTableName: %s\tCatalogName: %s\n" +
                    "ColumnType: %s\tColumnTypeName: %s\tWritable: %s\n" +
                    "DefinitelyWritable: %s\tColumnClassName: %s\n"
                    , md.isReadOnly(i), md.isAutoIncrement(i), md.isCaseSensitive(i), md.isSearchable(i), md
                    .isCurrency(i), md.isNullable(i), md.isSigned(i), md.getColumnDisplaySize(i), md
                    .getColumnLabel(i), md.getColumnName(i), md.getSchemaName(i), md.getPrecision(i), md
                    .getScale(i), md.getTableName(i), md.getCatalogName(i), md.getColumnType(i), md
                    .getColumnTypeName(i), md.isWritable(i), md.isDefinitelyWritable(i), md
                    .getColumnClassName(i));
        }
    }

    @Test
    @Ignore
    public void genFormat()
    {
        // 生成 testRSMetaData 测试中的代码
        StringBuilder format = new StringBuilder();
        StringBuilder param = new StringBuilder();
        for (Method method : ResultSetMetaData.class.getDeclaredMethods())
        {
            Class<?>[] pt = method.getParameterTypes();
            if (pt.length != 1 || pt[0] != int.class)
            {
                continue;
            }

            String methodName = method.getName();
            String name = null;
            if (methodName.startsWith("get"))
            {
                name = methodName.substring(3);
            }
            if (methodName.startsWith("is"))
            {
                name = methodName.substring(2);
            }

            if (name == null)
            {
                continue;
            }

            format.append(name).append(": %s\\t");
            param.append("md.").append(methodName).append("(i),");
        }

        System.out.println(format);
        System.out.println(param);
    }
}
