import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private class Node {
        private Node prev, next;
        private Item value;
    }

    private Node head, tail;
    private int count;

    public Deque() {
        count = 0;
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public int size() {
        return count;
    }

    private void testNull(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }
    }

    private void testEmpty() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
    }

    private void oneAdd(Item item) {
        Node node = new Node();
        node.value = item;
        head = node;
        tail = node;
    }

    private Item oneRemove() {
        Item ret = head.value;
        head = null;
        tail = null;
        return ret;
    }

    public void addFirst(Item item) {
        testNull(item);
        count++;
        if (size() == 1) {
            oneAdd(item);
        } else {
            Node itemNode = new Node();
            itemNode.value = item;
            head.prev = itemNode;
            itemNode.next = head;
            head = itemNode;
        }

    }

    public void addLast(Item item) {
        testNull(item);
        count++;
        if (size() == 1) {
            oneAdd(item);
        } else {
            Node itemNode = new Node();
            itemNode.value = item;
            itemNode.prev = tail;
            tail.next = itemNode;
            tail = itemNode;
        }

    }

    public Item removeFirst() {
        testEmpty();
        count--;
        if (size() == 0) {
            return oneRemove();
        } else {
            Node itemNode = head;
            head = head.next;
            head.prev = null;
            return itemNode.value;
        }

    }

    public Item removeLast() {
        testEmpty();
        count--;
        if (size() == 0) {
            return oneRemove();
        } else {
            Node itemNode = tail;
            tail = tail.prev;
            tail.next = null;
            return itemNode.value;
        }

    }

    public Iterator<Item> iterator() {
        return new Iterator<Item>() {
            private Node current = head;

            public boolean hasNext() {
                return current != null;
            }

            public Item next() {
                if (current == null) {
                    throw new NoSuchElementException();
                }
                Item ret = current.value;
                current = current.next;
                return ret;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }


    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<Integer>();

        deque.addFirst(1); // [1]
        deque.addFirst(3); // [3,1]
        deque.addFirst(5); // [5,3,1]
        deque.addLast(2); // [5,3,1,2]
        deque.addLast(4); // [5,3,1,2,4]
        deque.addLast(6); // [5,3,1,2,4,6]
        deque.removeFirst(); // [3,1,2,4,6]
        deque.removeLast(); // [3,1,2,4]
        for (int i : deque) {
            StdOut.println(i);
        }
    }
}
