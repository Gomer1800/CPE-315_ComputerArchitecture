# Name: Luis Gomez, Yu Asai
# Section: 1
# Description: Reverses the order of the bits
#
#    System.out.print(" This program reverses a number\n\n");
#    System.out.print(" Enter a 32 bit int: ");
#    Scanner in = new Scanner(System.in);
#    long i = in.nextLong();
#    int a = 0;
#    long result = 0;
#    long temp = 0;
#    while (a<31)
#    {
#         temp = i & 1;
#         result = result | temp;
#         i>>>= 1;
#         result <<= 1;
#         a++;
#    }
#    System.out.println(" \n Result = "+result);
#  }
#}
# $s0 is i
# $t0 is a
# $t1 is result
# $t3 is temp


# declare global so programmer can see actual addresses.
.globl welcome
.globl prompt
.globl sumText

#  Data Area (this area contains strings to be displayed during the program)
.data

welcome:
  .asciiz " This program reverses a number\n\n"

prompt:
  .asciiz " Enter a 32 bit int: "

sumText:
	.asciiz " \n Result = "

#Text Area (i.e. instructions)
.text

main:

	# Display the welcome message (load 4 into $v0 to display)
  # " This program reverses a number\n\n"
  ori     $v0, $0, 4

	# This generates the starting address for the welcome message.
	# (assumes the register first contains 0).
	lui     $a0, 0x1001
	syscall

	# Display prompt
	ori     $v0, $0, 4

	# This is the starting address of the prompt (notice the
	# different address from the welcome message)
  # " Enter a 32 bit int: "
	lui     $a0, 0x1001
	ori     $a0, $a0,0x22
	syscall

	# Read 1st integer from the user (5 is loaded into $v0, then a syscall)
	ori     $v0, $0, 5
	syscall

	# Clear $s0 for first val
	ori     $s0, $0, 0
	addu    $s0, $v0, $s0

  # int a = 0, result = 0, temp = 0
  ori $t0, $0, 0
  ori $t1, $0, 0

  #while (a<31)
  loop: slti $t2 $t0 31
  beq $t2, $0, end

  #inside while loop
	andi $t3, $s0, 1
  or $t1, $t1, $t3
  srl $s0, $s0, 1
  sll $t1, $t1, 1
  addi $t0, $t0, 1
  j loop

  #end
	# Display the result text
	end: ori     $v0, $0, 4
	lui     $a0, 0x1001
	ori     $a0, $a0,0x38
	syscall

	# Display the sum
	# load 1 into $v0 to display an integer
	ori     $v0, $0, 1
	addu 	 $a0, $t1, $0
	syscall

	# Exit (load 10 into $v0)
	ori     $v0, $0, 10
	syscall
