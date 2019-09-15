package Search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SearchCities {
	
	// representing the food prices
	private static double foodItem1;
	private static double foodItem2;
	
	// storing th name of the store 
	private static String store;
	
	//storing prices of items
	private static String item1 = "";
	private static String item2 = "";
	
	// this string holds the result which will be printed to the output file
	private static String results = new String();
	
	// helper method to get the name of the store
	public static String getStore() {
		return store;
	}

	// this method is used to get the name of a city, given it's number representation (using an enum in "CityValues.java")
	public static String getName(int value) {
		String s = "";
		for(CityValues i : CityValues.values()) {
			if (i.getValue() == value) {
				s = i.name();
			}
		}
		return s;
	}
	
	// this method is used tp get the longitude or latitude of a certain city
	private static double getCoord(String L, String city) {
		BufferedReader reader;
		double coord;
		
		try {
			reader = new BufferedReader(new FileReader("data/USCities.csv"));
			String line = reader.readLine();
			line = reader.readLine();
			String[] arrLine = line.split(",");
			
			//Reading all lines from connectedCities.txt
			while (line != null) {
				arrLine[3] = arrLine[3].replaceAll("[^a-zA-Z]", "");
				
				//returning longitude/latitude value if the city matches the given input
				if (arrLine[3].equals(city)) {
					if(L.equals("lon")) return Double.parseDouble(arrLine[5]);
					else if (L.equals("lat")) return Double.parseDouble(arrLine[4]);
				}
				
				line = reader.readLine();
				if(line != null) arrLine = line.split(",");		
			}
			reader.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		return 0.0;
	}
	
	// this method is used to find a restaurant that is within 0.5 degrees long/lat of the given city
	// the return value is a string consisting of the information of the restaurant
	public static String findCloseRest(double lon, double lat, char rest) {
		BufferedReader reader;
		String info;
		String file = "";
		if ((rest == 'M') || (rest == 'm')) {
			file = "data/mcdonalds.csv";
		}
		else if ((rest == 'w') || (rest == 'W')) {
			file = "data/wendys.csv";
		}
		else if ((rest == 'b') || (rest == 'B')) {
			file = "data/burgerking.csv";
		}
		
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = reader.readLine();
			line = reader.readLine();
			String[] arrLine = line.split(",");
			
			//Reading all lines from
			while (line != null) {
				
				// checking long/lat of the place 
				if((Math.abs(Double.parseDouble(arrLine[0]) - lon) < 0.5) && (Math.abs(Double.parseDouble(arrLine[1]) - lat) < 0.5)) {
					// returning the restaurant's info
					return line;
				}
				line = reader.readLine();
				if(line != null) arrLine = line.split(",");
				
			}
			reader.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return "no closest place";
		
	}
	
	// making a digraph based off of the connectedCities.txt (3.2)
	public static Digraph makeDiGraph() {
		BufferedReader reader;
		Digraph diGraph = new Digraph(33);
		
		try {
			reader = new BufferedReader(new FileReader("data/connectedCities.txt"));
			String line = reader.readLine();
			String[] arrLine = line.split(", ");
			
			//Reading all lines from connectedCities.txt
			while (line != null) {	
				//array of two strings consisting of first city and second city (direction)
				arrLine = line.split(", ");
				
				//Removing all characters except for capital and lower case letters
				arrLine[0] = arrLine[0].replaceAll("[^a-zA-Z]", "");
				arrLine[1] = arrLine[1].replaceAll("[^a-zA-Z]", "");

				//getting the value of each city from enum
				CityValues x0 = CityValues.valueOf(arrLine[0]);
				CityValues x1 = CityValues.valueOf(arrLine[1]);
				//turning value to an int
				int xx0 = x0.getValue();
				int xx1 = x1.getValue();
				
				//inserting the int representation of the two cities into the Digraph
				diGraph.addEdge(xx0, xx1);
				
				//reading next line
				line = reader.readLine();
			}
			reader.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		return diGraph;
	}
	
	
	//finding cheapest two items, since we can not use the same item in two consecutive stops
	public static void cheapFood() {
		
		double temp;
		
		BufferedReader reader;
		String[] info;
		
		try {
			reader = new BufferedReader(new FileReader("data/menu.csv"));
			String line = reader.readLine();
			line = reader.readLine();
			info = line.split(",");
			store = info[0];
			info[2] = info[2].replace("$", "");
			
			// initializing item1 to be the first item in the file
			foodItem1 = Double.parseDouble(info[2]);
			item1 = info[1];
			
			
			line = reader.readLine();
			info = line.split(",");
			info[2] = info[2].replace("$", "");
			
			if(Double.parseDouble(info[2]) > foodItem1) {
				foodItem2 = Double.parseDouble(info[2]);
				item2 = info[1];
			}
			else {
				foodItem2 = foodItem1;
				item2 = item1;
				foodItem1 = Double.parseDouble(info[2]);
				item1 = info[1];

			}
			
			//Reading all lines from connectedCities.txt
			while (line != null) {	
				
				line = reader.readLine();
				if (line != null) info = line.split(",");
				info[2] = info[2].replace("$", "");
				temp = Double.parseDouble(info[2]); 
				
				if (temp < foodItem2) {
					if(temp < foodItem1) {
						foodItem2 = foodItem1;
						item2 = item1;

						foodItem1 = temp;
						item1 = info[1];

						store = info[0];
					}
					else {
						foodItem2 = temp;
						item2 = info[1];

						store = info[0];
					}
				}
			}
			reader.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// making an edge weighted digraph based off of the connectedCities.txt (3.4)
	public static EdgeWeightedDigraph makeEWDiGraph() {
		BufferedReader reader;
		EdgeWeightedDigraph EWD = new EdgeWeightedDigraph(33);
		
		try {
			reader = new BufferedReader(new FileReader("data/connectedCities.txt"));
			String line = reader.readLine();
			String[] arrLine = line.split(", ");
			
			//Reading all lines from connectedCities.txt
			while (line != null) {	
				//array of two strings consisting of first city and second city (direction)
				arrLine = line.split(", ");
				
				//Removing all characters except for capital and lower case letters
				arrLine[0] = arrLine[0].replaceAll("[^a-zA-Z]", "");
				arrLine[1] = arrLine[1].replaceAll("[^a-zA-Z]", "");

				//getting the value of each city from enum
				CityValues x0 = CityValues.valueOf(arrLine[0]);
				CityValues x1 = CityValues.valueOf(arrLine[1]);
				//turning value to an int
				int xx0 = x0.getValue();
				int xx1 = x1.getValue();
				
				//inserting the int representation of the two cities into the Digraph
				EWD.addEdge(new DirectedEdge(xx0, xx1, 1.0));
				
				//reading next line
				line = reader.readLine();
			}
			reader.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		return EWD;
	}	
	
	public static String findDijkstra() {
		String ret = new String("");
		EdgeWeightedDigraph EWD = makeEWDiGraph();
		DijkstraSP DSP = new DijkstraSP(EWD, 0);
		
		// for loop to add all the node of the path found by dijkstra to the results
		int c = 0;
        for (DirectedEdge e : DSP.pathTo(32)) {
        	if (c == 0) ret = ret + getName(e.from());
        	ret = ret + (", " + getName(e.to()));
        	c++;
        }
        return ret;
                
	}

	// finding routes from Boston to Minneapolis (3.2)
	public static void findBtoM() {
		
		Digraph diGraph = makeDiGraph();
				
		// for loop to print every node in the result of the BFS
		BreadthFirstDirectedPaths bfs = new BreadthFirstDirectedPaths(diGraph, 0);
		results = results + "BFS: ";
		results = results + String.format("%s to %s:  ", getName(0), getName(32));
        for (int x : bfs.pathTo(32)) {
            if (x == 0) {
            	results = results + getName(x);
            }
            else{
            	results = results + String.format(", " + getName(x));
            }
        }
        
        results = results + "\n";
        
		// for loop to print every node in the result of the DFS
        DepthFirstDirectedPaths dfs = new DepthFirstDirectedPaths(diGraph, 0);        
        results = results + ("DFS: ");
		results = results + String.format("%s to %s:  ", getName(0), getName(32));
        for (int x : dfs.pathTo(32)) {
            if (x == 0) {
            	results = results + getName(x);
            }
            else{
            	results = results + String.format(", " + getName(x));
            }
        }
        
	}
	
	//Please run the main to write output to files
    public static void main(String[] args) throws IOException {  	
    	
    	// BFS & DFS from Boston to Mineapolis which add result to the `results` variale 
     	findBtoM();
    	
    	// running disjkstra to find shortes path
    	String shortestPath = findDijkstra();
    	String[] arrStr = shortestPath.split(", ");
        	
    	
		BufferedReader reader;
		
		results = results + "\n\n";
		
		// finding cheapest food items
    	cheapFood();
    	
    	String[] place;
    	String item;
    	double price;
    	
    	results = results + "City            " + "Item                         " + "Price       " + "Restaurants                " + "\n";
    	results = results + (arrStr[0] + "          NO STOP                      NO STOP	 NO STOP" + "\n");
    	
    	// for loop to print results to output text file
    	for(int i = 1; i < arrStr.length; i++) {
    		if(i % 2 != 0) {
    			item = item1;
    			price = foodItem1;
    		}
    		else {
    			item = item2;
    			price = foodItem2;
    		}
    		
    		place = (findCloseRest(getCoord("lon", arrStr[i].toUpperCase()), getCoord("lat", arrStr[i].toUpperCase()), store.charAt(0))).split(",");
       		results = results + String.format("%-15s %-28s $%-10f %s, %s\n", arrStr[i], item, price, place[2], place[3]);
    	
    	}
    	File f = new File("a2_out.txt");
    	FileWriter wr = new FileWriter(f);
    	wr.write(results);
    	wr.close();
    }
}
