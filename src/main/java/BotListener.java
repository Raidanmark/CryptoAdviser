import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class BotListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return; // Ignore messages from other bots
        }

        String messageContent = event.getMessage().getContentRaw();
        MessageChannelUnion channel = event.getChannel();


        if (messageContent.equalsIgnoreCase("!recommend")) {
            channel.sendMessage("Please wait, analyzing top cryptocurrencies. This may take a few minutes...").queue();

            CoinGeckoApiClient apiClient = new CoinGeckoApiClient();
            CryptoAnalyzer analyzer = new CryptoAnalyzer();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            new Thread(() -> {
                try {
                    // Step 1: Get the IDs of the top 500 cryptocurrencies
                    List<Map<String, String>> topCoinsData = apiClient.getTopCoinsData(100);

                    // Step 2: Print the list of top 50 cryptocurrencies to the console
                    System.out.println("Топ 50 криптовалют по рыночной капитализации:");
                    for (Map<String, String> coinData : topCoinsData) {
                        String name = coinData.get("name");
                        String id = coinData.get("id");
                        System.out.println("- " + name + " (" + id + ")");
                    }

                    // Разделитель
                    System.out.println("\nРекомендации по покупке на основе анализа \"золотого креста\":");

                    int totalCoins = topCoinsData.size();
                    int currentCoinIndex = 1;

                    // Для каждой криптовалюты получаем историю цен закрытия и выполняем анализ
                    for (Map<String, String> coinData : topCoinsData) {
                        String coinName = coinData.get("name");
                        String coinId = coinData.get("id");

                        System.out.println("\nАнализируем криптовалюту " + currentCoinIndex + " из " + totalCoins + ": " + coinName + " (" + coinId + ")");


                        String recommendation = "";

                        try {
                            // Получаем цены закрытия с датами
                            List<PriceData> closingPrices = apiClient.getClosingPrices(coinId, 50);

                            // Выполняем анализ
                            recommendation = analyzer.analyze(coinId, closingPrices);

                            // Выводим рекомендацию
                            System.out.println(recommendation);

                        } catch (IOException e) {
                            System.out.println("Ошибка при получении данных для " + coinName + ": " + e.getMessage());
                        }

                        currentCoinIndex++;
                        // Step 6: Send recommendations to the channel
                        if (recommendation.isEmpty()) {
                            channel.sendMessage("No recommendations for purchasing at the moment.").queue();
                        } else {
                            channel.sendMessage("Purchase recommendations:\n" + recommendation.toString()).queue();
                        }
                    }
                } catch (IOException e) {
                    channel.sendMessage("An error occurred while fetching the data. Please try again later.").queue();
                    e.printStackTrace();
                }
            }).start();

        } else if (messageContent.equalsIgnoreCase("!help")) {
            channel.sendMessage("Available commands:\n!recommend - get cryptocurrency purchase recommendations\n!help - list of commands").queue();
        }
    }
}
