import java.util.ArrayList;
import java.util.List;

public class HexadecimalSudoku {
	
  /**
   * Find an assignment of values to sudoku cells that makes the sudoku valid.
   * 
   * @param the sudoku to be solved
   * @return whether a solution was found if a solution was found, the sudoku is
   *         filled in with the solution if no solution was found, restores the
   *         sudoku to its original value
   */
	
	public static boolean solveSudoku(int[][] sudoku) {
		int r = 0;
		int c = 0;
		return solveSudoku(sudoku, r, c);
	}
	
  private static boolean solveSudoku(int[][] sudoku, int row, int col) {
	  
  /**
   * Find the legal values for the given sudoku and cell.
   * 
   * @param sudoku, the sudoku being solved.
   * @param row the row of the cell to get values for.
   * @param col the column of the cell.
   * @return an ArrayList of the valid values.
   */
	  if (row == 16) {
	      return true;
	    }
	    int nextrow = row;
	    int nextcol = col+1;
	    if(nextcol == 16) {
	      nextcol = 0;
	      nextrow++;
	    }

	    if(sudoku[row][col] != -1) {
	      if(solveSudoku(sudoku,nextrow,nextcol)) {
	        return true;
	      }
	    }
	    else {
	      List<Integer> validChoices = legalValues(sudoku, row, col);
	      for(Integer val: validChoices) {
	        sudoku[row][col] = val;
	        boolean solved = solveSudoku(sudoku, nextrow, nextcol);
	        if (solved) {
	          return checkSudoku(sudoku, false);
	        }
	        else {
	          sudoku[row][col]=-1;
	        }
	    }
	    }
	    return false;
  }
  
  private static boolean checkRow(int[][] sudoku, int row, int cell ) {
	  for (int col = 0; col < 16; col++) {
		  if (sudoku[row][col] == cell) {
			  return false;
		  }
	  }
	  return true;
  }

  private static boolean checkCol(int[][] sudoku, int col, int cell) {
	  for (int row = 0; row < 16; row++) {
		  if (sudoku[row][col] == cell) {
			  return false;
		  }
	  }
	  return true;
  }

  private static boolean checkSquare(int[][] sudoku, int row, int col, int cell) {
	  int base_row = row / 4 * 4;
	  int base_col = col / 4 * 4;
	  for(int i = base_row; i < base_row + 4; i++) {
	    for (int j = base_col; j < base_col + 4; j++) {
	      if(sudoku[i][j] == cell) {
	        return false;
	      }
	  }
	  }
	  return true;
  }
  
  private static ArrayList<Integer> legalValues(int[][] sudoku, int row, int col) {
		ArrayList<Integer> legalValues = new ArrayList();
		  for (int cell = 0; cell < 16; cell++ ) {
			  if (checkRow(sudoku, row, cell) && checkCol(sudoku, col, cell) && checkSquare(sudoku, row, col, cell)) {
				 legalValues.add(cell); 
			  }
		  }
		  return legalValues;
	  }
  
  /**
   * checks that the sudoku rules hold in this sudoku puzzle. cells that contain
   * 0 are not checked.
   * 
   * @param the sudoku to be checked
   * @param whether to print the error found, if any
   * @return true if this sudoku obeys all of the sudoku rules, otherwise false
   */
  public static boolean checkSudoku(int[][] sudoku, boolean printErrors) {
    if (sudoku.length != 16) {
      if (printErrors) {
        System.out.println("sudoku has " + sudoku.length + " rows, should have 16");
      }
      return false;
    }
    for (int i = 0; i < sudoku.length; i++) {
      if (sudoku[i].length != 16) {
        if (printErrors) {
          System.out.println("sudoku row " + i + " has " + sudoku[i].length + " cells, should have 16");
        }
        return false;
      }
    }
    /* check each cell for conflicts */
    for (int i = 0; i < sudoku.length; i++) {
      for (int j = 0; j < sudoku.length; j++) {
        int cell = sudoku[i][j];
        if (cell == -1) {
          continue; /* blanks are always OK */
        }
        if ((cell < 0) || (cell > 16)) {
          if (printErrors) {
            System.out.println("sudoku row " + i + " column " + j + " has illegal value " + String.format("%02X", cell));
          }
          return false;
        }
        /* does it match any other value in the same row? */
        for (int m = 0; m < sudoku.length; m++) {
          if ((j != m) && (cell == sudoku[i][m])) {
            if (printErrors) {
              System.out.println("sudoku row " + i + " has " + String.format("%X", cell) + " at both positions " + j + " and " + m);
            }
            return false;
          }
        }
        /* does it match any other value it in the same column? */
        for (int k = 0; k < sudoku.length; k++) {
          if ((i != k) && (cell == sudoku[k][j])) {
            if (printErrors) {
              System.out.println("sudoku column " + j + " has " + String.format("%X", cell) + " at both positions " + i + " and " + k);
            }
            return false;
          }
        }
        /* does it match any other value in the 4x4? */
        for (int k = 0; k < 4; k++) {
          for (int m = 0; m < 4; m++) {
            int testRow = (i / 4 * 4) + k; /* test this row */
            int testCol = (j / 4 * 4) + m; /* test this col */
            if ((i != testRow) && (j != testCol) && (cell == sudoku[testRow][testCol])) {
              if (printErrors) {
                System.out.println("sudoku character " + String.format("%X", cell) + " at row " + i + ", column " + j
                    + " matches character at row " + testRow + ", column " + testCol);
              }
              return false;
            }
          }
        }
      }
    }
    return true;
  }


  /**
   * Converts the sudoku to a printable string
   * 
   * @param the sudoku to be converted
   * @param whether to check for errors
   * @return the printable version of the sudoku
   */
  public static String toString(int[][] sudoku, boolean debug) {
    if ((!debug) || (checkSudoku(sudoku, true))) {
      String result = "";
      for (int i = 0; i < sudoku.length; i++) {
        if (i % 4 == 0) {
          result = result + "+---------+---------+---------+---------+\n";
        }
        for (int j = 0; j < sudoku.length; j++) {
          if (j % 4 == 0) {
            result = result + "| ";
          }
          if (sudoku[i][j] == -1) {
            result = result + "  ";
          }
          else {
            result = result + String.format("%X", sudoku[i][j]) + " ";
          }
        }
        result = result + "|\n";
      }
      result = result + "+---------+---------+---------+---------+\n";
      return result;
    }
    return "illegal sudoku";
  }

}
