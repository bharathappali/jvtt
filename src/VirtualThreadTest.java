import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Goal of the VirtualThreadTest (VTT) is to compare Java Virtual Threads and Java Native Threads in aspect of
 * the time taken to invoke, run and complete a Sample Job (Which sleeps for a second)
 *
 * We use two kinds of executor services:
 *  - Native Fixed thread pool service (Executors.newFixedThreadPool)
 *  - Virtual Thread executor service (Executors.newVirtualThreadPerTaskExecutor)
 *
 *  We select the thread pool based on env var "USE_VIRTUAL".
 *
 *  We are not switching the thread pool in the same run as JIT might have made the application hot and results might
 *  be favourable for the later executor we run
 *
 *  We use the "--enable-preview" option as virtual threads are part of preview release of JDK 19
 */
public class VirtualThreadTest {
    public static void main(String[] args) {
        // Env var to check
        final String useVirtual = "USE_VIRTUAL";

        // Setting isVirtual as false to use native threads by default
        boolean isVirtual = false;

        // Number of Threads and Jobs
        int numNativeThreads = 100;
        int numJobs = 10000;

        String envCheck = System.getenv(useVirtual);
        if (null != envCheck && envCheck.equalsIgnoreCase("true")) {
            isVirtual = true;
        }

        ExecutorService executorService = null;

        System.out.println("Is Virtual threads used : " + isVirtual);
        if (isVirtual) {
            /**
             * Initialising the executor service with a virtual thread executor
             * We don't create a thread pool here but create a new virtual thread for each job
             * Number of virtual threads created will be equal to num jobs
             */
            executorService = Executors.newVirtualThreadPerTaskExecutor();
        } else {
            /**
             * Initialising the executor service with fixed number of threads
             */
            executorService = Executors.newFixedThreadPool(numNativeThreads);
            System.out.println("Native threads count : " + numNativeThreads);
        }

        List<VTTJob> vttJobs = new ArrayList<VTTJob>(numJobs);

        // Creating Virtual Thread Test Jobs
        System.out.println("Creating " + numJobs + " Jobs");
        for (int i = 0; i < numJobs; i++) {
            vttJobs.add(new VTTJob());
        }

        // Recording the time stamp at the start of invoking the jobs
        long timeStart = System.currentTimeMillis();

        // Starting the jobs
        List<Future<String>> futures = null;
        try {
            System.out.print("Running Jobs ");
            futures = executorService.invokeAll(vttJobs);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Wait n check if all jobs are done
        boolean isDone = false;
        try {
        for (Future<String> future: futures)
            future.get();

        for (Future<String> future: futures)
            isDone = future.isDone();
        System.out.println(" Done.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        long timeEnd = System.currentTimeMillis();
        long timeDiff = timeEnd - timeStart;
        System.out.println("Time taken - " + timeDiff + " ms");
        // Shutting down the executor service
        executorService.shutdown();
    }
}
