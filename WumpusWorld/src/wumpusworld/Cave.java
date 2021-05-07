package wumpusworld;

public class Cave {
	Cave[] c = new Cave[] {null, null, null, null};
	boolean stench = false, prob_pit = false, wumpus = false, breeze = false, pit = false;
	int cost = 10;
	int x, y;
	Cave(){
	}
	Cave(int x, int y){
		this.x = x;
		this.y = y;
	}	
}