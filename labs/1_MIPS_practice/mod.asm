#      private static final String PROMPT_TITLE = 
#         "Fast Mod Program.\n " +
#         "Compute A % B";
#
#      // Setting up I/O
#      Scanner in = new Scanner(System.in);
#      System.out.println( PROMPT_TITLE ); 
#
#      // Get A, B from user
#      System.out.print( "\nEnter A: "); 
#      int a = in.nextInt();
#
#      System.out.print( "\nEnter B: "); 
#      int b = in.nextInt();
#
#      System.out.println("\nYou  entered integer " + a);
#
#      // Compute Power of two for B
#      int power = 0;
#      if ( b !=  1 ) {
#         int b_shifted = b;
#         for(power = 0; b_shifted > 1; power++) {
#            b_shifted = b_shifted >> 1;
#         } 
#      }
#      System.out.println( b + " = 2 ^ " + power );
#
#      // Compute Mode A % B = mod
#
#      // 1) A/B = C
#      int c = a >> power;
#      
#      // 2_ a - b * c = mod
#      int mod = a - (c << power) ; 
#      
#      // print result
#      System.out.print( "Result: " + mod + "\n" ); 

.globl welcome
.globl prompt
.globl sumText

# Data Area (this area contains strings to be displayed during the program)
.data

welcome:
   .asciiz " Fast Mod Program \n Compute A % B\n"

prompt:
   .asciiz " Enter an integer: "

modText:
   .asciiz " \n Mod = "

# Text Area (instructions)
.text

main:
   # Setting up I/O
   ori   $v0, $0, 4
