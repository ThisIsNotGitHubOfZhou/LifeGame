package life;

public class Life {
    private int maxRow;
    private int maxCol;
 
    private int[][] grid;
 
    public Life(int maxRow, int maxCol) {
        this.maxRow = maxRow;
        this.maxCol = maxCol;
        grid = new int[maxRow + 2][maxCol + 2];//��������Ϊʲô�Ӷ�����Ϊ���Բ����Ǳ߿����
        for (int row = 0; row <= maxRow + 1; row++)//��ʼ������
            for (int col = 0; col <= maxCol + 1; col++)
                grid[row][col] = 0;
    }
 
    public int[][] getGrid() {//��װ
        return grid;
    }
 
    public void setGrid(int[][] grid) {//��װ
        this.grid = grid;
    }
 
    public void update() {//��������
        int[][] newGrid = new int[maxRow + 2][maxCol + 2];
 
        for (int row = 1; row <= maxRow; row++)
            for (int col = 1; col <= maxCol; col++)
                switch (getNeighborCount(row, col)) {
                    case 2:
                        newGrid[row][col] = grid[row][col]; // Status stays the same.����
                        break;
                    case 3:
                        newGrid[row][col] = 1; // Cell is alive.����ȥ�򸴻�
                        break;
                    default:
                        newGrid[row][col] = 0; // Cell is dead.
                }
 
        for (int row = 1; row <= maxRow; row++)
            for (int col = 1; col <= maxCol; col++)
                grid[row][col] = newGrid[row][col];
    }
 
    private int getNeighborCount(int row, int col) {//�õ��ھ����
        int count = 0;
 
        for (int i = row - 1; i <= row + 1; i++)
            for (int j = col - 1; j <= col + 1; j++)
                count += grid[i][j]; // Increase the count if neighbor is alive.
        count -= grid[row][col]; // Reduce count, since cell is not its own neighbor.
 
        return count;
    }
}
