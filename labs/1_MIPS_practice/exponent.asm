#      private static final String PROMPT_TITLE = 
#         "Exponent Program.\n " +
#         "Compute A ^ B";
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
#		 if (A == 1 || B == 0) { total = 1 }
#		 else if (B == 1) { total = A }
#		 else {
#      total = b
#      for(power = B-1; power > 0; power--) {	
#      	 
#      	for(times = A-1; times > 1; times--) {	
# 	    		total += total
#		 }
#      // print result
#      System.out.print( "Exp = " + total + "\n" ); 

.globl welcome
.globl promptA
.globl promptB
.globl expText

# Data Area (this area contains strings to be displayed during the program)
.data

# 0
welcome:
   .asciiz "Exponent Program \nCompute A ^ B \n\n"
# 35
promptA:
   .asciiz "\nEnter integer A: "
# 54
promptB:
   .asciiz "\nEnter integer B: "
# 73
expText:
   .asciiz "\nExp = "

# Text Area (instructions)
.text

main:
   # Welcome message
   ori $v0, $0, 4
	# reset a0 to 0 index
   lui $a0, 0x1001
   syscall

	# total = 0
	and $s2, $0, $0

   # Prompt A
   ori $v0, $0, 4
   lui $a0, 0x1001
   ori $a0, $a0, 35
   syscall
	
   # Get A
   ori $v0, $0, 5
   syscall
	add $s0, $s0, $v0

   # Prompt B
   ori $v0, $0, 4
   lui $a0, 0x1001
   ori $a0, $a0, 54
   syscall
	
   # Get B
   ori $v0, $0, 5
   syscall
	add $s1, $s1, $v0

	# case A is One
	addi $t1, $s0, -1
	blez $t1, one

	# case B ^ 0 
	beq $s1, $0, zero

	# case B ^ 1
	addi $t0, $s1, -1
	blez $t0, one

	add $s2, $s0, $0
	addi $s1, $s1, -1
outer:
	# for(power = B-1; power > 0; power--) {	
	blez $s1, exit
	addi $s1, $s1, -1

	and $t0, $s0, $s0
	addi $t0, $t0, -1
inner:
	# for(times = A; times > 1; times--) {	
	# 	total += total
	blez $t0, outer	
	add $s2, $s2, $s2
	addi $t0, $t0, -1
	j inner
		
zero:
	addi $s2, $s2, 1
	j exit

one:
	add $s2, $s2, $s0
	
exit:
   # Prompt Exp = ?
   ori $v0, $0, 4
   lui $a0, 0x1001
   ori $a0, $a0, 73
   syscall

	# print result
   ori $v0, $0, 1
   lui $a0, 0x1001
   add $a0, $s2, $0
   syscall

    # Exit (load 10 into $v0)
   ori $v0, $0, 10
   syscall
