package com.gerson.projectpath_pro.utils;

public class StringValidator {

    // Only allows A, AA, B, BB, Z, ZZ, AAA, AAA
    // But also allows AB, ABBB, ABC, so that's why we use the method below
    private static final String REGEX = "^[A-Z]+$";

    public static boolean isValidLabelString(String label) {
        String regex = REGEX;

        // Validate that all letters are the same
        return label.matches(regex) && label.chars().distinct().count() == 1;
    }

    public static boolean predecessorsIsNotRecursive(String predecessors, String label) {
        // for cases where the activity has no predecessors
        if (predecessors == null) {
            return true; // it isn't recursive
        }

        String[] predecessorsArray = predecessors.split(",");

        // Verify if label is present in predecessors
        for (String predecessor : predecessorsArray) {
            if (predecessor.equals(label)) {
                return false; // it's recursive
            }
        }
        return true; // it isn't recursive
    }

    public static boolean isLabelGreaterThanPredecessors(String predecessors, String label) {
        if (predecessors == null) {
            return true; // If there is no predecessors, condition aplies
        }

        String[] predecessorsArray = predecessors.split(",");

        // Usa the comparator to verify each predecessor
        CustomStringComparator comparator = new CustomStringComparator();

        for (String predecessor : predecessorsArray) {
            if (comparator.compare(predecessor, label) > 0) {
                // if predecessor is greater than the label, condition doesn't apply
                return false;
            }
        }
        return true; // All predecessors are lower or equal
    }

}
