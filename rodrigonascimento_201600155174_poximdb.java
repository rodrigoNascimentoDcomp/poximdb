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
     * Shifts the branches of the tree to the right
     * from startIndex onwards. The last position in the array 
     * should be empty.
     * 
     * @param tree          Tree that will have it's branches shifted.
     * @param startIndex    First position of the array that will be shifted.
     * @return              Tree with an empty position.
     */
    public static Tree shiftRight(Tree tree, int startIndex) {

        for (int i = tree.order - 2; i > startIndex; i--) {

            tree.keys[i] = tree.keys[i - 1];
            tree.children[i + 1] = tree.children[i];
        }

        tree.keys[startIndex] = null;
        tree.children[startIndex + 1] = null;

        return tree;
    }

    /**
     * Adds a new branch to a tree and splits it.
     * 
     * @param primaryTree         Original tree.
     * @param newBranch        Branch to be added.
     * @param newBranchIndex   Position in witch the new tree will be added.
     * @return                  Split tree.
     */
    public static Tree splitTree(Tree primaryTree, Tree newBranch, int newBranchIndex) {

        Tree topTree = new Tree(primaryTree.order);
        Tree leftTree = new Tree(primaryTree.order);
        Tree rightTree = new Tree(primaryTree.order);

        // Gets the middle index of the original tree
        int midIndex;
        if (primaryTree.order % 2 == 0) {
            midIndex = (int) (primaryTree.order / 2) - 1;
        } else {
            midIndex = (int) primaryTree.order / 2;
        }

        if (newBranchIndex == midIndex) {

            int j = 0;
            for (int i = 0; i < primaryTree.order; i++) {

                if (i < newBranchIndex) {

                    leftTree.keys[i] = primaryTree.keys[i];
                    leftTree.children[i] = primaryTree.children[i];

                } else if (i == newBranchIndex) {

                    topTree.keys[0] = newBranch.keys[0];
                    leftTree.children[i] = newBranch.children[0];
                    rightTree.children[0] = newBranch.children[1];

                } else {

                    rightTree.keys[j] = primaryTree.keys[i - 1];
                    rightTree.children[++j] = primaryTree.children[i];
                }
            }
            
        } else if (newBranchIndex < midIndex) {

            int j = 0;
            for (int i = 0; i < primaryTree.order; i++) {

                if (i < newBranchIndex) {

                    leftTree.keys[i] = primaryTree.keys[i];
                    leftTree.children[i] = primaryTree.children[i];

                } else if (i == newBranchIndex) {

                    leftTree.keys[i] = newBranch.keys[0];
                    leftTree.children[i] = newBranch.children[0];
                    leftTree.children[i + 1] = newBranch.children[1];

                } else {

                    if (i < midIndex) {

                        leftTree.keys[i] = primaryTree.keys[i - 1];
                        leftTree.children[i + 1] = primaryTree.children[i];

                    } else if (i == midIndex) {

                        topTree.keys[0] = primaryTree.keys[i - 1];
                        rightTree.children[0] = primaryTree.children[i];

                    } else {

                        rightTree.keys[j] = primaryTree.keys[i - 1];
                        rightTree.children[++j] = primaryTree.children[i];
                    }
                }
            }
            
        } else {

            int j = 0;
            for (int i = 0 ; i < primaryTree.order - 1; i++) {

                if (i < midIndex) {

                    leftTree.keys[i] = primaryTree.keys[i];
                    leftTree.children[i] = primaryTree.children[i];

                } else if (i == midIndex) {

                    leftTree.children[i] = primaryTree.children[i];
                    topTree.keys[0] = primaryTree.keys[i];

                } else {

                    if (i < newBranchIndex) {

                        rightTree.keys[j] = primaryTree.keys[i];
                        rightTree.children[j] = primaryTree.children[i];
                        j++;

                    } else if (i == newBranchIndex) {

                        rightTree.keys[j] = newBranch.keys[0];
                        rightTree.children[j] = newBranch.children[0];
                        rightTree.children[++j] = newBranch.children[1];

                    } else {

                        rightTree.keys[j] = primaryTree.keys[i];
                        rightTree.children[++j] = primaryTree.children[i + 1];

                    }
                }
            }
        }

        topTree.children[0] = leftTree;
        topTree.children[1] = rightTree;

        return topTree;
    }

    /**
     * Adds a node to a leaf and splits it.
     * 
     * @param tree      Original leaf.
     * @param newNode   Node to be added.
     * @return          Split leaf.
     */
    public static Tree splitLeaf(Tree tree, Node newNode) {

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
                break;
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

            tree = insertToLeaf(tree, newNode);

        } else {
            // Go down

            for (int i = 0; i < tree.order - 1; i++) {

                Tree auxTree = new Tree(tree.order);

                if (tree.keys[i] == null || newNode.key.compareTo(tree.keys[i].key) < 0) {

                    auxTree = insertNode(tree.children[i], newNode);    // Go left

                    // If a tree was returned, it was already added from inside
                    // the method to the tree passed as argument.

                    tree = insertToTree(tree, auxTree, i);

                    break;

                } else if (i == tree.order - 2) {
                    
                    auxTree = insertNode(tree.children[i + 1], newNode);    // Go right

                    tree = insertToTree(tree, auxTree, i);

                    break;
                }
            }            
        }

        return tree;
    }

    /**
     * Inserts a new node into a leaf.
     * 
     * @param tree      Leaf that will have the node added to.
     * @param newNode   Node to be added to the tree.
     * @return          Leaf with the added node.
     */
    public static Tree insertToLeaf(Tree tree, Node newNode) {

        // If the keys array is not full, add to it
        if (tree.keys[tree.order - 2] == null) {

            for (int i = 0; i < tree.order - 1; i++) {

                // Adds to the first empty node
                if (tree.keys[i] == null) {

                    tree.keys[i] = newNode;
                    break;

                // Swaps the new node with the current node
                // and ads the current node to the tree again
                } else if (newNode.key.compareTo(tree.keys[i].key) < 0) {
                    
                    Node auxNode = tree.keys[i];
                    tree.keys[i] = newNode;
                    newNode = auxNode;

                }
            }

        } else {
            // Creates a new Tree
            tree = splitLeaf(tree, newNode);
        }

        return tree;
    }

    /**
     * Inserts a new branch into a tree.
     * 
     * @param tree      Original tree.
     * @param branch    Branch to be added to the tree.
     * @param index     Index on witch the branch will be added.
     * @return          Tree with the added branch.
     */
    public static Tree insertToTree(Tree tree, Tree branch, int index) {

        // A tree was returned
        if (branch.children[0] != null) {

            // If the returned tree wasn't already added to the tree
            if (branch != tree.children[index] && branch != tree.children[index + 1]) {

                if (tree.keys[index] == null) {

                    tree.keys[index] = branch.keys[0];
                    tree.children[index] = branch.children[0];
                    tree.children[index + 1] = branch.children[1];

                // If the keys array is not full
                } else if (tree.keys[tree.order - 2] == null) {

                    tree = shiftRight(tree, index);
                    tree.keys[index] = branch.keys[0];
                    tree.children[index] = branch.children[0];
                    tree.children[index + 1] = branch.children[1];
                    
                } else {
                    tree = splitTree(tree, branch, index);
                }
            }
        }

        return tree;
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
     * Returns the tree where the key is.
     * @param tree  Tree to be searched.
     * @param key   Desired key.
     * @return      Tree that has the key searched.
     */
    public static Tree selectTree(Tree tree, String key) {

        if (tree == null)
            return null;

        for (int i = 0; i < tree.order - 1; i++) {

            if (tree.keys[i] == null || key.compareTo(tree.keys[i].key) < 0) {

                return selectTree(tree.children[i], key);

            } else if (key.compareTo(tree.keys[i].key) == 0) {

                return tree;

            } else if (tree.order - 2 == i) {

                return selectTree(tree.children[i + 1], key);
            }
        }

        return tree;
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
                        Tree foundTree = selectTree(tree, hash);

                        writeToFile(args[1], "[" + hash + "]\n");
                        if (foundTree == null) {
                            writeToFile(args[1], "-\n");
                            break;
                        } else {
                            for (int j = 0; j < foundTree.order - 1; j++) {
                                if (foundTree.keys[j] == null) {
                                    break;
                                } else {
                                    writeToFile(args[1], foundTree.keys[j].name + ":size=" + foundTree.keys[j].size + ",hash=" + foundTree.keys[j].key + "\n");
                                }
                            }
                        }

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