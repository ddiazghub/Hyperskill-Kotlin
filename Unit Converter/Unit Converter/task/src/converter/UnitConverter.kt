package converter

class MeasurementUnit(val symbol: String, val singularName: String, val pluralName: String, vararg aliases: String) {
    val aliases = aliases
}

class UnitConverter {
    private sealed class Converter {
        class Intermediate(val next: String, val converter: (Double) -> Double) : Converter()
        class Direct(val converters: MutableMap<String, (Double) -> Double>) : Converter()
    }

    private val converters: MutableMap<String, Converter> = mutableMapOf()
    val aliases: MutableMap<String, String> = mutableMapOf()
    val units: MutableMap<String, MeasurementUnit> = mutableMapOf()

    fun containsUnit(unit: String) = this.converters.containsKey(unit.lowercase()) || this.aliases.containsKey(unit.lowercase())

    fun addUnit(unit: MeasurementUnit) {
        for (alias in unit.aliases) {
            this.aliases[alias] = unit.symbol
        }

        this.aliases[unit.pluralName] = unit.symbol
        this.aliases[unit.singularName] = unit.symbol
        this.units[unit.symbol] = unit
    }

    fun formatConversion(values: Pair<Number, Number>, units: Pair<String, String>): String {
        val unit1 = this.getUnitName(units.first)
        val unit2 = this.getUnitName(units.second)

        return "${formatUnit(values.first, unit1)} is ${formatUnit(values.second, unit2)}"
    }

    fun addConverter(conversion: Pair<String, String>, rate: Double) = this.addIntermediate(conversion, rate)
    fun addConverter(conversion: Pair<String, String>, converter: (Double) -> Double) = this.addDirect(conversion, converter)

    fun convert(value: Double, conversion: Pair<String, String>): Double? {
        val unit1 = this.getUnitName(conversion.first)
        val unit2 = this.getUnitName(conversion.second)

        if (!(this.converters.containsKey(unit1)))
            return null

        return if (unit1 == unit2) value
            else converters[unit1]?.let { converter ->
                when (converter) {
                    is Converter.Intermediate -> this.convert(converter.converter.invoke(value), converter.next to unit2)
                    is Converter.Direct -> converter.converters[unit2]?.invoke(value)
                }
            }
    }

    fun pluralFor(unit: String): String? = this.units[this.getUnitName(unit)]?.pluralName
    fun singularFor(unit: String): String? = this.units[this.getUnitName(unit)]?.singularName

    private fun addIntermediate(conversion: Pair<String, String>, rate: Double) {
        val (convertFrom, convertTo) = (conversion.first to conversion.second)
        this.converters[convertFrom] = Converter.Intermediate(convertTo, byRate(rate))
        this.addDirect(conversion.second to conversion.first, byRate(1.0 / rate))
    }

    private fun addDirect(conversion: Pair<String, String>, converter: (Double) -> Double) {
        val (convertFrom, convertTo) = (conversion.first to conversion.second)

        if (!this.converters.containsKey(convertFrom)) {
            this.converters[convertFrom] = Converter.Direct(mutableMapOf())
        }

        (this.converters[convertFrom]!! as Converter.Direct).converters[convertTo] = converter
    }

    private fun getUnitName(unit: String): String = this.aliases[unit] ?: unit

    private fun formatUnit(value: Number, unit: String): String = "$value ${if (value == 1.0) this.units[unit]?.singularName else this.units[unit]?.pluralName}"

    companion object {
        fun byRate(rate: Double): (Double) -> Double = { value -> value * rate }
    }
}