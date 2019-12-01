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
    public Shift_Register(int userSize) {
    /*
    Needs to Know:
    - register size
    */
    this._Size = userSize;
    this._Register = new ArrayList<String> (_Size);
    this.initialize();
    }

    public Shift_Register() {
    /*
    Default Constructor, assumes size of 2
    */
    this._Register = new ArrayList<String> (_Size);
    this.initialize();
    }

    // METHODS
    public int getInt() {
    /*
    This is a getter function which returns the decimal equivalent of the shift register contents
    */
    String stringValue = this.getString();
    int value = Integer.parseInt(stringValue, 2); // need string version of shift register binary value
    return value;
    }

    public int getSize() {
    /*
    This getter function returns the size of the 1-dimensional shift register.
    */
    return _Register.size();
    }

    public String getString() {
    /*
    This getter returns a string representation of the binary value stored in the shift register
    */
        String string = "";
        for(int i=0; i<_Size; i++) {
            string += _Register.get(i);
        }
    return string;
    }

    public void insert(boolean myBool) {
    /*
    This function allows the world to insert a 1 or 0 into the shift register from the right.
    */
        // shift all existing values to the left by one
        _Register.remove(0);
        // add newest value to the end
        String newValue = (myBool) ? "1":"0";
        _Register.add(_Size-1, newValue);
    }

    public void print() {
    /*
    This function prints the shift register contents to standard out
    */
        System.out.println(_Register);
    }

    public void printDebug() {
    /*
    Prints all properties of object neatly
    */
        System.out.println("\nShift Register DEBUG INFO");
        System.out.printf("Size = %d\n",this.getSize());
        System.out.println("getString() returns " + this.getString());
        System.out.printf("getInt() returns %d\n", this.getInt());
        
        this.print();
    }

    private void initialize() {
    /*
    This function is used to initialize the shift register to 0
    */
        for(int i=0;i<_Size;i++) { _Register.add("0"); }
    }
}