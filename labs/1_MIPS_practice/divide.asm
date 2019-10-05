# Name: Luis Gomez, Yu Asai
# Section: 1
# Description: divides a 64-bit number by a 31-bit number
#
#    System.out.print(" This program divides 2 number\n\n");
#    System.out.print(" Enter a 32 bit int: ");
#    Scanner in1 = new Scanner(System.in);
#    long a1 = in1.nextLong();
#    System.out.print(" Enter a 32 bit int: ");
#    Scanner in2 = new Scanner(System.in);
#    long a2 = in2.nextLong();
#    System.out.print(" Enter a 31 bit int: ");
#    Scanner in3 = new Scanner(System.in);
#    int b = in3.nextInt();
#    int x = 0;
#    long result = 0;
#    long temp = 0;
#    long mask1 = 1;
#    long maskF = 2147483648L; //only 1 for MSB
#    long t1;
#    while (b != 1)
#    {
#        t1 = a1 & 1;
#        a1>>>=1;
#        a2>>>=1;
#        if (t1 == 1){
#          a2 |= maskF;
#        }
#        b>>>=1;
#    }
#    System.out.println(" \n Result = "+a1 + ", " + a2);

# declare global so programmer can see actual addresses.
.globl welcome
.globl prompt
.globl sumText

#  Data Area (this area contains strings to be displayed during the program)
.data

welcome:
  .asciiz " This program divides 2 32-bit numbers with a 31-bit divisor\n\n"

prompt:
  .asciiz " Enter 1st 32-bit: "
  .asciiz " Enter 2st 32-bit: "
  .asciiz " Enter a div: "

sumText:
	.asciiz " \n Result = "


#Text Area (i.e. instructions)
.text

main:

	# Display the welcome message (load 4 into $v0 to display)
  # " This program divides 2 32-bit numbers with a 31-bit divisor\n\n"
  ori     $v0, $0, 4

	# This generates the starting address for the welcome message.
	# (assumes the register first contains 0).
	lui     $a0, 0x1001
	syscall

	# Display prompt
	ori     $v0, $0, 4

	# This is the starting address of the prompt (notice the
	# different address from the welcome message)
  # " Enter 1st 32-bit: "
	lui     $a0, 0x1001
	ori     $a0, $a0,0x3F
	syscall

	# Read 1st integer from the user (5 is loaded into $v0, then a syscall)
	ori     $v0, $0, 5
	syscall

	# Clear $s0 for first 32-bit dividend
	ori     $s0, $0, 0
	addu    $s0, $v0, $s0

	# Display prompt (4 is loaded into $v0 to display)
  # " Enter 2nd 32-bit: "
	ori     $v0, $0, 4
	lui     $a0, 0x1001
	ori     $a0, $a0,0x53
	syscall

	# Read 2nd integer
	ori	$v0, $0, 5
	syscall

	# $v0 now has the value of the second integer

  # Clear $s1 for second 32-bit dividend
  ori     $s1, $0, 0
	addu    $s1, $v0, $s1

	# Display " Enter a div: "
	ori     $v0, $0, 4
	lui     $a0, 0x1001
	ori     $a0, $a0,0x67
	syscall

  # Read div
  ori	$v0, $0, 5
  syscall

  # Clear $s2 for the divisor
  ori     $s2, $0, 0
	addu    $s2, $v0, $s2

  #temporary values
  addiu   $t5, $t5, 1
  lui   $t6, 32768

  #while (b!= 1)
  loop: beq $s2, $t5, end

  #inside while loop
  and $t1, $s0, $t5
  srl $s0, $s0, 1
  srl $s1, $s1, 1
  beq $t1, $t5, mask
  back: srl $s2, $s2, 1
  j loop

  mask: or $s1, $s1, $t6
  j back

  #end
  # Display the result text
  end: ori     $v0, $0, 4
  lui     $a0, 0x1001
  ori     $a0, $a0,0x77
  syscall

	# load $s0 into $v0 to display an integer
  ori     $v0, $0, 1
	add 	$a0, $s0, $0
	syscall
  
  # Display the result text
  ori     $v0, $0, 4
  lui     $a0, 0x1001
  ori     $a0, $a0,0x77
  syscall

  #load $s1 into $v0 to display an integer
  ori     $v0, $0, 1
  add 	$a0, $s1, $0
  syscall

	# Exit (load 10 into $v0)
	ori     $v0, $0, 10
	syscall
