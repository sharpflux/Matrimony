package com.example.matrimonyapp.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateParser {
    private static DateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");

    public static String convertDateToString(Date date) {
        String strDate = "";
        strDate = dateFormat1.format(date);
        return strDate;
    }
}