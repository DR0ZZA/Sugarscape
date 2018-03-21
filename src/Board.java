import java.util.Random;

public class Board {

    private final int numAgents;
    private Square[][] grid   = new Square[50][50];
    private Agent[] agents;

    private Random generator = new Random();
    
    public Board(int numAgents) {
        
        this.numAgents = numAgents;
        agents = new Agent[numAgents];

                        
        for (int y = 0 ; y < 50 ; y++) {
            for (int x = 0 ; x < 50 ; x++) {
                grid[49-y][x] = new Square(x, y, 0, 0, Occupier.none);
            }
        }
        
        for (int i = 0 ; i < numAgents ; i++) {
            int agentGridY = generator.nextInt(49);
            int agentGridX = generator.nextInt(49);
            grid[agentGridY][agentGridX].setOccupier(Occupier.agent);
            agents[i] = new Agent(this, grid[agentGridY][agentGridX]);
        }
        
        //mountains bot left
        createSugarCircle(11, 30, 1);
        createSugarCircle(12, 27, 2);
        createSugarCircle(13, 24, 3);
        createSugarCircle(14, 21, 4);
        //mountains bot right
        createSugarCircle(29, 48, 1);
        createSugarCircle(30, 45, 2);
        createSugarCircle(31, 42, 3);
        createSugarCircle(32, 39, 4);
        
    }

    private void createSugarCircle(int circleGenStartX, int circleGenStartY, int sugarValue) {

        int modifier = 0;

        if (sugarValue == 4) {
            modifier = 4;
        } else if (sugarValue == 3) {
            modifier = 6;
        } else if (sugarValue == 2) {
            modifier = 8;
        } else if (sugarValue == 1) {
            modifier = 10;
        } else {}
            
        int edge = modifier-1;
        int topEndX = 0;
        int topEndY = 0;
        int middleEndX = 0;
        int middleEndY = 0;
        
        for (int lineDifferenceXY = 0 ; lineDifferenceXY <= edge ; lineDifferenceXY++) {                                // Circle top
            for (int x = (circleGenStartX-lineDifferenceXY) ; x <= ((circleGenStartX+edge)+lineDifferenceXY) ; x++) {
                if (50-(circleGenStartY-lineDifferenceXY) >= 0 && 50-(circleGenStartY-lineDifferenceXY) < 50
                    && x >= 0 && x < 50) {
                    grid[50-(circleGenStartY-lineDifferenceXY)][x].setSugar(sugarValue);
                    topEndY = circleGenStartY-lineDifferenceXY;
                    topEndX = x;
                }
            }
        }
        for (int diffY = 1 ; diffY < edge ; diffY++) {                                                                  // Circle middle
            for (int x = (circleGenStartX-edge) ; x <= (topEndX) ; x++) {
                if (50-(topEndY-diffY) >= 0 && 50-(topEndY-diffY) < 50
                    && x >= 0 && x < 50) {
                    grid[50-(topEndY-diffY)][x].setSugar(sugarValue);
                    middleEndY = topEndY-diffY;
                    middleEndX = x;
                }
            }
        }
        for (int lineDifferenceXY = edge ; lineDifferenceXY >= 0 ; lineDifferenceXY--) {                                // Circle bot
            for (int x = (circleGenStartX-lineDifferenceXY) ; x <= (circleGenStartX+edge)+lineDifferenceXY ; x++) {
                if (50-((middleEndY-1)-(edge-lineDifferenceXY)) >= 0 && 50-((middleEndY-1)-(edge-lineDifferenceXY)) < 50
                    && x >= 0 && x < 50) {
                    grid[50-((middleEndY-1)-(edge-lineDifferenceXY))][x].setSugar(sugarValue);
                }
            }
        }
    }

    public Square getSquare(int x, int y) {
        return grid[49-y][x];
    }

    public Agent[] getAgents() {
        return agents;
    }

    public boolean areAllAgentsDead() {
        int numAgentsDead = 0;
        for (int i = 0 ; i < numAgents ; i++) {
            if (!agents[i].isAlive()) {
                numAgentsDead++;
            }
        }
        return numAgentsDead == numAgents;
    }

    public void applyMove(Move move) {  // assuming alive
        
        if (!move.getTo().equals(move.getFrom())) {
            grid[49-move.getTo().getY()][move.getTo().getX()] = move.getTo();
            
            grid[49-move.getFrom().getY()][move.getFrom().getX()] = move.getFrom();
            grid[49-move.getFrom().getY()][move.getFrom().getX()].resetSugar();
            
            getSquare(move.getTo().getX(), move.getTo().getY()).setOccupier(Occupier.agent);
            getSquare(move.getFrom().getX(), move.getFrom().getY()).setOccupier(Occupier.none);
        } else {
            grid[49-move.getFrom().getY()][move.getFrom().getX()].resetSugar();
        }
    }

    public void applyAllMovesThenRegenSugar() {
        for (int i = 0 ; i < numAgents ; i++) {
            if (!agents[i].isAlive()) {
                agents[i].getSquare().setOccupier(Occupier.none);
                //agents[i].getSquare().setSugar(agents[i].getStomach());
            } else {
                agents[i].makeMove();
            }
        }
        regenerateSugar();
    }

    public void regenerateSugar() {

        for (int y = 0 ; y < 50 ; y++) {
            for (int x = 0 ; x < 50 ; x++) {
                grid[49-y][x].incrementSugar();
            }
        }
    }
        
    public void display() {

        System.out.println();
        System.out.println("    " + new String(new char[5]).replace("\0",  "0 1 2 3 4 5 6 7 8 9 ") + "   ");
        System.out.println();

        for (int y = 50 ; y >= 1 ; y--) {
            System.out.print(((y-1) % 10) + "   ");
            for (int x = 50 ; x >= 1 ; x--) {
                if (grid[50-y][50-x].occupiedBy() == Occupier.none && grid[50-y][50-x].getSugar() == 0 ) {
                    System.out.print(". ");
                } else if (grid[50-y][50-x].occupiedBy() == Occupier.agent) {
                    System.out.print("# ");
                } else {
                    System.out.print(grid[50-y][50-x].getSugar() + " ");
                }
            }
            System.out.println("   " + ((y-1)%10));
        }
        System.out.println();
        System.out.println("    " + new String(new char[5]).replace("\0",  "0 1 2 3 4 5 6 7 8 9 ") + "   ");
        //System.out.println();
        //System.out.println();
        
    }

   

    //private String repeatString(String) {
        
                
                

}
