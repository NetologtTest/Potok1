import java.util.*;
import java.util.concurrent.*;

public class Main {

    static ExecutorService threadPool = Executors.newFixedThreadPool(5);
    static List<Future> list = new ArrayList<>();
    static Future<String> task;


    public static void main(String[] args) throws InterruptedException, ExecutionException {
        String[] texts = new String[25];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("aab", 30_000);
        }

        long startTs = System.currentTimeMillis(); // start time
        Callable callable = null;
        for (String text : texts) {
            callable = () -> {
                int maxSize = 0;
                for (int i = 0; i < text.length(); i++) {
                    for (int j = 0; j < text.length(); j++) {
                        if (i >= j) {
                            continue;
                        }
                        boolean bFound = false;
                        for (int k = i; k < j; k++) {
                            if (text.charAt(k) == 'b') {
                                bFound = true;
                                break;
                            }
                        }
                        if (!bFound && maxSize < j - i) {
                            maxSize = j - i;
                        }
                    }
                }
                return text.substring(0, 100) + " -> " + maxSize;
            };

            task = threadPool.submit(callable);
            list.add(task);
        }
        long endTs = System.currentTimeMillis(); // end time

        for (int i = 0; i < list.size(); i++) {
            String resultOfTask = String.valueOf(list.get(i).get());
            System.out.println(resultOfTask);
        }


        threadPool.shutdown();


        System.out.println("Time: " + (endTs - startTs) + "ms");

    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}
