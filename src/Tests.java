import java.util.Arrays;

public class Tests {

    public static void main(String[] args) {

        //testBoardDisplay();
        
        //testBoardDisplayAndAgents();

        //testBoardMoveRegen();

        testSystem();
    }

    static void testBoardDisplay() {
        Board grid = grid1();
        grid.display();
        
    }

    static void testBoardDisplayAndAgents() {
        Board grid = grid1();
        grid.display();
        System.out.println(Arrays.toString(grid.getAgents()));

    }

    static void testBoardMoveRegen() {
        Board grid = grid1();
        for (int i = 0 ; i < 4 ; i++) {
            System.out.println(Arrays.toString(grid.getAgents()));
            grid.display();
            grid.applyAllMovesThenRegenSugar();
        }
    }

    static void testSystem() {
        Board grid = grid1();
        try {
            while (!grid.areAllAgentsDead()) {
                grid.display();
                grid.applyAllMovesThenRegenSugar();
                Thread.sleep(400);
                //System.out.println(Arrays.toString(grid.getAgents()));
                //Thread.sleep(1000);
            }
            grid.display();
            System.out.println(Arrays.toString(grid.getAgents()));
        } catch (InterruptedException ex) {}
    }

    public static Board grid1() {
        Board grid = new Board(1);
        return grid;
    }

    public static Board grid2() {
        Board grid = new Board(250);
        return grid;
    }
        

}
