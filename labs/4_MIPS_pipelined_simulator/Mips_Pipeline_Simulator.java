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
   private List<String> _PipelineRegMem;

   private int _Num1,_Num2;

   private Parser _myParser;
   private Emulator_FSM _myEmulator;

   boolean _OK_TO_STEP;

   // PERFORMANCE VARIABLES
   int _NumInst;
   int _NumCycles;

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
       System.out.println("Simulator Step()");
       // Check is there a valid _Num1 ?
      _Num1 = (_Num1 == 0) ? 1:_Num1;

      // Setup
      String currentCommand = this.getCurrentCommand();
      List<String> nextCommands = this.getNextCommands();
      boolean branchTakenFlag = false;
      boolean jumpFlag        = false;
      boolean loadWordFlag    = false;

      for(int i=0; i<_Num1; i++)
      {
         if(_OK_TO_STEP)
         {
            // 1) Store current command token
            currentCommand = this.getCurrentCommand();
            // 2) call emulator step
            _myEmulator.step();
            // 3) Store next 3 command tokens
            nextCommands = this.getNextCommands();
            // 4) Store Edge Case Flags
            branchTakenFlag = _myEmulator.getBranchTakenFlag();
            jumpFlag        = _myEmulator.getJumpFlag();
            loadWordFlag    = _myEmulator.getLoadWordFlag();
         }
         // 5) Call Yus function step()
         _OK_TO_STEP = this.stepCycle(currentCommand,nextCommands, branchTakenFlag, jumpFlag, loadWordFlag);
         // 6) Update Stage
         _CurrentStage = this.getNextStage();
         this.showPipelineState();
      }
   }

   private void run() {
   // r
       System.out.println("Simulator run()");
      // Setup
      String currentCommand = this.getCurrentCommand();
      List<String> nextCommands = this.getNextCommands();
      boolean branchTakenFlag = false;
      boolean jumpFlag        = false;
      boolean loadWordFlag    = false;

      do {
         if(_OK_TO_STEP)
         {
            // 1) Store current command token
            currentCommand = this.getCurrentCommand();
            // 2) call emulator step
            _myEmulator.step();
            // 3) Store next 3 command tokens
            nextCommands = this.getNextCommands();
            // 4) Store Edge Case Flags
            branchTakenFlag = _myEmulator.getBranchTakenFlag();
            jumpFlag        = _myEmulator.getJumpFlag();
            loadWordFlag    = _myEmulator.getLoadWordFlag();
         }
         // 5) Call Yus function step()
         _OK_TO_STEP = this.stepCycle(currentCommand,nextCommands, branchTakenFlag, jumpFlag, loadWordFlag);
         // 6) Update Stage
         _CurrentStage = this.getNextStage();
      } while(this.checkPipelineEmpty() == false);
      this.printPerformance();
   }

   private void printPerformance() {
      System.out.println("Program complete");
      System.out.format("CPI = %f Cycles = %d Instructions = %d\n",
      ((double)_NumInst)/_NumCycles,_NumCycles,_NumInst);
   }

   private void dumpRegState() {
   // d
      _myEmulator.dumpRegState();
   }

  private void showPipelineState() {
   // p
      System.out.println("pc if/id id/exe exe/mem mem/wb");
      String strList = String.join(", ", _PipelineRegMem);
      System.out.println(strList);
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
      _OK_TO_STEP = true;
      _NumInst = 0;
      _NumCycles = 0;
   }
   
   private void exit() {
   // q
      //System.out.println("exit()");
      this._NextState = fsmState.EXIT;
   }

   private List<String> getNextCommands() {
   // returns array list consisting of up to 3 next tokens
      List<String> nextCommands = new ArrayList<String>();
      int tempPC = _myEmulator.getPC() + 1;
      for(int i=0; tempPC < _AssemblyCode.size()-1 && i<3; i++) {
         System.out.println("tempPC = " + Integer.toString(tempPC));
         tempPC++;
         nextCommands.add(_AssemblyCode.get(tempPC).get(0));
      }
      return nextCommands;
   }

   private String getCurrentCommand() {
      int tempPC = _myEmulator.getPC();
      if(tempPC < _AssemblyCode.size()) {
         return _AssemblyCode.get(tempPC).get(0);
      }
      else { return "empty"; }
   }

   private pipelineStage getNextStage() {
      pipelineStage nextStage = pipelineStage.if_id;
      if      (_CurrentStage == pipelineStage.mem_wb  ) { nextStage = pipelineStage.if_id;   }
      else if (_CurrentStage == pipelineStage.exe_mem ) { nextStage = pipelineStage.mem_wb;  }
      else if (_CurrentStage == pipelineStage.id_exe  ) { nextStage = pipelineStage.exe_mem; }
      else if (_CurrentStage == pipelineStage.if_id   ) { nextStage = pipelineStage.id_exe;  }
      return nextStage;
   }

   private void initStages() {
      _PipelineRegMem = new ArrayList<String>(5);
      _PipelineRegMem.add("0");
      for(int i=0;i < 5; i++) {
         _PipelineRegMem.add("empty");
      }
   }

   // YU's Cycle Step Function

   private boolean stepCycle(
      String currentCommand,
      List<String> nextCommands,
      boolean branchTakenFlag,
      boolean jumpFlag,
      boolean loadWordFlag)
   {
      int PC = _myEmulator.getPC() - 1;
      List<String> _5Stages = new ArrayList<String>(5);

      if(!_PipelineRegMem.get(4).equals("squash") &&
         !_PipelineRegMem.get(4).equals("stall") &&
         !_PipelineRegMem.get(4).equals("empty"))
      {
         _NumInst++;
      }
      if(PC < _AssemblyCode.size())
      {
         if (branchTakenFlag){
            // edge case
            System.out.println("Branch Taken");
            updatePipeline(currentCommand, branchTakenFlag, jumpFlag, loadWordFlag);
            //set next instruction to current
            currentCommand = nextCommands.get(0); 
            nextCommands.remove(0);
            if (_CurrentStage == pipelineStage.mem_wb) {// reset after you moved it 
               branchTakenFlag = false;                                   // not exectuting following 3 commands;
            }
         }
         else if (jumpFlag) {
            // edge case
            System.out.println("Jump");
            updatePipeline(currentCommand, branchTakenFlag, jumpFlag, loadWordFlag);
            if (_CurrentStage == pipelineStage.id_exe) {// reset after you moved it 
               jumpFlag = false;
            }
         }
         else if (loadWordFlag) {
            // edge case
            System.out.println("Load Word");
            updatePipeline(currentCommand, branchTakenFlag, jumpFlag, loadWordFlag);
            // set next instruction to current
            currentCommand = nextCommands.get(0);
            nextCommands.remove(0);
            if (_CurrentStage == pipelineStage.exe_mem) {  // reset
               loadWordFlag = false;
            }
         }
         else {
            // default
            System.out.println("default");
            updatePipeline(currentCommand, branchTakenFlag, jumpFlag, loadWordFlag);
         }
         // Finished 1 Cycle
         _NumCycles++;
         if (branchTakenFlag || jumpFlag || loadWordFlag) {
            return false;
         }
         else {
            return true;
         }
      } // _PC >= _AssemblyCode.size()
      else { return false; }
   }
   
   public void updatePipeline(
      String currentCommand,
      boolean branchTakenFlag,
      boolean jumpFlag,
      boolean loadWordFlag)
   {
      System.out.println("updatePipeline");
      int PC = _myEmulator.getPC();

      //_PipelineRegMem.set(0,Integer.toString(Integer.parseInt(_PipelineRegMem.get(0)) + 1));
      // Branch
      if (branchTakenFlag && _CurrentStage == pipelineStage.exe_mem) { //checks before moves
         _PipelineRegMem.set(4,_PipelineRegMem.get(3)); 
         _PipelineRegMem.set(1,"squash");
         _PipelineRegMem.set(2,"squash");
         _PipelineRegMem.set(3,"squash");
         _PipelineRegMem.set(0,Integer.toString(PC));  //set to the given immed PC 
      }
      // Jump
      else if (jumpFlag && _CurrentStage == pipelineStage.if_id){  //checks before moves
         shiftRight();         
         _PipelineRegMem.set(1,"squash");
         _PipelineRegMem.set(0,Integer.toString(PC)); //set to the given immed PC 
      }
      // LW
      else if (loadWordFlag && _CurrentStage == pipelineStage.id_exe){ //checks before moves
         shiftRight();
         _PipelineRegMem.set(1,_PipelineRegMem.get(2)); //currentCommand?
         _PipelineRegMem.set(2,"stall");
         _PipelineRegMem.set(0,Integer.toString(PC));  //set to the PC before   
      }
      // Default
      else {
         shiftRight();
         _PipelineRegMem.set(1,currentCommand);
         if (branchTakenFlag || jumpFlag || loadWordFlag) {                  // on the sqaush/stall time 
            _PipelineRegMem.set(0,Integer.toString(Integer.parseInt(_PipelineRegMem.get(0)) + 1));  //increment PC manually
         }
         else {
            _PipelineRegMem.set(0,Integer.toString(PC));   // regular times, just get the PC received from fsm 
         }
      }
   }

   private void shiftRight() {
      System.out.println("shiftRight()");
      for (int index = _PipelineRegMem.size()-2; index >= 1; index --) {
         _PipelineRegMem.set(index+1,_PipelineRegMem.get(index));
      }
   }

   private boolean checkPipelineEmpty() {
      for(int i=1; i<=5; i++) {
         if(!_PipelineRegMem.get(i).equals("empty") &&
            !_PipelineRegMem.get(i).equals("squash") &&
            !_PipelineRegMem.get(i).equals("stall")) 
         {
            return false;
         }
      }
      return true;
   }
}
