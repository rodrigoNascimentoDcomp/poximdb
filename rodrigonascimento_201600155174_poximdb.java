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

                    if (tree.keys[i] == null) {
                        tree.keys[i] = newNode;
                        return tree;
                    } else if (newNode.key.compareTo(tree.keys[i].key) < 0
                                || newNode.key.compareTo(tree.keys[i].key) == 0) {
                        
                        Node auxNode = tree.keys[i];
                        tree.keys[i] = newNode;
                        return insertNode(tree, auxNode);

                    }
                }

                return tree;

            } else {
                // Create a new Tree

                Node[] nodeArray = new Node[tree.order];

                System.arraycopy(tree.keys, 0, nodeArray, 0, tree.keys.length);
                
                nodeArray = insertNode(nodeArray, newNode);
                int middleIndex = (int) nodeArray.length / 2;

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

        } else {
            // Go down

            for (int i = 0; i < tree.order - 1; i++) {

                // Go to the left children if the current key
                // is null or bigger or equal than the new one
                if (i == tree.order - 2) {
                    // Go right if it's the last key and the new node is bigger

                    tree.children[tree.order - 1] = insertNode(tree.children[tree.order - 1], newNode);
                } else {
                    tree.children[i] = insertNode(tree.children[i], newNode);
                }
            }

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

            } else if (tree.order - 1 == i) {
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