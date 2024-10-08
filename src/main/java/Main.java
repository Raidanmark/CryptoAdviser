import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.requests.GatewayIntent;
import io.github.cdimascio.dotenv.Dotenv; // Импорт библиотеки dotenv

public class Main {
    public static void main(String[] args) throws LoginException {

        Dotenv dotenv = Dotenv.configure().filename("Secret.env").load();
        String token = dotenv.get("DISCORD_TOKEN"); // Get Discord Bot Token from env

        JDA jda = JDABuilder.createDefault(token) //Bot creating
                .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT) // Create ability to get with messages
                .addEventListeners(new BotListener()) //Allows to handle events
                .build(); //Bot launch

        System.out.println("Bot successfully launched!"); // Indicator that bot started
    }
}