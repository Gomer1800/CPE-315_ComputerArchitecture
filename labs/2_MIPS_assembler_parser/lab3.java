/*
Name: Luis Gomez, Yu Asai
Section: 1
Description: Java Assembler
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

public class lab3 {
   
   public static void main(String [] args) {

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

      Emulator_FSM myFSM = new Emulator_FSM( myParser._AssemblyCode );
      myFSM.run_FSM();
   }
}
