package piece;
import mypack.GamePanel;

public class Bishop extends Piece{
	
	public Bishop(int color,int col,int row){
		super(color,col,row);

		if(color==GamePanel.WHITE){
			image=getImage("res/piece/w-bishop");
		}
		else{
			image=getImage("res/piece/b-bishop");
		}
	}

	public boolean canMove(int targetCol,int targetRow){
		if(isWithinBoard(targetCol,targetRow) && !isSameSquare(targetCol,targetRow)){
			if(Math.abs(targetCol-preCol)==Math.abs(targetRow-preRow)){
				if(isValidSquare(targetCol,targetRow) && !pieceIsOnDiagonal(targetCol,targetRow)){
					return true;
				}
			}
		}

		return false;
	}
}
