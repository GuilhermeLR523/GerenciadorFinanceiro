package br.com.cotemig.gerenciadorfinanceiro.util;

import java.math.BigDecimal;

public final class ValorUtil {

    private ValorUtil() {
    }

    public static BigDecimal converterParaBigDecimal(String texto) {
        if (texto == null || texto.isBlank()) {
            throw new NumberFormatException("Valor vazio.");
        }

        String valor = texto
                .trim()
                .replace("R$", "")
                .replace(" ", "");

        if (valor.contains(",")) {
            valor = valor
                    .replace(".", "")
                    .replace(",", ".");
        } else if (!valor.matches("[-+]?\\d+\\.\\d{1,2}")) {
            valor = valor.replace(".", "");
        }

        return new BigDecimal(valor);
    }
}
