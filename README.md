# CSV-sorter

maven project, java 8

the jar file path is target/VeevaAssignment-1.0-SNAPSHOT.jar

for running the project, need to send 2 parameters: 
args[0] = max elements in memory - the maximum values that we can store in memory
args[1] = input file path
optional - args[2] await termination time(in round hours) - the sort proccess uses threads for parallel ruuning.
the defalt termination time is 1 hour.

example run command:
java -jar target/VeevaAssignment-1.0-SNAPSHOT.jar 5 src/main/resources/test/test1.csv 

