package com.example.skai.utils

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class CurrencyConverterTest : StringSpec({

    "Cuando formateo un precio, debe mostrar formato CLP correcto" {

        val price = 19990.0
        

        val result = CurrencyConverter.toCLP(price)
        

        result shouldBe "CLP $19.990"
    }

    "Cuando formateo un precio con decimales, debe redondear a entero" {

        val price = 15990.50
        

        val result = CurrencyConverter.toCLP(price)
        

        result shouldBe "CLP $15.990"
    }

    "Cuando formateo un precio alto, debe usar separadores de miles" {

        val price = 89990.0
        

        val result = CurrencyConverter.toCLP(price)
        

        result shouldBe "CLP $89.990"
    }

    "Cuando formateo sin símbolo, debe retornar solo el número" {

        val price = 29990.0
        

        val result = CurrencyConverter.toCLPWithoutSymbol(price)
        

        result shouldBe "29.990"
        result.contains("$") shouldBe false
        result.contains("CLP") shouldBe false
    }

    "Cuando formateo con label completo, debe incluir CLP al final" {

        val price = 49990.0
        

        val result = CurrencyConverter.toCLPWithLabel(price)
        

        result shouldBe "CLP $49.990 CLP"
    }

    "Cuando formateo un precio pequeño, debe mostrar correctamente" {

        val price = 4990.0
        

        val result = CurrencyConverter.toCLP(price)
        

        result shouldBe "CLP $4.990"
    }

    "Cuando formateo precio de 100, debe mostrar sin separador" {

        val price = 100.0
        

        val result = CurrencyConverter.toCLP(price)
        

        result shouldBe "CLP $100"
    }
})

