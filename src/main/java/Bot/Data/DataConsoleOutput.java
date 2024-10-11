package Bot.Data;

import Bot.SMA_Method.SMA_Analysis;

import java.util.List;


public class DataConsoleOutput {

    public void TopCoinListConsoleOutput(List<String> topCoinsData, Integer TopCoinsListLang) {
        // Print the list of top  cryptocurrencies to the console
        System.out.println("Top "+ TopCoinsListLang +" cryptocurrencies by market capitalization:");
        for (String str: topCoinsData) {
            System.out.println("- (" + str + ")");
        }
    }
}

    /*
public class ConsoleOutput {

    // Create instances of API client and analyzer
    CoinGeckoApiClient apiClient = new CoinGeckoApiClient();
    SMA_Analysis analyzer = new SMA_Analysis();




    * //Console output
                // Step 2: Print the list of top 100 cryptocurrencies to the console
                System.out.println("Top 100 cryptocurrencies by market capitalization:");
                for (Map<String, String> coinData : topCoinsData) {
                    String name = coinData.get("name");
                    String id = coinData.get("id");
                    System.out.println("- " + name + " (" + id + ")");
                }
                * */

    //Console output
    // Print a separator
     //               System.out.println("\nPurchase recommendations based on \"golden cross\"analysis:");

    /*
    *  //Console output Analyzing cryptocurrency
                    System.out.println("\nAnalyzing cryptocurrency " + currentCoinIndex + " из " + totalCoins + ": " + coinName + " (" + coinId + ")");
    *

}*/

