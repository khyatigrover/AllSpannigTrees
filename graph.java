import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;
public class graph {
  static int k=-1;
  static int flag =0;
  public static Scanner scn = new Scanner(System.in); 
	public class vertex {
		String name;                          // contains name of vertex
		HashMap<String, Integer> neighbours;  // Hashmap containing the name of all its neighbour vertices and the length of edge from the vertex to the neighbour

		public vertex(String name) {
			this.name = name;
			this.neighbours = new HashMap<>();
		}
	}
    public HashMap<String, vertex> vertices;  // hashmap containing the vertex name and the corresponding vertex class.

	public graph() {
		this.vertices = new HashMap<>();      // contsructor of graph initialises the hashmap
	}
	
	
	// function to check whether a vertex is present in graph or not
	public boolean containsvertex(String name) {
		return vertices.containsKey(name);
	}
	
	// function to add vertex in a graph
    public void addvertex(String vname) {
		if (vertices.containsKey(vname)) {
			return;
		} else {
			vertex newv = new vertex(vname);
			vertices.put(vname, newv);
		}
	}
    
    // function to add edge between the vertices already present in graph
	public void addedge(String v1name, String v2name, int cost) {
		vertex v1 = vertices.get(v1name);
		vertex v2 = vertices.get(v2name);
		if (v1 == null || v2 == null) {
			return;
		} else {
			v1.neighbours.put(v2name, cost);
			v2.neighbours.put(v1name, cost);
		}
	}
	
	private class pair {
		String osf;
		vertex v;
	}	
	
	// function to check if graph is connected or not
	public boolean isconnected() {
		HashMap<String, Boolean> hp = new HashMap<>();
		LinkedList<pair> stack = new LinkedList<>();
		pair p = new pair();

		String zvn = this.vertices.keySet().iterator().next();// for getting the
																// first string
		vertex v = vertices.get(zvn);
		p.osf = zvn;
		p.v = v;
		stack.add(p);
		while (stack.size() != 0) {
			pair temp = stack.removeFirst();
			if (!hp.containsKey(temp.v.name)) {
				hp.put(temp.v.name, true);

				Set<String> sets = temp.v.neighbours.keySet();
				for (String ss : sets) {

					vertex vtx = vertices.get(ss);

					if (!hp.containsKey(vtx.name)) {
						pair mp = new pair();
						mp.v = vtx;
						mp.osf = temp.osf + temp.v.name;
						stack.addFirst(mp);
					}

				}
			}

		}
		return hp.size() == vertices.size();

	}
	
	
	public class edge implements Comparable<edge> {
		int cost;
		int pseudocost;
		vertex vtx;
		vertex acvtx;
  // criteria for comparing 2 edges
		public int compareTo(edge o) {
			
			return this.cost - o.cost;
		}
	}
// returns MST in the form of an array containing the edges present in MST and the total cost of MST
	// it takes input a hashmap comprising of string and boolean
	// boolean true indicates that the it is compulsory to include the corresponding string in the MST
	// boolean false indicates that the corresponding string should not be included in the MST
	public ArrayList<String> MST(HashMap<String, Boolean> hp) {
		
		int sum=0;   // sum calculates the cost of MST
		ArrayList<String> rvs = new ArrayList<>();
		graph rv = new graph();
		HashMap<String, edge> hms = new HashMap<>();

		PriorityQueue<edge> hps = new PriorityQueue<>();

		Set<String> points = vertices.keySet();
		for (String ss : points) {
			vertex pointvertex = vertices.get(ss);
			edge mp = new edge();
			mp.cost = Integer.MAX_VALUE;
			mp.pseudocost=Integer.MAX_VALUE;
			mp.vtx = pointvertex;
			mp.acvtx = null;

			hms.put(pointvertex.name, mp);
			hps.add(mp);
			

		}

		while (hps.size() != 0) {
			edge np = hps.remove();
			
			rv.addvertex(np.vtx.name);
			if (np.acvtx != null) {
				rv.addedge(np.vtx.name, np.acvtx.name, np.cost);
				rvs.add(np.acvtx.name+np.vtx.name+"");
				sum+=np.pseudocost;
			}

			Set<String> nbrs = np.vtx.neighbours.keySet();
			for (String ss : nbrs) {
				if (rv.containsvertex(ss)) {
					continue;
				}
				
               
				edge tochange = hms.get(ss);
			
	               
				if (tochange.cost > np.vtx.neighbours.get(ss)) {
					
					String s1=tochange.vtx.name+np.vtx.name+"";
					String s2=np.vtx.name+tochange.vtx.name+"";
					
					if(hp.containsKey(s1)||hp.containsKey(s2)){
						
						// the string s1 or s2 is either to be compulsory included or not included
						if((hp.containsKey(s1)&&hp.get(s1)==false)||(hp.containsKey(s2)&&hp.get(s2)==false)){
							// if it is not to be included
							// do nothing
						}
						if((hp.containsKey(s1)&&hp.get(s1)==true)||(hp.containsKey(s2)&&hp.get(s2)==true)){
							// if it is to be included,make its cost 0,so that it is extracted from the priority queue in the next iteration
							// change the pseudocost to the cost of the edge so that it helps in calculating the total cost of MST
							tochange.cost = 0;
							tochange.pseudocost=np.vtx.neighbours.get(ss);
							tochange.acvtx = np.vtx;
							hps.remove(hms.get(ss));
							hps.add(tochange);
						}
						
					}
					else{ 
						// if the string is not present in hashmap i.e there is no compulsion on its inclusion or exclusion
					tochange.pseudocost=np.vtx.neighbours.get(ss);
					tochange.cost = np.vtx.neighbours.get(ss);
					tochange.acvtx = np.vtx;
					hps.remove(hms.get(ss));
					hps.add(tochange);
				}
					}
			}

		}
		
		if(rv.isconnected())
		{   // i.e. if MST exists 
			rvs.add(sum+"");
			return rvs;}
		else {
			// if MST cannot be formed
			ArrayList<String> p =new ArrayList<String>() ;
			p.add("0");
		    return p;
		}
	}
	// function to reverse a string
	public String reverse(String s){
		String s1;
		char a=s.charAt(1);
		char b=s.charAt(0);
		s1=a+b+"";
		return s1;
	}
	
	// Main function that takes input and prints the spanning trees in increasing order of their weight
	public static void main(String[] args) {
		System.out.println("Press 0 for generating all and press 1 for generating kth smallest");
	    flag=scn.nextInt(); 
		if(flag==1){
		System.out.println("Enter the k to find kth smallest MST");
		k=scn.nextInt();
	     }
		graph gt = new graph();
		
		//  Adding inputs 
		
		gt.addvertex("V");
		gt.addvertex("W");
		gt.addvertex("X");
		gt.addvertex("Y");
		
		
		gt.addedge("Y", "W", 6);
		gt.addedge("V", "W", 1);
		gt.addedge("X", "Y", 4);
		
		gt.addedge("V", "Y", 3);
		gt.addedge("W", "X", 5);
    	gt.addedge("V", "X", 2);
    	
    	
		
		HashMap<String , Boolean> hp = new HashMap<>();
		// 
		ArrayList<String> mst = gt.MST(hp);
		
		PriorityQueue<HashMap<String, Boolean>> partitionqueue = new PriorityQueue<>( new Comparator<HashMap<String,Boolean>>( ) {

					@Override
					public int compare(HashMap<String, Boolean> o1, HashMap<String, Boolean> o2) {
						ArrayList<String> ars1=gt.MST(o1);
						ArrayList<String> ars2=gt.MST(o2);
						return Integer.parseInt(ars1.get(ars1.size()-1)) - Integer.parseInt(ars2.get(ars2.size()-1)); // the length of mst formed by o1 - length of mst formed by o2
					}
				} );
		boolean isMSTprinted=false;
		int count=1;
		partitionqueue.add(hp);
		while(partitionqueue.size()!=0){
			
		HashMap<String, Boolean> msthm = partitionqueue.remove();
		mst=gt.MST(msthm);      // we are assuring ki agar priority queue me gaya hai to mst exist krta hai therefore not checking the condition of mst being empty
		if(flag==0){
			System.out.println(mst);
			isMSTprinted=true;
			
		}else if(k==count){
			System.out.println(mst);
			isMSTprinted=true;
			break;
		}
		// create partitions using mst and msthm and add them in queue if their mst exists
			int counter = 1;
			HashMap<String, Boolean> partition_to_be_added = new HashMap<>();
			
			int l=0;
			while(true){
				partition_to_be_added= new HashMap<>();
				Set<String> set = msthm.keySet();
				for(String ss:set){
					Boolean b = msthm.get(ss);
					partition_to_be_added.put(ss,b);
				}
				
			int i=1;
			
			for( l=0;l<mst.size()-1;l++){           
				String s=mst.get(l);
				
				if(partition_to_be_added.containsKey(s)||partition_to_be_added.containsKey(gt.reverse(s))){
					// do nothing 
				}
				else
					if(counter==i){
						partition_to_be_added.put(s, false);
						counter++;
						ArrayList<String> d=gt.MST(partition_to_be_added);
						
						if(d.size()!=1){
							partitionqueue.add(partition_to_be_added);
							
						}
						break;
					}
					
					else{
						partition_to_be_added.put(s,true);
						i++;
					}
				}
			if(l==mst.size()-1){	
				break;
			}
			
			}
		
			count++;
			}		
		
		if(!isMSTprinted){
			System.out.println("No "+k+"th smallest mst exists.Enter k within the range");
		}
	}
}

