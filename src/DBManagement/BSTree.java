package DBManagement;

import java.util.ArrayList;

/**
 * Balanced tree for log(n) searching
 *
 * @author Matteus Schmidt
 * ITP 265, SPRING 24', Coffee
 * Email: maschmid@usc.edu
 * Date Created: 4/24/24
 */

public final class BSTree {
    private final class Node { // complete
        private String[] key;
        private Node left, right;

        public Node(String[] key) {
            this.key = key;
        }
    }

    private int comparatorIndex;
    private Node root;
    private int size;

    public BSTree(int comparatorIndex) { // complete
        this.comparatorIndex = comparatorIndex;
        root = null;
        size = 0;
    }

    public void insert(String[] key) { // complete
        if (size == 0) {
            root = new Node(key);
            size++;
        }
        else {
            boolean isDouble;
            try {
                Double.parseDouble(key[comparatorIndex]);
                isDouble = true;
            } catch (NumberFormatException e) {
                isDouble = false;
            }
            insertHelper(root, key, isDouble);
        }
    }

    // A recursive function to insert a new key in BST
    private Node insertHelper(Node pointer, String[] key, boolean isDouble) { // complete
        if (pointer == null) {
            pointer = new Node(key);
            size++;
            return pointer;
        }
        if (isDouble) {
            Double keyDouble = Double.parseDouble(key[comparatorIndex]);
            Double pointerDouble = Double.parseDouble(pointer.key[comparatorIndex]);
            if (keyDouble.compareTo(pointerDouble) < 0) pointer.left = insertHelper(pointer.left, key, isDouble);
            else if (keyDouble.compareTo(pointerDouble) > 0) pointer.right = insertHelper(pointer.right, key, isDouble);
            else pointer.key = key;
        }
        else {
            if (key[comparatorIndex].compareTo(pointer.key[comparatorIndex]) < 0) pointer.left = insertHelper(pointer.left, key, isDouble);
            else if (key[comparatorIndex].compareTo(pointer.key[comparatorIndex]) > 0) pointer.right = insertHelper(pointer.right, key, isDouble);
            else pointer.key = key;
        }
        return pointer;
    }

    public boolean contains(String key) { // complete
        return containsHelper(root, key);
    }

    private boolean containsHelper(Node pointer, String key) { // complete
        if (pointer == null) return false;
        if (pointer.key[comparatorIndex].equals(key)) return true;
        try {
            Double keyDouble = Double.parseDouble(key);
            Double pointerDouble = Double.parseDouble(pointer.key[comparatorIndex]);
            if (keyDouble.compareTo(pointerDouble) < 0) return containsHelper(pointer.left, key);
            else return containsHelper(pointer.right, key);
        } catch (NumberFormatException e) {
            if (key.compareTo(pointer.key[comparatorIndex]) < 0) return containsHelper(pointer.left, key);
            else return containsHelper(pointer.right, key);
        }
    }

    public String[] getStrUser(String key) { return getStrUserHelper(root, key); }
    private String[] getStrUserHelper(Node pointer, String key) {
        if (pointer == null) return null;
        if (pointer.key[comparatorIndex].equals(key)) return pointer.key;

        if (key.compareTo(pointer.key[comparatorIndex]) < 0) return getStrUserHelper(pointer.left, key);
        else return getStrUserHelper(pointer.right, key);
    }

    public ArrayList<String[]> inOrderTraversal() {
        ArrayList<Node> list = inOrderTraversal(root);
        ArrayList<String[]> returnList = new ArrayList<>();
        for (Node node : list) returnList.add(node.key);
        return returnList;
    }

    private ArrayList<Node> inOrderTraversal(Node root) { // complete
        ArrayList<Node> returnList = new ArrayList<>();
        inOrderHelper(root, returnList);
        return returnList;
    }

    private void inOrderHelper(Node root, ArrayList<Node> returnList) { // complete
        if (root == null) return;
        inOrderHelper(root.left, returnList);
        returnList.add(root);
        inOrderHelper(root.right, returnList);
    }

    public String[] getMax() { return getMax(root); }

    public String[] getMax(Node user) {
        if (user == null) throw new NullPointerException();
        if (user.right != null) return getMax(user.right);
        return user.key;
    }
}
