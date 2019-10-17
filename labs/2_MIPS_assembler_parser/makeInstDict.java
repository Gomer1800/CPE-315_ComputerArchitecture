import java.util.*;

public class makeInstDict {
   private Map<String, String> instDict = new HashMap<String, String>();

   public void create(){
      /*and, or, add, addi, sll, sub, slt, beq, bne, lw, sw, j, jr, and jal */
      instDict.put("and", "100100");
      instDict.put("or", "100101");
      instDict.put("add", "100000");
      instDict.put("addi", "001000");
      instDict.put("sll",  "000000");
      instDict.put("sub", "100010");
      instDict.put("slt", "101010");
      instDict.put("beq", "000100");
      instDict.put("bne", "000101");
      instDict.put("lw", "100011");
      instDict.put("sw", "101011");
      instDict.put("j", "000010");
      instDict.put("jr", "001000");
      instDict.put("jal", "000011");
   }

   public Map<String, String> getInstDict() {
      return this.instDict;
   }

   public void setInstDict(Map<String, String> instDict) {
      this.instDict = instDict;
   }
}
