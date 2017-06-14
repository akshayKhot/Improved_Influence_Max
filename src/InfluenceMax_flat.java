import java.util.Random;

public class InfluenceMax_flat extends InfluenceMax {

    int nMAX = 10000000;
    int[] sketches;
    int[] nodes;
    int[] node_infl;

    int count_sketches;

	public InfluenceMax_flat(String basename, int beta) throws Exception {
		super(basename, beta);

		sketches = new int[nMAX];
        nodes = new int[nMAX];
        node_infl = new int[n];
        for(int i=0;i<nMAX;i++)
        {
            sketches[i] = -1;
            nodes[i] = -1;
        }

        get_sketch();
	}

	public void get_sketch() {

        long sketchStartTime = System.currentTimeMillis();

	    double weight_of_current_index = 0.0;
	    int index = 0;
        int sketch_num = 0;

        count_sketches = 0;
        Random gen_rnd = new Random();

	    while(weight_of_current_index < W) {
	    	int v = permutation[gen_rnd.nextInt(n)];
	        marked.clear();
	        BFS(v,marked);

	        int total_out_degree = 0;
            int iteration = 0;
	        for (int u = marked.nextSetBit(0); u >= 0; u = marked.nextSetBit(u+1))
	        {
                sketches[count_sketches + iteration] = sketch_num;
                nodes[count_sketches + iteration] = u;
                node_infl[u] = node_infl[u] + 1;
                iteration = iteration++;
	            total_out_degree += G.outdegree(u);
	        }

	        weight_of_current_index += (marked.cardinality() + total_out_degree);
	        index = ( index + 1 ) % n;
            sketch_num++;
            count_sketches += marked.cardinality();
	    }

        System.out.println("Number of Sketches: " + sketch_num);

        // Cutting off the tails of sketches and nodes arrays, making the arrays shorter
        int[] iSketch = new int[count_sketches + 1];
        System.arraycopy(sketches,0,iSketch,0,count_sketches);

        int[] iNode = new int[count_sketches + 1];
        System.arraycopy(nodes,0,iNode,0,count_sketches);

        long sketchEndTime = System.currentTimeMillis() - sketchStartTime;

        System.gc();

        int set_infl = 0;

        long seedStartTime = System.currentTimeMillis();
        get_seeds(iSketch, iNode, node_infl, k, count_sketches, sketch_num, set_infl);
        long seedEndTime = System.currentTimeMillis() - seedStartTime;

        System.out.println("Time taken to get sketches: " + sketchEndTime/1000.0 + " seconds");
        System.out.println("Time taken to compute seeds: " + seedEndTime/1000.0 + " seconds");
	}

    void get_seeds(int[] sketch_I, int[] node_I, int[] node_infl, int k, int count_sketches, int sketch_num, int set_infl) {

        int infl_max = 0;
        int max_node = 0;
        int total_infl = 0;

        for(int v=0;v<n;v++)
        {
            if(node_infl[v] < 1)
                continue;
            else
            {
                int temp = node_infl[v];
                if(temp > infl_max)
                {
                    infl_max = temp;
                    max_node = v;
                }
            }
        }

        infl_max = node_infl[max_node] * n / sketch_num;
        total_infl = set_infl + infl_max;

        //System.out.println("Max Node = " + max_node + ", Its Influence = " + infl_max);

        if((k - 1)==0) {
            System.out.println("Total Influence of " + this.k + " nodes = " + total_infl + "\n");
            return;
        }

        // Re-calculating the influence of the remaining nodes: remove max node and the sketches it participated in
        // plus re-calculate the influence
        for(int j=0;j<count_sketches;j++)
        {
            if(node_I[j] == -1)
                continue;
            else
            {
                if(node_I[j] == max_node)
                {
                    int redundant_sketch = sketch_I[j];

                // As sketches are added to the array in numerical order, the same redundant sketch can be found before and after the max node
                    int l = j+1;
                    while(sketch_I[l] == redundant_sketch) {
                        node_infl[node_I[l]] = node_infl[node_I[l]] - 1;
                        sketch_I[l] = -1;
                        node_I[l] = -1;
                        l++;
                    }
                    if(j>0) // (j!=0) Boundary of the arrays sketch_I and node_I
                    {
                        int ll = j-1;
                        while(sketch_I[ll] == redundant_sketch) {
                            node_infl[node_I[ll]] = node_infl[node_I[ll]] - 1;
                            sketch_I[ll] = -1;
                            node_I[ll] = -1;
                            ll--;
                        }
                    }
                    sketch_I[j] = -1;
                    node_I[j] = -1;
                }
            }
        }
        node_infl[max_node] = 0;

        get_seeds(sketch_I, node_I, node_infl, k-1, count_sketches, sketch_num, total_infl);
    }



}
