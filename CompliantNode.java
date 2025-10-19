import java.util.HashSet;
import java.util.Set;

/**
 * This compliant node implements an efficient "gossip what's new" strategy
 * to avoid "Killed" (resource exhaustion) errors.
 *
 * 1. It maintains a master set 'allKnownTransactions' for the final consensus.
 * 2. It maintains a temporary set 'newTransactionsToBroadcast' which only
 * holds transactions learned in the *previous* round.
 * 3. During the simulation, sendToFollowers() sends only the small
 * 'newTransactionsToBroadcast' set. This is highly efficient.
 * 4. After the simulation, the final call to sendToFollowers() sends the
 * complete 'allKnownTransactions' set, as required by the assignment.
 */
public class CompliantNode implements Node {

    private final int numRounds;
    private int currentRound;

    // The master set of all transactions ever seen. This is for the final consensus.
    private Set<Transaction> allKnownTransactions;

    // Set of transactions to broadcast *this* round (i.e., what was new *last* round).
    private Set<Transaction> newTransactionsToBroadcast;

    // We only accept transactions from nodes we follow.
    private boolean[] followees;

    public CompliantNode(double p_graph, double p_malicious, double p_txDistribution, int numRounds) {
        this.numRounds = numRounds;
        this.currentRound = 0;
        this.allKnownTransactions = new HashSet<>();
        this.newTransactionsToBroadcast = new HashSet<>();
    }

    public void setFollowees(boolean[] followees) {
        this.followees = followees;
    }

    /**
     * Initializes the node with its first set of transactions.
     */
    public void setPendingTransaction(Set<Transaction> pendingTransactions) {
        // Add to our master list
        this.allKnownTransactions.addAll(pendingTransactions);
        
        // These are the first transactions to be broadcast in round 1.
        this.newTransactionsToBroadcast.addAll(pendingTransactions);
    }

    /**
     * This method's behavior changes based on the round.
     * 1. During simulation rounds: It sends only *new* transactions.
     * 2. After the final round: It sends the *complete* consensus set.
     */
    public Set<Transaction> sendToFollowers() {
        // Increment the round counter *every time* this is called.
        this.currentRound++;

        // The simulation runs for 'numRounds' (e.g., 10).
        // The final call happens *after* the loop, at round 'numRounds + 1' (e.g., 11).
        if (this.currentRound > this.numRounds) {
            // Final call: return everything we know.
            return new HashSet<>(this.allKnownTransactions);
        }

        // --- During Simulation ---
        // Otherwise, only send the transactions we just learned in the last round.
        Set<Transaction> toSend = new HashSet<>(this.newTransactionsToBroadcast);

        // We've broadcast them, so clear the set for the next round of receiving.
        this.newTransactionsToBroadcast.clear();

        return toSend;
    }

    /**
     * Receives candidates, updates the master list, and queues up
     * new transactions to be broadcast in the *next* round.
     */
    public void receiveFromFollowees(Set<Candidate> candidates) {
        
        for (Candidate candidate : candidates) {
            // Only accept transactions from nodes we are explicitly following.
            if (this.followees[candidate.sender]) {
                
                // Add the transaction to the master set.
                // .add() returns true if this is the first time we've seen this transaction.
                if (this.allKnownTransactions.add(candidate.tx)) {
                    
                    // If it's new, we need to broadcast it in the *next* round.
                    this.newTransactionsToBroadcast.add(candidate.tx);
                }
            }
        }
    }
}