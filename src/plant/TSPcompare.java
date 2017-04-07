package plant;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.Timer;

import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.PrimMST;
import edu.princeton.cs.algs4.StdDraw;

public class TSPcompare {

	private Random rand = new Random();
	private double[][] v; 							//stores x and y components of each vertex
	private ArrayList<Integer> path;				//stores the order in which the vertices are visited
	private double totalDist;						//total distance traveled
	private ArrayList<ArrayList<Integer>> lines; 	//used in draw method to check if there is already a line between 2 vertices

	//int vNum = number of vertices in graph
	public TSPcompare(int vNum){
		StdDraw.setCanvasSize(800, 800);
        StdDraw.enableDoubleBuffering();
        StdDraw.setScale(-10, 110);		//allows for empty border around graph

        StdDraw.setPenColor(Color.BLACK);

		v = new double[vNum][2];		//initialized to hold x and y for each vertex
		lines = new ArrayList<>(vNum);  //initialize lines

		//assign random values for each vertex and draw them
		for(int i = 0; i < vNum; i++){
			double x = rand.nextDouble()*100;
			double y = rand.nextDouble()*100;
			v[i][0] = x;
			v[i][1] = y;
			if(i == 0){		//for starting vertex
				StdDraw.setPenColor(new Color(13, 255, 19, 255));
				StdDraw.filledCircle(x, y, 1.25);
				StdDraw.setPenColor(Color.BLACK);
			}
			else{
				StdDraw.filledCircle(x, y, .75);
			}
			if(vNum<100){
				StdDraw.text(x, y-3, Integer.toString(i));	//draw the number of the vertex if there are less than 100 vertices
			}
			lines.add(new ArrayList<Integer>());			//add an arraylist to lines for each vertex
		}

		//displays the key
		StdDraw.text(25, 108, "KEY:");
		StdDraw.text(35, 108, "Greedy");
		StdDraw.text(43.5, 105, "Twice-Around-The-Tree");
		StdDraw.setPenColor(Color.RED);
		StdDraw.filledRectangle(30, 108.5, 1, 1);
		StdDraw.setPenColor(Color.BLUE);
		StdDraw.filledRectangle(30, 105.5, 1, 1);
	}

	//starts at vertex 0 and visits closest vertex
	public void greedy(){
		boolean[] visited = new boolean[v.length];	//stores if a vertex has been visited or not
		int current = 0;							//the vertex that is being visited
		totalDist = 0;								//sets total distance to 0
		path = new ArrayList<>();					//initialize the path

		//add vertex 0 as the first vertex
		path.add(0);
		visited[0] = true;

		//iterate until there are v.length-1 edges
		for(int i = 1; i < v.length; i++){
			int near = 0;							//stores which vertex is closest to current vertex
			double nearValue = Integer.MAX_VALUE;	//distance to closest node

			//iterates through all vertices
			for(int j = 0; j < v.length; j++){
				if(!visited[j] && j != current){	//if the vertex has not been visited and is not the current vertex
					double value = Math.sqrt(Math.pow(v[current][0] - v[j][0], 2)+Math.pow(v[current][1] - v[j][1], 2));	//calculate distance to vertex
					if(value < nearValue){	//set this vertex as the nearest if it is closer
						nearValue = value;
						near = j;
					}
				}
			}
			//add new vertex to the path
			current = near;
			visited[current] = true;
			path.add(current);
			totalDist += nearValue;	//add to total distance
		}

		//add 0 as the final node and add to the distance
		path.add(0);
		totalDist += (Math.sqrt(Math.pow(v[path.get(path.size()-2)][0] - v[0][0], 2)+Math.pow(v[path.get(path.size()-2)][1] - v[0][1], 2)));

		drawPath(Color.RED);	//draw the path for this algorithm
	}

	//creates a mst and walks around the tree to find a path
	public void twiceAround(){
		EdgeWeightedGraph weightGraph = new EdgeWeightedGraph(v.length);	//stores two vertices and the weight of the edge
		//set vertices and weights for each edge in graph
		for(int i = 0; i < v.length; i++){
			for(int j = 0; j < v.length; j++){
				double weight = Math.sqrt(Math.pow(v[i][0] - v[j][0], 2)+Math.pow(v[i][1] - v[j][1], 2));
				weightGraph.addEdge(new Edge(i, j, weight));
			}
		}
		PrimMST mst = new PrimMST(weightGraph);	//find a minimum spanning tree using prim's algorithm

		boolean[] visited = new boolean[v.length];	//stores if a vertex has been visited
		Stack<Integer> s = new Stack<Integer>();	//used to navigate through tree
		path = new ArrayList<>();					//initializes the path

		//add 0 as the first vertex in the path
		s.push(0);
		visited[0] = true;
		path.add(0);
		totalDist = 0;

		//continues until all vertices have been visited
		while(!s.isEmpty()){
			int i = s.peek();

			boolean found = false;		//if the current vertex has 0 unvisited neighbors
			for(Edge e : mst.edges()){	//iterate through all edges in mst
				try{
					int j = e.other(i);	//j equals the other vertex of an edge with vertex i
					if(!visited[j]){	//if j has not already been visited
						s.push(j);		//push j onto the stack
						if(!path.contains(j)){	//add j to the path if it has not already been added
							int last = path.get(path.size()-1);
							totalDist += (Math.sqrt(Math.pow(v[last][0] - v[j][0], 2) + Math.pow(v[last][1] - v[j][1], 2)));
							path.add(j);
						}
						visited[j] = true;
						found = true;
						break;				//next iteration
					}
				}
				catch(Exception ex){}	//catches if e doesn't have a vertex i
			}
			if(!found){
				s.pop();		//pop the vertex if it has no univisted neighbors
			}
		}

		//add 0 to the path
		int last = path.get(path.size()-1);
		totalDist += (Math.sqrt(Math.pow(v[0][0] - v[last][0], 2) + Math.pow(v[0][1] - v[last][1], 2)));
		path.add(0);

		drawPath(Color.BLUE);	//draw the path for this algorithm
	}

	//draws the path of an algorithm using StdDraw
	public void drawPath(Color color){
		StdDraw.setPenColor(color);
		for(int i = 0; i < path.size(); i++){
				System.out.printf("%d%s", path.get(i), i == path.size()-1 ? "\n" : " -> ");		//print out path to console
				if(i > 0){
					int current = path.get(i);
					int last = path.get(i-1);

					//if a line has been drawn between these vertices then shift the line
					//use Math functions to determine where to draw the line
					//basically determine theta, add 90 degrees and shift the line at that angle
					if(lines.get(current).contains(last)){
						double x1 = v[last][0];
						double y1 = v[last][1];
						double x2 = v[current][0];
						double y2 = v[current][1];
						double dx = x2 - x1;
						double dy = y2 - y1;

						double theta = Math.atan2(dy, dx);

						double rad = Math.toRadians(90);
						double newX1 = x1 + .75*Math.cos(rad+theta);
						double newY1 = y1 + .75*Math.sin(rad+theta);
						double newX2 = x2 + .75*Math.cos(rad+theta);
						double newY2 = y2 + .75*Math.sin(rad+theta);

						StdDraw.line(newX1, newY1, newX2, newY2);	//draw the line
						drawArrow(newX2, newX1, newY2, newY1);		//draw arrow at the end of the line
					}
					//otherwise draw the line from the centers of the vertices
					else{
						StdDraw.line(v[last][0], v[last][1], v[current][0], v[current][1]);	//draw the line
						lines.get(current).add(last);	//add the line to lines that have been drawn
						lines.get(last).add(current);
						drawArrow(v[current][0], v[last][0], v[current][1], v[last][1]);	//draw arrow at the end of the line
					}
				}
		}
		System.out.printf("Distance traveled: %.2f\n", totalDist);	//print the total distance traveled to 2 decimal places
	}

	//algorithm found on Stack Overload
	private void drawArrow(double tipX, double tailX, double tipY, double tailY)
	{
	    double arrowLength = 1.75; //can be adjusted
	    double dx = tipX - tailX;
	    double dy = tipY - tailY;

	    double theta = Math.atan2(dy, dx);	//angle of the line

	    double rad = Math.toRadians(30); //angle of on side of the triangle
	    double x = tipX - arrowLength * Math.cos(theta + rad);
	    double y = tipY - arrowLength * Math.sin(theta + rad);

	    double phi2 = Math.toRadians(-30);//angle of other side of the triangle
	    double x2 = tipX - arrowLength * Math.cos(theta + phi2);
	    double y2 = tipY - arrowLength * Math.sin(theta + phi2);

	    double[] arrowYs = new double[3];	//y position for three points of the triangle
	    arrowYs[0] = tipY;
	    arrowYs[1] =  y;
	    arrowYs[2] =  y2;


	    double[] arrowXs = new double[3];	//x position for three points of the triangle
	    arrowXs[0] = tipX;
	    arrowXs[1] = x;
	    arrowXs[2] = x2;

	    StdDraw.filledPolygon(arrowXs, arrowYs);	//draw the triangle
	}

	public static void main(String[] args){
		TSPcompare tsp = new TSPcompare(Integer.parseInt(args[0]));	//create new TSPcompare with args[0] = the number of vertices

		long startTime = System.currentTimeMillis();
		tsp.greedy();	//run the greedy algorithm
		System.out.println("Total time: " + (double)(System.currentTimeMillis()-startTime)/1000 + " seconds\n");	//amount of time for the algorithm to complete

		startTime = System.currentTimeMillis();
		tsp.twiceAround();	//run the twice-around-the-tree algorithm
		System.out.println("Total time: " + (double)(System.currentTimeMillis()-startTime)/1000 + " seconds\n");	//amount of time for the algorithm to complete
		StdDraw.show();	//display StdDraw
	}
}
