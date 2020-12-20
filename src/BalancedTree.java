
public class BalancedTree {
    Node root;

    /**
     * 2-3 Tree class that supports some log(n) functions.
     */
    public BalancedTree() {
        root = new Node(null, null);
        root.size = 0;
    }

    /**
     * Updates the key of a given node.
     *
     * @param x Node.
     */
    public void updateKey(Node x) {
        if (x.left == null) {
            return;
        }
        x.size = x.left.size;
        x.sum = x.left.sum.createCopy();
        x.key = x.left.key.createCopy();
        if (x.middle != null) {
            x.key = x.middle.key.createCopy();
            x.size += x.middle.size;
            x.sum.addValue(x.middle.sum.createCopy());
        }
        if (x.right != null) {
            x.key = x.right.key.createCopy();
            x.size += x.right.size;
            x.sum.addValue(x.right.sum.createCopy());
        }
    }

    /**
     * Updates the value of a given node.
     *
     * @param x Node.
     */
    public void updateValue(Node x) {
        x.value = x.left.value.createCopy();
        if (x.middle != null) {
            x.value = x.middle.value.createCopy();
        }
        if (x.right != null) {
            x.value = x.right.value.createCopy();
        }
    }

    /**
     * Sets the children of a given node.
     *
     * @param x Node.
     * @param l Node's left child.
     * @param m Node's middle child.
     * @param r Node's right child.
     */
    public void setChildren(Node x, Node l, Node m, Node r) {
        x.left = l;
        x.middle = m;
        x.right = r;
        l.p = x;
        if (m != null) {
            m.p = x;
        }
        if (r != null) {
            r.p = x;
        }
        updateKey(x);
        updateValue(x);
    }

    /**
     * Insert child to given node and splits into two nodes if necessary.
     *
     * @param x Given node.
     * @param z Child node to add.
     * @return New node created, if not created returns null.
     */
    public Node insertAndSplit(Node x, Node z) {
        if (x == null) return null;
        Node l = x.left;
        Node m = x.middle;
        Node r = x.right;
        if (r == null) {
            if (z.key.compareTo(l.key) < 0) {
                setChildren(x, z, l, m);
            } else if (z.key.compareTo(m.key) < 0) {
                setChildren(x, l, z, m);
            } else {
                setChildren(x, l, m, z);
            }
            return null;
        }
        Node y = new Node(null, null);
        if (z.key.compareTo(l.key) < 0) {
            setChildren(x, z, l, null);
            setChildren(y, m, r, null);
        } else if (z.key.compareTo(m.key) < 0) {
            setChildren(x, l, z, null);
            setChildren(y, m, r, null);
        } else if (z.key.compareTo(r.key) < 0) {
            setChildren(x, l, m, null);
            setChildren(y, z, r, null);
        } else {
            setChildren(x, l, m, null);
            setChildren(y, r, z, null);
        }
        return y;
    }

    /**
     * Inserts a new leaf to the tree.
     *
     * @param newKey   Key.
     * @param newValue Value.
     */
    public void insert(Key newKey, Value newValue) {
        Node z = new Node(newKey.createCopy(), newValue.createCopy());
        z.size = 1;
        z.sum = z.value.createCopy();
        if (root == null || root.key == null) {
            root = z;
            return;
        }
        if (root.left == null) {
            Node w = new Node(null, null);
            if (root.key.compareTo(z.key) > 0) {
                setChildren(w, z, root, null);
            } else {
                setChildren(w, root, z, null);
            }
            root = w;
            return;
        }
        Node y = root;
        while (y.left != null) {
            if (z.key.compareTo(y.left.key) < 0) {
                y = y.left;
            } else if (z.key.compareTo(y.middle.key) < 0) {
                y = y.middle;
            } else {
                y = y.right != null ? y.right : y.middle;
            }
        }
        Node x = y.p;
        z = insertAndSplit(x, z);
        while (x != root && x != null) {
            x = x.p;
            if (z != null) {
                z = insertAndSplit(x, z);
            } else {
                updateKey(x);
            }
        }
        if (z != null) {
            Node w = new Node(null, null);
            setChildren(w, x, z, null);
            root = w;
        }
    }


    /**
     * Searches for a given key in the tree that rooted at given node.
     *
     * @param x   Node.
     * @param key Key to search.
     * @return Node that holds the given key, or null if not found.
     */
    private Node searchKey(Node x, Key key) {
        if (x == null) return null;
        if (x.left == null) {
            if (x.key.compareTo(key) == 0) {
                return x;
            }
            return null;
        }
        if (key.compareTo(x.left.key) <= 0) {
            return searchKey(x.left, key);
        } else if (x.middle != null && key.compareTo(x.middle.key) <= 0) {
            return searchKey(x.middle, key);
        }
        return searchKey(x.right, key);
    }


    /**
     * Borrows a node or merge nodes.
     *
     * @param y Node.
     * @return The parent of the nodes after merged or borrowed.
     */
    public Node borrowOrMerge(Node y) {
        Node z = y.p;
        if (y == z.left) {
            Node x = z.middle;
            if (x.right != null) {
                setChildren(y, y.left, x.left, null);
                setChildren(x, x.middle, x.right, null);
            } else {
                setChildren(x, y.left, x.left, x.middle);
                y = null;
                setChildren(z, x, z.right, null);
            }
            return z;
        }
        if (y == z.middle) {
            Node x = z.left;
            if (x.right != null) {
                setChildren(y, x.right, y.left, null);
                setChildren(x, x.left, x.middle, null);
            } else {
                setChildren(x, x.left, x.middle, y.left);
                y = null;
                setChildren(z, x, z.right, null);
            }
            return z;
        }
        Node x = z.middle;
        if (x.right != null) {
            setChildren(y, x.right, y.left, null);
            setChildren(x, x.left, x.middle, null);
        } else {
            setChildren(x, x.left, x.middle, y.left);
            y = null;
            setChildren(z, z.left, x, null);
        }
        return z;
    }

    /**
     * Deletes a key from the tree
     *
     * @param key Key to delete.
     */
    public void delete(Key key) {
        Node x = searchKey(root, key);
        if (x == root) {
            root = null;
            return;
        }
        if (x != null) {
            Node y = x.p;
            if (x == y.left) {
                if (y.middle == null) {
                    y.left = null;
                } else {
                    setChildren(y, y.middle, y.right, null);
                }
            } else if (x == y.middle) {
                setChildren(y, y.left, y.right, null);
            } else {
                setChildren(y, y.left, y.middle, null);
            }
            x = null;
            while (y != null) {
                if (y.middle == null) {
                    if (y != root) {
                        y = borrowOrMerge(y);
                    } else {
                        root = y.left;
                        y.left.p = null;
                        y = null;
                        return;
                    }
                } else {
                    updateKey(y);
                    y = y.p;
                }
            }
        }
    }


    /**
     * Searches for a key in the tree.
     *
     * @param key Key to search.
     * @return Value of the node that holds the given key, null if not found.
     */
    public Value search(Key key) {
        Node x = searchKey(root, key);
        if (x != null) {
            return x.value;
        }
        return null;
    }


    /**
     * The rank of the given key in the linear order.
     *
     * @param key Key to search.
     * @return The rank in the linear order, if not found return 0.
     */
    public int rank(Key key) {
        int rank = 1;
        Node x = searchKey(root, key);
        if (x != null) {
            Node y = x.p;
            while (y != null) {
                if (x == y.middle) {
                    rank = rank + y.left.size;
                } else if (x == y.right) {
                    rank = rank + y.left.size + y.middle.size;
                }
                x = y;
                y = y.p;
            }
            return rank;
        }
        return 0;
    }

    /**
     * Selects the key that found in the 'index' position in the linear order.
     *
     * @param index Position in the linear order.
     * @return The key of the given index.
     */
    public Key select(int index) {
        if (index == 0) {
            return null;
        }
        Node x = selectAux(root, index);
        return x == null ? null : x.key.createCopy();
    }

    /**
     * Searches for the node that found in the 'i' position in the linear order rooted at a given node.
     *
     * @param x Root
     * @param i Position in the linear order.
     * @return Node of the 'i' position in the linear order, or null if not found.
     */
    private Node selectAux(Node x, int i) {
        if (x == null || x.size < i) {
            return null;
        }
        if (x.left == null) {
            return x;
        }
        int s_left = x.left.size;
        int s_left_middle = x.middle == null ? x.left.size : x.left.size + x.middle.size;
        if (i <= s_left) {
            return selectAux(x.left, i);
        } else if (i <= s_left_middle) {
            return selectAux(x.middle, i - s_left);
        }
        return selectAux(x.right, i - s_left_middle);
    }


    /**
     * Finds the successor of the node that holds the given key in the tree or the node itself if found.
     *
     * @param key Key to search.
     * @return The node that holds the key if found in the tree, otherwise return the successor node.
     */
    private Node Successor(Key key) {
        Node y = searchKey(root, key);
        if (y != null) return y;
        Node x = root;
        if (x == null) return null;
        while (x != null) {
            if (x.left == null) return x;
            if (key.compareTo(x.left.key) < 0) {
                x = x.left;
            } else if (x.middle != null && key.compareTo(x.middle.key) < 0) {
                x = x.middle;
            } else {
                x = x.right;
            }
        }
        return null;
    }


    /**
     * Finds the node that is in the rightmost position in the tree rooted at the given node.
     *
     * @param x Root.
     * @return The rightmost node in the given tree, if the tree is empty then return null.
     */
    private Node getRightNode(Node x) {
        Node y = null;
        while (x != null) {
            if (x.right != null) {
                x = x.right;
            } else if (x.middle != null) {
                x = x.middle;
            } else if (x.left != null) {
                x = x.left;
            } else {
                return x;
            }
            y = x.p;
        }
        return y;
    }

    /**
     * Sum values of leaves in a given interval.
     *
     * @param key1 Left interval's key.
     * @param key2 Right interval's key.
     * @return The sum of values in the given interval, return null if no keys are in the interval.
     */
    public Value sumValuesInInterval(Key key1, Key key2) {
        if (root == null) return null;
        Key max = key2.createCopy();
        Key min = key1.createCopy();
        Node left = Successor(min);
        if (left == null) {
            left = getRightNode(root);
        }
        Node right = Successor(max);
        if (right == null) {
            right = getRightNode(root);
        } else {
            if (right.key.compareTo(max) > 0) {
                int rank_right = rank(right.key);
                if (rank_right > 1) {
                    right = selectAux(root, rank_right - 1);
                }
            }
        }
        if (right.key.compareTo(left.key) < 0) {
            return null;
        }
        if (left.key.compareTo(max) > 0 || right.key.compareTo(min) < 0) {
            return null;
        }
        if (left == right) {
            return left.value.createCopy();
        }
        Value total_l = null;
        Value total_r = null;
        while (left != right) {
            // left
            if (left == left.p.right) {
                if (left.left == null) {
                    total_l = left.value.createCopy();
                }
            } else if (left == left.p.middle) {
                if (left.left == null) {
                    total_l = left.value.createCopy();
                }
                if (left.p.right != null && left.p.right != right) {
                    total_l.addValue(left.p.right.sum.createCopy());
                }
            } else {
                if (left.left == null) {
                    total_l = left.value.createCopy();
                }
                if (left.p.middle != null && left.p.middle != right) {
                    total_l.addValue(left.p.middle.sum.createCopy());
                }
                if (left.p.right != null && left.p.right != right && left.p.middle != right) {
                    total_l.addValue(left.p.right.sum.createCopy());
                }
            }
            //right
            if (right == right.p.right) {
                if (right.left == null) {
                    total_r = right.value.createCopy();
                }
                if (right.p.middle != left && right.p.left != left) {
                    total_r.addValue(right.p.middle.sum.createCopy());
                    total_r.addValue(right.p.left.sum.createCopy());
                }
            } else if (right == right.p.middle) {
                if (right.left == null) {
                    total_r = right.value.createCopy();
                }
                if (right.p.left != left) {
                    total_r.addValue(right.p.left.sum.createCopy());
                }
            } else {
                if (right.left == null) {
                    total_r = right.value.createCopy();
                }
            }
            left = left.p;
            right = right.p;
        }
        total_l.addValue(total_r.createCopy());
        return total_l.createCopy();
    }
}
