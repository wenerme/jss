package jss.util.impl;

import jss.util.IsValue;

public class SimpleValue<T> implements IsValue<T>
{
    private T value;

    public SimpleValue(T value)
    {
        this.value = value;
    }

    @Override
    public T get()
    {
        return value;
    }
}
