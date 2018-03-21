import java.util.*;

public class Agent {

    private Board grid;
    private Square square;
    private int vision;
    private int stomach;
    private int age;
    private int maxAge;
    private int metabolism;

    private Random generatorVision = new Random();
    private Random generatorMaxAge = new Random();
    private Random generatorMetabolism = new Random();
    private Random generatorInitStomach = new Random();

    private Random generatorRandomNumber = new Random();
    
    public Agent(Board grid, Square square) {
        this.grid   = grid;
        this.square = square;
        vision     = generatorVision.nextInt(7)+1;
        stomach    = generatorInitStomach.nextInt(9)+1;
        age        = 0;
        maxAge     = generatorMaxAge.nextInt(100);
        metabolism = generatorMetabolism.nextInt(3)+1;
    }

    public Square aquireTarget() {
        Square target = square;
        for (int i = vision; i >= 1; i--) {
            int[] row = new int[]{-i, -i, i, i};
            int[] col = new int[]{-i, i, -i, i};
            List<Square> possibleTargets = new ArrayList<>();
            for (int j = 0 ; j < 4 ; j++) {
                int potentialTargetX = square.getX()+col[j];
                int potentialTargetY = square.getY()+row[j];
                if (isWithinBounds(potentialTargetX, potentialTargetY)) {
                    if (grid.getSquare(potentialTargetX, potentialTargetY).occupiedBy() != Occupier.agent) {
                        possibleTargets.add(grid.getSquare(potentialTargetX, potentialTargetY));
                    }
                }
            }
            Collections.shuffle(possibleTargets);
            if (!possibleTargets.isEmpty()) {
                int targetSugar = target.getSugar();
                if (possibleTargets.stream().anyMatch(s -> s.getSugar() > targetSugar)) {
                    target = possibleTargets.stream().filter(a ->
                            a.getSugar() == (possibleTargets.stream().map(s -> s.getSugar()).max(Integer::max)).get()).findFirst().get();
                }
                if (possibleTargets.stream().allMatch(s -> s.getSugar() == 0)) {
                    break;
                }
            }
        }
        /*Random generator = new Random();
                int randomIndex = generator.nextInt(4);
                if (isWithinBounds(square.getX()+col[randomIndex], square.getY()+row[randomIndex])) {
                    if (grid.getSquare(target.getX()+col[randomIndex], target.getY()+row[randomIndex]).getSugar() > target.getSugar()) {
                        target = grid.getSquare(target.getX()+col[randomIndex], target.getY()+row[randomIndex]);
                        break;
                    }
                }*/
        /*Random generator = new Random();
        if (target.getSugar() == 0) {
            for (int i = vision ; i >= 0 ; i--) {
                int[] row = new int[]{-i, -i, i, i};
                int[] col = new int[]{-i, i, -i, i};
                int randomCoord = generator.nextInt(4);
                if (isWithinBounds(square.getX() + col[randomCoord], square.getY() + row[randomCoord])) {
                    target = grid.getSquare(square.getX() + col[randomCoord], square.getY() + row[randomCoord]);
                }
            }
        }*/
        return target;
    }
    private int ensureRange(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }
    private boolean isWithinBounds(int x, int y) {
        return (x >= 0 && x <= 49) && (y >= 0 && y <= 49);
    }

        /*Square[] visionRangeN = new Square[vision];
        Square[] visionRangeE = new Square[vision];
        Square[] visionRangeS = new Square[vision];
        Square[] visionRangeW = new Square[vision];

        for (int i = 1 ; i <= vision ; i++) {
            if (square.getY()+i < 50) {
                visionRangeN[i-1] = grid.getSquare(square.getX(), square.getY()+i);
            } else {
                visionRangeN[i-1] = null;
            }
            if (square.getX()+i < 50) {
                visionRangeE[i-1] = grid.getSquare(square.getX()+i, square.getY());
            } else {
                visionRangeE[i-1] = null;
            }
            if (square.getY()-i >= 0) {
                visionRangeS[i-1] = grid.getSquare(square.getX(), square.getY()-i);
            } else {
                visionRangeS[i-1] = null;
            }
            if (square.getX()-i >= 0) {
                visionRangeW[i-1] = grid.getSquare(square.getX()-i, square.getY());
            } else {
                visionRangeW[i-1] = null;
            }
        }

        int[] sugarsN = new int[vision];
        int[] sugarsE = new int[vision];
        int[] sugarsS = new int[vision];
        int[] sugarsW = new int[vision];

        for (int i = 0 ; i < vision ; i++) {
            if (visionRangeN[i] == null || visionRangeN[i].occupiedBy() == Occupier.agent) {
                sugarsN[i] = -1;
            } else {
                sugarsN[i] = visionRangeN[i].getSugar();
            }
            if (visionRangeE[i] == null || visionRangeE[i].occupiedBy() == Occupier.agent) {
                sugarsE[i] = -1;
            } else {
                sugarsE[i] = visionRangeE[i].getSugar();
            }
            if (visionRangeS[i] == null || visionRangeS[i].occupiedBy() == Occupier.agent) {
                sugarsS[i] = -1;
            } else {
                sugarsS[i] = visionRangeS[i].getSugar();
            }
            if (visionRangeW[i] == null || visionRangeW[i].occupiedBy() == Occupier.agent) {
                sugarsW[i] = -1;
            } else {
                sugarsW[i] = visionRangeW[i].getSugar();
            }
        }

        int[] maxSugars = new int[4];

        maxSugars[0] = maxInt(sugarsN);
        maxSugars[1] = maxInt(sugarsE);
        maxSugars[2] = maxInt(sugarsS);
        maxSugars[3] = maxInt(sugarsW);

        int targetIndex = 0;
        Square target = null;

        int randomNum  = generatorRandomNumber.nextInt(4);
        int randomNum3 = generatorRandomNumber.nextInt(3);

        if (maxInt(maxSugars) == maxInt(sugarsN) && maxInt(maxSugars) != maxInt(sugarsE)
            && maxInt(maxSugars) != maxInt(sugarsS) && maxInt(maxSugars) != maxInt(sugarsW)) {
            for (int i = 0 ; i < vision ; i++) {
                if (maxInt(sugarsN) == sugarsN[i]) {
                    targetIndex = i;
                }
            }
            target = visionRangeN[targetIndex];
        } else if (maxInt(maxSugars) == maxInt(sugarsE) && maxInt(maxSugars) != maxInt(sugarsN)
                   && maxInt(maxSugars) != maxInt(sugarsS) && maxInt(maxSugars) != maxInt(sugarsW)) {
            for (int i = 0 ; i < vision ; i++) {
                if (maxInt(sugarsE) == sugarsE[i]) {
                    targetIndex = i;
                }
            }
            target = visionRangeE[targetIndex];
        } else if (maxInt(maxSugars) == maxInt(sugarsS) && maxInt(maxSugars) != maxInt(sugarsN)
                   && maxInt(maxSugars) != maxInt(sugarsE) && maxInt(maxSugars) != maxInt(sugarsW)) {
            for (int i = 0 ; i < vision ; i++) {
                if (maxInt(sugarsS) == sugarsS[i]) {
                    targetIndex = i;
                }
            }
            target = visionRangeS[targetIndex];
        } else if (maxInt(maxSugars) == maxInt(sugarsW) && maxInt(maxSugars) != maxInt(sugarsN)
                   && maxInt(maxSugars) != maxInt(sugarsE) && maxInt(maxSugars) != maxInt(sugarsS)) {
            for (int i = 0 ; i < vision ; i++) {
                if (maxInt(sugarsW) == sugarsW[i]) {
                    targetIndex = i;
                }
            }
            target = visionRangeW[targetIndex];
        } else {
            if (maxInt(maxSugars) == maxInt(sugarsN) && maxInt(maxSugars) == maxInt(sugarsE)
                && maxInt(maxSugars) != maxInt(sugarsS) && maxInt(maxSugars) != maxInt(sugarsW)) {
                if (randomNum <= 1) {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsN) == sugarsN[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeN[targetIndex];
                } else {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsE) == sugarsE[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeE[targetIndex];
                }
            } else if (maxInt(maxSugars) == maxInt(sugarsN) && maxInt(maxSugars) == maxInt(sugarsS)
                       && maxInt(maxSugars) != maxInt(sugarsE) && maxInt(maxSugars) != maxInt(sugarsW)) {
                if (randomNum <= 1) {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsN) == sugarsN[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeN[targetIndex];
                } else {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsS) == sugarsS[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeS[targetIndex];
                }
            } else if (maxInt(maxSugars) == maxInt(sugarsN) && maxInt(maxSugars) == maxInt(sugarsW)
                       && maxInt(maxSugars) != maxInt(sugarsE) && maxInt(maxSugars) != maxInt(sugarsS)) {
                if (randomNum <= 1) {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsN) == sugarsN[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeN[targetIndex];
                } else {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsW) == sugarsW[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeW[targetIndex];
                }
            } else if (maxInt(maxSugars) == maxInt(sugarsE) && maxInt(maxSugars) == maxInt(sugarsS)
                        && maxInt(maxSugars) != maxInt(sugarsN) && maxInt(maxSugars) != maxInt(sugarsW)) {
                if (randomNum <= 1) {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsE) == sugarsE[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeE[targetIndex];
                } else {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsS) == sugarsS[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeS[targetIndex];
                }
            } else if (maxInt(maxSugars) == maxInt(sugarsE) && maxInt(maxSugars) == maxInt(sugarsW)
                        && maxInt(maxSugars) != maxInt(sugarsN) && maxInt(maxSugars) != maxInt(sugarsS)) {
                if (randomNum <= 1) {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsE) == sugarsE[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeE[targetIndex];
                } else {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsW) == sugarsW[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeW[targetIndex];
                }
            } else if (maxInt(maxSugars) == maxInt(sugarsS) && maxInt(maxSugars) == maxInt(sugarsW)
                       && maxInt(maxSugars) != maxInt(sugarsN) && maxInt(maxSugars) != maxInt(sugarsE)) {
                if (randomNum <= 1) {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsS) == sugarsS[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeS[targetIndex];
                } else {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsW) == sugarsW[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeW[targetIndex];
                }
            } else if (maxInt(maxSugars) == maxInt(sugarsN) && maxInt(maxSugars) == maxInt(sugarsE) && maxInt(maxSugars) == maxInt(sugarsS)
                       && maxInt(maxSugars) != maxInt(sugarsW)) {
                if (randomNum3 == 0) {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsN) == sugarsN[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeN[targetIndex];
                } else if (randomNum3 == 1) {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsE) == sugarsE[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeE[targetIndex];
                } else {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsS) == sugarsS[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeS[targetIndex];
                }
            } else if (maxInt(maxSugars) == maxInt(sugarsN) && maxInt(maxSugars) == maxInt(sugarsE) && maxInt(maxSugars) == maxInt(sugarsW)
                       && maxInt(maxSugars) != maxInt(sugarsS)) {
                if (randomNum3 == 0) {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsN) == sugarsN[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeN[targetIndex];
                } else if (randomNum3 == 1) {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsE) == sugarsE[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeE[targetIndex];
                } else {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsW) == sugarsW[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeW[targetIndex];
                }
            } else if (maxInt(maxSugars) == maxInt(sugarsE) && maxInt(maxSugars) == maxInt(sugarsS) && maxInt(maxSugars) == maxInt(sugarsW)
                       && maxInt(maxSugars) != maxInt(sugarsN)) {
                if (randomNum3 == 0) {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsE) == sugarsE[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeE[targetIndex];
                } else if (randomNum3 == 1) {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsS) == sugarsS[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeS[targetIndex];
                } else {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsW) == sugarsW[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeW[targetIndex];
                }
            } else if (maxInt(maxSugars) == maxInt(sugarsN) && maxInt(maxSugars) == maxInt(sugarsE) && maxInt(maxSugars) == maxInt(sugarsS) && maxInt(maxSugars) == maxInt(sugarsW)) {
                if (randomNum == 0) {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsN) == sugarsN[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeN[targetIndex];
                } else if (randomNum == 1) {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsE) == sugarsE[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeE[targetIndex];
                } else if (randomNum == 2) {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsS) == sugarsS[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeS[targetIndex];
                } else if (randomNum == 3) {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsW) == sugarsW[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeW[targetIndex];
                }
            } else { 
                if (randomNum == 0) {
                    target = visionRangeN[vision-1];
                } else if (randomNum == 1) {
                    target = visionRangeE[vision-1];
                } else if (randomNum == 2) {
                    target = visionRangeS[vision-1];
                } else if (randomNum == 3) {
                    target = visionRangeW[vision-1];
                } 
            } 
        }
        return target;
    }*/

    


















/*VisionRangeSquares visionSquaresMatrix = new VisionRangeSquares(grid, vision, square);
        VisionRangeSquares sugarsMatrix = new VisionRangeSquares(grid, vision, square);
        
        int maxSugarDirection = VisionRangeSquares.returnMaxSugarDirection(visionSquaresMatrix);

        /*for (int i = 0 ; i < vision ; i++) {
            for (int j = 0 ; j < 4 ; j++) {
                if (visionSquaresMatrix.traverseAllDirectionsAndReturnNESW(i)[j] != null) {
                    sugarsMatrix[j][i] = visionSquaresMatrix.traverseAllDirectionsAndReturnNESW(i)[j].getSugar();
                } else {
                    sugarsMatrix[j][i] = -1;
                }
            }
            } */

        //int maxSugarDirection = VisionRangeSquares.returnMaxSugarDirection(sugarsMatrix);

        //if (maxSugarDirection == 0) {}*/
            






      






        
        /*
        Square[] visionRangeN = new Square[vision];
        Square[] visionRangeE = new Square[vision];
        Square[] visionRangeS = new Square[vision];
        Square[] visionRangeW = new Square[vision];

        for (int i = 1 ; i <= vision ; i++) {
            if (square.getY()+i < 50) {
                visionRangeN[i-1] = grid.getSquare(square.getX(), square.getY()+i);
            } else {
                visionRangeN[i-1] = null;
            }
            if (square.getX()+i < 50) {
                visionRangeE[i-1] = grid.getSquare(square.getX()+i, square.getY());
            } else {
                visionRangeE[i-1] = null;
            }
            if (square.getY()-i >= 0) {
                visionRangeS[i-1] = grid.getSquare(square.getX(), square.getY()-i);
            } else {
                visionRangeS[i-1] = null;
            }
            if (square.getX()-i >= 0) {
                visionRangeW[i-1] = grid.getSquare(square.getX()-i, square.getY());
            } else {
                visionRangeW[i-1] = null;
            }
        }

        int[] sugarsN = new int[vision];
        int[] sugarsE = new int[vision];
        int[] sugarsS = new int[vision];
        int[] sugarsW = new int[vision];

        for (int i = 0 ; i < vision ; i++) {
            if (visionRangeN[i] == null || visionRangeN[i].occupiedBy() == Occupier.agent) {
                sugarsN[i] = -1;
            } else {
                sugarsN[i] = visionRangeN[i].getSugar();
            }
            if (visionRangeE[i] == null || visionRangeE[i].occupiedBy() == Occupier.agent) {
                sugarsE[i] = -1;
            } else {
                sugarsE[i] = visionRangeE[i].getSugar();
            }
            if (visionRangeS[i] == null || visionRangeS[i].occupiedBy() == Occupier.agent) {
                sugarsS[i] = -1;
            } else {
                sugarsS[i] = visionRangeS[i].getSugar();
            }
            if (visionRangeW[i] == null || visionRangeW[i].occupiedBy() == Occupier.agent) {
                sugarsW[i] = -1;
            } else {
                sugarsW[i] = visionRangeW[i].getSugar();
            }
        }

        int[] maxSugars = new int[4];

        maxSugars[0] = maxInt(sugarsN);
        maxSugars[1] = maxInt(sugarsE);
        maxSugars[2] = maxInt(sugarsS);
        maxSugars[3] = maxInt(sugarsW);

        int targetIndex = 0;
        Square target = null;

        int randomNum  = generatorRandomNumber.nextInt(4);
        int randomNum3 = generatorRandomNumber.nextInt(3);

        if (maxInt(maxSugars) == maxInt(sugarsN) && maxInt(maxSugars) != maxInt(sugarsE)
            && maxInt(maxSugars) != maxInt(sugarsS) && maxInt(maxSugars) != maxInt(sugarsW)) {
            for (int i = 0 ; i < vision ; i++) {
                if (maxInt(sugarsN) == sugarsN[i]) {
                    targetIndex = i;
                }
            }
            target = visionRangeN[targetIndex];
        } else if (maxInt(maxSugars) == maxInt(sugarsE) && maxInt(maxSugars) != maxInt(sugarsN)
                   && maxInt(maxSugars) != maxInt(sugarsS) && maxInt(maxSugars) != maxInt(sugarsW)) {
            for (int i = 0 ; i < vision ; i++) {
                if (maxInt(sugarsE) == sugarsE[i]) {
                    targetIndex = i;
                }
            }
            target = visionRangeE[targetIndex];
        } else if (maxInt(maxSugars) == maxInt(sugarsS) && maxInt(maxSugars) != maxInt(sugarsN)
                   && maxInt(maxSugars) != maxInt(sugarsE) && maxInt(maxSugars) != maxInt(sugarsW)) {
            for (int i = 0 ; i < vision ; i++) {
                if (maxInt(sugarsS) == sugarsS[i]) {
                    targetIndex = i;
                }
            }
            target = visionRangeS[targetIndex];
        } else if (maxInt(maxSugars) == maxInt(sugarsW) && maxInt(maxSugars) != maxInt(sugarsN)
                   && maxInt(maxSugars) != maxInt(sugarsE) && maxInt(maxSugars) != maxInt(sugarsS)) {
            for (int i = 0 ; i < vision ; i++) {
                if (maxInt(sugarsW) == sugarsW[i]) {
                    targetIndex = i;
                }
            }
            target = visionRangeW[targetIndex];
        } else {
            if (maxInt(maxSugars) == maxInt(sugarsN) && maxInt(maxSugars) == maxInt(sugarsE)
                && maxInt(maxSugars) != maxInt(sugarsS) && maxInt(maxSugars) != maxInt(sugarsW)) {
                if (randomNum <= 1) {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsN) == sugarsN[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeN[targetIndex];
                } else {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsE) == sugarsE[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeE[targetIndex];
                }
            } else if (maxInt(maxSugars) == maxInt(sugarsN) && maxInt(maxSugars) == maxInt(sugarsS)
                       && maxInt(maxSugars) != maxInt(sugarsE) && maxInt(maxSugars) != maxInt(sugarsW)) {
                if (randomNum <= 1) {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsN) == sugarsN[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeN[targetIndex];
                } else {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsS) == sugarsS[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeS[targetIndex];
                }
            } else if (maxInt(maxSugars) == maxInt(sugarsN) && maxInt(maxSugars) == maxInt(sugarsW)
                       && maxInt(maxSugars) != maxInt(sugarsE) && maxInt(maxSugars) != maxInt(sugarsS)) {
                if (randomNum <= 1) {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsN) == sugarsN[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeN[targetIndex];
                } else {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsW) == sugarsW[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeW[targetIndex];
                }
            } else if (maxInt(maxSugars) == maxInt(sugarsE) && maxInt(maxSugars) == maxInt(sugarsS)
                        && maxInt(maxSugars) != maxInt(sugarsN) && maxInt(maxSugars) != maxInt(sugarsW)) {
                if (randomNum <= 1) {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsE) == sugarsE[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeE[targetIndex];
                } else {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsS) == sugarsS[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeS[targetIndex];
                }
            } else if (maxInt(maxSugars) == maxInt(sugarsE) && maxInt(maxSugars) == maxInt(sugarsW)
                        && maxInt(maxSugars) != maxInt(sugarsN) && maxInt(maxSugars) != maxInt(sugarsS)) {
                if (randomNum <= 1) {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsE) == sugarsE[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeE[targetIndex];
                } else {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsW) == sugarsW[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeW[targetIndex];
                }
            } else if (maxInt(maxSugars) == maxInt(sugarsS) && maxInt(maxSugars) == maxInt(sugarsW)
                       && maxInt(maxSugars) != maxInt(sugarsN) && maxInt(maxSugars) != maxInt(sugarsE)) {
                if (randomNum <= 1) {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsS) == sugarsS[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeS[targetIndex];
                } else {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsW) == sugarsW[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeW[targetIndex];
                }
            } else if (maxInt(maxSugars) == maxInt(sugarsN) && maxInt(maxSugars) == maxInt(sugarsE) && maxInt(maxSugars) == maxInt(sugarsS)
                       && maxInt(maxSugars) != maxInt(sugarsW)) {
                if (randomNum3 == 0) {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsN) == sugarsN[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeN[targetIndex];
                } else if (randomNum3 == 1) {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsE) == sugarsE[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeE[targetIndex];
                } else {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsS) == sugarsS[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeS[targetIndex];
                }
            } else if (maxInt(maxSugars) == maxInt(sugarsN) && maxInt(maxSugars) == maxInt(sugarsE) && maxInt(maxSugars) == maxInt(sugarsW)
                       && maxInt(maxSugars) != maxInt(sugarsS)) {
                if (randomNum3 == 0) {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsN) == sugarsN[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeN[targetIndex];
                } else if (randomNum3 == 1) {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsE) == sugarsE[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeE[targetIndex];
                } else {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsW) == sugarsW[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeW[targetIndex];
                }
            } else if (maxInt(maxSugars) == maxInt(sugarsE) && maxInt(maxSugars) == maxInt(sugarsS) && maxInt(maxSugars) == maxInt(sugarsW)
                       && maxInt(maxSugars) != maxInt(sugarsN)) {
                if (randomNum3 == 0) {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsE) == sugarsE[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeE[targetIndex];
                } else if (randomNum3 == 1) {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsS) == sugarsS[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeS[targetIndex];
                } else {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsW) == sugarsW[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeW[targetIndex];
                }
            } else if (maxInt(maxSugars) == maxInt(sugarsN) && maxInt(maxSugars) == maxInt(sugarsE) && maxInt(maxSugars) == maxInt(sugarsS) && maxInt(maxSugars) == maxInt(sugarsW)) {
                if (randomNum == 0) {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsN) == sugarsN[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeN[targetIndex];
                } else if (randomNum == 1) {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsE) == sugarsE[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeE[targetIndex];
                } else if (randomNum == 2) {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsS) == sugarsS[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeS[targetIndex];
                } else if (randomNum == 3) {
                    for (int i = 0 ; i < vision ; i++) {
                        if (maxInt(sugarsW) == sugarsW[i]) {
                            targetIndex = i;
                        }
                    }
                    target = visionRangeW[targetIndex];
                }
            } else { 
                if (randomNum == 0) {
                    target = visionRangeN[vision-1];
                } else if (randomNum == 1) {
                    target = visionRangeE[vision-1];
                } else if (randomNum == 2) {
                    target = visionRangeS[vision-1];
                } else if (randomNum == 3) {
                    target = visionRangeW[vision-1];
                } 
            } 
        }
        return target;
    }
*/
    private int maxInt(int[] array) {
        int max = array[0];
        for (int i = 0 ; i < array.length ; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    public void decreaseStomach() {
        stomach = stomach - metabolism;
    }

    public void eatSugar(Square target) {
        stomach = stomach + target.getSugar();
    }

    public void incrementAge() {
        age++;
    }

    public void makeMove() {                  // assuming alive
        Square target = aquireTarget();
        if (isAlive() && target != null) {
            Move move = new Move(square, target);
            grid.applyMove(move);
            eatSugar(target);
            decreaseStomach();
            incrementAge();
            square = target;
        } else {
            Move move = new Move(square, square);
            grid.applyMove(move);
            eatSugar(square);
            decreaseStomach();
            incrementAge();
        }
    }

    public boolean isAlive() {
        return age <= maxAge && stomach >= 0;
    }

    public Square getSquare() {
        return square;
    }

    public int getStomach() {
        return stomach;
    }





    @Override
    public String toString() {
        int visibleStomach = 0;
        if (stomach <= 0) {
            visibleStomach = 0;
        } else {
            visibleStomach = stomach;
        }
        
        return ("Agent: " +"("+coordToString(square.getX())+", "+coordToString(square.getY())+")" + ", " + "vision: " + vision + ", " + "stomach: "
                + visibleStomach + ", " + "age: " + age + ", " + "metabolism: " + metabolism + ", " + "alive: " + isAlive() + ")" + "\n");
    }

    private String coordToString(int coord) {
        if (coord < 10) {
            return "0" + coord;
        } else {
            return Integer.toString(coord);
        }
    }
    
}
