package bookLendingClient.model;

import java.io.Serializable;
import java.util.Iterator;

class QueueEmptyException extends Exception implements Serializable{
	private static final long serialVersionUID = 4777186567419220972L;
	public QueueEmptyException() {
		super("栈空时不能POP。");
	}
}
public class Queue<E> implements java.lang.Iterable<E>,Serializable{
	private static final long serialVersionUID = 7774408928669424205L;
	private class node implements Serializable{
		private static final long serialVersionUID = 511741148597692018L;
		private E value;
		private node next;
		public node(E value) {
			this.value = value;
			this.next = null;
		}
	}

	private node head;
	private node rear;
	private int size;
	public Queue() {
		this.head = null;
		this.rear = null;
		this.size = 0;
	}
	@Override
	public Iterator<E> iterator() {
		return new Iterator<E>() {
			private node work = head;
			@Override
			public boolean hasNext() {
				return this.work != null;
			}
			@Override
			public E next() {
				E temp = this.work.value;
				this.work = this.work.next;
				return temp;
			}
		
		};
	}
	public int size() {
		return this.size;
	}
	public boolean isEmpty() {
		if(this.size == 0)
			return true;
		return false;
	}
	public void push(E value) {
		node newNode = new node(value);
		if(this.head == null) {
			this.head = this.rear = newNode;
		}else {
			this.rear.next = newNode;
			this.rear = newNode;
		}
		this.size++;
	}
	public E pop() {
		if(isEmpty()) {
			try {
				throw new QueueEmptyException();
			}catch(QueueEmptyException e) {
				e.printStackTrace();
				return null;
			}
		}else {
			this.size--;
			E temp = this.head.value;
			this.head = this.head.next;
			return temp;
		}
	}
	public int getPos(E value) {
		node p = this.head;
		int pos = 0;
		while(p != null) {
			pos++;
			if(p.value.equals(value)) {
				return pos;
			}
			
			p = p.next;
		}
		return 0;
	}
	public void remove(E v) {
		if(this.head == null) {
			return;
		}
		if(this.head.value.equals(v) && this.size == 1) {
			this.rear = this.head = null;
			this.size--;
			return;
		}
		if(this.head.value.equals(v)) {
			this.head = this.head.next;
			this.size--;
			return;
		}else if(this.rear.value.equals(v)) {
			node p = this.head;
			while(p.next != this.rear) {
				p = p.next;
			}
			this.rear = p;
			p.next = null;
			this.size--;
			return;
		}
		node p = this.head;
		while(p.next != null && !p.next.value.equals(v)) {
			p = p.next;
		}
		if(p.next == null) {
			return;
		}
		if(p.next.value.equals(v)) {
			p.next = p.next.next;
			this.size--;
			return;
		}
	}
}
