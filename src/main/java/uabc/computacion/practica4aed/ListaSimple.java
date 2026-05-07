package uabc.computacion.practica4aed;

public class ListaSimple {

    //Nodo interno de la lista
    public static class NodoLista {
        Node info;
        NodoLista siguiente;

        NodoLista(Node info) {
            this.info      = info;
            this.siguiente = null;
        }
    }

    private NodoLista inicio;

    public ListaSimple() {
        inicio = null;
    }

    public void insertarInicio(Node dato) {
        NodoLista nodo = new NodoLista(dato);
        nodo.siguiente = inicio;
        inicio         = nodo;
    }

    public void insertarFinal(Node dato) {
        NodoLista nodo = new NodoLista(dato);
        if (inicio == null) {
            inicio = nodo;
        } else {
            NodoLista r = inicio;
            while (r.siguiente != null) {
                r = r.siguiente;
            }
            r.siguiente = nodo;
        }
    }

    public Node eliminarInicio() {
        if (inicio == null) {
            System.out.println("Lista vacía");
            return null;
        }
        Node dato = inicio.info;
        inicio    = inicio.siguiente;
        return dato;
    }

    public Node eliminarFinal() {
        if (inicio == null) {
            System.out.println("Lista vacía");
            return null;
        }
        if (inicio.siguiente == null) {
            Node dato = inicio.info;
            inicio    = null;
            return dato;
        }
        NodoLista anterior = inicio;
        NodoLista actual   = inicio.siguiente;
        while (actual.siguiente != null) {
            anterior = actual;
            actual   = actual.siguiente;
        }
        Node dato          = actual.info;
        anterior.siguiente = null;
        return dato;
    }

    public boolean eliminarDato(Node dato) {
        if (inicio == null) return false;

        // Esta en el inicio de la lista
        if (inicio.info == dato) {
            inicio = inicio.siguiente;
            return true;
        }

        // En caso de que este en el medio o a final de la lista
        NodoLista anterior = inicio;
        NodoLista actual = inicio.siguiente;
        while (actual != null) {
            if (actual.info == dato) {
                anterior.siguiente = actual.siguiente;
                return true;
            }
            anterior = actual;
            actual = actual.siguiente;
        }

        return false;
    }

    // Retorna el primer Nodo de la lista y recorre la lista en la clase GameBoard
    public Node obtenerInicio() {
        return inicio == null ? null : inicio.info;
    }

    // Retorna el NodoLista de inicio para que podamos avanzar con .siguiente
    public NodoLista obtenerNodoInicio() {
        return inicio;
    }

    public boolean estaVacia() {
        return inicio == null;
    }

    public int tamanio() {
        int count = 0;
        NodoLista r = inicio;
        while (r != null) {
            count++;
            r = r.siguiente;
        }
        return count;
    }

    public String mostrarLista() {
        StringBuilder cadena = new StringBuilder();
        NodoLista r = inicio;
        while (r != null) {
            cadena.append(r.info).append(" ");
            r = r.siguiente;
        }
        return cadena.length() == 0 ? "Lista vacía" : cadena.toString();
    }
}