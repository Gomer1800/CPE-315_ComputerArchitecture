import java.util.*;

public class StepFunction{
   //public List<String> _5Stages = new ArrayList<String>(5);
  // public String currentCommand;
  // public pipelineStage currentStage;
   //public List<String> nextCommands;
  // public boolean branchTakenFlag;
   //public boolean jumpFlag;
   //public boolean loadWordFlag;

   //Emulator_FSM emulator_FSM = new Emulator_FSM( myParser._AssemblyCode, parse);  
   

   /*public static void main(String [] args) {
      initStages();
      System.out.println(_5Stages);
      nextCommands.add("lw");
      nextCommands.add("lw");
      nextCommands.add("lw");
      loadWordFlag = true;
      pipelineStage.id_exe = "empty";
   }*/

   
   // all stages set to "empty"
   public void initStages(List<String> _5Stages) {
      int i = 1;
      _5Stages.add("0");
      while (i < 5) {
         _5Stages.add("empty");
         i++;
      }
   }

   public boolean StepFunction(
      pipelineStage currentStage,
      String currentCommand,
      List<String> nextCommands,
      int PC,
      boolean branchTakenFlag,
      boolean jumpFlag,
      boolean loadWordFlag
      ) {
      List<String> _5Stages = new ArrayList<String>(5);
      if (branchTakenFlag){
         updatePipeline(_5Stages,PC,branchTakenFlag,jumpFlag,loadWordFlag,currentStage, currentCommand);
         //set next instruction to current
         currentCommand = nextCommands.get(0); 
         nextCommands.remove(0);
         if (currentStage == pipelineStage.mem_wb) {// reset after you moved it 
            branchTakenFlag = false;                                   // not exectuting following 3 commands;
         }
      }
      else if (jumpFlag) {
         updatePipeline(_5Stages,PC,branchTakenFlag,jumpFlag,loadWordFlag,currentStage, currentCommand);
         if (currentStage == pipelineStage.id_exe) {// reset after you moved it 
            jumpFlag = false;
         }
      }
      else if (loadWordFlag) {
         updatePipeline(_5Stages,PC,branchTakenFlag,jumpFlag,loadWordFlag,currentStage, currentCommand);
         // set next instruction to current
         currentCommand = nextCommands.get(0);
         nextCommands.remove(0);
         if (currentStage == pipelineStage.exe_mem) {  // reset
            loadWordFlag = false;
         }
      }
      else {
         updatePipeline(_5Stages,PC,branchTakenFlag,jumpFlag,loadWordFlag,currentStage, currentCommand);
      }
      if (branchTakenFlag | jumpFlag | loadWordFlag) {
         return false;
      }
      else {
         return true;
      }
   }
   
   public void updatePipeline(List<String> _5Stages, int PC, boolean branchTakenFlag, 
                              boolean jumpFlag, boolean loadWordFlag,
                              pipelineStage currentStage,String currentCommand) {
      _5Stages.set(0,Integer.toString(Integer.parseInt(_5Stages.get(0)) + 1));
      // Branch
      if (branchTakenFlag && currentStage == pipelineStage.exe_mem) { //checks before moves
         _5Stages.set(4,_5Stages.get(3)); 
         _5Stages.set(1,"squash");
         _5Stages.set(2,"squash");
         _5Stages.set(3,"squash");
         _5Stages.set(0,Integer.toString(PC));  //set to the given immed PC 
      }
      // Jump
      else if (jumpFlag && currentStage == pipelineStage.if_id){  //checks before moves
         shiftRight(_5Stages);         
         _5Stages.set(1,"squash");
         _5Stages.set(0,Integer.toString(PC)); //set to the given immed PC 
      }
      // LW
      else if (loadWordFlag && currentStage == pipelineStage.id_exe){ //checks before moves
         shiftRight(_5Stages);
         _5Stages.set(1,_5Stages.get(2)); //currentCommand?
         _5Stages.set(2,"stall");
         _5Stages.set(0,Integer.toString(PC));  //set to the PC before   
      }
      // Default
      else {
         shiftRight(_5Stages);
         _5Stages.set(1,currentCommand);  
         if (branchTakenFlag | jumpFlag | loadWordFlag) {                  // on the sqaush/stall time 
            _5Stages.set(0,Integer.toString(Integer.parseInt(_5Stages.get(0)) + 1));  //increment PC manually
         }
         else {
            _5Stages.set(0,Integer.toString(PC));   // regular times, just get the PC received from fsm 
         }
      }
   }

   public void shiftRight(List<String> _5Stages) {
      for (int index = _5Stages.size()-2; index >= 1; index --) {
         _5Stages.set(index+1,_5Stages.get(index));
      }
   }
}   
