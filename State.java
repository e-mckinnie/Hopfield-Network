package main;

import java.util.Arrays;

/**
 * Represents a certain state of the Network.
 *
 */
public class State {
    private int[] nodes;
    private static final boolean debug = false;
    
    //Abstraction Function:
    //State represents a state of the network with values for nodes
    // stored in a list, and a name for the String.
    //
    // Representation invariant:
    // nodes != null
    // all nodes are 0 or 1
    
    /**
     * Constructs a new State.
     * 
     * @param nodes nodes for the state
     * @effects creates State with given nodes
     */
    public State(int[] nodes) {
        this.nodes = new int[nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            this.nodes[i] = nodes[i];
        }
        checkRep();
    }
    
    /**
     * Gets current state.
     * 
     * @returns current state
     */
    public int[] getState() {
        checkRep();
        return this.nodes;
    }
    
    /**
     * Standard equality operation.
     * 
     * @param o object to compare with
     * @return if o is an instance of State
     * and has the same value as this
     */
    @Override
    public boolean equals(Object o) {
        if (! (o instanceof State)) {
            return false;
        }
        State other = (State) o;
        return Arrays.equals(this.nodes, other.nodes);
    }
    
    /**
     * Standard hashcode operation.
     * 
     * @return int that all equal objects should return
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(this.nodes);
    }
  
    
    /*
     * Checks representation invariant.
     */
    private void checkRep() {
        assert nodes != null: "nodes should not be null";
        if (debug) {
            for (int i: nodes) {
                assert i == 0 || i == 1: "all nodes must be 0 or 1";
            }
        }
            
    }

}
