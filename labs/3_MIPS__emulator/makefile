JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
        lab3.java \
        BinaryDecoder.java \
        Helpers.java \
        makeInstDict.java \
        Mips_Emulator.java \
        Mips_Filter.java \
        Mips_Parser.java \
        readFile.java \

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
