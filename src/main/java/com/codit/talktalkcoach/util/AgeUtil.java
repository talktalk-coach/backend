package com.codit.talktalkcoach.util;

import java.time.LocalDate;
import java.time.Period;

public class AgeUtil {

    private AgeUtil() {}

    public static int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    public static boolean isUnder14(LocalDate birthDate) {
        if (birthDate == null) return false;
        return calculateAge(birthDate) < 14;
    }
}
