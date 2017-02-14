package txt;

import java.util.ListIterator;
import java.util.NoSuchElementException;

public class LinkedList {
	class Node {
		public Object data;
		public Node next;
		public Node previous;
	}

	private Node first;
	private Node last;
	
	public LinkedList() {
		first = null;
	}

	/*
	 * First node manipulation
	 */

	public Object getFirst() {
		if (first == null) {
			throw new NoSuchElementException();
		} else {
			return first.data; // Returns the data stored in the first node, if
								// it exists
		}
	}

	public void addFirst(Object element) {
		Node newNode = new Node();
		newNode.data = element; // Builds new Node w parameter as data
		newNode.next = first; // Attached newNode to the front of the list
		first = newNode; // Assigns first address to newNode, making it the new
							// reference to first
	}

	public Object removeFirst() {
		if (first == null) {
			throw new NoSuchElementException();
		}
		Object element = first.data; // duplicates the first object to be
										// returned
		first = first.next; // cuts first element off, and attached 'first'
							// address to the second element
		return element; // returns the copied first element, as the real first
						// will be collected
	}

	/*
	 * ListIterator build/use
	 */

	class LinkedListIterator implements ListIterator {
		private Node position;
		private Node previous;
		private boolean isAfterNext;

		public LinkedListIterator() {
			position = null;
			previous = null;
			isAfterNext = false;
		}

		public Object next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			previous = position;
			isAfterNext = true;
			if (position == null) {
				position = first;
			} else {
				position = position.next;
			}

			return position.data;
		}

		@Override
		public void add(Object element) {
			if (position == null) {
				addFirst(element);
				position = first;
			} else {
				Node newNode = new Node();
				newNode.data = element;
				newNode.next = position.next;
				position.next = newNode;
				position = newNode;
			}
			isAfterNext = false;

		}

		@Override
		public boolean hasNext() {
			if (position == null) {
				return first != null;
			} else {
				return position.next != null;
			}
		}

		@Override
		public boolean hasPrevious() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public int nextIndex() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Object previous() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int previousIndex() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void remove() {
			if (!isAfterNext) {
				throw new IllegalStateException();
			}

			if (position == first) {
				removeFirst();
			}

			else {
				previous.next = position.next;
			}

			position = previous;
			isAfterNext = false;

		}

		@Override
		public void set(Object element) {
			if(!isAfterNext){
				throw new IllegalStateException();
			}
			
			position.data = element;

		}
	}
}
