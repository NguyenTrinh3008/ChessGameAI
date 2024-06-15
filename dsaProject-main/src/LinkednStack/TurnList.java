/*
Name: Trinh Binh Nguyen
Purpose: Defines the TurnList class for managing a linked list of turns in a chess game.
*/

package LinkednStack;

import chess.Piece;

public class TurnList {
    private Node head;
    private Node tail;

    private class Node {
        Piece.Color turn;
        Node next;
        Node prev;

        public Node(Piece.Color turn) {
            this.turn = turn;
            next = null;
            prev = null;
        }

        public Piece.Color getTurn(){
            return turn;
        }
    }

    public void InsertFirst(Piece.Color turn) {
        Node newnode = new Node(turn);
        if (head == null) {
            head = newnode;
            tail = newnode;
        } else {
            newnode.next = head;
            head = newnode;
            newnode.prev = head;
        }
    }

    public void RemoveFirst() {
        if(isEmpty()){
            return;
        }
        if (head != null) {
            Node current = head;
            head = current.next;
        }
    }

    public Piece.Color getTurn() {
        return head.turn;
    }


    public boolean isEmpty() {
        return head == null;
    }
}
