package wumpusworld;
import java.util.HashMap;

public class Graph {
	World w;
	int cX, cY;
	boolean wumpus_found;
	Cave C[][] = new Cave[4][4];
	HashMap<Cave , Integer> caves_left = new HashMap<Cave , Integer>();
	HashMap<Cave , Integer> min_distance = new HashMap<Cave , Integer>();
	HashMap<Cave , Cave> route = new HashMap<Cave , Cave>();
	Graph(World world, int x, int y,boolean wumpus_found) {
		this.w = world;
		this.cX = x;
		this.cY = y;
		this.wumpus_found = wumpus_found;
	}
	void createGraph() {
		for(int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				this.C[i][j] = new Cave(i+1, j+1);
			}
		}
		for(int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				int [] adj_x = getAdj_x(i+1);
				int [] adj_y = getAdj_y(j+1);
				for(int k = 0; k < 4; k++) {
					if(w.isValidPosition(adj_x[k], adj_y[k])) {
						this.C[i][j].c[k] = this.C[adj_x[k]-1][adj_y[k]-1];
					}
				}
			}
		}
	}
	
	void updateGraph() {
		int pit_count = 0;
		for(int i = 1; i < 5; i++) 
		{
			for(int j = 1; j<5; j++) 
			{
				if(w.hasPit(i, j))
				{
					pit_count++;
					this.C[i-1][j-1].cost = 10000;
				}
				if(this.w.hasBreeze(i, j) && pit_count!=3) 
				{
					int [] adjx = getAdj_x(i);
					int [] adjy = getAdj_y(j);
					for (int k =0; k<4; k++) 
					{
						if(this.w.isValidPosition(adjx[k], adjy[k])  && !this.w.isVisited(adjx[k], adjy[k])) 
						{
							this.C[adjx[k]-1][adjy[k]-1].cost = 1000;
							this.C[adjx[k]-1][adjy[k]-1].prob_pit = true;
						}
					}
				}
			}
		}
		
		for(int i = 1; i < 5; i++) {
			for(int j = 1; j<5; j++) {
				if(this.w.hasStench(i, j) && !this.wumpus_found) {
					System.out.println("Stench Loop!");
					int [] adjx = getAdj_x(i);
					int [] adjy = getAdj_y(j);
					for (int k =0; k<4; k++) 
					{
						if(this.w.isValidPosition(adjx[k], adjy[k])  && !this.w.isVisited(adjx[k], adjy[k])) 
						{
							this.C[adjx[k]-1][adjy[k]-1].cost = 50000;
							System.out.println("X "+ adjx[k]+"Y "+ adjy[k]+this.C[adjx[k]-1][adjy[k]-1].cost);
							this.C[adjx[k]-1][adjy[k]-1].stench = true;
						}
					}
				}
			}
		}
		
		for(int i = 1; i < 5; i++) {
			for(int j = 1; j<5; j++) {
				if(!this.w.hasBreeze(i, j) && !this.w.hasStench(i, j)&& this.w.isVisited(i, j))
				{
					int [] adjx = getAdj_x(i);
					int [] adjy = getAdj_y(j);
					for (int k =0; k<4; k++) 
					{
						if(this.w.isValidPosition(adjx[k], adjy[k])  && !this.w.isVisited(adjx[k], adjy[k])) 
						{
							this.C[adjx[k]-1][adjy[k]-1].cost = 10;
							this.C[adjx[k]-1][adjy[k]-1].prob_pit = false;
							this.C[adjx[k]-1][adjy[k]-1].stench = false;
						}
					}
				}
			}
		}
		
		for(int i=0; i<4; i++) {
	 		for(int j=0; j<4; j++){	
	 			System.out.println("the cost at node ["+(i+1)+", "+(j+1)+"] is "+this.C[i][j].cost);
	 		}
	 	}
	}
    
	void minPath() {
		Cave present_cave = this.C[cX-1][cY-1];
		System.out.println("Stenchvyjfuyfdyt gyjygvjygvLoop!");
		for(int i=0; i<4; i++) {
	 		for(int j=0; j<4; j++){	
	 			this.caves_left.put(C[i][j], 1000000);
	 		}
	 	}
		 caves_left.put(present_cave, 0);
		 while(this.caves_left.size() != 0) 
		 {
			 System.out.println("Stenchvyjfuyfdyt Loop!");
			 // extracting node with minimum key value from the nodes_left
			 Cave min = this.extract_min();
			 
			 // placing the node extracted from the nodes_left in the min_distance
			 this.min_distance.put(min, this.caves_left.get(min));	
			 
			 // removing the the node from the nodes_left
			 this.caves_left.remove(min);
			 
			 // updating the distances of the neighbors of min 
			 this.update_Caves_left(min);
			 
		 }
		 int t = 4000000;
		 for (HashMap.Entry<Cave, Integer> pair: this.min_distance.entrySet()) {
			 
			    if(pair.getValue()<t) {
	            	t = pair.getValue();
	            	System.out.println("distance: "+pair.getValue());
	            }
		}
		 this.min_distance.remove(present_cave); 
			 // extracting the node corresponding to the minimum key value in min_distance
			 Cave dest = this.find_dest_cave();
			 System.out.println("X and Y values:"+ dest.x+" "+dest.y);
			 Cave final_dest = new Cave();
			 while(dest != present_cave) 
			 {
				 final_dest = dest; 
				 dest = route.get(dest);
			 }
			 
			 for(int i = 0; i < 4; i++) 
			 {
				 if(present_cave.c[i] != null && final_dest.x == present_cave.c[i].x && final_dest.y == present_cave.c[i].y) 
				 {
					 System.out.println("The program was here.");
					 move_shoot(present_cave.c[i].x-present_cave.x, present_cave.c[i].y-present_cave.y, 1);
				 }
			 }
	}
	
	void update_Caves_left(Cave present_cave) 
	{
		for(int i =0; i<4; i++) 
		{
			if(present_cave.c[i] != null && this.caves_left.containsKey(present_cave.c[i])){
				if( (this.min_distance.get(present_cave) + present_cave.c[i].cost) < this.caves_left.get(present_cave.c[i])){
					 System.out.println("connection 1" );
					 this.caves_left.replace(present_cave.c[i] , (present_cave.c[i].cost + this.min_distance.get(present_cave)));
					 this.route.put(present_cave.c[i], present_cave);
			}
		}
	}
	}
	
	Cave extract_min()
	{
		 int t = Integer.MAX_VALUE;
		 Cave min_Cave = new Cave();
		 for (HashMap.Entry<Cave, Integer> pair: this.caves_left.entrySet()) {
			 
			    if(pair.getValue()<t) {
	            	t = pair.getValue();
	            	min_Cave = pair.getKey();
	            }
		} 
		return min_Cave;
	 }
	
	Cave find_dest_cave()
	{
		 int t = Integer.MAX_VALUE;
		 Cave minCave = new Cave();
		 for(int i=0; i<4; i++) {
				for(int j=0; j<4; j++){	
					if(w.isVisited(i+1, j+1)) this.min_distance.remove(this.C[i][j]);
				}
			}
		 
		 
		 for (HashMap.Entry<Cave, Integer> pair: this.min_distance.entrySet()) {
			 
			    if(pair.getValue()<t) {
	            	t = pair.getValue();
	            	minCave = pair.getKey();
	            }
		}
		return minCave;
		
	 }
	
	
	
	public int[] getAdj_x(int x){
        return new int[]{x, x, x+1, x-1};
    }
    
    public int[] getAdj_y(int y){
        return new int[]{y-1, y+1, y, y};
    }
    
    public void move_shoot(int a, int b, int c){ //a = x - cX;  b = y -cY;
        if(a == 1 && b == 0){
            if(w.getDirection() == World.DIR_RIGHT){
            	if(c==1) w.doAction(World.A_MOVE);
            	else w.doAction(World.A_SHOOT);
                return;
                }
            if(w.getDirection() == World.DIR_LEFT){
                w.doAction(World.A_TURN_LEFT);
                w.doAction(World.A_TURN_LEFT);
                if(c==1) w.doAction(World.A_MOVE);
            	else w.doAction(World.A_SHOOT);
                return;
                }
            if(w.getDirection() == World.DIR_UP){
                w.doAction(World.A_TURN_RIGHT);
                if(c==1) w.doAction(World.A_MOVE);
            	else w.doAction(World.A_SHOOT);
                return;
            }
            
            if(w.getDirection() == World.DIR_DOWN){
                w.doAction(World.A_TURN_LEFT);
                if(c==1) w.doAction(World.A_MOVE);
            	else w.doAction(World.A_SHOOT);
                return;
            }
            
        }
        if(a == -1 && b == 0){
            if(w.getDirection() == World.DIR_LEFT){
            	if(c==1) w.doAction(World.A_MOVE);
            	else w.doAction(World.A_SHOOT);
                return;
                }
            if(w.getDirection() == World.DIR_RIGHT){
                w.doAction(World.A_TURN_LEFT);
                w.doAction(World.A_TURN_LEFT);
                if(c==1) w.doAction(World.A_MOVE);
            	else w.doAction(World.A_SHOOT);
                return;
                }
            if(w.getDirection() == World.DIR_UP){
                w.doAction(World.A_TURN_LEFT);
                if(c==1) w.doAction(World.A_MOVE);
            	else w.doAction(World.A_SHOOT);
                return;
            }
            
            if(w.getDirection() == World.DIR_DOWN){
                w.doAction(World.A_TURN_RIGHT);
                if(c==1) w.doAction(World.A_MOVE);
            	else w.doAction(World.A_SHOOT);
                return;
            }
            
        }
        //Move to down square
        if(a == 0 && b == -1){
            if(w.getDirection() == World.DIR_RIGHT){
                w.doAction(World.A_TURN_RIGHT);
                if(c==1) w.doAction(World.A_MOVE);
            	else w.doAction(World.A_SHOOT);
                return;
                }
            if(w.getDirection() == World.DIR_LEFT){
                w.doAction(World.A_TURN_LEFT);
                if(c==1) w.doAction(World.A_MOVE);
            	else w.doAction(World.A_SHOOT);
                return;
                }
            if(w.getDirection() == World.DIR_UP){
                w.doAction(World.A_TURN_RIGHT);
                w.doAction(World.A_TURN_RIGHT);
                if(c==1) w.doAction(World.A_MOVE);
            	else w.doAction(World.A_SHOOT);
                return;
            }
            
            if(w.getDirection() == World.DIR_DOWN){
            	if(c==1) w.doAction(World.A_MOVE);
            	else w.doAction(World.A_SHOOT);
                return;
            }
            
        }
        //Move to up square
        if(a == 0 && b == 1){
            if(w.getDirection() == World.DIR_RIGHT){
                w.doAction(World.A_TURN_LEFT);
                if(c==1) w.doAction(World.A_MOVE);
            	else w.doAction(World.A_SHOOT);
                return;
                }
            if(w.getDirection() == World.DIR_LEFT){
                w.doAction(World.A_TURN_RIGHT);
                if(c==1) w.doAction(World.A_MOVE);
            	else w.doAction(World.A_SHOOT);
                return;
                }
            if(w.getDirection() == World.DIR_UP){
            	if(c==1) w.doAction(World.A_MOVE);
            	else w.doAction(World.A_SHOOT);
                return;
            }
            
            if(w.getDirection() == World.DIR_DOWN){
                w.doAction(World.A_TURN_RIGHT);
                w.doAction(World.A_TURN_RIGHT);
                if(c==1) w.doAction(World.A_MOVE);
            	else w.doAction(World.A_SHOOT);
                return;
            }
            
        }
        
    }
}
