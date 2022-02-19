package com.example.list.androidchart.uitily;

import android.graphics.Color;

import java.util.ArrayList;

public class PieColors {
    private static ArrayList<Integer> colors;
    private PieColors(){

    }
    public static ArrayList<Integer> getColors(){
        if(colors ==null) {
            colors = new ArrayList<Integer>();

            colors.add(Color.argb(255, 100, 149, 237));
            colors.add(Color.argb(255, 220, 220, 220));
            colors.add(Color.argb(255, 255, 228, 181));
            colors.add(Color.argb(255, 176,48,96));
            colors.add(Color.argb(255, 240,128,128));

            colors.add(Color.argb(255, 160,32,240));
            colors.add(Color.argb(255, 205, 133, 63));
            colors.add(Color.argb(255, 255, 0, 255));
            colors.add(Color.argb(255, 219,147,112));
            colors.add(Color.argb(255, 112, 128, 144));

            colors.add(Color.argb(255, 255,239,219));
            colors.add(Color.argb(255, 60, 179, 113 ));
            colors.add(Color.argb(255, 0, 255, 0));
            colors.add(Color.argb(255, 179, 238, 58));
            colors.add(Color.argb(255, 32, 178, 170));
            colors.add(Color.argb(255, 238,180,34));
            colors.add(Color.argb(255, 255,255,0));
            colors.add(Color.argb(255, 205,181,205));
            colors.add(Color.argb(255, 139,95,101));
            colors.add(Color.argb(255, 209,146,117));
            colors.add(Color.argb(255, 147, 112, 219));
            colors.add(Color.argb(255, 238,122,233));
            colors.add(Color.argb(255, 227,91,216));
            colors.add(Color.argb(255, 153,204,50));
            colors.add(Color.argb(255, 255, 228, 181));
            colors.add(Color.argb(255, 255, 228, 181));
            colors.add(Color.argb(255, 255, 228, 181));
            colors.add(Color.argb(255, 255, 228, 181));
            colors.add(Color.argb(255, 255, 228, 181));
            colors.add(Color.argb(255, 255, 228, 181));
            colors.add(Color.argb(255, 255, 228, 181));
        }
        return colors;
    }
}
