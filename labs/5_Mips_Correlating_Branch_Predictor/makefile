JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
        lab5.java \
        BinaryDecoder.java \
        Emulator_AssemblyDecoder.java \
        Helpers.java \
        makeInstDict.java \
        Mips_Correlating_Branch_Predictor.java \
        Mips_Emulator.java \
        Mips_Filter.java \
        Mips_Parser.java \
        Parser.java \
        readCommands.java \
        readFile.java \
        Shift_Register.java \

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
