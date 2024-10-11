package Bot.Bot;

import io.github.cdimascio.dotenv.Dotenv;

public class Config {
    private final Dotenv dotenv;

    //Method to upload .env file
    public Config() {
        dotenv = Dotenv.configure().filename("src/main/java/resources/Secret.env").load();
    }

    //Method to get the token from the .env file
    public String getDiscordToken() {
        return dotenv.get("DISCORD_TOKEN");
    }
}
