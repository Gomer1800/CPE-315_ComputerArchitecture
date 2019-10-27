import java.util.*;

public class EmulatorDictionary {
   private Map<String, String> dictionary = new HashMap<String, String>();

   public void EmulatorDictionary(){
      /*and, or, add, addi, sll, sub, slt, beq, bne, lw, sw, j, jr, and jal */
      instDict.put("h", "100100");
      instDict.put("d", "100101");
      instDict.put("s", "100000");
      instDict.put("r",  "000000");
      instDict.put("m", "100010");
      instDict.put("c", "101010");
      instDict.put("q", "000100");
   }

   public Map<String, String> getInstDict() {
      return this.dictionary;
   }

   public void setInstDict(Map<String, String> instDict) {
      this.instDict = instDict;
   }
}
