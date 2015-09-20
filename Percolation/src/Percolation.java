import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import java.nio.file.*;
import java.io.*;

public class Percolation {
    private boolean [][] grid;
    private int size;
    private int virtualTop;
    private int virtualBottom;
    private WeightedQuickUnionUF uf;
    public Percolation(int N) {              // create N-by-N grid, with all sites blocked
        if (N <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        uf = new WeightedQuickUnionUF(N*N + 2); // N*N is the virtual top, N*N + 1 is the virtual bottom. 
        size = N;
        virtualTop = size*size;
        virtualBottom = virtualTop + 1;   
        grid = new boolean[N][N];
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
        if ((i > 0) && (grid[i-1][j])) {
            uf.union(index, size*(i-1) + j);
        }
        if ((i < size-1) && (grid[i+1][j])) { 
            uf.union(index, size*(i+1) + j); 
        }
        if ((j > 0) && (grid[i][j-1])) {
            uf.union(index, size*i + j - 1);
        }
        if ((j < size-1) && (grid[i][j+1])) {
            uf.union(index, size*i + j + 1);
        }
        if (i == 0) { //top row
            uf.union(index, virtualTop);
        }
        if (i == size - 1) { //bottom row
            uf.union(index, virtualBottom);
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
        if (!isOpen(i, j)) {
            return false;
        }
        if (i == 1) {  // if it is open, it is full. for the first row. 
            return true;
        }
        int index = size * (i - 1) + (j - 1);
        //System.out.println("isFull: " + uf.connected(virtualTop, index));
        if (percolates()) {
            return false;
        }
        return uf.connected(virtualTop, index);
    }

    public boolean percolates() {            // does the system percolate?
        if (size == 1) {
            return grid[0][0];
        }
        return uf.connected(virtualTop, virtualBottom);
    }

    public static void main(String[] args) {
        if (args.length != 4)
        {
            System.out.println("You must supply one command-line arguments: filename openedGrid isFullX isFullY");
            return;
        }
        int opened = 0;
        int isFullX = 0;
        int isFullY = 0;
       try
        {
            opened = Integer.parseInt(args[1]);
            isFullX = Integer.parseInt(args[2]);
            isFullY = Integer.parseInt(args[3]);            
        }
        catch (NumberFormatException numberformatexception)
        {
            System.out.println("You must supply two postive integer command-line arguments: N and seed");
            return;
        }        
        Percolation p = new Percolation(1);
        try {
            int i = -1;
            for (String line : Files.readAllLines(Paths.get("../testing/" + args[0]))) {
                i++;
                if (i == 0) {
                    int N = Integer.parseInt(line);
                    p = new Percolation(N);
                    continue;
                }
                String[] splited = line.split("\\s+");
                int ii = Integer.parseInt(splited[1]);
                int jj = Integer.parseInt(splited[2]);
                p.open(ii, jj);
                //System.out.println("percolated: " + p.percolates());
                if (i == opened) {
                    System.out.println(p.isFull(isFullX, isFullY));
                }
            }                   
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}