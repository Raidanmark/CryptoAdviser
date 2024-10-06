import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class CoinGeckoApiClient {
    private static final String BASE_URL = "https://api.coingecko.com/api/v3";
    private OkHttpClient client = new OkHttpClient();
    private Gson gson = new Gson();

    // Создаем RateLimiter с лимитом 9 запросов в 60 секунд (1 минута)
    private static final int MAX_REQUESTS_PER_MINUTE = 1;
    private static final long TIME_WINDOW_MILLIS = 15_000L; // 60 секунд
    private RateLimiter rateLimiter = new RateLimiter(MAX_REQUESTS_PER_MINUTE, TIME_WINDOW_MILLIS);

    /**
     * Получает данные топ N криптовалют по рыночной капитализации с их идентификаторами и названиями
     *
     * @param limit количество криптовалют для получения
     * @return список карт с данными криптовалют (id и name)
     * @throws IOException если произошла ошибка при выполнении запроса
     */
    public List<Map<String, String>> getTopCoinsData(int limit) throws IOException {
        // Используем RateLimiter перед выполнением запроса
        rateLimiter.acquire();

        String url = BASE_URL + "/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=" + limit + "&page=1&sparkline=false";
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Ошибка при запросе данных: " + response);
            }

            String jsonResponse = response.body().string();

            Type listType = new TypeToken<List<Map<String, Object>>>() {}.getType();
            List<Map<String, Object>> coinsData = gson.fromJson(jsonResponse, listType);

            List<Map<String, String>> coinList = new ArrayList<>();
            for (Map<String, Object> coin : coinsData) {
                String id = (String) coin.get("id");
                String name = (String) coin.get("name");
                Map<String, String> coinInfo = new HashMap<>();
                coinInfo.put("id", id);
                coinInfo.put("name", name);
                coinList.add(coinInfo);
            }

            return coinList;
        }
    }

    /**
     * Получает историю цен закрытия криптовалюты за последние N дней
     *
     * @param coinId идентификатор криптовалюты (например, "bitcoin")
     * @param days   количество дней для получения данных
     * @return список объектов PriceData с датой и ценой
     * @throws IOException если произошла ошибка при выполнении запроса
     */
    public List<PriceData> getClosingPrices(String coinId, int days) throws IOException {
        // Используем RateLimiter перед выполнением запроса
        rateLimiter.acquire();

        String url = BASE_URL + "/coins/" + coinId + "/market_chart?vs_currency=usd&days=" + days + "&interval=daily";
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Ошибка при запросе данных: " + response);
            }

            String jsonResponse = response.body().string();

            Type type = new TypeToken<Map<String, List<List<Double>>>>() {}.getType();
            Map<String, List<List<Double>>> marketData = gson.fromJson(jsonResponse, type);

            List<List<Double>> prices = marketData.get("prices");
            List<PriceData> closingPrices = new ArrayList<>();

            for (List<Double> pricePoint : prices) {
                long timestamp = pricePoint.get(0).longValue();
                double price = pricePoint.get(1);

                // Конвертируем timestamp в дату
                Date date = new Date(timestamp);
                closingPrices.add(new PriceData(date, price));
            }

            return closingPrices;
        }
    }
}
