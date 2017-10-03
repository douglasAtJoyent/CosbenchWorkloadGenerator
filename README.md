# CosbenchWorkloadGenerator
A file generator to create workloads for cosbench, more useful to those who are at Joyent since this will produce some configs that are specific to our cosbench driver.

This will create a file, when processed by Cosbench will create a directory tree in Manta, and then add files to the last directories in that tree. This will usually take several init stages, the reason being is that cosbench will not process things in a top to bottom fashion but it sometimes does things rather randomly. So each level of the tree is added in it's own init. This file can be used for a test, but there maybe a few things one may want to tweak from the test, this does not add all configurations for the test, but it tries to create a test that is usable. 

Property  file example:
depth=7
resultFile=./benchmarkLoad.xml
branches=2
leaves=2
directoryNameLength=4
filesize=2MB2
readRatio=20

depth - the depth of the tree that will be created for reading and writing. 
resultFile - the location of the file that is written.
branches - the number of directories at each level for reading.
leaves - the number of files at the lowest point in the tree.
directoryNameLength - the length of the name of each of the directories.
fileszie - the size of the files that will be created e.g. (2)MB 
readRatio - the ratio of reads to writes in the main section.

Useage:
 1. git clone https://github.com/douglasAtJoyent/CosbenchWorkloadGenerator
 2. go to where you clone project.
 3. mvn package
 4. edit the generator.property to suite your needs.
 5. run java -jar target/CosbenchWorkloadGenerator-0.0.1-SNAPSHOT.jar 
 At this point the file should be produced.
 
