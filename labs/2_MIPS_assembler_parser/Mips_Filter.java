import java.util.ArrayList;
import java.util.List;

public class Mips_Filter {
   
   // ATTRIBUTES
   
   private ArrayList<String> _myList;

   // FUNCTIONS

   // setters
   public void assign_list( ArrayList<String> some_list){
      _myList = some_list;
   }
   
   // getters
   public ArrayList<String> get_List() {
      return _myList;
   }

   public void print() {
   // Print Array: prints array contents by stepping through
      for(int i=0; i < _myList.size(); i++)
         System.out.println( _myList.get(i) );  
   }

   private void filter_comments() {
   // Filter Comments
   }

   private void filter_blanks() {
   // Filter Blank Lines
   }
}
