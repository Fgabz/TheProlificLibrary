package com.prolificinteractive.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by Fanilo on 20/01/2015.
 */
public class MyCustomLayout extends RelativeLayout
{
    public MyCustomLayout(Context context)
    {
        super(context);
    }

    public MyCustomLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public float getXFraction() {
        return getX() / getWidth(); // TODO: guard divide-by-zero
    }

    public void setXFraction(float XFraction) {
        // TODO: cache width
        final int width = getWidth();
        setY((width > 0) ? (XFraction * width) : -9999);
    }

    public float getYFraction() {
        return getY() / getHeight(); // TODO: guard divide-by-zero
    }

    public void setYFraction(float YFraction) {
        // TODO: cache width
        final int height = getHeight();
        setY((height > 0) ? (YFraction * height) : -9999);
    }
}
