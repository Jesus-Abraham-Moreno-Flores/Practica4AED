package uabc.computacion.practica4aed;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Dialogo dialogo = new Dialogo();
        dialogo.showAndWait().ifPresent(resultado -> {
            Controlador controller = new Controlador(stage, resultado[0], resultado[1]);
            Scene escena = new Scene(controller);
            stage.setTitle("Numbers Match");
            stage.setScene(escena);
            stage.setResizable(false);
            stage.show();
            stage.sizeToScene();
        });

        // Si el jugador cierra el dialogo sin confirmar las configuracion, simplemente se cierra
        if (stage.getScene() == null) {
            stage.close();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
