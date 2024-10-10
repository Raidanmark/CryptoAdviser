package Bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class BotLaunch {
    private Config config;

    public BotLaunch() {
        config = new Config(); //Load configuration
    }

    //Method to start bot
    public void start() {

        String token = config.getDiscordToken(); //Get token from Config class

        JDA jda = JDABuilder.createDefault(token) //Bot creating
                .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT) // Create ability to get with messages
                .addEventListeners(new BotListener()) //Allows to handle events
                .build(); //Bot launch

        System.out.println("Bot successfully launched!"); // Indicator that bot started
    }

}
