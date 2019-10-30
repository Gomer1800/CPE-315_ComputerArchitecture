import java.util.*;
import java.lang.*;

public class Emulator_AssemblyDecoder {
 
   // METHODS 
   public int decodeAssembly(/* Line of Mips Code, Register Memory */
      List<String> assemblyCode,
      int [] regMem,
      int PC) 
   {
      String inst = assemblyCode.get(0);

      if (inst.equals("addi")) {
      // rt = rs + immed
         System.out.println("addi");
         int rs    = regMem[Helpers._RegNum(assemblyCode.get(2))];
         int immed = Integer.parseInt(assemblyCode.get(3));
         regMem[Helpers._RegNum(assemblyCode.get(1))] = rs + immed;
         return (PC+1);
      }
      else if (inst.equals("beq")) {
      // offset if rs == rt 
         String rs =    Helpers._5bit_register_decoder(assemblyCode.get(1));
         String rt =    Helpers._5bit_register_decoder(assemblyCode.get(2));
         String immed = Helpers._16bit_signed(Integer.parseInt(assemblyCode.get(3)));
         //System.out.println(code + " " + rs + " " + rt + " " + immed);         
      }
      else if ((inst.equals("addi")) | (inst.equals("beq")) | (inst.equals("bne"))) {
         String rs =    Helpers._5bit_register_decoder(assemblyCode.get(1));
         String rt =    Helpers._5bit_register_decoder(assemblyCode.get(2));
         String immed = Helpers._16bit_signed(Integer.parseInt(assemblyCode.get(3)));
         //System.out.println(code + " " + rs + " " + rt + " " + immed);         
      }
      else if ((inst.equals("add")) | (inst.equals("sub")) |  
         (inst.equals("slt")) | (inst.equals("and")) | (inst.equals("or"))) {
         String rd = Helpers._5bit_register_decoder(assemblyCode.get(1));
         String rs = Helpers._5bit_register_decoder(assemblyCode.get(2));
         String rt = Helpers._5bit_register_decoder(assemblyCode.get(3));
         //System.out.println("000000 " +  rs + " " + rt + " " + rd + " 00000 " + code);
      }
      else if ((inst.equals("j")) | (inst.equals("jal"))) {
         String immed = Helpers._26bit_signed(Integer.parseInt(assemblyCode.get(1)));
         //System.out.println(code + " " + immed);
      }
      else if ((inst.equals("jr"))) {
         String rs = Helpers._5bit_register_decoder(assemblyCode.get(1));
         //System.out.println("000000 " + rs + " 000000000000000 " + code);
      }
      else if ((inst.equals("lw")) | (inst.equals("sw"))) {
         String rt =    Helpers._5bit_register_decoder(assemblyCode.get(1));  
         String immed = Helpers._16bit_signed(Integer.parseInt(assemblyCode.get(2)));
         String rs =    Helpers._5bit_register_decoder(assemblyCode.get(3));
         //System.out.println(code + " " + rs + " " + rt + " " + immed); 
      }
      else if (inst.equals("sll")) {
         String rd =    Helpers._5bit_register_decoder(assemblyCode.get(1));
         String rt =    Helpers._5bit_register_decoder(assemblyCode.get(2));
         String shamt = Helpers._5bit_unsigned(Integer.parseInt(assemblyCode.get(3)));
         //System.out.println("000000 00000 " + rt + " " + rd + " " + shamt + " " + code); 
      } 
      return PC;
   }
}   
