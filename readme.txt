1) Input file for Milestone 1: exmp01_M1.txt

You can see the specification in the figure 
exmp01_M1_spec.gif

In this case, the result should be "ab".
If not look at the figure 
exmp01_M1_spec_neg.gif
to make sure that the returned string is accepted by the automaton, i.e, you are not computing the shortest string.

2) Input file for Milestone 2: exmp01_M2.txt

You can see the model in the figure 
exmp01_M2_sys.gif
and the specification in the figure 
exmp01_M2_spec.gif

The negation of the specification as a DFA is shown in
exmp01_M2_spec_neg_dfa.gif
and in a minimized automaton in 
exmp01_M2_spec_neg_dfa_min.gif

The product of the negation of the spec and the model is shown in figures
exmp01_M2_prod.gif
exmp01_M2_prod_min.gif

Running exmp01_M2.txt as an input the software should return the string "bab"

If not, take a look at the product automaton to see if the string that your software returns is part of the language. Your string might be different because you used DFS or because multiple final states are reachable at the same path length.





