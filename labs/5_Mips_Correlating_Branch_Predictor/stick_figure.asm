#Yu Asai and Luis Gomez
#Fall 2019 CPE 315 
#MIPS program that draws a stick figure
#.text

main: 
   #Circle(30,100,20) #head
   addi $a0,$0,30
   addi $a1,$0,100
   addi $a2,$0,20
   jal circle
   
   #Line(30,80,30,30) #body
   addi $a0,$0,30
   addi $a1,$0,80
   addi $a2,$0,30
   addi $a3,$0,30
   jal line
   
   #Line(20,1,30,30) #left leg
   addi $a0,$0,20
   addi $a1,$0,1
   addi $a2,$0,30
   addi $a3,$0,30
   jal line
   
   #Line(40,1,30,30) #right leg
   addi $a0,$0,40
   addi $a1,$0,1
   addi $a2,$0,30
   addi $a3,$0,30
   jal line
   
   #Line(15,60,30,50) #left arm
   addi $a0,$0,15
   addi $a1,$0,60
   addi $a2,$0,30
   addi $a3,$0,50
   jal line
   
   #Line(30,50,45,60) #right arm
   addi $a0,$0,30
   addi $a1,$0,50
   addi $a2,$0,45
   addi $a3,$0,60
   jal line
   
   #Circle(24,105,3) #left eye
   addi $a0,$0,24
   addi $a1,$0,105
   addi $a2,$0,3
   jal circle
   
   #Circle(36,105,3) #right eye
   addi $a0,$0,36
   addi $a1,$0,105
   addi $a2,$0,3
   jal circle
   
   #Line(25,90,35,90) #mouth center
   addi $a0,$0,25
   addi $a1,$0,90
   addi $a2,$0,35
   addi $a3,$0,90
   jal line
   
   #Line(25,90,20,95) #mouth left
   addi $a0,$0,25
   addi $a1,$0,90
   addi $a2,$0,20
   addi $a3,$0,95
   jal line
   
   #Line(35,90,40,95) #mouth right
   addi $a0,$0,35
   addi $a1,$0,90
   addi $a2,$0,40
   addi $a3,$0,95
   jal line
   
   j end1
   
#def Line(x0, y0, x1, y1)
line:
   #save $s0 = $a0
   add $s0,$a0,$0
   #save $s1 = $a1
   add $s1,$a1,$0
   #save $s2 = $a2
   add $s2,$a2,$0
   #save $s3 = $a3
   add $s3,$a3,$0
   
   #get t0 = abs(y1-y0)
   sub $t0,$s3,$s1  #y1-y0
   slt $t1,$t0,$0   #t0 < 0: 1 if neg, 0 if pos 
   beq $t1,$0,pos
   sub $t0,$0,$t0   #0-t0 to get pos 
     
pos:
   #get t1 = abs(x1-x0)
   sub $t1,$s2,$s0
   slt $t2,$t1,$0
   beq $t2,$0,pos1 
   sub $t1,$0,$t1

pos1:
   #if t1 < t0: t3 = 1; else: t3 = 0   st 
   slt $s7,$t1,$t0
   bne $s7,$0,one
   
back1: 
   #if x1 < x0
   #if s2 < s0: t4 = 1; else: t4 = 0 
   slt $t4,$s2,$s0
   bne $t4,$0,x0_g_x1
   
back:
   #li $v0,1
   #add $a0,$t3,$0
   #syscall
   #deltay = abs(y1-y0)
   sub $t0,$s3,$s1  #y1-y0
   slt $t1,$t0,$0   #t0 < 0: 1 if neg, 0 if pos 
   beq $t1,$0,pos2
   sub $t0,$0,$t0   #0-t0 to get pos 
   
pos2:
   #deltax = x1 - x0
   sub $t1,$s2,$s0
   #error = t2 = 0
   add $t2,$0,$0 
   #y = y0
   add $s6,$s1,$0
   #x1 + 1
   addi $s2,$s2,1
   
   #if y0 < y1 
   slt $t5,$s1,$s3
   bne $t5,$0,less
   #ystep  
   addi $t4,$0,-1
   j loop 

less:
   addi $t4,$0,1
   j loop
  
  # jr $ra
loop: 
   #for x from x0 to x1
   beq $s0,$s2, end
   
   #if st == 1 
   #addi $t7,$0,1
   bne $s7,$0,invplot
   #plot(x,y)
   #store x
   sw $s0,0($sp)
   addi $sp,$sp,1  #step to next array cell   
   #store y
   sw $s6,0($sp)
   addi $sp,$sp,1
   j err 

invplot:
   #plot(y,x)
   #store y
   sw $s6,0($sp)
   addi $sp,$sp,1 #step to next array cell   
   #store x
   sw $s0,0($sp)
   addi $sp,$sp,1 #step to next array cell
err:   
   #error = error + deltay
   add $t2,$t2,$t0 
   
   #if 2*error >= deltax -> 2*error < deltax 
   #addi $t9,$0,2

   #2*error
   sll $t6,$t2,1
   #2*error < deltax: t7 = 1 else t7 = 0
   slt $t7,$t6,$t1   
   beq $t7,$0,err2
   addi $s0,$s0,1
   j loop
   
   #2*error >= deltax
err2:
   #y = y + ystep
   add $s6,$s6,$t4
   #error = error - deltax
   #sub $t2,$t2,$t1
   #-deltax
   sub $t5,$0,$t1
   #error = error - deltax
   add $t2,$t2,$t5
   addi $s0,$s0,1
   j loop

one:
   #swap(x0, y0)
   add $a0,$s0,$0
   add $a1,$s1,$0
   sw $ra,0($sp)
   jal swap
   lw $ra,0($sp)
   add $s0,$s4,$0
   add $s1,$s5,$0

   #swap(x1, y1)
   add $a0,$s2,$0
   add $a1,$s3,$0
   sw $ra,0($sp)
   jal swap
   lw $ra,0($sp)
   add $s2,$s4,$0
   add $s3,$s5,$0
   j back1 

x0_g_x1:
   #swap(x0, x1)
   add $a0,$s0,$0
   add $a1,$s2,$0
   sw $ra,0($sp)
   jal swap 
   lw $ra,0($sp)
   add $s0,$s4,$0
   add $s2,$s5,$0
   
   #swap(y0, y1)
   add $a0,$s1,$0
   add $a1,$s3,$0
   sw $ra,0($sp)
   jal swap
   lw $ra,0($sp)
   add $s1,$s4,$0
   add $s3,$s5,$0
   j back 

swap:
   #temp = a0
   add $t2,$a0,$0
   #a0 = a1 
   add $a0,$a1,$0
   #a1 = temp
   add $a1,$t2,$0
   #save s4 = a0
   add $s4,$a0,$0
   #save s5 = a1
   add $s5,$a1,$0
   jr $ra

#Circle(xc,yc,r)
#array pointer at sp
circle:
   #s0 = a0 = xc
   add $s0,$a0,$0
   #s1 = a1 = yc
   add $s1,$a1,$0
   #s2 = a2 = r
   add $s2,$a2,$0

   #x = s3 = 0
   add $s3,$0,$0
   #y = s4 = r 
   add $s4,$s2,$0
   #g = s5 = 3 - 2*r
   sll $s5,$a2,1
   sub $s5,$0,$s5
   addi $s5,$s5,3
   #diagonalInc = s6 = 10 - 4*r
   sll $s6,$a2,2
   sub $s6,$0,$s6
   addi $s6,$s6,10
   #rightInc = s7 = 6
   addi $s7,$0,6
      
circleLoop:   
   #while x <= y
   #if y<x: 1; else 0
   slt $t9,$s4,$s3
   bne $t9,$0,end
   
   #-x
   sub $t8,$0,$s3
   
   #xc+x = t0
   add $t0,$s0,$s3
   #xc-x = t1
   add $t1,$s0,$t8
   #yc+x = t6
   add $t6,$s1,$s3
   #yc-x = t7
   add $t7,$s1,$t8
   
   #-y 
   sub $t8,$0,$s4
   #yc+y = t2
   add $t2,$s1,$s4
   #yc-y = t3
   add $t3,$s1,$t8
   #xc+y = t4
   add $t4,$s0,$s4
   #xc-y = t5
   add $t5,$s0,$t8


   #plot(xc+x,yc+y)
   #store xc+x
   sw $t0,0($sp)
   addi $sp,$sp,1  #step to next array cell   
   #store yc+y
   sw $t2,0($sp)
   addi $sp,$sp,1
   
   #plot(xc+x,yc-y)
   #store xc+x
   sw $t0,0($sp)
   addi $sp,$sp,1  
   #store yc-y
   sw $t3,0($sp)
   addi $sp,$sp,1 
   
   #plot(xc-x,yc+y)
   #store xc-x
   sw $t1,0($sp)
   addi $sp,$sp,1
   #store yc+y
   sw $t2,0($sp)
   addi $sp,$sp,1
   
   #plot(xc-x,yc-y)
   #store xc-x
   sw $t1,0($sp)
   addi $sp,$sp,1
   #store yc-y
   sw $t3,0($sp)
   addi $sp,$sp,1
   
   #plot(xc+y,yc+x)
   #store xc+y
   sw $t4,0($sp)
   addi $sp,$sp,1
   #store yc+x
   sw $t6,0($sp)
   addi $sp,$sp,1
   
   #plot(xc+y,yc-x)
   #store xc+y
   sw $t4,0($sp)
   addi $sp,$sp,1
   #store yc-x
   sw $t7,0($sp)
   addi $sp,$sp,1
   
   #plot(xc-y,yc+x)
   #store xc-y
   sw $t5,0($sp)
   addi $sp,$sp,1
   #store yc+x
   sw $t6,0($sp)
   addi $sp,$sp,1
   
   #plot(xc-y,yc-x)
   #store xc-y
   sw $t5,0($sp)
   addi $sp,$sp,1
   #store yc-x
   sw $t7,0($sp)
   addi $sp,$sp,1
   
   #if g>=0, else 
   #if g<0 1; else 0
   slt $t0,$s5,$0
   bne $t0,$0,else
   #g += diagonalInc
   add $s5,$s5,$s6
   #diagonalInc += 8
   addi $s6,$s6,8
   #y -= 1
   addi $s4,$s4,-1
   j incre
else:
   #g += rightInc
   add $s5,$s5,$s7
   #diagonalInc += 4
   addi $s6,$s6,4
incre:
   #rightInc += 4
   addi $s7,$s7,4
   #x += 1
   addi $s3,$s3,1
   j circleLoop

end:
   jr $ra 

end1:
