import java.util.Iterator;
import java.util.NoSuchElementException;
public class Queue<T> implements Iterable<T> {
	private int N;         // number of elements on queue
	private Node<T> first;    // beginning of queue
	private Node<T> last;     // end of queue

	// helper linked list class
	private static class Node<T> {
		public Node() { }
		public T item;
		public Node<T> next;
	}

	/**
	 * Create an empty queue.
	 */
	public Queue() {
		first = null;
		last  = null;
		N = 0;
	}

	/**
	 * Is the queue empty?
	 */
	public boolean isEmpty() {
		return first == null;
	}

	/**
	 * Return the number of items in the queue.
	 */
	public int size() {
		return N;
	}

	public T peek() {
		if (isEmpty()) throw new NoSuchElementException("Queue underflow");
		return first.item;
	}

	/**
	 * Add the item to the queue.
	 */
	public void enqueue(T item) {
		Node<T> oldlast = last;
		last = new Node<>();
		last.item = item;
		last.next = null;
		if (isEmpty()) first = last;
		else           oldlast.next = last;
		N++;
	}

	public T dequeue() {
		if (isEmpty()) throw new NoSuchElementException("Queue underflow");
		T item = first.item;
		first = first.next;
		N--;
		if (isEmpty()) last = null;
		return item;
	}

	public String toString() {
		StringBuilder s = new StringBuilder();
		for (T item : this)
			s.append(item + " ");
		return s.toString();
	}

	private static <T> boolean check(Queue<T> that) {
		int N = that.N;
		Queue.Node<T> first = that.first;
		Queue.Node<T> last = that.last;
		if (N == 0) {
			if (first != null) return false;
			if (last  != null) return false;
		}
		else if (N == 1) {
			if (first == null || last == null) return false;
			if (first != last)                 return false;
			if (first.next != null)            return false;
		}
		else {
			if (first == last)      return false;
			if (first.next == null) return false;
			if (last.next  != null) return false;

			int numberOfNodes = 0;
			for (Queue.Node<T> x = first; x != null; x = x.next) {
				numberOfNodes++;
			}
			if (numberOfNodes != N) return false;

			Queue.Node<T> lastNode = first;
			while (lastNode.next != null) {
				lastNode = lastNode.next;
			}
			if (last != lastNode) return false;
		}

		return true;
	}


	public Iterator<T> iterator()  {
		return new ListIterator();
	}

	private class ListIterator implements Iterator<T> {
		private Node<T> current = first;

		public boolean hasNext()  { return current != null;                     }
		public void remove()      { throw new UnsupportedOperationException();  }

		public T next() {
			if (!hasNext()) throw new NoSuchElementException();
			T item = current.item;
			current = current.next;
			return item;
		}
	}

	public void toGraphviz(String filename) {
		GraphvizBuilder gb = new GraphvizBuilder ();
		toGraphviz (gb, null, first);
		gb.toFile (filename, "rankdir=\"LR\"");
	}
	private void toGraphviz (GraphvizBuilder gb, Node<T> prev, Node<T> n) {
		if (n == null) { gb.addNullEdge (prev); return; }
		gb.addLabeledNode (n, n.item.toString ());
		if (prev != null) gb.addEdge (prev, n);
		toGraphviz (gb, n, n.next);
	}

	public static void main(String[] args) {
		Trace.drawStepsOfMethod("main");
		Trace.drawStepsOfMethod("enqueue");
		Trace.drawStepsOfMethod("dequeue");
		Trace.run();
		StdIn.fromString ("to be or not to - be - - that - - - is");
		Queue<String> q = new Queue<>();
		int count = 0;
		while (!StdIn.isEmpty()) {
			String item = StdIn.readString();
			if (!item.equals("-")) q.enqueue(item);
			else if (!q.isEmpty()) StdOut.print(q.dequeue() + " ");
		}
		StdOut.println("(" + q.size() + " left on queue)");
	}
}
