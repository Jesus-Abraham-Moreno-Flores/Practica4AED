package uabc.computacion.practica4aed;

import java.util.ArrayList;

/**
 * Clase Node
 * Modela un nodo en una lista enlazada de 8 direcciones
 * Implementa Comparable para identificar concordancias entre casillas
 *
 * @author Cecilia M. Curlango Rosas
 * @version 01 2026
 */
public class Node implements Comparable<Node> {

    private int number;
    private Node up, down,
            left, right,
            downLeft, downRight,
            upLeft, upRight;

    public Node(int number) {
        this.number = number;
        up = down = left = right = null;
        downLeft = downRight = upLeft = upRight = null;
    }

    @Override
    public int compareTo(Node other) {
        if (other == null) return -1;
        return isMatchValue(other) ? 0 : -1;
    }

    // Verifica si dos nodos concuerdan en valor
    // Concuerdan si son iguales O si suman 10
    public boolean isMatchValue(Node input) {
        if (input == null) return false;
        return this.number == input.number || this.number + input.number == 10;
    }

    // Verificamos si el nodo input es vecino directo en cualquiera de las 8 direcciones
    public boolean isNeighbor(Node input) {
        if (input == null) return false;
        return input == up || input == down || input == left || input == right || input == upLeft || input == upRight ||
                input == downLeft || input == downRight;
    }

    public void delete() {

        // El izquierdo ahora apunta al derecho y viceversa
        if (left  != null) left.setRight(right);
        if (right != null) right.setLeft(left);

        //Se reconecta la cadena vertical
        if (up   != null) up.setDown(down);
        if (down != null) down.setUp(up);

        // Cada vecino diagonal deja de apuntar hacia acas
        if (up != null){
            up.setDownLeft(null);
            up.setDownRight(null);
        }
        if (down != null){
            down.setUpLeft(null);
            down.setUpRight(null);
        }
        if (left != null){
            left.setUpRight(null);
            left.setDownRight(null);
        }
        if (right != null){
            right.setUpLeft(null);
            right.setDownLeft(null);
        }
        if (upLeft != null){ upLeft.setDownRight(null); }
        if (upRight != null){ upRight.setDownLeft(null); }
        if (downLeft != null){ downLeft.setUpRight(null); }
        if (downRight != null){ downRight.setUpLeft(null); }

        up = down = left = right = null;
        downLeft = downRight = upLeft = upRight = null;
    }

    // Retorna una lista con todos los vecinos que no son nulos
    public ArrayList<Node> getNeighbors() {
        ArrayList<Node> neighbors = new ArrayList<>();

        Node[] candidates = { up, down, left, right,
                upLeft, upRight, downLeft, downRight };

        for (Node n : candidates) {
            if (n != null) neighbors.add(n);
        }

        return neighbors;
    }

    // Getters y Setters
    public int  getNumber(){ return number; }
    public void setNumber(int number){ this.number = number; }

    public Node getUp(){ return up; }
    public void setUp(Node up){ this.up = up; }

    public Node getDown(){ return down; }
    public void setDown(Node down){ this.down = down; }

    public Node getLeft(){ return left; }
    public void setLeft(Node left){ this.left = left; }

    public Node getRight(){ return right; }
    public void setRight(Node right){ this.right = right; }

    public Node getDownLeft(){ return downLeft; }
    public void setDownLeft(Node n){ this.downLeft = n; }

    public Node getDownRight(){ return downRight; }
    public void setDownRight(Node n){ this.downRight = n; }

    public Node getUpLeft(){ return upLeft; }
    public void setUpLeft(Node n){ this.upLeft = n; }

    public Node getUpRight(){ return upRight; }
    public void setUpRight(Node n){ this.upRight = n; }

    @Override
    public String toString() { return number + ""; }
}