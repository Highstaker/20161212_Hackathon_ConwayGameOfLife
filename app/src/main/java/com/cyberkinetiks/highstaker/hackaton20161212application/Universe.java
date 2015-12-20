package com.cyberkinetiks.highstaker.hackaton20161212application;

import android.util.Log;

import static java.util.Arrays.deepEquals;

/**
 * Created by highstaker on 12.12.15.
 */
//ALT+INSERT = proposes constructors, etc.


public class Universe {//abstract layer for life game
    public final int SIZE;

    public static final int LIVE = 0;//the initial state of a living cell. The older it gets, the larger its number gets
    public static final int DEAD = -1;
    public static final int DYING = -2;
    public static final int BORN = -3;

    private int[][] universe;//grid
    private int[][] cell_ages;
    public int time = 0;
    public int aliveCount;
    public boolean systemStable = false;//set to true when the system no longer changes

    private void initArrays()
    {
        universe = new int[SIZE][SIZE];
        cell_ages = new int[SIZE][SIZE];
    }

    public Universe() {
        //Initializing Universe with default size
        SIZE = 50;
        initArrays();
        //prev_universe = new int[SIZE][SIZE];
        initializeTiles();
    }//Universe()

    public Universe(int size) {
        //Initialize new universe with a specified size.
        SIZE = size;
        initArrays();
        //prev_universe = new int[SIZE][SIZE];
        initializeTiles();

    }//Universe()

    public Universe(int[][] u,int[][] ages,int size)
    {
        //apply saved state
        SIZE = size;
        universe = u;
        cell_ages = ages;
        aliveCount = countAlives();
    }

    public int[][] getUniverse()
    {
        //returns the universe
        return universe;
    }

    public int getCellAge(int x,int y)
    {
        //returns the universe
        return cell_ages[x][y];
    }

    public int[][] getCellAges()
    {
        //returns the universe
        return cell_ages;
    }

    public void initializeTiles()
    {
        for(int x=0; x < SIZE; x++)
            for(int y=0; y < SIZE; y++)
            {
                //spawn random alive cells
                universe[x][y] = (Math.random() > 0.5 ? LIVE : DEAD);
                cell_ages[x][y] = 0;
            }
        time=0;
        aliveCount = countAlives();
        systemStable = false;
    }

    private int countAlives()
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
            universe[x][y] = BORN;
        }

    }

    public void doStep(){
        //Log.d("CKdebug","doStep()");
        //backup the universe, so it could be compared later
        //and yeah, copying multidimentional arrays is not as simple as it might be

        int[][] prev_universe = new int[SIZE][SIZE];//a copy of current universe, will be left as is until the end of cycle

        for(int i = 0; i < SIZE; i++)
        {
            prev_universe[i] = universe[i].clone();
        }

        for(int x=0; x < SIZE; x++)
            for(int y=0; y < SIZE; y++)
            {
                //check the rules and set the cells to dying or born state
                checkCell(x,y);
                //increment the age of living cells
                if (universe[x][y] == LIVE)
                {
                    cell_ages[x][y]++;
                }
                else
                {
                    ;
                }
            }

        //make the born cells living and the dying ones dead
        for(int x=0; x < SIZE; x++)
            for(int y=0; y < SIZE; y++)
            {
                if(universe[x][y] == DYING) {
                    universe[x][y] = DEAD;
                }
                else if(universe[x][y] == BORN) {
                    universe[x][y] = LIVE;
                    cell_ages[x][y] = 0;
                }
            }

        //if the grid hasn't changed, system stable.
        // deepEquals works with multidimentional arrays, unlike equals, cause multidimentional arrays in Java are actually arrays of arrays.
        if(deepEquals(universe, prev_universe)){
            Log.d("CKdebug", "system is stable!");
            systemStable = true;
            }
        else
        {
            //count time or recount alives only if the system has changed
            time++;
            aliveCount = countAlives();
        }
    }

}//class Universe
