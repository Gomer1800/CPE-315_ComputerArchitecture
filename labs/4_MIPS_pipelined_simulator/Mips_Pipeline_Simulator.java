import java.util.*;
import java.lang.Math;
   
enum fsmState {
   INIT, READ, EXEC, EXIT;
}

enum pipelineStage {
   if_id, id_exe, exe_mem, mem_wb;
}

public class Mips_Pipeline_Simulator {

   // ATTRIBUTES

   private Map< String, Runnable> _Commands = new HashMap<>();

   private fsmState _NextState;
   private pipelineStage _CurrentStage;
   
   private List<List<String>> _AssemblyCode;

   private int [] _RegMem;
   private int [] _DataMem;
   private List<String> _pipelineRegMem;

   private int _Num1,_Num2;

   private Parser _myParser;
   private Emulator_FSM _myEmulator;

   // CONSTRUCTOR

   public Mips_Pipeline_Simulator(
      List<List<String>> assemblyCode,
      Parser parser,
      Emulator_FSM emulator)
   {
      // assign attributes
      this._NextState = fsmState.INIT;
      this._AssemblyCode = assemblyCode;
      this._myParser = parser;
      this._myEmulator = emulator;

      // Populate hashmap with simulator command functions
      this._Commands.put("h", () -> this.printHelp());
      this._Commands.put("d", () -> this.dumpRegState());
      this._Commands.put("p", () -> this.showPipelineState());
      this._Commands.put("s", () -> this.step());
      this._Commands.put("r", () -> this.run());
      this._Commands.put("m", () -> this.printDataMem());
      this._Commands.put("c", () -> this.clearAll());
      this._Commands.put("q", () -> this.exit());
   }

   // METHODS

   public void run_FSM() {
      List<String> cmd = new ArrayList<>();

      while(this._NextState != fsmState.EXIT) 
      {
         switch(this._NextState)
         {
            case INIT:
               //System.out.println("FSM INIT");
               this._NextState = fsmState.READ;
               this._CurrentStage = pipelineStage.if_id;
               this.clearAll();
               break;

            case READ:
               //System.out.println("FSM READ");
               this._NextState = fsmState.EXEC;

               // get next line of simulator instructions
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
               break;

            case EXEC:
               //System.out.println("FSM EXEC");
               this._NextState = fsmState.READ;

               // exec will take the command token call its corresponding logic
               this.exec(cmd.get(0), _Num1, _Num2);
               // Reset these variables since emulator command is now done
               this._Num1 = 0;
               this._Num2 = 0;
               break;

            default:
               this._NextState = fsmState.EXIT;
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
  
   private void printHelp() {
   // h
   System.out.println(
      "\nh = show help\n" +
      "d = dump register state\n" +
      "p = show pipeline registers\n" +
      "s = step through a single clock cycle step (i.e. simulate 1 cycle and stop)\n" +
      "s num = step through num instructions of the program\n" +
      "r = run until the program ends nad display timing summary\n" +
      "m num1 num2 = display data memory from location num1 to num2\n" +
      "c = clear all registers, memory, and the program counter to 0\n" +
      "q = exit the program\n"
   );
   }

   private void step() {
   // s {num1}
       System.out.println("Step()");
      _Num1 = (_Num1 == 0) ? 1:_Num1;
      for(int i=0; i<_Num1; i++)
      {
         // 1) Store current command token
         String currentCommand = this.getCurrentCommand();
         // 2) call emulator step
         _myEmulator.step();
         // 3) Store next 3 command tokens
         List<String> nextCommands = this.getNextCommands();
         // 4) Store Edge Case Flags
         boolean branchTakenFlag = _myEmulator.getBranchTakenFlag();
         boolean jumpFlag        = _myEmulator.getJumpFlag();
         boolean loadWordFlag    = _myEmulator.getLoadWordFlag();
         // 5) Call Yus function step()
         // 6) Update Stage
         _CurrentStage = this.getNextStage();
      } 
   }

   private void run() {
   // r
      //System.out.println("run()");
      //while(_PC < _AssemblyCode.size())
      //{
      //   _PC = _Decoder.decodeAssembly( _AssemblyCode.get(_PC), _RegMem, _PC, _DataMem, _SP);
      ///}
   }

   private void dumpRegState() {
   // d
      _myEmulator.dumpRegState();
   }

  private void showPipelineState() {
   // p
   }

   private void printDataMem() {
   // m num1 num2
      //System.out.println("\nprintMem()");
      _myEmulator.printDataMem();
   }

   private void clearAll() {
   // c
       //System.out.println("clearAll()");
       _myEmulator.clearAll();
       this.initStages();
      _pipelineRegMem = new ArrayList<String>(5);
   }
   
   private void exit() {
   // q
      //System.out.println("exit()");
      this._NextState = fsmState.EXIT;
   }

   private void initStages() {
      _pipelineRegMem = new ArrayList<String>(5);
      for(int i=0;i < 5; i++) {
         _pipelineRegMem.add("empty");
      }
   }

   private List<String> getNextCommands() {
   // returns array list consisting of up to 3 next tokens
      List<String> nextCommands = new ArrayList<String>();
      int tempPC = _myEmulator.getPC() + 1;
      for(int i=0; tempPC < _AssemblyCode.size() && i<3; i++) {
         tempPC++;
         nextCommands.add(_AssemblyCode.get(tempPC).get(0));
      }
      return nextCommands;
   }

   private String getCurrentCommand() {
      int tempPC = _myEmulator.getPC();
      return _AssemblyCode.get(tempPC).get(0);
   }

   private pipelineStage getNextStage() {
      pipelineStage nextStage = pipelineStage.if_id;
      if      (_CurrentStage == pipelineStage.mem_wb  ) { nextStage = pipelineStage.if_id;   }
      else if (_CurrentStage == pipelineStage.exe_mem ) { nextStage = pipelineStage.mem_wb;  }
      else if (_CurrentStage == pipelineStage.id_exe  ) { nextStage = pipelineStage.exe_mem; }
      else if (_CurrentStage == pipelineStage.if_id   ) { nextStage = pipelineStage.id_exe;  }
      return nextStage;
   }
}
