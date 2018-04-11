IP=140.112.30.39
Port=5858
all:
	javac src/*.java -d . -encoding MS950

run-server:
	java -cp . bin/Server

run-client:
	java -cp . bin/Client $(IP) $(Port)

clean:
	rm -f bin/*.class


