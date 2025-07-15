package piece;

import mypack.Board;
import mypack.GamePanel;
import mypack.Typeo;

import java.io.*; //need this for filestream
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
//import java.awt.*;

import javax.imageio.ImageIO;
//import javax.swing.*;

//import org.w3c.dom.events.MouseEvent;

public class Piece{
	
	public BufferedImage image;
	public Typeo type;
	public int x,y;
	public int col,row,preCol,preRow;
	public int color;
	public Piece hittingP;
	public boolean moved,twoStepped;//make this a pawn exclusive twoStepped;

	public Piece(int color,int col,int row){
		
		this.color=color;
		this.col=col;
		this.row=row;
		x=getX(col);
		y=getY(row);
		preCol=col;
		preRow=row;
	}

	public BufferedImage getImage(String imagePath){
		
		BufferedImage image=null;

		try{
			image=ImageIO.read(new FileInputStream(imagePath+".png"));
		}

		catch(IOException e){
			e.printStackTrace();
		}

		return image;
	}

	public int getX(int col){return (col*Board.SQUARE_SIZE);}
	public int getY(int row){return (row*Board.SQUARE_SIZE);}

	public int getCol(int x){return x/Board.SQUARE_SIZE;}
	public int getRow(int y){return y/Board.SQUARE_SIZE;}

    public int getIndex(){
		for(int index=0;index<GamePanel.pieces.size();index++){
			if(GamePanel.pieces.get(index)==this){
				return index;
			}
		}
		return 0;
	}

	public boolean canMove(int targetCol,int targetRow){
		return false;
	}

	public boolean isWithinBoard(int targetCol,int targetRow){
		if(targetCol>=1 && targetCol<=8 && targetRow>=1 && targetRow<=8){return true;}
		return false;
	}

	public boolean isSameSquare(int targetCol,int targetRow){
		if(targetCol==preCol && targetRow==preRow){return true;}
		return false;
	}

	public Piece gettingHitP(int targetCol,int targetRow){
		for(Piece piece:GamePanel.pieces){
			if(piece.col==targetCol && piece.row==targetRow && piece!=this){
				return piece;
			}
		}
		return null;
	}
	
	public boolean isValidSquare(int targetCol,int targetRow){

		hittingP=gettingHitP(targetCol,targetRow);

		if(hittingP==null){
			return true;
		}
		
		else{//this square is occupied
			if(hittingP.color !=this.color){
				return true;
			}
			else{
				hittingP=null;
			}
		}
		return false;
	}
	
	public boolean pieceIsOnStraightLine(int targetCol,int targetRow){
		//for left
		for(int c=preCol-1;c>targetCol;c--){
			for(Piece piece:GamePanel.pieces){
				if(piece.col==c && piece.row==targetRow){
					hittingP=piece;
					return true;
				}
			}
		}

		//for right
		for(int c=preCol+1;c<targetCol;c++){
			for(Piece piece:GamePanel.pieces){
				if(piece.col==c && piece.row==targetRow){
					hittingP=piece;
					return true;
				}
			}
		}
		//for up
		for(int r=preRow-1;r>targetRow;r--){
			for(Piece piece:GamePanel.pieces){
				if(piece.row==r && piece.col==targetCol){
					hittingP=piece;
					return true;
				}
			}
		}

		for(int r=preRow+1;r<targetRow;r++){
			for(Piece piece:GamePanel.pieces){
				if(piece.row==r && piece.col==targetCol){
					hittingP=piece;
					return true;
				}
			}
		}

		return false;
	}

	public boolean pieceIsOnDiagonal(int targetCol,int targetRow){

		if(targetRow<preRow){
			//up left
			for(int c=preCol-1;c>targetCol;c--){
				int diff=Math.abs(c-preCol);
				for(Piece piece : GamePanel.pieces){
					if(piece.col==c && piece.row==preRow-diff){
						hittingP=piece;
						return true;
					}
				}
			}

			//up right
			for(int c=preCol+1;c<targetCol;c++){
				int diff=Math.abs(c-preCol);
				for(Piece piece : GamePanel.pieces){
					if(piece.col==c && piece.row==preRow-diff){
						hittingP=piece;
						return true;
					}
				}
			}
		}

		if(targetRow>preRow){
			//down left
			for(int c=preCol-1;c>targetCol;c--){
				int diff=Math.abs(c-preCol);
				for(Piece piece : GamePanel.pieces){
					if(piece.col==c && piece.row==preRow+diff){
						hittingP=piece;
						return true;
					}
				}
			}

			//down right
			for(int c=preCol+1;c<targetCol;c++){
				int diff=Math.abs(c-preCol);
				for(Piece piece : GamePanel.pieces){
					if(piece.col==c && piece.row==preRow+diff){
						hittingP=piece;
						return true;
					}
				}
			}
		}

		return false;
	}

	public void draw(Graphics2D g2){

		//method1: piece is only drawn when preCol changes at the moves end
		g2.drawImage(image,getX(preCol),getY(preRow),Board.SQUARE_SIZE,Board.SQUARE_SIZE,null);

		//method2:piece is drawn along side as piece's location is cosntanly changes wherever cursor is
		//g2.drawImage(image,x,y,Board.SQUARE_SIZE,Board.SQUARE_SIZE,null);
	}
}

