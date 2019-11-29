import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.io.*;
import java.lang.*;

/*
Predictor States:
00 - Strongly NT 
01 - Weakly   NT
10 - Weakly    T
11 - Strongly  T
*/
enum PredictorState {
   00, 01, 10, 11;
}

public class Mips_Correlating_Branch_Predictor {
    
    // ATTRIBUTES
    private int _Size; // This determines the size of the predictor table, it is determined by the GHR bit size

    private int _Number_Correct_Predictions;

    private List<PredictorState> _Table; // this is predictor table, each element is 2 bits wide
    
    private List<String> _GHR;
    // CONSTRUCTOR
    public Mips_Correlating_Branch_Predictor (int userTableSize) {
    /*
    Needs to Know:
    - table size
    */
    this._Size
    
    }

    // METHODS
    public void updateTable(int index, boolean someBoolean) {
    /*
    This function updates the predictor table at the specified index using the incoming boolean
    */
    }

    public int getSize() {
    /*
    This getter function returns the size of the predictor table
    */
    }

    public void print() {
    /*
    This function prints the contents of the predictor table
    */
    }
}