import java.util.Iterator;
import java.util.NoSuchElementException;


public class RandomizedQueue<Item> implements Iterable<Item> {

    private int count, capacity;
    private Item[] que;

    public RandomizedQueue() {
        count = 0;
        capacity = 2;
        que = (Item[]) new Object[capacity];
    }


    private void resize(int targetCapacity) {
        Item[] newque = (Item[]) new Object[targetCapacity];
        for (int i = 0; i < count; i++) {
            newque[i] = que[i];
        }
        que = newque;
        capacity = targetCapacity;
    }

    private void maintainUp() {
        if (count > capacity - 1) {
            resize(capacity << 1);
        }
    }

    private void maintainDown() {
        if (count < capacity / 4) {
            resize(capacity >> 1);
        }
    }

    private void swap(int i, int j) {
        Item tmp = que[i];
        que[i] = que[j];
        que[j] = tmp;
    }

    private void scramble() {
        int r = StdRandom.uniform(count);
        swap(r, count - 1);
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public int size() {
        return count;
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }
        que[count++] = item;
        maintainUp();
        scramble();
    }

    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item ret = que[--count];
        que[count] = null;
        maintainDown();
        return ret;
    }

    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        scramble();
        return que[count - 1];
    }

    public Iterator<Item> iterator() {

        class RandomIterator implements Iterator<Item> {

            private int[] order;
            private int current;

            RandomIterator() {
                current = 0;
                order = new int[count];
                for (int i = 0; i < count; i++) {
                    order[i] = i;
                    int r = StdRandom.uniform(i + 1);
                    int t = order[r];
                    order[r] = order[i];
                    order[i] = t;
                }
            }

            public boolean hasNext() {
                return current < count;
            }

            public Item next() {
                if (current >= count) {
                    throw new NoSuchElementException();
                }
                return que[order[current++]];
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        }
        return new RandomIterator();
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> randomQueue = new RandomizedQueue<Integer>();

        for (int i = 1; i < 100; i++) {
            randomQueue.enqueue(i);
        }
        for (int i = 1; i < 100; i++) {
            StdOut.println(randomQueue.dequeue());
        }

    }
}
