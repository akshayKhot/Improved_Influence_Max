import java.util.ArrayDeque;
import java.util.BitSet;
import java.util.Deque;
import java.util.Random;
import it.unimi.dsi.webgraph.ImmutableGraph;

public class InfluenceMax {
    ImmutableGraph G;
    int n;
    long m;
    double W;

    double p = 0.1;
    int k = 5;

    int[] permutation;
    BitSet marked;


    public InfluenceMax(String basename, int beta) throws Exception {

        G = ImmutableGraph.load("sym-noself/" + basename);
        n = G.numNodes();
        m = G.numArcs();
        W = beta * (n + m) * Math.log(n);

        System.out.println("\nG = " + basename + "\nn = "+ n + ", m = " + m);
        System.out.println("Beta=" + beta + " k=" + k + " p=" + p + "\n");

        marked = new BitSet(n);
        permutation = new int[n];

        for(int i=0; i<n; i++)
            permutation[i] = i;

        Random rnd = new Random();
        for (int i=n; i>1; i--) {
            int j = rnd.nextInt(i);
            int temp = permutation[i-1];
            permutation[i-1] = permutation[j];
            permutation[j] = temp;
        }

    }
}
