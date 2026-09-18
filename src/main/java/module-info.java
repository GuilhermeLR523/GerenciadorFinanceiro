module br.com.cotemig.gerenciadorfinanceiro {

    requires javafx.controls;
    requires javafx.fxml;

    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.sql;

    opens br.com.cotemig.gerenciadorfinanceiro.controller
            to javafx.fxml;

    opens br.com.cotemig.gerenciadorfinanceiro.model
            to org.hibernate.orm.core, javafx.base;

    exports br.com.cotemig.gerenciadorfinanceiro;
}
