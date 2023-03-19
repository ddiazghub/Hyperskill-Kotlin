package converter

import java.lang.Exception

val LENGTH = setOf(
    "m", "meter", "meters",
    "km", "kilometer", "kilometers",
    "cm", "centimeter", "centimeters",
    "mm", "millimeter", "millimeters",
    "mi", "mile", "miles",
    "yd", "yard", "yards",
    "ft", "foot", "feet",
    "in", "inch", "inches"
)

val WEIGHT = setOf(
    "g", "gram", "grams",
    "kg", "kilogram", "kilograms",
    "mg", "milligram", "milligrams",
    "lb", "pound", "pounds",
    "oz", "ounce", "ounces",
)

fun main() {
    val converter = UnitConverter()

    // Distance
    converter.addUnit(MeasurementUnit("m", "meter", "meters"))
    converter.addUnit(MeasurementUnit("km", "kilometer", "kilometers"))
    converter.addUnit(MeasurementUnit("cm", "centimeter", "centimeters"))
    converter.addUnit(MeasurementUnit("mm", "millimeter", "millimeters"))
    converter.addUnit(MeasurementUnit("mi", "mile", "miles"))
    converter.addUnit(MeasurementUnit("yd", "yard", "yards"))
    converter.addUnit(MeasurementUnit("ft", "foot", "feet"))
    converter.addUnit(MeasurementUnit("in", "inch", "inches"))

    converter.addConverter("km" to "m", 1000.0)
    converter.addConverter("cm" to "m", 0.01)
    converter.addConverter("mm" to "m", 0.001)
    converter.addConverter("mi" to "m", 1609.35)
    converter.addConverter("yd" to "m", 0.9144)
    converter.addConverter("ft" to "m", 0.3048)
    converter.addConverter("in" to "m", 0.0254)

    // Weight
    converter.addUnit(MeasurementUnit("g", "gram", "grams"))
    converter.addUnit(MeasurementUnit("kg", "kilogram", "kilograms"))
    converter.addUnit(MeasurementUnit("mg", "milligram", "milligrams"))
    converter.addUnit(MeasurementUnit("lb", "pound", "pounds"))
    converter.addUnit(MeasurementUnit("oz", "ounce", "ounces"))

    converter.addConverter("kg" to "g", 1000.0)
    converter.addConverter("mg" to "g", 0.001)
    converter.addConverter("lb" to "g", 453.592)
    converter.addConverter("oz" to "g", 28.3495)

    // Temperature
    converter.addUnit(MeasurementUnit("c", "degree celsius", "degrees celsius", "celsius", "dc"))
    converter.addUnit(MeasurementUnit("f", "degree fahrenheit", "degrees fahrenheit", "fahrenheit", "df"))
    converter.addUnit(MeasurementUnit("k", "kelvin", "kelvins"))

    converter.addConverter("f" to "c") { f -> (f - 32) * 5 / 9 }
    converter.addConverter("c" to "f") { c -> c * 9 / 5 + 32 }
    converter.addConverter("k" to "c") { k -> k - 273.15 }
    converter.addConverter("c" to "k") { c -> c + 273.15 }
    converter.addConverter("f" to "k") { f -> (f + 459.67) * 5 / 9 }
    converter.addConverter("k" to "f") { k -> k * 9 / 5 - 459.67 }

    while (true) {
        print("Enter what you want to convert (or exit): ")

        try {
            when (val input = readln().lowercase()) {
                "exit" -> break
                else -> {
                    val split = input.split(' ')
                    val value = split[0].toDouble()

                    val (convertFrom, convertTo) = when (split.size) {
                        4 -> arrayOf(split[1], split[3])
                        5 -> if (arrayOf("degree", "degrees").contains(split[1])) {
                            arrayOf("${split[1]} ${split[2]}", split[4])
                        } else {
                            arrayOf(split[1], "${split[3]} ${split[4]}")
                        }
                        else -> arrayOf("${split[1]} ${split[2]}", "${split[4]} ${split[5]}")
                    }

                    if (convertFrom in LENGTH && value < 0) {
                        println("Length shouldn't be negative")
                        continue
                    }

                    if (convertFrom in WEIGHT && value < 0) {
                        println("Weight shouldn't be negative")
                        continue
                    }

                    val result = when (val converted = converter.convert(value, convertFrom to convertTo)) {
                        null -> "Conversion from ${
                            if (converter.containsUnit(convertFrom)) converter.pluralFor(
                                convertFrom
                            ) else "???"
                        } to ${if (converter.containsUnit(convertTo)) converter.pluralFor(convertTo) else "???"} is impossible"

                        else -> converter.formatConversion(value to converted, convertFrom to convertTo)
                    }

                    println(result)
                }
            }
        } catch (e: Exception) {
            println("Parse error")
        }
    }
}
