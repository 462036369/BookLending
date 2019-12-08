package bookLendingClient.model;

import java.io.Serializable;
import java.util.Iterator;

import bookLendingClient.model.LinkedList;

enum Color{
	RED,BLACK
}
@SuppressWarnings({"unused"})
public class RedBlackTree<E extends Comparable<E>,R> implements Serializable,Iterable<R>{
	private static final long serialVersionUID = -409658742054721051L;
	private node root;
	public node getRoot() {
		return root;
	}

	public void setRoot(node root) {
		this.root = root;
	}

	private class node implements Serializable,Comparable<node>{
		private static final long serialVersionUID = 7013619912550016495L;
		private Color color;
		private E key;
		private R value;
		private node left;
		private node right;
		private node parent;
		public node(Color color, E key, R value, RedBlackTree<E, R>.node left, RedBlackTree<E, R>.node right,
				RedBlackTree<E, R>.node parent) {
			this.color = color;
			this.key = key;
			this.value = value;
			this.left = left;
			this.right = right;
			this.parent = parent;
		}
		public node() {
			this.left = this.right = this.parent = null;
		}
		/**
		 * 左旋
		 */
		private void leftRotate() {
			
			node other = this.right;//other为当前结点的右孩子
			this.right = other.left;//当前结点的右孩子变成原来右孩子的左孩子
			if(other.left != null)
				other.left.parent = this;//设置右孩子的左孩子的双亲为当前结点
			other.parent = this.parent;//设置当前子树的新根为当前节点的右孩子
			if(other.parent == null) {//如果当前节点为树的根
				setRoot(other);//设置新根为右孩子
			}else {//如果不是根
				if(this.parent.left == this) {//如果当前结点是父节点的左孩子
					this.parent.left = other;//设置新左孩子为原结点右孩子
				}else {
					this.parent.right = other;//否则设置新右孩子为原结点的右孩子
				}
			}
			other.left = this;//设置原右孩子的左孩子变成原子树的根
			this.parent = other;//设置原子树的双亲为新根
		}
		/**
		 * 右旋
		 */
		private void rightRotate() {
			//参考左旋，只是方向不同而已
			node other = this.left;
			this.left = other.right;
			if(other.right != null) {
				other.right.parent = this;
			}
			other.parent = this.parent;
			if(other.parent == null) {
				setRoot(other);
			}else {
				if(this.parent.left == this) {
					this.parent.left = other;
				}else {
					this.parent.right = other;
				}
			}
			other.right = this;
			this.parent = other;
		}
		@Override
		public int compareTo(RedBlackTree<E, R>.node o) {
			return this.key.compareTo(o.key);
		}
	}
	
	private void add(node newNode) {
		node now = this.root;
		node pre = null;
		//查找插入位置
		while(now != null) {
			pre = now;
			if(newNode.key.compareTo(now.key) < 0) {//比当前结点小
				now = now.left;//向左
			}else {
				now = now.right;//否则向右
			}
		}
		newNode.parent = pre;//设置插入结点的父节点
		if(pre != null) {
			if(newNode.key.compareTo(pre.key) < 0) {//如果比插入位置的键值小
				pre.left = newNode;//设置成左孩子
			}else {
				pre.right = newNode;//否则设置成右孩子
			}
		}else {
			this.root = newNode;//如果插入位置的父节点就是空，那便是根节点
		}
		newNode.color = Color.RED;
		insertFix(newNode);
		
	}
	private void insertFix(node newNode) {
		node parent,grand;
		while(((parent = newNode.parent) != null) && parent.color == Color.RED ) {
			grand = parent.parent;
			if(parent == grand.left) {//如果父节点是祖父节点的左孩子
				node uncle = grand.right;
				if(uncle != null && uncle.color == Color.RED) {//如果叔叔结点是红色
					uncle.color = Color.BLACK;
					parent.color = Color.BLACK;
					grand.color = Color.RED;
					newNode = grand;
					continue;
				}
				if(parent.right == newNode) {//如果叔叔结点是黑色且当前结点是右孩子
					node temp;
					parent.leftRotate();
					temp = parent;
					parent = newNode;
					newNode = temp;
				}
				//叔叔是黑色，且当前节点是左孩子
				parent.color = Color.BLACK;
				grand.color = Color.RED;
				grand.rightRotate();
			}else {//如果父节点是祖父节点的右孩子
				node uncle = grand.left;
				if(uncle != null && uncle.color == Color.RED) {//如果叔叔是红色
					uncle.color = Color.BLACK;
					parent.color = Color.BLACK;
					grand.color = Color.RED;
					newNode = grand;
					continue;
				}
				if(parent.left == newNode) {//如果叔叔是黑色且当前结点是父节点的左孩子
					parent.rightRotate();
					node temp = parent;
					parent = newNode;
					newNode = temp;
				}
				//叔叔黑色，且当前节点是右孩子
				parent.color = Color.BLACK;
				grand.color = Color.RED;
				grand.leftRotate();
			}
		}
		this.root.color = Color.BLACK;
	}

	public void add(E key,R value) {
		node newNode = new node(Color.BLACK, key, value, null, null, null);
		if(newNode != null) {
			add(newNode);
		}
	}
	
	public void remove(E key) {
		node newNode;
		if((newNode = removeSearch(key)) != null) {
			realRemove(newNode);
		}
	}
	
	private void realRemove(node newNode) {
		if(newNode.left != null && newNode.right != null) {//如果左右子树都不为空
			node s = newNode.right;//查找删除点的后继结点S
			while(s.left != null) {
				s = s.left;
			}
			newNode.key = s.key;//将后继点的信息复制到删除点
			newNode.value = s.value;
			newNode = s;//删除点转移成为后继点
		}
		//现在newNode要么指向删除点的后继点，要么指向一个至少一个孩子为空的删除点
		//不管指向哪一种，要么直接删除，要么用唯一的孩子替代
		node replace;
		if(newNode.left != null)    replace = newNode.left;
		else	replace = newNode.right;
		if(replace != null) {//当前点有唯一孩子
			replace.parent = newNode.parent;//孩子的双亲设置为目前删除点的双亲
			if(newNode.parent == null) {//如果删除点的双亲是空，代表删除点是根节点
				this.root = replace;//直接使用唯一孩子作为树的根节点
			}else if(newNode == newNode.parent.left) {//如果删除点是删除点双亲的左孩子
				newNode.parent.left = replace;//设置双亲的左孩子为替代点
			}else {
				newNode.parent.right = replace;//设置右孩子为替代点
			}
			newNode.left = newNode.right = newNode.parent = null;//删除点所有引用全部置空
			if(newNode.color == Color.BLACK) {//如果删除点的颜色是黑色，代表需要更新，如果删除一个红色结点，那对整棵树不会有任何的影响
				removeFix(replace);//从替代点开始更新，“黑+黑”
			}
		}else if(newNode.parent == null) {//如果当前点没有孩子，且双亲为空，代表要删除的是根节点
			this.root = null;//根节点置空
		}else {//如果当前点没有孩子，且双亲非空
			if(newNode.color == Color.BLACK) {//如果此点是黑色，删除黑色节点会导致不平衡，所以更新
				removeFix(newNode);//向情况4靠拢
			}
			if(newNode.parent != null) {//如果双亲非空
				if(newNode == newNode.parent.left) {//设置双亲指向自己的引用指向空
					newNode.parent.left = null;
				}else if(newNode == newNode.parent.right){
					newNode.parent.right = null;
				}
				newNode.parent = null;//将自己的双亲置空
			}
		}
		
	}
	
	private void removeFix(node x) {
		//与《算法导论》情况及处理相同
		while(x != this.root && x.color == Color.BLACK) {//当x既不是根节点也不是黑色的时候一直更新
			if(x == x.parent.left) {//如果x是其父节点的左孩子
				node sib = x.parent.right;//存储x的兄弟
				if(sib.color == Color.RED) {//如果兄弟结点是红色，对应情况1
					sib.color = Color.BLACK;//兄弟结点变黑
					x.parent.color = Color.RED;//父节点变红
					x.parent.leftRotate();//父节点左旋
					sib = x.parent.right;//重新设置x的兄弟结点
				}
				if((sib.left == null || sib.left.color == Color.BLACK) && (sib.right == null || sib.right.color == Color.BLACK)) {//兄弟结点的左右孩子都是黑色
					//兄弟节点是黑色，且俩侄子都是黑色，对应情况2
					sib.color = Color.RED;//兄弟的颜色变红
					x = x.parent;//x更新为x的双亲
				}else {//兄弟节点是黑色，且俩侄子有一个不是黑色
					if(sib.right == null || sib.right.color == Color.BLACK) {
						//右侄子是黑色，对应情况3
						sib.left.color = Color.BLACK;//左侄子变黑
						sib.color = Color.RED;//兄弟变红
						sib.rightRotate();//兄弟右旋
						sib = x.parent.right;//重新设置兄弟节点
					}
					//左侄子是黑色，对应情况4
					sib.color = x.parent.color;//兄弟节点变成父节点颜色
					x.parent.color = Color.BLACK;//父节点变黑
					sib.right.color = Color.BLACK;//右侄子变黑
					x.parent.leftRotate();//父节点左旋
					x = this.root;//x设为根节点
				}
				
			}else {
				//与上面相同，替换所有左右
				node sib = x.parent.left;
				if(sib.color == Color.RED) {
					sib.color = Color.BLACK;
					x.parent.color = Color.RED;
					x.parent.rightRotate();
					sib = x.parent.left;
				}
				if((sib.left == null || sib.left.color == Color.BLACK) && (sib.right == null || sib.right.color == Color.BLACK)) {//兄弟结点的左右孩子都是黑色
					sib.color = Color.RED;
					x = x.parent;
				}else {
					if(sib.left == null || sib.left.color == Color.BLACK) {
						sib.right.color = Color.BLACK;
						sib.color = Color.RED;
						sib.leftRotate();
						sib = x.parent.left;
					}
					sib.color = x.parent.color;
					x.parent.color = Color.BLACK;
					sib.left.color = Color.BLACK;
					x.parent.rightRotate();
					x = this.root;
				}
			}
		}
		x.color = Color.BLACK;
	}

	public R get(E key) {
		node cur = this.root;
		while(cur != null) {
			if(key.compareTo(cur.key) < 0) {
				cur = cur.left;
			}else if(key.compareTo(cur.key) > 0) {
				cur = cur.right;
			}else {
				return cur.value;
			}
		}
		return null;
	}
	
	private node removeSearch(E key) {
		node cur = this.root;
		while(cur != null) {
			if(key.compareTo(cur.key) < 0) {
				cur = cur.left;
			}else if(key.compareTo(cur.key) > 0) {
				cur = cur.right;
			}else {
				return cur;
			}
		}
		return null;
	}

	public boolean test() {
		if(this.root == null) {
			return true;
		}
		if(this.root.color == Color.RED) {
			return false;
		}
		int count = 0;
		node cur = this.root;
		while(cur != null) {
			if(cur.color == Color.BLACK) {
				count++;
			}
			cur = cur.left;
		}
		int num = 0;
		return roadTest(this.root,count,num);
	}

	private boolean roadTest(node root, int count, int num) {
		if(root == null) {
			return true;
		}
		if(root.color == Color.RED && root.parent != null && root.parent.color == Color.RED) {
			System.out.println(root.key + "有连续红节点");
			return false;
		}
		if(root.color == Color.BLACK) {
			num++;
		}
		if(root.left == null && root.right == null) {
			if(num != count) {
				System.out.println(root.key + "黑节点数量不一样");
				return false;
			}
		}
		return roadTest(root.left, count, num) && roadTest(root.right, count, num);
	}

	@Override
	public Iterator<R> iterator() {
		
		return new Iterator<R>() {
			node it = getFirstNode();
			
			public boolean hasNext() {
				return it != null;
			}

			private RedBlackTree<E, R>.node getFirstNode() {
				node now = root;
				while(now.left != null) {
					now = now.left;
				}
				return now;	
			}

			@Override
			public R next() {
				node res = it;
				it = getNext(it);
				return res.value;
			}

			private RedBlackTree<E, R>.node getNext(RedBlackTree<E, R>.node t) {
				if(t.right != null) {
					t = t.right;
					while(t.left != null) {
						t = t.left;
					}
					return t;
				}else {
					node parent = t.parent;
					if(parent == null) {
						return null;
					}
					while(parent != null && parent.right == t) {
						t = t.parent;
						parent = t.parent;
					}
					if(t.right != parent) {
						return parent;
					}
					return null;
				}

			}
		};
	}

	private void getList(node t,LinkedList<RedBlackTree<E, R>.node> list) {
		if(t != null) {
			getList(t.left, list);
			list.add(t);
			getList(t.right,list);
		}
	}
}
