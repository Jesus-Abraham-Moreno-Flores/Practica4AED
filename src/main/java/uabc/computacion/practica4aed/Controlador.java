package uabc.computacion.practica4aed;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Controlador extends BorderPane {

    private Stage stage;

    private Tablero gameBoard;
    private TableroUI gamePanel;

    private Label labelSuma;
    private Label labelEncontradas;
    private Label labelPendientes;
    private Button botonDeshacer;

    public Controlador(Stage stage, int filas, int columnas) {
        this.stage = stage;
        this.gameBoard = new Tablero(filas, columnas);
        this.gamePanel = new TableroUI(gameBoard, this);
        construirUI();
    }

    // Construimos toda la interfaz, o sea la parte superior, el centro y la parte inferior de la pantalla
    public void construirUI() {
        // Barra superior con botones
        botonDeshacer = new Button("Deshacer");
        Button botonPista = new Button("Pista");
        Button botonNuevo = new Button("Nuevo Juego");

        botonDeshacer.setOnAction(e -> deshacer());
        botonPista.setOnAction(e -> pista());
        botonNuevo.setOnAction(e -> nuevoJuego());

        botonDeshacer.setDisable(true);

        HBox barraTop = new HBox(10, botonDeshacer, botonPista, botonNuevo);
        barraTop.setPadding(new Insets(12, 16, 12, 16));
        barraTop.setAlignment(Pos.CENTER_LEFT);
        barraTop.setStyle("-fx-background-color: #28263c;");

        // Barra inferior con contadores
        labelEncontradas = new Label("Encontradas: 0");
        labelPendientes = new Label("Pendientes: " + gameBoard.getConcordanciasPendientes());
        labelSuma = new Label("Suma: " +  gameBoard.getSumaPares());

        labelSuma.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13;");
        labelEncontradas.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13;");
        labelPendientes.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13;");

        HBox barraBottom = new HBox(30, labelEncontradas, labelPendientes, labelSuma);
        barraBottom.setPadding(new Insets(10, 16, 10, 16));
        barraBottom.setAlignment(Pos.CENTER_LEFT);
        barraBottom.setStyle("-fx-background-color: #28263c;");

        // Panel del tablero centrado
        VBox centro = new VBox(gamePanel);
        centro.setAlignment(Pos.CENTER);
        centro.setPadding(new Insets(16));

        setTop(barraTop);
        setCenter(centro);
        setBottom(barraBottom);
    }

    // Metodo para deshacer el ultimo movimiento
    public void deshacer() {
        gameBoard.deshacerMovimiento();
        gamePanel.limpiar();
        actualizarContadores();
        botonDeshacer.setDisable(!gameBoard.puedeDeshacer());
    }

    // Solicitamos una pista al tablero y se la pasa al panel
    public void pista() {
        Node[] par = gameBoard.obtenerPista();
        if (par == null) {
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Pista");
            alerta.setHeaderText(null);
            alerta.setContentText("No hay concordancias disponibles en este momento.");
            alerta.showAndWait();
        } else {
            gamePanel.mostrarPista(par);
        }
    }

    // Reinicia el juego mostrando el dialogo de las dimensiones del juego
    public void nuevoJuego() {
        Dialogo dialogo = new Dialogo();
        dialogo.showAndWait().ifPresent(resultado -> {
            gameBoard = new Tablero(resultado[0], resultado[1]);
            gamePanel = new TableroUI(gameBoard, this);
            construirUI();
            actualizarContadores();
            stage.sizeToScene();
        });
    }

    // Este metoodo lo llama el GamePanel cada vez que se realiza un match exitoso
    public void onMatchRealizado() {
        actualizarContadores();
        botonDeshacer.setDisable(!gameBoard.puedeDeshacer());

        if (gameBoard.juegoTerminado()) {
            mostrarFin("Has ganado!", "Completaste el tablero. Encontradas: "
                    + gameBoard.getConcordanciasEncontradas(), true);
        } else if (!gameBoard.hayMovimientos()) {
            mostrarFin("Sin movimientos", "Ya no hay concordancias posibles. Encontradas: "
                    + gameBoard.getConcordanciasEncontradas(), false);
        }
    }

    // Actualizamos las etiquetas de contadores
    public void actualizarContadores() {
        labelEncontradas.setText("Encontradas: " + gameBoard.getConcordanciasEncontradas());
        labelPendientes.setText("Pendientes: " + gameBoard.getConcordanciasPendientes());
        labelSuma.setText("Suma: " + gameBoard.getSumaPares());
    }

    // Mostramos el dialogo cuando acaba el juego
    public void mostrarFin(String titulo, String mensaje, boolean gano) {
        Alert alerta = new Alert(gano ? Alert.AlertType.INFORMATION : Alert.AlertType.WARNING);
        alerta.setTitle(titulo);
        alerta.setHeaderText(titulo);
        alerta.setContentText(mensaje + "\n\n¿Deseas jugar de nuevo?");

        ButtonType si = new ButtonType("Sí");
        ButtonType no = new ButtonType("No");
        alerta.getButtonTypes().setAll(si, no);

        alerta.showAndWait().ifPresent(respuesta -> {
            if (respuesta == si) nuevoJuego();
        });
    }
}