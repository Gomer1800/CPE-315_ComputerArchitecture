import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.io.*;

import java.lang.*;

public class Helpers {

   public static String _5bRegNum(String regStr) {
   // convert register string to binary
      int regNum  = Integer.parseInt(regStr.substring(2));
      return String.format("%5s", Integer.toBinaryString(regNum)).replace(' ', '0');
   }
}
