package Bot.SMA_Method;

import Bot.Data.PriceData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SMA_Analysis {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Date format for output

    // Analyzes the cryptocurrency for a "golden cross"
    public String analyze(List<String> coinIds, List<List<PriceData>> priceDataList) {  // Изменено: теперь принимается двумерный список
        int shortPeriod = 20; // Short-term SMA (20 days)
        int longPeriod = 50; // Long-term SMA (50 days)

        StringBuilder analysisResult = new StringBuilder();  // Добавлено: использовать StringBuilder для результатов анализа

        // Iterate over each cryptocurrency
        for (int coinIndex = 0; coinIndex < coinIds.size(); coinIndex++) {  // Добавлено: цикл для каждого идентификатора монеты
            String coinId = coinIds.get(coinIndex);
            List<PriceData> coinPrices = priceDataList.get(coinIndex);  // Получаем данные для конкретной криптовалюты

            System.out.println("\nStarting analysis for " + coinId);

            // Check if there is enough data to calculate the long-term SMA
            if (coinPrices.size() < longPeriod) {
                System.out.println("Insufficient data for analysis of " + coinId + ". At least " + longPeriod + " days of data are required.");
                analysisResult.append("Insufficient data for analysis of ").append(coinId).append(".\n");
                continue;  // Переходим к следующей монете, если недостаточно данных
            }

            List<Double> shortSMAList = new ArrayList<>(); // List to store short-term SMA values
            List<Double> longSMAList = new ArrayList<>();  // List to store long-term SMA values

            // Calculate moving averages for each period
            for (int i = longPeriod - 1; i < coinPrices.size(); i++) {
                // Get prices for short-term and long-term SMA
                List<Double> shortClosingPrices = new ArrayList<>();  // List for short-term closing prices
                for (int j = i - shortPeriod + 1; j <= i; j++) {
                    shortClosingPrices.add(coinPrices.get(j).getPrice()); // Получаем цену для каждого дня из списка
                }

                List<Double> longClosingPrices = new ArrayList<>(); // List for long-term closing prices
                for (int j = i - longPeriod + 1; j <= i; j++) {
                    longClosingPrices.add(coinPrices.get(j).getPrice()); // Получаем цену для каждого дня из списка
                }

                // Calculate SMA
                double shortSMA = calculateSMA(shortClosingPrices); // Calculate short-term SMA
                double longSMA = calculateSMA(longClosingPrices); // Calculate long-term SMA

                shortSMAList.add(shortSMA); // Add short-term SMA to the list
                longSMAList.add(longSMA); // Add long-term SMA to the list

                // Print intermediate calculations
                System.out.println("\nDate: " + dateFormat.format(coinPrices.get(i).getDate()));
                System.out.println("Short-term prices (" + shortPeriod + " days): " + shortClosingPrices);
                System.out.println("Short-term SMA: " + shortSMA);
                System.out.println("Long-term prices (" + longPeriod + " days): " + longClosingPrices);
                System.out.println("Long-term SMA: " + longSMA);
            }

            // Check for the presence of a "golden cross"
            boolean goldenCross = isGoldenCross(shortSMAList, longSMAList);

            if (goldenCross) {
                System.out.println("\nA \"golden cross\" has been detected for " + coinId + "!");
                analysisResult.append("Recommended to buy ").append(coinId).append(". A moving average crossover (\"golden cross\") was detected.\n");
            } else {
                System.out.println("\nNo \"golden cross\" detected for " + coinId + ".");
                analysisResult.append("No buy signal for ").append(coinId).append(".\n");
            }
        }

        return analysisResult.toString();  // Возвращаем собранные результаты анализа
    }

    // Calculates the simple moving average (SMA)
    private double calculateSMA(List<Double> prices) {
        double sum = 0.0; // Variable to hold the sum of prices
        for (Double price : prices) {
            sum += price; // Accumulate the sum of prices
        }
        return sum / prices.size();  // Return the average
    }

    // Checks for the presence of a "golden cross" based on short-term and long-term SMA
    private boolean isGoldenCross(List<Double> shortSMAList, List<Double> longSMAList) {
        int size = shortSMAList.size();  // Get the size of the short SMA list
        if (size < 2) return false; // Not enough data to determine a golden cross

        double prevShortSMA = shortSMAList.get(size - 2); // Previous short-term SMA
        double prevLongSMA = longSMAList.get(size - 2); // Previous long-term SMA

        double currShortSMA = shortSMAList.get(size - 1); // Current short-term SMA
        double currLongSMA = longSMAList.get(size - 1); // Current long-term SMA

        return (prevShortSMA <= prevLongSMA) && (currShortSMA > currLongSMA);
    }
}

