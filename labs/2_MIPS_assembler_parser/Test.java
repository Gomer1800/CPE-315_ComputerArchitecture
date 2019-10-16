import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Test {
   
   public static void main(String [] args) {

      // test array list
      ArrayList<String> myList =  new ArrayList<>();   

      myList.add("An");
      myList.add(" ");
      myList.add("Array");
      myList.add(" ");
      myList.add("List");

      // init filter object
      Mips_Filter myFilter = new Mips_Filter();

      myFilter.assign_list( myList );
      myFilter.print();
   }
}
