package Bot.Data;

import Bot.SMA_Method.SMA_Analysis;

import java.util.List;


public class DataConsoleOutput {

    public void TopCoinListConsoleOutput(List<String> topCoinsData, Integer TopCoinsListLang) {
        // Print the list of top  cryptocurrencies to the console
        System.out.println("Top " + TopCoinsListLang + " cryptocurrencies by market capitalization:");
        for (String str : topCoinsData) {
            System.out.println("- (" + str + ")");
        }
    }
}




