import java.lang.*;
import java.util.List;

public class Caller{
   public List<String> functionCaller(boolean isScript, List<List<String>> tokenized) {
      List<String> nextCommand;
      if (isScript) {
         nextCommand = tokenized.get(0);
         tokenized.remove(0);   // remove, so that you don't have to keep track of indices
         return nextCommand;
      }
      else {  //stdin
         List<String> lineList = readCommands.parseCommands(false, "");
         Parser parse = new Parser();
         parse.tokenize(lineList);
         tokenized = parse.gettokenized();
         nextCommand = tokenized.get(0);
         tokenized.remove(0);
         return nextCommand;
      }
   }
}
