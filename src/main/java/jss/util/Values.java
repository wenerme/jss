package jss.util;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import java.util.EnumSet;
import jss.proto.define.CapabilityFlag;
import jss.util.impl.SimpleValue;

public class Values
{
    private static final Table<Class, Object, Object> cache = HashBasedTable.create();

    private Values() {}

    public static <T extends Enum<T> & IsInteger> EnumSet<T> asEnumSet(long flag, Class<T> clazz)
    {
        EnumSet<T> set = EnumSet.noneOf(clazz);
        for (int i = 0; i < 32; i++)
        {
            int v = 1 << i;
            if ((flag & v) > 0)
            {
                T e = fromValue(clazz, v);
                if (e == null)
                    continue;
                set.add(e);
            }
        }

        return set;
    }

    public static int or(IsInteger... integers)
    {
        int result = 0;
        for (IsInteger integer : integers)
        {
            result |= integer.get();
        }
        return result;
    }

    public static int or(Iterable<? extends IsInteger> integers)
    {
        int result = 0;
        for (IsInteger integer : integers)
        {
            result |= integer.get();
        }
        return result;
    }

    public static <V, T extends Enum & IsValue<V>> void cache(Class<T> type)
    {
        for (T item : type.getEnumConstants())
        {
            cache.put(type, item.get(), item);
        }
    }

    public static <T> IsValue<T> valueOf(T value)
    {
        return new SimpleValue<>(value);
    }

    /**
     * @return null if not found
     */
    @SuppressWarnings("unchecked")
    public static <V, T extends Enum & IsValue<V>> T fromValue(Class<T> type, V v)
    {
        return (T) cache.get(type, v);
    }

    public static void main(String[] args)
    {
        cache(CapabilityFlag.class);
        EnumSet<CapabilityFlag> set = asEnumSet(CapabilityFlag.CLIENT_BASIC_FLAGS.get(), CapabilityFlag.class);
        System.out.println(set);

        set = asEnumSet(or(CapabilityFlag.CLIENT_LONG_FLAG, CapabilityFlag.CLIENT_COMPRESS), CapabilityFlag.class);
        System.out.println(set);

        System.out.println(or(set));
    }
}
