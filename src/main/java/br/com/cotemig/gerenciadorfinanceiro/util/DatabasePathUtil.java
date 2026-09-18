package br.com.cotemig.gerenciadorfinanceiro.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class DatabasePathUtil {

    private static final String NOME_APLICACAO = "GerenciadorFinanceiro";
    private static final String NOME_BANCO = "gerenciador_financeiro.db";

    private DatabasePathUtil() {
    }

    public static Path getDiretorioAplicacao() {
        String localAppData = System.getenv("LOCALAPPDATA");

        Path diretorio;

        if (localAppData != null && !localAppData.isBlank()) {
            diretorio = Path.of(localAppData, NOME_APLICACAO);
        } else {
            diretorio = Path.of(
                    System.getProperty("user.home"),
                    "." + NOME_APLICACAO
            );
        }

        criarDiretorio(diretorio);

        return diretorio;
    }

    public static Path getCaminhoBanco() {
        return getDiretorioAplicacao().resolve(NOME_BANCO);
    }

    public static String getUrlJdbc() {
        return "jdbc:sqlite:" + getCaminhoBanco().toAbsolutePath();
    }

    private static void criarDiretorio(Path diretorio) {
        try {
            Files.createDirectories(diretorio);
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Não foi possível criar a pasta de dados do aplicativo.",
                    e
            );
        }
    }
}