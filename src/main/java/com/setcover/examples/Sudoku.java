package com.setcover.examples;

public class Sudoku {

    static boolean solve_seduko(int [][] matrix) {
        int n = matrix.length;
        int row = -1;
        int col = -1;
        boolean isEmpty = true;
        // return the coordinates of the next free cell
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                if (matrix[i][j] == 0)
                {
                    row = i;
                    col = j;

                    // we still has free cell
                    isEmpty = false;
                    break;
                }
            }
        }

        // no empty space left
        if (isEmpty)
        {

            return true;
        }

        // for each free cell fill it and the Sudoku and recursivley see if it's the answer or a dead end
        for (int num = 1; num <= n; num++)
        {
            if (is_safe(matrix, row, col, num))
            {
                matrix[row][col] = num;
                if (solve_seduko(matrix))
                {
                    // print(board, n);
                    return true;
                }
                else
                {
                    matrix[row][col] = 0; // replace it
                }
            }
        }
        // System.out.println("false");
        return false;
    }

    static boolean is_safe(int [][] matrix, int i, int j, int num)	{
        //number is not in row
        for(int k=0; k <matrix.length; k++) {
            if(matrix[i][k] == num)
                return false;
        }
        // num is not in colmn
        for(int k=0; k <matrix.length; k++) {
            if(matrix[k][j] == num)
                return false;
        }
        // num is not in small Grid
        int sqrt = (int) Math.sqrt(matrix.length);
        int imin = i - i % sqrt;
        int jmin = j - j % sqrt;
        for(int k=imin; k<imin+sqrt; k++)
        {
            for(int l=jmin; l<jmin+sqrt; l++)
            {
                if(matrix[k][l] == num)
                {
                    return false;
                }
            }

        }

        return true;
    }




    public static void main(String args[]) {

        int [][] sudoku = {
                {1,0,0,1,0,0,1},
                {1,0,0,1,0,0,0},
                {0,0,0,1,4,0,1},
                {0,0,1,0,1,1,0},
                {0,1,1,0,0,1,1},
                {0,1,0,0,0,0,1},
                {1,0,0,1,0,0,0}

        };

        solve_seduko(sudoku);
        for (int i = 0; i < sudoku.length; i++) {
            for (int j = 0; j < sudoku[i].length; j++) {
                System.out.print(sudoku[i][j] + " ");

            }
            System.out.println();
        }

    }
}
