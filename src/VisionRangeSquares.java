import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.*;
import java.util.Arrays;

public class VisionRangeSquares {

    private final Board grid;
    private final int vision;
    private final Square initSquare;
    private final Square[][] visionSquares;
    private static int length;

    private static Random generator;
    
    public VisionRangeSquares(Board grid, int vision, Square initSquare) {
        this.grid = grid;
        this.vision = vision;
        this.initSquare = initSquare;
        visionSquares = findVisionSquares(vision, initSquare);
        length = 0;
    }
    private Square[][] findVisionSquares(int vision, Square initSquare) {
        Square[][] result = new Square[4][vision];
        for (int i = 1 ; i <= vision ; i++) {
            if (initSquare.getY()+i < 50 && grid.getSquare(initSquare.getX(), initSquare.getY()+i).occupiedBy() != Occupier.agent) {
                result[0][i-1] = grid.getSquare(initSquare.getX(), initSquare.getY()+i);
            } else {
                result[0][i-1] = null;
            }
            if (initSquare.getX()+i < 50 && grid.getSquare(initSquare.getX()+i, initSquare.getY()).occupiedBy() != Occupier.agent) {
                result[1][i-1] = grid.getSquare(initSquare.getX()+i, initSquare.getY());
            } else {
                result[1][i-1] = null;
            }
            if (initSquare.getY()-i >= 0 && grid.getSquare(initSquare.getX(), initSquare.getY()-i).occupiedBy() != Occupier.agent) {
                result[2][i-1] = grid.getSquare(initSquare.getX(), initSquare.getY()-i);
            } else {
                result[2][i-1] = null;
            }
            if (initSquare.getX()-i >= 0 && grid.getSquare(initSquare.getX()-i, initSquare.getY()).occupiedBy() != Occupier.agent) {
                result[3][i-1] = grid.getSquare(initSquare.getX()-i, initSquare.getY());
            } else {
                result[3][i-1] = null;
            }
        }
        this.length = result[0].length;
        return result;
    }

    public Square[] traverseAllDirectionsAndReturnNESW(int index) {
        Square[] result = new Square[4];
        for (int i = 0 ; i < vision ; i++) {
            result[0] = visionSquares[0][i];
            result[1] = visionSquares[1][i];
            result[2] = visionSquares[2][i];
            result[3] = visionSquares[3][i];
        }
        return result;
    }

    // -1 means null
    /* public static int returnMaxSugarDirection(int[][] sugarMatrix) {

        int result = -1;
        int[] maxInts = new int[4];
        generator = new Random()
        //List<Boolean> D = new ArrayList<Integer>();    
        if (maxInt(sugarMatrix[0]) == maxInt(sugarMatrix[1]) && maxInt(sugarMatrix[0]) == maxInt(sugarMatrix[2]) && maxInt(sugarMatrix[0]) == maxInt(sugarMatrix[3])) {
                generator.nextInt(4);
        } else if
           (maxInt(sugarMatrix[0]) == maxInt(sugarMatrix[1]) && maxInt(sugarMatrix[0]) == maxInt(sugarMatrix[2]) && maxInt(sugarMatrix[0]) != maxInt(sugarMatrix[3])) {
            


            //maxInts[i] = maxInt(sugarMatrix[i]);
        }
        
        for (int i = 0 ; i < 4 ; i++) {
            if (maxInt(maxInts) == maxInts[i]) {
                result = i;
            }
        }
        
        return result;
        } */

    // -1 implies ... something
    public static int returnMaxSugarDirection(VisionRangeSquares sugarMatrix) {
        
        int max = -1;
        
        for (int i = 0 ; i < length ; i++) {
            if (maxInt((Arrays.stream(sugarMatrix.traverseAllDirectionsAndReturnNESW(i)).mapToInt(item -> item.getSugar())).toArray()) > max) {
                max = i;
            }
        }
        return max;
    }
            
    
    private static int maxInt(int[] array) {
        int max = array[0];
        for (int i = 0 ; i < array.length ; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    private static int randomDirection() {
        generator = new Random();
        return generator.nextInt(4);
    }
    
}
