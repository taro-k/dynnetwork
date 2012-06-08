package org.cytoscape.dyn.internal.model.tree;

/**
 * <code> DynIntervalTreeImpl </code> implements the interval tree for fast searching of intervals 
 * in a given time range. An interval tree is a red-black tree that maintains a dynamic set of 
 * elements, with each node {@link DynNode} containing an interval {@link DynInterval}.  The leaf 
 * nodes do not contain data, and reference to a dummy sentinel node (nil). The root is also a nil 
 * sentinel node, whose left reference points to the network root.
 * 
 * <code> DynIntervalTreeImpl </code> guarantees O(log n) insertion and deletion of elements.
 *  
 * @author sabina
 *
 * @param <T>
 */
public final class DynIntervalTreeImpl<T> extends AbstractDynIntervalTree<T>
{

	public DynIntervalTreeImpl()
	{
		super();
	}
	
	public DynIntervalTreeImpl(DynNode<T> root)
	{
		super(root);
	}
	
	public DynIntervalTreeImpl(DynInterval<T> interval)
	{
		super(interval);
	}
	
	protected void insert(DynNode<T> z, DynNode<T> root)
	{
		int dir = 0;
		while (!root.isLeaf())
		{	
			
			// If duplicate
			if (z.getStart()==root.getStart() && z.getEnd()==root.getEnd())
			{
				root.addInterval(z.getIntervalList().get(0));
				return;
			}
			
			// Otherwise check direction
			dir = (z.getStart()<root.getStart() || (z.getStart()==root.getStart() && z.getEnd()<root.getEnd()))?0:1;
			
			root.getChildren(dir).setParent(root);
			root.setMax(max(z,root));
			root = root.getChildren(dir);
		}
		root.getParent().setChildren(dir, z);
		insertFixUp(z, dir);
	}
		
	private void insertFixUp(DynNode<T> z, int dir)
	{
		z.isBlack(false);
		while (!z.getParent().isBlack())
		{
			dir = getParentDirection(z);
			if (!z.getParent().getParent().getChildren(1-dir).isBlack())
			{
				z.getParent().getParent().isBlack(false);
				z.getParent().getParent().getLeft().isBlack(true);
				z.getParent().getParent().getRight().isBlack(true);
				z = z.getParent().getParent();
			}
			else
			{
				if (z == z.getParent().getChildren(1-dir))
				{	
					z = z.getParent();
					rotate(z,dir);
				}
				rotate(z.getParent().getParent(), 1-dir);
			}
		}
		this.root.getLeft().isBlack(true);
	}
	
	protected void remove(DynNode<T> z)
	{
		z.setMax(Double.NEGATIVE_INFINITY);
		for (DynNode<T> i = z.getParent(); i != root; i = i.getParent())
			i.setMax(max(i.getLeft(), i.getRight()));

		DynNode<T> y;
		DynNode<T> x;

		if (z.getLeft().isLeaf() || z.getRight().isLeaf())
			y = z;
		else
			y = this.successor(z);
		if (y.getLeft().isLeaf())
			x = y.getRight();
		else
			x = y.getLeft();
		x.setParent(y.getParent());
		if (root == x.getParent())
			root.setLeft(x);
		else
			y.getParent().setChildren(getParentDirection(y),x);
		if (y!=z)
		{
			if (y.isBlack())
				removeFixUp(x);
			y.setLeft(z.getLeft());
			y.setRight(z.getRight());
			y.isBlack(z.isBlack());
			z.getParent().setChildren(getParentDirection(z),y);
		}
		else if (y.isBlack())
			removeFixUp(x);
	}
	
	private void removeFixUp(DynNode<T> x)
	{
		int dir = 0;
		while (x!=root.getLeft() && x.isBlack())
		{
			dir = getParentDirection(x);
			DynNode<T> w = x.getParent().getChildren(1-dir);
			if (!w.isBlack())
			{
				w.isBlack(true);
				x.getParent().isBlack(false);
				rotate(x.getParent(),1-dir);
				w = x.getParent().getChildren(1-dir);
			}
			if (w.getChildren(dir).isBlack() && w.getChildren(1-dir).isBlack())
			{
				w.isBlack(false);
				x = x.getParent();
			} 
			else {
				if (w.getChildren(1-dir).isBlack()) {
					w.getChildren(dir).isBlack(true);
					w.isBlack(false);
					rotate(w,dir);
					w = x.getParent().getChildren(1-dir);
				}
				w.isBlack(x.getParent().isBlack());
				x.getParent().isBlack(true);
				w.getChildren(1-dir).isBlack(true);
				rotate(x.getParent(),dir);
				x = root.getLeft();
			}
		}
		x.isBlack(true);
	}      
    private DynNode<T> successor(DynNode<T> root)
    {
    	DynNode<T> right = root.getRight();
    	if (!right.isLeaf())
    		return this.getTreeMinimum(right);
    	else
    		return this.getUp(right);
    }
    
    private DynNode<T> getUp(DynNode<T> root)
    {
    	DynNode<T> up = root.getParent();
    	while (!up.isLeaf() && root == up.getRight())
    	{
    		root = up;
    		up = up.getParent();
    	}
    	return up;
    }

    private DynNode<T> getTreeMinimum(DynNode<T> root)
    {
    	DynNode<T> left = root;
    	while (!root.getLeft().isLeaf())
    		left = left.getLeft();
    	return left;
    }

    private DynNode<T> rotate(DynNode<T> root, int dir)
    {
    	DynNode<T> pivot = root.getChildren(1-dir);
    	root.setChildren(1-dir,pivot.getChildren(dir));
    	root.getParent().setChildren(getThisDirection(root, dir),pivot);
    	pivot.setChildren(dir,root);
    	root.isBlack(false);
    	pivot.isBlack(true);
    	pivot.setMax(root.getMax());
    	root.setMax(max(root.getLeft(),root.getRight(),root));
    	return pivot;
    }

    private int getThisDirection(DynNode<T> z, int dir)
    {
    	if (z == z.getParent().getChildren(dir))
			return dir;
		else
			return 1-dir;
    }
    
    private int getParentDirection(DynNode<T> z)
    {
    	if (z.getParent() == z.getParent().getParent().getLeft())
			return 0;
		else
			return 1;
    }
    
    private double max(DynNode<T> a, DynNode<T> b)
    {
    	return Math.max(a.getMax(), b.getMax());
    }
    
    private double max(DynNode<T> a, DynNode<T> b, DynNode<T> c)
    {
    	return Math.max(Math.max(a.getMax(), b.getMax()), c.getEnd());
    }
		
}
