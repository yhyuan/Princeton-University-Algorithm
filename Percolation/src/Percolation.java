import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean [][] grid;
    private int size;
    private WeightedQuickUnionUF uf;
    public Percolation(int N) {              // create N-by-N grid, with all sites blocked
        if (N <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        grid = new boolean[N][N];
        uf = new WeightedQuickUnionUF(N*N);
        size = N;
        for (int i = 0; i <= N-1; i++) {
            for (int j = 0; j <= N-1; j++) {
                grid[i][j] = false;             
            }
        }
    }
    public void open(int m, int n) {         // open site (row i, column j) if it is not already1   ``````` 
        int i = m - 1;
        int j = n - 1;
        if ((i < 0) || (j < 0) || (i >= size) || (j >= size)) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        if (grid[i][j]) {
            return;
        }
        grid[i][j] = true;
        int index = size*i + j;
        if ((i > 0) && (grid[i-1][j])) { //  
            uf.union(index, size*(i-1) + j);
        }
        if ((i < size-1) && (grid[i+1][j])) { // 
            uf.union(index, size*(i+1) + j); 
        }
        if ((j > 0) && (grid[i][j-1])) {  //  
            uf.union(index, size*i + j - 1);
        }
        if ((j < size-1) && (grid[i][j+1])) { //  
            uf.union(index, size*i + j + 1);
        }
    }
    public boolean isOpen(int i, int j) {    // is site (row i, column j) open?
        if ((i < 1) || (j < 1) || (i > size) || (j > size)) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        return grid[i-1][j-1];
    }
    public boolean isFull(int i, int j) {    // is site (row i, column j) full?
        if ((i < 1) || (j < 1) || (i > size) || (j > size)) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        if (!grid[i-1][j-1]) {
            return false;
        }
        int index = size * (i - 1) + (j - 1);
        for (int k = 0; k <= size-1; k++) {
            if (grid[0][k] && uf.connected(k, index)) {
                return true;
            }
        }
        return false;
    }
    public boolean percolates() {            // does the system percolate?
        if (size == 1) {
            return grid[0][0];
        }
        for (int i = 0; i <= size-1; i++) {
            int index1 = i;
            for (int j = 0; j <= size-1; j++) {
                int index2 = size*(size-1) + j;
                if (uf.connected(index1, index2)) {
                    return true;
                }
            }
        }       
        return false;
    }
    public static void main(String[] args) {
        Percolation p = new Percolation(1);
        //p.open(1, 1);
        System.out.println("percolates = " + p.percolates());
        //System.out.println(p.percolate());
    }
}