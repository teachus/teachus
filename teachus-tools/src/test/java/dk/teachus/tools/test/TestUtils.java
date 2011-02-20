package dk.teachus.tools.test;

import java.io.File;

public class TestUtils {
	
	public static void printFolder(File folder, int level) {
		System.out.println(createIndent(level)+"["+folder.getName()+"]");
		File[] children = folder.listFiles();
		for (File child : children) {
			if (child.isDirectory()) {
				printFolder(child, level+1);
			} else {
				System.out.println(createIndent(level+1)+child.getName());
			}
		}
	}
	
	private static String createIndent(int level) {
		// Create level indent
		String indent = "";
		for (int i = 0; i < level; i++) {
			indent += "  ";
		}
		indent += "+ ";
		return indent;
	}

}
