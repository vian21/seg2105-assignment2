compile:
	javac -d bin -sourcepath . edu/seg2105/edu/server/**/*.java edu/seg2105/client/**/*.java edu/seg2105/OCSF/OCSF/src/ocsf/**/*.java

client:
	-java -cp bin edu.seg2105.client.ui.ClientConsole xmen localhost 1234 || true

server:
	java -cp bin edu.seg2105.edu.server.backend.EchoServer 1234 || true
	
clean:
	rm -rf bin/*