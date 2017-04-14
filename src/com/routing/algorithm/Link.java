package com.routing.algorithm;

/***
 * This class is used to define an edge (link) between two vertices (nodes).
 * 
 * @author Shruti Gupta
 *
 */

public class Link {
	// router1 and router2 are the name of the end nodes of the edge.
	public final String router1, router2;
	
	//This variable stores the link weight of the link i.e. distance between the two end nodes.
	public int link_weight;

	/***
	 * This constructor is used to initialize an Edge for a given network.
	 * @param router1	Name of router 1 (First node) of the link.
	 * @param router2	Name of router 2 (Second node) of the link.
	 * @param link_weight is the Weight of the link i.e. distance between Node 1 and Node 2
	 */
	public Link(String router1, String router2, int link_weight) {
		this.router1 = router1;
		this.router2 = router2;
		this.link_weight = link_weight;
	}

	/***
	 * This method overrides the toString() method and displays the end nodes of the edge with the link weight.
	 */
	@Override
	public String toString() {
		return "Link [" + router1 + " -- " + router2 + " = " + link_weight + "]";
	}
}

