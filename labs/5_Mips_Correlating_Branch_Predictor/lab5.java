/*
Name: Luis Gomez
Section: 1
Description: Mips Emulator
Composed of 4 Modules and 2 Helper Classes

Module
1: File Reader
2: Filter/Tokenizer
3: Label/Instruction parser
4: Binary Decoder 

Helpers:
1: Helper Functions
2: Instruction Set Dictionary
*/


import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class lab5 {
   
   public static void main(String [] args) {

      // DEBUG: Print Arrguments
      /*
      int i=0;
      for(String s: args) {
         System.out.printf("%d) %s\n", i, s);
         i++;
      }
      */
      // ASSEMBLER

      // Set Up: Create hardcoded instruction dictionary for reference
      makeInstDict instructionDictionary = new makeInstDict();
      instructionDictionary.create();
      Map<String, String> myInstructions = instructionDictionary.getInstDict();

      // Stage 1, read raw Assembly
      List<String> rawAssembly = new ArrayList<>();
      rawAssembly = readFile.parseFile(args[0]);
            
      // Stage 2, filter out blank lines, comments, tokenize strings 
      Mips_Filter myFilter = new Mips_Filter();
      myFilter.set_list( rawAssembly ); 
      myFilter.tokenize();
      List<List<String>> filteredAssembly = myFilter.get_list();
      //myFilter.print();

      // Stage 3, parse tokenized assembly array.
      // Remove Labels, creating label dictionary
      // Produce new array consisting of only assembly tokens with final addresses
      Mips_Parser myParser = new Mips_Parser();
      myParser.addressArr(filteredAssembly, myInstructions);
      List<List<String>> cleanTokens =  myParser._AssemblyCode;
      Map<String, Integer> labelDictionary = myParser.getlabelDict();
      myParser.setDifference();
      
      // Stage 4, output machine code to stdout
      BinaryDecoder myDecoder = new BinaryDecoder();
      myDecoder._AssemblyCode = myParser._AssemblyCode;
      myDecoder._instructionDictionary = myInstructions;
      myDecoder.decodeBinary();

      // EMULATOR

      boolean isScript;
      String file;
      int myGHRSize = 2;

      Parser scriptParser = new Parser();

      // Evaluate Terminal Arguments
      // Check if script present 
      if (args.length == 2)
      { // Script or GHR size might be present
         if(args[1].contains(".script"))
         { // Script file detected
            scriptParser._isScript = true;
            scriptParser._file = args[1];
         }
         else 
         { // GHR size detected
            myGHRSize = Integer.parseInt(args[1]);
         }
      }
      if (args.length == 3)
      { // Bot Script and GHR size present
            scriptParser._isScript = true;
            scriptParser._file = args[1];
            myGHRSize = Integer.parseInt(args[2]);
      }
      else {
         scriptParser._isScript = false;
         scriptParser._file = "";
         // GHR size defaults to 2
      }   
      
      Mips_Emulator myEmulator = new Mips_Emulator( myParser._AssemblyCode, scriptParser, myGHRSize);

      List<String> lineList = new ArrayList<>();
      if(scriptParser._isScript) {
         lineList = readCommands.parseCommands(scriptParser._isScript, scriptParser._file);
      }
      
      scriptParser.tokenize(lineList);
      /* print tokenized List */
      //scriptParser.print();
 
      myEmulator.run_FSM();

      // TESTING GHR
      //Scanner in = new Scanner(System.in);
      /*
      int a = in.nextInt();

      Shift_Register myGHR = new Shift_Register(a);
      myGHR.printDebug();
   
      // test adding a new value
      myGHR.insert(true);
      myGHR.insert(false);
      myGHR.printDebug();
      */

      /*
      // TESTING PREDICTOR
      int b = in.nextInt();
      Mips_Correlating_Branch_Predictor myTable = new Mips_Correlating_Branch_Predictor(b);
      myTable.printDebug();

      //testing updating predictor with some booleans
      myTable.updateTable(true);
      myTable.updateTable(false);
      myTable.printDebug();
      */
   }
}
