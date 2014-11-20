package com.github.mpjct.jmpjct.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class IO
{
    public static InputStream tryGetInputStream(String file)
    {
        InputStream config = ClassLoader.getSystemResourceAsStream(file);
        if (config == null)
        {
            try
            {
                config = new FileInputStream(file);
            } catch (FileNotFoundException e)
            {
                throw new RuntimeException(e);
            }
        }
        return config;
    }
}
