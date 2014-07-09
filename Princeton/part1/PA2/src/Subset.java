public class Subset {
    public static void main(String[] args) {

        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<String>();

        for (int i = 0; i < k; i++) {
            randomizedQueue.enqueue(StdIn.readString());
        }
        int seqno = k;
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            seqno++;
            int r = StdRandom.uniform(seqno);
            if (r < k) {
                randomizedQueue.dequeue();
                randomizedQueue.enqueue(s);
            }
        }

        for (int i = 0; i < k; i++) {
            StdOut.println(randomizedQueue.dequeue());
        }
    }
}

