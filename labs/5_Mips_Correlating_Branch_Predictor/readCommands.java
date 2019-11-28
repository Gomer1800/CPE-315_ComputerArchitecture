import java.util.*;
import java.util.stream.Collectors;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader; 

public class readCommands {
   public static List<String> parseCommands(boolean scriptMode, String filename) {
      List<String> lineList = new ArrayList<>();
      if (scriptMode == true) {
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
      } 
      else {
         try {
            BufferedReader reader =  
            new BufferedReader(new InputStreamReader(System.in)); 
            String command = reader.readLine(); 
            lineList.add(command);
         }
         catch (IOException e){
            e.printStackTrace();
         }
      }
      return lineList;
   }
}   

      
