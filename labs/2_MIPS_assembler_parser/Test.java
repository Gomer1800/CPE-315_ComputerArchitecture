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
      myList.add(" label: lw $a3, 32($3) ");
      myList.add("    # add $s0, $t1 ,  $v0");

      // init filter object
      Mips_Filter myFilter = new Mips_Filter();

      myFilter.set_list( myList );
      
      myFilter.tokenize();

      myListList = myFilter.get_list();
 
      // Compare Filter Results
      myFilter.print();

      // Testing 5bit unsigned
      String regStr = myListList.get(1).get(1);
      int regNum = Helpers._RegNum(regStr);
      System.out.println("\nRegister: " + regStr + " in 5bit-unsigned: " + Helpers._5bit_unsigned(regNum));
      
      // Testing 16bit signed
      int someNum = 31;
      String addressStr = myListList.get(2).get(3);
      int addressNum = Integer.parseInt(addressStr);
      int distance = someNum - addressNum;
      System.out.println(
         "\nAddress Distance (" + Integer.toString(someNum) + " - " + addressStr +
            ") = " + Integer.toString(someNum-addressNum) +
         " in 16bit-signed: " + Helpers._16bit_signed(distance));

      // Compare Filter Results
      myFilter.print();
   }
}
