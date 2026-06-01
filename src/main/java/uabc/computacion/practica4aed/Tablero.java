package uabc.computacion.practica4aed;
import java.util.Random;

public class Tablero {

    private int filas;
    private int columnas;
    private int sumaPares;
    private ListaSimple lista;
    private Pila<Movimiento> historial;
    private int concordanciasEncontradas;
    private int concordanciasPendientes;

    public Tablero(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        this.sumaPares = 0;
        this.lista = new ListaSimple();
        this.historial = new Pila<>(filas * columnas);
        this.concordanciasEncontradas = 0;
        this.concordanciasPendientes = 0;
        iniciarTablero();
    }

    // Creamos los nodos para insertalos en la lista y conectar sus punteros
    private void iniciarTablero() {
        int total = filas * columnas;
        int[] numeros = new int[total];

        // Llenamos el arreglo primero de forma secuencial
        for (int i = 0; i < total; i++) {
            numeros[i] = (i % 9) + 1;
        }

        // Mezclamos los numeros de manera aleatoria
        Random random = new Random();
        for (int i = total - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = numeros[i];
            numeros[i] = numeros[j];
            numeros[j] = temp;
        }

        // Insertamos los nodos en la lista con los numeros ya mezclados
        int indice = 0;
        for (int r = 0; r < filas; r++) {
            for (int c = 0; c < columnas; c++) {
                Node nodo = new Node(numeros[indice], r, c);
                lista.insertarFinal(nodo);
                indice++;
            }
        }

        conectarNodos();
        concordanciasPendientes = contarConcordancias();
    }

    // Aqui recorremos la lista y asignamos los 8 punteros a cada nodo
    public void conectarNodos() {
        ListaSimple.NodoLista r = lista.obtenerNodoInicio();
        while (r != null) {
            Node nodo = r.info;
            nodo.setLeft(buscarNodo(nodo.getRow(), nodo.getCol() - 1));
            nodo.setRight(buscarNodo(nodo.getRow(), nodo.getCol() + 1));

            nodo.setUp(buscarDiagonal(nodo.getRow(), nodo.getCol(), -1, 0));
            nodo.setDown(buscarDiagonal(nodo.getRow(), nodo.getCol(), 1, 0));
            nodo.setUpLeft(buscarDiagonal(nodo.getRow(), nodo.getCol(), -1, -1));
            nodo.setUpRight(buscarDiagonal(nodo.getRow(), nodo.getCol(),  -1, 1));
            nodo.setDownLeft(buscarDiagonal(nodo.getRow(), nodo.getCol(), 1, -1));
            nodo.setDownRight(buscarDiagonal(nodo.getRow(), nodo.getCol(), 1, 1));
            r = r.siguiente;
        }
    }

    // Busca un nodo en la lista por fila y columna
    public Node buscarNodo(int row, int col) {
        ListaSimple.NodoLista r = lista.obtenerNodoInicio();
        while (r != null) {
            if (r.info.getRow() == row && r.info.getCol() == col) {
                return r.info;
            }
            r = r.siguiente;
        }
        return null;
    }

    public Node buscarDiagonal(int row, int col, int deltaRow, int deltaCol){
        int fila = row + deltaRow;
        int columna = col + deltaCol;

        while(fila >=0 && fila < filas && columna >=0 && columna < columnas){
            Node nodoEncontrado =  buscarNodo(fila, columna);
            if(nodoEncontrado != null){
                return nodoEncontrado;
            }
            fila += deltaRow;
            columna += deltaCol;
        }
        return null;
    }

    // Aqui probamos hacer el match entre dos numeros seleccionados, regresa true si fue el caso
    public boolean intentarMatch(Node a, Node b) {
        if (a == null || b == null) return false;
        if (a == b) return false;
        if (a.compareTo(b) != 0) return false;
        if (!sonVecinos(a, b)) return false;

        historial.push(new Movimiento(a, b));

        lista.eliminarDato(a);
        lista.eliminarDato(b);
        a.delete();
        b.delete();

        concordanciasEncontradas++;
        sumaPares += a.getNumber() + b.getNumber();
        concordanciasPendientes = contarConcordancias();

         conectarNodos();

        return true;
    }

    // Verificamos si son vecinos directos a las 8 direcciones o vecinos lineales si es el siguiente que esta activo
    public boolean sonVecinos(Node a, Node b) {
        if (a.isNeighbor(b)) return true;
        return vecinoLineal(a, b) || vecinoLineal(b, a);
    }

    // Verifica si b es el siguiente nodo activo después de a en la lista
    // Esto cubre el caso del final de renglón con inicio del siguiente
    public boolean vecinoLineal(Node desde, Node hasta) {
        ListaSimple.NodoLista r = lista.obtenerNodoInicio();

        // Avanzar hasta encontrar 'desde'
        while (r != null && r.info != desde) {
            r = r.siguiente;
        }

        // Si no lo encontramos, no son vecinos lineales
        if (r == null) return false;

        // El siguiente en la lista debe ser 'hasta'
        r = r.siguiente;
        return r != null && r.info == hasta;
    }

    // Deshacemos el ultimo movimiento reinsertando los nodos y volviendolos a poner en la lista
    public void deshacerMovimiento() {
        if (historial.pilaVacia()) return;

        Movimiento m = historial.pop();

        lista.insertarFinal(m.nodoA);
        lista.insertarFinal(m.nodoB);

        reconstruirLista();

        concordanciasEncontradas--;
        sumaPares -= m.nodoA.getNumber() + m.nodoB.getNumber();
        concordanciasPendientes = contarConcordancias();
    }

    // Reordena la lista por fila y columna luego de presionar deshacer
    public void reconstruirLista() {
        // Sacamos todos los nodos de la lista actual
        int total = lista.tamanio();
        Node[] nodos = new Node[total];
        for (int i = 0; i < total; i++) {
            nodos[i] = lista.eliminarInicio();
        }

        // Ordenamos por fila y columna
        for (int i = 1; i < total; i++) {
            Node clave = nodos[i];
            int j = i - 1;
            while (j >= 0 && (nodos[j].getRow() > clave.getRow() ||
                    (nodos[j].getRow() == clave.getRow() && nodos[j].getCol() > clave.getCol()))) {
                nodos[j + 1] = nodos[j];
                j--;
            }
            nodos[j + 1] = clave;
        }

        // Reinsertamos en el orden correcto
        for (Node n : nodos) {
            lista.insertarFinal(n);
        }

        // Reconectamos todos los punteros
        conectarNodos();
    }

    // Buscamos la primera pareja que sea valida en la lista para dar la pista
    public Node[] obtenerPista() {
        ListaSimple.NodoLista r = lista.obtenerNodoInicio();
        while (r != null) {
            Node a = r.info;
            ListaSimple.NodoLista s = r.siguiente;
            while (s != null) {
                Node b = s.info;
                if (a.compareTo(b) == 0 && sonVecinos(a, b)) {
                    return new Node[]{a, b};
                }
                s = s.siguiente;
            }
            r = r.siguiente;
        }
        return null;
    }

    // Aqui contamos cuantas concordancias validas existen aun
    public int contarConcordancias() {
        int count = 0;
        ListaSimple.NodoLista r = lista.obtenerNodoInicio();
        while (r != null) {
            Node a = r.info;
            ListaSimple.NodoLista s = r.siguiente;
            while (s != null) {
                Node b = s.info;
                if (a.compareTo(b) == 0 && sonVecinos(a, b)) {
                    count++;
                }
                s = s.siguiente;
            }
            r = r.siguiente;
        }
        return count;
    }

    public boolean hayMovimientos() {
        return obtenerPista() != null;
    }

    public boolean juegoTerminado() {
        return lista.estaVacia();
    }

    // Getters
    public ListaSimple getLista() { return lista; }
    public int getFilas() { return filas; }
    public int getColumnas() { return columnas; }
    public int getSumaPares() { return sumaPares; }
    public int getConcordanciasEncontradas() { return concordanciasEncontradas; }
    public int getConcordanciasPendientes() { return concordanciasPendientes; }
    public boolean puedeDeshacer() { return !historial.pilaVacia(); }
}