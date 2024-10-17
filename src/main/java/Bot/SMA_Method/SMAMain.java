package Bot.SMA_Method;

import Bot.Data.DataConsoleOutput;
import Bot.Data.DataFetcher;
import java.util.List;
import Bot.Data.PriceData;

public class SMAMain {

    //Amount of top coins
    private final Integer TopCoinsListLang = 1;
    //Amount of days logs
    private final Integer DaysAmount = 50;

    public String SMAmain(){
        //Initialization string of coins for recommendation
        String SMARecommendation = "";
        //Initialization list of coins' closing prices
        List<List<PriceData>> closingPrices;

        //Initialization DataFetcher class
        DataFetcher dataFetcher = new DataFetcher();
        //Initialization ConsoleOutput class
        DataConsoleOutput consoleOutput = new DataConsoleOutput();

        //Call top coins list data
        List<String> topCoinList = dataFetcher.TopCrypto(TopCoinsListLang);

        //Call coins list data Console output
        consoleOutput.TopCoinListConsoleOutput(topCoinList,TopCoinsListLang);

        //Call closing price data
        closingPrices = dataFetcher.GetClosingPrice(topCoinList, TopCoinsListLang, DaysAmount);

        //Call Data analysis
        SMA_Analysis analyzer = new SMA_Analysis();
        SMARecommendation = analyzer.analyze(topCoinList, closingPrices);

        return SMARecommendation;
    }

}
