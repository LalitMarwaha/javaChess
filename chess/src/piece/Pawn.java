package piece;

import mypack.GamePanel;
import mypack.Typeo;

public class Pawn extends Piece {

    public Pawn(int color,int col,int row){
		super(color,col,row);

		type=Typeo.PAWN;
        
		if(color==GamePanel.WHITE){
			image=getImage("/res/piece/w-pawn");
		}
		else{
			image=getImage("/res/piece/b-pawn");
		}
	}

	public boolean canMove(int targetCol,int targetRow){
		if(isWithinBoard(targetCol,targetRow) && !isSameSquare(targetCol,targetRow)){
			
			//Define the move value based on its color
			int moveValue;
			if(color==GamePanel.WHITE){
				moveValue=-1;
			}

			else{
				moveValue=1;
			}

			//check the hitting piece
			hittingP=gettingHitP(targetCol,targetRow);

			//1 square movement
			if(targetCol==preCol && targetRow==preRow+moveValue && hittingP==null ){
				return true;
			}

			//2square movement
			if(targetCol==preCol && targetRow==preRow+moveValue*2 && hittingP==null && moved==false && !pieceIsOnStraightLine(targetCol,targetRow)){
				return true;
			}

			//diagonal and capture
			if(Math.abs(targetCol-preCol)==1 && targetRow==preRow+moveValue && hittingP!=null && hittingP.color!=color){
				return true;
			}

			//en passant peasant? 

			if(Math.abs(targetCol-preCol)==1 && targetRow==preRow+moveValue){
				for(Piece piece:GamePanel.pieces){

					if(piece.col==targetCol && /*piece.row==targetRow &&*/ piece.twoStepped==true){

						hittingP=piece;
						System.out.println(hittingP.col+"x"+hittingP.row);
						return true;
					}
				}
			}
		}

		return false;
	}
}
