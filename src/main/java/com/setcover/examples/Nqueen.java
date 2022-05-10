package com.setcover.examples;

//https://www.youtube.com/watch?v=xouin83ebxE&list=PLtuGPaXoUEWigGtqvFol61Kz_3UWZPTW_&index=2&t=656s
public class Nqueen {

    static int rec = 0;

    public boolean solve_Nqueen(char[][] chessBoard, int numOfQueens) {
        int count = 0;

        if (numOfQueens > chessBoard.length) {
            System.out.println("the number of queens must be smaller than or equal to the number of the rows!!");
            return true;
        }
        // sum all the Q's
        for (int row = 0; row < chessBoard.length; row++) {
            for (int col = 0; col < chessBoard.length; col++) {
                if (chessBoard[row][col] == 'Q')
                    count++;

            }

        }


        if (count == numOfQueens)
            return true;

        for (int row = 0; row < chessBoard.length; row++) {
            for (int col = 0; col < chessBoard.length; col++) {
                if (chessBoard[row][col] == 0) {
                    if (is_Safe(chessBoard, row, col)) {
                        chessBoard[row][col] = 'Q';
                        rec++;
                        if (solve_Nqueen(chessBoard, numOfQueens))
                            return true;

                        else
                            chessBoard[row][col] = 0;
                    }

                }
            }

        }


        return false;
    }


    private static boolean is_Safe(char[][] chessBoard, int row, int col) {

        // safe row?
        for (int i = 0; i < chessBoard.length; i++) {
            if (chessBoard[row][i] != 0)
                return false;
        }

        // safe colmn?
        for (int i = 0; i < chessBoard.length; i++) {
            if (chessBoard[i][col] != 0)
                return false;
        }

        // safe diagonals?
        for (int i = 0; i < chessBoard.length; i++) {
            for (int j = 0; j < chessBoard.length; j++) {
                if ((i - j == row - col || i + j == row + col) && (chessBoard[i][j] != 0))
                    return false;
            }

        }

        return true;
    }


    public static void print(char[][] matrix) {

        for (int row = 0; row < matrix.length; row++) {
            System.out.println("");
            System.out.println("-------------------");
            System.out.print("|");
            for (int column = 0; column < matrix.length; column++) {
                System.out.print(matrix[row][column] + "|");
            }

        }
        System.out.println("");
        System.out.println("-------------------");


    }

    public static void main(String args[]) {

        Nqueen queen = new Nqueen();


        char[][] q = new char[8][8];
        queen.solve_Nqueen(q, 6);


        print(q);
        System.out.println(rec);

    }
}