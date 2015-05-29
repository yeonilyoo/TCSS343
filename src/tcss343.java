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
            array = readFile(args[0]);  //Taking argument is requirment
            //* PRINT FULL ARRAY
            for(int i = 0; i < array.length; i++) {
                for (int j = 0; j < array.length; j++)
                    System.out.print(array[i][j] + "\t");
                System.out.println();
            }
            //*/

            dynamic(array);
            bruteforce(array);
        } catch(IOException e) {
            System.out.println(e);
        }
    }

    public static void bruteforce(int[][] array) {
        Datapath n = new tcss343().bruteforce(array, array.length - 1);
        System.out.println("Brute Force: "+ n.value);
        System.out.println("Brute Force Path: " + n.path);
    }

    public Datapath bruteforce(int[][] array, int point) {
        int min = array[0][point];
        String path = Integer.toString(point)+" ";
        for(int i = 1; i < point; i++) {
            Datapath data = bruteforce(array, i);
            int temp = data.value + array[i][point];
            if(temp < min) {
                min = temp;
                path = data.path + Integer.toString(point);
            }
        }
        return  new Datapath(min, path);
    }

    public static void divideconquer(int[][] array) {
        int storage[][] = new int[2][array.length];
        for(int i = 0; i < array.length;i++){
            for(int j=i; j < array.length; j++) {

            }
        }
    }

    public static void dynamic(int[][] array) {
        int storage[][] = new int[2][array.length];
        for(int i = 0; i < array.length; i++) {
            int shortest = array[0][i];
            int used = 0;
            for(int j = 1; j < i; j++) {
                int tempshort = array[j][i] + storage[0][j];
                if(tempshort < shortest) {
                    shortest = tempshort;
                    used = j;
                }
            }
            storage[0][i]=shortest;
            storage[1][i]=used;
        }
        System.out.println("Dynamic Programming: "+storage[0][array.length-1]);

        // Grabing Path
        Stack path = new Stack();
        int pointer = array.length - 1;
        while(pointer != 0) {
            path.push(pointer);
            pointer = storage[1][pointer];
        }
        System.out.print("Dynamic Programming Path: ");
        while(!path.isEmpty())
            System.out.print(path.pop() + " ");
        System.out.println();
    }

    private static int[][] readFile(String filename) throws IOException {
        BufferedReader reader = new BufferedReader( new FileReader(filename));
        int linecounter = 1;
        String line = reader.readLine();
        String temp[] = line.split("\t");
        int array[][] = new int[temp.length][temp.length];

        for(int i = 0; i < temp.length; i++) {
            if(temp[i].compareTo("NA") == 0) {
                array[0][i] = -1;
            } else {
                array[0][i] = Integer.parseInt(temp[i]);
            }
        }

        while((line = reader.readLine()) != null) {
            temp = line.split("\t");
            for(int i = 0; i < temp.length; i++) {
                if(temp[i].compareTo("NA") == 0) {
                    array[linecounter][i] = -1;
                } else {
                    array[linecounter][i] = Integer.parseInt(temp[i]);
                }
            }
            linecounter++;
        }
        reader.close();
        return array;
    }


    private class Datapath {
        private int value;
        private String path;
        private Datapath(int value, String path) {
            this.value = value;
            this.path = path;
        }
    }
}
