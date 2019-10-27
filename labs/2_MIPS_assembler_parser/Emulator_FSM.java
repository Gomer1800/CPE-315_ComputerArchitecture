import java.util.*;
import java.lang.Math;
   
enum State {
   INIT, READ, EXEC, UPDATE, EXIT;
}

public class Emulator_FSM {

   // ATTRIBUTES

   List<String> _EmulatorCommands =
      new ArrayList<String>(Arrays.asList( "h", "d", "s", "r", "m", "c", "q"));

   private Map< String, Runnable> _Commands = new HashMap<>();

   private State _NextState;
   
   private List<List<String>> _AssemblyCode;

   private int [] _RegMem;
   private int [][] _DataMem;

   private int _PC;
   private int _Num1,_Num2;

   // CONSTRUCTOR

   public Emulator_FSM( /* callback function */ List<List<String>> assemblyCode ){
      this._NextState = State.INIT;
      this._AssemblyCode = assemblyCode;
      this._Commands.put("h", () -> this.printHelp());
      this._Commands.put("d", () -> this.dumpRegState());
      // s num
      // r
      this._Commands.put("m", () -> this.printDataMem());
      this._Commands.put("c", () -> this.clearAll());
      this._Commands.put("q", () -> this.exit());
   }

   // METHODS

   public void run_FSM() {
     
      Queue<String> commands = new LinkedList<>();
      commands.add("h");
      commands.add("m");
      commands.add("c");
      commands.add("d");
      commands.add("q");
      this._Num1 = 191;
      this._Num2 = 193;

      String cmd = "";

      while(this._NextState != State.EXIT) 
      {
         switch(this._NextState)
         {
            case INIT:
               this._NextState = State.READ;
               System.out.println("FSM INIT");
               this.clearAll();
               break;

            case READ:
               // get next line of emulator instructions
               cmd = commands.remove();

               this._NextState = State.EXEC;
               System.out.println("FSM READ");
               System.out.print("mips> ");
               // CALL BACK FUNCTION USED HERE
               System.out.println(cmd);
               break;

            case EXEC:
               this._NextState = State.READ;
               System.out.println("FSM EXEC");
               this.exec(cmd, this._Num1, this._Num2);
               break;

            default:
               this._NextState = State.EXIT;
               break;
         } 
      }
   }
   
   public void read() {
   // Call for next command
   // Parse and Verify command
   // What do I mean by Parse, incoming token list are delimmited
   // Should this be a state?
   // How is this different from runCommand?
   }
   
   private void exec( String command, int ... num) {
   // Execute Command
   // requires loading of data no?
   // does this also include update registers?
      this._Num1 = num[0];
      this._Num2 = num[1];
      this._Commands.get(command).run();
   }
  
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
      int yMin = (int)Math.floor(this._Num1/192);
      int yMax = (int)Math.floor(this._Num2/192);
      int xMin = this._Num1 % 192;
      int xMax = this._Num2 % 192;

      for(int y=yMin; i<=this._Num2; y++) {
         for(int x=0; i<=this._Num2; x++) {
            System.out.println("[" + Integer.toString(i++) + "] = " + Integer.toString(_DataMem[y][x]));
         }
      }
      System.out.println("\n");
   }
 
   // HELPERS

   private void clearAll() {
   // c
      System.out.println("clearAll()");
      this._RegMem = new int[32];
      this._DataMem = new int[8][192];
      this._PC = 0;
   }
   
   private void exit() {
   // q
      System.out.println("exit()");
      this._NextState = State.EXIT;
   }
}
