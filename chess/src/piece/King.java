package piece;
import mypack.GamePanel;
import mypack.Typeo;

public class King extends Piece{
	
	public King(int color,int col,int row){
		super(color,col,row);

		type=Typeo.KING;

		if(color==GamePanel.WHITE){
			image=getImage("res/piece/w-king");
		}
		else{
			image=getImage("res/piece/b-king");
		}
	}

	public boolean canMove(int targetCol,int targetRow){
		
		if(isWithinBoard(targetCol,targetRow)){

			//Movement
			if(Math.abs(targetCol-preCol)+Math.abs(targetRow-preRow)==1 ||
				Math.abs(targetCol-preCol)*Math.abs(targetRow-preRow)==1){

					if(isValidSquare(targetCol,targetRow)){
						return true;
					}
			}

			//Castling
			/*if(moved==false){
				
				//short castle
				//here we also need to check if knight is empty or not either we can do the same thing as Left which is what i think should be done
				if(targetCol==preCol+2 && targetRow==preRow && !pieceIsOnStraightLine(targetCol,targetRow) && gettingHitP(targetCol,targetRow)==null){
					for(Piece piece:GamePanel.pieces){
						if(piece.col==preCol+3 && piece.row==preRow && piece.moved==false){
							GamePanel.castlingP=piece;
							return true;
						}
					}
				}

				//long castle
				if(targetCol==preCol-2 && targetRow==preRow && !pieceIsOnStraightLine(targetCol,targetRow) && gettingHitP(targetCol,targetRow)==null){
					Piece p[]=new Piece[2];
					for(Piece piece:GamePanel.pieces){

						if(piece.col==preCol-3 && piece.row==targetRow){
							p[0]=piece; //knight
						}

						if(piece.col==preCol-4 && piece.row==targetRow){
							p[1]=piece; //rook
						}

						if(p[0]==null && p[1]!=null && p[1].moved==false){
							GamePanel.castlingP=p[1];
							return true;
						}
					}
				}
			}*/
		}

		return false;
	}
}
