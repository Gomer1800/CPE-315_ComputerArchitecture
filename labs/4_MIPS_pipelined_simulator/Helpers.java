import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.io.*;

import java.lang.*;

public class Helpers {

   public static String _16bit_signed( int num ) {
   // positive number binary string
      String binary = String.format("%16s",
         Integer.toBinaryString(Math.abs(num))).replace(' ', '0');
      // System.out.println("which num? " + Integer.toString(num));
      if( num >= 0)
         return binary;
      else {
         StringBuffer buff = new StringBuffer(binary);
         return Helpers.findTwoscomplement(buff);
      }
   }
 
   public static String _26bit_signed( int num ) {
   // positive number binary string
      String binary = String.format("%26s",
         Integer.toBinaryString(Math.abs(num))).replace(' ', '0');
      // System.out.println("which num? " + Integer.toString(num));
      if( num >= 0)
         return binary;
      else {
         StringBuffer buff = new StringBuffer(binary);
         return Helpers.findTwoscomplement(buff);
      }
   }

   private static String findTwoscomplement(StringBuffer str) 
    { 
        int n = str.length(); 
        // System.out.println("what size n? " + Integer.toString(n));
       
        // Traverse the string to get first '1' from 
        // the last of string 
        int i; 
        for (i = n-1 ; i >= 0 ; i--) 
            if (str.charAt(i) == '1') 
                break; 
       
        // If there exists no '1' concat 1 at the 
        // starting of string 
        if (i == -1) 
            return "1" + str; 
       
        // Continue traversal after the position of 
        // first '1' 
        for (int k = i-1 ; k >= 0; k--) 
        { 
            //Just flip the values 
            if (str.charAt(k) == '1') 
                str.replace(k, k+1, "0"); 
            else
                str.replace(k, k+1, "1"); 
        } 
       
        // return the modified string 
        return str.toString(); 
    }

   public static String _5bit_register_decoder(String reg) {
      int regNum = Helpers._RegNum(reg); 
      return _5bit_unsigned(regNum);
   }

   public static String _5bit_unsigned(int num) {
   // returns a 5bit binary string representation of regNum
      return String.format("%5s", Integer.toBinaryString(num)).replace(' ', '0');
   }

   public static int _RegNum( String regStr ) {
   // Finds matching register and returns Reg Num,
   // returns -1 if error
      // System.out.println("Which reg? " + regStr);
      if( regStr.equals("$zero") | regStr.equals("$0")) { return  0; }

      if( regStr.equals("$v0") ) { return  2; }
      if( regStr.equals("$v1") ) { return  3; }

      if( regStr.equals("$a0") ) { return  4; }
      if( regStr.equals("$a1") ) { return  5; }
      if( regStr.equals("$a2") ) { return  6; }
      if( regStr.equals("$a3") ) { return  7; }

      if( regStr.equals("$t0") ) { return  8; }
      if( regStr.equals("$t1") ) { return  9; }
      if( regStr.equals("$t2") ) { return  10; }
      if( regStr.equals("$t3") ) { return  11; }
      if( regStr.equals("$t4") ) { return  12; }
      if( regStr.equals("$t5") ) { return  13; }
      if( regStr.equals("$t6") ) { return  14; }
      if( regStr.equals("$t7") ) { return  15; }
      
      if( regStr.equals("$s0") ) { return  16; }
      if( regStr.equals("$s1") ) { return  17; }
      if( regStr.equals("$s2") ) { return  18; }
      if( regStr.equals("$s3") ) { return  19; }
      if( regStr.equals("$s4") ) { return  20; }
      if( regStr.equals("$s5") ) { return  21; }
      if( regStr.equals("$s6") ) { return  22; }
      if( regStr.equals("$s7") ) { return  23; }

      if( regStr.equals("$t8") ) { return  24; }
      if( regStr.equals("$t9") ) { return  25; }

      if( regStr.equals("$gp") )   { return 28; }

      if( regStr.equals("$sp") )   { return 29; }

      if( regStr.equals("$fp") )   { return 30; }

      if( regStr.equals("$ra") )   { return 31; }

      // Invalid Reg Num
      return -1;
   }
}
