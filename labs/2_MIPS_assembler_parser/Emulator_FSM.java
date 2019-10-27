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

   private int [][] _RegMem;

   private int _PC;
   private int _Num1,_Num2;

   // CONSTRUCTOR

   public Emulator_FSM( /* callback function */ List<List<String>> assemblyCode ){
      this._NextState = State.INIT;
      this._AssemblyCode = assemblyCode;
      this._Commands.put("h", () -> this.printHelp());
      this._Commands.put("m", () -> this.printMem());
   }

   // METHODS

   public void run_FSM() {
      while(this._NextState != State.EXIT) 
      {
         String command = "m";

         switch(this._NextState)
         {
            case INIT:
               this._NextState = State.READ;
               System.out.println("FSM INIT");
               this.init();
               break;

            case READ:
               // get next line of emulator instructions
               this._NextState = State.EXEC;
               System.out.println("FSM READ");
               System.out.print("mips> ");
               // CALL BACK FUNCTION USED HERE
               System.out.println(command);
               break;

            case EXEC:
               this._NextState = State.EXIT;
               this.exec( command, 2 , 5);
               System.out.println("FSM EXEC");
               break;

            default:
               this._NextState = State.EXIT;
               break;
         } 
      }
   }
   
   private void init() {
   // Initialize Emulated Registers
      _RegMem = new int[8][192];
      _PC = 0;
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
   
   public void updateData() {
   // update registers if data manipulated
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

   private void printMem() {
   // m num1 num2
      System.out.println("\nprintMem()");
      int i = this._Num1;
      int yMin = (int)Math.floor(this._Num1/192);
      int yMax = (int)Math.floor(this._Num2/192);
      int xMin = this._Num1 % 192;
      int xMax = this._Num2 % 192;

      for(int y=yMin; i<=this._Num2; y++) {
         for(int x=0; i<=this._Num2; x++) {
            System.out.println("[" + Integer.toString(i++) + "] = " + Integer.toString(_RegMem[y][x]));
         }
      }
      System.out.println("\n");
   }
 
   // HELPERS

   private void resetMem() {
      for(int y=0; y<8; y++) {
         for(int x=0; x<192; x++) {
            _RegMem[y][x] = 0;
         }
      }
   }
}
