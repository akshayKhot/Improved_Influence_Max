/**
 * Created by akshaykhot on 2017-05-26.
 */
public class Main {

    public static void main(String args[]) throws Exception {

        long startTime1, startTime2, estimatedTime1, estimatedTime2;

        String graphName = "cnr-2000-t";

        startTime1 = System.currentTimeMillis();
        InfluenceMax_list list = new InfluenceMax_list(graphName);
        list.get_sketch();
        estimatedTime1 = System.currentTimeMillis() - startTime1;

        System.out.println("------------------------------------------------------------------");

        startTime2 = System.currentTimeMillis();
        InfluenceMax_flat flat = new InfluenceMax_flat(graphName);
        flat.get_sketch();
        estimatedTime2 = System.currentTimeMillis() - startTime2;

        System.out.println("------------------------------------------------------------------");

        System.out.println("Time taken by List implementation: " + estimatedTime1 / 1000.0 + " seconds");
        System.out.println("Time taken by Flat implementation: " + estimatedTime2 / 1000.0 + " seconds");


    }

}
