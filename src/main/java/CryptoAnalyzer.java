import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CryptoAnalyzer {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Date format for output

    //Analyzes the cryptocurrency for a "golden cross"
    public String analyze(String coinId, List<PriceData> priceDataList) {
        int shortPeriod = 20; // Short-term SMA (20 days)
        int longPeriod = 50; // Long-term SMA (50 days)

        System.out.println("\nStarting analysis for  " + coinId);

        // Check if there is enough data to calculate the long-term SMA
        if (priceDataList.size() < longPeriod) {
            System.out.println("Insufficient data for analysis of " + coinId + ". At least " + longPeriod + " days of data are required.");
            return "Insufficient data for analysis of " + coinId + ".";
        }

        List<Double> shortSMAList = new ArrayList<>(); // List to store short-term SMA values
        List<Double> longSMAList = new ArrayList<>();  // List to store long-term SMA values

        // Calculate moving averages for each period
        for (int i = longPeriod - 1; i < priceDataList.size(); i++) {
            // Get prices for short-term and long-term SMA
            List<Double> shortClosingPrices = new ArrayList<>();  // List for short-term closing prices
            List<String> shortDates = new ArrayList<>(); // List for short-term dates
            for (int j = i - shortPeriod + 1; j <= i; j++) {
                shortClosingPrices.add(priceDataList.get(j).getPrice()); // Add closing price to the list
                shortDates.add(dateFormat.format(priceDataList.get(j).getDate())); // Add formatted date to the list
            }

            List<Double> longClosingPrices = new ArrayList<>(); // List for long-term closing prices
            List<String> longDates = new ArrayList<>(); // List for long-term dates
            for (int j = i - longPeriod + 1; j <= i; j++) {
                longClosingPrices.add(priceDataList.get(j).getPrice()); // Add closing price to the list
                longDates.add(dateFormat.format(priceDataList.get(j).getDate())); // Add formatted date to the list
            }

            // Calculate SMA
            double shortSMA = calculateSMA(shortClosingPrices); // Calculate short-term SMA
            double longSMA = calculateSMA(longClosingPrices); // Calculate long-term SMA

            shortSMAList.add(shortSMA); // Add short-term SMA to the list
            longSMAList.add(longSMA); // Add long-term SMA to the list

            // Print intermediate calculations
            System.out.println("\nDate: " + dateFormat.format(priceDataList.get(i).getDate()));
            System.out.println("Short-term prices (" + shortPeriod + " days): " + shortClosingPrices);
            System.out.println("Short-term SMA: " + shortSMA);

            System.out.println("Long-term prices (" + longPeriod + " days): " + longClosingPrices);
            System.out.println("Long-term SMA: " + longSMA);
        }

        // Check for the presence of a "golden cross"
        boolean goldenCross = isGoldenCross(shortSMAList, longSMAList);

        if (goldenCross) {
            System.out.println("\nA \"golden cross\" has been detected for" + coinId + "!");
            return "Recommended to buy " + coinId + ". A moving average crossover (\"golden cross\") was detected.";
        } else {
            System.out.println("\nNo \"golden cross\" detected for " + coinId + ".");
            return "No buy signal for " + coinId + ".";
        }
    }

    // Calculates the simple moving average (SMA)
    private double calculateSMA(List<Double> prices) {
        double sum = 0.0; // Variable to hold the sum of prices
        for (Double price : prices) {
            sum += price; // Accumulate the sum of prices
        }
        return sum / prices.size();  // Return the average
    }

    //Checks for the presence of a "golden cross" based on short-term and long-term SMA
    private boolean isGoldenCross(List<Double> shortSMAList, List<Double> longSMAList) {
        int size = shortSMAList.size();  // Get the size of the short SMA list
        if (size < 2) return false; // Not enough data to determine a golden cross

        // Previous SMA values
        double prevShortSMA = shortSMAList.get(size - 2); // Previous short-term SMA
        double prevLongSMA = longSMAList.get(size - 2); // Previous long-term SMA

        // Current SMA values
        double currShortSMA = shortSMAList.get(size - 1); // Current short-term SMA
        double currLongSMA = longSMAList.get(size - 1); // Current long-term SMA

        System.out.println("\nChecking for a \"golden cross\":");
        System.out.println("Previous short-term SMA: " + prevShortSMA);
        System.out.println("Previous long-term SMA: " + prevLongSMA);
        System.out.println("Current short-term SMA: " + currShortSMA);
        System.out.println("Current long-term SMA: " + currLongSMA);

        // Check for "golden cross" conditions
        boolean goldenCross = (prevShortSMA <= prevLongSMA) && (currShortSMA > currLongSMA);

        System.out.println("Golden cross condition met: " + goldenCross);

        return goldenCross; // Return the result of the golden cross check
    }
}

