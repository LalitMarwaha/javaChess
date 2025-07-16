package piece;

import mypack.GamePanel;
import mypack.Typeo;

public class Rook extends Piece{
	
	public Rook(int color,int col,int row){
		super(color,col,row);

		type=Typeo.ROOK;

		if(color==GamePanel.WHITE){
			image=getImage("/res/piece/w-rook");
		}
		else{
			image=getImage("/res/piece/b-rook");
		}
	}

	public boolean canMove(int targetCol,int targetRow){
		if(isWithinBoard(targetCol,targetRow) && !isSameSquare(targetCol,targetRow)){
			if(targetCol==preCol || targetRow==preRow){
				if(isValidSquare(targetCol,targetRow) && !pieceIsOnStraightLine(targetCol,targetRow)){
					return true;
				}
			}
		}
		return false;
	}
}
