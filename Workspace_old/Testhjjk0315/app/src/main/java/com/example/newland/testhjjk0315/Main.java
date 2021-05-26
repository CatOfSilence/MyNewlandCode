package com.example.newland.testhjjk0315;

import java.util.HashSet;
import java.util.Set;

public class Main {
    static Object obj;
    public static void main(String[] args) {
        Set set = new HashSet();
        set.add(1);
        set.add(2);
        set.add(3);
        set.add(4);

        for (Object ob: set) {
            obj = ob;
        }
        System.out.println(obj);
    }
}
