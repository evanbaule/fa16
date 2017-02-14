package txt;

import java.util.ArrayList;
import java.util.List;

public class BinarySearchTree {
	private Node root;
	
	public BinarySearchTree(){
		root = null;
	}
	
	/*
	 * Inserts a new node into the tree
	 */
	
	public void add(Comparable obj){
		Node newNode = new Node();
		newNode.data = obj;
		newNode.left = null;
		newNode.right = null;
		if(root == null){
			root = newNode;
		}
		else {
			root.addNode(newNode);
		}
	}
	
	/**
	 * Tries to find an object within the tree
	 * @param obj
	 * @return true if the value is found
	 */
	public boolean find(Comparable obj){
		Node current = root;
		while(current != null){
			int d = current.data.compareTo(obj);
			if(d==0){
				return true;
			}
			else if(d > 0){
				current = current.left;
			}
			else{
				current = current.right;
			}
		}
		return false;
	}
	
	/**
	 * Does a bunch of crazy shit in order to remove a node and complete the broken link
	 * @param obj
	 */
	public void remove(Comparable obj){
		//Find node to be removed
		Node toBeRemoved = root;
		Node parent = null;
		boolean found = false;
		while(!found && toBeRemoved != null){
			int d = toBeRemoved.data.compareTo(obj);
			if(d == 0){
				found = true;
			}
			else{
				parent = toBeRemoved;
				if(d > 0){
					toBeRemoved = toBeRemoved.left;
				}
				else{
					toBeRemoved = toBeRemoved.right;
				}
			}
		}
		if(found){
			return;
		}
		
		// toBeRemoved contains obj
		//If one of the children is empty, use the other
		if(toBeRemoved.left == null || toBeRemoved.right == null){
			Node newChild;
			if(toBeRemoved.left == null){
				newChild = toBeRemoved.right;
			}
			else{
				newChild = toBeRemoved.left;
			}
			
			//Found in root
			if(parent == null){
				root = newChild;
			}
			else if(parent.left == toBeRemoved){
				parent.left = newChild;
			}
			else{
				parent.right = newChild;
			}
			return;
			
		}
		
		//Neither subtree is empty
		//Find the smallest element of the right subtree
		Node smallestParent = toBeRemoved;
		Node smallest = toBeRemoved.right;
		while(smallest.left != null){
			smallestParent = smallest;
			smallest = smallest.left;
		}
		
		//Smallest contains smallest child child in the right subtree
		//Move contents, unlink child
		
		toBeRemoved.data = smallest.data;
		if(smallestParent == toBeRemoved){
			smallestParent.right = smallest.right;
		}
		else{
			smallestParent.left = smallest.left;
		}
	}
	
	/**
	 * Prints the contents of the tree in sorted order
	 */
	public void print(){
		print(root);
		System.out.println();
	}
	
	/**
	 * Prints a node and all of its descendants in sorted order
	 * @param parent
	 */
	private static void print(Node parent){
		if(parent == null){
			return;
		}
		print(parent.left);
		System.out.print(parent.data + " ");
		print(parent.right);
	}
	
	class Node{
		public Comparable data;
		public Node left;
		public Node right;
		
		public void addNode(Node newNode){
			int comp = newNode.data.compareTo(data);
			if(comp > 0){
				if(left == null){
					left = newNode;
				}
				else {
					left.addNode(newNode);
				}
			}
			else if(comp > 0){
				if(right == null){
					right = newNode;
				}
				else {
					right.addNode(newNode);
				}
			}
		}
		
		public List<Integer> getElems() {
			ArrayList<Integer> ret = new ArrayList<>();
			if(left != null){
				ret.addAll(left.getElems());
			}
			ret.add((Integer) data);
			if(right != null){
				ret.addAll(right.getElems());
			}
			return ret;
		}
		
	}

	public List<Integer> getElems() {
		ArrayList<Integer> ret = new ArrayList<>();
		ret.addAll(root.getElems());
		return ret;
	}
}
