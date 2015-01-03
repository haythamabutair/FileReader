import java.awt.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;

public class CleanUp {
//	public static void main(String[] args) {
//		try {
//			CleanUp c = new CleanUp("../rawfileFINAL.txt");
//
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}

	public CleanUp(String url) throws IOException {
		cleanUp(url);

	}

	/*
	 * public static void main(String[] args) { try {
	 * cleanUp("/Users/haysam_121/Desktop/haytham2.txt"); } catch (IOException
	 * e) { // TODO Auto-generated catch block e.printStackTrace(); } }
	 */

	public static void cleanUp(String string) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(string));
		String inputLine;
		StringBuilder a = new StringBuilder();
		ArrayList<String> toClean = new ArrayList<String>();
		boolean write = false;
		int count = 0;
		while ((inputLine = in.readLine()) != null) {
			if (inputLine.toLowerCase().contains(("notice of sale"))) {
				char temp = inputLine.charAt(inputLine.length() - 2);
				boolean valid = inputLine.charAt(inputLine.length() - 2) >= '0'
						&& inputLine.charAt(inputLine.length() - 2) <= '9' ? true
						: false;
				if (inputLine.toLowerCase().contains("book")
						|| inputLine.toLowerCase().contains("page") || valid) {
					write = true;
					inputLine += ("\r\n end-of-legal-ad \r\n");
					toClean.add(inputLine.toString());
					a = new StringBuilder();
					count++;
					valid = false;
				}

			}
			if (write) {
				a.append(inputLine);
				write = false;
			}
		}
		in.close();
		writeStringToCleanFile(toClean, new File(
				"cleanedFINAL.txt"));
	}

	public static void writeStringToCleanFile(ArrayList<String> x, File aFile)
			throws IOException {
		Writer output = new BufferedWriter(new FileWriter(aFile));
		int counter = 0;
		try {
			// FileWriter always assumes default encoding is OK!
			for (String cleaned : x) {
				cleaned = cleaned.replaceAll("<br ", "");
				cleaned = cleaned.replaceAll("/>", "");
				cleaned = cleaned.replaceAll(";br", "");
				cleaned = cleaned.replaceAll("/&gt;", "");
				cleaned = cleaned.replaceAll("&lt;", "");
				cleaned = cleaned.replaceAll("&lt", "");
				cleaned = cleaned.replaceAll("\t", "");
				if (cleaned.length() > 500){
					output.write(cleaned);
					counter++;
				}
			}
			output.write("/n count: " + counter + "/n"); 
		}

		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		finally {
			output.close();
		}

	}
}
