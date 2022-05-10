package com.dancingLinks;

import java.util.ArrayList;
import java.util.Arrays;

public class SudokuDLX extends DancingLinks{

    static int S;
    static int side;

    // sudoku has numbers 1-9. A 0 indicates an empty cell that we will need to
// fill in.
    private int[][] makeExactCoverGrid(int[][] sudoku)
    {
        int[][] R = sudokuExactCover();
        for(int i = 1; i <= S; i++)
        {
            for(int j = 1; j <= S; j++)
            {
                int n = sudoku[i - 1][j - 1];
                if (n != 0)
                { // zero out in the constraint board
                    for(int num = 1; num <= S; num++)
                    {
                        if (num != n)
                        {
                            Arrays.fill(R[getIdx(i, j, num)], 0);
                        }
                    }
                }
            }
        }
        return R;
    }

    // Returns the base exact cover grid for a SUDOKU puzzle
    private int[][] sudokuExactCover()
    {
        int[][] R = new int[S * S * S][S * S * 4];

        int hBase = 0;

        // row-column constraints
        for(int r = 1; r <= S; r++)
        {
            for(int c = 1; c <= S; c++, hBase++)
            {
                for(int n = 1; n <= S; n++)
                {
                    R[getIdx(r, c, n)][hBase] = 1;
                }
            }
        }

        // row-number constraints
        for(int r = 1; r <= S; r++)
        {
            for(int n = 1; n <= S; n++, hBase++)
            {
                for(int c1 = 1; c1 <= S; c1++)
                {
                    R[getIdx(r, c1, n)][hBase] = 1;
                }
            }
        }

        // column-number constraints

        for(int c = 1; c <= S; c++)
        {
            for(int n = 1; n <= S; n++, hBase++)
            {
                for(int r1 = 1; r1 <= S; r1++)
                {
                    R[getIdx(r1, c, n)][hBase] = 1;
                }
            }
        }

        // box-number constraints

        for(int br = 1; br <= S; br += side)
        {
            for(int bc = 1; bc <= S; bc += side)
            {
                for(int n = 1; n <= S; n++, hBase++)
                {
                    for(int rDelta = 0; rDelta < side; rDelta++)
                    {
                        for(int cDelta = 0; cDelta < side; cDelta++)
                        {
                            R[getIdx(br + rDelta, bc + cDelta, n)][hBase] = 1;
                        }
                    }
                }
            }
        }

        return R;
    }

    // row [1,S], col [1,S], num [1,S]
    private int getIdx(int row, int col, int num){
        return (row - 1) * S * S + (col - 1) * S + (num - 1);
    }


    // methods to turn SetCover solution into an original solved Sudoku Matrix
    @Override
    public void handleSolution(ArrayList<Node> solutions)
    {
        int[][] result = parseBoard(solutions);
        printSolution(result);
    }
    public static void printSolution(int[][] result)
    {
        int N = result.length;
        for(int i = 0; i < N; i++)
        {
            String ret = "";
            for(int j = 0; j < N; j++)
            {
                ret += result[i][j] + " ";
            }
            System.out.println(ret);
        }
        System.out.println();
    }

    private int[][] parseBoard(ArrayList<Node> solutions)
    {
        double s =Math.sqrt((double)solutions.size());
        int size = (int) s;
        int[][] result = new int[size][size];
        for(Node n : solutions)
        {
            Node rcNode = n;
            int min = rcNode.getColumn().getColID();
            for(Node tmp = n.getRight(); tmp != n; tmp = tmp.getRight())
            {
                int val = tmp.getColumn().getColID();
                if (val < min){
                    min = val;
                    rcNode = tmp;
                }
            }
            int ans1 = rcNode.getColumn().getColID();
            int ans2 = rcNode.getRight().getColumn().getColID();
            int r = ans1 / size;
            int c = ans1 % size;
            int num = (ans2 % size) + 1;
            result[r][c] = num;
        }
        return result;
    }

    //test
    public static void main(String args[]) {

        SudokuDLX d = new SudokuDLX();
        int [][] sudoku = new int [4][4];

        S = sudoku.length;
        double smallGrid = Math.sqrt((double)S);
        side = (int) smallGrid;
        int [][] exSud=  d.makeExactCoverGrid(sudoku);
//	d.printSolution(exSud);
        d.createToridolMatrix(exSud);
        d.search(0);

    }


}