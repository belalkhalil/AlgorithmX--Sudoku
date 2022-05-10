package com.dancingLinks;

import java.util.ArrayList;
public abstract class DancingLinks {

	/*

	this implementation of Dancing links is based on GFG c++ implemenation
	https://www.geeksforgeeks.org/exact-cover-problem-algorithm-x-set-1/
	https://www.geeksforgeeks.org/exact-cover-problem-algorithm-x-set-2-implementation-dlx/

	*/

    protected class Node{

        Node Left;
        Node Right;
        Node Up;
        Node Down;
        Node Column;

        int rowID;
        int colID;
        int numNodesInCol;


        public Node getLeft() {
            return Left;
        }
        public void setLeft(Node left) {
            Left = left;
        }
        public Node getRight() {
            return Right;
        }
        public void setRight(Node right) {
            Right = right;
        }
        public Node getUp() {
            return Up;
        }
        public void setUp(Node up) {
            Up = up;
        }
        public Node getDown() {
            return Down;
        }
        public void setDown(Node down) {
            Down = down;
        }
        public Node getColumn() {
            return Column;
        }
        public void setColumn(Node column) {
            Column = column;
        }
        public int getRowID() {
            return rowID;
        }
        public void setRowID(int rowID) {
            this.rowID = rowID;
        }
        public int getColID() {
            return colID;
        }
        public void setColID(int colID) {
            this.colID = colID;
        }
        public int getNumNodesInCol() {
            return numNodesInCol;
        }

    }

    // Header node, contains pointer to the
    // list header node of first column
    Node header = new Node();

    // Matrix to contain nodes of linked mesh
    private Node [][] ToridolMatrix;

    // Problem Matrix
    private boolean [][] problemMatrix ;

    // vector containing solutions
    ArrayList<Node> solutions = new ArrayList<Node>();

    // Number of rows and columns in problem matrix
    int nRow =0; int nCol =0;
    public void setnRow(int nRow)
    {
        this.nRow = nRow;
    }

    public void setnCol(int nCol)
    {
        this.nCol = nCol;
    }

    // Functions to get next index in any direction
    // for given index (circular in nature)
    int getRight(int i){return (i+1) % nCol; }
    int getLeft (int i){return (i-1 < 0) ? nCol-1 : i-1 ; }
    int getUp   (int i){return (i-1 < 0) ? nRow : i-1 ; }
    int getDown (int i){return (i+1) % (nRow+1); }



    static boolean [][] to_SetCover (int [][] originalMatrix) {
        boolean [][] boolRepresentaion = new boolean[originalMatrix.length+1][originalMatrix[0].length];
        for(int i =0; i< originalMatrix.length; i++)
        {
            for(int j=0; j< originalMatrix[0].length; j++)
            {
                if(i == 0) boolRepresentaion[i][j] = true;
                if (originalMatrix[i][j] !=0)
                    boolRepresentaion[i+1][j] = true;
            }
        }
        return boolRepresentaion;
    }


    // create Toridol Matrix
    Node createToridolMatrix( int [][] originalMatrix)
    {

        setnRow(originalMatrix.length);
        setnCol(originalMatrix[0].length);
        boolean [][]  problemMatrix = to_SetCover(originalMatrix);


        ToridolMatrix = new Node [originalMatrix.length+1][originalMatrix[0].length];

        // initiallize the Node Matrix
        for(int i = 0; i <= nRow; i++)
        {
            for(int j = 0; j < nCol; j++)
            {  if(problemMatrix[i][j])
                ToridolMatrix[i][j] = new Node();

            }

        }

        // One extra row for list header nodes
        // for each column
        for(int i = 0; i <= nRow; i++)
        {
            for(int j = 0; j < nCol; j++)
            {
                // If it's 1 in the problem matrix then
                // only create a node
                if(problemMatrix[i][j])
                {


                    int a, b;

                    // If it's 1, other than 1 in 0th row
                    // then count it as node of column
                    // and increment node count in column header
                    if(i !=0) ToridolMatrix[0][j].numNodesInCol += 1;

                    // Add pointer to column header for this
                    // column node
                    ToridolMatrix[i][j].setColumn(ToridolMatrix[0][j]);

                    // set row and column id of this node
                    ToridolMatrix[i][j].setRowID(i);
                    ToridolMatrix[i][j].setColID(j);

                    // Link the node with neighbors

                    // Left pointer
                    a = i; b = j;
                    do{ b = getLeft(b); } while(!problemMatrix[a][b] && b != j);
                    ToridolMatrix[i][j].setLeft(ToridolMatrix[i][b]);

                    // Right pointer
                    a = i; b = j;
                    do { b = getRight(b); } while(!problemMatrix[a][b] && b != j);
                    ToridolMatrix[i][j].setRight(ToridolMatrix[i][b]);

                    // Up pointer
                    a = i; b = j;
                    do { a = getUp(a); } while(!problemMatrix[a][b] && a != i);
                    ToridolMatrix[i][j].setUp(ToridolMatrix[a][j]);

                    // Down pointer
                    a = i; b = j;
                    do { a = getDown(a); } while(!problemMatrix[a][b] && a != i);
                    ToridolMatrix[i][j].setDown(ToridolMatrix[a][j]);
                }
            }
        }
        // link header right pointer to column
        // header of first column
        header.setRight(ToridolMatrix[0][0]);

        // link header left pointer to column
        // header of last column
        header.setLeft(ToridolMatrix[0][nCol-1]);

        ToridolMatrix[0][0].setLeft(header);
        ToridolMatrix[0][nCol-1].setRight(header);
        return header;


    }

    // Cover the given node completely
    // means that: delete a certain row. based on column
    void cover( Node targetNode)
    {
        Node row, rightNode;

        // get the pointer to the header of column
        // to which this node belong
        Node colNode = targetNode.getColumn();

        // unlink column header from it's neighbors
        colNode.getLeft().setRight(colNode.getRight());
        colNode.getRight().setLeft( colNode.getLeft());

        // Move down the column and remove each row
        // by traversing right
        for(row = colNode.getDown(); row != colNode; row = row.getDown())
        {
            for(rightNode = row.getRight(); rightNode != row;
                rightNode = rightNode.getRight())
            {
                rightNode.getUp().setDown(rightNode.getDown());
                rightNode.getDown().setUp(rightNode.Up);

                // after unlinking row node, decrement the
                // node count in column header
                ToridolMatrix[0][rightNode.getColID()].numNodesInCol -= 1;
            }
        }
    }


    // Uncover the given node completely
    // add the row again
    void uncover(Node targetNode)
    {
        Node rowNode, leftNode;

        // get the pointer to the header of column
        // to which this node belong
        Node colNode = targetNode.getColumn();

        // Move down the column and link back
        // each row by traversing left
        for(rowNode = colNode.getUp(); rowNode != colNode; rowNode = rowNode.getUp())
        {
            for(leftNode = rowNode.getLeft(); leftNode != rowNode;
                leftNode = leftNode.getLeft())
            {
                leftNode.getUp().setDown( leftNode);
                leftNode.getDown().setUp(leftNode);

                // after linking row node, increment the
                // node count in column header
                ToridolMatrix[0][leftNode.getColID()].numNodesInCol += 1;
            }
        }

        // link the column header from it's neighbors
        //this is the kern of the Dancing Nodes
        colNode.getLeft().setRight(colNode);
        colNode.getRight().setLeft(colNode);

    }

    // Traverse column headers right and
    // return the column having minimum
    // node count
    Node getMinColumn()
    {
        Node h = header;
        Node min_col = h.getRight();
        h = h.getRight().getRight();
        do
        {
            if(h.numNodesInCol < min_col.getNumNodesInCol())
            {
                min_col = h;
            }
            h = h.getRight();
        }while(h != header);

        return min_col;
    }



    // Search for exact covers
    void search(int k)
    {

        Node rowNode;
        Node rightNode;
        Node leftNode;
        Node column;

        // if no column left, then we must
        // have found the solution
        if(header.getRight() == header)
        {
            handleSolution(solutions);
            return;
        }

        // choose column deterministically
        column = getMinColumn();

        // cover chosen column
        cover(column);

        for(rowNode = column.getDown(); rowNode != column;
            rowNode = rowNode.getDown() )
        {
            solutions.add(rowNode);


            for(rightNode = rowNode.getRight(); rightNode != rowNode;
                rightNode = rightNode.getRight())
                cover(rightNode);

            // move to level k+1 (recursively)
            search(k+1);

            // if solution in not possible, backtrack (uncover)
            // and remove the selected row (set) from solution
            solutions.remove(solutions.size()-1);

            column = rowNode.getColumn();
            for(leftNode = rowNode.getLeft(); leftNode != rowNode;
                leftNode = leftNode.getLeft())
                uncover(leftNode);
        }

        uncover(column);
    }

    public abstract void handleSolution(ArrayList<Node> solutions);

    // test
    public static void main(String args [])
    {
	    /*
	     Example problem

	     X = {1,2,3,4,5,6,7}
	     set-1 = {1,4,7}
	     set-2 = {1,4}
	     set-3 = {4,5,7}
	     set-4 = {3,5,6}
	     set-5 = {2,3,6,7}
	     set-6 = {2,7}
	     set-7 = {1,4}

	     Solutions : {6 ,4, 2} and {6, 4, 7}
	    */


        int [][] Matrix = {

                {1,0,0,1,0,0,1},
                {1,0,0,1,0,0,0},
                {0,0,0,1,4,0,1},
                {0,0,1,0,1,1,0},
                {0,1,1,0,0,1,1},
                {0,1,0,0,0,0,1},
                {1,0,0,1,0,0,0}


        };

//	   DancingLinks d = new DancingLinks();
//
//	   d.createToridolMatrix(Matrix);
//
//	   d.search(0);
//




    }

}