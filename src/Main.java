
public class Main {

    public static void main(String args[]) throws Exception {

        long startTime1, startTime2, estimatedTime1, estimatedTime2;
        int[] betas = {2, 4};

        String graphName = "cnr-2000-t";
        System.out.println("\nG = " + graphName);
        System.out.println("----------------------");


        for(int b : betas) {
            startTime1 = System.currentTimeMillis();
            new InfluenceMax_list(graphName, b);
            estimatedTime1 = System.currentTimeMillis() - startTime1;
            System.out.println("Total time(List): " + estimatedTime1 / 1000.0 + " seconds");
        }

        System.out.println("------------------------------------------------------------------");

        for(int b : betas) {
            startTime2 = System.currentTimeMillis();
            new InfluenceMax_flat(graphName, b);
            estimatedTime2 = System.currentTimeMillis() - startTime2;
            System.out.println("Total time(Flat): " + estimatedTime2 / 1000.0 + " seconds");
        }

    }

}
