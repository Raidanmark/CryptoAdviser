import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class RateLimiter {
    private final int maxRequests; // Maximum number of requests allowed in the time window
    private final long timeWindowMillis; // Time window in milliseconds for the rate limit
    private long windowStartTime; // Start time of the current time window
    private AtomicInteger requestCount; // Counter for the number of requests made in the current time window

    // Creates a new rate limiter for requests
    public RateLimiter(int maxRequests, long timeWindowMillis) {
        this.maxRequests = maxRequests; // Set the maximum number of requests
        this.timeWindowMillis = timeWindowMillis; // Set the time window duration
        this.windowStartTime = System.currentTimeMillis(); // Initialize the start time of the time window
        this.requestCount = new AtomicInteger(0); // Initialize the request counter
    }

    //Waits if necessary to avoid exceeding the request limit
    public synchronized void acquire() {
        long currentTime = System.currentTimeMillis(); // Get the current time

        if (currentTime - windowStartTime >= timeWindowMillis) {
            // Reset the counter and update the window start time if the time window has passed
            requestCount.set(0);
            windowStartTime = currentTime; // Update the start time for the new window
        }

        if (requestCount.get() >= maxRequests) {
            // Calculate the wait time until the next window starts
            long waitTime = timeWindowMillis - (currentTime - windowStartTime);
            System.out.println("Превышен лимит запросов. Ожидание " + waitTime + " миллисекунд.");

            try {
                TimeUnit.MILLISECONDS.sleep(waitTime); // Wait for the calculated time
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore the interrupted status
            }

            // Reset the counter and update the window start time after waiting
            requestCount.set(0); // Reset the request counter
            windowStartTime = System.currentTimeMillis(); // Update the start time for the new window
        }

        requestCount.incrementAndGet(); // Increment the request counter
    }
}
