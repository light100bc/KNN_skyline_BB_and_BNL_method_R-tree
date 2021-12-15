import java.util.LinkedList;

public class Node {
        final float[] coords;
        final float[] dimensions;
        final LinkedList<Node> children;
        final boolean leaf;
        Node parent;

        Node(float[] coords, float[] dimensions, boolean leaf) {
            this.coords = new float[coords.length];
            this.dimensions = new float[dimensions.length];
            System.arraycopy(coords, 0, this.coords, 0, coords.length);
            System.arraycopy(dimensions, 0, this.dimensions, 0, dimensions.length);
            this.leaf = leaf;
            children = new LinkedList<Node>();
        }
}
