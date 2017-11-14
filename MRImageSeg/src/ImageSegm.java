import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;
import mapreduce.framework.Mapper;
import mapreduce.framework.Pair;
import mapreduce.framework.Reducer;
import mapreduce.framework.WorkFlow;
import qTree.*;

@SuppressWarnings("deprecation")
public class ImageSegm {
	BufferedImage image;
	int width;
	int height;
	
	public static class QTMapper implements Mapper<Integer, Node> {

		@Override
		public MultiMap<Integer, Node> map(Pair<Integer, Node> inputPair) {
			// TODO Auto-generated method stub
			MultiMap <Integer, Node> outputMM = new MultiValueMap<>();
			Node node = inputPair.getValue();
			boolean splitState = node.splitNode();
			if (splitState) {
				outputMM.put(node.getDepth()+1, node.getNWChild());
				outputMM.put(node.getDepth()+1, node.getNEChild());
				outputMM.put(node.getDepth()+1, node.getSWChild());
				outputMM.put(node.getDepth()+1, node.getSEChild());
			}
			return outputMM;
		}
	}
	
	public static class QTReducer implements Reducer<Integer, Node> {
		
		@Override
		public Pair<Integer, Node> reduce(Pair<Integer, Collection<Node>> inPair) {
			// TODO Auto-generated method stub
			Node parent = inPair.getValue().iterator().next();
			parent.mergeNode();
			
			Pair <Integer, Node> outPair = new Pair<Integer, Node>(parent.getDepth(), parent);
						
			return outPair;
		}	
	}
	
	public ImageSegm (int threshold, int numMapThreads, int numRedThreads, String inputImage, String outputImage) {
		try {
			File input = new File(inputImage);
			image = ImageIO.read(input);
			width = image.getWidth();
			height = image.getHeight();
			
			int intensMat[][] = new int[width][height]; // Squared intensity matrix
			
			// RGB to HSI -> Intensity matrix construction
			for (int i=0; i<height; i++){
				for(int j=0; j<width; j++){
					Color c = new Color(image.getRGB(j, i));
					int intensity = (int)(c.getRed()+c.getGreen()+c.getBlue())/3;
					intensMat[j][i] = intensity;
				}
			}
			
			long startTime, endTime;
			//long[] elapsedTimes = new long[20];
			long elapsedTimes = 0;
			int[][] redMat;
			//for (int i=0; i<elapsedTimes.length; i++) {
				startTime = System.nanoTime();
			
				QTree testTree = new QTree(0, 0, intensMat.length, intensMat[0].length, intensMat, threshold);
				Node root = testTree.getRoot();
				ArrayList<Pair<Integer, Node>> nodeLst = new ArrayList<>();
				nodeLst.add(new Pair<Integer, Node> (root.getDepth(), root));
				
				WorkFlow<Integer, Node> wf = new WorkFlow<Integer, Node>(new QTMapper(), new QTReducer(),
						numMapThreads, numRedThreads);
	
				//System.out.println(testTree.getHeight());
				//System.out.println(testTree.getRoot().getType());
	
				MultiMap<Integer, Node> outMapMM = new MultiValueMap<>();
				outMapMM.put(0, testTree.getRoot());
				
				// Iterative map stage execution
				while (!nodeLst.isEmpty()) {
					MultiMap<Integer, Node> tmpMapMM = wf.parMapDriver(nodeLst);
					nodeLst.clear();
					outMapMM.putAll(tmpMapMM); // Add new generated subtrees to outMapMM
					
					Set<Integer> keys = tmpMapMM.keySet();
					for (int key : keys) {
						Collection<?> values = (Collection<?>) tmpMapMM.get(key);
						Iterator<?> valuesIterator = values.iterator();
						int i1 = 0;
						while (valuesIterator.hasNext()) {
							i1++;
							Node node = (Node) valuesIterator.next();
							Pair<Integer, Node> entry = new Pair<Integer, Node>(node.getDepth(), node);
							if (node.checkThreshold()) {
								nodeLst.add(entry);
							} else {
								node.blurNode();
							}
							//System.out.println(Integer.toString(node.getMean()) + node.getDepth() + node.getType() + node.getThreshold());
						}
						if (i1>0)
							testTree.incHeight();
					}
				}
				
				//System.out.println(testTree.getHeight());
				// Iterative reduce stage execution
				while (testTree.getHeight()>0) {
					MultiMap<Integer, Node> tmpRedMM = new MultiValueMap<>();
					Collection<?> deepestPtrs = (Collection<?>) outMapMM.get(testTree.getHeight()-1);				
					Iterator<?> ptrsIt = deepestPtrs.iterator();
					int i1 = 0;
					while (ptrsIt.hasNext()) {
						i1++;
						Node node = (Node) ptrsIt.next();
						tmpRedMM.put(i1, node);
						//System.out.println(Integer.toString(node.getMean()) + node.getDepth() + node.getType() + node.getThreshold());
					}
					if (i1>0)
						testTree.decHeight();
					
					wf.parRedDriver(tmpRedMM);
				}
				endTime = System.nanoTime();
				elapsedTimes = endTime-startTime;
				//elapsedTimes[i] = endTime-startTime;
			//}
			
			//System.out.println("Total parallel execution time [ns]: " + statistics(elapsedTimes));
			System.out.println("Total parallel execution time [ns]: " + elapsedTimes);
			
			//System.out.println(testTree.getRoot().getType());
			//System.out.println(testTree.getHeight());
			
			redMat = (int[][]) testTree.getRoot().getValue();
			
			// Intensity to GrayColor -> Output image construction
			for (int i=0; i<height; i++){
				for(int j=0; j<width; j++){
					int intensity = redMat[j][i];
					Color newC = new Color(intensity, intensity, intensity);
					image.setRGB(j, i, newC.getRGB());
				}
			}
			
			File ouptut = new File(outputImage);
			ImageIO.write(image, "jpg", ouptut);
		} catch (Exception e) {}
	}
	
	private static long statistics (long[] elapsedTimes) {
		long sum = 0;
		long min = Long.MAX_VALUE;
		long max = 0;
		long et;
		for (int i=0; i<elapsedTimes.length; i++) {
		 et = elapsedTimes[i];
		 sum += et;
		 if (et>max) max = et;
		 if (et<min) min = et;
		}		
		return (sum-max-min)/(elapsedTimes.length-2);
		//return (sum)/(elapsedTimes.length);	
	}	
	
	public static void main (String args[]) throws Exception {
		if (args.length < 5) {
			System.err.println("Usability: ImageSegm threshold inputImage outputImage numMapThreads numRedThreads");
			System.exit(0);
		}
		try {
			int threshold = Integer.parseInt(args[0]); // 0 - 255
			if (threshold < 0 || threshold > 255) {
				System.err.println("Threshold must be in the range 0-255!");
				System.exit(0);
			}
			String inputImage = args[1];
			String outputImage = args[2];
			int numMapThreads = Integer.parseInt(args[3]);
			int numRedThreads = Integer.parseInt(args[4]);
			
			ImageSegm obj = new ImageSegm(threshold, numMapThreads, numRedThreads, inputImage, outputImage);
		} catch (NumberFormatException e) {
			System.err.println("ImageSegm threshold inputImage outputImage numMapThreads numRedThreads: All arguments must be "
					+ "positive integers!");
		}
		
	}
}
