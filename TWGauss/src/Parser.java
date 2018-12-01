import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Parser {
    private static final String FILE_NAME = "/Users/marcinaman/IdeaProjects/TW/TWGauss/src/input.txt";

    public static Matrix parseFile(){
        Path path = Paths.get(FILE_NAME);

        Matrix matrix = new Matrix();

        try {
            List<String> fileContent = Files.readAllLines(path);

            int size = parseSize(fileContent);

            matrix.setSize(size);

            matrix.setMatrix(parseMatrix(fileContent, size));

            matrix.setResult(parseLine(fileContent.get(size+1).split(" "), size));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return matrix;
    }

    private static int parseSize(List<String> file){
        return Integer.parseInt(file.get(0).trim());
    }

    private static double[][] parseMatrix(List<String> file, int size){
        double[][] matrix = new double[size][size];

        for(int i=0;i<size;i++){
            String line = file.get(i+1);
            String[] content = line.trim().split(" ");
            matrix[i] = parseLine(content, size);
        }

        return matrix;
    }

    private static double[] parseLine(String[] line, int size){
        double[] parsed = new double[size];

        for(int i=0;i<size;i++){
            parsed[i] = Double.parseDouble(line[i]);
        }

        return parsed;
    }
}
