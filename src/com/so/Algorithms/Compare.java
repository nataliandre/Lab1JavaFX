package com.so.Algorithms;

import java.util.Comparator;

public class Compare implements Comparator<Process>{
    public int compare(Process p1, Process p2) {
        int value = 0;

        if (p1.getBurstTime() > p2.getBurstTime())
            value = 1;
        if (p1.getBurstTime() < p2.getBurstTime())
            value = -1;
        return value;
    }
}
