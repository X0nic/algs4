/**
 *  The <tt>PercolationStats</tt> class is used to calculate the typical
 *  percolation threshold for an NxN grid.
 *  <p>
 *
 *  @author Nathan Lee
 *  */

public class PercolationStats {
  private double[] experiments;
  private int experimentCount;
  private double experimentSum;
  private int gridSize;

  // perform T independent experiments on an N-by-N grid
  public PercolationStats(int N, int T)
  {
    if (N < 1)
      throw new IllegalArgumentException("Grid needs to be at least 1x1");

    if (T < 1)
      throw new IllegalArgumentException("Need to perform at least 1 experiment");

    experimentCount = T;
    experiments = new double[experimentCount];
    experimentSum = 0;
    gridSize = N;

    for (int i = 0; i < experimentCount; i++)
    {
      addExperimentResults(i, runExperiment());
    }
  }

  // sample mean of percolation threshold
  public double mean()
  {
    if (experimentCount < 1)
      return 0;

    return experimentSum / experimentCount;
  }

  // sample standard deviation of percolation threshold
  public double stddev()
  {
    if (gridSize <= 1)
      return Double.NaN;

    if (experimentCount < 1)
      return 0;

    double totalDeviation = 0;
    for (int i = 0; i < experimentCount; i++)
    {
      double deviation = experiments[i] - mean();
      totalDeviation += deviation * deviation;
    }

    return Math.sqrt(totalDeviation / experimentCount);
  }

  // low  endpoint of 95% confidence interval
  public double confidenceLo()
  {
    return mean() - (1.96 * stddev()) / Math.sqrt(experimentCount);
  }

  // high endpoint of 95% confidence interval
  public double confidenceHi()
  {
    return mean() + (1.96 * stddev()) / Math.sqrt(experimentCount);
  }

  private void addExperimentResults(int offset, double results)
  {
      experiments[offset] = results;
      experimentSum += results;
  }

  private double runExperiment()
  {
    Percolation perc = new Percolation(gridSize);

    int count = 0;
    while (!perc.percolates())
    {
      int x = StdRandom.uniform(gridSize)+1;
      int y = StdRandom.uniform(gridSize)+1;

      if (!perc.isOpen(x, y))
      {
        perc.open(x, y);
        count++;
      }
    }

    double results = (double) count / (gridSize*gridSize);
    // StdOut.println(count + " / " + gridSize*gridSize + " = " + results );

    return results;
  }


  public static void main(String[] args)    // test client (described below)
  {
    int N = Integer.parseInt(args[0]);
    int T = Integer.parseInt(args[1]);

    PercolationStats stats = new PercolationStats(N, T);

    StdOut.println("mean                    = " + stats.mean());
    StdOut.println("stddev                  = " + stats.stddev());
    StdOut.println("95% confidence interval = " + stats.confidenceLo()
                                         + ", " + stats.confidenceHi());
  }

}
