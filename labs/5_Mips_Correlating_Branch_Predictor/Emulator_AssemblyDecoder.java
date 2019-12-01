import java.util.*;
import java.lang.*;

public class Emulator_AssemblyDecoder {
 
   // ATTRIBUTES

   // Correlating Branch Predictor
   public Mips_Correlating_Branch_Predictor _Predictor;

   // CONSTRUCTOR
   public Emulator_AssemblyDecoder() { 
   /*
   default constructor creates GHR of size 2
   */
      this._Predictor = new Mips_Correlating_Branch_Predictor(2);
   }

   public Emulator_AssemblyDecoder(int user_GHR_size) {
   /*
   creates GHR with user given size
   */
      this._Predictor = new Mips_Correlating_Branch_Predictor(user_GHR_size);
   }

   // METHODS 
   public int decodeAssembly(/* Line of Mips Code, Register Memory */
      List<String> assemblyCode,
      int [] regMem,
      int PC,
      int [] dataMem,
      int SP)
   {
      String inst = assemblyCode.get(0);

      // DEBUG PRINTOUT
      System.out.println("decodeAssembly() ... " + assemblyCode);

      if (inst.equals("addi")) {
      // rt = rs + immed
         //System.out.println("addi " + assemblyCode.get(2) + " = " + assemblyCode.get(1) + " + " + assemblyCode.get(3));
         int rt    = regMem[Helpers._RegNum(assemblyCode.get(1))];
         int rs    = regMem[Helpers._RegNum(assemblyCode.get(2))];
         int immed = Integer.parseInt(assemblyCode.get(3));
         regMem[Helpers._RegNum(assemblyCode.get(1))] = rs + immed;
         return (PC+1);
      }

      else if (inst.equals("beq")) {
      // offset if rs == rt 
       //  System.out.println("beq " + assemblyCode.get(1) + " == " + assemblyCode.get(2) + ", relative offset = " + assemblyCode.get(3));
         int rs    = regMem[Helpers._RegNum(assemblyCode.get(1))];
         int rt    = regMem[Helpers._RegNum(assemblyCode.get(2))];
         int immed = Integer.parseInt(assemblyCode.get(3));
         
         // Call Correlating Predictor
         _Predictor.updateTable(rs == rt);

         if (rs == rt) {
            return (PC + 1 + immed);
         }
         return (PC+1);
      }
      
      // offset if rs != rt 
      else if (inst.equals("bne")) {
       //  System.out.println("bne " + assemblyCode.get(1) + " != " + assemblyCode.get(2));
         int rs    = regMem[Helpers._RegNum(assemblyCode.get(1))];
         int rt    = regMem[Helpers._RegNum(assemblyCode.get(2))];
         int immed = Integer.parseInt(assemblyCode.get(3));

         // Call Correlating Predictor
         _Predictor.updateTable(rs != rt);

         if (rs != rt){
            return (PC + 1 + immed);
         }
         return (PC+1);
      }

      //  rd = rs + rt
      else if (inst.equals("add")) {
       //  System.out.println("add " + assemblyCode.get(2) + " + " + assemblyCode.get(3));
         int rs    = regMem[Helpers._RegNum(assemblyCode.get(2))];
         int rt    = regMem[Helpers._RegNum(assemblyCode.get(3))];
         regMem[Helpers._RegNum(assemblyCode.get(1))] = rs + rt;
         return (PC+1);
      }

      // rd = rs - rt
      else if (inst.equals("sub")) {
       //  System.out.println("sub " + assemblyCode.get(2) + " - " + assemblyCode.get(3));
         int rs    = regMem[Helpers._RegNum(assemblyCode.get(2))];
         int rt    = regMem[Helpers._RegNum(assemblyCode.get(3))];
         regMem[Helpers._RegNum(assemblyCode.get(1))] = rs - rt;
         return (PC+1);
      }

      // rd = rs & rt 
       else if (inst.equals("and")) {
       //  System.out.println("and " + assemblyCode.get(2) + " & " + assemblyCode.get(3));
         int rs    = regMem[Helpers._RegNum(assemblyCode.get(2))];
         int rt    = regMem[Helpers._RegNum(assemblyCode.get(3))];
         regMem[Helpers._RegNum(assemblyCode.get(1))] = rs & rt;
         return (PC+1);
      }

      // rd = rs | rt 
      else if (inst.equals("or")) {
       //  System.out.println("or " + assemblyCode.get(2) + " | " + assemblyCode.get(3));
         int rs    = regMem[Helpers._RegNum(assemblyCode.get(2))];
         int rt    = regMem[Helpers._RegNum(assemblyCode.get(3))];
         regMem[Helpers._RegNum(assemblyCode.get(1))] = rs | rt;
         return (PC+1);
      }

      // set rd = 1 if rs < rt else rd = 0
      else if (inst.equals("slt")) {
       //  System.out.println("slt " + assemblyCode.get(2) + " < " + assemblyCode.get(3));
         int rs    = regMem[Helpers._RegNum(assemblyCode.get(2))];
         int rt    = regMem[Helpers._RegNum(assemblyCode.get(3))];
         if (rs < rt) {
            regMem[Helpers._RegNum(assemblyCode.get(1))] = 1;
         }
         else{
            regMem[Helpers._RegNum(assemblyCode.get(1))] = 0; 
         }
         return (PC+1);
      }

      // offset by immed
      else if(inst.equals("j")) {   // not sure if it's correct
       //  System.out.println("j " + assemblyCode.get(1));
         int immed = Integer.parseInt(assemblyCode.get(1)); 
         return (immed);
      }

      // offet by rs
      else if(inst.equals("jr")) {
       //  System.out.println("jr " + assemblyCode.get(1));
         int rs    = regMem[Helpers._RegNum(assemblyCode.get(1))];
         return rs;  //needs PC+1?  
      }

      // offset by immed and link ra 
      else if(inst.equals("jal")) {
         //System.out.println("jal " + assemblyCode.get(1));
         int immed = Integer.parseInt(assemblyCode.get(1));
         regMem[31] = PC + 1;  //regMem[31] = $ra
         return immed;
      }

      // rs = rt << shamt
      else if(inst.equals("sll")) {
       //  System.out.println("sll " + assemblyCode.get(2) + " << " + assemblyCode.get(3));
         int rt   = regMem[Helpers._RegNum(assemblyCode.get(2))];     
         int shamt = Integer.parseInt(assemblyCode.get(3));
         regMem[Helpers._RegNum(assemblyCode.get(1))] = rt << shamt;
         return (PC+1);
      }
      
      else if (inst.equals("lw")){
         //System.out.println("lw " + assemblyCode.get(1) + " = Mem[ " + assemblyCode.get(2) + " + " + assemblyCode.get(3) + "]");
         int rt    = regMem[Helpers._RegNum(assemblyCode.get(1))];
         int immed = Integer.parseInt(assemblyCode.get(2));
         int rs    = regMem[Helpers._RegNum(assemblyCode.get(3))]; 
         regMem[Helpers._RegNum(assemblyCode.get(1))] = dataMem[rs + immed]; 
         return (PC+1);
      }
      
      else if (inst.equals("sw")){
        // System.out.println("sw " + "Mem[ " + assemblyCode.get(2) + " + " + assemblyCode.get(3) + "] = " + assemblyCode.get(1)); 
         int rt    = regMem[Helpers._RegNum(assemblyCode.get(1))];
         int immed = Integer.parseInt(assemblyCode.get(2));
         int rs    = regMem[Helpers._RegNum(assemblyCode.get(3))]; 
         dataMem[rs + immed] = rt;
         return (PC+1);
      }
      return PC;
   }

   public void reset() {
      _Predictor.reset();
   }

   public void printBranchPredictorAccuracy() {
      _Predictor.printAccuracy();
   }
}   
