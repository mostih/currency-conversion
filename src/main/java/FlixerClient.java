
import com.google.gson.*;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class FlixerClient {
    private static final String BASE_URL = "http://data.fixer.io/api/latest";
    private String baseCurrency = "EUR";
    private String targetCurrency = "USD";
    private String flixerKey = getFlixerKey();
    private String targetURL;

    private static String getFlixerKey() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileReader("apikey.properties"));
        return properties.getProperty("FLIXER_KEY");
    }

    public FlixerClient() throws IOException {
        this.targetURL = this.BASE_URL
                + "?access_key=" + this.flixerKey
                + "&base=" + this.baseCurrency
                + "&symbols=" + this.targetCurrency;
    }

    public FlixerClient(String baseCurrency, String targetCurrency) throws IOException {
        this.targetURL = this.BASE_URL
                + "?access_key=" + this.flixerKey
                + "&base=" + baseCurrency
                + "&symbols=" + targetCurrency;
    }

    public double getConversionRate() throws IOException {
        InputStream inputStream = new URL(this.targetURL).openStream();
        Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
        JsonElement returnValue = jsonObject.get("rates").getAsJsonObject().get(this.targetCurrency);
        return returnValue.getAsDouble();
    }
}
