import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.io.*;

import java.lang.*;

public class Helpers {

   public static String _RegBinary(int regNum, int numBits) {
      return String.format("%5s", Integer.toBinaryString(regNum)).replace(' ', '0');
   }

   public static int _RegNum( String regStr ) {
   // Finds matching register and returns Reg Num,
   // returns -1 if error
      if( regStr.equals("$zero") ) { return  0; }

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
