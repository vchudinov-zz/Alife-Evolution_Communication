Evolution of communication
=============================
This project was developed in collaboration with Simon Clemens (stcl@itu.dk) as part of the Artificial Life Spring 2014 course at IT University Copenhagen. 

In it we try to replicate the findings from : 
Floreano, D., Mitri, S., Magnenat, S., & Keller, L. (2007). Evolutionary conditions for the emergence of communication in robots. Current biology, 17(6), 514-519.

We used a JNEAT implementation and build our own robot simulator. 
- The folder Experiments contains the parameters for each experiment as well as the genomes used for each agent.
src/sivi/experiments contains the actual experiments code - Experiment controller is the class from which everything is run
- Simulator contains the wolrd simulation and a visualizer class
- neat contains the jNEAT implementation
