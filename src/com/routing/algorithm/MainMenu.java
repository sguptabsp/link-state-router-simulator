package com.routing.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;


public class MainMenu 
{
	

	public static void main(String[] args) {
		ArrayList<Link> edges=null;

		while(true){
		
        System.out.println("CS542 LINK STATE ROUTING SIMULATOR");
		System.out.println("Press numbers from the following Menu options:");
		System.out.println("(1) Create a Network Topology");
		System.out.println("(2) Build a connection Table");
		System.out.println("(3) Shortest Path to destination Router");
		System.out.println("(4) Modify a Topology");
		System.out.println("(5) Best Router for Broadcast");
		System.out.println("(6) Exit");
		
		
		 // Reading from System.in
		Scanner reader = new Scanner(System.in); 
		//reader = new Scanner(System.in);
		System.out.println("Enter the menu option: ");
		int n = reader.nextInt();
		
        switch (n) {
        
            case 1:  
            Scanner reader2 = new Scanner(System.in);  // Reading from System.in
            System.out.println("Enter the file name of the file to be read(in the form of 'FILENAME.txt':");
            String fileName = reader2.nextLine();
            		 try{
	                    edges = CreateTopologyMatrix.createTopologyMatrix(System.getProperty("user.dir")+"\\"+fileName);
				       System.out.println(CreateTopologyMatrix.printAdjacencyMatrix().replaceAll("-1", "--"));
            		 }
            		 catch (Exception ex) {
         				// Error Handling
         				//System.out.println("topology matrix data file is wrong '" + fileName + "'");
         			} 
                     break;
            case 2:  
            		
            	if(isValidChoice(edges))
            	{
            		try{
            		 DijkshtrasAlgorithm topology = new DijkshtrasAlgorithm(edges);
            		 System.out.println("Enter the Source Router");
            		Scanner reader3 = new Scanner(System.in);
            		 String source = reader3.nextLine();
            		 
            		 topology.printConnectionTable(edges,source);
            		}
            		catch(Exception e)
   				     {
   					 System.out.println("Soruce router does not exist!!!");
   				     }
            	}
                break;
                
            case 3:  
            	if(isValidChoice(edges))
            	{
            		try{
            	     System.out.println("Enter the Source Router");
            		 Scanner reader3 = new Scanner(System.in);
            		 String source = reader3.nextLine();
            		 
            		 System.out.println("Enter the Destination Router");
            		 Scanner reader4 = new Scanner(System.in);
            		 String destination = reader4.nextLine();
            		 
            		 // ArrayList<Link> edges2 = CreateTopologyMatrix.createTopologyMatrix();
            		  DijkshtrasAlgorithm topology2 = new DijkshtrasAlgorithm(edges);
            		 //topology.printRoutingTable(edges2);
            		 //topology2 = new DijkshtrasAlgorithm(edges2);
            		 topology2.do_DijkshtrasAlgo(source);
         			Set<List<Nodes>> shortestPaths = topology2.getAllShortestPathsTo(destination);
         			
         			System.out.println("The shortest path from router "+source+" to router "+destination+" is "+topology2.printPaths(shortestPaths)+", the total cost is "+topology2.getDistance(shortestPaths));
         			
            		}
            		catch(Exception e)
   				     {
   					 System.out.println("Either Source or Destination router does not exist!!!");
   				     }
            
            	}
                     break;
            case 4: 
            	if(isValidChoice(edges))
            	{
            	System.out.println("The Original Matrix is:");
        		System.out.println(CreateTopologyMatrix.printAdjacencyMatrix().replaceAll("-1", "--"));
        		System.out.println("Enter the router to be removed");
				// Scanner sc = new Scanner(System.in);
				 int nodeNumber = reader.nextInt();
				 try{
				     edges = CreateTopologyMatrix.removeNode(nodeNumber);
            	     
            	     System.out.println("The New Matrix is:");
             		System.out.println(CreateTopologyMatrix.printAdjacencyMatrix().replaceAll("-1", "--"));
             		
             	 DijkshtrasAlgorithm topology = new DijkshtrasAlgorithm(edges);
             	System.out.println("Enter the Source Router");
        		Scanner reader3 = new Scanner(System.in);
        		 String source = reader3.nextLine();
        		 
        		 System.out.println("Enter the Destination Router");
        		 Scanner reader4 = new Scanner(System.in);
        		 
        		 String destination = reader4.nextLine();
           		topology.printConnectionTable(edges,source);
           		
           		topology.do_DijkshtrasAlgo(source);
     			Set<List<Nodes>> shortestPaths = topology.getAllShortestPathsTo(destination);
     			
     			System.out.println("The shortest path from router "+source+" to router "+destination+" is "+topology.printPaths(shortestPaths)+", the total cost is "+topology.getDistance(shortestPaths));
     			
				 } catch(Exception e)
				 {
					 System.out.println("Router does not exist!!!");
				 }
            	}
                     break;
            case 5:
            	DijkshtrasAlgorithm topology = new DijkshtrasAlgorithm(edges);
          		// topology.printRoutingTable(edges);
            	TreeMap<Integer,String> minCostList= topology.routerWithShortestPath(edges);
            	Integer minmumCost = minCostList.firstKey();
            	System.out.println("The Best Router for broadcast is :"+ minCostList.get(minmumCost));
            	System.out.println("The minimum cost from this router is :"+ minmumCost);
              break;
            case 6:  
            	System.out.println("Exit CS542-04 2016 Fall project. Good Bye!");
            	System.exit(0);;
                     break;
            
            default:
               System.out.println("Wrong Choice!! Please try again with right choice");
                     break;
        }
        }
      
    }
	
	 static boolean isValidChoice(ArrayList<Link> edges)
	 {
		 if(edges==null)
		 {
			 System.out.println("Please first Create topology");
			 return false;
		 }
		 else
		 return true;
	 }

	
}
