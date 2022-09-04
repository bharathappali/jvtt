import java.util.UUID;
import java.util.concurrent.Callable;

public class VTTJob implements Callable<String> {
    @Override
    public String call() throws Exception {
        try {
            // Sleeping for a minute
            Thread.sleep(1000);
            System.out.print(".");
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
            return null;
        }
        return UUID.randomUUID().toString();
    }
}
