import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class RateLimiter {
    private final int maxRequests;
    private final long timeWindowMillis;
    private long windowStartTime;
    private AtomicInteger requestCount;

    /**
     * Создает новый ограничитель скорости запросов
     *
     * @param maxRequests      максимальное количество запросов
     * @param timeWindowMillis временное окно в миллисекундах
     */
    public RateLimiter(int maxRequests, long timeWindowMillis) {
        this.maxRequests = maxRequests;
        this.timeWindowMillis = timeWindowMillis;
        this.windowStartTime = System.currentTimeMillis();
        this.requestCount = new AtomicInteger(0);
    }

    /**
     * Ожидает, если необходимо, чтобы не превышать лимит запросов
     */
    public synchronized void acquire() {
        long currentTime = System.currentTimeMillis();

        if (currentTime - windowStartTime >= timeWindowMillis) {
            // Сбрасываем счетчик и обновляем время начала окна
            requestCount.set(0);
            windowStartTime = currentTime;
        }

        if (requestCount.get() >= maxRequests) {
            // Вычисляем время ожидания до следующего окна
            long waitTime = timeWindowMillis - (currentTime - windowStartTime);
            System.out.println("Превышен лимит запросов. Ожидание " + waitTime + " миллисекунд.");

            try {
                TimeUnit.MILLISECONDS.sleep(waitTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Сбрасываем счетчик и обновляем время начала окна
            requestCount.set(0);
            windowStartTime = System.currentTimeMillis();
        }

        requestCount.incrementAndGet();
    }
}
