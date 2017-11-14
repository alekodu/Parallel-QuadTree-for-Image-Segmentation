# Parallel-QuadTree-for-Image-Segmentation
Parallel QuadTree Algorithm Implementation for Image Segmentation using MapReduce.

The quadtree algorithm for image segmentation presented in this report has been implemented using Java language. This implementation uses a propietary MapReduce framework for which the provided JAR library file MapReducev5.jar must be included in the class-path (also with the apache common utilities commons-collections-4-4.1.jar which includes the MultiValueMap data structure).

The implemented program can be compiled and executed as follows:

## Compilation:
	javac -cp commons-collections4-4.1.jar:MapReducev5.jar:. ImageSegm.java
## Execution:
	java -cp commons-collections4-4.1.jar:MapReducev5.jar:. ImageSegm threshold inputImage outputImage numMapThreads numRedThreads

The submitted source code folder for this assignment contains the following subfolders:
## SeqImageSegm: 
	This folder contains the source codes for the sequential algorithm implementation.
## MRImageSegm:
	This folder contains the source codes for the parallel algorithm implementation using MapReduce.
