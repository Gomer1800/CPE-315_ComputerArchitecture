import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.io.*;
import java.lang.*;

public class Parser {

   public boolean _isScript;
   
   private List<List<String>> _tokenized = new ArrayList<List<String>>();
   
   public List<List<String>> gettokenized() {
      return _tokenized;
   }

   public List<List<String>> tokenize(List<String> CommandList) {
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
      return _tokenized;
   }
    
   private boolean is_blank( List<String> tokens)
   {
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
   
   public void fromStdin(List<String> cmd)
   {
      System.out.println("fromStdin()");
      List<String> nextCommand;
      List<String> lineList = readCommands.parseCommands(false, "");
      this.tokenize(lineList);
      List<List<String>> tokenized = this.gettokenized();
      cmd = tokenized.get(0);
      tokenized.remove(0);
   }

   public void fromScript(List<String> cmd)
   {
      System.out.println("fromScript()");
      cmd = _tokenized.get(0);
      _tokenized.remove(0);   // remove, so that you don't have to keep track of indices
   }
   
   public void print() 
   {
      /* print tokenized List */
      for (int i=0; i < _tokenized.size(); i++) {
         for (int j = 0; j < _tokenized.get(i).size(); j++) {
            System.out.print(_tokenized.get(i).get(j) + " ");
         }
         System.out.println();
      }
   }
}     
