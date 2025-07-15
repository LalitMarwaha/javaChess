package mypack;

import java.awt.*;

public class Board {

	final int MAX_COL = 8;
	final int MAX_ROW = 8;
	public static final int SQUARE_SIZE = 100;
	public static final int HALF_SQUARE_SIZE = SQUARE_SIZE / 2;
    int[] xcord={100,200,300,400,500,600,700,800},ycord={100,200,300,400,500,600,700,800};

	public static Color xcol=new Color(210, 165, 125);
	public static Color ycol=new Color(175, 115, 70);

	public void draw(Graphics2D g2) {

		for (int row = 0; row < MAX_ROW; row++) {
			for (int col = 0; col < MAX_COL; col++) {

				if((row+col)%2==0){
					g2.setColor(xcol);
				}

				else{
					g2.setColor(ycol);
				}


				//g2.fillRect(, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
                g2.fillRect(xcord[col],ycord[row], SQUARE_SIZE, SQUARE_SIZE);
				g2.setColor(Color.BLACK);
				g2.drawRect(xcord[col],ycord[row], SQUARE_SIZE, SQUARE_SIZE);
			}	
		}
	}

    public void drawLabel(Graphics2D g2){

		g2.setFont(new Font("Helvetica",Font.PLAIN,20));
			g2.setColor(Color.white);

			g2.drawString("a",145,80);
			g2.drawString("8",70,160);
			g2.drawString("a",145,930);
			g2.drawString("8",920,160);
			
			g2.drawString("b",245,80);
			g2.drawString("7",70,260);
			g2.drawString("b",245,930);
			g2.drawString("7",920,260);
			
			g2.drawString("c",345,80);
			g2.drawString("6",70,360);
			g2.drawString("c",345,930);
			g2.drawString("6",920,360);
			
			g2.drawString("d",445,80);
			g2.drawString("5",70,460);
			g2.drawString("d",445,930);
			g2.drawString("5",920,460);
			
			g2.drawString("e",545,80);
			g2.drawString("4",70,560);
			g2.drawString("e",545,930);
			g2.drawString("4",920,560);
			
			g2.drawString("f",645,80);
			g2.drawString("3",70,660);
			g2.drawString("f",645,930);
			g2.drawString("3",920,660);
			
			g2.drawString("g",745,80);
			g2.drawString("2",70,760);
			g2.drawString("g",745,930);
			g2.drawString("2",920,760);
			
			g2.drawString("h",840,80);
			g2.drawString("1",70,860);
			g2.drawString("h",840,930);
			g2.drawString("1",920,860);

	}
}



/*package mypack;

import java.awt.*;
import java.awt.Graphics2D;

public class Board {

	final int MAX_COL = 8;
	final int MAX_ROW = 8;
	static Color xcol,ycol,col1,col2,col3,col4;
	static boolean colouro;
	//static boolean labelVisible;
	int[] xcord={50,150,250,350,450,550,650,750},ycord={50,150,250,350,450,550,650,750};
	public static final int SQUARE_SIZE = 100;
	public static final int HALF_SQUARE_SIZE = SQUARE_SIZE / 2;

	public void draw(Graphics2D g2) {

		//g2.setStroke(new BasicStroke(4));

		for (int row = 0; row < MAX_ROW; row++) {
			for (int col = 0; col < MAX_COL; col++) {

				if(colouro==false){
					
					xcol=col1;
					ycol=col2;
				}

				else{
					xcol=col3;
					ycol=col4;
				}

				if((row+col)%2==0){
					g2.setColor(xcol);
				}

				else{
					g2.setColor(ycol);
				}

				g2.fillRect(xcord[col],ycord[row], SQUARE_SIZE, SQUARE_SIZE);
				g2.setColor(Color.BLACK);
				g2.drawRect(xcord[col],ycord[row], SQUARE_SIZE, SQUARE_SIZE);
			}
		}
	}

	public void drawLabel(Graphics2D g2){

		g2.setFont(new Font("Helvetica",Font.PLAIN,20));
			g2.setColor(Color.white);

			g2.drawString("a",100,870);
			g2.drawString("8",870,100);
			g2.drawString("a",90,30);
			g2.drawString("8",30,100);
			
			g2.drawString("b",200,870);
			g2.drawString("7",870,200);
			g2.drawString("b",190,30);
			g2.drawString("7",30,200);
			
			g2.drawString("c",300,870);
			g2.drawString("6",870,300);
			g2.drawString("c",290,30);
			g2.drawString("6",30,300);
			
			g2.drawString("d",400,870);
			g2.drawString("5",870,400);
			g2.drawString("d",400,30);
			g2.drawString("5",30,400);
			
			g2.drawString("e",500,870);
			g2.drawString("4",870,500);
			g2.drawString("e",500,30);
			g2.drawString("4",30,500);
			
			g2.drawString("f",600,870);
			g2.drawString("3",870,600);
			g2.drawString("f",600,30);
			g2.drawString("3",30,600);
			
			g2.drawString("g",700,870);
			g2.drawString("2",870,700);
			g2.drawString("g",700,30);
			g2.drawString("2",30,700);
			
			g2.drawString("h",800,870);
			g2.drawString("1",870,800);
			g2.drawString("h",800,30);
			g2.drawString("1",30,800);

	}
}*/
