public class Node {
    Key key;
    Value value;
    Node left;
    Node middle;
    Node right;
    Node p;
    int size = 0;
    Value sum;

    public Node(Key key, Value value) {
        this.key = key;
        this.value = value;
    }

}
