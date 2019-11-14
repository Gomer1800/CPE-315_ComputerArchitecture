import java.util.*;
import java.lang.Math;
   
enum State {
   INIT, READ, EXEC, EXIT;
}

public class Emulator_FSM {

   // ATTRIBUTES

   private Map< String, Runnable> _Commands = new HashMap<>();

   private State _NextState;
   
   private List<List<String>> _AssemblyCode;

   private int [] _RegMem;
   private int [] _DataMem;

   private int _SP;
   private int _PC;
   private int _Num1,_Num2;
   
   private Parser _myParser;

   // Edge Case Error Flags, needed by pipeline simulator
   int _PrevDestReg;
   boolean _BranchTakenFlag =  false;
   boolean _JumpFlag =  false;
   boolean _LoadWordFlag =  false;

   // CONSTRUCTORS

   public Emulator_FSM(
      List<List<String>> assemblyCode,
      Parser parser)
   {
      this._NextState = State.INIT;
      this._AssemblyCode = assemblyCode;
      this._myParser = parser;

      // Populate hashmap with emulator command functions
      this._Commands.put("h", () -> this.printHelp());
      this._Commands.put("d", () -> this.dumpRegState());
      this._Commands.put("s", () -> this.step());
      this._Commands.put("r", () -> this.run());
      this._Commands.put("m", () -> this.printDataMem());
      this._Commands.put("c", () -> this.clearAll());
      this._Commands.put("q", () -> this.exit());
   }

   public Emulator_FSM( List<List<String>> assemblyCode){
      this._NextState = State.INIT;
      this._AssemblyCode = assemblyCode;

      // Populate hashmap with emulator command functions
      this._Commands.put("h", () -> this.printHelp());
      this._Commands.put("d", () -> this.dumpRegState());
      this._Commands.put("s", () -> this.step());
      this._Commands.put("r", () -> this.run());
      this._Commands.put("m", () -> this.printDataMem());
      this._Commands.put("c", () -> this.clearAll());
      this._Commands.put("q", () -> this.exit());
   }

   // METHODS

   public void run_FSM() {
      List<String> cmd = new ArrayList<>();
      
      while(this._NextState != State.EXIT) 
      {
         switch(this._NextState)
         {
            case INIT:
               this._NextState = State.READ;
               //System.out.println("FSM INIT");
               this.clearAll();
               break;

            case READ:
               // get next line of emulator instructions
               System.out.print("mips> ");
               if(_myParser._isScript == true) {
                  cmd = _myParser.fromScript();
                  System.out.println(cmd.get(0));
               }
               else {
                  cmd = _myParser.fromStdin();
               }
               // get additional arguments if present
               _Num1 = (cmd.size() > 1) ? Integer.parseInt(cmd.get(1)):0;
               _Num2 = (cmd.size() > 2) ? Integer.parseInt(cmd.get(2)):0;

               this._NextState = State.EXEC;
               //System.out.println("FSM READ");
               break;

            case EXEC:
               this._NextState = State.READ;
               //System.out.println("FSM EXEC");
               // exec will take the command token call its corresponding logic
               this.exec(cmd.get(0), _Num1, _Num2);
               // Reset these variables since emulator command is now done
               this._Num1 = 0;
               this._Num2 = 0;
               break;

            default:
               this._NextState = State.EXIT;
               break;
         } 
      }
   }
   
   private void exec( String command, int ... num) {
   // Execute Command
      this._Num1 = num[0];
      this._Num2 = num[1];
      this._Commands.get(command).run();
   }

   // EMULATOR COMMAND LOGIC
  
   public void printHelp() {
   // h
   System.out.println(
      "\nh = show help\n" +
      "d = dump register state\n" +
      "s = single step through the program (i.e. execute 1 instruction and stop)\n" +
      "s num = step through num instructions of the program\n" +
      "r = run until the program ends\n" +
      "m num1 num2 = display data memory from location num1 to num2\n" +
      "c = clear all registers, memory, and the program counter to 0\n" +
      "q = exit the program\n"
   );
   }

   public void step() {
   // s {num1}
       System.out.println("Emulator Step()");
      _Num1 = (_Num1 == 0) ? 1:_Num1;
      for(int i=0; i<_Num1; i++)
      {
         if(_PC >= _AssemblyCode.size()) { break; }
         // TODO (Luis): REVERT BACK TO ASSEMBLY DECODER ONCE I FIGURE OUT SHALLOW COPY OF BOOLEANS
         _PC = this.decode(
            _AssemblyCode.get(_PC),
            _RegMem,
            _PC,
            _DataMem,
            _SP);
      } 
   }

   public void run() {
   // r
      System.out.println("run()");
      while(_PC < _AssemblyCode.size())
      {
         // TODO (Luis): REVERT BACK TO ASSEMBLY DECODER ONCE I FIGURE OUT SHALLOW COPY OF BOOLEANS
         _PC = this.decode(
            _AssemblyCode.get(_PC),
            _RegMem,
            _PC,
            _DataMem,
            _SP);
      } 
   }

   public void dumpRegState() {
   // d
      System.out.println(
         "\npc = "  + _PC +
         "\n$0 = "  + _RegMem[0]  + "\t$v0 = " + _RegMem[2]  + "\t$v1 = " + _RegMem[3]  + "\t$a0 = " + _RegMem[4] +
         "\n$a1 = " + _RegMem[5]  + "\t$a2 = " + _RegMem[6]  + "\t$a3 = " + _RegMem[7]  + "\t$t0 = " + _RegMem[8] +
         "\n$t1 = " + _RegMem[9]  + "\t$t2 = " + _RegMem[10] + "\t$t3 = " + _RegMem[11] + "\t$t4 = " + _RegMem[12] +
         "\n$t5 = " + _RegMem[13] + "\t$t6 = " + _RegMem[14] + "\t$t7 = " + _RegMem[15] + "\t$s0 = " + _RegMem[16] +
         "\n$s1 = " + _RegMem[17] + "\t$s2 = " + _RegMem[18] + "\t$s3 = " + _RegMem[19] + "\t$s4 = " + _RegMem[20] +
         "\n$s5 = " + _RegMem[21] + "\t$s6 = " + _RegMem[22] + "\t$s7 = " + _RegMem[23] + "\t$t8 = " + _RegMem[24] +
         "\n$t9 = " + _RegMem[25] + "\t$sp = " + _RegMem[29] + "\t$ra = " + _RegMem[31] + "\n"
      );
   }

   public void printDataMem() {
   // m num1 num2
      System.out.println("\nprintMem()");
      int i = this._Num1;
      int xMin = this._Num1 % 192;
      int xMax = this._Num2 % 192;

      for(int x=0; i<=this._Num2; x++) {
         System.out.println("[" + Integer.toString(i++) + "] = " + Integer.toString(_DataMem[x]));
      }

      System.out.println("\n");
   }

   public void clearAll() {
   // c
       //System.out.println("clearAll()");
      _RegMem = new int[32];
      _DataMem = new int[8192];
      _PC = 0;
      _SP = _DataMem[0];
      _PrevDestReg = -1;
      _BranchTakenFlag =  false;
      _JumpFlag =  false;
      _LoadWordFlag =  false;
   }
   
   public void exit() {
   // q
      System.out.println("exit()");
      this._NextState = State.EXIT;
   }

   public int getPC() {
      return this._PC;
   }

   public boolean getBranchTakenFlag() {
      return _BranchTakenFlag;
   }

   public boolean getJumpFlag() {
      return _JumpFlag;
   }

   public boolean getLoadWordFlag() {
      return _LoadWordFlag;
   }

   // TODO (Luis): this should get moved back to assembly decoder class, too much logic here
   private int decode(/* Line of Mips Code, Register Memory */
         List<String> assemblyCode,
         int [] regMem,
         int PC,
         int [] dataMem,
         int SP)
   {
      // SETUP

      String inst = assemblyCode.get(0);
      _BranchTakenFlag = false;
      _JumpFlag = false;
      _LoadWordFlag = false;

      // BUSINESS LOGIC

      if (inst.equals("addi")) {
      // rt = rs + immed
         //System.out.println("addi " + assemblyCode.get(2) + " = " + assemblyCode.get(1) + " + " + assemblyCode.get(3));
         int rt    = regMem[Helpers._RegNum(assemblyCode.get(2))];
         int rs    = regMem[Helpers._RegNum(assemblyCode.get(1))];
         int immed = Integer.parseInt(assemblyCode.get(3));
         regMem[Helpers._RegNum(assemblyCode.get(2))] = rs + immed;
         // Check if loadWord flag needed? else reset prev dest reg
         if(_PrevDestReg != -1) {
            System.out.format("prevRd = %d rt = %d, rs = %d\n",_PrevDestReg,Helpers._RegNum(assemblyCode.get(2)),Helpers._RegNum(assemblyCode.get(1)));
            _LoadWordFlag = (_PrevDestReg == Helpers._RegNum(assemblyCode.get(2))) ? true:
            ((_PrevDestReg == (Helpers._RegNum(assemblyCode.get(1)))) ? true:false);
            _PrevDestReg = -1;
         }
         return (PC+1);
      }

      else if (inst.equals("beq")) {
      // offset if rs == rt 
       //  System.out.println("beq " + assemblyCode.get(1) + " == " + assemblyCode.get(2) + ", relative offset = " + assemblyCode.get(3));
         int rs    = regMem[Helpers._RegNum(assemblyCode.get(1))];
         int rt    = regMem[Helpers._RegNum(assemblyCode.get(2))];
         int immed = Integer.parseInt(assemblyCode.get(3));
         // Check if loadWord flag needed? else reset prev dest reg
         if(_PrevDestReg != -1) {
            System.out.format("prevRd = %d rt = %d, rs = %d\n",_PrevDestReg,Helpers._RegNum(assemblyCode.get(2)),Helpers._RegNum(assemblyCode.get(1)));
            _LoadWordFlag = ((_PrevDestReg == Helpers._RegNum(assemblyCode.get(1))) ? true:
            (_PrevDestReg == Helpers._RegNum(assemblyCode.get(2))) ? true:false);
            _PrevDestReg = -1;
         }
         if (rs == rt) {
            _BranchTakenFlag = true;
            return (PC + 1 + immed);
         }
         return (PC+1);
      }

      // offset if rs != rt 
      else if (inst.equals("bne")) {
       //  System.out.println("bne " + assemblyCode.get(1) + " != " + assemblyCode.get(2));
         int rs    = regMem[Helpers._RegNum(assemblyCode.get(1))];
         int rt    = regMem[Helpers._RegNum(assemblyCode.get(2))];
         int immed = Integer.parseInt(assemblyCode.get(3));
         // Check if loadWord flag needed? else reset prev dest reg
         if(_PrevDestReg != -1) {
            System.out.format("prevRd = %d rt = %d, rs = %d\n",_PrevDestReg,Helpers._RegNum(assemblyCode.get(2)),Helpers._RegNum(assemblyCode.get(1)));
            _LoadWordFlag = ((_PrevDestReg == Helpers._RegNum(assemblyCode.get(1))) ? true:
            (_PrevDestReg == Helpers._RegNum(assemblyCode.get(2))) ? true:false);
            _PrevDestReg = -1;
         }
         if (rs != rt){
            _BranchTakenFlag = true;
            return (PC + 1 + immed);
         }
         return (PC+1);
      }

      //  rd = rs + rt
      else if (inst.equals("add")) {
       //  System.out.println("add " + assemblyCode.get(2) + " + " + assemblyCode.get(3));
         int rs    = regMem[Helpers._RegNum(assemblyCode.get(2))];
         int rt    = regMem[Helpers._RegNum(assemblyCode.get(3))];
         regMem[Helpers._RegNum(assemblyCode.get(1))] = rs + rt;
         // Check if loadWord flag needed? else reset prev dest reg
         if(_PrevDestReg != -1) {
            System.out.format("prevRd = %d rt = %d, rs = %d\n",_PrevDestReg,Helpers._RegNum(assemblyCode.get(3)),Helpers._RegNum(assemblyCode.get(2)));
            _LoadWordFlag = ((_PrevDestReg == Helpers._RegNum(assemblyCode.get(2))) ? true:
            (_PrevDestReg == Helpers._RegNum(assemblyCode.get(3))) ? true:false);
            _PrevDestReg = -1;
         }
         return (PC+1);
      }

      // rd = rs - rt
      else if (inst.equals("sub")) {
       //  System.out.println("sub " + assemblyCode.get(2) + " - " + assemblyCode.get(3));
         int rs    = regMem[Helpers._RegNum(assemblyCode.get(2))];
         int rt    = regMem[Helpers._RegNum(assemblyCode.get(3))];
         regMem[Helpers._RegNum(assemblyCode.get(1))] = rs - rt;
         // Check if loadWord flag needed? else reset prev dest reg
         if(_PrevDestReg != -1) {
            System.out.format("prevRd = %d rt = %d, rs = %d\n",_PrevDestReg,Helpers._RegNum(assemblyCode.get(3)),Helpers._RegNum(assemblyCode.get(2)));
            _LoadWordFlag = ((_PrevDestReg == Helpers._RegNum(assemblyCode.get(2))) ? true:
            (_PrevDestReg == Helpers._RegNum(assemblyCode.get(3))) ? true:false);
            _PrevDestReg = -1;
         }
         return (PC+1);
      }

      // rd = rs & rt 
       else if (inst.equals("and")) {
       //  System.out.println("and " + assemblyCode.get(2) + " & " + assemblyCode.get(3));
         int rs    = regMem[Helpers._RegNum(assemblyCode.get(2))];
         int rt    = regMem[Helpers._RegNum(assemblyCode.get(3))];
         regMem[Helpers._RegNum(assemblyCode.get(1))] = rs & rt;
         // Check if loadWord flag needed? else reset prev dest reg
         if(_PrevDestReg != -1) {
            System.out.format("prevRd = %d rt = %d, rs = %d\n",_PrevDestReg,Helpers._RegNum(assemblyCode.get(3)),Helpers._RegNum(assemblyCode.get(2)));
            _LoadWordFlag = ((_PrevDestReg == Helpers._RegNum(assemblyCode.get(2))) ? true:
            (_PrevDestReg == Helpers._RegNum(assemblyCode.get(3))) ? true:false);
            _PrevDestReg = -1;
         }
         return (PC+1);
      }

      // rd = rs | rt 
      else if (inst.equals("or")) {
       //  System.out.println("or " + assemblyCode.get(2) + " | " + assemblyCode.get(3));
         int rs    = regMem[Helpers._RegNum(assemblyCode.get(2))];
         int rt    = regMem[Helpers._RegNum(assemblyCode.get(3))];
         regMem[Helpers._RegNum(assemblyCode.get(1))] = rs | rt;
         // Check if loadWord flag needed? else reset prev dest reg
         if(_PrevDestReg != -1) {
            System.out.format("prevRd = %d rt = %d, rs = %d\n",_PrevDestReg,Helpers._RegNum(assemblyCode.get(3)),Helpers._RegNum(assemblyCode.get(2)));
            _LoadWordFlag = ((_PrevDestReg == Helpers._RegNum(assemblyCode.get(2))) ? true:
            (_PrevDestReg == Helpers._RegNum(assemblyCode.get(3))) ? true:false);
            _PrevDestReg = -1;
         }
         return (PC+1);
      }

      // set rd = 1 if rs < rt else rd = 0
      else if (inst.equals("slt")) {
       //  System.out.println("slt " + assemblyCode.get(2) + " < " + assemblyCode.get(3));
         int rs    = regMem[Helpers._RegNum(assemblyCode.get(2))];
         int rt    = regMem[Helpers._RegNum(assemblyCode.get(3))];
         if (rs < rt) {
            regMem[Helpers._RegNum(assemblyCode.get(1))] = 1;
         }
         else{
            regMem[Helpers._RegNum(assemblyCode.get(1))] = 0; 
         }
         // Check if loadWord flag needed? else reset prev dest reg
         if(_PrevDestReg != -1) {
            System.out.format("prevRd = %d rt = %d, rs = %d\n",_PrevDestReg,Helpers._RegNum(assemblyCode.get(3)),Helpers._RegNum(assemblyCode.get(2)));
            _LoadWordFlag = ((_PrevDestReg == Helpers._RegNum(assemblyCode.get(2))) ? true:
            (_PrevDestReg == Helpers._RegNum(assemblyCode.get(3))) ? true:false);
            _PrevDestReg = -1;
         }
         return (PC+1);
      }

      // offset by immed
      else if(inst.equals("j")) {   // not sure if it's correct
       //  System.out.println("j " + assemblyCode.get(1));
         int immed = Integer.parseInt(assemblyCode.get(1));
         _JumpFlag = true;
         return (immed);
      }

      // offet by rs
      else if(inst.equals("jr")) {
       //  System.out.println("jr " + assemblyCode.get(1));
         int rs    = regMem[Helpers._RegNum(assemblyCode.get(1))];
         _JumpFlag = true;
         // Check if loadWord flag needed? else reset prev dest reg
         if(_PrevDestReg != -1) {
            System.out.format("prevRd = %d, rs = %d\n",_PrevDestReg,Helpers._RegNum(assemblyCode.get(1)));
            _LoadWordFlag = ((_PrevDestReg == Helpers._RegNum(assemblyCode.get(1))) ? true:false);
            _PrevDestReg = -1;
         }
         return rs;  //needs PC+1?  
      }

      // offset by immed and link ra 
      else if(inst.equals("jal")) {
         //System.out.println("jal " + assemblyCode.get(1));
         int immed = Integer.parseInt(assemblyCode.get(1));
         regMem[31] = PC + 1;  //regMem[31] = $ra
         _JumpFlag = true;
         return immed;
      }

      // rs = rt << shamt
      else if(inst.equals("sll")) {
       //  System.out.println("sll " + assemblyCode.get(2) + " << " + assemblyCode.get(3));
         int rt   = regMem[Helpers._RegNum(assemblyCode.get(2))];     
         int shamt = Integer.parseInt(assemblyCode.get(3));
         regMem[Helpers._RegNum(assemblyCode.get(1))] = rt << shamt;
         // Check if loadWord flag needed? else reset prev dest reg
         if(_PrevDestReg != -1) {
            System.out.format("prevRd = %d rt = %d\n",_PrevDestReg,Helpers._RegNum(assemblyCode.get(2)));
            _LoadWordFlag = ((_PrevDestReg == Helpers._RegNum(assemblyCode.get(2))) ? true:false);
            _PrevDestReg = -1;
         }
         return (PC+1);
      }

      else if (inst.equals("lw")){
         //System.out.println("lw " + assemblyCode.get(1) + " = Mem[ " + assemblyCode.get(2) + " + " + assemblyCode.get(3) + "]");
         int rt    = regMem[Helpers._RegNum(assemblyCode.get(1))];
         int immed = Integer.parseInt(assemblyCode.get(2));
         int rs    = regMem[Helpers._RegNum(assemblyCode.get(3))];
         rt = regMem[rs + immed];
         _PrevDestReg = rs + immed; // Index of destination register
         return (PC+1);
      }
      
      else if (inst.equals("sw")){
        // System.out.println("sw " + "Mem[ " + assemblyCode.get(2) + " + " + assemblyCode.get(3) + "] = " + assemblyCode.get(1)); 
         int rt    = regMem[Helpers._RegNum(assemblyCode.get(1))];
         int immed = Integer.parseInt(assemblyCode.get(2));
         int rs    = regMem[Helpers._RegNum(assemblyCode.get(3))]; 
         rt = regMem[rs + immed];
         // Check if loadWord flag needed? else reset prev dest reg
         if(_PrevDestReg != -1) {
            System.out.format("prevRd = %d rt = %d, rs = %d\n",_PrevDestReg,Helpers._RegNum(assemblyCode.get(1)),Helpers._RegNum(assemblyCode.get(3)));
            _LoadWordFlag = (_PrevDestReg == Helpers._RegNum(assemblyCode.get(1))) ? true:
            ((_PrevDestReg == (Helpers._RegNum(assemblyCode.get(3)))) ? true:false);
            _PrevDestReg = -1;
         }
         return (PC+1);
      }
      return PC;
   }
}
