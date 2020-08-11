

/**
 * FibonacciHeap
 *
 * An implementation of fibonacci heap over integers.
 */
public class FibonacciHeap
{

	private int size, numTrees, numMarked;
	private HeapNode start;
	private HeapNode min;
	static int totalCuts;
	static int totalLinks;

   /**
    * public boolean isEmpty()
    *
    * precondition: none
    * 
    * The method returns true if and only if the heap
    * is empty.
    *   
    */
    public boolean isEmpty()
    {
    	return size==0;
    }
		
   /**
    * public HeapNode insert(int key)
    *
    * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap. 
    */
    public HeapNode insert(int key)
    {    
    	HeapNode toInsert = new HeapNode(key);
    	if (this.isEmpty()) {
    		this.min = toInsert;
    		this.start = toInsert;
    		this.size = 1;
    		this.numTrees = 1;
    		toInsert.setNext(toInsert);
    		toInsert.setPrev(toInsert);
    		return toInsert;
    	}
    	if (toInsert.getKey() < this.min.getKey()) {
    		this.min = toInsert;
    	}
    	toInsert.setNext(this.start);
    	this.start.getPrev().setNext(toInsert);
    	toInsert.setPrev(this.start.getPrev());
    	toInsert.getPrev().setNext(toInsert);
    	this.start.setPrev(toInsert);
    	this.start = toInsert;
    	this.numTrees++;
    	this.size++;
    	return toInsert;
    }

   /**
    * public void deleteMin()
    *
    * Delete the node containing the minimum key,
    * by using the method link to create a legal binomial heap.
    *
    */
    public void deleteMin()
    {
    	HeapNode z=this.min;
    	this.size--;
    	if(size==0) { //size was 1
    		this.min=null;
    		this.numMarked=0;
    		this.numTrees=0;
    		this.start=null;
    		return;
    	}
    	HeapNode[] arr=new HeapNode[((int)Math.floor(Math.log(size)/Math.log(2)))*2+1];
    	if(this.numTrees==1) {
    		this.numTrees=this.start.getRank();
    		this.start=this.start.getChild(); //won't be null execption because size was bigger than 1
    		this.start.setParent(null);
    	}
    	if (z.getChild()!=null) {
			z.getPrev().setNext(z.getChild());
			HeapNode tmp=z.getChild().getPrev();
			z.getChild().setPrev(z.getPrev());
			tmp.setNext(z.getNext());
			z.getNext().setPrev(tmp);
			if(z==this.start) {
				this.start=z.getChild();
			}
    	}
    	else {
			z.getPrev().setNext(z.getNext());
			z.getNext().setPrev(z.getPrev());
			if(z==this.start) {
				this.start=this.start.getNext();
			}
    	}
    	HeapNode x=this.start;
    	while(x!=null) {                       
    		x.setParent(null);
    		if(x.isMarked()) {
    			x.setMarked(false);
    			this.numMarked--;
    		}
    		HeapNode y=x;
    		x=x.getNext();
    		while(arr[y.getRank()]!=null) {
    			y=link(arr[y.getRank()],y);
    			arr[y.getRank()-1]=null;
    		}
    		arr[y.getRank()]=y;
			if(x==this.start) {
				break;
			}                              
    	}
    	HeapNode tmp=null;
		boolean flag=true;
    	for(int i=0;i<arr.length;i++) {
    		if(arr[i]!=null) {
    			if(flag) {
    				this.numTrees=1;
    				this.start=arr[i];
    				this.min=arr[i];
    				flag=false;
    				this.start.setNext(this.start);
    				this.start.setPrev(this.start);
    			}
    			else {
    				this.numTrees++;
    				tmp.setNext(arr[i]);
    				arr[i].setPrev(tmp);
    				if(arr[i].getKey()<this.min.getKey()) {
    					this.min=arr[i];
    				}
    			}
				tmp=arr[i];
    		}
    	}
    	tmp.setNext(this.start);
    	this.start.setPrev(tmp);
    }
    
    /**
     * private HeapNode link(HeapNode x,HeapNode y)
     * 
     * linking two trees to one,
     * the root with the smaller key will be the new root.
     * 
     */
    private HeapNode link(HeapNode x,HeapNode y)
    {
    	if (x.getKey()>y.getKey()) {
    		HeapNode tmp=x;
    		x=y;
    		y=tmp;
    		y.setNext(null);
    	}
    	else {
    		x.setNext(null);
    	}
    	x.setNext(x);
    	x.setPrev(x);
    	if (x.getChild()==null) {
    		y.setNext(y);
    		y.setPrev(y);
    	}
    	else {
    		y.setNext(x.getChild());
    		y.setPrev(x.getChild().getPrev());
    		x.getChild().setPrev(y);
    		y.getPrev().setNext(y);
    	}
    	x.setChild(y);
    	y.setParent(x);
    	x.setRank(x.getRank()+1);
    	this.numTrees--;
    	totalLinks++;
    	return x;
    }
   /**
    * public HeapNode findMin()
    *
    * Return the node of the heap whose key is minimal. 
    *
    */
    public HeapNode findMin()
    {
    	return this.min;
    } 
    
   /**
    * public void meld (FibonacciHeap heap2)
    *
    * Meld the heap with heap2
    *
    */
    public void meld (FibonacciHeap heap2)
    {
    	if(!heap2.isEmpty()&&heap2!=null) {
    		this.numMarked+=heap2.numMarked;
    		this.numTrees+=heap2.numTrees;
    		this.size+=heap2.size;
    		if(this.isEmpty()) {
    			this.min=heap2.min;
    			this.start=heap2.start;
    		}
    		else {
    			if(this.min.getKey()>heap2.min.getKey()) {
    				this.min=heap2.min;
    			}
    			this.start.getPrev().setNext(heap2.start);
    			this.start.setPrev(heap2.start.getPrev());
    			heap2.start.setPrev(this.start.getPrev());
    			this.start.getPrev().setNext(this.start);
    		}
    	}
    }

   /**
    * public int size()
    *
    * Return the number of elements in the heap
    *   
    */
    public int size()
    {
    	return this.size;
    }
    	
    /**
    * public int[] countersRep()
    *
    * Return a counters array, where the value of the i-th entry is the number of trees of order i in the heap. 
    * 
    */
    public int[] countersRep()
    {
    	if (this.isEmpty()) {
    		return new int[0];
    	}
    	int max = this.start.getRank();
    	HeapNode x = this.start.getNext();
    	while (x != this.start) {
    		if (x.getRank() > max) {
    			max = x.getRank();
    		}
    		x = x.getNext();
    	}
    	int[] arr = new int[max+1];
    	arr[x.getRank()]++;
    	x = this.start.getNext();
    	while (x != this.start) {
    		arr[x.getRank()]++;
    		x = x.getNext();
    	}
        return arr;
    }
	
   /**
    * public void delete(HeapNode x)
    *
    * Deletes the node x from the heap,
    * by using the methods decreaseKey and deleteMin. 
    *
    */
    public void delete(HeapNode x) 
    {    
    	int delta = x.getKey() - this.min.getKey() + 1; 
    	this.decreaseKey(x, delta);
    	this.deleteMin();
    }

   /**
    * public void decreaseKey(HeapNode x, int delta)
    *
    * The function decreases the key of the node x by delta. The structure of the heap should be updated
    * to reflect this chage (for example, the cascading cuts procedure should be applied if needed).
    */
    public void decreaseKey(HeapNode x, int delta)
    {    
    	x.setKey(x.getKey() - delta);
    	if (x.getParent() != null) {
    		if (x.getParent().getKey() > x.getKey()) {
    			if (x.getParent().isMarked()) {
    				this.cascadingCut(x, x.getParent()); 
    			}
    			else {
    				if (x.getParent().getParent() != null) {
    					x.getParent().setMarked(true);
    					this.numMarked++;
    				}
    				this.cut(x, x.getParent());
    				
    			}
    		}
    	}
    	if (this.min.getKey() > x.getKey()) {
    		this.min = x;
    	}
    }

   /**
    * public int potential() 
    *
    * This function returns the current potential of the heap, which is:
    * Potential = #trees + 2*#marked
    * The potential equals to the number of trees in the heap plus twice the number of marked nodes in the heap. 
    */
    public int potential() 
    {    
    	return this.numTrees +2*this.numMarked;
    }
    /**
     * public void cut(HeapNode x, HeapNode y)
     * 
     * The function cuts node x from its parent, y.
     */
    public void cut(HeapNode x, HeapNode y) {
    	totalCuts++;
    	x.setParent(null);
    	//this.start = x;
    	if (x.isMarked()) {
    		x.setMarked(false);
    		this.numMarked--;
    	}
    	y.setRank(y.getRank() - 1);
    	if (x.getNext() == x) {
    		y.setChild(null);
    	}
    	else {
    		if (y.getChild() == x) {
    			y.setChild(x.getNext());
    		}
    		x.getPrev().setNext(x.getNext());
    		x.getNext().setPrev(x.getPrev());
    	}
    	x.setNext(this.start);
		this.start.getPrev().setNext(x);
		x.setPrev(this.start.getPrev());
		this.start.setPrev(x);
		this.start=x;
    	this.numTrees++;
    }
    
    /**
     * public void cascadingCut(HeapNode x, HeapNode y)
     * 
     * Perform a cascading-cut process starting at x, thus meaning,
     * cut x from its parent, y. Cut y from its parent if y was marked, and so on. 
     */
    
    public void cascadingCut(HeapNode x, HeapNode y) {
    	this.cut(x, y);
    	if (y.getParent() != null) {
    		if (!y.isMarked()) {
    			y.setMarked(true);
    			this.numMarked++;
    		}
    		else {
    			this.cascadingCut(y, y.getParent());
    		}
    	}
    	if (y.getParent() == null && y.isMarked()) {
    		y.setMarked(false);
    		this.numMarked--;
    		
    	}
    }

   /**
    * public static int totalLinks() 
    *
    * This static function returns the total number of link operations made during the run-time of the program.
    * A link operation is the operation which gets as input two trees of the same rank, and generates a tree of 
    * rank bigger by one, by hanging the tree which has larger value in its root on the tree which has smaller value 
    * in its root.
    */
    public static int totalLinks()
    {    
    	return totalLinks; // should be replaced by student code
    }

   /**
    * public static int totalCuts() 
    *
    * This static function returns the total number of cut operations made during the run-time of the program.
    * A cut operation is the operation which diconnects a subtree from its parent (during decreaseKey/delete methods). 
    */
    public static int totalCuts()
    {    
    	return totalCuts;
    }

     /**
    * public static int[] kMin(FibonacciHeap H, int k) 
    *
    * This static function returns the k minimal elements in a binomial tree H.
    * this method using the method insertChildren.
    * The function should run in O(k(logk + deg(H)). 
    */
    public static int[] kMin(FibonacciHeap H, int k)
    {    
    	int[] arr = new int[k];
        arr[0]=H.min.getKey(); //min==tree
        HeapNode st=H.start.getChild();
        FibonacciHeap assistent=new FibonacciHeap();
        FibonacciHeap.insertChildren(st, assistent);
        for(int i=1;i<k;i++) {
        	HeapNode tmp =assistent.findMin();
        	arr[i]=tmp.getKey();
        	assistent.deleteMin();
        	if(tmp.getOriginal().getChild()!=null) {
        		FibonacciHeap.insertChildren(tmp.getOriginal().getChild(), assistent);
        	}
        }
        return arr;
    }
    
    /**
     * private static void insertChildren(HeapNode st, FibonacciHeap assistent)
     * 
     * Insert all children of root to temporary heap in the method Kmin.
     */
    private static void insertChildren(HeapNode st, FibonacciHeap assistent) {
        HeapNode tmp =assistent.insert(st.getKey());
        tmp.setOriginal(st);
        HeapNode tmp2=st.getNext();
        while(tmp2!=st) {
        	tmp=assistent.insert(tmp2.getKey());
            tmp.setOriginal(tmp2);
            tmp2=tmp2.getNext();
        }
    }
   /**
    * public class HeapNode
    * 
    * If you wish to implement classes other than FibonacciHeap
    * (for example HeapNode), do it in this file, not in 
    * another file 
    *  
    */
    public class HeapNode{
    	public int key;
    	private int rank;
    	private boolean marked;
    	private HeapNode child, next, prev, parent, original;

      	public HeapNode(int key) {
    	    this.key = key;
          }
      	
      	public  HeapNode(int key,HeapNode original) {
      		this.key=key;
      		this.original=original;
      	}

      	public int getRank() {
      		return rank;
      	}

      	public void setRank(int rank) {
		this.rank = rank;
      	}

      	public boolean isMarked() {
      		return marked;
      	}

      	public void setMarked(boolean marked) {
      		this.marked = marked;
      	}

      	public HeapNode getChild() {
      		return child;
      	}

      	public void setChild(HeapNode child) {
      		this.child = child;
      	}

      	public HeapNode getNext() {
      		return next;
      	}

      	public void setNext(HeapNode next) {
      		this.next = next;
      	}

      	public HeapNode getPrev() {
      		return prev;
      	}

      	public void setPrev(HeapNode prev) {
      		this.prev = prev;
      	}

      	public HeapNode getParent() {
      		return parent;
      	}

      	public void setParent(HeapNode parent) {
      		this.parent = parent;
      	}

      	public void setKey(int key) {
			this.key = key;
		}

		public int getKey() {
      		return this.key;
      	}

      	public void setOriginal(HeapNode original) {
			this.original = original;
		}

		public HeapNode getOriginal() {
      		return this.original;
      	}
    }
}
