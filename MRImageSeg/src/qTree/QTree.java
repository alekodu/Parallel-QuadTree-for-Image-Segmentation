package qTree;

//import java.util.ArrayList;
//import java.util.List;

import qTree.Node.nodeType;

//@SuppressWarnings("deprecation")
public class QTree {

	private int height = 0;
	private Node root = null;
	
	public QTree (int x, int y, int imageWidth, int imageHeight, int[][] value, int threshold) {
		Node root = new Node(x, y, imageWidth, imageHeight, nodeType.VOID, null, 0, threshold);
		this.root = root;
		
		if (value != null) {
			this.root.setType(nodeType.LEAF);
			this.root.setValue(value);
			this.root.compMeanValue();
		}
	}
	
	public void incHeight () {
		this.height++;
	}
	
	public void decHeight () {
		this.height--;
	}
	
	public int getHeight () {
		return this.height;
	}
	
	public void resetHeight () {
		this.height = 0;
	}
	
	public Node getRoot () {
		return this.root;
	}
	
	public void clrTree () {
		getRoot().setType(nodeType.VOID);
		getRoot().setChildren(null, null, null, null);
		resetHeight();
	}
	
	/*public void blurLeaf (Node node) {
		int[][] nodeValue = (int[][]) node.getValue();
		if (nodeValue!=null && node.getType()==nodeType.LEAF)
			node.blurNode();
	}
	
	public void searchDeepestPrt (Node node, List<Node> parents) {
		if (node.getType() == nodeType.PTR) {
			searchDeepestPrt(node.getNWChild(), parents);
			searchDeepestPrt(node.getNEChild(), parents);
			searchDeepestPrt(node.getSWChild(), parents);
			searchDeepestPrt(node.getSEChild(), parents);
		} else if (node.getType() == nodeType.LEAF) {
			if (node.cmpDeepest(getHeight())) {
				parents.add(node.getParent());
				//parents.add(node);
				//System.out.println("Leaf found!");
			}
		}
	}
	
	public int[][] mergeTree () {
		while (getHeight() > 0) {
			List<Node> nodesToMerge = new ArrayList<Node>();
			searchDeepestPrt(getRoot(), nodesToMerge);
			if (nodesToMerge.size() > 0) {
				for (int i=0; i<nodesToMerge.size(); i++)
					nodesToMerge.get(i).mergeNode();
			} else {
				decHeight();
			}
		}
		
		return (int[][]) getRoot().getValue();
	}
	
	public void splitSubtree (Node node) {
		while (node.splitNode()) {			
			if (getHeight() < node.getDepth()+1)
				incHeight();
			splitSubtree(node.getNWChild());
			splitSubtree(node.getNEChild());
			splitSubtree(node.getSWChild());
			splitSubtree(node.getSEChild());
		}
		
		blurLeaf(node);
		
		//int[][] nodeValue = (int[][]) node.getValue();
		//for (int i=0; i<nodeValue.length; i++) {
		//	for (int j=0; j<nodeValue[0].length; j++) {
		//		System.out.print(nodeValue[i][j] + " ");
		//	}
		//	System.out.println();
		//}
	}	
	
	public static void main (String args[]) {
		int[][] test = new int[9][12];
		
		for (int i=0; i<test.length; i++) {
			for (int j=0; j<test[i].length; j++) {
				test[i][j] = (i+1)*(j+1);
				System.out.print(test[i][j] + " ");
			}
			System.out.println();
		}
		
		QTree testTree = new QTree(0, 0, test.length, test[0].length, test, 20);
		
		//int[][] rootValue = (int[][]) testTree.getRoot().getValue();
		//for (int i=0; i<rootValue.length; i++) {
		//	for (int j=0; j<rootValue[i].length; j++) {
		//		System.out.print(rootValue[i][j] + " ");
		//	}
		//	System.out.println();
		//}
		
		testTree.splitSubtree(testTree.getRoot());
		System.out.println(testTree.getHeight());
		
		List<Node> leafs = new ArrayList<Node>();
		testTree.searchDeepestPrt(testTree.getRoot(), leafs);
		System.out.println(leafs.size());
		//for (int n=0; n<leafs.size(); n++) {
		//	//System.out.println(leafs.get(n).getType());
		//	int[][] leafMat = (int[][]) leafs.get(n).getValue();
			
		//	for (int i=0; i<leafMat.length; i++) {
		//		for (int j=0; j<leafMat[i].length; j++) {
		//			System.out.print(leafMat[i][j] + " ");
		//		}
		//		System.out.println();
		//	}			
		//}
		
		//System.out.println((testTree.getRoot().getValue()==null)?"null!":"value!");
		testTree.mergeTree();
		System.out.println(testTree.getHeight());
		
		int[][] rootValue = (int[][]) testTree.getRoot().getValue();
		for (int i=0; i<rootValue.length; i++) {
			for (int j=0; j<rootValue[i].length; j++) {
				System.out.print(rootValue[i][j] + " ");
			}
			System.out.println();
		}
	}*/		
}