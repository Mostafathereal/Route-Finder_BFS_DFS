/**
 * 
 */
package Search;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Tango
 *
 */
public class TestSearchCities {
	
	Digraph dig;
	
	//Helper method to check dircetion of edges
	private boolean isIn(Integer v1, Integer v2) {
		for(Integer i: dig.adj(v1)) {
			if (i == v2) {
				return true;
			}
		}
		return false;
	}
	

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		dig = SearchCities.makeDiGraph();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
	
	/**
	 * Test method for {@link Search.SearchCities#makeDiGraph(int)}.
	 */
	@Test
	public void testAllRoutesDFS() {
        DepthFirstDirectedPaths dfs = new DepthFirstDirectedPaths(dig, 0);        
		BreadthFirstDirectedPaths bfs = new BreadthFirstDirectedPaths(dig, 0);        
        // Testing connectivity of every possible route
        for (int v = 0; v < dig.V(); v++) {
            assertEquals(bfs.hasPathTo(v), dfs.hasPathTo(v));
        }
	}
	
	/**
	 * Test method for {@link Search.SearchCities#makeGraph(int)}.
	 */
	@Test
	public void testStore() {
		SearchCities S = new SearchCities();
		S.cheapFood();
		assertEquals(S.getStore(), "McDonald’s");
	}
	
	/**
	 * Test method for {@link Search.SearchCities#makeDiGraph(int)}.
	 */
	@Test
	public void testmakeDiGraph() {
		
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
				
				// Asserting direction to be from xx0 to xx1
				assertTrue(isIn(xx0, xx1));
						
				//reading next line
				line = reader.readLine();
			}
			reader.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		assertEquals(dig.toString(), diGraph.toString());		
	}
	
	
	/**
	 * Test method for {@link Search.SearchCities#makeGraph(int)}.
	 */
	@Test
	public void testAllRoutesBFS() {
		
		BreadthFirstDirectedPaths bfs = new BreadthFirstDirectedPaths(dig, 0);
        DepthFirstDirectedPaths dfs = new DepthFirstDirectedPaths(dig, 0);        
                for (int v = 0; v < dig.V(); v++) {
            assertEquals(dfs.hasPathTo(v), bfs.hasPathTo(v));
        }
	}

}
