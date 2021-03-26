import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FileManager {


    public static void main(String[] args) throws IOException {
        if (args.length > 0){
            File inputFile = new File(args[0]);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile));

            List<String> outputLines = new ArrayList<>();

            CurrencyConverter currencyConverter = new CurrencyConverter();
            FlixerClient flixerClient = new FlixerClient();
            double conversionRate = flixerClient.getConversionRate();

            DecimalFormat decimalFormat = new DecimalFormat("#.####");
            decimalFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ENGLISH));

            String inputLine;
            while ((inputLine = bufferedReader.readLine()) != null){
                double inputValue = Double.parseDouble(inputLine);
                double convertedValue = currencyConverter.convertCurrency(inputValue,conversionRate);
//                outputLines.add(String.valueOf(convertedValue));
                outputLines.add(decimalFormat.format(convertedValue));
            }

            Path outputFile = Paths.get("output.txt");
            Files.write(outputFile, outputLines, StandardCharsets.UTF_8);

        }
    }

}
