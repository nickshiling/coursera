import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private Node head;
    private Node tail;
    private int size;

    private class Node {
        public Item item;
        public Node next;
        public Node previous;
    }

    private class DequeIterator implements Iterator<Item> {

        private Node current = head;

        public boolean hasNext() { return current != null; }
        public void remove() { throw new java.lang.UnsupportedOperationException(); }
        public Item next() {
            if (current == null) {
                throw new java.util.NoSuchElementException();
            }
            Item item = current.item;
            current = current.next;

            return item;
        }
    }

    public Deque() {
        head = null;
        tail = null;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void addFirst(Item item) {
        if (item == null) {
            throw new java.lang.IllegalArgumentException();
        }

        Node newNode = new Node();
        newNode.item = item;
        newNode.next = head;

        if (head != null) {
            head.previous = newNode;
        } else {
            tail = newNode;
        }
        head = newNode;

        size++;
    }

    public void addLast(Item item) {
        if (item == null) {
            throw new java.lang.IllegalArgumentException();
        }

        Node newNode = new Node();
        newNode.item = item;
        newNode.previous = tail;

        if (tail != null) {
            tail.next = newNode;
        } else {
            head = newNode;
        }

        tail = newNode;

        size++;
    }

    public Item removeFirst() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }

        Node removed = head;
        if (head.next != null) {
            head = head.next;
            head.previous = null;
            if (head.next != null) head.next.previous = head;
        } else {
            head = null;
            tail = null;
        }

        size--;
        removed.previous = null;
        removed.next = null;

        return removed.item;
    }

    public Item removeLast() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }

        Node removed = tail;
        if (tail.previous != null)
        {
            tail = tail.previous;
            tail.next = null;
            if (tail.previous != null) tail.previous.next = tail;
        } else {
            tail = null;
            head = null;
        }

        size--;
        removed.previous = null;
        removed.next = null;

        return  removed.item;
    }

    public Iterator<Item> iterator() {
        return new DequeIterator();
    }
}
