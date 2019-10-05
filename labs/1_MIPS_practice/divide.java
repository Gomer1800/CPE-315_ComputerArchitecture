/*
 * Compute power of two
 * counter = 0
 * Loop ( Num > 1? ):
 * True: increment counter, shift right 
 * False: Counter value is power
 *
 * Compute quotient A/B = div
 * C = power of two for B
 * div = A >> C
 *
 */ 

import java.util.Scanner;

public class divide {

private static final String PROMPT_TITLE = 
   "Divide Program.\n " +
   "Compute A % B";

   public static void main(String [] args) {
      // Setting up I/O
      Scanner in = new Scanner(System.in);
      System.out.println( PROMPT_TITLE ); 

      // Get A, B from user
      System.out.print( "\nEnter A Upper: "); 
      int aUpper = in.nextInt();

      System.out.print( "\nEnter A Lower: "); 
      int aLower = in.nextInt();

      System.out.println("\nYou  entered A: " + Integer.toString(aUpper) + "," + Integer.toString(aLower));

      System.out.print( "\nEnter B Upper: "); 
      int bUpper = in.nextInt();

      System.out.print( "\nEnter B Lower: "); 
      int bLower = in.nextInt();

      System.out.println("\nYou  entered B: " + Integer.toString(bUpper) + "," + Integer.toString(bLower));

      // Compute Power of two for B
      int power = 0;
      if ( b !=  1 ) {
         int b_shifted = b;
         for(power = 0; b_shifted > 1; power++) {
            b_shifted = b_shifted >> 1;
         } 
      }
      System.out.println( b + " = 2 ^ " + power );
   }
}
