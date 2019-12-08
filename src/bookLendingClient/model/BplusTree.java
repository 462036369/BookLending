package bookLendingClient.model;

import java.io.Serializable;
import java.util.Iterator;

class MyEntry<E extends Comparable<E>,R> implements Comparable<MyEntry<E,R>>,Serializable{
	private static final long serialVersionUID = -396978163501927756L;
	private E key;
	private R value;
	public MyEntry(E key, R value) {
		this.key = key;
		this.value = value;
	}
	public MyEntry() {
	}
	public E getKey() {
		return key;
	}
	public void setKey(E key) {
		this.key = key;
	}
	public R getValue() {
		return value;
	}
	public void setValue(R value) {
		this.value = value;
	}
	@Override
	public int compareTo(MyEntry<E, R> o) {
		return this.key.compareTo(o.key);
	}

}
@SuppressWarnings({"unused"})
public class BplusTree<E extends Comparable<E>,R> implements Serializable,Iterable<R>{
	private static final long serialVersionUID = -4858311249383304239L;
	private class node implements Comparable<node>,Serializable{
		private static final long serialVersionUID = 6646795256415593413L;
		private boolean isLeaf;
		private boolean isRoot;
		private node parent;
		private node previous;
		private node next;
		private LinkedList<MyEntry<E,R>> entries;
		private LinkedList<node> children;
		

		node(boolean isLeaf){
			this.isLeaf = isLeaf;
			this.entries = new LinkedList<>();
			if(!this.isLeaf) {
				this.children = new LinkedList<>();
			}
		}
		node(boolean isLeaf,boolean isRoot){
			this(isLeaf);
			this.isRoot = isRoot;
		}
		public LinkedList<MyEntry<E,R>> getEntries(){
			return this.entries;
		}
		
		/**
		 * 获取指定键值所对应的实值
		 * @param key 指定的键值，与大类泛型相同，需实现Comparable接口，具有比较大小的功能
		 * @return 返回对应key的实值
		 */
		private R get(E key) {
			//如果是叶子结点
			if(this.isLeaf) {
				Iterator<MyEntry<E,R>> it = this.entries.iterator();
				MyEntry<E,R> temp;
				//遍历叶子节点的关键字
				while(it.hasNext()) {
					temp = it.next();
					if(temp.getKey().compareTo(key) == 0) {
						return temp.getValue();
					}
				}
				return null;//未找到
			}else {//如果不是叶子节点，根据大小关系递归查找
				if(key.compareTo(this.entries.get(0).getKey()) <= 0) {//如果比当前节点第一个关键字还小
					return this.children.get(0).get(key);//递归查找第一个孩子
				}else if(key.compareTo(this.entries.get(this.entries.size() - 1).getKey()) >= 0) {//如果比最后一个关键字还大
					return this.children.get(this.children.size() - 1).get(key);//递归查找最后一个孩子
				}else {
					for(int i = 0;i < this.entries.size() - 1;i++) {//否则遍历所有关键字
						if(this.entries.get(i).getKey().compareTo(key) <= 0 && this.entries.get(i + 1).getKey().compareTo(key) > 0) {
							//找到一个比前一个关键字大比后一个关键字小的位置
							return this.children.get(i).get(key);//从这个位置递归查询
						}
					}
				}
				
			}
			return null;
		}
		
		private R getNext(E key) {
			
			if(this.isLeaf) {
				Iterator<MyEntry<E,R>> it = this.entries.iterator();
				MyEntry<E,R> temp;
				//遍历叶子节点的关键字
				while(it.hasNext()) {
					temp = it.next();
					if(temp.getKey().compareTo(key) == 0) {
						if(it.hasNext()) {//如果当前关键字还有下一个关键字
							temp = it.next();//获取下一个关键字
							return temp.getValue();//返回下一个关键字的实值
						}else if(this.next != null) {//如果没有下一个关键字
							//如果存在后继结点
							if(this.next.entries != null) {
								return this.next.entries.get(0).getValue();//返回后继节点的第一个关键字的实值
							}
						}//如果既没有下一个关键字，也没有后继结点
						return null;
					}
				}
				return null;//未找到
			}else {//如果不是叶子节点，根据大小关系递归查找
				if(key.compareTo(this.entries.get(0).getKey()) <= 0) {//如果比当前节点第一个关键字还小
					return this.children.get(0).get(key);//递归查找第一个孩子
				}else if(key.compareTo(this.entries.get(this.entries.size() - 1).getKey()) >= 0) {//如果比最后一个关键字还大
					return this.children.get(this.children.size() - 1).get(key);//递归查找最后一个孩子
				}else {
					for(int i = 0;i < this.entries.size() - 1;i++) {//否则遍历所有关键字
						if(this.entries.get(i).getKey().compareTo(key) <= 0 && this.entries.get(i + 1).getKey().compareTo(key) > 0) {
							//找到一个比前一个关键字大比后一个关键字小的位置
							return this.children.get(i).get(key);//从这个位置递归查询
						}
					}
				}
				
			}
			return null;
		}
		
		/**
		 * 插入新节点，如果键值已存在，则更新实值
		 * @param key 要插入的键值
		 * @param value 要插入的实值
		 */
		private void insert(E key,R value) {
			if(this.isLeaf) {//如果当前结点已经是叶子节点了
				if(judge(key) || this.entries.size() < order) {//如果当前结点存在该键值或者还未达到阶数
					//可以直接插入或者更新
					insertOrUpdate(key,value);
					if(this.parent != null) {//父节点不为空
						this.parent.updateInsert();//更新父节点
					}
				}else {//当前结点需要分裂
					insertOrUpdate(key,value);
					node left = new node(true);//叶子结点分裂后一定是叶子节点
					node right = new node(true);
					//设置叶子节点横向链表的关系
					if(this.previous != null) {
						this.previous.next = left;
						left.previous = this.previous;
					}
					if(this.next != null) {
						this.next.previous = right;
						right.next = this.next;
					}
					if(this.previous == null) {
						setHead(left);
					}
					left.next = right;
					right.previous = left;
					this.previous = null;
					this.next = null;
					
					int leftSize = (order + 1) / 2 + (order + 1) % 2;
					int rightSize = (order + 1) / 2;
					for(int i = 0;i < leftSize;i++) {
						left.entries.add(this.entries.get(i));
					}
					for(int i = 0;i < rightSize;i++) {
						right.entries.add(this.entries.get(leftSize + i));
					}
					
					if(this.parent != null) {
						int pos = this.parent.children.indexOf(this);//寻找当前结点在父节点的孩子列表中找到当前结点的位置
						this.parent.children.remove(pos);//将自身从父节点孩子列表中删除
						left.parent = right.parent = this.parent;
						this.parent.children.add(pos, left);
						this.parent.children.add(pos + 1,right);
						this.children = null;
						this.entries = null;
						this.parent.updateInsert();//保险起见，将父节点更新一下
						
						this.parent = null;
					}else {
						this.isRoot = false;
						node parent = new node(false,true);
						setRoot(parent);
						left.parent = parent;
						right.parent = parent;
						parent.children.add(left);
						parent.children.add(right);
						
						this.children = null;
						this.entries = null;
						parent.updateInsert();
					}
				}
			}else {//如果不是叶子结点
				if(key.compareTo(this.entries.get(0).getKey()) <= 0) {
					this.children.get(0).insert(key, value);
				}else if(key.compareTo(this.entries.get(this.entries.size() - 1).getKey()) >= 0){
					this.children.get(this.children.size() - 1).insert(key, value);
				}else {
					for(int i = 0;i < this.entries.size() - 1;i++) {
						if(this.entries.get(i).getKey().compareTo(key) <= 0 && this.entries.get(i + 1).getKey().compareTo(key) > 0) {
							this.children.get(i).insert(key, value);
							break;
						}
					}
				}
			}

		}
		
		
		/**
		 * 由于插入操作使得当前节点或父节点不满足条件，需要调整
		 */
		private void updateInsert() {
			validate(this);
			//如果孩子数超过了阶数，需要分裂
			if(this.children.size() > order) {
				node left = new node(false);//分解的结点有孩子结点，所以一定不是叶子节点
				node right = new node(false);
				//左右结点的关键字长度
				int leftSize = (order + 1) / 2 + (order + 1) % 2;
				int rightSize = (order + 1) / 2;
				//复制子节点信息
				for(int i = 0;i < leftSize;i++) {
					left.children.add(this.children.get(i));
					left.entries.add(new MyEntry<E,R>(this.children.get(i).entries.get(0).getKey(),null));
					this.children.get(i).parent = left;
				}
				for(int i = 0;i < rightSize;i++) {
					right.children.add(this.children.get(leftSize + i));
					right.entries.add(new MyEntry<E,R>(this.children.get(leftSize + i).entries.get(0).getKey(),null));
					this.children.get(leftSize + i).parent = right;
				}
				//如果还有父节点
				if(this.parent != null) {
					int pos = this.parent.children.indexOf(this);//寻找当前结点在父节点的孩子列表中找到当前结点的位置
					this.parent.children.remove(pos);//将自身从父节点孩子列表中删除
					left.parent = right.parent = this.parent;
					this.parent.children.add(pos, left);
					this.parent.children.add(pos + 1,right);
					this.children = null;
					this.entries = null;
					this.parent.updateInsert();//保险起见，将父节点更新一下
					this.parent = null;
				}else {//如果是根节点的话，分裂后原根节点就不是根节点了
					this.isRoot = false;
					node parent = new node(false,true);
					setRoot(parent);
					left.parent = parent;
					right.parent = parent;
					parent.children.add(left);
					parent.children.add(right);
					
					this.children = null;
					this.entries = null;
					parent.updateInsert();
				}
			}
		}
		
		
		/**
		 * 使结点合法化
		 * @param node 结点
		 */
		private void validate(BplusTree<E, R>.node node) {
			//如果关键字数量和孩子数量相同
			if(node.entries.size() == node.children.size()) {
				for(int i = 0;i < node.entries.size();i++) {//遍历当前结点的关键字
					E key = node.children.get(i).entries.get(0).getKey();//取得当前关键字对应的孩子结点的第一个关键字
					if(node.entries.get(i).getKey().compareTo(key) != 0) {//如果不相等
						node.entries.remove(i);//移除后在相同位置添加更新过的新关键字
						node.entries.add(i,new MyEntry<E,R>(key,null));
						if(!node.isRoot) {//如果不是根节点
							validate(node.parent);//递归向上更新
						}
					}
				}
			}else if(node.isRoot && node.children.size() >= 2 || node.children.size() >= order / 2 && node.children.size() <= order
					 && node.children.size() >= 2) {
				//如果关键字数量和孩子数量不相等
				//如果当前节点是根节点且孩子数量>=2
				//或者孩子数量大于等于阶数一半且小于等于阶数且要大于2（B+树性质）
				node.entries.clear();//清空所有关键字
				for(int i = 0;i < node.children.size();i++) {
					E key = node.children.get(i).entries.get(0).getKey();//取得第i个孩子的第1个关键字

					node.entries.add(new MyEntry<E,R>(key,null));//根据孩子的关键字添加当前节点的关键字
					if(!node.isRoot) {
						validate(node.parent);//如果不是根节点，递归向上更新
					}
				}
			}
		}
		
		
		/**
		 * 插入或者更新结点
		 * @param key 要插入的键值
		 * @param value 要插入的实值
		 */
		private void insertOrUpdate(E key, R value) {
			MyEntry<E,R> entry = new MyEntry<E,R>(key, value);
			if(this.entries.size() == 0) {//如果当前结点的关键字列表为空
				this.entries.add(entry);//直接插入到列表中
				return;
			}
			for(int i = 0;i < this.entries.size();i++) {//遍历列表，列表是从小到大有序的
				//所以如果有等于的，一定是先遍历到等于的
				if(this.entries.get(i).getKey().compareTo(key) == 0) {
					this.entries.get(i).setValue(value);//更新键值对应的实值
					return;
				}else if(this.entries.get(i).getKey().compareTo(key) > 0) {
					//第一个大于参数键值的位置
					if(i == 0) {//如果第一个值就大于键值
						this.entries.add(0, entry);//插入到首位
						return;
					}else {
						this.entries.add(i,entry);//否则插入到列表中第一个大于他的元素的前边
						return;
					}
				}
			}
			this.entries.add(this.entries.size(),entry);//都没有插入或更新，插入到尾部
		}
		
		
		/**
		 * 判断当前结点是否存在该键值
		 * @param key 需要判断的键值
		 * @return 是否已有该键值
		 */
		private boolean judge(E key) {
			Iterator<MyEntry<E,R>> it = this.entries.iterator();
			MyEntry<E,R> temp;
			//遍历当前结点的所有关键字
			while(it.hasNext()) {
				temp = it.next();
				if(temp.getKey().compareTo(key) == 0) {
					return true;
				}
			}
			return false;
		}
		
		/**
		 * 删除指定键值的结点
		 * @param key 指定的键值
		 */
		private void remove(E key) {
			if(this.isLeaf) {//如果当前结点是叶子结点
				if(!this.judge(key)) {//如果当前叶子结点不存在该键值
					return;//直接返回，无法删除不存在的键值
				}
				if(this.isRoot) {//如果既是叶子节点也是根节点（只有一层）
					realRemove(key);
				}else {//如果只是叶子节点
					if(this.entries.size() > order / 2 && this.entries.size() > 2) {//如果关键字数量大于阶数一半
						//即删除后依然满足b+树条件
						realRemove(key);
					}else {//小于等于阶数,删除一个元素后自身一定不满足条件了
						if(this.previous != null && this.previous.entries.size() > order / 2 && this.previous.entries.size() > 2 
								&& this.previous.parent == this.parent) {
							//如果跟自己相同父节点的前驱结点删除一个还可以满足条件，就从前驱结点借一个补充到自己结点
							MyEntry<E,R> entry = this.previous.entries.get(this.previous.entries.size() - 1);//取到前驱结点的最后一个关键字
							this.previous.entries.remove(this.previous.entries.size() - 1);//删除点前驱结点中最后一个结点
							this.entries.add(0, entry);//将其补充到自己的首位
							realRemove(key);//正常删除关键字
						}else if(this.next != null && this.next.entries.size() > order / 2 && this.next.entries.size() > 2 
								&& this.next.parent == this.parent) {
							//如果前驱结点不能借补，就尝试从后继结点借补
							MyEntry<E,R> entry = this.next.entries.get(0);//取得后继结点的第一个关键字
							this.next.entries.remove(0);//移除后继结点的第一个关键字
							this.entries.add(entry);//添加到自己关键字的尾部
							realRemove(key);//正常删除关键字
						}else {//无法借补，只能通过合并结点来完成
							if(this.previous != null && (this.previous.entries.size() <= order / 2 || this.previous.entries.size() <= 2) && 
									this.previous.parent == this.parent) {
								//如果前驱结点和自身是同一个父节点，但前驱结点刚好满足，删除后一定不满足，考虑和前驱结点合并
								for(int i = this.previous.entries.size() - 1;i >= 0;i--) {
									//从后到前从前驱结点关键字中取出，插入到自身关键字首位
									this.entries.add(0,this.previous.entries.get(i));
								}
								realRemove(key);//正常删除关键字
								this.previous.entries = null;//销毁前驱结点
								this.previous.parent = null;
								this.parent.children.remove(this.previous);
								//更新叶子链表
								if(this.previous.previous != null) {
									node temp = this.previous;//临时指向前驱
									temp.previous.next = this;//前驱的前驱的下一个等于自身，相当于删除掉前驱
									this.previous = temp.previous;//自身的前驱指向前驱的前驱
									temp.previous = null;
									temp.next = null;
								}else {//前驱的前驱为空，即前驱本来是链表头结点
									//将自身设置为头结点
									setHead(this);
									this.previous.next = null;
									this.previous = null;
								}
							}else if(this.next != null && (this.next.entries.size() <= order / 2 || this.next.entries.size() <= 2) && 
									this.next.parent == this.parent) {
								//同后继结点合并
								for(int i = 0;i < this.next.entries.size();i++) {
									this.entries.add(this.next.entries.get(i));//顺序添加到自身尾部
								}
								realRemove(key);
								this.next.parent = null;
								this.parent.children.remove(this.next);
								if(this.next.next != null) {
									node temp = this.next;
									temp.next.previous = this;
									this.next = temp.next;
									temp.next = null;
									temp.previous = null;
								}else {
									this.next.previous = null;
									this.next = null;
								}
							}
						}
					}
					this.parent.removeUpdate();
				}
				
			}else {
				//如果不是叶子结点
				if(key.compareTo(this.entries.get(0).getKey()) <= 0) {
					this.children.get(0).remove(key);
				}else if(key.compareTo(this.entries.get(this.entries.size() - 1).getKey()) >= 0) {
					this.children.get(this.entries.size() - 1).remove(key);
				}else {
					for(int i = 0;i < this.entries.size() - 1;i++) {
						if(this.entries.get(i).getKey().compareTo(key) <= 0 && this.entries.get(i + 1).getKey().compareTo(key) > 0) {
							this.children.get(i).remove(key);
							break;
						}
					}
				}
			}
		}
		
		/**
		 * 移除关键字后的更新
		 */
		private void removeUpdate() {
			validate(this);
			//如果子节点数不满足条件，需要于子节点合并
			if(this.children.size() < order / 2 || this.children.size() < 2) {
				if(this.isRoot) {//如果是根节点的话
					if(this.children.size() >= 2) {//孩子数大于等于2就满足条件
						return;
					}else {//否则需要合并
						node root = this.children.get(0);
						root.parent = null;
						root.isRoot = true;
						setRoot(root);
						this.entries = null;
						this.children = null;
					}
				}else {//如果不是根节点
					//从当前结点的父节点那里算出当前结点的“前驱”结点和“后继”结点
					int thisPos = this.parent.children.indexOf(this);
					int prePos = thisPos - 1;
					int nextPos = thisPos + 1;
					node pre = null,next = null;
					if(prePos >= 0) {
						pre = this.parent.children.get(prePos);
					}
					if(nextPos < this.parent.children.size() - 1) {
						next = this.parent.children.get(nextPos);
					}
					if(pre != null && pre.children.size() > order / 2 && pre.children.size() > 2) {
						//如果当前结点的“前驱”满足条件，则从“前驱”处借补
						node temp = pre.children.get(pre.children.size() - 1);
						pre.children.remove(pre.children.size() - 1);
						temp.parent = this;
						this.children.add(0,temp);
						validate(pre);
						validate(this);
						this.parent.removeUpdate();
					}else if(next != null && next.children.size() > order / 2 && next.children.size() > 2) {
						//如果“后继”结点满足条件，从“后继”结点借补
						node temp = next.children.get(0);
						next.children.remove(0);
						temp.parent = this;
						this.children.add(temp);
						validate(next);
						validate(this);
						this.parent.removeUpdate();
					}else {
						//无法借补，需要合并
						if(pre != null && (pre.children.size() <= order / 2 || pre.children.size() <= 2)) {
							//如果“前驱”结点满足条件
							for(int i = pre.children.size() - 1;i >= 0;i--) {//遍历“前驱结点”的孩子
								node child = pre.children.get(i);
								this.children.add(0,child);
								child.parent = this;
							}
							pre.children = null;
							pre.entries = null;
							pre.parent = null;
							this.parent.children.remove(pre);
							validate(this);
							this.parent.removeUpdate();
						}else if(next != null && (next.children.size() <= order / 2 || next.children.size() <= 2)) {
							//如果“后继”结点满足条件
							for(int i = 0;i < next.children.size();i++) {
								node child = next.children.get(i);
								this.children.add(child);
								child.parent = this;
							}
							next.children = null;
							next.parent = null;
							next.entries = null;
							this.parent.children.remove(next);
							validate(this);
							this.parent.removeUpdate();
						}
					}
				}
			}
		}
		/**
		 * 真正执行删除操作
		 * @param key 要删除的键值
		 */
		private void realRemove(E key) {
			int pos = -1;
			for(int i = 0;i < this.entries.size();i++) {//遍历当前节点所有关键字
				if(this.entries.get(i).getKey().compareTo(key) == 0) {//如果存在和要删除的关键字相同的关键字
					pos = i;//记录存在的关键字的位置
					break;
				}
			}
			if(pos != -1) {//确实存在相同的关键字
				this.entries.remove(pos);//从关键字列表中删除
			}
			
		}
		
		@Override
		public int compareTo(BplusTree<E, R>.node o) {
			return 0;
		}
		
	}
	private node root;
	private node head;
	private int order;
	public BplusTree(int order) {
		if(order < 3) {
			System.out.println("阶数太低了");
			System.exit(0);
		}
		this.order = order;
		this.head = this.root = new node(true,true);
		
	}
	public node getRoot() {
		return root;
	}
	public void setRoot(node root) {
		this.root = root;
	}
	public node getHead() {
		return head;
	}
	public void setHead(node head) {
		this.head = head;
	}
	public R get(E key) {
		return this.root.get(key);
	}        
	public R getNext(E key) {
		
		return this.root.getNext(key);
	}
	public void add(E key,R value) {
		this.root.insert(key, value);
	}
	public void remove(E key) {
		this.root.remove(key);
	}
	@Override
	public Iterator<R> iterator() {
		return new Iterator<R>() {
			private node p = head;
			private int pos = 0;
			@Override
			public boolean hasNext() {
				return this.p != null;
			}

			@Override
			public R next() {
				R temp = this.p.entries.get(this.pos).getValue();
				this.pos++;
				if(this.pos == p.entries.size()) {
					this.pos = 0;
					this.p = this.p.next;
				}
				return temp;
			}
		};
	}
	
}
