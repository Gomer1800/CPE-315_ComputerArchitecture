import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.io.*;
import java.lang.*;
import java.lang.Math;

/*
Predictor States:
00 - Strongly NT 
01 - Weakly   NT
10 - Weakly    T
11 - Strongly  T
*/
enum PredictorState {
   NULL, STRONG_NT, WEAK_NT, WEAK_T, STRONG_T;
}

public class Mips_Correlating_Branch_Predictor {
    
    // ATTRIBUTES
    private int _Size; // This determines the size of the predictor table, it is determined by the GHR bit size
    private int _GHR_Size;

    private int _Number_Correct_Predictions;

    private int _Number_Total_Predictions;
    
    private List<PredictorState> _Table; // this is predictor table, each element is 2 bits wide
    
    private Shift_Register _GHR;

    // CONSTRUCTOR
    public Mips_Correlating_Branch_Predictor (int ghrSize) { // this is the incoming GHR size needed
    /*
    Receives GHR size from user
    */
        this._GHR_Size = ghrSize;
        this._Size = (int) Math.pow(2,ghrSize);
        this.reset();
    }

    public Mips_Correlating_Branch_Predictor () { // this is the incoming GHR size needed
    /*
    Default constructor with GHR size of 2
    */
        this._GHR_Size = 2;
        this._Size = (int) Math.pow(2, 2);
        this.reset();
    }

    // METHODS
    public void updateTable(int index, boolean someBoolean) {
    /*
    This function updates the predictor table at the specified index using the incoming boolean
    */
    }

    public int getPredictorSize() {
    /*
    This getter function returns the size of the predictor table
    */
        return _Table.size() + 0;
    }

    public void print() {
    /*
    This function prints the contents of the predictor table
    */
        System.out.println("Predictor Table print()");
        for(int i=0; i<_Size; i++) {
            System.out.printf("%d. ", i);
            // check predictor state before printing
            switch(_Table.get(i))
            {
                case WEAK_NT:
                    System.out.println("WEAK   NT");
                    break;

                case STRONG_NT:
                    System.out.println("STRONG NT");
                    break;

                case WEAK_T:
                    System.out.println("WEAK    T");
                    break;

                case STRONG_T:
                    System.out.println("STRONG  T");
                    break;

                default: // default null
                    System.out.println("EMPTY");
                    break;
            }
        }
    }


    public void printDebug() {
    /*
    Prints all properties of object neatly
    */
        System.out.println("\nPredictor Table DEBUG INFO");
        System.out.printf("Size = %d\n", _Table.size());
        this.print();

        _GHR.printDebug();
    }
    public void reset() {
    /*
    Initializes GHR and Predictor Table objects, and counters
    */
        // reset counters
        this._Number_Correct_Predictions = 0;
        this._Number_Total_Predictions   = 0;

        // initialize objects
        this._GHR   = new Shift_Register(_GHR_Size);
        this._Table = new ArrayList<PredictorState>(_Size);

        // initialize table values to default NULL
        for(int i=0; i<_Size; i++) {
            _Table.add(PredictorState.NULL);
        }
    }
}