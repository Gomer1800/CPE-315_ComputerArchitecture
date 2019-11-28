import java.util.*;
import java.lang.*;

public class Mips_Parser {
   public List<List<String>> _AssemblyCode = new ArrayList<List<String>>();
   public Map<String, Integer> labelDict = new HashMap<String, Integer>();
   public List<Integer> instructionWithLabels = new ArrayList<Integer>();

   
   public void addressArr(List<List<String>> tokenList, 
              Map<String, String> instDict) {
      int j = 0;
      int colon = 0;
      for (int i = 0; i < tokenList.size(); i++){
         List<String> instLine = tokenList.get(i);
         /* if instruction => put the whole tokenList.get(i) in the new array */
         /* increment the index j */
         if (instDict.containsKey(instLine.get(0))) {
            String inst = instLine.get(0);                        
            saveLabelJumpIndex(inst,j);
            _AssemblyCode.add(instLine);
            j++;
         }   
         else if ((colon = instLine.get(0).indexOf(":")) != -1) {  //contains colon -> label
            String label = instLine.get(0).substring(0,colon);
            if (colon != (instLine.get(0).length()-1)) {   // if colon index is not the last char in 0th index
               String instructionOrjunk = instLine.get(0).substring(colon+1,instLine.get(0).length());           
               if (instDict.containsKey(instructionOrjunk) == false) { // is junk
                  invalidInst(instructionOrjunk);
                  break;
               }
               else { // is instruction
                  labelDict.put(label,j);  //save label to the label Dictionary 
                  instLine.set(0,instructionOrjunk);  // set the inst to 0th index 
                  saveLabelJumpIndex(instructionOrjunk,j);
                  _AssemblyCode.add(instLine);
                  j++;
               }
            }              
            else if (instLine.size() > 1) {   // test1: add $s0, $s0, $t0
               String instructionOrjunk = instLine.get(1);
               if (instDict.containsKey(instructionOrjunk) == false) { // is junk
                  invalidInst(instructionOrjunk);
                  break;
               }
               else {                        // is instruction
                  labelDict.put(label,j);  //save label to the label Dictionary 
                  saveLabelJumpIndex(instructionOrjunk,j);
                  instLine.remove(0);                  /* remove the label from the sublist */
                  _AssemblyCode.add(instLine);      /*add the rest to _AssemblyCode */
                  j++;
               }
            }
            else {   //just label
               labelDict.put(label,j);
            }
         }
         else {
            invalidInst(instLine.get(0));
            break;
         }
      }     
   }

   public void invalidInst(String junk) {
       String errPrint = "invalid instruction: ";
       String errMessage = errPrint + junk;
       List<String> err = new ArrayList<String>();
       err.add(errMessage);
       _AssemblyCode.add(err);
   }
   

   public void saveLabelJumpIndex(String inst, int newArrIndex) {
      if (inst.equals("beq") | (inst.equals("bne")) | (inst.equals("j")) | inst.equals("jal")){ 
         instructionWithLabels.add(newArrIndex);    
      }
   }
   

   public void labelJump(int currentIndex) {
      String inst = _AssemblyCode.get(currentIndex).get(0);
     /* if (labelDict.containsKey(tokenLine.get(3))) {*/
      if (inst.equals("beq")) {
         difference(currentIndex, 3);
      }
      else if (inst.equals("bne")) {
         difference(currentIndex, 3);
      }
      else if ((inst.equals("j")) | (inst.equals("jal"))) {
         String label = _AssemblyCode.get(currentIndex).get(1);
         int labelAddress = labelDict.get(label);
         _AssemblyCode.get(currentIndex).set(1,Integer.toString(labelAddress));
      }      
   }
   
   public void difference(int currentIndex, int labelIndex) {
      String label = _AssemblyCode.get(currentIndex).get(labelIndex);
      int diff;
      int labelAddress = labelDict.get(label);
      if (labelAddress < currentIndex) {
         diff = ((currentIndex + 1) - labelAddress)* -1;
         _AssemblyCode.get(currentIndex).set(labelIndex,Integer.toString(diff));
     }
      else {
         diff = (labelAddress - (currentIndex + 1));
         _AssemblyCode.get(currentIndex).set(labelIndex,Integer.toString(diff));
      }
   }   
   
   //loop through the List instructionWithLabels, replace the label names with the difference */
   public void setDifference() {
      for (int i = 0; i < instructionWithLabels.size(); i++){
         int index = instructionWithLabels.get(i);
         labelJump(index);     
      }   
   }

   public Map<String, Integer> getlabelDict() {
      return this.labelDict;
   }

   public List<List<String>> getAssemblyCode() {
      return this._AssemblyCode;
   }

   public void setlabelDict(Map<String, Integer> labelDict) {
      this.labelDict = labelDict;
   }

   public void set_AssemblyCode(List<List<String>> assemblyCode) {
      this._AssemblyCode = assemblyCode;
   }
}   
