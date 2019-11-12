import java.util.*;

public class test{
   public static void main(String[] args) {
      List<String> lineList = new ArrayList<>();
      Parser parse = new Parser();
      Caller fnCall = new Caller();
      boolean isScript;
      String file;
      List<List<String>> tokenized;
      
      if (args.length == 1) {
         isScript = true;
         file = args[0];
      }
      else {
         isScript = false;
         file = "";
      }   
      lineList = readCommands.parseCommands(isScript, file);
      parse.tokenize(lineList);
      tokenized = parse.gettokenized();
      /* print tokenized List */
      for (int i=0; i < tokenized.size(); i++) {
         for (int j = 0; j < tokenized.get(i).size(); j++) {
            System.out.print(tokenized.get(i).get(j) + " ");
         }
         System.out.println();
      }
      // until exit 
      fnCall.functionCaller(isScript, tokenized);  
   }
}   
