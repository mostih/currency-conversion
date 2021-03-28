package at.mostih.currencyconverter;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CurrencyConverter {

    /**
     * Method that calculates the exchange value from a given value and it's exchange rate.
     *
     * @param valueToConvert
     * @param exchangeRate
     * @return a double value representing the exchange value
     */
    public static double convertValue(double valueToConvert, double exchangeRate) {
        return valueToConvert * exchangeRate;
    }

    /**
     * Method that converts a list of Doubles to a list of Strings with it's respective exchanged values.
     *
     * @param valuesToConvert
     * @param fixedExchangeRate set to 'false' if the exchange rate should be updated for each individual conversion
     * @return a list of Strings containing the converted values
     * @throws IOException
     */
    public static List<String> convertValues(List<Double> valuesToConvert, boolean fixedExchangeRate) throws IOException {
        List<String> convertedValues = new ArrayList<>();

        FixerClient fixerClient = new FixerClient();

        DecimalFormat decimalFormat = new DecimalFormat("#.####");
        decimalFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ENGLISH));

        if (fixedExchangeRate) {
            double exchangeRate = fixerClient.getExchangeRate();
            for (Double valueToConvert : valuesToConvert) {
                convertedValues.add(decimalFormat.format(convertValue(valueToConvert, exchangeRate)));
            }
        } else {
            for (Double valueToConvert : valuesToConvert) {
                double exchangeRate = fixerClient.getExchangeRate();
                convertedValues.add(decimalFormat.format(convertValue(valueToConvert, exchangeRate)));
            }
        }

        return convertedValues;
    }


    public static void main(String[] args) throws IOException {
        FileManager fileManager = new FileManager();
        if (args.length == 1) {
            File inputFile = new File(args[0]);
            String inputFileExtension = fileManager.getFileExtension(inputFile);

            boolean fileSupport = (inputFileExtension.equals(".txt") || inputFileExtension.equals(".csv") || inputFileExtension.equals(""));
            if (!fileSupport) {
                System.out.println("The given input file type is currently not supported.");
                System.exit(0);
            }

            List<Double> inputValues = fileManager.getValuesFromFile(inputFile);
            List<String> convertedValues = convertValues(inputValues, true);

            fileManager.writeToOutputFile("output" + inputFileExtension, convertedValues);

        } else {
            System.err.println("Input error! Correct usage:\n" +
                    "java -jar currency-conversion.jar <inputfile.txt / inputfile.csv / inputfile>");
            System.exit(0);
        }
    }
}
