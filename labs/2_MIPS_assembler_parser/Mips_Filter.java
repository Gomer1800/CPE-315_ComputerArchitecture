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
      System.out.println( "output size: " + _outputList.size());
   }
   
   // getters
   public List<List<String>> get_list() {
      return _outputList;
   }

   public void print() {
   // Print Array: prints array contents by stepping through
      System.out.println("input:"); 
      for(int i=0; i < _inputList.size(); i++)
         System.out.println( _inputList.get(i) );  
     
      System.out.println("output:"); 
      for(int i=0; i < _outputList.size(); i++)
         System.out.println( _outputList.get(i) );  
   }

   public void tokenize() {
   // Tokenize Strings and assign to output list 
   List<String> tokens;
   //StringTokenizer st;
   //String delims = " ,";

   System.out.println( "tokenize list of size: " + _inputList.size());
      for(int i=0; i<_inputList.size(); i++) {
         //st = new StringTokenizer(_inputList.get(i), delims);
         tokens = new ArrayList<String>(Arrays.asList((_inputList.get(i)).trim().split("[ ,\\s+\t\n]+")));
         //while(st.hasMoreTokens()) {
         //   tokens.add(st.nextToken());
         //}

         System.out.println( "token #" + i + ": " + tokens );

         if(this.is_blank(tokens) == true) {
            continue;
         }
         else if (this.is_comment(tokens) == true) {
            continue;
         }
         else { 
            System.out.println("adding tokens...");
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
   System.out.println("is_blank: size()= " + tokens.size());
   if(tokens.isEmpty())   { return true; }
   if(tokens.size() == 1 && tokens.contains("")) { return true; }
   else return false;
   }
}
