package sudokuSolver;
import java.io.IOException;
import javax.servlet.http.*;

//@SuppressWarnings("serial")

public class WebSudokuSolverServlet extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws IOException {
			doGet(req, resp);
	} //end of doPost
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		resp.setContentType("text/html");
		String head = "<!DOCTYPE html>\n<html>\n<head>\n<link type=\"text/css\" rel=\"stylesheet\" href=\"style.css\" />\n</head>\n<body>\n";
		String foot = "</td>\n<td class=\"again\"><a href=\"index.html\">Solve<br>Another<br>Sudoku</a></td>\n</tr>\n</table>\n<div class=\"footer\">Mikayla Thompson | CS112 | Yale University</div>\n</body>\n</html>";
		String title = "<table class=page>\n<tr>\n<td>\n<table class=\"title\">\n<tr>\n<td class=\"S\">S</td>\n<td class=\"titleText\">udoku<br>olver</td>\n</tr>\n</table>\n</td>\n<td>";
		
		resp.getWriter().println(head);
		resp.getWriter().println(title);
		int[][] unsolved = makeArray(req);
		int[][] solved = solve(unsolved);
		String tablecode = puzToHTML(solved);
		resp.getWriter().println(tablecode);
		resp.getWriter().println(foot);

	} //end of doGet
	
	public static int[][] makeArray(HttpServletRequest req) {
		String nums = "";
//		for (int i = 0; i < 81; i++) {
//			nums += req.getParameter("n" + i);
//		}
		nums += req.getParameter("numbers");
		int[][] sudoku = new int[9][9];
		for (int row = 0; row < 9; row ++) {
			for (int col = 0; col < 9; col ++) {
				sudoku[row][col] = nums.charAt(0) - 48;
				nums = nums.substring(1);
			}
		}
		return sudoku;
	}
	
	public static int[][] solve(int[][] sudoku) {
		while (countZeroes(sudoku) > 0) {
			int pre0 = countZeroes(sudoku);
			sudoku = runBoard(sudoku);
			int post0 = countZeroes(sudoku);
			if (pre0 == post0){
				break;
			}
		}
		return sudoku;
	}
	
	public static String puzToHTML(int[][] puzzle) {
		String filled = "\n<table class=sudoku>";
		for (int row = 0; row < 9; row ++) {
			for (int col = 0; col < 9; col ++) {
				if (col == 0) {
					filled += "\n<tr> ";
				}
				filled += "\n<td class=";
				if (row == 3 || row == 4 || row == 5) {
					filled += " \"midrow\""; 
				}
				if (col == 3 || col == 4 || col == 5) {
					filled += " \"midcol\"";
				}
				filled += ">" + puzzle[row][col] + " </td> ";
				if (col == 8) {
					filled += "\n</tr> ";
				}
			}
		}
		filled += "\n</table>";
		return filled;
	}
	
	public static int[][] runBoard(int[][] sudoku) {
		sudoku = runBoxes(runColumns(runRows(sudoku)));
		return sudoku;
	}
	
	public static int[][] runRows(int[][] sudoku) {
		for (int row = 0; row < 9; row++) {
			for (int num = 1; num <= 9; num++) {
				boolean rowArray[] = { false, false, false, false, false,
						false, false, false, false };
				for (int column = 0; column < 9; column++) {
					if (sudoku[row][column] == 0) {
						rowArray[column] = isPossible(sudoku, row, column, num);
					} // end "if 0"
				} // end "for each column"
				int count = 0;
				int place = 0;
				for (int i = 0; i < 9; i++) {
					if (rowArray[i]) {
						count++;
						place = i;
					} // end "if rowArray True"
				} // end "for each in row"
				if (count == 1) {
					if (num != 0) {
						sudoku[row][place] = num;
					}
				} // end "if row has one true"
			} // end "for each number"
		}
		return sudoku;
	}
	
	public static int[][] runColumns(int[][] sudoku) {
		for (int col = 0; col < 9; col++) {
			for (int num = 1; num <= 9; num++) {
				boolean colArray[] = { false, false, false, false, false,
						false, false, false, false };
				for (int row = 0; row < 9; row++) {
					if (sudoku[row][col] == 0) {
						colArray[row] = isPossible(sudoku, row, col, num);
					} // end "if 0"
				} // end "for each column"
				int count = 0;
				int place = 0;
				for (int i = 0; i < 9; i++) {
					if (colArray[i]) {
						count++;
						place = i;
					} // end "if rowArray True"
				} // end "for each in row"
				if (count == 1) {
					if (num != 0) {
						sudoku[place][col] = num;
					}
				} // end "if col has one true"
			} // end "for each number"
		}
		return sudoku;
	}
	
	public static int[][] runBoxes(int[][] sudoku) {
		for (int boxCol = 0; boxCol < 9; boxCol += 3) {
			for (int boxRow = 0; boxRow < 9; boxRow += 3) {
				for (int num = 1; num <= 9; num++) {
					boolean boxArray[] = { false, false, false, false, false,
							false, false, false, false };
					int index = 0;
					for (int row = boxRow; row < boxRow + 3; row++) {
						for (int col = boxCol; col < boxCol + 3; col++) {
							if (sudoku[row][col] == 0) {
								if (isPossible(sudoku, row, col, num)) {
									boxArray[index] = true;
								}
							}
							index++;
						}
					}
					int count = 0;
					index = 0;
					for (int i = 0; i < 9; i++) {
						if (boxArray[i]) {
							count++;
							index = i;
						} // end "if rowArray True"
					}
					if (count == 1) {
						sudoku[boxRow + index / 3][boxCol + index % 3] = num;
					}
				}
			}
		}
		return sudoku;
	}
	public static int countZeroes(int[][] sudoku) {
		int count = 0;
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				if (sudoku[row][col] == 0) {
					count++;
				}
			}
		}
		return count;
	}
	
	public static boolean isPossible(int[][] sudoku, int row, int column,
			int numToCheck) {
		for (int i = 0; i < 9; i++) // check row
		{
			if (sudoku[row][i] == numToCheck) {
				return false;
			}
		}
		for (int i = 0; i < 9; i++) // check column
		{
			if (sudoku[i][column] == numToCheck) {
				return false;
			}
		}
		int vertStart, horzStart;
		if (row <= 2) {
			vertStart = 0;
		} else if (row <= 5) {
			vertStart = 3;
		} else {
			vertStart = 6;
		}
		if (column <= 2) {
			horzStart = 0;
		} else if (column <= 5) {
			horzStart = 3;
		} else {
			horzStart = 6;
		}
		for (int i = horzStart; i <= horzStart + 2; i++) {
			for (int j = vertStart; j <= vertStart + 2; j++) {
				if (sudoku[j][i] == numToCheck) {
					return false;
				}
			}
		}
		return true;
	} // end isPossible
	
	
	
}
