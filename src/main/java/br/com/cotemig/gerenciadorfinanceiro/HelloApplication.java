package br.com.cotemig.gerenciadorfinanceiro;

import br.com.cotemig.gerenciadorfinanceiro.config.JPAUtil;
import br.com.cotemig.gerenciadorfinanceiro.util.ViewUtil;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(
                ViewUtil.carregarFXML("main-view.fxml"),
                1000,
                650
        );

        stage.getIcons().add(
                new Image(
                        HelloApplication.class.getResourceAsStream(
                                "icons/gerenciador-financeiro.png"
                        )
                )
        );

        ViewUtil.aplicarEstilo(scene);

        stage.setTitle("Gerenciador Financeiro");
        stage.setScene(scene);
        stage.setMinWidth(850);
        stage.setMinHeight(550);
        stage.show();
    }

    @Override
    public void stop() {
        JPAUtil.fechar();
    }

    public static void main(String[] args) {
        launch();
    }
}
