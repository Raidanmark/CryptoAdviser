package Bot.SMA_Method;

import java.util.List;

public class MethodConsoleOutput {

    public void TopCoinListConsoleOutput(List<String> topCoinsData) {
        // Print the list of top 100 cryptocurrencies to the console
        System.out.println("Top 100 cryptocurrencies by market capitalization:");
        for (String str: topCoinsData) {
            System.out.println("- (" + str + ")");
        }
    }
}
