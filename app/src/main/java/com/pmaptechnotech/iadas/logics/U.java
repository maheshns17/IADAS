package com.pmaptechnotech.iadas.logics;


import com.pmaptechnotech.iadas.models.User;

import java.util.Random;

/**
 * Created by Admin on 1/7/2018.
 */

public class U {
    public static User user;
    public static final int SIGNATURE = 1;
    public static int STROKES = 0;
    public static int X_AXIS = 0;
    public static int Y_AXIS = 0;
    public static String DEPT_TYPE = "";

    private static int getRandomNumberInRange() {
        int min=100; int max=200;
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public static int getXAxisValue() {
        int min=100; int max=200;
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;

    }

    public static int getYAxisValue() {
        int min=100; int max=200;
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;

    }

}
