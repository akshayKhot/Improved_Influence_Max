import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class InfluenceMax_list extends InfluenceMax{
	
	public InfluenceMax_list(String basename, int beta) throws Exception {
        super(basename, beta);
	}

	public void get_sketch() {

        List<List<Integer>> I = new ArrayList<>();
        for(int j=0;j<n;j++)
            I.add(new ArrayList<>());
                           
	    double weight_of_current_index = 0.0;
	    int index = 0;
        int sketch_num = 0;

        Random gen_rnd = new Random();

	    while(weight_of_current_index < W)
	    {
	    	int v = permutation[gen_rnd.nextInt(n)];
	        marked.clear();
	        BFS(v,marked);

	        int total_out_degree = 0;
	        for (int u = marked.nextSetBit(0); u >= 0; u = marked.nextSetBit(u+1))
	        {
                I.get(u).add(sketch_num);
	            total_out_degree += G.outdegree(u);
	        }
	        weight_of_current_index += (marked.cardinality() + total_out_degree);
	        index = (index+1) % n;
            sketch_num++;
	    }

	    System.out.println("Index: " + index + " Number of Sketches: " + sketch_num);

        System.gc();
        int set_infl = 0;
        get_seeds(I, k, sketch_num, set_infl);
	}
    
    void get_seeds(List<List<Integer>> I, int k, int sketch_num, int set_infl) {

        int infl_max = 0;
        int max_node = 0;
        int total_infl = 0;

        for(int v=0;v<n;v++)
        {
            if(I.get(v).size() < 1)
                continue;
            else {
                int temp = I.get(v).size() * n/sketch_num;
                if(temp > infl_max) {
                    infl_max = temp;
                    max_node = v;
                }
            }
        }

        total_infl = set_infl + infl_max;

        System.out.println("Max Node = " + max_node + ", Its Influence = " + infl_max);

        if((k - 1)==0) {
            System.out.println("Total Influence of " + this.k + " nodes = " + total_infl);
            return;
        }

        for(int u=0;u<n;u++) {
            if((I.get(u).size() < 1) || (u == max_node))
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
