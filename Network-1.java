package main;
import java.util.Random;

/**
 * An immutable representation of a Hopfield Network. A network is a collection of values
 * that represent pixels of an image. 
 * 
 */
public class Network {
    private int[] nodes;
    private int[][] weights;
    private static Random r;
    private static final boolean debug = false;
    
 // Abstraction Function:
    // A Network is represented by an array of node values, and a 2D array of weights.
    // The weight at [i][j] represents the weight between the nodes located at nodes[i] and
    // at nodes[j].
    //
    // Representation Invariant:
    // nodes != null
    // weights != null
    // all node values should be 0 or 1
    
    /**
     * Creates a new Network object.
     * 
     * @param total number of nodes there will be
     * @effects creates an empty Network object
     */
    public Network(int size) {
        nodes = new int[size];
        weights = new int[size][size];
        Network.r = new Random();
        checkRep();
    }
    
    /**
     * Returns the pixel values of this Network.
     * 
     * @return pixel values of this Network; empty if it hasn't been initialized yet
     */
    public int[] getNodes() {
        checkRep();
        return nodes;
    }
    
    /**
     * Trains network by updating the weights given by the equation
     * w_i_j = sum (2v_i - 1)(2v_j - 1) where v_i and v_j are the values of the
     * nodes of the network and w_i_j is the weight between them.
     * 
     * @param state values to be added as Nodes to the Network
     * @requires state != null
     * @modifies this
     * @effects updates the weights of the network accordingly
     * @throws IndexOutOfBoundException if state is not sizexsize
     */
    public void train(int[] state) {
        checkRep();
        initialize(state);
        updateConnections();
        checkRep();
    }
    
    /*
     * Updates Connections in this Network.
     * @modifies this
     * @effects updates all connections based on current state of nodes
     */
    private void updateConnections() {
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[0].length; j++) {
                int weight = (2*nodes[i] - 1)*(2*nodes[j] - 1);
                if (i == j) {
                    weight = 0;
                }
                weights[i][j] += weight;
            }
        }
    }
    
    /**
     * Initializes nodes to the given state. Required to be called before update is.
     * 
     * @param state state that we want the Network to interpret
     * @requires state != null
     * @modifies this
     * @effects resets nodes based on the given state
     * @throws IndexOutOfBoundsException is state is not sizexsize
     * 
     */
    public void initialize(int[] state) {
        checkRep();
        for (int i = 0; i < state.length; i++) {
            nodes[i] = state[i];
        }
        checkRep();
    }
    
    /**
     * Updates a single Node in the Network based on the current state (initialized)
     * based on the equation v_i_in = sum (w_j_i * v_j) where v_i is the selected
     * node, v_j is the value of one of the nodes of the given state, and
     * w_j_i is the connection between them. 
     * we set v_i = 1 if v_i_in >= 0 and v_i = 0 if v_i_in < 0.
     * 
     * @requires initialize to have been called first
     * @modifies this
     * @effects updates a single node based on the equation
     * 
     */
    public void update() {
        checkRep();
        int index = Network.r.nextInt(nodes.length);
        int in = 0;
        for(int j = 0; j < nodes.length; j++) {
            in += weights[index][j] * nodes[j];
        }
        if (in >= 0) {
           nodes[index] = 1;
        } else {
           nodes[index] = 0;
        }
        checkRep();
    }
    
    /**
     * Returns the energy of the current state of the network according to 
     * the equation -0.5 sum(w_i_j * v_i * v_j)
     * 
     * @return value of the energy of the current network
     */
    public double getEnergy() {
        checkRep();
        int sum = 0;
        for (int i = 0; i < nodes.length; i++) {
            for (int j = 0; j < nodes.length; j++) {
                sum += nodes[i] * nodes[j] * weights[i][j];
            }
        }
        checkRep();
        return (sum*-1) * 0.5;
    }
    
    /**
     * Checks that the representation invariant holds (if any).
     */
    private void checkRep() {
       assert nodes != null: "nodes is null";
       assert weights != null: "weights is null";
       if (debug) {
           for (int i: nodes) {
               assert i == 0 || i == 1: "all nodes must be 0 or 1";
           }
       }
    }
}
