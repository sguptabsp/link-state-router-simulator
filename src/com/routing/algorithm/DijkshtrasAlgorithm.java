
package com.routing.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;

/***
 * This class TODO:This class defines the Dijkshtras Algorithm using priority queue.
 * And evaluate connection table and shortest path in the given topology.
 * @author Shruti Gupta
 *
 */
public class DijkshtrasAlgorithm {

	// This MAP data structure stores all given vertices in CreateTopologyMatrix in <Key, Value> pairs where Key is the name of the node/router and Value is the Nodes Object.
	// This is built using the list of Edges in the network.
	private final Map<String, Nodes> topology; 
	// This structure is used to store all the possible paths between two nodes.
	private Set<List<Nodes>> allPossiblePaths;
	// This structure is used to for storing all the shortest paths available in the network.
		private Set<List<Nodes>> allShortestPaths;
	
		/***
	 * This constructor builds a graph from a set of edges.
	 * @param edges	List of edges which we get after parsing the text file.
	 */
	public DijkshtrasAlgorithm(ArrayList<Link> edges) {
		topology = new HashMap<>(edges.size());

		// This block of code detects all the nodes from the set of links.
		for (Link e : edges) {
			if (!topology.containsKey(e.router1))
				topology.put(e.router1, new Nodes(e.router1));
			if (!topology.containsKey(e.router2))
				topology.put(e.router2, new Nodes(e.router2));
		}

		// This below for loop detects the neighboring vertices.
		for (Link e : edges) {
			// Adding neighboring nodes in both the directions i.e. if there is an edge 
			// between Node 1 and Node 2, then Node 1 is neighbor of Node 2 and Node 2 is the neighbor if Node 1.
			topology.get(e.router1).neighbours.put(topology.get(e.router2), e.link_weight);
			topology.get(e.router2).neighbours.put(topology.get(e.router1), e.link_weight);
		}
	}

	/***
	 * This method performs Dijkstras algorithm on the graph and builds the shortest path(s) from the source node to all other nodes in the graph.
	 * 
	 * @param sourceId	Name of the Node from which the shortest path(s) has to found
	 */
	public void do_DijkshtrasAlgo(String sourceId) {
		// Retrieve the Node object for the source node having name = sourceId.
		Nodes source = topology.get(sourceId);
		source.dist = 0;

		// Maintain a Priority Queue for the network topology traversal. Basically, it stores the list of nodes which have been visited.
		PriorityQueue<Nodes> vQueue = new PriorityQueue<Nodes>();
		vQueue.add(source);
		List<Nodes> prev = null;

		while (!vQueue.isEmpty()) {
			// Get the first node in the queue
			Nodes u = vQueue.poll();

			// Iterate through the neighbor nodes of the first node
			for (Map.Entry<Nodes, Integer> a : u.neighbours.entrySet()) {

				// Get the value of the Node (Vertex) from the <Vertex, Distance> pair
				Nodes nv = a.getKey();
				prev = nv.prev;
				
				// Get the distance of the vertex. This distance is the total distance of the path up to this current Vertex (Node)
				int weight = a.getValue();
				
				// Add the new node's distance and distance upto previous node
				int distanceViaU = u.dist + weight;

				// If the new distance is less than the neighboring node
				// TODO:
				if (distanceViaU < nv.dist) {
					vQueue.remove(nv);
					nv.dist = distanceViaU;
					nv.previous = u;
					vQueue.add(nv);
					prev = new ArrayList<Nodes>();
					prev.add(u);
					nv.prev = prev;
				} else if (distanceViaU == nv.dist) {
					if (prev != null)
						prev.add(u);
					else {
						prev = new ArrayList<Nodes>();
						prev.add(u);
						nv.prev = prev;
					}
				}
			}
		}
	}

	/***
	 * This method returns a list of of nodes (vertex) in a single path from the source node to the target node.
	 * Remember that source node is specified when calling the do_DijkshtrasAlgo(source) method.
	 * do_DijkshtrasAlgo(source) method must be called before calling this method.
	 * 
	 * @param target 	Name of the target node to which the path from the source node has to be returned
	 * @return			Returns a List of Vertex (node) in the path from source to target.
	 * 					The nodes in the list are in the order in which they are in the path.
	 */
	public List<Nodes> getShortestPathTo(String target) {
		// Initializing the path with an empty list
		List<Nodes> path = new ArrayList<Nodes>();
		// Iterate through the previous node of the target node
		for (Nodes vertex = topology.get(target); vertex != null; vertex = vertex.previous)
			path.add(vertex);
		
		// Reverse the List so that it appears from source to target and return back to the caller
		Collections.reverse(path);
		return path;
	}

	/***
	 * This method returns a Set containing all possible shortest paths from the source node to the target node.
	 * Each path is defined as a List of Vertex (Node) in this Set. 
	 * Remember that source node is specified when calling the do_DijkshtrasAlgo(source) method.
	 * do_DijkshtrasAlgo(source) method must be called before calling this method.
	 * 
	 * @param target 	Name of the target node to which the path from the source node has to be returned
	 * @return			Returns a Set containing a List of Vertex (node) in the path from source to target. (Each List represents a path)
	 * 					The nodes in the list are in the order in which they are in the path.
	 */
	public Set<List<Nodes>> getAllShortestPathsTo(String target) {
		// Initialize the set with an empty set with no list (path)
		allShortestPaths = new HashSet<List<Nodes>>();
		Nodes v = topology.get(target);
		
		// Call the method which gives us all the shortest paths.
		getShortestPath(new ArrayList<Nodes>(), v);
		
		// Return the Set
		return allShortestPaths;
	}

	/***
	 * This method recursively adds and print all the shortest paths available from source node to destination node.
	 * This is an internal (private) method defined to make use of recursion and is used by getAllShortestPathsTo(...) method.
	 * 
	 * @param shortestPath	A List containing a single shortest path from the source node to the target node.
	 * @param target		A target node (Vertex object) that specifies the paths to be evaluated from source node to this target node.
	 * @return				Returns a List containing nodes (Vertex object) in a single shortest path from source to target.
	 */
	private List<Nodes> getShortestPath(List<Nodes> shortestPath, Nodes target) {
		// This is used to get the target node's previous node value
		List<Nodes> prev = target.prev;
		
		if (prev == null) {
			// If there is no previous node, it means we have reached end of path. So we reverse the list so that nodes are in from source to target order.
			
			shortestPath.add(target);
			Collections.reverse(shortestPath);
			allShortestPaths.add(shortestPath);
		} else {
			// If previous node exists, then add this node to the single shortest path list
			List<Nodes> updatedPath = new ArrayList<Nodes>(shortestPath);
			updatedPath.add(target);
			
			// We then iterate through all the previous nodes. There might be more shortest path if there are multiple previous values. 
			for (Iterator<Nodes> iterator = prev.iterator(); iterator.hasNext();) {
				Nodes vertex = (Nodes) iterator.next();
				
				getShortestPath(updatedPath, vertex);
			}
		}
		
		return shortestPath;
	}

	/***
	 * This method returns a Set containing all the paths from the source node to the destination node.
	 * Each path is defined as a List of Vertex (Node) in this Set. 
	 * It is not required that you call route(source) method before calling this method.
	 * 
	 * @param source		Name of the source node whose paths you want to retrieve.
	 * @param destination	Name of the destination node whose paths you want to retrieve.
	 * @return				Returns a Set containing a List of Vertex (node) in the path from source to target. (Each List represents a path)
	 * 						The nodes in the list are in the order in which they are in the path.
	 */
	public Set<List<Nodes>> getAllPaths(String source, String destination) {
		allPossiblePaths = new HashSet<List<Nodes>>();
		LinkedList<Nodes> visited = new LinkedList<Nodes>();

		visited.add(topology.get(source));
		see_paths(visited, topology.get(destination));

		return allPossiblePaths;
	}

	/***
	 * This method performs a Breadth First Search algorithm on the graph to get all the paths from the source node to the destination node.
	 *
	 * @param visited	A LinkedList structure which stores a list of all the visited nodes.
	 * @param end		Vertex object of the destination node whose path you want to get.
	 *					Remember that source node will be already there in the <visited> list when this method is called for the first time.
	 */
	private void see_paths(LinkedList<Nodes> visited, Nodes end) {
		// Retrieve all the neighboring nodes of the last node in the visited nodes list.
		Map<Nodes, Integer> neighbours = visited.getLast().neighbours;

		// Examine all the adjacent (neighbor) nodes
		for (Map.Entry<Nodes, Integer> neighbor : neighbours.entrySet()) {
			Nodes node = (Nodes) neighbor.getKey();
			
			// If this node is already visited, then skip.
			if (visited.contains(node)) {
				continue;
			}
			
			if (node.equals(end)) {
				visited.add(node);
				
				allPossiblePaths.add(new LinkedList<Nodes>(visited));
				
				visited.removeLast();
				break;
			}
		}
		
		// After we have visited the neighboring nodes, we do a recursion call again
		for (Map.Entry<Nodes, Integer> neighbor : neighbours.entrySet()) {
			Nodes node = (Nodes) neighbor.getKey();
			if (visited.contains(node) || node.equals(end)) {
				continue;
			}
			visited.addLast(node);
			see_paths(visited, end);
			visited.removeLast();
		}
	}

	/*private void printPath(LinkedList<Vertex> visited) {
		for (Vertex node : visited) {
			System.out.print(node.name);
			System.out.print(" ");
		}
		System.out.println();
	}*/

	/*public void getNeighbours() {
		for (Vertex v : graph.values()) {
			System.out.printf("\n\n%s:", v.name);
			for (Map.Entry<Vertex, Integer> n : v.neighbours.entrySet()) {
				System.out.printf(" %s,", ((Vertex) n.getKey()).name);
			}
		}
	}*/
	
	public String printPaths(Set<List<Nodes>> paths) {
		StringBuffer text = new StringBuffer();
		
		text.append("PATHS AVAILABLE = ").append(paths.size()).append("\n\n");
		int i = 1;
		
		for (Iterator<List<Nodes>> iter = paths.iterator(); iter.hasNext(); i++) {
			List<Nodes> p = (List<Nodes>) iter.next();
			text.append("Path ").append(i).append(" : ");
			
			String path = "";
			for (Nodes v : p) {
				path += " -->  " + v.node_name;
			}
			path = path.replaceFirst(" --> ", "");
			text.append(path).append("\n\n");
		}
		
		return text.toString();
	}
	
	/***
	 * This method returns the distance value from the shortest paths from source node to destination node.
	 * @param paths	A Set containing shortest paths from source node to destination node.
	 * @return		Returns an integer value specifying the Shortest Path's Distance from source node to destination node.
	 */
	public int getDistance(Set<List<Nodes>> paths) {
		int distance = -1;
		for (Iterator<List<Nodes>> iter = paths.iterator(); iter.hasNext();) {
			List<Nodes> p = (List<Nodes>) iter.next();
			distance = p.get(p.size() - 1).dist;
			return distance;
		}
		return distance;
	}
	
	/***
	 * This method returns a Map structure which contains <Destination, Next Hop> from the source node to all the other nodes in the network.
	 * 
	 * @return	A TreeMap structure having <Destination, Next_Hop> pairs from the source node to all other other nodes in the network.
	 */
	public TreeMap<String, String> getPaths() {
		TreeMap<String, String> routingTable = new TreeMap<>();
		
		for (Nodes v : topology.values()) {
			List<Nodes> list = new LinkedList<>();
			v.printPath(list);
			Collections.reverse(list);
			if (list.size() > 0) {
				routingTable.put(list.get(0).node_name, list.get(list.size() - 1).node_name);
			} else {
				routingTable.put(v.node_name, "-");
			}
		}
		return routingTable;
	}
	
	/***
	 * This map returns a TreeMap structure which has the routing table (also called as connection table) of all the nodes.
	 * @param edges	A List of all the Edges in the network.
	 * @return		Returns a TreeMap structure which has the routing table of all the nodes.
	 */
	public TreeMap<String, TreeMap<String, String>> getConnectionTable(ArrayList<Link> edges) {
		TreeMap<String, TreeMap<String, String>> routingTable = new TreeMap<>();
		
		for (Nodes v : topology.values()) {
			DijkshtrasAlgorithm d = new DijkshtrasAlgorithm(edges);
			d.do_DijkshtrasAlgo(v.node_name);
			TreeMap<String, String> localTable = d.getPaths();
			routingTable.put(v.node_name, localTable);
		}
		//System.out.println("ROuting Table#####################################:"+routingTable);
		return routingTable;
	}
	
	/***
	 * This method returns a TreeMap string source id and integer total cost evaluated to find the overall shortest path from every node in the system ,so as to declare the particular node as broadcast node.
	 * @param edges	A List of all the Edges in the network.
	 * @return		Returns the TreeMap structure of source string and its minimum cost integer
	 * 	 */
	public TreeMap<Integer,String> routerWithShortestPath(ArrayList<Link> edges)
	{
		Set<String> routerList = topology.keySet();
		TreeMap<Integer,String> minCostList = new TreeMap<>();
		
		for(String router : routerList){
			 String sourceId = router;
			 int totalCost=0;
			 DijkshtrasAlgorithm topoTemp = new DijkshtrasAlgorithm(edges);
			 topoTemp.do_DijkshtrasAlgo(sourceId);
				for(String destRouter : routerList){
					if(!sourceId.equalsIgnoreCase(destRouter)){
			          Set<List<Nodes>> shortestPaths = topoTemp.getAllShortestPathsTo(destRouter);
			          totalCost = totalCost+topoTemp.getDistance(shortestPaths);
					}
				}
				
				minCostList.put(totalCost, sourceId);		
			
		}
		
		return minCostList;
		
	}
	
	/***
	 * This method returns a string which has routing table of all the nodes in the network in a formatted manner for printing purpose.
	 * @param edges	A List of all the Edges in the network.
	 * @return		Returns the string which has the routing tables in formatted manner.
	 */
	public String printConnectionTable(ArrayList<Link> edges, String sourceId) {
		
		TreeMap<String, TreeMap<String, String>> routingTable = getConnectionTable(edges);
		StringBuffer connectionTable = new StringBuffer();
		
			connectionTable.append(" ROUTER ").append(sourceId).append(" CONNECTION TABLE\n\n");
			connectionTable.append("     Destination    Next Hop \n");
			connectionTable.append("--------------------------------------\n");
			
			TreeMap<String, String> rt = (TreeMap<String, String>) routingTable.get(sourceId);
			for (Map.Entry<String, String> entry : rt.entrySet()) {
				String line = String.format("%13s %19s", entry.getKey() , entry.getValue());
				connectionTable.append(line).append("\n");
			}
			connectionTable.append("--------------------------------------\n\n");
			
			
		
		System.out.println(connectionTable.toString());
		return connectionTable.toString();
	}
}
