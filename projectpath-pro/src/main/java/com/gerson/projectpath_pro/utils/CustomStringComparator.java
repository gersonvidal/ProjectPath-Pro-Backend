package com.gerson.projectpath_pro.utils;

import java.util.Comparator;

public class CustomStringComparator implements Comparator<String> {
    @Override
    public int compare(String s1, String s2) {
        // Order by length first
        if (s1.length() != s2.length()) {
            return Integer.compare(s1.length(), s2.length());
        }
        // If they are the same length, compare lexicographically
        return s1.compareTo(s2);
    }
}