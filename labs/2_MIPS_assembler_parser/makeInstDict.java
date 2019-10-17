import java.util.*;

public class makeInstDict {
   private Map<String, List<String>> instDict = new HashMap<String, List<String>>();

   public void create(){
      /*and, or, add, addi, sll, sub, slt, beq, bne, lw, sw, j, jr, and jal */
      /* List for a value of dictionary: [format, type, binary code] */
      List<String> and =  Arrays.asList("R", "funct","100100");
      List<String> or =   Arrays.asList("R", "funct","100101");
      List<String> add =  Arrays.asList("R", "funct","100000");
      List<String> addi = Arrays.asList("I",   "op", "001000");
      List<String> sll =  Arrays.asList("R", "funct","000000");
      List<String> sub =  Arrays.asList("R", "funct","100010");
      List<String> slt =  Arrays.asList("R", "funct","101010");
      List<String> beq =  Arrays.asList("I",    "op","000100");
      List<String> bne =  Arrays.asList("I",    "op","000101");
      List<String> lw =   Arrays.asList("I",    "op","100011");
      List<String> sw =   Arrays.asList("I",    "op","101011");
      List<String> j =    Arrays.asList("J",    "op","000010");
      List<String> jr =   Arrays.asList("R", "funct","001000");
      List<String> jal =  Arrays.asList("J",    "op","000011");
   
      instDict.put("and", and);
      instDict.put("or", or);
      instDict.put("add", add);
      instDict.put("addi", addi);
      instDict.put("sll", sll);
      instDict.put("sub", sub);
      instDict.put("slt", slt);
      instDict.put("beq", beq);
      instDict.put("bne", bne);
      instDict.put("lw", lw);
      instDict.put("sw", sw);
      instDict.put("j", j);
      instDict.put("jr", jr);
      instDict.put("jal", jal);
   }

   public Map<String, List<String>> getInstDict() {
      return this.instDict;
   }

   public void setInstDict(Map<String, List<String>> instDict) {
      this.instDict = instDict;
   }
}
