public class Percolation {
  private boolean[] grid;
  private int gridSize;
  private WeightedQuickUnionUF quickUnion;

  private static final int VIRTUAL_TOP = 0;
  private static final int VIRTUAL_BOTTOM = 1;

  public Percolation(int N)               // create N-by-N grid, with all sites blocked
  {
    grid = new boolean[N*N+2];
    gridSize = N;
    quickUnion = new WeightedQuickUnionUF(N*N+2);

    // StdOut.println(grid.length);
  }

  public void open(int i, int j)          // open site (row i, column j) if it is not open already
  {
    if (isOpen(i,j) == false)
    {
      grid[xyTo1D(i,j)] = true;

      if (isOpen(i-1,j))
      {
        // StdOut.println("Union Top: " + i + " " + j);
        quickUnion.union(xyTo1D(i,j), xyTo1D(i-1,j));
      }
      if (isOpen(i+1,j))
      {
        // StdOut.println("Union Bottom: " + i + " " + j);
        quickUnion.union(xyTo1D(i,j), xyTo1D(i+1,j));
      }
      if (isOpen(i,j-1))
      {
        // StdOut.println("Union Left: " + i + " " + j);
        quickUnion.union(xyTo1D(i,j), xyTo1D(i,j-1));
      }
      if (isOpen(i,j+1))
      {
        // StdOut.println("Union Right: " + i + " " + j);
        quickUnion.union(xyTo1D(i,j), xyTo1D(i,j+1));
      }

      if (i==1)
      {
        // StdOut.println("Connect to top: " + i + " " + j);
        quickUnion.union(VIRTUAL_TOP, xyTo1D(i,j));
      }

      if (i==gridSize)
      {
        // StdOut.println("Connect to bottom: " + i + " " + j);
        quickUnion.union(VIRTUAL_BOTTOM, xyTo1D(i,j));
      }
    }
  }

  public boolean isOpen(int i, int j)     // is site (row i, column j) open?
  {
    if (i<1 || j<1)
      return false;

    if (i>gridSize || j>gridSize)
      return false;

    return grid[xyTo1D(i,j)];
  }

  public boolean isFull(int i, int j)     // is site (row i, column j) full?
  {
    // return false;

    // return quickUnion.connected(xyTo1D(1,1), xyTo1D(i,j)) && isOpen(i,j);
    return quickUnion.connected(0, xyTo1D(i,j)) && isOpen(i,j);
  }

  public boolean percolates()             // does the system percolate?
  {
    // if ( quickUnion.connected(VIRTUAL_TOP, VIRTUAL_BOTTOM) )
    //   StdOut.println("Purcolated");

    return quickUnion.connected(VIRTUAL_TOP, VIRTUAL_BOTTOM);
  }

  public static void main(String[] args)   // test client (optional)
  {
  }

  private int xyTo1D(int i, int j)
  {
    int x = j-1;
    int y = i-1;

    int coords = y * gridSize + x;

    coords += 2; // Add room for virtual points.

    return coords;
  }
}
