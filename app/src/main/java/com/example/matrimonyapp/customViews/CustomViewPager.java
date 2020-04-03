package com.example.matrimonyapp.customViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;

public class CustomViewPager extends ViewPager {


    public CustomViewPager(@NonNull Context context) {
        super(context);
    }

    public CustomViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPageChangeListener();
    }


    public void initPageChangeListener()
    {
        addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                requestLayout();
            }
        });
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height =0;

        //for(int i=0; i<getChildCount(); i++)
            View child = getCurrentView(this);
            //View child = getChildAt(getCurrentItem());
            if(child!=null)
            {

                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED));

                int h = child.getMeasuredHeight();
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(h,MeasureSpec.EXACTLY);
            }


        super.onMeasure(widthMeasureSpec,heightMeasureSpec);

    }

    View getCurrentView(ViewPager viewPager)
    {
        try{

            final int currentItem = viewPager.getCurrentItem();

            for(int i=0; i<viewPager.getChildCount(); i++)
            {
                final View child = viewPager.getChildAt(i);
                final ViewPager.LayoutParams layoutParams = (ViewPager.LayoutParams)child.getLayoutParams();

                Field f = layoutParams.getClass().getDeclaredField("position");

                f.setAccessible(true);

                int position = (Integer)f.get(layoutParams);

                if(!layoutParams.isDecor && currentItem==position) {
                    return child;
                }

            }


        }catch (NoSuchFieldException e)
        {
            e.fillInStackTrace();
        }catch (IllegalArgumentException e)
        {
            e.fillInStackTrace();
        }catch (IllegalAccessException e)
        {
            e.fillInStackTrace();
        }
        return null;

    }

}
