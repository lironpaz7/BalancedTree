public class Node {
    Key key;
    Value value;
    Node left;
    Node middle;
    Node right;
    Node p;
    int size = 0;
    Value sum;

    /**
     * Node of the tree.
     * @param key The node's key.
     * @param value The node's value.
     */
    public Node(Key key, Value value) {
        this.key = key;
        this.value = value;
    }

}
