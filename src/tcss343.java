import java.io.*;
import java.util.Stack;

/**
 * Created by Yeonil on 5/27/2015.
 */
public class tcss343 {
    public static void main(String[] args) {
//        System.out.println(System.getProperty("user.dir"));   //To find directory
        int[][] array;
        try {
            array = readFile(args[0]);  //Taking argument is requirement
            //* PRINT FULL ARRAY
            for(int i = 0; i < array.length; i++) {
                for (int j = 0; j < array.length; j++)
                    System.out.print(array[i][j] + "\t");
                System.out.println();
            }
            // END OF PRINTING ARRAY */

            dynamic(array);
            divideAndConquer(array);
            bruteforce(array);
        } catch(IOException e) {
            System.out.println(e);
        }
    }

    public static void bruteforce(int[][] array) {
        int shortest[] = new int[array.length-2];   //Storage that will contain shortest path
        int cheapest = array[0][array.length-1];    //Initializing cheapest path to from 0 to n
        int storage[] = new int[array.length-2];    //Storage that will contain for testing path
                                                    //Storage points from array's 1~n-1, so it's off by 1
        for(int j = 0; j < array.length-2; j++)     //Setting path to zero
            shortest[j] = 0;

        for(long i = 1; i < Math.pow(2, array.length-2); i++) { //Loop 2^n times, brute force every possibility
            int cost = 0;                                       //cost which keeps track new path's cost
            int pointer = 0;                                    //Pointer which keeps track row
            //Setting up bits
            for(int j = 0; j < array.length-2; j++) {           //Checking if Jth bit is active
                if((i & (1L << j)) != 0) {
                    storage[j] = 1;
                    cost += array[pointer][j+1];                //Add path's cost
                    pointer = j+1;                              //jump the row
                } else
                    storage[j]=0;
            }

            cost += array[pointer][array.length-1];             //Add final path cost
            if(cost < cheapest) {                               //Check if cost is cheaper than before
                cheapest = cost;
                for(int j = 0; j < array.length-2; j++) //rewrite path, but array is pointer so writing one by one
                    shortest[j] = storage[j];
            }
        }
        //Printing result of brute force
        System.out.println("Brute Force : " + cheapest);
        System.out.print("Brute Force Path : [0");
        for(int j = 0; j < array.length-2; j++)
            if(shortest[j] == 1)
                System.out.print(" -> " + (j+1));
        System.out.println(" -> " + (array.length-1) + "]");
    }

    public static void divideAndConquer(int[][] array) {
        Datapath n = new tcss343().divideAndConquer(array, array.length - 1);
        System.out.println("Divide and Conquer: "+ n.value);
        System.out.println("Divide and Conquer Path: " + n.path);
    }

    public Datapath divideAndConquer(int[][] array, int point) {
        int min = array[0][point];
        String path = Integer.toString(point)+" ";
        for(int i = 1; i < point; i++) {
            Datapath data = divideAndConquer(array, i);
            int temp = data.value + array[i][point];
            if(temp < min) {
                min = temp;
                path = data.path + Integer.toString(point) + " ";
            }
        }
        return  new Datapath(min, path);
    }

    public static void dynamic(int[][] array) {
        int storage[][] = new int[2][array.length];
        for(int i = 0; i < array.length; i++) { //Finding lowest cost with dynamic programming approach
            int shortest = array[0][i];         //Initialize shortest to get to ith element is from 0th
            int used = 0;                       //Storing path
            for(int j = 1; j < i; j++) {        //iterate through each possible path to ith element
                int tempshort = array[j][i] + storage[0][j];
                if(tempshort < shortest) {      //Found lower cost
                    shortest = tempshort;       //Set shortest to new lower cost
                    used = j;                   //Store new path
                }
            }                                   //End of iteration
            storage[0][i]=shortest;             //Store lowest cost
            storage[1][i]=used;                 //Store path
        }                                       //End of finding lowest cost
        System.out.println("Dynamic Programming: "+storage[0][array.length-1]); //Printing Lowest cost

        // Grabing Path
        Stack path = new Stack();               //Stack that will store path start from nth to 0th
        int pointer = array.length - 1;         //Pointer which points to current element starting from nth
        while(pointer != 0) {                   //Iterate until pointer reaches 0
            path.push(pointer);                 //store which path has be used
            pointer = storage[1][pointer];
        }                                       //End of iteration, now stack has full path to nth
        //Start printing Path
        System.out.print("Dynamic Programming Path: ");
        while(!path.isEmpty())                  //Print until stack is empty.
            System.out.print(path.pop() + " ");
        System.out.println();
    }

    /**
     * readFile method reads file from given string name and converts to 2D array.
     * @param filename String name of file
     * @return two dimensional array with "NA" converted to -1
     * @throws IOException from BufferedReader
     */
    private static int[][] readFile(String filename) throws IOException {
        String split = "\t";
        String na = "NA";
        BufferedReader reader = new BufferedReader( new FileReader(filename));
        int linecounter = 1;    //linecounter tracks of line. It is set to 1 because 0th line will be handled manually
        String line = reader.readLine();   //Read first line. This is required to fiqure out size of n and create array
        String temp[] = line.split(split);  //Split string by tab
        int array[][] = new int[temp.length][temp.length];  //Create n x n array

        for(int i = 0; i < temp.length; i++) {              //Since first line is read, put info into array
            if(temp[i].compareTo(na) == 0) {
                array[0][i] = -1;
            } else {
                array[0][i] = Integer.parseInt(temp[i]);
            }
        }

        while((line = reader.readLine()) != null) {         //Read until end of file
            temp = line.split(split);
            for(int i = 0; i < temp.length; i++) {
                if(temp[i].compareTo(na) == 0) {
                    array[linecounter][i] = -1;
                } else {
                    array[linecounter][i] = Integer.parseInt(temp[i]);
                }
            }
            linecounter++;
        }                                                   //End of reading file
        reader.close();                                     //Close buffered reader
        return array;                                       //Return 2D array
    }

    /**
     * Datapath is being used by bruteforce method. Datapath is required only because bruthforce needs to return
     * two value (int value, String path).
     */
    private class Datapath {
        private int value;
        private String path;
        private Datapath(int value, String path) {
            this.value = value;
            this.path = path;
        }
    }
}
