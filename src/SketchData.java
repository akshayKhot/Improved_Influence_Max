import java.util.List;

public class SketchData {

    private int sketch_num;
    private List<List<Integer>> I;
    private int index;

    public SketchData(int sketch_num, List<List<Integer>> I, int index) {
        this.sketch_num = sketch_num;
        this.I = I;
        this.index = index;
    }

    public int getSketch_num() {
        return sketch_num;
    }

    public List<List<Integer>> getI() {
        return I;
    }

    public int getIndex() {
        return index;
    }
}
