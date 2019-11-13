import java.util.*;
import java.lang.*;

public class Emulator_Assembly_Decoder {
 
   // METHODS 
   public static int decode(/* Line of Mips Code, Register Memory */
      List<String> assemblyCode,
      int [] regMem,
      int PC,
      int [] dataMem,
      int SP,
      boolean branchTakenFlag,
      boolean jumpFlag,
      boolean loadWordFlag,
      int prevDestReg)
   {
      // SETUP

      String inst = assemblyCode.get(0);
      branchTakenFlag = false;
      jumpFlag = false;
      loadWordFlag = false;

      // BUSINESS LOGIC

      if (inst.equals("addi")) {
      // rt = rs + immed
         //System.out.println("addi " + assemblyCode.get(2) + " = " + assemblyCode.get(1) + " + " + assemblyCode.get(3));
         int rt    = regMem[Helpers._RegNum(assemblyCode.get(2))];
         int rs    = regMem[Helpers._RegNum(assemblyCode.get(1))];
         int immed = Integer.parseInt(assemblyCode.get(3));
         regMem[Helpers._RegNum(assemblyCode.get(2))] = rs + immed;
         // Check if loadWord flag needed? else reset prev dest reg
         if(prevDestReg != -1) {
            System.out.format("prevRd = %d rt = %d, rs = %d\n",prevDestReg,Helpers._RegNum(assemblyCode.get(2)),Helpers._RegNum(assemblyCode.get(1)));
            loadWordFlag = (prevDestReg == Helpers._RegNum(assemblyCode.get(2))) ? true:
            ((prevDestReg == (Helpers._RegNum(assemblyCode.get(1)))) ? true:false);
            prevDestReg = -1;
         }
         return (PC+1);
      }

      else if (inst.equals("beq")) {
      // offset if rs == rt 
       //  System.out.println("beq " + assemblyCode.get(1) + " == " + assemblyCode.get(2) + ", relative offset = " + assemblyCode.get(3));
         int rs    = regMem[Helpers._RegNum(assemblyCode.get(1))];
         int rt    = regMem[Helpers._RegNum(assemblyCode.get(2))];
         int immed = Integer.parseInt(assemblyCode.get(3));
         // Check if loadWord flag needed? else reset prev dest reg
         if(prevDestReg != -1) {
            System.out.format("prevRd = %d rt = %d, rs = %d\n",prevDestReg,Helpers._RegNum(assemblyCode.get(2)),Helpers._RegNum(assemblyCode.get(1)));
            loadWordFlag = (prevDestReg == Helpers._RegNum(assemblyCode.get(1))) ? true:
            ((prevDestReg == (Helpers._RegNum(assemblyCode.get(2)))) ? true:false);
            prevDestReg = -1;
         }
         if (rs == rt) {
            branchTakenFlag = true;
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
         // Check if loadWord flag needed? else reset prev dest reg
         if(prevDestReg != -1) {
            System.out.format("prevRd = %d rt = %d, rs = %d\n",prevDestReg,Helpers._RegNum(assemblyCode.get(2)),Helpers._RegNum(assemblyCode.get(1)));
            loadWordFlag = (prevDestReg == Helpers._RegNum(assemblyCode.get(1))) ? true:
            ((prevDestReg == (Helpers._RegNum(assemblyCode.get(2)))) ? true:false);
            prevDestReg = -1;
         }
         if (rs != rt){
            branchTakenFlag = true;
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
         // Check if loadWord flag needed? else reset prev dest reg
         if(prevDestReg != -1) {
            System.out.format("prevRd = %d rt = %d, rs = %d\n",prevDestReg,Helpers._RegNum(assemblyCode.get(3)),Helpers._RegNum(assemblyCode.get(2)));
            loadWordFlag = (prevDestReg == Helpers._RegNum(assemblyCode.get(2))) ? true:
            ((prevDestReg == (Helpers._RegNum(assemblyCode.get(3)))) ? true:false);
            prevDestReg = -1;
         }
         return (PC+1);
      }

      // rd = rs - rt
      else if (inst.equals("sub")) {
       //  System.out.println("sub " + assemblyCode.get(2) + " - " + assemblyCode.get(3));
         int rs    = regMem[Helpers._RegNum(assemblyCode.get(2))];
         int rt    = regMem[Helpers._RegNum(assemblyCode.get(3))];
         regMem[Helpers._RegNum(assemblyCode.get(1))] = rs - rt;
         // Check if loadWord flag needed? else reset prev dest reg
         if(prevDestReg != -1) {
            System.out.format("prevRd = %d rt = %d, rs = %d\n",prevDestReg,Helpers._RegNum(assemblyCode.get(3)),Helpers._RegNum(assemblyCode.get(2)));
            loadWordFlag = (prevDestReg == Helpers._RegNum(assemblyCode.get(2))) ? true:
            ((prevDestReg == (Helpers._RegNum(assemblyCode.get(3)))) ? true:false);
            prevDestReg = -1;
         }
         return (PC+1);
      }

      // rd = rs & rt 
       else if (inst.equals("and")) {
       //  System.out.println("and " + assemblyCode.get(2) + " & " + assemblyCode.get(3));
         int rs    = regMem[Helpers._RegNum(assemblyCode.get(2))];
         int rt    = regMem[Helpers._RegNum(assemblyCode.get(3))];
         regMem[Helpers._RegNum(assemblyCode.get(1))] = rs & rt;
         // Check if loadWord flag needed? else reset prev dest reg
         if(prevDestReg != -1) {
            System.out.format("prevRd = %d rt = %d, rs = %d\n",prevDestReg,Helpers._RegNum(assemblyCode.get(3)),Helpers._RegNum(assemblyCode.get(2)));
            loadWordFlag = (prevDestReg == Helpers._RegNum(assemblyCode.get(2))) ? true:
            ((prevDestReg == (Helpers._RegNum(assemblyCode.get(3)))) ? true:false);
            prevDestReg = -1;
         }
         return (PC+1);
      }

      // rd = rs | rt 
      else if (inst.equals("or")) {
       //  System.out.println("or " + assemblyCode.get(2) + " | " + assemblyCode.get(3));
         int rs    = regMem[Helpers._RegNum(assemblyCode.get(2))];
         int rt    = regMem[Helpers._RegNum(assemblyCode.get(3))];
         regMem[Helpers._RegNum(assemblyCode.get(1))] = rs | rt;
         // Check if loadWord flag needed? else reset prev dest reg
         if(prevDestReg != -1) {
            System.out.format("prevRd = %d rt = %d, rs = %d\n",prevDestReg,Helpers._RegNum(assemblyCode.get(3)),Helpers._RegNum(assemblyCode.get(2)));
            loadWordFlag = (prevDestReg == Helpers._RegNum(assemblyCode.get(2))) ? true:
            ((prevDestReg == (Helpers._RegNum(assemblyCode.get(3)))) ? true:false);
            prevDestReg = -1;
         }
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
         // Check if loadWord flag needed? else reset prev dest reg
         if(prevDestReg != -1) {
            System.out.format("prevRd = %d rt = %d, rs = %d\n",prevDestReg,Helpers._RegNum(assemblyCode.get(3)),Helpers._RegNum(assemblyCode.get(2)));
            loadWordFlag = (prevDestReg == Helpers._RegNum(assemblyCode.get(2))) ? true:
            ((prevDestReg == (Helpers._RegNum(assemblyCode.get(3)))) ? true:false);
            prevDestReg = -1;
         }
         return (PC+1);
      }

      // offset by immed
      else if(inst.equals("j")) {   // not sure if it's correct
       //  System.out.println("j " + assemblyCode.get(1));
         int immed = Integer.parseInt(assemblyCode.get(1));
         jumpFlag = true;
         return (immed);
      }

      // offet by rs
      else if(inst.equals("jr")) {
       //  System.out.println("jr " + assemblyCode.get(1));
         int rs    = regMem[Helpers._RegNum(assemblyCode.get(1))];
         jumpFlag = true;
         // Check if loadWord flag needed? else reset prev dest reg
         if(prevDestReg != -1) {
            System.out.format("prevRd = %d, rs = %d\n",prevDestReg,Helpers._RegNum(assemblyCode.get(1)));
            loadWordFlag = (prevDestReg == Helpers._RegNum(assemblyCode.get(1))) ? true:false;
            prevDestReg = -1;
         }
         return rs;  //needs PC+1?  
      }

      // offset by immed and link ra 
      else if(inst.equals("jal")) {
         //System.out.println("jal " + assemblyCode.get(1));
         int immed = Integer.parseInt(assemblyCode.get(1));
         regMem[31] = PC + 1;  //regMem[31] = $ra
         jumpFlag = true;
         return immed;
      }

      // rs = rt << shamt
      else if(inst.equals("sll")) {
       //  System.out.println("sll " + assemblyCode.get(2) + " << " + assemblyCode.get(3));
         int rt   = regMem[Helpers._RegNum(assemblyCode.get(2))];     
         int shamt = Integer.parseInt(assemblyCode.get(3));
         regMem[Helpers._RegNum(assemblyCode.get(1))] = rt << shamt;
         // Check if loadWord flag needed? else reset prev dest reg
         if(prevDestReg != -1) {
            System.out.format("prevRd = %d rt = %d\n",prevDestReg,Helpers._RegNum(assemblyCode.get(2)));
            loadWordFlag = (prevDestReg == Helpers._RegNum(assemblyCode.get(2))) ? true:false;
            prevDestReg = -1;
         }
         return (PC+1);
      }

      else if (inst.equals("lw")){
         //System.out.println("lw " + assemblyCode.get(1) + " = Mem[ " + assemblyCode.get(2) + " + " + assemblyCode.get(3) + "]");
         int rt    = regMem[Helpers._RegNum(assemblyCode.get(1))];
         int immed = Integer.parseInt(assemblyCode.get(2));
         int rs    = regMem[Helpers._RegNum(assemblyCode.get(3))];
         rt = regMem[rs + immed];
         prevDestReg = rs + immed; // Index of destination register
         return (PC+1);
      }
      
      else if (inst.equals("sw")){
        // System.out.println("sw " + "Mem[ " + assemblyCode.get(2) + " + " + assemblyCode.get(3) + "] = " + assemblyCode.get(1)); 
         int rt    = regMem[Helpers._RegNum(assemblyCode.get(1))];
         int immed = Integer.parseInt(assemblyCode.get(2));
         int rs    = regMem[Helpers._RegNum(assemblyCode.get(3))]; 
         rt = regMem[rs + immed];
         // Check if loadWord flag needed? else reset prev dest reg
         if(prevDestReg != -1) {
            System.out.format("prevRd = %d rt = %d, rs = %d\n",prevDestReg,Helpers._RegNum(assemblyCode.get(1)),Helpers._RegNum(assemblyCode.get(3)));
            loadWordFlag = (prevDestReg == Helpers._RegNum(assemblyCode.get(1))) ? true:
            ((prevDestReg == (Helpers._RegNum(assemblyCode.get(3)))) ? true:false);
            prevDestReg = -1;
         }
         return (PC+1);
      }

      return PC;
   }
}   
