import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] data;
    private int size;

    private class RandomizeIterator implements Iterator<Item> {
        private final int[] iterationOrder;
        private int currentIndex = 0;

        public RandomizeIterator() {
            iterationOrder = new int[size];

            for (int i = 0; i < size; i++) {
                iterationOrder[i] = i;
            }

            StdRandom.shuffle(iterationOrder);
        }

        public boolean hasNext() { return currentIndex < size;  }
        public void remove() { throw new java.lang.UnsupportedOperationException(); }
        public Item next() {
            if (!hasNext()) { throw new java.util.NoSuchElementException(); }

            return data[iterationOrder[currentIndex++]];
        }
    }

    public RandomizedQueue() {
        data = (Item[]) new Object[1];
        size = 0;
    }

    public boolean isEmpty() { return size == 0; }

    public int size() { return size; }

    public void enqueue(Item item) {
        if (item == null) { throw new java.lang.IllegalArgumentException(); }

        expandSize();

        data[size++] = item;
    }

    private void expandSize() {
        if (size >= data.length) {
            adjustSize(size * 2);
        }
    }

    private void shrinkSize() {
        int newSize = data.length / 4;
        if (newSize <= 0) newSize = 1;

        if (size <= newSize) {
            adjustSize(newSize);
        }
    }

    private void adjustSize(int newSize) {
        Item[] newData = (Item[]) new Object[newSize];

        for (int i = 0; i < size; i++) {
            newData[i] = data[i];
        }

        data = newData;
    }

    public Item dequeue() {
        if (isEmpty()) { throw new java.util.NoSuchElementException(); }

        int dequeIndex = StdRandom.uniform(size);

        Item dequeueItem = data[dequeIndex];
        data[dequeIndex] = data[--size];
        data[size] = null;
        shrinkSize();

        return dequeueItem;
    }

    public Item sample() {
        if (isEmpty()) { throw new java.util.NoSuchElementException(); }

        int dequeIndex = StdRandom.uniform(size);
        return data[dequeIndex];
    }

    public Iterator<Item> iterator() {
        return new RandomizeIterator();
    }
}
