import java.util.*;
import java.util.stream.Collectors; 
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;

public class readFile {
   public static List<String> parseFile(String filename) {
      List<String> lineList = new ArrayList<>();
      try {
         File file = new File(filename);
         FileReader fileReader = new FileReader(file);
         BufferedReader br = new BufferedReader(fileReader);

         lineList = br.lines().collect(Collectors.toList());  
         fileReader.close();      
      }
      catch (IOException e) {
         e.printStackTrace();
      }
      return lineList;
   }
}   

