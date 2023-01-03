// CS 1501 Summer 2022
// MultiWay Trie Node implemented as an external class which
// implements the TrieNodeInt InterfaceAddress.  For this
// class it is assumed that all characters in any key will
// be letters between 'a' and 'z'.

package TriePackage;

import java.util.*;
@SuppressWarnings("unchecked")
public class MTAlphaNode<V> implements TrieNodeInt<V>
{
	private static final int R = 26;	// 26 letters in alphabet (all lowercase)
  protected V val;
  protected TrieNodeInt<V> [] next;
	protected int degree;

	// You must supply the methods for this class.  See TrieNodeInt.java
	// for the specifications.  See also handout MTNode.java for a
	// partial implementation.  Don't forget to include the conversion
	// constructor (passing a DLBNode<V> as an argument).

	public MTAlphaNode(V data)
	{
		val = data;
		degree = 0;
		next = (TrieNodeInt<V> []) new TrieNodeInt<?>[R];
	}

	public MTAlphaNode()
	{
		val = null;
		degree = 0;
		next = (TrieNodeInt<V> []) new TrieNodeInt<?>[R];
	}

	public MTAlphaNode(DLBNode<V> dlbNode)
	{
		val = dlbNode.getData();
		degree = dlbNode.getDegree();
		next = (TrieNodeInt<V> []) new TrieNodeInt<?>[R];

		Iterable<TrieNodeInt<V>> q1 = dlbNode.children();
		Queue<Character> q2 = dlbNode.chars();

		for(TrieNodeInt<V> currChild : q1)
			 setNextNode(q2.remove(), currChild);
	}

	// Return the next node in the trie corresponding to character
	// c in the current node, or null if there is no next node for
	// that character.
	public TrieNodeInt<V> getNextNode(char c)
	{
		return next[c - 97]; //lowercase a is 97 in ASCII
	}
	// Set the next node in the trie corresponding to character c
	// to the argument node.  If the node at that position was previously
	// null, increase the degree of this node by one (since it is now
	// branching by one more link).
	public void setNextNode(char c, TrieNodeInt<V> node)
	{
		if (next[c-97] == null)
			degree++;
		next[c-97] = node;
	}

	// Return the data at the current node (or null if there is no data)
	public V getData()
	{
		return val;
	}

	// Set the data at the current node to the data argument
	public void setData(V data)
	{
		val = data;
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
			return 116; //4*26 for array + 4 for array pointer + 4 for int R + 4 for int degree
	}

	// Return an Iterable collection of the references to all of the children
	// of this node.  Do not put any null references into this result.  The
	// order of the children as stored in the TrieNodeInt<V> node must be
	// maintained in the returned Iterable.  The easiest way to do this is to
	// put all of the references into a Queue and to return the Queue (since a
	// Queue implements Iterable and maintains the order of the children).
	// This method will allow us to access all of the children of a node without
	// having to know how the node is actually implemented.
	public Queue<TrieNodeInt<V>> children()
	{
		Queue<TrieNodeInt<V>> q = new LinkedList<>();
		for(int i = 0; i < next.length; i++)
			if(next[i] != null) q.add(next[i]);

		return q;
	}
}
