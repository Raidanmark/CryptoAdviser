# Crypto Advisor

This Discord bot is designed for cryptocurrency analysis using data from the CoinGecko API. Its main goal is to help users identify potential investment opportunities based on a technical indicator called the "golden cross."

## Goals of the Bot:

- **Investor Assistance:** Providing users with up-to-date information and technical analysis to make informed investment decisions in cryptocurrencies.
- **Automation of Analysis:** Reducing manual data collection and processing work, automating the technical analysis process.

## Key Features:

### Data Retrieval and Caching:

- **Top Cryptocurrencies:** The bot requests a list of top cryptocurrencies by market capitalization.
- **Historical Prices:** The bot retrieves closing price data for each cryptocurrency for the last 30 days.
- **Data Caching:** Retrieved data is stored in cache to minimize the number of API requests and comply with request limits. Cached data is reused for subsequent analyses until the cache is cleared.

### Technical Analysis:

- **Simple Moving Averages (SMA):** The bot calculates short-term (5 days) and long-term (20 days) simple moving averages for each cryptocurrency.
- **Golden Cross Detection:** The bot checks if the short-term SMA crosses above the long-term SMA, which is a bullish buy signal.

### Result Output:

- **Console Output:** The bot provides detailed calculations in the console, including closing prices, SMA values, and golden cross checks.
- **Recommendations:** If a golden cross is detected for a cryptocurrency, the bot recommends it for purchase and notifies the user.

### Command Management:

- **`!recommend`:** Initiates the cryptocurrency analysis process. If the data is already cached, the bot uses it; otherwise, it retrieves the data from CoinGecko and stores it for future analyses.
- **`!help`:** Displays a list of available commands and a brief description of their functions.

### Rate Limiting:

- **Rate Limiter:** The bot includes a rate-limiting mechanism to ensure that API request limits are not exceeded, preventing API blocks and ensuring the bot operates smoothly.
