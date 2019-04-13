ftp:
	javac ./*.java
run_server:
	java Server
run_cliente:
	java Client
clean:
	rm *.class -f
clean_log:
	rm log.txt -f