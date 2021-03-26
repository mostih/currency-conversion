import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CurrencyConverter {
    public static double convertCurrency(double valueToConvert, double exchangeRate){
        return valueToConvert*exchangeRate;
    }

    public static void main(String[] args) throws IOException {
        FileManager fileManager = new FileManager();
        if (args.length == 1){
            File inputFile = new File(args[0]);
            String inputFileExtension = fileManager.getFileExtension(inputFile);

            boolean fileSupport = (inputFileExtension.equals(".txt") || inputFileExtension.equals(".csv") || inputFileExtension.equals(""));
            if (!fileSupport){
                System.out.println("The used input file extension is currently not supported.");
                System.exit(0);
            }

            List<Double> inputValues = fileManager.getValuesFromFile(inputFile);
            List<String> convertedValues = new ArrayList<>();
            DecimalFormat decimalFormat = new DecimalFormat("#.####");
            decimalFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ENGLISH));

            FlixerClient flixerClient = new FlixerClient();
            double exchangeRate = flixerClient.getConversionRate();

            for (Double valueToConvert : inputValues) {
                convertedValues.add(decimalFormat.format(convertCurrency(valueToConvert, exchangeRate)));
            }

            fileManager.writeToOutputFile("output.txt",convertedValues);

        }
    }
}
