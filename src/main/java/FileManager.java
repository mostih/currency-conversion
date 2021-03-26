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

    public List<Double> getValuesFromFile(File file) throws IOException {
        List<Double> values = new ArrayList<>();

        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        String inputLine;
        while ((inputLine = bufferedReader.readLine()) != null){
            values.add(Double.parseDouble(inputLine));
        }
        return values;
    }

    public void writeToOutputFile(String destinationFile, List<String> outputLines) throws IOException {
        Path outputFile = Paths.get(destinationFile);
        Files.write(outputFile, outputLines, StandardCharsets.UTF_8);
    }

    // https://stackoverflow.com/a/21974043
    public String getFileExtension(File file){
        String filename = file.getName();
        int lastIndexOfName = filename.lastIndexOf(".");
        if (lastIndexOfName == -1){
            return "";
        }
        return filename.substring(lastIndexOfName);
    }



}
