import java.util.*;
import java.lang.*;

public class Mips_Parser {
   public List<List<String>> tokensAddress = new ArrayList<List<String>>();
   public Map<String, Integer> labelDict = new HashMap<String, Integer>();
   
   public void addressArr(List<List<String>> tokenList, 
              Map<String, String> instDict) {
      int j = 0;
      for (int i = 0; i < tokenList.size(); i++){
         List<String> instLine = tokenList.get(i);
         /* if instruction => put the whole tokenList.get(i) in the new array */
         /* increment the index j */
         if (instDict.containsKey(instLine.get(0))) {
            tokensAddress.add(instLine);
            labelJump(j);
            j++;
         }   
         /* if not instruction check ... if label, else junk */
         else {
            /* if label, it has ":" as the last character of 0th index*/
            /*char lastChar = instLine.get(0).charAt(instLine.get(0).length() - 1);*/
            
            int colon; 
            if ((colon = instLine.get(0).indexOf(":")) != -1) {    /* is  label */
               instLine.set(0,instLine.get(0).substring(0,colon));
               labelDict.put(instLine.get(0), j);
               if (tokenList.get(i).size() > 1) {              /* check 1st index: instruction or junk ? */
                  if (instDict.containsKey(instLine.get(1))) {   /*instruction*/
                     instLine.remove(0);                  /* remove the label from the sublist */ 
                     tokensAddress.add(instLine);      /*add the rest to tokensAddress */      
                     labelJump(j);
                     j++;
                  }
                  else {                                      /* invalid instruction */                                  
                     String junk = instLine.get(1);
                     invalidInst(junk);
                     break;        
                  }     
               }
            }   
           else {  /* 0th index junk */
               String junk = instLine.get(0);
               invalidInst(junk);
               break;
            }
         }
      }
   }
   
   public void invalidInst(String junk) {
       String errPrint = "invalid instruction: ";
       String errMessage = errPrint + junk;
       List<String> err = new ArrayList<String>();
       err.add(errMessage);
       tokensAddress.add(err);
   }
   public void labelJump(int currentIndex) {
      String inst = tokensAddress.get(currentIndex).get(0);
     /* if (labelDict.containsKey(tokenLine.get(3))) {*/
      if (inst.equals("beq")) {
         difference(currentIndex, 3);
      }
      else if (inst.equals("bne")) {
         difference(currentIndex, 3);
      }
      else if (inst.equals("j") | inst.equals("jal")) {
         String label = tokensAddress.get(currentIndex).get(1);
         int labelAddress = labelDict.get(label);
         //difference(currentIndex, 1); 
         tokensAddress.get(currentIndex).set(1,Integer.toString(labelAddress));
      }      
   }

   public void difference(int currentIndex, int labelIndex) {
      String label = tokensAddress.get(currentIndex).get(labelIndex);
      int labelAddress = labelDict.get(label);
      if (labelAddress < currentIndex) {
         int diff = ((currentIndex + 1) - labelAddress)* -1;
         tokensAddress.get(currentIndex).set(labelIndex,Integer.toString(diff));
      }
      else {
         int diff = (currentIndex - labelAddress);
         tokensAddress.get(currentIndex).set(labelIndex,Integer.toString(diff));
      } 
   }   

   public Map<String, Integer> getlabelDict() {
      return this.labelDict;
   }

   public List<List<String>> gettokensAddress() {
      return this.tokensAddress;
   }

   public void setlabelDict(Map<String, Integer> labelDict) {
      this.labelDict = labelDict;
   }

   public void settokensAddress(List<List<String>> tokensAddress) {
      this.tokensAddress = tokensAddress;
   }
}   
