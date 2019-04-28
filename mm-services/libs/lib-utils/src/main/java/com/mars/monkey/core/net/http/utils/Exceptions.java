package com.mars.monkey.core.net.http.utils;

/**
 * Created on 2018/2/22.
 *
 * @author YouFeng.Zhu
 */
public class Exceptions
{
    public static void illegalArgument(String msg, Object... params)
    {
        throw new IllegalArgumentException(String.format(msg, params));
    }


}
