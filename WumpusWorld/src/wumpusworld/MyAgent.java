package wumpusworld;

import java.util.Arrays;
import java.lang.Math;

/**
 * Contains starting code for creating your own Wumpus World agent.
 * Currently the agent only make a random decision each turn.
 * 
 * @author Johan Hagelbäck
 */
public class MyAgent implements Agent
{
    private World w;
    int rnd;
    int counter = 0;
    boolean wumpus_found = false;
    /**
     * Creates a new instance of your solver agent.
     * 
     * @param world Current world state 
     */
    public MyAgent(World world)
    {
        w = world;   
    }
   
            
    /**
     * Asks your solver agent to execute an action.
     */

    public void doAction()
    {   
    	Graph g = new Graph(w, w.getPlayerX(), w.getPlayerY(), this.wumpus_found); 
        g.createGraph();
        g.updateGraph();
        //Location of the player
        int cX = w.getPlayerX();
        int cY = w.getPlayerY();
        System.out.println("The x and y values are: " + cX + " " + cY);
        
        //Basic action:
        //Grab Gold if we can.
        if (w.hasGlitter(cX, cY))
        {
            w.doAction(World.A_GRAB);
            return;
        }
        
        //Basic action:
        //We are in a pit. Climb up.
        if (w.isInPit())
        {
            w.doAction(World.A_CLIMB);
            return;
        }
        
        //Test the environment
        if (w.hasBreeze(cX, cY))
        {
            System.out.println("I am in a Breeze");
        }
        if (w.hasStench(cX, cY))
        {
            System.out.println("I am in a Stench");
        }
        if (w.hasPit(cX, cY))
        {
            System.out.println("I am in a Pit");
        }
        if (w.getDirection() == World.DIR_RIGHT)
        {
            System.out.println("I am facing Right");
        }
        if (w.getDirection() == World.DIR_LEFT)
        {
            System.out.println("I am facing Left");
        }
        if (w.getDirection() == World.DIR_UP)
        {
            System.out.println("I am facing Up");
        }
        if (w.getDirection() == World.DIR_DOWN)
        {
            System.out.println("I am facing Down");
        }
        
        
        if(w.hasStench(1, 1)) 
        {
        	int temp = 1;
        	if(w.hasArrow()) 
        	{
        		System.out.println("Hello1234");
	        	if(w.getDirection() == World.DIR_UP) 
	        	{
	        		w.doAction(World.A_SHOOT);
	        	}
	        	
		 		else if(w.getDirection() == World.DIR_LEFT) 
		 		{
		 			w.doAction(World.A_TURN_RIGHT);
		 			w.doAction(World.A_SHOOT);
		 			}
		 		else if(w.getDirection() == World.DIR_DOWN) 
		 		{
		 			w.doAction(World.A_TURN_LEFT);
		 			w.doAction(World.A_SHOOT);
		 			temp = 2;
		 		}
		 		else 
		 		{
		 			w.doAction(World.A_SHOOT);
		 			temp = 2;
		 		}
        	}
        	
        	if(w.wumpusAlive()) 
        	{
        		boolean wumpus_known = true;
        		Graph g2 = new Graph(w, w.getPlayerX(), w.getPlayerY(), wumpus_known);  
                g2.createGraph();
                g2.updateGraph();
                if(temp == 2) g2.C[0][1].cost = 500000;
                else g2.C[1][0].cost = 500000;
        		g2.minPath();
        	}
        	else g.minPath();
        }

        else if (w.hasStench(cX, cY))
	        {  
	        	if(w.hasArrow()) {
		            int[] x = new int[]{cX+1, cX+1, cX-1, cX-1, cX+2, cX-2, cX, cX};
		            int[] y = new int[]{cY+1, cY-1, cY-1, cY+1, cY, cY, cY+2, cY-2};
		            boolean shooted = false;
		            for(int i = 0; i < x.length; i++) {
		            	if(w.hasStench(x[i], y[i])) 
		            	{
		            		if(i<4) 
		            		{
		            			if(w.isVisited(x[i], cY))
		            			{
		            				g.move_shoot(cX-cX, y[i]-cY, 2);
		            				shooted = true;
		            				break;
		            			}
		            			else if(w.isVisited(cX, y[i])) 
		            			{
		            				g.move_shoot(x[i]-cX, cY-cY, 2);
		            				shooted = true;
		            				break;
		            			}		
		            		}
		            		else 
		            		{
		            			g.move_shoot((x[i]-cX)/2,(y[i]-cY/2), 2);
		            			shooted = true;
		            			break;
		            		}
		            	}
		            
		            }
		            if (shooted == false) 
		            {
		            	g.minPath();
		            }
	        	}
	        	else 
	        	{
	        		g.minPath(); 
	        	}
	        }
        else 
        {
        	g.minPath();
        }
    }  //doAction() ends here  
    
     /**
     * Generates a random instruction for the Agent.
     */
    public int decideRandomMove()
    {
      return (int)(Math.random() * 4);
    }
}