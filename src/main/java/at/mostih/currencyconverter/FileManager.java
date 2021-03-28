package at.mostih.currencyconverter;

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

    /**
     * A method that returns a list of Doubles where each element represents a line from the given input file.
     *
     * @param file the input file. (has to be .txt or .csv)
     * @return a list of Doubles representing each line
     * @throws IOException
     */
    public List<Double> getValuesFromFile(File file) throws IOException {
        List<Double> values = new ArrayList<>();

        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        String inputLine;
        while ((inputLine = bufferedReader.readLine()) != null) {
            values.add(Double.parseDouble(inputLine));
        }
        bufferedReader.close();
        return values;
    }

    /**
     * A method that creates an output file from a given list of Strings.
     *
     * @param destinationFile the path including the target filename
     * @param outputLines     a list of Strings where each element represents a line in the output file
     * @throws IOException
     */
    public void writeToOutputFile(String destinationFile, List<String> outputLines) throws IOException {
        Path outputFile = Paths.get(destinationFile);
        Files.write(outputFile, outputLines, StandardCharsets.UTF_8);
    }


    /**
     * A method that extracts the extension of a given file and returns it as a String.
     * See {@linktourlhttps://stackoverflow.com/a/21974043}
     *
     * @param file the input file
     * @return a String representing the file extension including the dot.
     */
    public String getFileExtension(File file) {
        String filename = file.getName();
        int lastIndexOfName = filename.lastIndexOf(".");
        if (lastIndexOfName == -1) {
            return "";
        }
        return filename.substring(lastIndexOfName);
    }


}
