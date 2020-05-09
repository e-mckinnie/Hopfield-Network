package main;

import java.util.Map;

/**
 * Interface to train the network, test the network, and print the results.
 *
 */
public class Client {
    
    public static void main (String[] args) {
        //make sure that the file path is relative to src and all files are in the same package
        Map<State, String> attractors = DataParser.getStates("src/main/small_binary_train.csv");
        Map<State, String> tests = DataParser.getStates("src/main/small_binary_test.csv");
        Network hopfield = new Network(5*3);
        
        long startTime = System.currentTimeMillis();
        for (State state: attractors.keySet()) {
            hopfield.train(state.getState());
        }
        long trainingTime = System.currentTimeMillis();
        System.out.println("training time: " + (trainingTime - startTime) + " milliseconds");
        
        int total = 0;
        int successes = 0;
        
        for (State test: tests.keySet()) {
            hopfield.initialize(test.getState());
            while (!attractors.containsKey(new State(hopfield.getNodes()))) {
                hopfield.update();
            }
            //hopfield nodes now resemble an attractor state
            String label = attractors.get(new State(hopfield.getNodes()));
            if (label.equals(tests.get(test))) {
                 successes++;
            } 
            total++;
        }
       long endTime = System.currentTimeMillis();
       long duration = endTime - startTime;
       System.out.println("testing time: " + (endTime - trainingTime) + " milliseconds");
       System.out.println("total time: " + duration + " milliseconds");
       System.out.println("success rate: " + successes + " out of " + total);
    }
}
