package com.example.runnable;

import java.util.ArrayList;
import java.util.List;

public class MultiParallel {

    public static void main(String[] args) {
        int cores = Runtime.getRuntime().availableProcessors();
        String[] strings = new String[1000];
        List<String> ls = new ArrayList<>();
        for (int i = 0; i < strings.length; i++) {
            ls.add("hehe" + i);
        }
        int total = ls.size();
        int page = 1;
        int limit = 10;
        Thread[] threads = new Thread[cores];

        for (Thread thread : threads) {

        }
    }

}
