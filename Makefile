JFLAGS = -g -d out
JC = javac

default:
	mkdir -p out/mazes
	cp -r src/mazes out
	$(JC) $(JFLAGS) src/*.java

clean:
	rm -r out