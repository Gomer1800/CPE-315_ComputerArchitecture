import java.lang.*;
import java.util.List;

public class Caller{
   public List<String> fnCallStdin() {
      List<String> nextCommand;
      List<String> lineList = readCommands.parseCommands(false, "");
      Parser parse = new Parser();
      parse.tokenize(lineList);
      List<List<String>> tokenized = parse.gettokenized();
      nextCommand = tokenized.get(0);
      tokenized.remove(0);
      return nextCommand;
   }
  
   public List<String> fnCallScript(List<List<String>> tokenized) {
      List<String> nextCommand = tokenized.get(0);
      tokenized.remove(0);   // remove, so that you don't have to keep track of indices
      return nextCommand;
   }
}
