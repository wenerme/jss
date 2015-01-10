package jss.util.jdbc;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 查询结果集辅助操作类
 */
public class ResultSets
{

    private ResultSets()
    {
    }


    public static QueryResult toQueryResult(ResultSet rs) throws SQLException
    {
        ResultSetMetaData meta = rs.getMetaData();
        int columns = meta.getColumnCount();
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>(50);
        QueryResult result = new QueryResult();
        for (int i = 1; i <= columns; i++)
        {
            result.getColumn().add(meta.getColumnName(i));
        }

        while (rs.next())
        {
            List<Object> row = Lists.newArrayListWithCapacity(columns);
            for (int i = 1; i <= columns; ++i)
            {
                row.add(rs.getObject(i));
            }
            result.getRows().add(row);
        }

        return result;
    }

    public static QueryResult toQueryResult(ResultSet rs, Predicate<List<Object>> filter) throws SQLException
    {
        ResultSetMetaData meta = rs.getMetaData();
        int columns = meta.getColumnCount();
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>(50);
        QueryResult result = new QueryResult();
        for (int i = 1; i <= columns; i++)
        {
            result.getColumn().add(meta.getColumnName(i));
        }

        while (rs.next())
        {
            List<Object> row = Lists.newArrayListWithCapacity(columns);
            for (int i = 1; i <= columns; ++i)
            {
                row.add(rs.getObject(i));
            }
            if (filter.apply(row))
                result.getRows().add(row);
        }

        return result;
    }

    public static List<Map<String, Object>> toListMap(ResultSet rs) throws SQLException
    {
        return toListMap(rs, rs.getMetaData());
    }

    public static List<Map<String, Object>> toListMap(ResultSet rs, ResultSetMetaData md) throws SQLException
    {
        int columns = md.getColumnCount();
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>(50);
        while (rs.next())
        {
            // 这里必须使用 LinkedHashMap
            Map<String, Object> row = new LinkedHashMap<>();
            for (int i = 1; i <= columns; ++i)
            {
                row.put(md.getColumnName(i), rs.getObject(i));
            }
            list.add(row);
        }

        return list;
    }


    @SuppressWarnings("unchecked")
    public static <T> T firstValue(List<Map<String, Object>> rs)
    {
        if (rs.size() == 0 || rs.get(0).size() == 0)
            return null;

        return (T) rs.get(0).values().iterator().next();
    }


    public static void print(ResultSet rs) throws SQLException
    {
        QueryResult result = toQueryResult(rs);
        TableDisplay.display(result.getColumn(), result.getRows());
    }
}
