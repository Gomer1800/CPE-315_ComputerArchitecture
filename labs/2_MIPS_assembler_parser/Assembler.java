import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class Assembler {
   
   public static void main(String [] args) {

      // Stage 1, read raw Assembly
      List<String> rawAssembly = new ArrayList<>();
      rawAssembly = readFile.parseFile(args[0]);
      makeInstDict dict = new makeInstDict();
      dict.create();
      
      // Stage 2, filter out blank lines, comments, tokenize strings 
      Mips_Filter myFilter = new Mips_Filter();
      myFilter.set_list( rawAssembly ); 
      myFilter.tokenize();
      List<List<String>> tokenAssembly = myFilter.get_list();

      // Compare Filter Results
      myFilter.print();
   }
}
