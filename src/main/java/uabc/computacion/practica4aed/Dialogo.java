package uabc.computacion.practica4aed;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;

public class Dialogo extends Dialog<int[]> {

    private final Spinner<Integer> spinnerFilas;
    private final Spinner<Integer> spinnerColumnas;

    public Dialogo() {
        setTitle("Numbers Match");
        setHeaderText("Configura el tamaño del tablero");

        spinnerFilas = new Spinner<>(4, 10, 4);
        spinnerColumnas = new Spinner<>(10, 20, 10);

        spinnerFilas.setEditable(true);
        spinnerColumnas.setEditable(true);

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(12);
        grid.setPadding(new Insets(20));
        grid.setAlignment(Pos.CENTER);

        grid.add(new Label("Renglones:"), 0, 0);
        grid.add(spinnerFilas, 1, 0);
        grid.add(new Label("Columnas:"), 0, 1);
        grid.add(spinnerColumnas, 1, 1);

        getDialogPane().setContent(grid);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Asi convertimos el resultado del diálogo en un arreglo con filas y columnas
        setResultConverter(boton -> {
            if (boton == ButtonType.OK) {
                return new int[]{spinnerFilas.getValue(), spinnerColumnas.getValue()};
            }
            return null;
        });
    }
}