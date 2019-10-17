import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class Assembler {
   
   public static void main(String [] args) {

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

      // Stage 3, parse tokenized assembly array.
      // Remove Labels, creating label dictionary
      // Produce new array consisting of only assembly tokens with final addresses
      Mips_Parser myParser = new Mips_Parser();
      myParser.addressArr(filteredAssembly, myInstructions);
      Map<String, Integer> labelDictionary = myParser.getlabelDict();

      // Stage 4, output machine code to stdout
      BinaryDecoder myDecoder = new BinaryDecoder();
      myDecoder._AssemblyCode = myParser.tokensAddress;
      myDecoder._instructionDictionary = myInstructions;
      myDecoder.decodeBinary();
   }
}
