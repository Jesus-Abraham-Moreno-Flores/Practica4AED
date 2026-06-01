package uabc.computacion.practica4aed;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class TableroUI extends GridPane {

    private static final int TAMANIO_CELDA = 60;
    private static final String ESTILO_NORMAL =
            "-fx-min-width: 60; -fx-min-height: 60;" +
                    "-fx-background-color: #ffffff;" +
                    "-fx-border-color: #c8c8d2; -fx-border-width: 1;" +
                    "-fx-font-size: 18; -fx-font-weight: bold;" +
                    "-fx-text-fill: #323250;";
    private static final String ESTILO_SELECCIONADO =
            "-fx-min-width: 60; -fx-min-height: 60;" +
                    "-fx-background-color: #5088f0;" +
                    "-fx-border-color: #3060c0; -fx-border-width: 2;" +
                    "-fx-font-size: 18; -fx-font-weight: bold;" +
                    "-fx-text-fill: white;";
    private static final String ESTILO_PISTA =
            "-fx-min-width: 60; -fx-min-height: 60;" +
                    "-fx-background-color: #ffc31e;" +
                    "-fx-border-color: #c8960a; -fx-border-width: 2;" +
                    "-fx-font-size: 18; -fx-font-weight: bold;" +
                    "-fx-text-fill: #785000;";
    private static final String ESTILO_VACIA =
            "-fx-min-width: 60; -fx-min-height: 60;" +
                    "-fx-background-color: #e6e6eb;" +
                    "-fx-border-color: #d0d0d8; -fx-border-width: 1;";

    private final Tablero gameBoard;
    private final Controlador controller;
    private Node seleccionado;
    private Node[] pista;

    public TableroUI(Tablero gameBoard, Controlador controller) {
        this.gameBoard = gameBoard;
        this.controller = controller;
        this.seleccionado = null;
        this.pista = null;

        setHgap(4);
        setVgap(4);
        setAlignment(Pos.CENTER);

        dibujarTablero();
    }

    // Recorremos la lista y ponemos tambien un boton por cada posicion del tablero
    public void dibujarTablero() {
        getChildren().clear();

        // Llenamos todas las posiciones con celdas vacias
        for (int r = 0; r < gameBoard.getFilas(); r++) {
            for (int c = 0; c < gameBoard.getColumnas(); c++) {
                Button celda = new Button();
                celda.setStyle(ESTILO_VACIA);
                add(celda, c, r);
            }
        }

        // Ahora recorremos la lista y reemplazamos las posiciones activas
        ListaSimple.NodoLista r = gameBoard.getLista().obtenerNodoInicio();
        while (r != null) {
            Node nodo = r.info;
            Button boton = crearBoton(nodo);
            add(boton, nodo.getCol(), nodo.getRow());
            r = r.siguiente;
        }
    }

    // Creamos el boton para cada nodo que este activo dandole su estilo
    public Button crearBoton(Node nodo) {
        Button boton = new Button(String.valueOf(nodo.getNumber()));

        if (pista != null && (nodo == pista[0] || nodo == pista[1])) {
            boton.setStyle(ESTILO_PISTA);
        } else if (nodo == seleccionado) {
            boton.setStyle(ESTILO_SELECCIONADO);
        } else {
            boton.setStyle(ESTILO_NORMAL);
        }

        boton.setOnAction(e -> manejarClic(nodo));
        return boton;
    }

    // Maneja el clic sobre una casilla
    public void manejarClic(Node nodo) {
        pista = null;

        if (seleccionado == null) {
            // Primera seleccion
            seleccionado = nodo;
            dibujarTablero();
            return;
        }

        if (seleccionado == nodo) {
            // Si le damos clic al nodo ya seleccionado entonces lo deseleccionamos
            seleccionado = null;
            dibujarTablero();
            return;
        }

        // El segundo nodo seleccionado probamos directamente un match
        boolean exitoso = gameBoard.intentarMatch(seleccionado, nodo);
        seleccionado = null;

        if (!exitoso) {
            // Si no concuerda selecciona el nuevo nodo
            seleccionado = nodo;
        } else {
            controller.onMatchRealizado();
        }

        dibujarTablero();
    }

    // Mostramos una pista resaltando los dos nodos sugeridos
    public void mostrarPista(Node[] par) {
        this.pista = par;
        seleccionado = null;
        dibujarTablero();
    }

    // Con esto quitamos la seleccion y la pista
    public void limpiar() {
        seleccionado = null;
        pista = null;
        dibujarTablero();
    }
}
