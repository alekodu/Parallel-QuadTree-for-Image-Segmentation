package qTree;
public class Node {
	
	private int depth = 0;
	private int threshold = 0;
	private int mean = 0;
	private int x = 0;
	private int y = 0;
	private int w = 0;
	private int h = 0;
	private Node parent = null;
	private Node nWChild = null;
	private Node nEChild = null;
	private Node sWChild = null;
	private Node sEChild = null;
	private nodeType type = null;
	private Object value = null;
	
	public enum nodeType {
		VOID, PTR, LEAF
	}
	
	public Node (int x, int y, int w, int h, nodeType type, Node parent, int depth, int threshold) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.type = type;
		this.parent = parent;
		this.depth = depth;
		this.threshold = threshold;
	}
	
	public void clrMean () {
		this.mean = 0;
	}
	
	public void setMean (int mean) {
		this.mean = mean;
	}
	
	public int getMean () {
		return this.mean;
	}
	
	public void setDepth (int depth) {
		this.depth = depth;
	}
	
	public int getDepth () {
		return this.depth;
	}
	
	public void setThreshold (int threshold) {
		this.threshold = threshold;
	}
	
	public int getThreshold () {
		return this.threshold;
	}
	
	public boolean cmpDeepest (int height) {
		if (getType() == nodeType.LEAF) {
			return (getDepth()==height);
		} else {
			return false;
		}
	}
	
	public int[] getPoint () {
		int[] point = {this.x, this.y};
		return point;
	}
	
	public void setX (int x) {
		this.x = x;
	}
	
	public int getX () {
		return this.x;
	}
	
	public void setY (int y) {
		this.y = y;
	}
	
	public int getY () {
		return this.y;
	}
	
	public int[] getDimension () {
		int[] dim = {this.w, this.h};
		return dim;
	}
	
	public void setW (int w) {
		this.w = w;
	}
	
	public int getW () {
		return this.w;
	}
	
	public void setH(int h) {
		this.h = h;
	}
	
	public int getH () {
		return this.h;
	}
	
	public void setType (nodeType type) {
		this.type = type;
	}
	
	public nodeType getType () {
		return this.type;
	}
	
	public void setParent (Node parent) {
		this.parent = parent;
	}
	
	public Node getParent () {
		return this.parent;
	}
	
	public boolean setValue (Object value) {
		if (this.type == nodeType.LEAF) {
			this.value = value;
			return true;
		} else {
			return false;
		}
	}
	
	public Object getValue () {
		return this.value;
	}
	
	public boolean setChildren (Node nWChild, Node nEChild, Node sWChild, Node sEChild) {
		if (this.type == nodeType.PTR) {
			this.nWChild = nWChild;
			this.nEChild = nEChild;
			this.sWChild = sWChild;
			this.sEChild = sEChild;
			return true;
		} else {
			return false;
		}
	}
	
	public boolean setNWChild (Node nWChild) {
		if (this.type == nodeType.PTR) {
			this.nWChild = nWChild;
			return true;
		} else {
			return false;
		}
	}
	
	public boolean setNEChild (Node nEChild) {
		if (this.type == nodeType.PTR) {
			this.nEChild = nEChild;
			return true;
		} else {
			return false;
		}
	}
	
	public boolean setSWChild (Node sWChild) {
		if (this.type == nodeType.PTR) {
			this.sWChild = sWChild;
			return true;
		} else {
			return false;
		}
	}
	
	public boolean setSEChild (Node sEChild) {
		if (this.type == nodeType.PTR) {
			this.sEChild = sEChild;
			return true;
		} else {
			return false;
		}
	}
	
	public Node[] getChildren () {
		Node[] children = {this.nWChild, this.nEChild, this.sWChild, this.sEChild};
		return children;
	}
	
	public Node getNWChild () {
		return this.nWChild;
	}
	
	public Node getNEChild () {
		return this.nEChild;
	}	
	
	public Node getSWChild () {
		return this.sWChild;
	}
	
	public Node getSEChild () {
		return this.sEChild;
	}
	
	// stop condition
	public boolean checkThreshold () {
		int min = 255;
		int max = 0;
		int [][] nodeValue = (int[][]) getValue();
		for (int i=0; i<nodeValue.length; i++) {
			for (int j=0; j<nodeValue[0].length; j++) {
				if (min > nodeValue[i][j])
					min = nodeValue[i][j];
				if (max < nodeValue[i][j])
					max = nodeValue[i][j];
			}
		}
		
		int delta = max - min;
		if (delta > getThreshold())
			return true;
		else
			return false;
	}
	
	public void compMeanValue () {
		int[][] nodeValue = (int[][]) getValue();
		int count = 0;
		int sum = 0;
		for (int i=0; i<nodeValue.length; i++) {
			for (int j=0; j<nodeValue[0].length; j++) {
				count++;
				sum += nodeValue[i][j];
			}
		}	
		setMean(sum/count);
	}
	
	public void blurNode () {
		int [][] nodeValue = (int[][]) getValue();
		int mean = getMean();
		for (int i=0; i<nodeValue.length; i++) {
			for (int j=0; j<nodeValue[0].length; j++) {
				nodeValue[i][j] = mean;
			}
		}
		setValue(nodeValue);
	}
	
	public void blurAdjChildren (Node child1, Node child2) {
		int meanChild1 = child1.getMean();
		int meanChild2 = child2.getMean();
		if (Math.abs(meanChild1-meanChild2) < 1) {
			int meanAdj = (meanChild1+meanChild2)/2;
			int[][] valueChild1 = (int[][]) child1.getValue();
			int[][] valueChild2 = (int[][]) child2.getValue();
			for (int i=0; i<valueChild1.length; i++) {
				for (int j=0; j<valueChild1[0].length; j++) {
					valueChild1[i][j] = meanAdj;
				}
			}
			child1.setValue(valueChild1);
			
			for (int i=0; i<valueChild2.length; i++) {
				for (int j=0; j<valueChild2[0].length; j++) {
					valueChild2[i][j] = meanAdj;
				}
			}
			child2.setValue(valueChild2);
		}
	}
	
	public void blurChildren (Node parent) {
		Node nWChild = parent.getNWChild();
		Node nEChild = parent.getNEChild();
		Node sWChild = parent.getSWChild();
		Node sEChild = parent.getSEChild();
		
		int meanNW = nWChild.getMean();
		int meanNE = nEChild.getMean();
		int meanSW = sWChild.getMean();
		int meanSE = sEChild.getMean();		
		
		if (Math.abs(meanNW-meanNE) < 20 &&
			Math.abs(meanNW-meanSW) < 20 &&
			Math.abs(meanNW-meanSE) < 20 &&
			Math.abs(meanNE-meanSW) < 20 &&
			Math.abs(meanSE-meanSW) < 20 &&
			Math.abs(meanNE-meanSE) < 20) {
			
			int meanAdj = (meanNW+meanNE+meanSW+meanSE)/4;
			
			int[][] valueNW = (int[][]) nWChild.getValue();
			int[][] valueNE = (int[][]) nEChild.getValue();
			int[][] valueSW = (int[][]) sWChild.getValue();
			int[][] valueSE = (int[][]) sEChild.getValue();
			
			for (int i=0; i<valueNW.length; i++) {
				for (int j=0; j<valueNW[0].length; j++) {
					valueNW[i][j] = meanAdj;
				}
			}
			nWChild.setValue(valueNW);
			
			for (int i=0; i<valueNE.length; i++) {
				for (int j=0; j<valueNE[0].length; j++) {
					valueNE[i][j] = meanAdj;
				}
			}
			nEChild.setValue(valueNE);
			
			for (int i=0; i<valueSW.length; i++) {
				for (int j=0; j<valueSW[0].length; j++) {
					valueSW[i][j] = meanAdj;
				}
			}
			sWChild.setValue(valueSW);
			
			for (int i=0; i<valueSE.length; i++) {
				for (int j=0; j<valueSE[0].length; j++) {
					valueSE[i][j] = meanAdj;
				}
			}
			sEChild.setValue(valueSE);
		}
	}
	
	// split image
	public boolean splitNode () {
		if ((getType() == nodeType.LEAF) && (getValue() != null) && (checkThreshold())) {
			if ((getW() > 1) && (getH() > 1)) { // && (w%2 == 0) && (h%2 == 0)
				int newW = getW()/2;
				int rW = getW()%2;
				int newH = getH()/2;
				int rH = getH()%2;
				
				int[][] intenMat = (int[][]) getValue();
				int[][] nWMat = new int[newW][newH];
				int[][] nEMat = new int[newW+rW][newH];
				int[][] sWMat = new int[newW][newH+rH];
				int[][] sEMat = new int[newW+rW][newH+rH];
				
				int nWX = getX(); 
				int	sWX = getX();
				int nEX = getX()+newW;
				int sEX = getX()+newW;
				
				int nWY = getY();
				int nEY = getY();
				int sWY = getY()+newH;
				int sEY = getY()+newH;
				
				// nW subimage
				for (int i=0; i<newW; i++) { // intenMat.length
					for (int j=0; j<newH; j++) { // intenMat[i].length
						nWMat[i][j] = intenMat[i][j];
					}
				}
				
				// nE subimage
				for (int i=newW; i<2*newW+rW; i++) {
					for (int j=0; j<newH; j++) {
						nEMat[i-newW][j] = intenMat[i][j];
					}
				}
				
				// sW subimage
				for (int i=0; i<newW; i++) {
					for (int j=newH; j<2*newH+rH; j++) {
						sWMat[i][j-newH] = intenMat[i][j];
					}
				}
				
				// sE subimage
				for (int i=newW; i<2*newW+rW; i++) {
					for (int j=newH; j<2*newH+rH; j++) {
						sEMat[i-newW][j-newH] = intenMat[i][j];
					}
				}

				setValue(null);
				setType(nodeType.PTR);
				
				Node nWChild = new Node(nWX, nWY, newW, newH, nodeType.LEAF, this, getDepth()+1, this.threshold);
				Node nEChild = new Node(nEX, nEY, newW+rW, newH, nodeType.LEAF, this, getDepth()+1, this.threshold);
				Node sWChild = new Node(sWX, sWY, newW, newH+rH, nodeType.LEAF, this, getDepth()+1, this.threshold);
				Node sEChild = new Node(sEX, sEY, newW+rW, newH+rH, nodeType.LEAF, this, getDepth()+1, this.threshold);
				
				boolean nWState = nWChild.setValue(nWMat);
				boolean nEState = nEChild.setValue(nEMat);
				boolean sWState = sWChild.setValue(sWMat);
				boolean sEState = sEChild.setValue(sEMat);
				
				nWChild.compMeanValue();
				nEChild.compMeanValue();
				sWChild.compMeanValue();
				sEChild.compMeanValue();
				clrMean();
				
				boolean sCState = setChildren(nWChild, nEChild, sWChild, sEChild);
				
				if (nWState && nEState && sWState && sEState && sCState) {
					return true;
				} else {
					return false;
				}				
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	// merge image
	public boolean mergeNode () {
		if (getType() == nodeType.PTR 
				&& getNWChild().getType() == nodeType.LEAF && getNEChild().getType() == nodeType.LEAF
				&& getSWChild().getType() == nodeType.LEAF && getSEChild().getType() == nodeType.LEAF) {
			
			// Retrieving children information
			/*int nWX = getNWChild().getX();
			int nEX = getNEChild().getX();
			int sWX = getSWChild().getX();
			int sEX = getSEChild().getX();
			
			int nWY = getNWChild().getY();
			int nEY = getNEChild().getY();
			int sWY = getSWChild().getY();
			int sEY = getSEChild().getY();*/
			
			int nWW = getNWChild().getW();
			int nEW = getNEChild().getW();
			int sWW = getSWChild().getW();
			int sEW = getSEChild().getW();
			
			int nWH = getNWChild().getH();
			int nEH = getNEChild().getH();
			int sWH = getSWChild().getH();
			int sEH = getSEChild().getH();
			
			//blurAdjChildren(getNWChild(), getNEChild());
			//blurAdjChildren(getNWChild(), getSWChild());
			//blurAdjChildren(getNWChild(), getSEChild());
			//blurAdjChildren(getSWChild(), getNEChild());
			//blurAdjChildren(getSWChild(), getSEChild());
			//blurAdjChildren(getSEChild(), getNEChild());
			blurChildren(this);
			
			int[][] nWMat = (int[][]) getNWChild().getValue();
			int[][] nEMat = (int[][]) getNEChild().getValue();
			int[][] sWMat = (int[][]) getSWChild().getValue();
			int[][] sEMat = (int[][]) getSEChild().getValue();
			
			// Removing child nodes
			setNEChild(null);
			setNWChild(null);
			setSEChild(null);
			setSWChild(null);
			
			// Changing PTR to LEAF type
			setType(nodeType.LEAF);
			
			int[][] mergeMat = new int[getW()][getH()];			
			
			// nW image
			for (int i=0; i<nWW; i++) {
				for (int j=0; j<nWH; j++) {
					mergeMat[i][j] = nWMat[i][j];
				}
			}			
			
			// nE image
			for (int i=0; i<nEW; i++) {
				for (int j=0; j<nEH; j++) {
					mergeMat[i+nWW][j] = nEMat[i][j];
				}
			}			
			
			// sW image
			for (int i=0; i<sWW; i++) {
				for (int j=0; j<sWH; j++) {
					mergeMat[i][j+nWH] = sWMat[i][j];
				}
			}			
			
			// sE image
			for (int i=0; i<sEW; i++) {
				for (int j=0; j<sEH; j++) {
					mergeMat[i+nWW][j+nWH] = sEMat[i][j];
				}
			}
			
			setValue(mergeMat);
			compMeanValue();
			
			return true;
		} else {
			return false;
		}
	}
	
	/*public static void main (String args[]) {
		int[][] test = new int[2][1];
		
		for (int i=0; i<test.length; i++) {
			for (int j=0; j<test[i].length; j++) {
				test[i][j] = (i+1)*(j+1);
				System.out.print(test[i][j] + " ");
			}
			System.out.println();
		}
		
		Node testNode = new Node(0, 0, test.length, test[0].length, nodeType.LEAF, null, 0);		
		testNode.setValue(test);
		System.out.println(testNode.getType());
		
		boolean splitState = testNode.splitNode();
		System.out.println(splitState);
		System.out.println(testNode.getType());
		
		if (splitState) {
			System.out.println("NWX: " + testNode.getNWChild().getX());
			System.out.println("NWY: " + testNode.getNWChild().getY());
			System.out.println("NWW: " + testNode.getNWChild().getW());
			System.out.println("NWH: " + testNode.getNWChild().getH());
			int[][] nWMat = (int[][]) testNode.getNWChild().getValue();
			for (int i=0; i<nWMat.length; i++) {
				for (int j=0; j<nWMat[0].length; j++) {
					System.out.print(nWMat[i][j] + " ");
				}
				System.out.println();
			}
			
			System.out.println();
			System.out.println("NEX: " + testNode.getNEChild().getX());
			System.out.println("NEY: " + testNode.getNEChild().getY());
			System.out.println("NEW: " + testNode.getNEChild().getW());
			System.out.println("NEH: " + testNode.getNEChild().getH());
			int[][] nEMat = (int[][]) testNode.getNEChild().getValue();
			for (int i=0; i<nEMat.length; i++) {
				for (int j=0; j<nEMat[0].length; j++) {
					System.out.print(nEMat[i][j] + " ");
				}
				System.out.println();
			}
			
			System.out.println();
			System.out.println("SWX: " + testNode.getSWChild().getX());
			System.out.println("SWY: " + testNode.getSWChild().getY());
			System.out.println("SWW: " + testNode.getSWChild().getW());
			System.out.println("SWH: " + testNode.getSWChild().getH());
			int[][] sWMat = (int[][]) testNode.getSWChild().getValue();
			for (int i=0; i<sWMat.length; i++) {
				for (int j=0; j<sWMat[0].length; j++) {
					System.out.print(sWMat[i][j] + " ");
				}
				System.out.println();
			}
			
			System.out.println();
			System.out.println("SEX: " + testNode.getSEChild().getX());
			System.out.println("SEY: " + testNode.getSEChild().getY());
			System.out.println("SEW: " + testNode.getSEChild().getW());
			System.out.println("SEH: " + testNode.getSEChild().getH());
			int[][] sEMat = (int[][]) testNode.getSEChild().getValue();
			for (int i=0; i<sEMat.length; i++) {
				for (int j=0; j<sEMat[0].length; j++) {
					System.out.print(sEMat[i][j] + " ");
				}
				System.out.println();
			}
		
			boolean mergeState = testNode.mergeNode();
			System.out.println(mergeState);
			System.out.println(testNode.getType());
			int[][] mergeMat = new int[testNode.getW()][testNode.getH()];
			mergeMat = (int[][]) testNode.getValue();
			for (int i=0; i<mergeMat.length; i++) {
				for (int j=0; j<mergeMat[0].length; j++) {
					System.out.print(mergeMat[i][j] + " ");
				}
				System.out.println();
			}
		}
	}*/
}