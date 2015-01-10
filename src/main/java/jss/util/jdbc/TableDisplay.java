package jss.util.jdbc;

import com.google.common.base.Strings;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TableDisplay
{
    private final Collection<?> titles;
    private final Collection<? extends Iterable> rows;
    private final Table<Integer, Integer, String> stringTable = HashBasedTable.create();
    private List<Integer> maxWidth;
    private String format;
    private String frame;
    private boolean initialized = false;

    /**
     * 打印类似 MySQL 那样的表
     */
    private TableDisplay(Collection<?> titles, Collection<? extends Iterable> rows)
    {
        this.titles = titles;
        this.rows = rows;
        maxWidth = Lists.newArrayList();
        for (int i = 0; i < titles.size(); i++)
        {
            maxWidth.add(0);
        }
    }

    public static void display(Collection<?> titles, Collection<? extends Iterable> rows)
    {
        new TableDisplay(titles, rows).display();
    }

    public static StringBuilder build(Collection<?> titles, Collection<? extends Iterable> rows)
    {
        return new TableDisplay(titles, rows).build();
    }

    public static TableDisplay create(Collection<?> titles, Collection<? extends Iterable> rows)
    {
        return new TableDisplay(titles, rows);
    }

    public static void main(String[] args)
    {
        ArrayList<Iterable> rows = Lists.newArrayList();
        rows.add(Lists.newArrayList("1", "wener"));
        rows.add(Lists.newArrayList("2", "xxx"));
        rows.add(Lists.newArrayList("xxx", "1"));
        TableDisplay td = new TableDisplay(Lists.newArrayList("ID", "Name"), rows);
        td.display();
    }

    public void display()
    {
        init();
        System.out.println(build());
    }

    public StringBuilder build()
    {
        init();

        StringBuilder sb = new StringBuilder();

        sb.append(frame)
          .append('\n');

        appendRow(sb, 0)
                .append('\n')
                .append(frame)
                .append('\n');


        for (int row = 1; row < rows.size() + 1; row++)
        {
            appendRow(sb, row)
                    .append('\n');
        }

        sb.append(frame)
          .append('\n');
        return sb;
    }

    private StringBuilder appendRow(StringBuilder sb, int row)
    {
        sb.append(String.format(format, stringTable.row(row).values().toArray()));
        return sb;
    }

    private void init()
    {
        if (initialized)
            return;
        initialized = true;


        int row = 0;
        int column = 0;
        for (Object title : titles)
        {
            cell(row, column++, title);
        }

        for (Iterable r : rows)
        {
            row++;
            column = 0;
            for (Object cell : r)
            {
                cell(row, column++, cell);
            }
        }

        {
            StringBuilder sb = new StringBuilder();
            sb.append('+');
            for (Integer width : maxWidth)
            {
                sb.append(Strings.repeat("-", width + 2))
                  .append("+");
            }

            frame = sb.toString();
        }
        {
            StringBuilder sb = new StringBuilder();
            sb.append("| ");
            for (Integer width : maxWidth)
            {
                sb.append("%-").append(width).append("s").append(" | ");
            }
            format = sb.toString();
        }
    }

    private int cell(int row, int column, Object title)
    {
        String value = cellString(title);
        stringTable.put(row, column, value);
        maxWidth.set(column, Math.max(maxWidth.get(column), value.length()));
        return column;
    }

    private String cellString(Object title)
    {
        if (title == null)
        {
            return "NULL";
        }
        return title.toString();
    }
}
