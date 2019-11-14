import java.util.*;
import java.lang.*;

public class BinaryDecoder {

   // ATTRIBUTES
   public List<List<String>> _AssemblyCode;
   public Map<String, String> _instructionDictionary;

   public void decodeBinary() {

      for (int i=0; i<_AssemblyCode.size(); i++)
      {
         List<String> instList = _AssemblyCode.get(i); 
         String inst = instList.get(0);
         String code = _instructionDictionary.get(inst);
         if ((inst.equals("addi")) | (inst.equals("beq")) | (inst.equals("bne"))) {
            String rs =    Helpers._5bit_register_decoder(instList.get(1));
            String rt =    Helpers._5bit_register_decoder(instList.get(2));
            String immed = Helpers._16bit_signed(Integer.parseInt(instList.get(3)));
            //System.out.println(code + " " + rs + " " + rt + " " + immed);         
         }
         else if ((inst.equals("add")) | (inst.equals("sub")) |  
            (inst.equals("slt")) | (inst.equals("and")) | (inst.equals("or"))) {
            String rd = Helpers._5bit_register_decoder(instList.get(1));
            String rs = Helpers._5bit_register_decoder(instList.get(2));
            String rt = Helpers._5bit_register_decoder(instList.get(3));
            //System.out.println("000000 " +  rs + " " + rt + " " + rd + " 00000 " + code);
         }
         else if ((inst.equals("j")) | (inst.equals("jal"))) {
            String immed = Helpers._26bit_signed(Integer.parseInt(instList.get(1)));
            //System.out.println(code + " " + immed);
         }
         else if ((inst.equals("jr"))) {
            String rs = Helpers._5bit_register_decoder(instList.get(1));
            //System.out.println("000000 " + rs + " 000000000000000 " + code);
         }
         else if ((inst.equals("lw")) | (inst.equals("sw"))) {
            String rt =    Helpers._5bit_register_decoder(instList.get(1));  
            String immed = Helpers._16bit_signed(Integer.parseInt(instList.get(2)));
            String rs =    Helpers._5bit_register_decoder(instList.get(3));
            //System.out.println(code + " " + rs + " " + rt + " " + immed); 
         }
         else if (inst.equals("sll")) {
            String rd =    Helpers._5bit_register_decoder(instList.get(1));
            String rt =    Helpers._5bit_register_decoder(instList.get(2));
            String shamt = Helpers._5bit_unsigned(Integer.parseInt(instList.get(3)));
            //System.out.println("000000 00000 " + rt + " " + rd + " " + shamt + " " + code); 
         } 
         else if (i == _AssemblyCode.size()-1) {
            System.out.println(instList.get(0)); 
            }
      }
   }
}   
