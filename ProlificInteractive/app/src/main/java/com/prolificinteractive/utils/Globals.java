package com.prolificinteractive.utils;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Fanilo on 20/01/2015.
 */
public class Globals
{
    private static Globals instance;

    @Getter
    @Setter
    private String url;

    // Restrict the constructor from being instantiated
    private Globals()
    {}


    public static Globals getInstance()
    {
        if (instance == null)
        {
            synchronized (Globals.class)
            {
                instance = new Globals();
            }
        }
        return instance;
    }
}
