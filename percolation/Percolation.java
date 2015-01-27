public class Percolation {
  private boolean[] grid;
  private int gridSize;
  private WeightedQuickUnionUF quickUnion;

  public Percolation(int N)               // create N-by-N grid, with all sites blocked
  {
    grid = new boolean[N*N];
    gridSize = N;
    quickUnion = new WeightedQuickUnionUF(N*N);

    // StdOut.println(grid.length);
  }
  
  public void open(int i, int j)          // open site (row i, column j) if it is not open already
  {
    if (isOpen(i,j) == false)
    {
      grid[xyTo1D(i,j)] = true;

      if (isOpen(i-1,j))
      {
        StdOut.println("Union Top: " + i + " " + j);
        quickUnion.union(xyTo1D(i,j), xyTo1D(i-1,j));
      }
      if (isOpen(i+1,j))
      {
        StdOut.println("Union Bottom: " + i + " " + j);
        quickUnion.union(xyTo1D(i,j), xyTo1D(i+1,j));
      }
      if (isOpen(i,j-1))
      {
        StdOut.println("Union Left: " + i + " " + j);
        quickUnion.union(xyTo1D(i,j), xyTo1D(i,j-1));
      }
      if (isOpen(i,j+1))
      {
        StdOut.println("Union Right: " + i + " " + j);
        quickUnion.union(xyTo1D(i,j), xyTo1D(i,j+1));
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

    return quickUnion.connected(xyTo1D(1,1), xyTo1D(i,j)) && isOpen(i,j);
  }
  
  public boolean percolates()             // does the system percolate?
  {
    // if ( quickUnion.connected(xyTo1D(1,1), xyTo1D(gridSize,gridSize)) )
    //   StdOut.println("Purcolated");

    return quickUnion.connected(xyTo1D(1,1), xyTo1D(gridSize,gridSize));
  }
  

  public static void main(String[] args)   // test client (optional)
  {
  }

  private int xyTo1D(int i, int j)
  {
    int x = j-1;
    int y = i-1;

    int coords = y * gridSize + x;
    // StdOut.println( x + " " + y + " = " + coords);

    return coords;
  }
}
