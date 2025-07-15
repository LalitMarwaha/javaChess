package piece;

import mypack.GamePanel;
import mypack.Typeo;

public class Knight extends Piece {
    
    public Knight(int color, int col, int row){
        super(color, col, row);

        type=Typeo.KNIGHT;

        if(color==GamePanel.WHITE){
            image=getImage("res/piece/w-knight");
        }

        else{
            image=getImage("res/piece/b-knight");
        }
    }

    public boolean canMove(int targetCol,int targetRow){
		
		if(isWithinBoard(targetCol,targetRow)){
			
			if(Math.abs(targetCol-preCol)*Math.abs(targetRow-preRow)==2){
				
				if(isValidSquare(targetCol,targetRow)){
					return true;
				}
			}
		}
		return false;
	}
}
