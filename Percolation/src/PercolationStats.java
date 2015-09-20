//javac Percolation.java -classpath .:../../lib/algs4.jar
//javac PercolationStats.java -classpath .:../../lib/algs4.jar
//java -classpath .:../../lib/algs4.jar PercolationStats 100 100
//zip percolation.zip Percolation.java PercolationStats.java

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
public class PercolationStats {
    private double mean, stddev, confidenceHi, confidenceLo;
    public PercolationStats(int N, int T) {    // perform T independent computational experiments on an N-by-N grid
        if ((N <= 0) || (T <= 0)) {
            throw new java.lang.IllegalArgumentException();
        }
        double [] thresholds = new double[T];
        for (int m = 0; m < T; m++) {
            Percolation p = new Percolation(N);
            int openCount = 0;
            while (!p.percolates()) {
                boolean open = true;
                int i = 0;
                int j = 0;
                while (open) {
                    i = StdRandom.uniform(N) + 1;
                    j = StdRandom.uniform(N) + 1;
                    open = p.isOpen(i, j);
                }
                p.open(i, j);
                openCount++;
            }
            thresholds[m] = openCount * 1.0 / (N*N);
        }
        mean = StdStats.mean(thresholds);
        stddev = StdStats.stddev(thresholds);
        double interval = 1.96*stddev/Math.sqrt(T);
        confidenceHi = mean + interval;
        confidenceLo = mean - interval;
    }
    public double mean() {                     // sample mean of percolation threshold
        return mean;
    }
    public double stddev() {                   // sample standard deviation of percolation threshold     
        return stddev;
    }
    public double confidenceHi() {
        return confidenceHi;
    }
    public double confidenceLo() {
        return confidenceLo;
    }
    public static void main(String[] args) {
        int N, T;
        if (args.length != 2)
        {
            System.out.println("You must supply two postive integer command-line arguments: N and seed");
            return;
        }
        try
        {
            N = Integer.parseInt(args[0]);
            T = Integer.parseInt(args[1]);
        }
        catch (NumberFormatException numberformatexception)
        {
            System.out.println("You must supply two postive integer command-line arguments: N and seed");
            return;
        }
        PercolationStats pStats = new PercolationStats(N, T);
        System.out.println("mean = " + pStats.mean());
        System.out.println("stddev = " + pStats.stddev());
        System.out.println("95% confidence interval = " + (pStats.confidenceLo()) + ", " + (pStats.confidenceHi()));
    }
 
}