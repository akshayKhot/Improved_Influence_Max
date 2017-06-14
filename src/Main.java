
public class Main {

    public static void main(String args[]) throws Exception {

        long startTime1, startTime2, estimatedTime1, estimatedTime2;
        int[] betas = {2, 4};

        String graphName = "cnr-2000-t";
        System.out.println("\nG = " + graphName);
        System.out.println("=======================");


        for(int i=0; i<betas.length; i++) {

            System.out.print("List");
            startTime1 = System.currentTimeMillis();
                new InfluenceMax_list(graphName, betas[i]);
            estimatedTime1 = System.currentTimeMillis() - startTime1;
            System.out.println("Total time(List): " + estimatedTime1 / 1000.0 + " seconds\n");

            System.out.println("----------------------");

            System.out.print("Flat");
            startTime2 = System.currentTimeMillis();
                new InfluenceMax_flat(graphName, betas[i]);
            estimatedTime2 = System.currentTimeMillis() - startTime2;
            System.out.println("Total time(Flat): " + estimatedTime2 / 1000.0 + " seconds\n");

            System.out.println("------------------------------------------------------------------");
        }


    }

}
