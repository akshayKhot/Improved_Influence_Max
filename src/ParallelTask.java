//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//import java.util.concurrent.Callable;
//import java.util.BitSet;
//
//public class ParallelTask extends InfluenceMax_parallel implements Callable<SketchData>  {
//
//    private int numOfProcessors;
//    private double weight_of_current_index;
//    private int sketch_num;
//    private int index;
//    private BitSet influenced_nodes;
//    private List<List<Integer>> I;
//
//    public ParallelTask(int numOfProcessors) {
//
//        this.numOfProcessors = numOfProcessors;
//        this.influenced_nodes = new BitSet(n);
//        I = new ArrayList<>();
//        for(int j=0;j<n;j++)
//            I.add(new ArrayList<>());
//    }
//
//    @Override
//    public SketchData call() throws Exception {
//        Random gen_rnd = new Random();
//        double W_i = W / numOfProcessors;
//
//        while(weight_of_current_index < W_i)
//        {
//            int v = permutation[gen_rnd.nextInt(n)];
//            influenced_nodes.clear();
//            BFS(v, influenced_nodes);
//
//            int total_out_degree = 0;
//            for (int u = influenced_nodes.nextSetBit(0); u >= 0; u = influenced_nodes.nextSetBit(u+1))
//            {
//                I.get(u).add(sketch_num);
//                total_out_degree += G.outdegree(u);
//            }
//            weight_of_current_index += (influenced_nodes.cardinality() + total_out_degree);
//            index = (index+1) % n;
//            sketch_num++;
//        }
//
//        return new SketchData(sketch_num, I, index);
//    }
//}
