import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.io.*;
import java.lang.*;

public class Parser {
   private List<List<String>> _tokenized = new ArrayList<List<String>>();
   
   public List<List<String>> gettokenized() {
      return _tokenized;
   }

   public void tokenize(List<String> CommandList) {
      List<String> tokens;
      
      for (int i=0; i< CommandList.size(); i++) { 
         tokens = new ArrayList<String>(Arrays.asList((CommandList.get(i)).trim().split("[ (),\\s]")));
         if(this.is_blank(tokens) == true) {
            continue;
         }
         else {
            _tokenized.add(tokens);
         }
      }
   }
    
   private boolean is_blank( List<String> tokens) {
      if (tokens.isEmpty())   { 
         return true; 
      }
      if(tokens.size() == 1 && tokens.contains("")) { 
         return true; 
      }      
      else {
         return false;
      }
   }
}     
