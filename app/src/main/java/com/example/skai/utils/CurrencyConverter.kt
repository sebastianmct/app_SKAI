package com.example.skai.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object CurrencyConverter {
    

    fun toCLP(price: Double): String {
        val symbols = DecimalFormatSymbols(Locale("es", "CL"))
        symbols.groupingSeparator = '.'
        symbols.decimalSeparator = ','
        
        val formatter = DecimalFormat("#,###", symbols)
        val formattedPrice = formatter.format(price.toInt()) // Convertir a entero para evitar decimales
        
        return "CLP $$formattedPrice"
    }


    fun toCLPWithoutSymbol(price: Double): String {
        val symbols = DecimalFormatSymbols(Locale("es", "CL"))
        symbols.groupingSeparator = '.'
        symbols.decimalSeparator = ','
        
        val formatter = DecimalFormat("#,###", symbols)
        return formatter.format(price.toInt())
    }
    

    fun toCLPWithLabel(price: Double): String {
        return toCLP(price) + " CLP"
    }
}

