/*
 * Compute power of two
 * Loop (Is first bit 0? ):
 * False: increment counter, shift right 
 * True: Counter value is power
 *
 * Compute Mod a % b = mod
 * 1) Compute a / b = c
 * 2) Compute a - b * c =  a % b
 */ 

import java.util.Scanner;

public class FastMod {

private static final String PROMPT_TITLE = 
   "Fast Mod Program.\n " +
   "Compute A % B";

   public static void main(String [] args) {
      // Setting up I/O
      Scanner in = new Scanner(System.in);
      System.out.println( PROMPT_TITLE ); 

      // Get A, B from user
      System.out.print( "\nEnter A: "); 
      int a = in.nextInt();
      System.out.print( "\nEnter B: "); 
      int b = in.nextInt();
      System.out.println("\nYou  entered integer " + a);

      // Compute Power of two for B
      int power = 0;
      if ( b !=  1 ) {
         int b_shifted = b;
         for(power = 0; b_shifted > 1; power++) {
            b_shifted = b_shifted >> 1;
         } 
      }
      System.out.println( b + " = 2 ^ " + power );

      // Compute Mode A % B = mod

      // 1) A/B = C
      int c = a >> power;
      
      // 2_ a - b * c = mod
      int mod = a - (c << power) ; 
      
      // print result
      System.out.print( "Result: " + mod + "\n" ); 
   }
}
