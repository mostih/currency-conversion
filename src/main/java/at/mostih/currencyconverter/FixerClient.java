package at.mostih.currencyconverter;

import com.google.gson.*;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class FixerClient {
    private static final String BASE_URL = "http://data.fixer.io/api/latest";
    private String baseCurrency = "EUR";
    private String targetCurrency = "USD";
    private String fixerKey = getFixerKey();
    private String targetURL;

    /**
     * A method that reads the Fixer API Key from the apikey.properties in the root directory of the project
     *
     * @return a String representing the Fixer API Key
     * @throws IOException
     */
    private static String getFixerKey() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileReader("apikey.properties"));
        return properties.getProperty("FIXER_KEY");
    }

    /**
     * Default Constructor for the FixerClient.
     * When used, baseCurrency will be set to EUR and targetCurrency will be set to USD by default.
     *
     * @throws IOException
     */
    public FixerClient() throws IOException {
        this.targetURL = this.BASE_URL
                + "?access_key=" + this.fixerKey
                + "&base=" + this.baseCurrency
                + "&symbols=" + this.targetCurrency;
    }

    /**
     * Constructor for the FixerClient that allows the modification of baseCurrency and targetCurrency.
     * Note that the Fixer demo Key only supports EUR as base currency.
     *
     * @param baseCurrency
     * @param targetCurrency
     * @throws IOException
     */
    public FixerClient(String baseCurrency, String targetCurrency) throws IOException {
        this.targetURL = this.BASE_URL
                + "?access_key=" + this.fixerKey
                + "&base=" + baseCurrency
                + "&symbols=" + targetCurrency;
    }

    /**
     * Method that invokes the Fixer API to get the latest exchange rate
     * for the currently set base and target Currency.
     *
     * @return a double value representing the latest exchange rate.
     * @throws IOException
     */
    public double getExchangeRate() throws IOException {
        InputStream inputStream = new URL(this.targetURL).openStream();
        Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
        JsonElement returnValue = jsonObject.get("rates").getAsJsonObject().get(this.targetCurrency);

        reader.close();
        return returnValue.getAsDouble();
    }
}
