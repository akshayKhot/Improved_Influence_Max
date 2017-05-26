import java.util.ArrayDeque;
import java.util.BitSet;
import java.util.Deque;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import it.unimi.dsi.webgraph.ImmutableGraph;

public class InfluenceMax_list {
	ImmutableGraph G;
	int n;
	long m;
	double eps = .1;
    double p;
    int k = 5;
    
    int[] permutation;
    BitSet marked;
	
	public InfluenceMax_list(String basename, Double  p) throws Exception {
		G = ImmutableGraph.load(basename);
		
		n = G.numNodes();
		m = G.numArcs();
        System.out.println("n="+n + ", m=" +m  + ", W=" +( 2 * (n+m)*Math.log(n)  ));
		
		marked = new BitSet(n);
		
		permutation = new int[n];

		for(int i=0; i<n; i++)
			permutation[i] = i;

		Random rnd = new Random();
        for (int i=n; i>1; i--) {
        	int j = rnd.nextInt(i);
        	//swap
        	int temp = permutation[i-1];
        	permutation[i-1] = permutation[j];
        	permutation[j] = temp;
        }
        System.out.println("Permutation array shuffled.");		
        
        
        this.p = p;
	}
    
	
	void get_sketch() {
        double W = 2 * (n + m) * Math.log(n);

        
        List<List<Integer>> I = new ArrayList<List<Integer>>();
        for(int j=0;j<n;j++)
        {
            I.add(new ArrayList<Integer>());
        }
        System.out.println("index I created.");
                           
	    double weight_of_current_index = 0.0;
	    int index = 0;
        int sketch_num = 0;

	    long startTime = System.currentTimeMillis();
        
        Random gen_rnd = new Random();

	    while(weight_of_current_index < W)
	    {
	    	if(index % 100000 == 0) {
	    		double sofarTimeInMin = (System.currentTimeMillis() - startTime)/(1000.0 * 60);
	    		double pctDone = 100*weight_of_current_index/W;
	    		
	    		System.out.println(
                        "sketch=" + sketch_num + 
	    				",  index=" + index +
	    				", weight_of_current_index=" + weight_of_current_index + 
	    				", this is " + pctDone + "% of W" +
	    				", elapsed " + sofarTimeInMin + " min");
 
	    	}
	        
	    	int v = permutation[gen_rnd.nextInt(n)];
	        marked.clear();
	        BFS(v,marked);
            
            int j=0;
	        int total_out_degree = 0;
	        for (int u = marked.nextSetBit(0); u >= 0; u = marked.nextSetBit(u+1))
	        {
                I.get(u).add(sketch_num);
	            total_out_degree += G.outdegree(u);
	        }
	        weight_of_current_index += (marked.cardinality() + total_out_degree);
	        index = ( index + 1 ) % n;
            sketch_num++;
	    }
        System.out.println();
	    System.out.println("Index: " + index +
                           " Number of Sketches: " + sketch_num);
        System.out.println();

        System.gc();
        int set_infl = 0;
        get_seeds(I, k, sketch_num, set_infl);
	}

		
	void BFS(int v, BitSet marked) {
		
		Random random = new Random();
		
		Deque<Integer> queue = new ArrayDeque<Integer>();

		queue.add(v);
        marked.set(v);
                
        while (!queue.isEmpty()) {
            int u = queue.remove();
            int[] u_neighbors = G.successorArray(u);
            int u_deg = G.outdegree(u);
        
            for (int ni = 0; ni < u_deg; ni++) {
                int uu = u_neighbors[ni];
                double xi = random.nextDouble();
                
                if (!marked.get(uu) && xi < p) {
                    queue.add(uu);
                    marked.set(uu);
                }
            }
        }
	}
    
    void get_seeds(List<List<Integer>> I, int k, int sketch_num, int set_infl) {

        int infl_max = 0;
        int max_node = 0;
        int node_sketch = 1;
        int total_infl = 0;

        
        for(int v=0;v<n;v++)
        {
            if(I.get(v).size() < node_sketch)
                continue;
            else {
                if(I.get(v).size() * n/sketch_num > infl_max) {
                    infl_max = I.get(v).size() * n/sketch_num;
                    max_node = v;
                }
            }
        }

        total_infl = set_infl + infl_max;

        System.out.println();
        System.out.println(
                           "Max Node = " + max_node +
                           ", Maximum Influence = " + total_infl);
        System.out.println();

        if((k - 1)==0)
            return;

        for(int u=0;u<n;u++) {
            if((I.get(u).size() < node_sketch) || (u == max_node))
                continue;
            else
            {
                I.get(u).removeAll(I.get(max_node));
            }
        }
        I.get(max_node).clear();
        
        get_seeds(I, k-1, sketch_num, total_infl);
    }
	
		

}
