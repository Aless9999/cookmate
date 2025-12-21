/*
 *
 *  * Copyright 2025 Кодер Als
 *  *
 *  * Licensed under the Apache License, Version 2.0
 *  * See https://www.apache.org/licenses/LICENSE-2.0
 *
 *
 */

package com.macnigor.cookmate.utils;

public class IngredientUtils {


    public static Integer parseAndConvertAmount(String amount) {
        if (amount == null || amount.isBlank()) return null;

        // 1. Извлекаем число
        String numberStr = amount.replaceAll("[^0-9]", "");
        if (numberStr.isEmpty()) return null;

        int number = Integer.parseInt(numberStr);

        // 2. Конвертируем по единице измерения
        int converted = convertAmount(amount, number);

        // 3. Возвращаем результат
        return converted;
    }

    // Метод convertAmount (из предыдущего примера)
    private static int convertAmount(String amount, int number) {
        String unit = extractUnit(amount);

        return switch (unit) {
            case "кг" -> number * 1000;   // кг -> г
            case "г" -> number;           // граммы оставляем
            case "л" -> number * 1000;    // литры -> мл
            case "мл" -> number;          // миллилитры оставляем
            case "стл" -> number * 15;  // столовые ложки -> мл
            default -> number;            // неизвестная единица
        };
    }


    // Метод extractUnit
    public static String extractUnit(String amount) {
        if (amount == null || amount.isBlank()) return "";

        // Убираем цифры
        String unit = amount.replaceAll("[0-9]", "").trim();

        // Убираем точки и лишние пробелы
        unit = unit.replaceAll("\\.", "").replaceAll("\\s+", "");

        // Например, "ст. л." -> "стл"
        return unit.toLowerCase();  // приводим к нижнему регистру для удобства
    }


}



