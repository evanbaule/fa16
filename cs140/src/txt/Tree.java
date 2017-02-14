package txt;

import java.util.ArrayList;
import java.util.List;

public class Tree {
	private Node root;
	
	//Nested class.. considered an implementation detail
	class Node{
		public Object data;
		public List<Node> children;
		
		//Recursive size helper, called from within method size() in Tree class
		public int size(){
			int sum = 0;
			for(Node child: children){
				sum += child.size();
			}
			
			return sum + 1;
		}
		
	}
	
	//Calls recursive size() method from within Node class
	public int size() {
		//Takes into account the possibility of an empty tree
		if(root == null){
			return 0;
		}
		else{
			return root.size();
		}
	}
	
	public Tree(Object rootData){
		root = new Node();
		root.data = rootData;
		root.children = new ArrayList<>();
	}
	
	public void addSubtree(Tree subtree){
		root.children.add(subtree.root);
	}
	
	
}
