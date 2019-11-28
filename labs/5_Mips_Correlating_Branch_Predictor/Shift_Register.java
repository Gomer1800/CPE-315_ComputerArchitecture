import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.io.*;
import java.lang.*;

public class Shift_Register {
    
    // ATTRIBUTES
    private int _Size = 2; // this determines the size of the shift register, if not specified by constructor it defaults to 2

    private List<String> _Register; // this is the shift register, size is determined by constructor
    
    // CONSTRUCTOR
    public Shift_Register() {
    /*
    Needs to Know:
    - register size
    */
    }

    // METHODS
    public int getInt() {
    /*
    This is a getter function which returns the decimal equivalent of the shift register contents
    */

    // Use helper functions to convert the string array to an integer equivalent, for 2, 4, 8 bit arrays
    }

    public int getSize() {
    /*
    This getter function returns the size of the 1-dimensional shift register.
    */
    }

    public void insert(boolean myBool) {
    /*
    This function allows the world to insert a 1 or 0 into the shift register 
    */
    }

    public void print() {
    /*
    This function prints the shift register contents to standard out
    */
    }
}