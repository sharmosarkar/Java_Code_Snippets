public class MillionSum {
    public static void main(String[] args) throws Exception {
        WorkThread t0 = new WorkThread(0, 500000);
        WorkThread t1 = new WorkThread(500000, 1000000);

        // ^ neither worker thread running
        // v both threads may be running

        t0.start();
        t1.start();

        t0.join();
        t1.join();

        // ^ both threads may be running
        // v neither worker thread is running

        System.out.println(t0.sum + t1.sum);
    }
}

class WorkThread extends Thread {
    final long ii0;
    final long ii1;

    public long sum;

    WorkThread(long start, long end) {
        ii0 = start;
        ii1 = end;
        sum = 0;
    }

    @Override
    public void run() {
        for (long ii = ii0; ii < ii1; ++ii) {
            sum += ii;
        }
    }
}