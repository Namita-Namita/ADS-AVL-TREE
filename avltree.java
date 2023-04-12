import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Class for creating AVL tree with info, left child and right child
class AvlNode {
	int info, height;
	AvlNode left, right;

	// Default Constructor to initialize the object
	public AvlNode() {
		left = null;
		right = null;
		info = 0;
		height = 0;
	}

	// Parameterized constructor for setting info
	public AvlNode(int d) {
		left = null;
		right = null;
		info = d;
		height = 1;
	}

}
// avltree class for creating, inserting, deleting, and searching elements in the avl tree.
class avltree {

	AvlNode root;
	/**
	 * Function to perform insertion in the AVL tree.
	 * 
	 * @param currRoot Root of the tree.
	 * @param info      Element to insert in the tree.
	 * @return New root of the tree after info is inserted.
	 */
	AvlNode insertingNode(AvlNode currRoot, int info) {

		// Regular BST insertion
		if (currRoot == null)
			return (new AvlNode(info));

		if (info < currRoot.info)
			currRoot.left = insertingNode(currRoot.left, info);
		else if (info > currRoot.info)
			currRoot.right = insertingNode(currRoot.right, info);
		else // Duplicate keys not allowed
			return currRoot;

		// Update the height of the tree if required
		currRoot.height = 1 + findMaximum(findHeightOfTree(currRoot.left), findHeightOfTree(currRoot.right));
		// Find the balance Factor for particular node to check for imbalance
		int balanceFactor = findBalanceFactor(currRoot);

		// If the tree is imbalance, RR rotation case
		if (balanceFactor > 1 && info < currRoot.left.info)
			return rightRotate(currRoot);

		// LL rotation Case
		if (balanceFactor < -1 && info > currRoot.right.info)
			return leftRotate(currRoot);

		// LR rotation Case
		if (balanceFactor > 1 && info > currRoot.left.info) {
			currRoot.left = leftRotate(currRoot.left);
			return rightRotate(currRoot);
		}

		// RL rotation Case
		if (balanceFactor < -1 && info < currRoot.right.info) {
			currRoot.right = rightRotate(currRoot.right);
			return leftRotate(currRoot);
		}

		// return the (unchanged) node pointer
		return currRoot;
	}
	/**
	 * Function to delete a node from the AVL tree. Deletion is not done if the key is not found.
	 * 
	 * @param currRoot root of the tree.
	 * @param info      the item to delete from the tree.
	 * @return New root of the tree after info is deleted from the tree.
	 */
	AvlNode deletingNode(AvlNode currRoot, int info) {
		// Regular BST deletion
		if (currRoot == null)
			return currRoot;

		// If the key to be deleted is smaller than the root's key, then it lies in left
		// subtree
		if (info < currRoot.info)
			currRoot.left = deletingNode(currRoot.left, info);

		// If the key to be deleted is greater than the root's key, then it lies in
		// right subtree
		else if (info > currRoot.info)
			currRoot.right = deletingNode(currRoot.right, info);

		// if key is same as root's key, then this is the node
		// to be deleted
		else {

			// node with only one child or no child
			if ((currRoot.left == null) || (currRoot.right == null)) {
				AvlNode temp = null;
				if (temp == currRoot.left)
					temp = currRoot.right;
				else
					temp = currRoot.left;

				// No child case
				if (temp == null) {
					temp = currRoot;
					currRoot = null;
				} else // One child case
					currRoot = temp; // Copy the contents of
				// the non-empty child
			} else {
				AvlNode temp = findMinValueNode(currRoot.right);

				// Copy the in-order successor's data to this node
				currRoot.info = temp.info;

				// Delete the in-order successor
				currRoot.right = deletingNode(currRoot.right, temp.info);
			}
		}

		// If the tree had only one node then return
		if (currRoot == null)
			return currRoot;

		// update the height of the tree
		currRoot.height = findMaximum(findHeightOfTree(currRoot.left), findHeightOfTree(currRoot.right)) + 1;

		// Find the balance factor at particular node to check for imbalance
		int balanceFactor = findBalanceFactor(currRoot);
		if (balanceFactor > 1 && findBalanceFactor(currRoot.left) >= 0)
			return rightRotate(currRoot);

		// LR rotation Case
		if (balanceFactor > 1 && findBalanceFactor(currRoot.left) < 0) {
			currRoot.left = leftRotate(currRoot.left);
			return rightRotate(currRoot);
		}

		// RR rotation Case
		if (balanceFactor < -1 && findBalanceFactor(currRoot.right) <= 0)
			return leftRotate(currRoot);

		// RL rotation Case
		if (balanceFactor < -1 && findBalanceFactor(currRoot.right) > 0) {
			currRoot.right = rightRotate(currRoot.right);
			return leftRotate(currRoot);
		}

		return currRoot;
	}
	/**
	 * Function to get the balance factor of the AVL tree.
	 * 
	 * @param avlNode the node for which we need to find the balance factor.
	 * @return integer thats the balance factor with respect to avlNode.
	 */
	int findBalanceFactor(AvlNode avlNode) {
		if (avlNode == null)
			return 0;

		return findHeightOfTree(avlNode.left) - findHeightOfTree(avlNode.right);
	}
	/**
	 * Function to perform right rotation on the AVL tree.
	 * 
	 * @param avlCurrNode Root of the tree.
	 * @return New root of the tree after performing right rotate on the tree.
	 */
	// Function to right rotate subtree
	AvlNode rightRotate(AvlNode avlCurrNode) {
		AvlNode leftChild = avlCurrNode.left;
		AvlNode rightChild = leftChild.right;

		// Rotate
		leftChild.right = avlCurrNode;
		avlCurrNode.left = rightChild;

		// Update heights
		avlCurrNode.height = findMaximum(findHeightOfTree(avlCurrNode.left), findHeightOfTree(avlCurrNode.right)) + 1;
		leftChild.height = findMaximum(findHeightOfTree(leftChild.left), findHeightOfTree(leftChild.right)) + 1;

		// Return new root
		return leftChild;
	}
	/**
	 * Function to perform left rotation on the AVL tree.
	 * 
	 * @param avlCurrNode Root of the tree.
	 * @return New root of the tree after performing left rotate on the tree.
	 */
	// Function to left rotate subtree
	AvlNode leftRotate(AvlNode avlCurrNode) {
		AvlNode rightChild = avlCurrNode.right;
		AvlNode leftChild = rightChild.left;

		// Rotate
		rightChild.left = avlCurrNode;
		avlCurrNode.right = leftChild;

		// Update heights
		avlCurrNode.height = findMaximum(findHeightOfTree(avlCurrNode.left), findHeightOfTree(avlCurrNode.right)) + 1;
		rightChild.height = findMaximum(findHeightOfTree(rightChild.left), findHeightOfTree(rightChild.right)) + 1;

		// Return new root
		return rightChild;
	}
	/**
	 * Function to search for an element within the tree.
	 * 
	 * @param avlNode Root of the tree.
	 * @param info      Element to search in the AVL tree.
	 * @return node if the element to search for is found else return null.
	 */
	// Function to search for a node in the AVL Tree
	AvlNode findNode(AvlNode avlNode, int info) {
		AvlNode current = avlNode;
		while (current != null) {
			if (current.info == info) {
				break;
			}
			current = current.info < info ? current.right : current.left;
		}
		return current;
	}

	// List for storing values found within the range
	List<Integer> nodeList = new ArrayList<>();
	/**
	 * Function to search for all the element within the range in the tree.
	 * 
	 * @param currRoot Root of the tree.
	 * @param r1 and r2 is the range in which the elements needs to be searched. 
	 * @return list of elements found within the range else returns null.
	 */
	List<Integer> findNodeInRange(AvlNode currRoot, int r1, int r2){
		// Base case, if the root is Null
		if (currRoot == null) {
			return null;
		}

		if (r1 < currRoot.info) {
			findNodeInRange(currRoot.left, r1, r2);
		}

		// if root's data lies in range, then prints root's data
		if (r1 <= currRoot.info && r2 >= currRoot.info) {
			// adding values found within the range in list
			nodeList.add(currRoot.info);
		}
		findNodeInRange(currRoot.right, r1, r2);
		return nodeList;

	}
	/**
	 * Function to find the smallest item in a subtree.
	 * 
	 * @param avlNode root of the tree.
	 * @return node containing the smallest item.
	 */
	// Function to search for the nodes in the tree with minimum value
	AvlNode findMinValueNode(AvlNode avlNode) {
		AvlNode current = avlNode;
		// loop down to find the leftmost leaf
		while (current.left != null)
			current = current.left;

		return current;
	}
	/**
	 * Function to get the height of the AVL tree.
	 * 
	 * @param currRoot root of the tree.
	 * @return integer as the height of the tree from currRoot.
	 */
	// Function to get the height of the tree
	int findHeightOfTree(AvlNode currRoot) {
		if (currRoot == null)
			return 0;

		return currRoot.height;
	}
	/**
	 * Function to get the maximum of two integers.
	 * 
	 * @param x and y integers.
	 * @return integer thats maximum among x and y.
	 */
	// Function to get maximum of two integers
	int findMaximum(int x, int y) {
		return (x > y) ? x : y;
	}
	/**
	 * Main function that takes file path as the input, reads the file and performs
	 * operations mentioned in the input file on AVL tree and creates the ouput.txt file and prints the output in
	 * the file.
	 * 
	 * @param args[] that consists the input file name/path.
	 */
	public static void main(String[] args) throws IOException {
		avltree avlTree = null;
		// Takes the path of input file as command line argument and passes to File()
		// method to read.
		File file = new File(args[0]);
		Scanner sc = new Scanner(file);
		// Created output.txt in the project directory with the project output
		FileWriter out = new FileWriter("output.txt", true);
		// Reads the input file line by line till EOF is reached
		while (sc.hasNextLine()) {
			String s = sc.nextLine();
			if (s.toUpperCase().contains("INITIALIZE")) {
				avlTree = new avltree();
				out.write("\n");
			} else if (s.toUpperCase().contains("INSERT")) {
				// Extract the integer value to be inserted in the tree
				int val = Integer.parseInt(s.replaceAll("[^0-9]", ""));
				avlTree.root = avlTree.insertingNode(avlTree.root, val);
			} else if (s.toUpperCase().contains("DELETE")) {
				// Extract the integer value to be inserted in the tree
				int val = Integer.parseInt(s.replaceAll("[^0-9]", ""));
				avlTree.root = avlTree.deletingNode(avlTree.root, val);
			} else if (s.toUpperCase().contains("SEARCH")) {
				// Case 1: to search for all the values lying between the given range k1 - k2
				if (s.contains(",")) {
					String t[] = s.split(",");
					// Extract the value of the range
					int val1 = Integer.parseInt(t[0].replaceAll("[^0-9]", ""));
					int val2 = Integer.parseInt(t[1].replaceAll("[^0-9]", ""));
					// List of values found within the range in the tree
					List<Integer> nodeKeys1 = avlTree.findNodeInRange(avlTree.root, val1, val2);
					// If no value if found print NULL in the output file
					if (nodeKeys1 == null) {
						out.write("NULL");
						out.write("\n");
					} else {
						String str = nodeKeys1.toString();
						String str2 = str.substring(1, str.length() - 1).replaceAll(", ", ",");
						out.write(str2);
						out.write("\n");
					}
					// Case 2: Search for a key in the tree
				} else {
					int val = Integer.parseInt(s.replaceAll("[^0-9]", ""));
					AvlNode element = avlTree.findNode(avlTree.root, val);
					if (element == null) {
						out.write("NULL");
						out.write("\n");
					} else {
						String ele= Integer.toString(element.info);
						out.write(ele);
						out.write("\n");
					}
				}

			}
		}
		out.close();
		sc.close();

	}

}
