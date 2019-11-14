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

   // EMULATOR CONTROL FLAG
   private boolean _OK_TO_STEP;
   private boolean Finished;

   // PERFORMANCE VARIABLES
   int _NumInst;
   int _NumCycles;
   int _OldPC;

   // Edge Case Flags
   // TODO (Luis): Fix names
   int branchCounter;
   private boolean branchTakenFlag;
   private boolean jumpFlag;
   private boolean loadWordFlag;
   List<String> nextCommands;

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
               // System.out.println("FSM INIT");
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
       // System.out.println("\nSimulator Step()");
       // Check is there a valid _Num1 ?
      _Num1 = (_Num1 == 0) ? 1:_Num1;

      // Setup
      String currentCommand = this.getCurrentCommand();

         for(int i=0; i<_Num1; i++)
         {
            if(Finished) {
               currentCommand = this.getCurrentCommand();
            }
            _OldPC = _myEmulator.getPC();
            //System.out.format("OLD PC = %d\n", _OldPC);
            if(_OK_TO_STEP)
            {
               // 2) call emulator step
               _myEmulator.step();
               //System.out.format("OLD PC AFTER STEP = %d\n", _OldPC);
               // 3) Store next 3 command tokens
               nextCommands = this.getNextCommands();
               // 4) Store Edge Case Flags
               branchTakenFlag = _myEmulator.getBranchTakenFlag();
               jumpFlag        = _myEmulator.getJumpFlag();
               loadWordFlag    = _myEmulator.getLoadWordFlag();
            }
            // 5) Call Yus function step()
            _OK_TO_STEP = this.stepCycle(_OldPC,currentCommand,nextCommands, branchTakenFlag, jumpFlag, loadWordFlag);
            // 1) Store current command token
            currentCommand = this.getCurrentCommand();
            // 6) Update Stage
            _CurrentStage = this.getNextStage();
            this.showPipelineState();
            //this.printPerformance();
         }
   }

   private void run() {
   // r
      // System.out.println("\nSimulator run()");
      // Setup
      int OLD_PC = 0;
      String currentCommand = this.getCurrentCommand();
      List<String> nextCommands = this.getNextCommands();

      do {
            if(Finished) {
               currentCommand = this.getCurrentCommand();
            }
            _OldPC = _myEmulator.getPC();
            // System.out.format("OLD PC = %d\n", _OldPC);
            if(_OK_TO_STEP)
            {
               // 2) call emulator step
               _myEmulator.step();
               // System.out.format("OLD PC AFTER STEP = %d\n", _OldPC);
               // 3) Store next 3 command tokens
               nextCommands = this.getNextCommands();
               // 4) Store Edge Case Flags
               branchTakenFlag = _myEmulator.getBranchTakenFlag();
               jumpFlag        = _myEmulator.getJumpFlag();
               loadWordFlag    = _myEmulator.getLoadWordFlag();
            }
            // 5) Call Yus function step()
            _OK_TO_STEP = this.stepCycle(_OldPC,currentCommand,nextCommands, branchTakenFlag, jumpFlag, loadWordFlag);
            // 1) Store current command token
            currentCommand = this.getCurrentCommand();
            // 6) Update Stage
            _CurrentStage = this.getNextStage();
            this.showPipelineState();
            //this.printPerformance();
      } while(this.checkPipelineEmpty() == false);
      this.printPerformance();
   }

   private void printPerformance() {
      //System.out.println("\nprintPerformance()");
      System.out.println("\nProgram complete");
      System.out.format("CPI = %f Cycles = %d Instructions = %d\n",
      ((double)_NumCycles)/_NumInst,_NumCycles,_NumInst);
   }

   private void dumpRegState() {
   // d
      _myEmulator.dumpRegState();
   }

  private void showPipelineState() {
   // p
      // System.out.println("\nshowPipelineState()");
      System.out.println("\npc if/id id/exe exe/mem mem/wb");
      String strList = String.join(" ", _PipelineRegMem);
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
      _NumCycles = 1;
      // TODO (Luis): fix these names please!
      branchTakenFlag = false;
      jumpFlag = false;
      loadWordFlag = false;
      _OldPC = 0;
      branchCounter = 0;
      nextCommands = this.getNextCommands();
      Finished = false;
   }
   
   private void exit() {
   // q
      //System.out.println("exit()");
      this._NextState = fsmState.EXIT;
   }

   private List<String> getNextCommands() {
   // returns array list consisting of up to 3 next tokens
      //System.out.println("\ngetNextCommands()");
      List<String> nextCommands = new ArrayList<String>();
      int tempPC = _OldPC + 0;
      for(int i=0; tempPC < _AssemblyCode.size()-1 && i<3; i++) {
         //System.out.println("tempPC = " + Integer.toString(tempPC));
         tempPC++;
         nextCommands.add(_AssemblyCode.get(tempPC).get(0));
      }
      return nextCommands;
   }

   private String getCurrentCommand() {
      //System.out.println("\ngetCurrentCommand()");
      int tempPC = _myEmulator.getPC();
      //System.out.format("getcommand size = %d PC = %d\n", _AssemblyCode.size(), tempPC);
      if(tempPC < _AssemblyCode.size()) {
         return _AssemblyCode.get(tempPC).get(0);
      }
      else { return "empty"; }
   }

   private pipelineStage getNextStage() {
      //System.out.print("\nCurrent Stage = ");
      pipelineStage nextStage = pipelineStage.if_id;
      if      (_CurrentStage == pipelineStage.mem_wb  ) {
         //System.out.println("EXE/MEM");
         nextStage = pipelineStage.if_id;   }
      else if (_CurrentStage == pipelineStage.exe_mem ) { 
         //System.out.println("ID/EXE");
         nextStage = pipelineStage.mem_wb;  }
      else if (_CurrentStage == pipelineStage.id_exe  ) {
         //System.out.println("IF/ID");
         nextStage = pipelineStage.exe_mem; }
      else if (_CurrentStage == pipelineStage.if_id   ) {
         //System.out.println("INIT OR MEM/WB");
         nextStage = pipelineStage.id_exe;  }
      return nextStage;
   }

   private void initStages() {
      _PipelineRegMem = new ArrayList<String>();
      _PipelineRegMem.add("0");
      for(int i=1;i < 5; i++) {
         _PipelineRegMem.add("empty");
      }
   }

   // YU's Cycle Step Function

   private boolean stepCycle(
      int OLD_PC,
      String currentCommand,
      List<String> nextCommands,
      boolean branchTakenFlag,
      boolean jumpFlag,
      boolean loadWordFlag)
   {
      //System.out.println("\nstepCycle()");
      int PC = OLD_PC;
      //System.out.println("Current Command " + currentCommand);
      //System.out.format("PC = %d\n", PC);
      if(!_PipelineRegMem.get(4).equals("squash") &&
         !_PipelineRegMem.get(4).equals("stall") &&
         !_PipelineRegMem.get(4).equals("empty"))
      {
         _NumInst++;
      }
      //System.out.format("branch = %b, jump = %b, lw = %b\n" , branchTakenFlag,jumpFlag,loadWordFlag);

      if(_myEmulator.getPC() < _AssemblyCode.size())
      {
         if (branchTakenFlag){
            // edge case
            //System.out.println("Branch Taken");
            if(_OK_TO_STEP) {
               updatePipeline(PC, currentCommand, branchTakenFlag, jumpFlag, loadWordFlag, nextCommands.size());
            }
            else {
               branchCounter++;
               updatePipeline(PC, nextCommands.remove(0), branchTakenFlag, jumpFlag, loadWordFlag, nextCommands.size());
            }
            //set next instruction to current
            //currentCommand = nextCommands.get(0);
            //System.out.println("New command " + currentCommand);
            //System.out.println("Next commands " + nextCommands);
            //nextCommands.remove(0);
            if (branchCounter == 3 ) {// reset after you moved it 
               branchTakenFlag = false;                                // not exectuting following 3 commands;
            }
         }
         else if (jumpFlag) {
            // edge case
            //System.out.println("Jump");
            updatePipeline(PC, currentCommand, branchTakenFlag, jumpFlag, loadWordFlag, nextCommands.size());
            if (_CurrentStage == pipelineStage.id_exe) {// reset after you moved it
               //System.out.println("CYCLE JUMP = " + jumpFlag);
               jumpFlag = false;
               //System.out.println("CYCLE JUMP = " + jumpFlag);
            }
         }
         else if (loadWordFlag) {
            // edge case
            //System.out.println("Load Word");
            updatePipeline(PC, currentCommand, branchTakenFlag, jumpFlag, loadWordFlag, nextCommands.size());
            // set next instruction to current
            currentCommand = nextCommands.get(0);
            nextCommands.remove(0);
            if (_CurrentStage == pipelineStage.exe_mem) {  // reset
               loadWordFlag = false;
            }
         }
         else {
            // default
            //System.out.println("default");
            updatePipeline(PC, currentCommand, branchTakenFlag, jumpFlag, loadWordFlag, nextCommands.size());
         }

         // Finished 1 Cycle
         _NumCycles++;
         
         if (branchTakenFlag || jumpFlag || loadWordFlag) {
            //System.out.println("STALL");
            return false;
         }
         else {
            //System.out.println("NO STALL");
            return true;
         }
      } // _PC >= _AssemblyCode.size()
      else {
         //System.out.println("FINISHED");
         if(Finished == false) {
            for(int i=1; i<5; i++) {
                     if(!_PipelineRegMem.get(i).equals("squash") &&
                        !_PipelineRegMem.get(i).equals("stall") &&
                        !_PipelineRegMem.get(i).equals("empty")) {
                           _NumCycles++;
                        }
            }
         }
         Finished = true;
         updatePipeline(PC, currentCommand, branchTakenFlag, jumpFlag, loadWordFlag, nextCommands.size());
         return false;
         }
   }
   
   public void updatePipeline(
      int PC,
      String currentCommand,
      boolean branchTakenFlag,
      boolean jumpFlag,
      boolean loadWordFlag,
      int commandsAvailable)
   {
      //System.out.println("\nupdatePipeline()");
      //int PC = _myEmulator.getPC();

      //_PipelineRegMem.set(0,Integer.toString(Integer.parseInt(_PipelineRegMem.get(0)) + 1));
      // Branch
      //System.out.format("branchCounter = %d\n", branchCounter);
      if (branchTakenFlag && branchCounter == 3) { //checks before moves
         //System.out.println("UPDATE BRANCH");
         _PipelineRegMem.set(4,_PipelineRegMem.get(3)); 
         _PipelineRegMem.set(1,"squash");
         _PipelineRegMem.set(2,"squash");
         _PipelineRegMem.set(3,"squash");
         _PipelineRegMem.set(0,Integer.toString(PC));  //set to the given immed PC 
      }
      // Jump
      else if (jumpFlag && _CurrentStage == pipelineStage.id_exe){  //checks before moves
         //System.out.println("UPDATE JUMP");
         shiftRight();         
         _PipelineRegMem.set(1,"squash");
         _PipelineRegMem.set(0,Integer.toString(PC)); //set to the given immed PC 
      }
      // LW
      else if (loadWordFlag && _CurrentStage == pipelineStage.id_exe){ //checks before moves
         //System.out.println("UPDATE LW");
         shiftRight();
         _PipelineRegMem.set(1,_PipelineRegMem.get(2)); //currentCommand?
         _PipelineRegMem.set(2,"stall");
         _PipelineRegMem.set(0,Integer.toString(PC));  //set to the PC before   
      }
      // Default
      else {
         //System.out.println("UPDATE DEFAULT");
         //System.out.println("commands avail " + commandsAvailable);
         shiftRight();
         _PipelineRegMem.set(1,currentCommand);
         //System.out.println("Update: Current Command " + currentCommand);
         if(branchTakenFlag) {
            _PipelineRegMem.set(0,Integer.toString(Integer.parseInt(_PipelineRegMem.get(0)) + 1));
         }
         else if (jumpFlag || loadWordFlag) {                  // on the sqaush/stall time 
            _PipelineRegMem.set(0,Integer.toString(Integer.parseInt(_PipelineRegMem.get(0)) + 1));  //increment PC manually
         }
         else {
            _PipelineRegMem.set(0,Integer.toString(PC + 1));   // regular times, just get the PC received from fsm 
         }
      }
   }

   private void shiftRight() {
      //System.out.println("shiftRight()");
      for (int index = _PipelineRegMem.size()-2; index >= 1; index --) {
         _PipelineRegMem.set(index+1,_PipelineRegMem.get(index));
      }
   }

   private boolean checkPipelineEmpty() {
      for(int i=1; i<5; i++) {
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
