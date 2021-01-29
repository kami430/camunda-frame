package com.flow;

import com.flow.base.utils.DateUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Apptest {

    public static void main(String[] args) {
        LocalDateTime dt = LocalDateTime.now();
        LocalDateTime ds = DateUtils.startOfQuarter(dt);
        System.out.println(ds.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        LocalDateTime dl = DateUtils.endOfQuarter(dt);
        System.out.println(dl.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }
}
