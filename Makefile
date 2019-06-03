ftp:
	javac -Xlint ./*.java
run_server:
	java Server
run_cliente:
	java Client
clean:
	rm *.class -f
clean_log:
	rm log.txt -f
clean_part:
	rm *.split -f
clean_cifrado:
	rm *.cifrado -f