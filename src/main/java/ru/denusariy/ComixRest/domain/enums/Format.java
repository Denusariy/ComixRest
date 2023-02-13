package ru.denusariy.ComixRest.domain.enums;

public enum Format {
    SING("Сингл"),
    TPB("ТПБ"),
    HARD("Хардкавер"),
    OMNI("Омнибус");

    private final String displayValue;
    private Format(String displayValue) {
        this.displayValue = displayValue;
    }
    public String getDisplayValue() {
        return displayValue;
    }
}
