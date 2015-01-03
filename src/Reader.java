import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.text.html.HTMLDocument.Iterator;

public class Reader {
	static ArrayList<Integer> list = new ArrayList<Integer>();
	static ArrayList<String> urls = new ArrayList<String>();
	static String dateStart;// = "2014-01-01";
	static String dateEnd;// = "2014-01-30";

	public static void main(String args[]) throws FileNotFoundException {

		String[] inputs = args[0].split(" ");
		System.out.println(args[0]);
		if (inputs[0].toLowerCase().equals("help"))
			System.out
					.println("format: startdate first then end date. \n"
							+ "ex: 2014-01-01 2014-01-31 \n"
							+ "please follow that format! start date then end date! exactly as shown above");
		else {
			dateStart = inputs[0].toString();
			dateEnd = inputs[1].toString();

			for (int i = 0; i < county.length; i++) {
				String[] links = readDoc(part1, part2, county[i]);
				try {
					String countyFile =county[i] + "FINAL.txt";
					writeToFile(links, new File(countyFile));
					writeSource(new File("rawfileFINAL.txt"));
					CleanUp clean = new CleanUp("rawfileFINAL.txt");

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	static String part1 = "http://www.georgiapublicnotice.com/pages/results_content/push?per_page=50&x_page=";
	static String part2 = "&rel=prev+start&class=&search_content[category]=gpn11&search_content[phrase_match]=&search_content[min_date]="
			+ dateStart
			+ "&search_content[max_date]="
			+ dateEnd
			+ "&search_content[page_label]=results_content&search_content[string]=search_category_gpn11+search_county_";

	static String[] county = { "Gwinnett" };
	//

	static ArrayList<String> ret = new ArrayList<String>();
	static int pages;

	public static String[] readDoc(String url1, String url2, String county)
			throws FileNotFoundException {
		String last = county.toLowerCase() + "&search_content[county]="
				+ county + "+County";
		String output = null;
		try {
			output = getUrlSource(url1 + 1 + url2 + last);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		pages = getNumberOfPages(output);

		for (int j = 1; j < pages; j++) {

			output = null;
			try {
				output = getUrlSource(url1 + j + url2 + last);
			} catch (IOException e) {
				e.printStackTrace();
			}

			String[] raw = output.split(" ");
			String link = "/view/";

			for (int i = 0; i < raw.length; i++) {
				if (raw[i].contains(link)) {

					String temp = raw[i];
					if (temp.toLowerCase().contains("intent"))
						continue;
					temp = temp.replace("href=", "");
					temp = temp.replaceAll("\"", "");
					temp = temp.replace(">", "");
					ret.add("http://www.georgiapublicnotice.com" + temp);
				}
			}
		}

		String[] returning = new String[ret.size()];
		returning = ret.toArray(returning);

		return returning;
	}

	private static String getUrlSource(String raw) throws IOException {

		HttpURLConnection connection = null;
		try {
			URL oracle = new URL(raw);
			URLConnection yc = oracle.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					yc.getInputStream()));
			String inputLine;
			StringBuilder a = new StringBuilder();
			;
			while ((inputLine = in.readLine()) != null)
				a.append(inputLine + "\n");
			in.close();
			return a.toString();
		} catch (Exception e) {
			return "";
		} finally {
		}

		/*
		 * URL yahoo = new URL(url); URLConnection yc = yahoo.openConnection();
		 * // BufferedReader in = new BufferedReader(new InputStreamReader( //
		 * yc.getInputStream(), "UTF-8")); BufferedReader in = new
		 * BufferedReader(new InputStreamReader(yc.getInputStream())); String
		 * inputLine; StringBuilder a = new StringBuilder(); while ((inputLine =
		 * in.readLine()) != null) a.append(inputLine); in.close();
		 */

	}

	static int curr = 0;

	private static int getNumberOfPages(String x) {
		if (x == null)
			return 0;
		String[] parse = x.split(" ");
		int returning = 0;
		for (int i = 0; i < parse.length; i++) {
			if (parse[i].toLowerCase().contains("x_page")) {
				String[] splitting = parse[i].split("x_page=");
				int k = 1;
				while (isInteger(splitting[1].substring(0, k))) {
					k++;
				}
				curr = Integer.parseInt(splitting[1].substring(0, k - 1));
				if (returning < curr)
					returning = curr;
			}

		}
		return returning;
	}

	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		// only got here if we didn't return false
		return true;
	}

	public static int getInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return 0;
		}
		// only got here if we didn't return false
		return Integer.parseInt((s));
	}

	public static int getNumber(String x) {
		String[] parse = x.split("full_story/");
		int returning = 0;
		for (int i = 0; i < parse.length; i++) {
			if (parse[1].toLowerCase().contains("article")) {
				String[] splitting = parse[1].split("article");
				int k = 1;
				while (isInteger(splitting[0].substring(0, k))) {
					k++;
				}
				curr = Integer.parseInt(splitting[0].substring(0, k - 1));
				if (returning < curr)
					returning = curr;
			}

		}
		return returning;
	}

	public static void writeSource(File path) throws IOException {
		// BufferedReader in = new BufferedReader(new FileReader(path));
		Writer output = new BufferedWriter(new FileWriter(path));
		int count = 0;
		for (String x : urls) {

			String raw = getUrlSource(x);
			output.write(raw + "\n\r");
			count++;
		}
		output.close();
		// CleanUp.cleanUp(path.toString());
	}

	public static void writeToFile(String[] x, File aFile) throws IOException {
		Writer output = new BufferedWriter(new FileWriter(aFile));

		try {
			// FileWriter always assumes default encoding is OK!
			for (int i = 0; i < x.length; i++)
				if (!list.contains(getNumber(x[i]))) {
					output.write(x[i] + "\n");
					urls.add(x[i]);
					list.add(getNumber(x[i]));
				}
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
