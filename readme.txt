Instructions on how to run the project
	1. 	Install Java version 8.151. Any other version may or may not execute the code properly
	2. 	Launch the project using "java -jar PROJECT.jar" without the quotation marks from command lines
	3. 	When prompt with the input file, supply using a relative path from the JAR or an absolute path

Instructions on compiling the project from scratch
	1. 	Install the maven build tool from https://maven.apache.org/download.cgi
	2. 	Navigate to the project folder containing the pom.xml file
	3. 	Execute "mvn clean" without quotation marks from command lines to clean previous builds of the project 
		(this will take some time for first time execution)
	4. 	Execute "mvn compile assembly:single" without quotation marks to compile and build the project into a standalone JAR
	5. 	Navigate to the target folder
	6. 	Execute the project using "java -jar project-0.0.1-SNAPSHOT-jar-with-dependencies.jar" without quotation marks