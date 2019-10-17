import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.io.*;

import java.lang.*;

public class Helpers {

   public static String getRegBinary( String regStr ) {
   // convert register string to binary
      String regNum  = Integer.parseInt(regStr[2:]);
      return Integer.toBinaryString( regNum ); 
   }
}
