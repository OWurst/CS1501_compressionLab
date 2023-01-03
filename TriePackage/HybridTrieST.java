// CS 1501 Summer 2022
// HybridTrieST<V> class

package TriePackage;

import java.util.*;
import java.io.*;
@SuppressWarnings("unchecked")
public class HybridTrieST<V> {

    private TrieNodeInt<V> root;
    private int treeType = 0;

    public HybridTrieST(int type)
	  {
		    root = null;
        this.treeType = type;
	  }

      // treeType = 0 --> multiway trie
    	// treeType = 1 --> DLB
    	// treeType = 2 --> hybrid

    public V get(StringBuilder key) {
        TrieNodeInt<V> x = get(root, key, 0);
        if (x == null) return null;
        return x.getData();
    }

    private TrieNodeInt<V> get(TrieNodeInt<V> x, StringBuilder key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char c = key.charAt(d);
        return get(x.getNextNode(c), key, d+1);
    }

    // Compare to searchPrefix in TrieSTNew.  Note that in this class all
	  // of the accesses to a node are via the interface methods.
	  public int searchPrefix(StringBuilder key)
	  {
		   return searchPrefix(root, key, 0);
	  }

	   private int searchPrefix(TrieNodeInt<V> x, StringBuilder key, int d)
     {
		     if (x == null)  // null reference means not found and not prefix
			     return 0;

		     if (d == key.length()) // all chars in the key are found
         {
				     int total = 0;
				     if (x.getData() != null)
					        total += 2;  // val is non-null so key is found
				     if (x.getDegree() > 0)
					        total += 1;  // x has at least one child so key
							                                     // is a prefix
				 return total;
			   }
			 else return searchPrefix(x.getNextNode(key.charAt(d)), key, d+1);
     }


     public void put(StringBuilder key, V val)
     {
         root = put(root, key, val, 0);
     }
 	// This method requires us to create a new node -- which in turn requires
 	// a class.  This is the only place where a MTNode<V> object is explicitly
 	// used.
     private TrieNodeInt<V> put(TrieNodeInt<V> x, StringBuilder key, V val, int d) {
         if (x == null && treeType == 0 ) x = new MTAlphaNode<V>();
 	       else if (x == null) x = new DLBNode<V>();

         if(treeType == 2 && x.getDegree() >= 11) x = DLBToMTConverter(x);

         if (d == key.length()) {
             x.setData(val);
             return x;
         }
         char c = key.charAt(d);
         x.setNextNode(c, put(x.getNextNode(c), key, val, d+1));
         return x;
     }

     public int getSize()
   	 {
        int currSize = 0;
   		  return getSize(root, currSize);
   	 }

     private int getSize(TrieNodeInt<V> X, int currSize)
     {
       int thisSize = 0;
       thisSize += X.getSize();
       Iterable<TrieNodeInt<V>> q = X.children();

       for(TrieNodeInt<V> currChild : q)
          thisSize += getSize(currChild, currSize);

       return currSize += thisSize;
    }

    public TrieNodeInt DLBToMTConverter(TrieNodeInt<V> x)
    {
      if(x instanceof MTAlphaNode<?>) return x;
      x = (DLBNode<V>)x;

      return new MTAlphaNode(x);
    }

     public int[] degreeDistribution()
     {
       int[] degDist = new int[27];
       for(int i = 0; i < degDist.length; i++)  degDist[i] = 0;

       return degreeDistribution(root, degDist);
     }
     private int[] degreeDistribution(TrieNodeInt<V> curr, int[] degDist)
     {
       degDist[curr.getDegree()]++;

       Iterable<TrieNodeInt<V>> q = curr.children();
       for(TrieNodeInt<V> currChild : q)
          degDist = degreeDistribution(currChild, degDist);

       return degDist;
     }

     public int countNodes(int type)
     {
       int count =  0;
       return countNodes(root, count, type);
     }
     private int countNodes(TrieNodeInt<V> curr, int count, int type)
     {
       int thisCount = 0;
       if(type == 1 && curr instanceof MTAlphaNode<?>) thisCount++;
       else if(type == 2 && curr instanceof DLBNode<?>) thisCount++;

       Iterable<TrieNodeInt<V>> q = curr.children();

       for(TrieNodeInt<V> currChild : q)
          thisCount += countNodes(currChild, count, type);

        return count + thisCount;
     }

     public void save(String file)
     {
       try(FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
        saver(root, out);
        }
        catch (IOException e)
        {
          System.out.println("error saving, likely a bad filename");
        }
      }

      private void saver(TrieNodeInt<V> curr, PrintWriter out)
      {
        if(curr.getData() != null) out.println(curr.getData());

        Iterable<TrieNodeInt<V>> q = curr.children();
        for(TrieNodeInt<V> currChild : q)
           saver(currChild, out);
      }
	   // You must supply the methods for this class.  See test program
	   // HybridTrieTest.java for details on the methods and their
	   // functionality.  Also see handout TrieSTMT.java for a partial
	   // implementation.
}
