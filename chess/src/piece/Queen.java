package piece;

import mypack.GamePanel;
import mypack.Typeo;

public class Queen extends Piece{
	
	public Queen(int color,int col,int row){
		super(color,col,row);

		type=Typeo.QUEEN;

		if(color==GamePanel.WHITE){
			image=getImage("res/piece/w-queen");
		}
		else{
			image=getImage("res/piece/b-queen");
		}
	}

	public boolean canMove(int targetCol,int targetRow){
		
		if(isWithinBoard(targetCol,targetRow) && !isSameSquare(targetCol,targetRow)){
				//rook
			if(targetCol==preCol || targetRow==preRow){
				if(isValidSquare(targetCol,targetRow) && !pieceIsOnStraightLine(targetCol,targetRow)){
					return true;
				}
			}

			//bishop
			if(Math.abs(targetCol-preCol)==Math.abs(targetRow-preRow)){
				if(isValidSquare(targetCol,targetRow) && !pieceIsOnDiagonal(targetCol,targetRow)){
					return true;
				}
			}
		}

		return false;
	}
}