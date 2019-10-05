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
.globl promptA
.globl promptB
.globl newline
.globl modText

# Data Area (this area contains strings to be displayed during the program)
.data

# 0
welcome:
   .asciiz " Fast Mod Program \n Compute A % B \n\n"
# 37 
promptA:
   .asciiz " Enter integer A: "
# 56
promptB:
   .asciiz " Enter integer B: "
# 75
newline:
   .asciiz "\n"
# 77
modText:
   .asciiz " \n Mod = "

# Text Area (instructions)
.text

main:
   # Welcome message
   ori $v0, $0, 4
	# reset a0 to 0 index
   lui $a0, 0x1001
   syscall

   # Prompt A
   ori $v0, $0, 4
   lui $a0, 0x1001
   ori $a0, $a0, 37
   syscall
	
   # Get A
   ori $v0, $0, 5
   syscall
	add $s0, $s0, $v0

   # Prompt B
   ori $v0, $0, 4
   lui $a0, 0x1001
   ori $a0, $a0, 56
   syscall
	
   # Get B
   ori $v0, $0, 5
   syscall
	add $s1, $s1, $v0

	# Display A
   ori $v0, $0, 1
   lui $a0, 0x1001
   add $a0, $s0, $0 
   syscall

   # newline
   ori $v0, $0, 4
   lui $a0, 0x1001
   ori $a0, $a0, 75
   syscall

	# Display A
   ori $v0, $0, 1
   lui $a0, 0x1001
   add $a0, $s1, $0
   syscall
   
   # Compute Power of two for B
   # s2 <- int power = 0;
	and $s2, $s2, $0

   # if ( b - 1 != 0 ) {
	# t0 <- b - 1
   addi $t0, $s1, -1
   beq $t0, $0, cont
 
	# $t0 <- int b_shifted = b;
   and $t0, $s1, $s1
   and $t1, $s1, $s1

power:
	# for(power = 0; b_shifted > 1; power++) {	
	# 	b_shifted = b_shifted >> 1;
   srl $t0, $t0, 1
   addi $s2, $s2, 1	
   addi $t1, $t0, -1
   blez $t1, cont
	j power

cont:

   # newline
   ori $v0, $0, 4
   lui $a0, 0x1001
   ori $a0, $a0, 75
   syscall

	# Display Power
   ori $v0, $0, 1
   lui $a0, 0x1001
   add $a0, $s2, $0
   syscall

    # Exit (load 10 into $v0)
   ori $v0, $0, 10
   syscall
