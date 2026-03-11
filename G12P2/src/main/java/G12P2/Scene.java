package G12P2;

import java.util.ArrayList;

public class Scene {

    private final int[][] grid;
    private final int[][] posCamaras;

    public Scene(int[][] grid) {
        this.grid = grid;
        this.posCamaras = extraerCamaras();
    }

    // extrae las cámaras (-1) del grid.
    private int[][] extraerCamaras() {
        ArrayList<int[]> pos = new ArrayList<>();
        for (int r = 0; r < grid.length; r++) for (
            int c = 0;
            c < grid[r].length;
            c++
        ) if (grid[r][c] == -1) pos.add(new int[] { r, c });
        return pos.toArray(new int[0][]);
    }

    public int getCols() {
        return grid.length > 0 ? grid[0].length : 0;
    }

    public int getRows() {
        return grid.length;
    }

    public int[][] getGrid() {
        return grid;
    }

    public int[][] getPosCamaras() {
        return posCamaras;
    }

    public int getNumCamaras() {
        return posCamaras.length;
    }

    public boolean isWall(int row, int col) {
        return grid[row][col] == 0;
    }

    public boolean isCamera(int row, int col) {
        return grid[row][col] == -1;
    }
}
