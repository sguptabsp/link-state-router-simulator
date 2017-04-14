package com.routing.routing.test;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.routing.algorithm.DijkshtrasAlgorithm;
import com.routing.algorithm.Link;
import com.routing.algorithm.Nodes;
import com.routing.algorithm.CreateTopologyMatrix;

public class MyTest {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		ArrayList<Link> edges = CreateTopologyMatrix.createTopologyMatrix("C:/input.txt");

		DijkshtrasAlgorithm my = new DijkshtrasAlgorithm(edges);
		my.do_DijkshtrasAlgo("1");
		Set<List<Nodes>> n = my.getAllShortestPathsTo("5");

		System.out.println(my.printPaths(n));
		System.out.println(my.getDistance(n));

		System.out.println("\nAll Paths from A to B");

		Set<List<Nodes>> all = my.getAllPaths("2", "5");

		System.out.println(my.printPaths(all));

		System.out.println(CreateTopologyMatrix.printAdjacencyMatrix());
		
		System.out.println(my.printConnectionTable(edges, "1"));
	}

}

