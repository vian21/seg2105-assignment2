# SEG2105 - Assignment 2

- To compile and run the project, you can use the following commands:

```bash
compile:
	javac -d bin -sourcepath . edu/seg2105/edu/server/**/*.java edu/seg2105/client/**/*.java edu/seg2105/OCSF/OCSF/src/ocsf/**/*.java

client:
	-java -cp bin edu.seg2105.client.ui.ClientConsole Patrick|| true

server:
	java -cp bin edu.seg2105.edu.server.backend.EchoServer || true
	
clean:
	rm -rf bin/*
```

- Or just run `make`