package txt;

public class BinaryTree {
	private Node root;
	
	//Empty Tree
	public BinaryTree(){
		root = null;
	}
	
	public BinaryTree(Object rootData, BinaryTree left, BinaryTree right){
		root = new Node();
		root.data = rootData;
		root.left = left.root;
		root.right = right.root;
	}
	
	//Node subclass
	class Node{
		public Object data;
		public Node left;
		public Node right;
		
	}
	
	//Helper method for size()
	private static int height(Node n){
		if(n == null){
			return 0;
		}
		else{
			return 1 + Math.max(height(n.left), height(n.right));
		}
	}
	
	//Recursive method that calls height(Node n) to calculate the height of the tree
	public int height(){
		return height(root);
	}
}
