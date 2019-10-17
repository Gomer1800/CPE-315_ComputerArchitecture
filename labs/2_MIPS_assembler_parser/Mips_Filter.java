import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.io.*;

import java.lang.*;

public class Mips_Filter {
   
   // ATTRIBUTES
   
   private List<String> _inputList;
   private List<List<String>> _outputList;

   // CONSTRUCTOR
   public Mips_Filter(){}

   // FUNCTIONS

   // setters
   public void set_list( List<String> some_list){
      _inputList = some_list;
      _outputList = new ArrayList<List<String>>();
   }
   
   // getters
   public List<List<String>> get_list() {
      return _outputList;
   }

   public void print() {
   // Print Array: prints array contents by stepping through
      System.out.println("\ninput:"); 
      for(int i=0; i < _inputList.size(); i++)
         System.out.println( _inputList.get(i) );  
     
      System.out.println("\noutput:"); 
      for(int i=0; i < _outputList.size(); i++)
         System.out.println( _outputList.get(i) );  
   }

   public void tokenize() {
   // Tokenize Strings and assign to output list 
   List<String> tokens;

      for(int i=0; i<_inputList.size(); i++) {
         tokens = new ArrayList<String>(Arrays.asList((_inputList.get(i)).trim().split("[ ,\\s+\t\n]+")));

         if(this.is_blank(tokens) == true) {
            continue;
         }
         else if (this.is_comment(tokens) == true) {
            continue;
         }
         else { 
            _outputList.add(tokens);
         }
      }
   }

   private boolean is_comment( List<String> tokens ) {
   // Filter Comments by looking for strings starting with # char
      return (tokens.get(0)).equals("#");
   }

   private boolean is_blank( List<String> tokens) {
   // Filter Blank Lines
   if(tokens.isEmpty())   { return true; }
   if(tokens.size() == 1 && tokens.contains("")) { return true; }
   else return false;
   }
}
