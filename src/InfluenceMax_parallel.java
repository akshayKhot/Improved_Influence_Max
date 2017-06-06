import org.omg.SendingContext.RunTime;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class InfluenceMax_parallel extends InfluenceMax {

    private int numOfProcessors;
    List<List<Integer>> I;

    public InfluenceMax_parallel(String basename, int beta) throws Exception {
        super(basename, beta);
        I = new ArrayList<>();
        for(int j=0;j<n;j++)
            I.add(new ArrayList<>());
        get_sketch();
    }

    ////////////
    class ParallelTask implements Callable<SketchData> {

        private double weight_of_current_index;
        private int sketch_num;
        private int index;
        private BitSet influenced_nodes;

        public ParallelTask() {
            this.influenced_nodes = new BitSet(n);
        }

        @Override
        public SketchData call() throws Exception {
            Random gen_rnd = new Random();
            double W_i = W / numOfProcessors;

            while(weight_of_current_index < W_i)
            {
                int v = permutation[gen_rnd.nextInt(n)];
                influenced_nodes.clear();
                BFS(v, influenced_nodes);

                int total_out_degree = 0;
                for (int u = influenced_nodes.nextSetBit(0); u >= 0; u = influenced_nodes.nextSetBit(u+1))
                {
                    I.get(u).add(sketch_num);
                    total_out_degree += G.outdegree(u);
                }
                weight_of_current_index += (influenced_nodes.cardinality() + total_out_degree);
                index = (index+1) % n;
                sketch_num++;
            }

            return new SketchData(sketch_num, I, index);
        }
    }

    ////////////

    public void get_sketch() {

        long sketchStartTime = System.currentTimeMillis();

        List<Future> futureList = new ArrayList<>();
        numOfProcessors = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(20);

        for(int i=0; i<4; i++)
            futureList.add(executorService.submit(new ParallelTask()));

        ArrayList<SketchData> sketch_objects = new ArrayList<>();
        int index = 0, sketch_num = 0;
        for(Future future : futureList) {
            try {
                SketchData sk = (SketchData) future.get();
                sketch_objects.add(sk);
                index += sk.getIndex();
                sketch_num += sk.getSketch_num();
            } catch (InterruptedException e) {}
            catch (ExecutionException e) {}
        }


        long sketchEndTime = System.currentTimeMillis() - sketchStartTime;


        System.out.println("Index: " + index + " Number of Sketches: " + sketch_num);
        System.gc();

        int set_infl = 0;

        long seedStartTime = System.currentTimeMillis();
        get_seeds(I, k, sketch_num, set_infl);
        long seedEndTime = System.currentTimeMillis() - seedStartTime;

        System.out.println("\tTime taken to get sketches: " + sketchEndTime/1000.0 + " seconds");
        System.out.println("\tTime taken to compute seeds: " + seedEndTime/1000.0 + " seconds");
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
                int temp = I.get(v).size();
                if(temp > infl_max) {
                    infl_max = temp;
                    max_node = v;
                }
            }
        }

        infl_max = I.get(max_node).size() * n/sketch_num;
        total_infl = set_infl + infl_max;

        System.out.println("Max Node = " + max_node + ", Its Influence = " + infl_max);

        if((k - 1)==0) {
            System.out.println("Total Influence of " + this.k + " nodes = " + total_infl + "\n");
            return;
        }

        List<Integer> nodes_in_max_node = I.get(max_node);
        for(int u=0;u<n;u++) {
            if((I.get(u).size() < 1) || (u == max_node))
                continue;
            else
                I.get(u).removeAll(nodes_in_max_node);
        }
        I.get(max_node).clear();

        get_seeds(I, k-1, sketch_num, total_infl);
    }
}
