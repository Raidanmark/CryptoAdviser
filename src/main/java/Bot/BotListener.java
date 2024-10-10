package Bot;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

import java.io.IOException;
//import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class BotListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        // Check if the message author is a bot
        if (event.getAuthor().isBot()) {
            return; // Ignore messages from other bots
        }

        // Get the content of the received message
        String messageContent = event.getMessage().getContentRaw();
        MessageChannelUnion channel = event.getChannel();


        // Check if the message content is the command "!recommend"
        if (messageContent.equalsIgnoreCase("!recommend")) {
            // Send a message to the channel indicating that analysis is in progress
            channel.sendMessage("Please wait, analyzing top cryptocurrencies. This may take a few minutes...").queue();

            // Create instances of API client and analyzer
            CoinGeckoApiClient apiClient = new CoinGeckoApiClient();
            SMA_Analysis analyzer = new SMA_Analysis();
            //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            // Create a new thread to perform the analysis
            new Thread(() -> {
                try {
                    // Step 1: Get the IDs of the top 100 cryptocurrencies
                    List<Map<String, String>> topCoinsData = apiClient.getTopCoinsData(100);

                    // Step 2: Print the list of top 50 cryptocurrencies to the console
                    System.out.println("Top 100 cryptocurrencies by market capitalization:");
                    for (Map<String, String> coinData : topCoinsData) {
                        String name = coinData.get("name");
                        String id = coinData.get("id");
                        System.out.println("- " + name + " (" + id + ")");
                    }

                    // Print a separator
                    System.out.println("\nPurchase recommendations based on \"golden cross\"analysis:");

                    int totalCoins = topCoinsData.size(); // Get the total number of coins
                    int currentCoinIndex = 1; // Initialize the current coin index


                    // For each cryptocurrency, get the closing price history and perform analysis
                    for (Map<String, String> coinData : topCoinsData) {
                        String coinName = coinData.get("name");
                        String coinId = coinData.get("id");

                        System.out.println("\nAnalyzing cryptocurrency " + currentCoinIndex + " из " + totalCoins + ": " + coinName + " (" + coinId + ")");


                        String recommendation = ""; // Initialize recommendation variable

                        try {
                            // Get closing prices with dates
                            List<PriceData> closingPrices = apiClient.getClosingPrices(coinId, 50);

                            // Perform analysis
                            recommendation = analyzer.analyze(coinId, closingPrices);

                            // Print the recommendation to the console
                            System.out.println(recommendation);

                        } catch (IOException e) {
                            // Handle any exceptions that occur while fetching data
                            System.out.println("Error retrieving data for " + coinName + ": " + e.getMessage());
                        }

                        currentCoinIndex++; // Increment the current coin index

                        // Step 6: Send recommendations to the channel
                        if (recommendation.isEmpty()) {
                            // If no recommendations were found, send a message indicating this
                            channel.sendMessage("No recommendations for purchasing at the moment.").queue();
                        } else {
                            // Otherwise, send the recommendations to the channel
                            channel.sendMessage("Purchase recommendations:\n" + recommendation).queue();
                        }
                    }
                } catch (IOException e) {
                    // Handle any errors that occur while fetching the data
                    channel.sendMessage("An error occurred while fetching the data. Please try again later.").queue();
                    e.printStackTrace(); // Print the stack trace for debugging
                }
            }).start(); // Start the analysis thread

            // Check if the message content is the command "!help"
        } else if (messageContent.equalsIgnoreCase("!help")) {
            // Send a list of available commands to the channel
            channel.sendMessage("Available commands:\n!recommend - get cryptocurrency purchase recommendations\n!help - list of commands").queue();
        }
    }
}
