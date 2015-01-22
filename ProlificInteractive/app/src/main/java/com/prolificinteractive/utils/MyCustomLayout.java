package com.prolificinteractive.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

/**
 * Created by Fanilo on 20/01/2015.
 */


/*
    CustomLayout for the slide animations
*/
public class MyCustomLayout extends RelativeLayout
{
    private float yFraction = 0;
    private float xFraction = 0;
    private ViewTreeObserver.OnPreDrawListener preDrawListener = null;


    public MyCustomLayout(Context context)
    {
        super(context);
    }

    public MyCustomLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public float getXFraction(float fraction)
    {
           return this.xFraction;
    }

    public void setXFraction(float fraction)
    {
        this.xFraction = fraction;

        if (getWidth() == 0)
        {
            if (preDrawListener == null)
            {
                preDrawListener = new ViewTreeObserver.OnPreDrawListener()
                {
                    @Override
                    public boolean onPreDraw()
                    {
                        getViewTreeObserver().removeOnPreDrawListener(preDrawListener);
                        setXFraction(xFraction);
                        return true;
                    }
                };
                getViewTreeObserver().addOnPreDrawListener(preDrawListener);
            }
            return;
        }
        float translationx = getWidth() * fraction;
        setTranslationX(translationx);
    }


    public void setYFraction(float fraction)
    {

        this.yFraction = fraction;

        if (getHeight() == 0)
        {
            if (preDrawListener == null)
            {
                preDrawListener = new ViewTreeObserver.OnPreDrawListener()
                {
                    @Override
                    public boolean onPreDraw()
                    {
                        getViewTreeObserver().removeOnPreDrawListener(preDrawListener);
                        setYFraction(yFraction);
                        return true;
                    }
                };
                getViewTreeObserver().addOnPreDrawListener(preDrawListener);
            }
            return;
        }

        float translationY = getHeight() * fraction;
        setTranslationY(translationY);
    }

    public float getYFraction()
    {
        return this.yFraction;

    }
}