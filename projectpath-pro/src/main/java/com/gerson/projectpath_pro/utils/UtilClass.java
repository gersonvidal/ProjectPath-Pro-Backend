package com.gerson.projectpath_pro.utils;

import java.util.Comparator;

public class UtilClass {

    public static int binarySearch(String[] array, String target, Comparator<String> comparator, int endIndex) {
        int low = 0;
        int high = array.length - endIndex;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            int cmp = comparator.compare(array[mid], target);

            if (cmp == 0) {
                return mid;
            } else if (cmp < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return -1;
    }
}
