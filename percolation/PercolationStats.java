public class PercolationStats {
  private double[] experiments;
  private int experimentCount;
  private double experimentSum;
  private int gridSize;

  public PercolationStats(int N, int T)     // perform T independent experiments on an N-by-N grid
  {
    experimentCount = T;
    experiments = new double[experimentCount];
    experimentSum = 0;
    gridSize = N;

    for(int i=0; i<experimentCount; i++)
    {
      addExperimentResults(i, runExperiment());
    }
  }

  public double mean()                      // sample mean of percolation threshold
  {
    if (experimentCount < 1)
      return 0;

    return experimentSum / experimentCount;
  }

  public double stddev()                    // sample standard deviation of percolation threshold
  {
    if (experimentCount < 1)
      return 0;

    double totalDeviation = 0;
    for(int i=0; i<experimentCount; i++)
    {
      double deviation = experiments[i] - mean();
      totalDeviation += deviation * deviation;
    }

    return Math.sqrt( totalDeviation / experimentCount );
  }

  public double confidenceLo()              // low  endpoint of 95% confidence interval
  {
    return mean() - (1.96 * stddev()) / Math.sqrt(experimentCount);
  }

  public double confidenceHi()              // high endpoint of 95% confidence interval
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
    while(! perc.percolates())
    {
      int x = StdRandom.uniform(gridSize)+1;
      int y = StdRandom.uniform(gridSize)+1;

      if (perc.isOpen(x,y) == false) 
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

    StdOut.println("mean                    = " + stats.mean() );
    StdOut.println("stddev                  = " + stats.stddev() );
    StdOut.println("95% confidence interval = " + stats.confidenceLo() + ", " + stats.confidenceHi() );
  }

}
