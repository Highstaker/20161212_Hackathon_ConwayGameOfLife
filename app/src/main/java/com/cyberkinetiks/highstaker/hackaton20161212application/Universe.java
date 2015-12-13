package com.cyberkinetiks.highstaker.hackaton20161212application;

/**
 * Created by highstaker on 12.12.15.
 */
//ALT+INSERT = proposes constructors, etc.

public class Universe {//abstract layer for life game
    public static final int SIZE = 50;

    public static final int LIVE = 0;
    public static final int DEAD = 1;
    public static final int DYING = 2;
    public static final int BORN = 3;

    int[][] universe;//grid
    public int time;
    public int aliveCount;

    public Universe() {
        //Initializing Universe
        universe= new int[SIZE][SIZE];
        initializeTiles();
    }//Universe()

    public void initializeTiles()
    {
        for(int x=0; x < SIZE; x++)
            for(int y=0; y < SIZE; y++)
            {
//            universe[x][y] = DEAD;
                //spawn random alive cells
                universe[x][y] = (Math.random() > 0.5 ? LIVE : DEAD);
            }
        time=0;
        aliveCount = countAlives();
    }

    int countAlives()
    {
        int count = 0;
        for(int x=0; x < SIZE; x++)
            for(int y=0; y < SIZE; y++)
            {
                if ( isAlive(x,y) ) count++;
            }

        return count;

    }

    boolean isAlive(int x, int y)
    {
        //handle out of bounds
        if(x < 0)x+=SIZE;
        if(y < 0)y+=SIZE;
        if(x > (SIZE-1))x-=SIZE;
        if(y > (SIZE-1))y-=SIZE;

        return universe[x][y] == LIVE;
    }

    static int [][] neighbours = {{1,1},{1,0},{1,-1},{0,1},{0,-1},{-1,1},{-1,0},{-1,-1}};

    int countNeighbourCells(int x, int y)
    {
        int count = 0;

        for (int[] offset:neighbours){
            if(isAlive(x+offset[0],y+offset[1]))
            {
                count++;
            }
        }

        return count;
    }

    void checkCell(int x, int y)
    {
        int n = countNeighbourCells(x,y);
        boolean isCellAlive = isAlive(x,y);

        if(isCellAlive &&(n < 2 || n > 3))
        {
            universe[x][y] = DYING;
        }
        else if (!isCellAlive && n==3)
        {
            universe[x][y]=BORN;
        }

    }

    public void doStep(){
        for(int x=0; x < SIZE; x++)
            for(int y=0; y < SIZE; y++)
            {
                checkCell(x,y);
            }

        for(int x=0; x < SIZE; x++)
            for(int y=0; y < SIZE; y++)
            {
                if(universe[x][y] == DYING) {
                    universe[x][y] = DEAD;
                }
                else if(universe[x][y] == BORN) {
                    universe[x][y] = LIVE;
                }
            }

        time++;
        aliveCount = countAlives();

    }

}//class Universe
