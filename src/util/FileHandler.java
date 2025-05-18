package util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    
    public static List<String> readFile(String filePath) throws IOException {
        // Ensure the file exists
        File file = new File(filePath);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
            return new ArrayList<>();
        }
        
        return Files.readAllLines(Paths.get(filePath));
    }
    
    public static void writeFile(String filePath, List<String> lines) throws IOException {
        // Ensure directory exists
        File file = new File(filePath);
        file.getParentFile().mkdirs();
        
        Files.write(Paths.get(filePath), lines);
    }
}