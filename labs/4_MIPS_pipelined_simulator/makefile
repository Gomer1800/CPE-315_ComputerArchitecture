JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
        lab4.java \
        BinaryDecoder.java \
        Helpers.java \
        makeInstDict.java \
        Mips_Filter.java \
        Mips_Parser.java \
        readFile.java \
        Emulator_FSM.java \
        Emulator_Assembly_Decoder.java \
        Mips_Pipeline_Simulator.java \

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
