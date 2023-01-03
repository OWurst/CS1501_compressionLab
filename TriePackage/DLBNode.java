// CS 1501 Summer 2022
// DLB Trie Node implemented as an external class which
// implements the TrieNodeInt<V> Interface

package TriePackage;

import java.util.*;
public class DLBNode<V> implements TrieNodeInt<V>
{
    protected Nodelet front;
    protected int degree;
	  protected V val;

    protected class Nodelet
    {
    	protected char cval;
    	protected Nodelet rightSib;
    	protected TrieNodeInt<V> child;

      protected Nodelet(char c, Nodelet next, TrieNodeInt<V> pointer)
      {
        this.rightSib = next;
        this.cval = c;
        this.child = pointer;
      }
    }

    public DLBNode(V data)
  	{
  		val = data;
  		degree = 0;
  		front = null;
  	}

  	public DLBNode()
  	{
  		val = null;
  		degree = 0;
  	  front = null;
  	}

    // Return the next node in the trie corresponding to character
  	// c in the current node, or null if there is no next node for
  	// that character.
  	public TrieNodeInt<V> getNextNode(char c)
    {
      Nodelet curr = front;
      while(curr != null)
      {
        if(curr.cval == c) return curr.child;
        curr = curr.rightSib;
      }
      return null;
    }

  	// Set the next node in the trie corresponding to character c
  	// to the argument node.  If the node at that position was previously
  	// null, increase the degree of this node by one (since it is now
  	// branching by one more link).
  	public void setNextNode(char c, TrieNodeInt<V> node)
    {

      if(front == null) {front = new Nodelet(c, null, node); degree++; return;}
      if(front.cval > c) {front = new Nodelet(c, front, node); degree++; return;}//insert at front case

      Nodelet curr = front;
      while(curr != null)
      {
        if(curr.cval == c){curr.child = node; return;}//nodelet exists case
        if(curr.rightSib == null){curr.rightSib = new Nodelet(c, null, node); degree++; return;}//insert at tail case
        if(curr.rightSib.cval > c) {curr.rightSib = new Nodelet(c, curr.rightSib, node); degree++; return;}         //inserInOrder case

        curr = curr.rightSib;
      }
    }



  	// Return the data at the current node (or null if there is no data)
  	public V getData()
    {
      return val;
    }
  	// Set the data at the current node to the data argument
  	public void setData(V data)
    {
      this.val = data;
    }
  	// Return the degree of the current node.  This corresponds to the
  	// number of children that this node has.
  	public int getDegree()
    {
      return degree;
    }

  	// Return the approximate size in bytes of the current node.  This is
  	// a very rough approximation based on the following:
  	// 1) Assume each reference in a node will use 4 bytes (whether it is
  	//    used or it is null)
  	// 2) Assume any primitive type is its specified size (see Java reference
  	//    for primitive type sizes in bytes)
  	// Note that the actual size of the node is implementation dependent and
  	// is not specified in the Java language.  There are tools to give a better
  	// approximation of this value but for our purposes, this approximation is
  	// fine.  See a brief discussion of this method in the Assignment 2
  	// document.
  	public int getSize()
    {
      return degree*10 + 4 + 4 + 4; //10 for each nodelet + 4 to store front pointer + 4 for degree int + 4 for Node Value
    }

  	// Return an Iterable collection of the references to all of the children
  	// of this node.  Do not put any null references into this result.  The
  	// order of the children as stored in the TrieNodeInt<V> node must be
  	// maintained in the returned Iterable.  The easiest way to do this is to
  	// put all of the references into a Queue and to return the Queue (since a
  	// Queue implements Iterable and maintains the order of the children).
  	// This method will allow us to access all of the children of a node without
  	// having to know how the node is actually implemented.
    @SuppressWarnings("unchecked")
  	public Iterable<TrieNodeInt<V>> children()
  	{
  		Queue<TrieNodeInt<V>> q = new LinkedList<>();
      Nodelet curr = front;

      while(curr != null)
      {
        q.add(curr.child);
        curr = curr.rightSib;
      }

  		return q;
  	}

    public Queue<Character> chars()
    {
      Queue<Character> q = new LinkedList<>();
      Nodelet curr = front;

      while(curr != null)
      {
        q.add(curr.cval);
        curr = curr.rightSib;
      }
      return q;
  }
}
