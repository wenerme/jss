package jss.util.jdbc;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;

/**
 * 用于表示一个 {@link java.sql.ResultSet ResultSet} 持有的数据
 */
public class QueryResult
{
    private List<String> column;
    private List<List<Object>> rows;


    public QueryResult()
    {
        this(Lists.<String>newArrayList(), Lists.<List<Object>>newArrayList());
    }

    public QueryResult(List<Map<String, Object>> result)
    {
        rows = Lists.newArrayList();
        if (result.size() == 0)
        {
            column = Lists.newArrayList();
        } else
        {
            column = Lists.newArrayList(result.iterator().next().keySet());
            for (Map<String, Object> map : result)
            {
                rows.add(Lists.newArrayList(map.values()));
            }
        }
    }

    public QueryResult(List<String> column, List<List<Object>> rows)
    {
        this.column = column;
        this.rows = rows;
    }

    @SuppressWarnings("unchecked")
    public <T> T getFirstValue()
    {
        return (T) rows.get(0).get(0);
    }

    /**
     * @return 查询结果总的行数, 如果没有查询结果, 则返回 0
     */
    public int getRowCount()
    {
        return rows == null ? 0 : rows.size();
    }

    public List<Map<String, Object>> asListMap()
    {
        List<Map<String, Object>> result = Lists.newArrayList();

        for (List<Object> row : rows)
        {
            Map<String, Object> rowMap = Maps.newLinkedHashMap();
            for (int i = 0; i < column.size(); i++)
            {
                rowMap.put(column.get(i), row.get(i));
            }
            result.add(rowMap);
        }

        return result;
    }

    public List<List<Object>> getRows()
    {
        return rows;
    }

    public List<String> getColumn()
    {
        return column;
    }

    @Override
    public String toString()
    {
        return "QueryResult{" +
                "column=" + column +
                ", rows=" + rows +
                '}';
    }
}
