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
    public void updateTable(boolean someBoolean) {
    /*
    This function updates the predictor table at the specified index using the incoming boolean
    */
            // Step 1: Use GHR to index into predictor table
            int index = _GHR.getInt();

            _Number_Total_Predictions++;

            // Step 2: Update Predictor table
            switch(_Table.get(index))
            {
                case WEAK_NT:
                    // If boolean is NT, change state to STRONG NT
                    if(someBoolean == false) {
                        _Number_Correct_Predictions++;
                        _Table.set(index, PredictorState.STRONG_NT);
                    }
                    // If boolean is  T, change state to WEAK T
                    else if(someBoolean == true) {
                        _Table.set(index, PredictorState.WEAK_T);
                    }
                    break;

                case STRONG_NT:
                    // If boolean is NT, no change
                    if(someBoolean == false) {
                        _Number_Correct_Predictions++;
                    }
                    // If boolean is  T, change state to WEAK NT
                    else if(someBoolean == true) {
                        _Table.set(index, PredictorState.WEAK_NT);
                    }
                    break;

                case WEAK_T:
                    // If boolean is NT, change state to WEAK NT
                    if(someBoolean == false) {
                        _Table.set(index, PredictorState.WEAK_NT);
                    }
                    // If boolean is  T, change state to STRONG T
                    else if(someBoolean == true) {
                        _Number_Correct_Predictions++;
                        _Table.set(index, PredictorState.STRONG_T);
                    }
                    break;

                case STRONG_T:
                    // If boolean is NT, change state to WEAK_T
                    if(someBoolean == false) {
                        _Table.set(index, PredictorState.STRONG_NT);
                    }
                    // If boolean is  T, no change
                    else if(someBoolean == true) {
                        _Number_Correct_Predictions++;
                    }
                    break;

                default: // default null, report error
                    System.out.printf("Null error at table index %d\n", index);
                    break;
            }

            // Step 3: Update GHR
            _GHR.insert(someBoolean);
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

    public void printAccuracy() {
    /*
    Prints total predictions vs correct predictions
    */
        System.out.printf("\naccuracy %.2f%% ", 100 * (((double)_Number_Correct_Predictions)/_Number_Total_Predictions));
        System.out.printf("(%d correct predictions", _Number_Correct_Predictions);
        System.out.printf(", %d predictions)\n\n", _Number_Total_Predictions);
    }

    public void printDebug() {
    /*
    Prints all properties of object neatly
    */
        System.out.println("\nPredictor Table DEBUG INFO");
        System.out.printf("Size = %d\n", _Table.size());
        this.printAccuracy();
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

        // initialize table values to WEAK NT
        for(int i=0; i<_Size; i++) {
            _Table.add(PredictorState.WEAK_NT);
        }
    }
}
