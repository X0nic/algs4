/**
 *  The <tt>Percolation</tt> class represents a grid data structure.
 *  The grid has open and closed site, and can by used to represent flow
 *  through a material.
 *  <p>
 *
 *  @author Nathan Lee
 *  */

public class Percolation {
  private static final int VIRTUAL_TOP = 0;
  private static final int VIRTUAL_BOTTOM = 1;

  private boolean[] grid;
  private int gridSize;
  private WeightedQuickUnionUF quickUnion;

  // create N-by-N grid, with all sites blocked
  public Percolation(int N)
  {
    if (N < 1) 
      throw new IllegalArgumentException("Grid needs to be at least 1x1");

    grid = new boolean[N*N+2];
    gridSize = N;
    quickUnion = new WeightedQuickUnionUF(N*N+2);
  }

  // open site (row i, column j) if it is not open already
  public void open(int i, int j)
  {
    if (i <= 0 || i > gridSize) 
      throw new IndexOutOfBoundsException("row index " + i + " out of bounds");

    if (j <= 0 || j > gridSize) 
      throw new IndexOutOfBoundsException("column index " + j + " out of bounds");

    if (!isOpen(i, j))
    {
      grid[xyTo1D(i, j)] = true;

      unionTop(i, j);
      unionBottom(i, j);
      unionLeft(i, j);
      unionRight(i, j);

      unionVirtualTop(i, j);
      unionVirtualBottom(i, j);
    }
  }

  private void unionTop(int i, int j)
  {
    unionIfNeeded(i, j, i-1, j);
  }

  private void unionBottom(int i, int j)
  {
    unionIfNeeded(i, j, i+1, j);
  }

  private void unionLeft(int i, int j)
  {
    unionIfNeeded(i, j, i, j-1);
  }

  private void unionRight(int i, int j)
  {
    unionIfNeeded(i, j, i, j+1);
  }

  private void unionVirtualTop(int i, int j)
  {
      if (i == 1)
      {
        quickUnion.union(VIRTUAL_TOP, xyTo1D(i, j));
      }
  }

  private void unionVirtualBottom(int i, int j)
  {
      if (i == gridSize)
      {
        quickUnion.union(VIRTUAL_BOTTOM, xyTo1D(i, j));
      }
  }

  private void unionIfNeeded(int firstCellRow, int firstCellColumn,
                             int secondCellRow, int secondCellColumn)
  {
    if (secondCellRow < 1 || secondCellColumn < 1) return;
    if (secondCellRow > gridSize || secondCellColumn > gridSize) return;

    if (isOpen(secondCellRow, secondCellColumn))
    {
      quickUnion.union(xyTo1D(firstCellRow, firstCellColumn),
                       xyTo1D(secondCellRow, secondCellColumn));
    }
  }

  public boolean isOpen(int i, int j)     // is site (row i, column j) open?
  {
    if (i <= 0 || i > gridSize)
      throw new IndexOutOfBoundsException("row index " + i + " out of bounds");
    if (j <= 0 || j > gridSize)
      throw new IndexOutOfBoundsException("column index " + j + " out of bounds");

    return grid[xyTo1D(i, j)];
  }

  public boolean isFull(int i, int j)     // is site (row i, column j) full?
  {
    if (i <= 0 || i > gridSize)
      throw new IndexOutOfBoundsException("row index " + i + " out of bounds");
    if (j <= 0 || j > gridSize)
      throw new IndexOutOfBoundsException("column index " + j + " out of bounds");

    return quickUnion.connected(0, xyTo1D(i, j)) && isOpen(i, j);
  }

  public boolean percolates()             // does the system percolate?
  {
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
