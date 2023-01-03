# omw3_CS1501_compressionLab
I did this project for school in CS1501, algorithms and data structures 2. It is an LZW compression algorithm that can be used to compress any file. It also provides users the option to let the dictionary reset itself when full, which can be useful for compressing folders with different file types, but not for files that are likely to follow consistent patterns, like text. BinarySTDin.java and BinarySTDOut.java come from https://algs4.cs.princeton.edu/55compression/, and the TriePackage used for the dictionary comes from a project I did earlier in the semester. Instructions for running the program can be found in the comments of LZWmod.java.