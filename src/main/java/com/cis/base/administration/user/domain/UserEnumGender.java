package com.cis.base.administration.user.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * @author YSivlay
 */
public enum UserEnumGender {

    M(1, "user.gender.male", "M"),
    F(2, "user.gender.female", "F");

    private final Integer id;
    private final String code;
    private final String value;

    private UserEnumGender(final Integer id, final String code, final String value) {
        this.id = id;
        this.code = code;
        this.value = value;
    }

    public static UserEnumGender fromInt(final Integer enumId) {
        UserEnumGender enumerations = null;
        if (enumId != null) {
            switch (enumId) {
                case 1 -> enumerations = M;
                case 2 -> enumerations = F;
                default -> {
                }
            }
        }
        return enumerations;
    }

    private static final Map<Integer, UserEnumGender> intToEnumMap = new HashMap<>();

    static {
        for (final UserEnumGender enumerations : values()) {
            intToEnumMap.put(enumerations.id, enumerations);
        }
    }

    private static int minValue;
    private static int maxValue;

    static {
        int i = 0;
        for (final UserEnumGender enumerations : values()) {
            if (i == 0) {
                minValue = enumerations.id;
            }
            intToEnumMap.put(enumerations.id, enumerations);
            if (minValue >= enumerations.id) {
                minValue = enumerations.id;
            }
            if (maxValue < enumerations.id) {
                maxValue = enumerations.id;
            }
            i = i + 1;
        }
    }

    public static int getMinValue() {
        return minValue;
    }

    public static int getMaxValue() {
        return maxValue;
    }

    public Integer getId() {
        return this.id;
    }

    public String getCode() {
        return this.code;
    }

    public String getValue() {
        return this.value;
    }
}