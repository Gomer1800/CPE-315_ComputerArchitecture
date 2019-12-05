import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.io.*;
import java.lang.*;

public class Parser {

   // ATTRIBUTES

   public String _file = "";

   public boolean _isScript;
   
   private List<List<String>> _tokenized = new ArrayList<List<String>>();
   
   // METHODS

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
   
   public List<String> fromStdin()
   {
      //DEBUG
      //System.out.print("fromStdin() ");
      
      List<String> nextCommand;
      List<String> lineList = readCommands.parseCommands(false, "");
      this.tokenize(lineList);
      _tokenized = this.gettokenized();
      nextCommand = _tokenized.get(0);
      _tokenized.remove(0);
      return nextCommand;
   }

   public List<String> fromScript()
   {
      System.out.print("fromScript() ");
      List<String> nextCommand = _tokenized.get(0);
      _tokenized.remove(0);   // remove, so that you don't have to keep track of indices
      return nextCommand;
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
