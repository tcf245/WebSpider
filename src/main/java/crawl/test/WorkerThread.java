package crawl.test;

/**
 * Created by tcf24 on 2016/11/26.
 */
public class WorkerThread implements Runnable {
    private String name;
    private int i = 0;

    public WorkerThread(String name) {
        this.name = name;
        System.out.println(this.name + " has been created.");
    }

    @Override
    public void run() {
        synchronized (WorkerThread.class){
            System.out.println(this.name + " get Object lock.");
            while (i < 100) {
                System.out.println(this.name + " --> " + i++);
            }
            System.out.println(this.name + " release Object lock..");
        }
    }
}
