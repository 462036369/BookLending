package bookLendingClient.model;

import java.io.Serializable;
import java.util.Iterator;

public class LinkedList<E> implements java.lang.Iterable<E>,Serializable{
	private static final long serialVersionUID = 3247549310497240264L;
	private class node implements Serializable,Comparable<node>{
		private static final long serialVersionUID = -4316821351497053921L;
		private E value;
		private node next;
		public node(E value) {
			this.value = value;
			this.next = null;
		}
		@Override
		public int compareTo(node o) {
			if(this.value instanceof Comparable) {
				return ((Comparable<E>) this.value).compareTo(o.value);
			}
			return 0;
		}
	}
	private node head;
	private node rear;
	private int size;
	public LinkedList() {
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
	public void add(E value) {
		this.size++;
		node newNode = new node(value);
		if(this.head == null) {
			this.head = this.rear = newNode;
		}else{
			this.rear.next = newNode;
			this.rear = newNode;
		}
	}
	public void add(int pos,E value) {
		
		node newNode = new node(value);
		if(pos < 0 || pos > this.size) {
			return;
		}
		if(this.head == null && pos == 0) {
			this.head = this.rear = newNode;
			this.size++;
			return;
		}
		if(pos == 0) {
			newNode.next = this.head;
			this.head = newNode;
			this.size++;
			return;
		}else if(pos == this.size) {
			add(value);
			return;
		}else {
			node p = this.head;
			while(pos > 0)
			{
				p = p.next;
				pos--;
			}
			newNode.next = p.next;
			p.next = newNode;
			swap(p,newNode);
			if(newNode.next == null) {
				this.rear = newNode;
			}
			this.size++;
		}
		
	}
	private void swap(node a,node b) {
		E temp = a.value;
		a.value = b.value;
		b.value = temp;
	}
	private node getMidNode(node left,node right) {
		node mark = left;
		node p = left;
		node q = left.next;
		while(q != right) {
			if(q.compareTo(mark) < 0) {
				p = p.next;
				
				this.swap(p,q);
			}
			q = q.next;
		}
		this.swap(left,p);
		return p;
	}
	private void quickSort(node begin,node end) {
		if(begin != end) {
			node mid = this.getMidNode(begin, end);
			quickSort(begin, mid);
			quickSort(mid.next, end);
		}
	}
	public void sorted() {
		this.quickSort(this.head, null);
	}
	public E get(int i) {
		if(i < 0 || i >= this.size) {
			return null;
		}
		node p = this.head;
		while(i > 0 && p != null) {
			p = p.next;
			i--;
		}
		if(p == null) {
			return null;
		}
		return p.value;
	}
	public void remove(int pos) {
		if(this.head == null) {
			return;
		}
		if(pos < 0 || pos >= this.size) {
			return;
		}
		if(pos == 0 && this.size == 1) {
			this.head = this.rear = null;
			this.size--;
			return;
		}
		if(pos == 0) {
			this.head = this.head.next;
			this.size--;
			return;
		}else if(pos == this.size - 1) {
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
		pos--;
		while(pos > 0) {
			p = p.next;
			pos--;
		}
		p.next = p.next.next;
		this.size--;
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
	public void clear() {
		this.head = this.rear = null;
		this.size = 0;
	}
	public int indexOf(E value) {
		node p = this.head;
		for(int i = 0;i < this.size;i++) {
			if(p.value.equals(value)) {
				return i;
			}
			p = p.next;
		}
		return -1;
	}
}
