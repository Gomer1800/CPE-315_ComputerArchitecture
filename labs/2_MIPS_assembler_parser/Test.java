import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Test {
   
   public static void main(String [] args) {

      // test array list
      List<String> myList =  new ArrayList<String>();   

      myList.add("# Luis is so cool!");
      myList.add("");
      myList.add(" add $s0, $t1 ,  $v0 # another comment");
      myList.add(" ");
      myList.add("    # add $s0, $t1 ,  $v0");

      // init filter object
      Mips_Filter myFilter = new Mips_Filter();

      myFilter.set_list( myList );
      
      myFilter.tokenize();

      myFilter.print();
   }
}
