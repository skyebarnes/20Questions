package twentyQuestions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * A model for the game of 20 questions
 *
 * @author Rick Mercer
 */
public class GameTree {

	class Node {
		public String data;
		public Node left, right;

		public Node(String data) {
			this.data = data;
			this.left = null;
			this.right = null;
		}

		public Node(String data, Node left, Node right) {
			this.data = data;
			this.left = left;
			this.right = right;
		}
	}

	Node root;
	Node current;
	String filename;

	/**
	 * Constructor needed to create the game.
	 *
	 * @param fileName this is the name of the file we need to import the game
	 *                 questions and answers from.
	 */
	public GameTree(String fileName) {
		Scanner scr;
		filename = fileName;
		try {
			scr = new Scanner(new File(fileName));
			root = CreateTree(scr, root);
			current = root;

		} catch (Exception e) {
			System.out.println(e);
		}

	}

	private Node CreateTree(Scanner scr, Node node) {
		if (!scr.hasNext()) {
			return node;
		}
		
		String data = scr.nextLine().trim();
		node = new Node(data);
		
		if (!data.endsWith("?")) {
			return node;
		}
	

		node.left = CreateTree(scr, node.left);
		node.right = CreateTree(scr, node.right);
		
		return node;

	}

	/*
	 * 
	 * Add a new question and answer to the currentNode. If the current node has the
	 * answer chicken, theGame.add("Does it swim?", "goose"); should change that
	 * node like this:
	 *
	 * -----------Feathers?-----------------Feathers?------
	 * -------------/----\------------------/-------\------
	 * ------- chicken horse-----Does it swim?-----horse--
	 * -----------------------------/------\---------------
	 * --------------------------goose--chicken-----------
	 */
	/**
	 * @param newQ The question to add where the old answer was.
	 * @param newA The new Yes answer for the new question.
	 */
	public void add(String newQ, String newA) {
		current.left = new Node(newA);
		current.right = new Node(current.data);
		current.data = newQ;
	}

	/**
	 * True if getCurrent() returns an answer rather than a question.
	 *
	 * @return False if the current node is an internal node rather than an answer
	 *         at a leaf.
	 */
	public boolean foundAnswer() {
		return !current.data.endsWith("?"); 
	}

	/**
	 * Return the data for the current node, which could be a question or an answer.
	 * Current will change based on the users progress through the game.
	 *
	 * @return The current question or answer.
	 */
	public String getCurrent() {
		return current.data; 
	}

	/**
	 * Ask the game to update the current node by going left for Choice.yes or right
	 * for Choice.no Example code: theGame.playerSelected(Choice.Yes);
	 *
	 * @param yesOrNo
	 */
	public void playerSelected(Choice yesOrNo) {

		if (yesOrNo == Choice.Yes) {
			current = current.left;
		} else {
			current = current.right;
		}

	}

	/**
	 * Begin a game at the root of the tree. getCurrent should return the question
	 * at the root of this GameTree.
	 */
	public void reStart() {
		current = root;
	}

	@Override
	public String toString() {
		return toStringHelper("", root, "");
	}

	private String toStringHelper(String text, Node node, String level) {
		if(node == null) {
			return "";
		}
		if(node.right == null || node.left == null) {
			return level + node.data + "\n";
		}
		
		return toStringHelper(text, node.right, level + "- ") + (level + node.data + "\n") + toStringHelper(text, node.left, level + "- ");

	}

	/**
	 * Overwrite the old file for this gameTree with the current state that may have
	 * new questions added since the game started.
	 *
	 */
	public void saveGame() {
		PrintWriter diskFile = null;
		try {
			diskFile = new PrintWriter(new File(filename));
		} catch (IOException e ) {
			System.out.println("Could not create file");
		}
		saveGame(diskFile, root);
		diskFile.close();
	}
	
	private void saveGame(PrintWriter diskFile, Node node) {
		if(node == null) {
			return;
		}
		
		diskFile.println(node.data);
		
		saveGame(diskFile, node.left);
		saveGame(diskFile, node.right);
	}
	
	
}


