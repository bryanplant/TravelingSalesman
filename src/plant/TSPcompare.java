package plant;

import java.util.Random;
import java.math.*;

public class TSPcompare {
	
	Random rand = new Random();
	double[][] v;
	
	public TSPcompare(int vNum){
		v = new double[vNum][2];
		
		for(int i = 0; i < vNum; i++){
			v[i][0] = rand.nextDouble()*100;
			v[i][1] = rand.nextDouble()*100;
		}
		
		/*for(int i = 0; i < v; i++){
			System.out.println("Vert" + i + ": " + vert[i][0] + ", " + vert[i][1]);
		}*/
	}
	
	public void greedy(){
		boolean[] visited = new boolean[v.length];
		
		for(int i = 0; i < v.length; i++){
			int near = 0;
			double nearValue = 100;
			
			for(int j = 0; j < v.length; j++){
				//double value = 
			}
		}
	}
	
	public static void main(String[] args){
		TSPcompare tsp = new TSPcompare(Integer.parseInt(args[0]));
		tsp.greedy();
	}
}
