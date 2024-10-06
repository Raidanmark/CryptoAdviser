import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CryptoAnalyzer {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Анализирует криптовалюту на наличие "золотого креста"
     *
     * @param coinId        идентификатор криптовалюты
     * @param priceDataList список объектов PriceData (дата и цена)
     * @return рекомендация по покупке или информация об отсутствии сигнала
     */
    public String analyze(String coinId, List<PriceData> priceDataList) {
        int shortPeriod = 20; // Краткосрочная SMA (5 дней)
        int longPeriod = 50; // Долгосрочная SMA (20 дней)

        System.out.println("\nНачало анализа для " + coinId);

        // Проверяем, достаточно ли данных для расчета долгосрочной SMA
        if (priceDataList.size() < longPeriod) {
            System.out.println("Недостаточно данных для анализа " + coinId + ". Требуется как минимум " + longPeriod + " дней данных.");
            return "Недостаточно данных для анализа " + coinId + ".";
        }

        List<Double> shortSMAList = new ArrayList<>();
        List<Double> longSMAList = new ArrayList<>();

        // Рассчитываем скользящие средние для каждого периода
        for (int i = longPeriod - 1; i < priceDataList.size(); i++) {
            // Получаем цены для краткосрочной и долгосрочной SMA
            List<Double> shortClosingPrices = new ArrayList<>();
            List<String> shortDates = new ArrayList<>();
            for (int j = i - shortPeriod + 1; j <= i; j++) {
                shortClosingPrices.add(priceDataList.get(j).getPrice());
                shortDates.add(dateFormat.format(priceDataList.get(j).getDate()));
            }

            List<Double> longClosingPrices = new ArrayList<>();
            List<String> longDates = new ArrayList<>();
            for (int j = i - longPeriod + 1; j <= i; j++) {
                longClosingPrices.add(priceDataList.get(j).getPrice());
                longDates.add(dateFormat.format(priceDataList.get(j).getDate()));
            }

            // Рассчитываем SMA
            double shortSMA = calculateSMA(shortClosingPrices);
            double longSMA = calculateSMA(longClosingPrices);

            shortSMAList.add(shortSMA);
            longSMAList.add(longSMA);

            // Выводим промежуточные расчеты
            System.out.println("\nДата: " + dateFormat.format(priceDataList.get(i).getDate()));
            System.out.println("Краткосрочные цены (" + shortPeriod + " дней): " + shortClosingPrices);
            System.out.println("Краткосрочная SMA: " + shortSMA);

            System.out.println("Долгосрочные цены (" + longPeriod + " дней): " + longClosingPrices);
            System.out.println("Долгосрочная SMA: " + longSMA);
        }

        // Проверяем наличие "золотого креста"
        boolean goldenCross = isGoldenCross(shortSMAList, longSMAList);

        if (goldenCross) {
            System.out.println("\nОбнаружен \"золотой крест\" для " + coinId + "!");
            return "Рекомендуется купить " + coinId + ". Обнаружено пересечение скользящих средних (\"золотой крест\").";
        } else {
            System.out.println("\n\"Золотой крест\" не обнаружен для " + coinId + ".");
            return "Нет сигнала на покупку для " + coinId + ".";
        }
    }

    /**
     * Рассчитывает простую скользящую среднюю (SMA)
     *
     * @param prices список цен для расчета SMA
     * @return значение SMA
     */
    private double calculateSMA(List<Double> prices) {
        double sum = 0.0;
        for (Double price : prices) {
            sum += price;
        }
        return sum / prices.size();
    }

    /**
     * Проверяет наличие "золотого креста" на основе краткосрочной и долгосрочной SMA
     *
     * @param shortSMAList список значений краткосрочной SMA
     * @param longSMAList  список значений долгосрочной SMA
     * @return true, если "золотой крест" обнаружен, иначе false
     */
    private boolean isGoldenCross(List<Double> shortSMAList, List<Double> longSMAList) {
        int size = shortSMAList.size();
        if (size < 2) return false;

        // Предыдущие значения SMA
        double prevShortSMA = shortSMAList.get(size - 2);
        double prevLongSMA = longSMAList.get(size - 2);

        // Текущие значения SMA
        double currShortSMA = shortSMAList.get(size - 1);
        double currLongSMA = longSMAList.get(size - 1);

        System.out.println("\nПроверка на \"золотой крест\":");
        System.out.println("Предыдущая краткосрочная SMA: " + prevShortSMA);
        System.out.println("Предыдущая долгосрочная SMA: " + prevLongSMA);
        System.out.println("Текущая краткосрочная SMA: " + currShortSMA);
        System.out.println("Текущая долгосрочная SMA: " + currLongSMA);

        // Проверяем условия "золотого креста"
        boolean goldenCross = (prevShortSMA <= prevLongSMA) && (currShortSMA > currLongSMA);

        System.out.println("Условие \"золотого креста\" выполнено: " + goldenCross);

        return goldenCross;
    }
}

