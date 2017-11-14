import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import qTree.*;

import javax.imageio.ImageIO;

public class ImageSegm {
	BufferedImage image;
	int width;
	int height;
	
	public ImageSegm (int threshold, String inputImage, String outputImage) {
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
			long[] elapsedTimes = new long[20];
			for (int i=0; i<elapsedTimes.length; i++) {
				startTime = System.nanoTime();
				QTree testTree = new QTree(0, 0, intensMat.length, intensMat[0].length, intensMat, 70);
				testTree.splitSubtree(testTree.getRoot());
				//System.out.println(testTree.getHeight());
				intensMat = testTree.mergeTree();
				//System.out.println(testTree.getHeight());
				endTime = System.nanoTime();
				elapsedTimes[i] = endTime-startTime;
			}
			
			System.out.println("Total sequential execution time [ns]: " + statistics(elapsedTimes));
			
			// Intensity to GrayColor -> Output image construction
			for (int i=0; i<height; i++){
				for(int j=0; j<width; j++){
					int intensity = intensMat[j][i];
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
		if (args.length < 3) {
			System.err.println("Usability: ImageSegm threshold inputImage outputImage");
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
			
			ImageSegm obj = new ImageSegm(threshold, inputImage, outputImage);
		} catch (NumberFormatException e) {
			System.err.println("ImageSegm threshold inputImage outputImage: All arguments must be "
					+ "positive integers!");
		}
		
	}
}