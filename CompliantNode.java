import java.util.HashSet;
import java.util.Set;

/**
 * A compliant node follows the "never forget" gossip protocol.
 * It maintains a set of all valid transactions it has ever seen
 * and broadcasts this complete set to its followers in every round.
 * It adds any new transactions it receives from nodes it follows.
 */
public class CompliantNode implements Node {

    // The set of all transactions this node has seen so far.
    private Set<Transaction> knownTransactions;

    // A boolean array where followees[i] is true if this node follows node i.
    private boolean[] followees;

    /**
     * Constructor for a CompliantNode.
     *
     * @param p_graph          Probability of an edge in the random graph (unused, but required by constructor).
     * @param p_malicious      Probability of a node being malicious (unused, but required by constructor).
     * @param p_txDistribution Probability of a transaction being in the initial set (unused, but required by constructor).
     * @param numRounds        The number of rounds in the simulation (unused, but required by constructor).
     */
    public CompliantNode(double p_graph, double p_malicious, double p_txDistribution, int numRounds) {
        // Initialize the set to store all known transactions.
        this.knownTransactions = new HashSet<>();
    }

    /**
     * Stores the list of nodes that this node follows.
     *
     * @param followees An array where followees[i] is true if this node follows node i.
     */
    public void setFollowees(boolean[] followees) {
        this.followees = followees;
    }

    /**
     * Initializes the node's set of known transactions with its pending list.
     *
     * @param pendingTransactions The initial set of transactions this node is aware of.
     */
    public void setPendingTransaction(Set<Transaction> pendingTransactions) {
        this.knownTransactions.addAll(pendingTransactions);
    }

    /**
     * Returns the set of all transactions this node is aware of.
     * This method is called in every round to get proposals for followers,
     * and it's called one final time to get the consensus set.
     * In this strategy, the behavior doesn't need to change; it always
     * returns the complete set of known transactions.
     *
     * @return The set of all transactions this node has seen.
     */
    public Set<Transaction> sendToFollowers() {
        // Return a copy of the set to prevent external modification.
        return new HashSet<>(this.knownTransactions);
    }

    /**
     * Receives candidate transactions from followees and adds any new ones
     * to the set of known transactions.
     *
     * @param candidates A set of candidate transactions broadcast by other nodes.
     */
    public void receiveFromFollowees(Set<Candidate> candidates) {
        // Iterate over all received candidate transactions.
        for (Candidate candidate : candidates) {
            // Check if the sender is a node that this node follows.
            // this.followees[candidate.sender] is true if we follow the sender.
            if (this.followees[candidate.sender]) {
                // If it's from a followee, add the transaction to our master set.
                // The HashSet automatically handles duplicates.
                this.knownTransactions.add(candidate.tx);
            }
        }
    }
}