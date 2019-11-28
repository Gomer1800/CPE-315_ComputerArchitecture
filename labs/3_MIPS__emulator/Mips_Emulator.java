import java.util.*;
import java.lang.Math;
   
enum State {
   INIT, READ, EXEC, EXIT;
}

public class Mips_Emulator{

   // ATTRIBUTES

   private Emulator_AssemblyDecoder _Decoder = new Emulator_AssemblyDecoder();

   private Map< String, Runnable> _Commands = new HashMap<>();

   private State _NextState;
   
   private List<List<String>> _AssemblyCode;

   private int [] _RegMem;
   private int [] _DataMem;

   private int _SP;
   private int _PC;
   private int _Num1,_Num2;
   
   Parser _myParser;

   // CONSTRUCTOR

   public Mips_Emulator( List<List<String>> assemblyCode , Parser parser){
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
      this._myParser = parser;
   }

   // METHODS

   public void run_FSM() {
      List<String> cmd = new ArrayList<>();
      /*
      // TODO: Whenn call back is supported, remove this 
      Queue<String> commands = new LinkedList<>();
      commands.add("h");
      commands.add("m");
      commands.add("s");
      commands.add("d");
      commands.add("c");
      commands.add("d");
      commands.add("q");
      this._Num1 = 1;
      this._Num2 = 2;
      */

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
   // requires loading of data no?
   // does this also include update registers?
      this._Num1 = num[0];
      this._Num2 = num[1];
      this._Commands.get(command).run();
   }

   // EMULATOR COMMAND LOGIC
  
   private void printHelp() {
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

   private void step() {
   // s {num1}
       System.out.println("Step()");
      _Num1 = (_Num1 == 0) ? 1:_Num1;
      System.out.printf("\t%d instruction(s) executed\n", _Num1);
      for(int i=0; i<_Num1; i++)
      {
         if(_PC >= _AssemblyCode.size()) { break; }
         _PC = _Decoder.decodeAssembly( _AssemblyCode.get(_PC), _RegMem, _PC , _DataMem, _SP);
      } 
   }

   private void run() {
   // r
      System.out.println("run()");
      while(_PC < _AssemblyCode.size())
      {
         System.out.printf("PC = %d, Assembly Size = %d\n", _PC, _AssemblyCode.size());
         _PC = _Decoder.decodeAssembly( _AssemblyCode.get(_PC), _RegMem, _PC, _DataMem, _SP);
      } 
   }

   private void dumpRegState() {
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

   private void printDataMem() {
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

   private void clearAll() {
   // c
       //System.out.println("clearAll()");
      _RegMem = new int[32];
      _DataMem = new int[8192];
      _PC = 0;
      _SP = _DataMem[0];
   }
   
   private void exit() {
   // q
      System.out.println("exit()");
      this._NextState = State.EXIT;
   }
}
