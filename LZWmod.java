/*************************************************************************
 *  Compilation:  javac LZWmod.java
 *  Execution:    java LZWmod - < input.txt   (compress)
 *  Execution:    java LZWmod + < input.txt   (expand)
 *  Execution:    java LZWmod - r < input.txt (compress with reset when dict is full)
 *  Execution:    java LZWmod + r < input.txt (expand with reset when dict is full)
 *  Execution:    java LZWmod - < input.txt > compressed.lzw (compress input.txt and store new encoding in compressed.lzw)(">" will work for all of the above) 
 *  Dependencies: BinaryIn.java BinaryOut.java
 *
 *  Compress or expand binary input from standard input using LZW.
 *
 *
 *************************************************************************/
import TriePackage.*;

public class LZWmod {
    private static final int R = 256;        // number of input chars
    private static int L = 512;       // number of codewords = 2^W
    private static int W = 9;         // codeword width
    private static final int maxW = 16;
    private static final int maxSize = 65536;

    public static void compress(boolean reset)
    {
      if(reset) BinaryStdOut.write(1,1);
      else BinaryStdOut.write(0,1);

      HybridTrieST<Integer> dict = newDict();

      int code = R+1;  // R is codeword for EOF
      StringBuilder prefix;
      StringBuilder addToDict = new StringBuilder("" + BinaryStdIn.readChar());

      while(!BinaryStdIn.isEmpty())
      {
        prefix = new StringBuilder(addToDict);
        addToDict.append(BinaryStdIn.readChar());

        if(dict.get(addToDict) == null)
        {
          BinaryStdOut.write(dict.get(prefix),W);
          if(code < L)
          {
            dict.put(addToDict, code++);
            addToDict.delete(0, addToDict.length()-1);
          }
          else if(W < maxW)
          {
            W++;
            L *= 2;
            dict.put(addToDict, code++);
            addToDict.delete(0, addToDict.length()-1);
          }
          else if(reset)
          {
            dict = newDict();
            code = R + 1;
            addToDict.delete(0, addToDict.length()-1);
            W = 9;
            L = 512;
          }
          else addToDict.delete(0, addToDict.length()-1);//case of full code and no reset
        }

      }
      BinaryStdOut.write(R, W);
      BinaryStdOut.close();
    }

    public static HybridTrieST<Integer> newDict()
    {
      HybridTrieST<Integer> ret = new HybridTrieST<Integer>(1);
      for (int i = 0; i < R; i++)
      {
        StringBuilder s = new StringBuilder("" + (char) i);
        ret.put(s, i);
      }
      //System.out.println("returning from reset " + R);
      return ret;
    }

    public static void expand() {
        boolean reset = BinaryStdIn.readBoolean();

        String[] st = new String[maxSize];
        int i; // next available codeword value

        // initialize symbol table with all 1-character strings
        for (i = 0; i < R; i++)
            st[i] = "" + (char) i;
        st[i++] = "";                        // (unused) lookahead for EOF

        int codeword = BinaryStdIn.readInt(W);
        String val = st[codeword];

        while (true) {
          if(i == L && W < maxW)
          {
            //System.out.println("-------------------------------------------");
            W++;
            L *= 2;}

            BinaryStdOut.write(val);
            codeword = BinaryStdIn.readInt(W);

            if (codeword == R) break;
            String s = st[codeword];

            if (i == codeword) s = val + val.charAt(0);   // special case hack

            if (i < L)
            {
              st[i++] = val + s.charAt(0);
            }
            else if(W < maxW)
            {
              W++;
              L *= 2;
              st[i++] = val + s.charAt(0);
            }
            else if(reset)
            {
              st = new String[L];
              for (i = 0; i < R; i++)
                  st[i] = "" + (char) i;
              st[i++] = "";
              W = 9;
              L = 512;
            }

            val = s;
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
      boolean reset;
      if(args.length == 2 && args[1].charAt(0) == 'r') reset = true;
      else reset = false;

        if      (args[0].equals("-")) compress(reset);
        else if (args[0].equals("+")) expand();
        else throw new RuntimeException("Illegal command line argument");
    }

}
