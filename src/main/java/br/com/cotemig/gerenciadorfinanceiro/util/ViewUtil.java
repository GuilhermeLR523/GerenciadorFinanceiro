package br.com.cotemig.gerenciadorfinanceiro.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.Optional;
import java.io.IOException;
import java.net.URL;

public final class ViewUtil {

    private static final String BASE_PATH =
            "/br/com/cotemig/gerenciadorfinanceiro/";

    private static final String STYLE_FILE = "style.css";

    private ViewUtil() {
    }

    public static Parent carregarFXML(String arquivoFxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(obterRecurso(arquivoFxml));
        return loader.load();
    }

    public static void aplicarEstilo(Scene scene) {
        scene.getStylesheets().add(
                obterRecurso(STYLE_FILE).toExternalForm()
        );
    }

    public static void abrirModal(
            String arquivoFxml,
            String titulo,
            Window janelaDona
    ) throws IOException {

        Parent root = carregarFXML(arquivoFxml);
        Scene scene = new Scene(root);
        aplicarEstilo(scene);

        Stage stage = new Stage();
        stage.setTitle(titulo);
        stage.setScene(scene);
        stage.setResizable(false);

        if (janelaDona != null) {
            stage.initOwner(janelaDona);
            stage.initModality(Modality.WINDOW_MODAL);
        } else {
            stage.initModality(Modality.APPLICATION_MODAL);
        }

        stage.showAndWait();
    }

    private static URL obterRecurso(String nomeArquivo) {
        URL recurso = ViewUtil.class.getResource(BASE_PATH + nomeArquivo);

        if (recurso == null) {
            throw new IllegalStateException(
                    "Recurso não encontrado: " + BASE_PATH + nomeArquivo
            );
        }

        return recurso;
    }

    public static boolean confirmarExclusao(
            String titulo,
            String mensagem
    ) {

        Alert alerta = new Alert(
                Alert.AlertType.CONFIRMATION
        );

        alerta.setTitle(titulo);
        alerta.setHeaderText("Confirmar exclusão");
        alerta.setContentText(mensagem);

        ButtonType botaoExcluir = new ButtonType(
                "Excluir",
                ButtonBar.ButtonData.OK_DONE
        );

        ButtonType botaoCancelar = new ButtonType(
                "Cancelar",
                ButtonBar.ButtonData.CANCEL_CLOSE
        );

        alerta.getButtonTypes().setAll(
                botaoExcluir,
                botaoCancelar
        );

        Optional<ButtonType> resultado =
                alerta.showAndWait();

        return resultado.isPresent()
                && resultado.get() == botaoExcluir;
    }
}
