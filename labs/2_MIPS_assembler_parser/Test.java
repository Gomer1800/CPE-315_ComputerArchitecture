import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Test {
   
   public static void main(String [] args) {

      // test array list
      List<String> myList =  new ArrayList<String>();   
      List<List<String>> myListList;

      myList.add("# Luis is so cool!");
      myList.add("main: ");
      myList.add(" add $s5, $t1 ,  $v0 # another comment");
      myList.add(" ");
      myList.add(" label: add $s7, $t1 ,  $v0 # another comment");
      myList.add("    # add $s0, $t1 ,  $v0");

      // init filter object
      Mips_Filter myFilter = new Mips_Filter();

      myFilter.set_list( myList );
      
      myFilter.tokenize();

      myListList = myFilter.get_list();
 
      // Testing helper
      String reg = myListList.get(1).get(1);
      System.out.println("\nRegister: " + reg + " in binary: " + Helpers._5bRegNum(reg));
      myFilter.print();
   }
}
