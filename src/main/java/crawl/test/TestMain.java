package crawl.test;


/**
 * Created by tcf24 on 2016/11/26.
 */
public class TestMain {
    public static void main(String[] args) {

//        Runnable demo = new WorkerThread("Worker");
//        for (int i = 0; i < 10; i++) {
//            Thread t = new Thread(demo,"Worker-" + i);
//            t.start();
//        }

        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(new WorkerThread("Worker-" + i));
//            t.start();
        }
    }
}
