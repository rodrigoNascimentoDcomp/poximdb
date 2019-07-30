import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

class Tree {

    public int order;
    public Node[] keys;
    public Tree[] children;

    public Tree(int order) {
        this.order = order;
        this.keys = new Node[order - 1];
        this.children = new Tree[order];
    }
}

class Node {

    public String key;
    public String name;
    public int size;

    public Node() {
        this.key = null;
        this.name = null;
        this.size = 0;
    }

    public Node(String key, String name, int size) {
        this.key = key;
        this.name = name;
        this.size = size;
    }
}

public class rodrigonascimento_201600155174_poximdb {

    public static Tree tree;
    public static int order;

    /**
     * Reads the line of the input with the file information
     * and returns a node containing it.
     * 
     * @param line  Input line.
     * @return      Node containing the information from the input line.
     */
    public static Node makeNode(String line) {

        Node node = new Node();
        
        int spaceIndex = line.indexOf(" ");
        node.name = line.substring(0, spaceIndex);

        line = line.substring(spaceIndex + 1);

        spaceIndex = line.indexOf(" ");
        node.size = Integer.parseInt(line.substring(0, spaceIndex));

        line = line.substring(spaceIndex + 1);

        node.key = line;

        return node;

    }

    public static Node[] addToNodeArray(Node[] nodes, Node newNode) {
        Node[] nodeArray = new Node[tree.order];

        System.arraycopy(nodes, 0, nodeArray, 0, nodes.length);
        
        nodeArray = insertNode(nodeArray, newNode);

        return nodeArray;
    }

    public static Tree splitTree(Tree firstTree, Tree secondTree, int secondTreeIndex) {
        Tree finalTree = new Tree(firstTree.order);
        Tree leftTree = new Tree(firstTree.order);
        Tree rightTree = new Tree(firstTree.order);

        // Gets the middle index of the original tree
        int midIndex;
        if (firstTree.order % 2 == 0) {
            midIndex = (int) (firstTree.order / 2) - 1;
        } else {
            midIndex = (int) firstTree.order / 2;
        }

        if (secondTreeIndex == midIndex) {

            finalTree.keys[0] = secondTree.keys[0];

            // Left child
            for (int i = 0; i < midIndex; i++) {
                leftTree.keys[i] = firstTree.keys[i];
                leftTree.children[i] = firstTree.children[i];
            }

            leftTree.children[midIndex] = secondTree.children[0];

            // Right child
            int j = 0;
            for (int i = midIndex; i < firstTree.order - 1; i++) {
                rightTree.keys[j] = firstTree.keys[i];
                rightTree.children[++j] = firstTree.children[i + 1];
            }

            rightTree.children[midIndex - 1] = secondTree.children[1];
            
        } else if (secondTreeIndex < midIndex) {

            finalTree.keys[0] = firstTree.keys[midIndex - 1];

            // Left child
            int j = 0;
            for (int i = 0; i < midIndex; i++) {
                if (i == secondTreeIndex) {
                    leftTree.keys[j] = secondTree.keys[0];
                    leftTree.children[j] = secondTree.children[0];
                    leftTree.children[++j] = secondTree.children[1];
                } else if (i < secondTreeIndex) {
                    leftTree.keys[j] = firstTree.keys[i];
                    leftTree.children[j++] = firstTree.children[i];
                } else {
                    leftTree.keys[j] = firstTree.keys[i];
                    leftTree.children[++j] = firstTree.children[i];
                }
            }

            leftTree.children[midIndex] = firstTree.children[midIndex];

            // Right child
            j = 0;
            for (int i = midIndex; i < firstTree.order - 1; i++) {
                rightTree.keys[j] = firstTree.keys[i];
                rightTree.children[j] = firstTree.children[i];
                j++;
            }

            rightTree.children[j] = firstTree.children[firstTree.order - 1];
            
        } else {

            finalTree.keys[0] = firstTree.keys[midIndex];

            // Left child
            int j = 0;
            for (int i = 0; i < midIndex; i++) {
                leftTree.keys[j] = firstTree.keys[i];
                leftTree.children[j] = firstTree.children[i];
                j++;
            }

            leftTree.children[j] = firstTree.children[j];

            // Right child
            j = 0;
            for (int i = midIndex + 1; i < rightTree.order; i++) {
                if (i == secondTreeIndex) {
                    rightTree.keys[j] = secondTree.keys[0];
                    rightTree.children[j] = secondTree.children[0];
                    rightTree.children[++j] = secondTree.children[1];
                } else if (i < secondTreeIndex) {
                    rightTree.keys[j] = firstTree.keys[i];
                    rightTree.children[j++] = firstTree.children[i];
                } else {
                    rightTree.keys[j] = firstTree.keys[i];
                    rightTree.children[++j] = firstTree.children[i];
                }
            }
            
        }

        finalTree.children[0] = leftTree;
        finalTree.children[1] = rightTree;

        return finalTree;
    }

    public static Tree splitTree(Tree tree, Node newNode) {
        Node[] nodeArray = new Node[tree.order];

        System.arraycopy(tree.keys, 0, nodeArray, 0, tree.keys.length);
        
        nodeArray = insertNode(nodeArray, newNode);
        int middleIndex;
        if (nodeArray.length % 2 == 0)
            middleIndex = (int) (nodeArray.length / 2) - 1;
        else
            middleIndex = (int) nodeArray.length / 2;

        Tree newTree = new Tree(tree.order);
        Tree leftChild = new Tree(tree.order);
        Tree rightChild = new Tree(tree.order);

        // Makes the middle node of the array
        // the new parent node
        newTree.keys[0] = nodeArray[middleIndex];

        // Copies the first half of the node array
        // into the left child
        for (int i = 0; i < middleIndex; i++) {
            leftChild.keys[i] = nodeArray[i];
        }

        // Copies the second half of the array
        // into the right child
        int j = 0;
        for (int i = middleIndex + 1; i < nodeArray.length; i++) {
            rightChild.keys[j] = nodeArray[i];
            j++;
        }

        // Adds the children to the tree
        newTree.children[0] = leftChild;
        newTree.children[1] = rightChild;

        return newTree;
    }

    /**
     * Gets a subtree out of a tree.
     * 
     * @param tree      Original tree.
     * @param begining  Starting index.
     * @param end       End index (exclusive).
     * @return          Subtree.
     */
    public static Tree getSubTree(Tree tree, int begining, int end) {
        
        Tree newTree = new Tree(tree.order);

        for (int i = begining; i < end; i++) {
            newTree.keys[i] = tree.keys[i];
            newTree.children[i] = tree.children[i];
        }

        newTree.children[end] = tree.children[end];

        return newTree;
    }

    /**
     * Inserts a new node into a Node array.
     * 
     * @param nodes     The array into which the new node shall be added.
     * @param newNode   The new node that will be added to the array.
     * @return          The array with the added node.
     */
    public static Node[] insertNode(Node[] nodes, Node newNode) {
        Node aux;
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] == null) {
                nodes[i] = newNode;
            } else if (newNode.key.compareTo(nodes[i].key) < 0) {
                aux = nodes[i];
                nodes[i] = newNode;
                newNode = aux;
            }
        }
        return nodes;
    }

    /**
     * Inserts a new node into the tree.
     * 
     * @param newNode Node to be inserted on the tree.
     */
    public static Tree insertNode(Tree tree, Node newNode) {

        // If the tree is null, add to the first key position
        if (tree == null) {
            tree = new Tree(order);
            tree.keys[0] = newNode;
            return tree;
        }

        // If the tree is a leaf
        if (tree.children[0] == null) {

            // If the keys are not full, add to them
            if (tree.keys[tree.order - 2] == null) {

                for (int i = 0; i < tree.order - 1; i++) {

                    // Adds to the first empty node
                    if (tree.keys[i] == null) {

                        tree.keys[i] = newNode;
                        return tree;

                    // Swaps the new node with the current node
                    // and ads the current node to the tree again through recursion
                    } else if (newNode.key.compareTo(tree.keys[i].key) <= 0) {
                        
                        Node auxNode = tree.keys[i];
                        tree.keys[i] = newNode;
                        return insertNode(tree, auxNode);

                    }
                }

                return tree;

            } else {
                // Create a new Tree
                return splitTree(tree, newNode);
            }

        } else {
            // Go down

            for (int i = 0; i < tree.order - 1; i++) {

                Tree auxTree = new Tree(tree.order);
                
                // Go left
                if (tree.keys[i] == null || newNode.key.compareTo(tree.keys[i].key) <= 0) {

                    auxTree = insertNode(tree.children[i], newNode);

                    if (auxTree.keys[0] != null) {
                        // A leaf was returned
                        if (auxTree.children[0] == null) {

                            tree.children[i] = auxTree;

                        } else {  // A tree was returned

                            if (auxTree != tree.children[i]) {
                                if (tree.keys[i] == null) {
                                    tree.keys[i] = auxTree.keys[0];
                                    tree.children[i] = auxTree.children[0];
                                    tree.children[i + 1] = auxTree.children[1];
                                } else {
                                    return splitTree(tree, auxTree, i);
                                }
                            }
                            
                        }
                    }

                } else if (i == tree.order - 2) {
                    
                    auxTree = insertNode(tree.children[i + 1], newNode);  // Go right

                    if (auxTree.keys[0] != null) {
                        // A leaf was returned
                        if (auxTree.children[0] == null) {
                            tree.children[i + 1] = auxTree;
                        } else {  // A tree was returned
    
                            if (tree.keys[i] == null) {
                                tree.keys[i] = auxTree.keys[0];
                                tree.children[i] = auxTree.children[0];
                                tree.children[i + 1] = auxTree.children[1];
                            } else {
                                tree = splitTree(tree, auxTree, i + 1);
                            }
                        }
                    }
                }
            }

            return tree;
        }
    }

    public static Tree insertTree(Tree tree, Tree subtree) {

        if (tree == null) {
            return subtree;
        }

        // tree is a leaf
        if (tree.children[0] == null) {

            boolean addRight = false;
            // Iterates over the tree to insert the new tree
            for (int i = 0; i < tree.order - 1; i++) {

                // if there's a tree left on the right, split

                // Compares the key on the position i of the original tree
                // with the key from the subtree
                int comparison = tree.keys[i].key.compareTo(subtree.keys[0].key);
                if (comparison > 0) {
                    addRight = true;
                }

                if (!addRight) {
                    
                }

            }

            return tree;

        } else {    // tree is not a leaf

            // Go down

            return tree;
        }
        
    }

    /**
     * Returns the requested node from the tree if found.
     * Otherwise, returns null.
     * 
     * @param key   Key to be used in the search.
     * @return      Requested node if found, null otherwise.
     */
    public static Node selectNode(Tree tree, String key) {

        if (tree == null)
            return null;

        for (int i = 0; i < tree.order - 1; i++) {

            if (tree.keys[i] == null) {

                return selectNode(tree.children[i], key);

            } else if (key.compareTo(tree.keys[i].key) == 0) {

                return tree.keys[i];

            } else if (key.compareTo(tree.keys[i].key) < 0) {

                return selectNode(tree.children[i], key);

            } else if (tree.order - 2 == i) {
                return selectNode(tree.children[i + 1], key);
            }
        }

        return null;
    }

    /**
     * Writes content to file.
     * 
     * @param fileName  Name of the file (with extension) to be writen.
     * @param content   Content to be writen on the file.
     * @throws FileNotFoundException
     */
    private static void writeToFile(String fileName, String content) throws FileNotFoundException {

        try(FileWriter fw = new FileWriter(fileName, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.print(content);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        
        try (FileInputStream inputStream = new FileInputStream(args[0])) {

            /*Tree oldTree = new Tree(3);
            Tree left = new Tree(3);
            Tree mid = new Tree(3);
            Tree right = new Tree(3);
            Tree newT = new Tree(3);
            Tree leftNew = new Tree(3);
            Tree rightNew = new Tree(3);

            oldTree.keys[0] = new Node("2", "2", 2);
            oldTree.keys[1] = new Node("4", "4", 4);
            left.keys[0] = new Node("1", "1", 1);
            mid.keys[0] = new Node("3", "3", 3);
            right.keys[0] = new Node("5", "5", 5);
            right.keys[0] = new Node("6", "6", 6);
            oldTree.children[0] = left;
            oldTree.children[1] = mid;
            oldTree.children[2] = right;

            newT.keys[0] = new Node("6", "6", 6);
            leftNew.keys[0] = new Node("5", "5", 5);
            rightNew.keys[0] = new Node("7", "7", 7);
            newT.children[0] = leftNew;
            newT.children[1] = rightNew;
            

            splitTree(oldTree, newT, 2);*/
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            // Sets the order of the tree
            order = Integer.parseInt(reader.readLine());

            Node node;

            // Reads the files
            int numberOfFiles = Integer.parseInt(reader.readLine());
            for (int i = 0; i < numberOfFiles; i++) {

                node = makeNode(reader.readLine());
                tree = insertNode(tree, node);
            }

            // Reads the commands
            int numberOfCommands = Integer.parseInt(reader.readLine());
            for (int i = 0; i < numberOfCommands; i++) {

                String line = reader.readLine();

                String command = line.substring(0, 6);

                switch (command) {

                    case "SELECT":

                        String hash = line.substring(7);
                        node = selectNode(tree, hash);

                        System.out.println("[" + hash + "]");
                        if (node != null)
                            System.out.println(node.name + ":size=" + node.size + ",hash=" + node.key);
                        else
                            System.out.println("-");

                        break;

                    case "INSERT":

                        node = makeNode(line.substring(7));
                        tree = insertNode(tree, node);

                        break;

                    default:
                        throw new RuntimeException("Invalid command");
                }

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}