package mypack;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.text.html.HTMLEditorKit.Parser;

import java.awt.*;
import java.util.*;
import piece.*;

public class GamePanel extends JPanel implements Runnable{
    
    public static final int WIDTH=1500;
    public static final int HEIGHT=1000;
    final int FPS=60;
    Thread gameThread;
    Board board=new Board();
	Mouse mouse=new Mouse();
	MoveLogger moveLogger=new MoveLogger("logger.txt");

	//BLINK
	public Timer blinkTimer;
	boolean isPieceVisible=true;
	final int BLINK_DELAY=300;

    //COLOR
    public static final int WHITE=10;
    public static final int BLACK=20;
    int currentColor = WHITE;
	
	//MENU STUFF
	private JComboBox<String> optionsBox;
	String selectedOption="Color OG";
	boolean labelvar=true;
	boolean undoVar;

	//NOTATION
	public static int moveCount=1;
	static String[] Moves=new String[100];
	static String[] capturedPiecesArr=new String[100];
	
	//BOOLEANS
	boolean canMove,validSquare,promotion,kingInCheck;
	boolean kingInCheck2;
	
	public String tempString;

	boolean gameOver,staleMate;
	boolean castled,castleL,castleR;

    //PIECES
    public static ArrayList<Piece> pieces= new ArrayList<>();
	public static ArrayList<Piece> pieces2= new ArrayList<>();
	public static ArrayList<Piece> tempPieces=new ArrayList<>();
	public static ArrayList<Piece> capturedPieces2=new ArrayList<>();
	public static Stack<Piece> capturedPieces=new Stack<>();
	public static ArrayList<Piece> promoPieces=new ArrayList<>();
	Piece activeP,checkingP,tempHittingP,tempPromoP;
	Piece undoP;

	public static Piece castlingP;

    public GamePanel(){

        setPreferredSize(new Dimension(WIDTH,HEIGHT));
        setBackground(Color.black);

		addMouseMotionListener(mouse);
		addMouseListener(mouse);

        setPieces();
		//setPieces2();
		copyPieces(pieces,pieces2);

		//menu stuff
		String[] options={"Color OG","Color NEW","Label (ON/OFF)","Undo"};
		optionsBox=new JComboBox<>(options);
		optionsBox.setSelectedItem(selectedOption);
		optionsBox.setBounds(900,90,90,30);

		//had to use lambda expression to get a successful compilation to avoid the error 
		optionsBox.addActionListener(e->{
			
				selectedOption =(String) optionsBox.getSelectedItem();

				if(selectedOption=="Color OG"){
					//Board.colouro=true;
					Board.xcol=new Color(240,240,240);
					Board.ycol=new Color(75,72,71);
					System.out.println("bord1");
				}

				if(selectedOption=="Color NEW"){
					//Board.colouro=false;
					Board.xcol=new Color(210, 165, 125);
					Board.ycol=new Color(175, 115, 70);
					System.out.println("bord2");
				}

				if(selectedOption=="Label (ON/OFF)"){
					
					if(labelvar==true){
						labelvar=false;
						System.out.println("truesl");
					}

					else{
						labelvar=true;
						System.out.println("laflse");
					}
					
				}
				
				if(selectedOption=="Undo"){

					if(activeP==null){
						undoVar=true;
						Undoer();
						changePlayer2();
					}
				}

		});
		add(optionsBox);

		/*JButton b=new JButton("Click Here");  
    	b.setBounds(85,110,105,50);*/  

		//BLINKING
		blinkTimer = new Timer(300, e -> {
            isPieceVisible = !isPieceVisible;
        });

    }

    public void launchGame(){

        gameThread = new Thread(this);
        gameThread.start();
    }

	private void copyPieces(ArrayList<Piece> source, ArrayList<Piece> target){
		target.clear();
		for(int i=0; i<source.size(); i++){
			target.add(source.get(i));
		}
	}

    @Override
    public void run() {

		// GAME LOOP
		double drawInterval = 100000000 / FPS; //originally 10000000
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;

		while (gameThread != null) {
			currentTime = System.nanoTime();

			delta += (currentTime - lastTime) / drawInterval;
			lastTime = currentTime;

			if (delta >= 1) {
				update();
				repaint();
				delta--;
			}
		}
	}

    public void update(){

		if(promotion){
			promoting();
		}

		else{
			if(mouse.clicked){
				if(activeP==null){
					for(Piece piece:pieces){

						if(piece.color==currentColor && 
							piece.col==mouse.locx/Board.SQUARE_SIZE && 
							piece.row==mouse.locy/Board.SQUARE_SIZE){

							activeP=piece;
							//potMove(activeP);
							blinkTimer.start();
							mouse.clicked=false;
						}
					}
				}
				else{

					moveMent();
					if(validSquare){
						updatePosition2();
						//updatePosition();
						//checkCastling();
						isKingInCheck();
		
						if(canPromote()){
							promotion=true;
							tempPromoP=activeP;
							pieces.remove(activeP.getIndex());
						}
						else{
							changePlayer();//turns can be changed only after a valid move
							
							if(kingInCheck){
								if(canAnyone()==false){
									gameOver=true;
								}
							}

							else{
								if(canAnyone()==false){
									staleMate=true;
								}
							}
						}
					}
					else{
						resetPosition();
					}
					activeP=null;
					//for some reason now temphittingP doesnt reset so..... i can also reset it after updPos
					tempHittingP=null;
					blinkTimer.stop();
					isPieceVisible=true;
					mouse.clicked=false;
				}
			}
			if(activeP!=null){
				checkHover();
			}
		}	
    }

	private void checkHover(){
		canMove=false;

		activeP.x=(mouse.mx/100)*100;
		activeP.y=(mouse.my/100)*100;
		activeP.col=activeP.getCol(activeP.x);
		activeP.row=activeP.getRow(activeP.y);

		if(activeP.canMove(activeP.col, activeP.row)){
			if(isIllegal(activeP)==false && opponentCanCaptureKing()==false){
				canMove=true;
			}
		}
		if(checkCastling()==true){
			canMove=true;
		}
	}

	private void moveMent(){
		validSquare=false;

		activeP.x=(mouse.locx/100)*100;
		activeP.y=(mouse.locy/100)*100;
		activeP.col=activeP.getCol(activeP.x);
		activeP.row=activeP.getRow(activeP.y);

		if(activeP.canMove(activeP.col, activeP.row)){

			if(isIllegal(activeP)==false && opponentCanCaptureKing()==false){

				if(activeP.hittingP!=null){
					tempHittingP=activeP.hittingP;
					capturedPieces.push(activeP.hittingP);
					pieces.remove(activeP.hittingP.getIndex());
					System.out.println(tempHittingP.type+"x"+tempHittingP.color);
				}
				validSquare=true;
			}
		}

		if(checkCastling()==true){
			validSquare=true;
			doCastling();
		}
	}

	private void updatePosition(){

		activeP.preCol=activeP.col;
		activeP.preRow=activeP.row;
		activeP.moved=true;
	}

	private void resetPosition(){
		activeP.col=activeP.preCol;
		activeP.row=activeP.preRow;
	}

	private boolean isIllegal(Piece king){
		if(king.type==Typeo.KING){

			for(Piece piece:pieces){
				if(piece.color!=king.color){
					if(piece.canMove(king.col,king.row)){
						return true;
					}
				}
			}
		}
		return false;
	}

	private void updatePosition2(){

		int tempCol=activeP.preCol;
		int tempRow=activeP.preRow;

		//en passant
		if(activeP.type==Typeo.PAWN){
			if(Math.abs(activeP.row-activeP.preRow)==2){
				activeP.twoStepped=true;
			}
		}

		activeP.preCol=activeP.col;
		activeP.preRow=activeP.row;

		if(tempHittingP==null){
			if(castled==false){
				tempString=moveCount+") "+getMap((activeP.type).toString())+": "+getCharForNumber(tempCol)+(9-tempRow)+" -> "+getCharForNumber(activeP.preCol)+(9-activeP.preRow);
				System.out.println(tempString);
				Moves[moveCount-1]=tempString;
			}
		}

		else{
			tempString=moveCount+") "+getMap((activeP.type).toString())+": "+getCharForNumber(tempCol)+(9-tempRow)+" x "+getCharForNumber(activeP.preCol)+(9-activeP.preRow);
			System.out.println(tempString);
			Moves[moveCount-1]=tempString;
		}

		if(castled){
			if(castleL){
				tempString=moveCount+") "+"O-O-O";
			}
			if(castleR){
				tempString=moveCount+") "+"O-O";
			}
			if(currentColor==WHITE){
				tempString+=" s";
			}
			else{
				tempString+=" k";
			}
		}
		if(activeP.moved==false && (activeP.type==Typeo.PAWN || activeP.type==Typeo.KING)){
			tempString+=" fM";
		}
		Moves[moveCount-1]=tempString;
		moveLogger.logMove();
		activeP.moved=true;

	}

	private String getCharForNumber(int i) {
    	return String.valueOf((char)('a'+i - 1));
	}

	private int getNumberForChar(char i){
		return ((int) (i-96));
	}

	private String getMap(String x){
		
		Map<String,String> column = new HashMap<>();

		column.put("PAWN","P");
    	column.put("KNIGHT","N");
    	column.put("BISHOP","B");
		column.put("ROOK","R");
    	column.put("QUEEN","Q");
    	column.put("KING","K");
    	 //hashmap can be made inside of this function
		return column.get(x);
	}

	private boolean checkCastling(){
		
		if(activeP.type==Typeo.KING && activeP.moved==false){
			Piece king=getKing(false);//so king and activeP are the same which sucks
			int kingCol=5;
			int kingRow;
			if(currentColor==WHITE){kingRow=8;}
			else{kingRow=1;}

			if(kingInCheck==false){
				//System.out.println("printchecl"+kingCol+"x"+kingRow+"="+activeP.col+"x"+activeP.row);
				if(activeP.col==kingCol+2 && activeP.row==kingRow){//add a not null to .moved thing 
					//System.out.println("printchecl"+kingCol+"x"+kingRow+"="+activeP.col+"x"+activeP.row);
					if(onBoard2(kingCol+1,kingRow)==null && onBoard2(kingCol+2,kingRow)==null && onBoard2(kingCol+3,kingRow).moved==false){
						for(Piece piece: pieces){
							if(piece.color!=currentColor){

								if(piece.canMove(kingCol+1, kingRow)){
									return false;
								}
								if(piece.canMove(kingCol+2, kingRow)){
									return false;
								}
							}
						}
						//System.out.println((onBoard(kingCol+1, kingRow).type).toString());
						System.out.println("printchecl"+kingCol+"x"+kingRow+"="+activeP.col+"x"+activeP.row);
						castlingP=onBoard2(kingCol+3, kingRow);
						return true;
					}
					//System.out.println((onBoard2(kingCol+2, kingRow).type).toString());
				}
				if(activeP.col==kingCol-3 && activeP.row==kingRow){
					if(onBoard2(kingCol-1,kingRow)==null && onBoard2(kingCol-2,kingRow)==null &&
					 onBoard2(kingCol-3,kingRow)==null && onBoard2(kingCol-4,kingRow).moved==false){
						
						for(Piece piece: pieces){
							if(piece.color!=currentColor){

								if(piece.canMove(kingCol-1, kingRow)){
									return false;
								}
								if(piece.canMove(kingCol-2, kingRow)){
									return false;
								}
								if(piece.canMove(kingCol-3, kingRow)){
									return false;
								}
							}
						}
						castlingP=onBoard2(kingCol-4, kingRow);
						return true;
					}
				}
			}
		}	
		return false;
	}

	private void doCastling(){

		if(castlingP!=null){

			if(castlingP.col==1){
				castlingP.col+=3;
				castleL=true;
			}
			else if(castlingP.col==8){
				castlingP.col-=2;
				castleR=true;
			}
			castlingP.preCol=castlingP.col;
			castled=true;
		}
	}

	private void changePlayer(){
		if(currentColor==WHITE){
			currentColor=BLACK;
			moveCount++;
			for(Piece piece: pieces){
				if(piece.color==BLACK){
					piece.twoStepped=false;
				}
			}
		}
		else{
			currentColor=WHITE;
			moveCount++;
			for(Piece piece: pieces){
				if(piece.color==WHITE){
					piece.twoStepped=false;
				}
			}
		}
	}

	private void changePlayer2(){
		if(currentColor==WHITE){
			currentColor=BLACK;
			moveCount--;
		}
		else{
			currentColor=WHITE;
			moveCount--;
		}
	}

	private boolean opponentCanCaptureKing(){

		ArrayList<Piece> tempPieces=new ArrayList<>();
		tempPieces.clear();
		copyPieces(pieces, tempPieces);

		Piece king=getKing(false);

		for(Piece piece : tempPieces){

			if(piece.color!=king.color && piece.canMove(king.col, king.row)){//i am adding checkinP not null to avoif erros

				if(activeP.color==king.color && checkingP!=null && (activeP.col==checkingP.col && activeP.row==checkingP.row) 
					&& isIllegal(activeP)==false ){

						tempPieces.remove(checkingP);
						if(opponentCanCaptureKing2(tempPieces)==false){
							return false;
						}
					}
				return true;
			}
		}
		
		return false;
	}

	private boolean opponentCanCaptureKing2(ArrayList<Piece> listo){
		Piece king=getKing(false);

		for(Piece piece : listo){

			if(piece.color!=king.color && piece.canMove(king.col, king.row)){
				return true;
			}
		}

		return false;
	}

	private boolean opponentCanCaptureKing3(Piece pieceo){

		ArrayList<Piece> tempPieces=new ArrayList<>();
		tempPieces.clear();
		copyPieces(pieces, tempPieces);

		Piece king=getKing(false);

		for(Piece piece : tempPieces){

			if(piece.color!=king.color && piece.canMove(king.col, king.row)){//i am adding checkinP not null to avoif erros

				if(pieceo.color==king.color && checkingP!=null && (pieceo.col==checkingP.col && pieceo.row==checkingP.row) 
					&& isIllegal(pieceo)==false ){
						
						tempPieces.remove(checkingP);
						if(opponentCanCaptureKing2(tempPieces)==false){
							return false;
						}
					}
				return true;
			}
		}
		
		return false;
	}

	private boolean canAnyone(){

		for(Piece piece:pieces){
			if(piece.color==currentColor){
				if(potMove(piece).isEmpty()==false){
					return true;
				}
			}
		}
		return false;
	}

	private ArrayList potMove(Piece pieceo){
		ArrayList<String> positions = new ArrayList<>();
		positions.clear();
		Set<String> encounteredElements = new HashSet<>();
		String coord;
		//Piece tempPiece;
		int posx,posy;
		posx=pieceo.col;
		posy=pieceo.row;

		for(int i=1;i<9;i++){
			for(int j=1;j<9;j++){

				//ATTENTION WHAT IF
				pieceo.col=i;
				pieceo.row=j;

				if(pieceo.color==currentColor && pieceo.canMove(i,j) && opponentCanCaptureKing3(pieceo)==false){
					coord=i+"x"+j;
					System.out.println(coord.charAt(0)+"ZZ"+coord.charAt(2));

					if(encounteredElements.contains(coord)){
						break;
					}
					else{
						positions.add(coord);
                		encounteredElements.add(coord);
					}
				}
			}
		}
		pieceo.col=posx;
		pieceo.row=posy;
		return positions;
	}

	private boolean isKingInCheck(){//this only works for opponent color king i need a new one to run it for current king

		Piece king=getKing(true);

		for(Piece piece: pieces){
			if(piece.color!=king.color && piece.canMove(king.col, king.row)){
				kingInCheck=true;
				checkingP=piece;
				return true;
			}
		}
		
		checkingP=null;
		kingInCheck=false;

		return false;
	}

	private Piece getKing(boolean opponent){
		
		Piece king=null;

		for(Piece piece:pieces){
			if(opponent){
				if(piece.type==Typeo.KING && piece.color!=currentColor){
					king=piece;
				}
			}
			else{
				if(piece.type==Typeo.KING && piece.color==currentColor){
					king=piece;
				}
			}
		}
		return king;
	}

	private boolean canPromote(){

		if(activeP.type==Typeo.PAWN){

			if((currentColor==WHITE && activeP.row==1) || (currentColor==BLACK && activeP.row==8)){

				promoPieces.clear();
				promoPieces.add(new Rook(currentColor, 9, 3));
				promoPieces.add(new Knight(currentColor, 9, 4));
				promoPieces.add(new Queen(currentColor, 9, 5));
				promoPieces.add(new Bishop(currentColor, 9, 6));

				return true;

			}
		}
		return false;
	}

	private void promoting(){
		if(mouse.clicked==true){

			for(Piece pro: promoPieces){

				if(pro.col==mouse.locx/Board.SQUARE_SIZE && pro.row==mouse.locy/Board.SQUARE_SIZE){

					switch (pro.type) {
						case ROOK:pieces.add(new Rook(currentColor, tempPromoP.col, tempPromoP.row)); break;
						case KNIGHT:pieces.add(new Knight(currentColor, tempPromoP.col, tempPromoP.row)); break;
						case QUEEN:pieces.add(new Queen(currentColor, tempPromoP.col, tempPromoP.row)); break;
						case BISHOP:pieces.add(new Bishop(currentColor, tempPromoP.col, tempPromoP.row)); break;
					
						default:
							break;
					}

					pieces.remove(activeP);
					tempPromoP=null;
					promotion=false;
					isKingInCheck();
					//changePlayer();
				}
			}
		}
	}

	private String Parser(String x, int i){

		String[] result=x.split(" ");
		String from=result[2];

		if(result.length>=4){
			String what=result[3];
			if(i==1){
				return what;
			}
		}

		if(result.length>=5){
			String to=result[4];
			if(i==2){
				return to;
			}
		}
		if(result.length==6){
			String info=result[5];
			if(i==3){
				return info;
			}
		}

		if(i==0){
			return from;
		}
		return "";
	}

	private String Parser2(String x, int i){

		String[] result=x.split(" ");
		String castle=result[1];
		String castleColor=result[2];

		if(i==0){
			return castle;
		}
		if(i==1){
			return castleColor;
		}
		return "";
	}

	private String getMap3(String x){
		
		Map<String,String> column = new HashMap<>();

		column.put("P:","PAWN");
    	column.put("N:","KNIGHT");
    	column.put("B:","BISHOP");
		column.put("R:","ROOK");
    	column.put("Q:","QUEEN");
    	column.put("K:","KING");
    	
		return column.get(x);
	}

	private void Undoer(){
		System.out.println("move"+Moves[moveCount-2]);
		if(Parser(Moves[moveCount-2],1).equals("->")){
			int valx1,valy1,valx2,valy2;
			valx1=getNumberForChar(Parser(Moves[moveCount-2],0).charAt(0));
			valy1=9-(Parser(Moves[moveCount-2],0).charAt(1) - '0');
			
			valx2=getNumberForChar(Parser(Moves[moveCount-2],2).charAt(0));
			valy2=9-(Parser(Moves[moveCount-2],2).charAt(1) - '0');

			System.out.println(valx2+"p"+valy2);
			System.out.println(valx1+"q"+valy1);

			undoP=onBoard(valx2,valy2);

			undoP.preCol=valx1;
			undoP.preRow=valy1;
			undoP.col=undoP.preCol;
			undoP.row=undoP.preRow;
		}
		
		else if(Parser(Moves[moveCount-2],1).equals("x")){
			int valx1,valy1,valx2,valy2;
			valx1=getNumberForChar(Parser(Moves[moveCount-2],0).charAt(0));
			valy1=9-(Parser(Moves[moveCount-2],0).charAt(1) - '0');
			
			valx2=getNumberForChar(Parser(Moves[moveCount-2],2).charAt(0));
			valy2=9-(Parser(Moves[moveCount-2],2).charAt(1) - '0');

			System.out.println(valx2+"p"+valy2);
			System.out.println(valx1+"q"+valy1);

			undoP=onBoard(valx2,valy2);

			undoP.preCol=valx1;
			undoP.preRow=valy1;
			undoP.col=undoP.preCol;
			undoP.row=undoP.preRow;

			pieces.add(capturedPieces.pop());
			//System.out.println();
		}

		if(Parser(Moves[moveCount-2],3).equals("fM")){
			undoP.moved=false;
		}

		if(Parser2(Moves[moveCount-2],1).equals("s")){

			if(Parser2(Moves[moveCount-2],0).equals("O-O-O")){

				castlingP.col=1;
				castlingP.preCol=castlingP.col;

				undoP=onBoard(3,8);
				undoP.col=5;
				undoP.preCol=undoP.col;
				undoP.moved=false;
			}

			if(Parser2(Moves[moveCount-2],0).equals("O-O")){
				castlingP.col=8;
				castlingP.preCol=castlingP.col;

				undoP=onBoard(7,8);
				undoP.col=5;
				undoP.preCol=undoP.col;
				undoP.moved=false;
			}
		}
			
		if(Parser2(Moves[moveCount-2],1).equals("k")){
			if(Parser2(Moves[moveCount-2],0).equals("O-O-O")){

				castlingP.col=1;
				castlingP.preCol=castlingP.col;

				undoP=onBoard(3,1);
				undoP.col=5;
				undoP.preCol=undoP.col;
				undoP.moved=false;
			}

			if(Parser2(Moves[moveCount-2],0).equals("O-O")){
				castlingP.col=8;
				castlingP.preCol=castlingP.col;

				undoP=onBoard(7,1);
				undoP.col=5;
				undoP.preCol=undoP.col;
				undoP.moved=false;
			}
		}
	}

	public Piece onBoard(int targetCol, int targetRow){
		for(Piece piece:GamePanel.pieces){
			if(piece.col==targetCol && piece.row==targetRow){
				return piece;
			}
		}
		return null;
	}

	public Piece onBoard2(int targetCol, int targetRow){
		for(Piece piece:GamePanel.pieces){
			if(piece.preCol==targetCol && piece.preRow==targetRow){
				return piece;
			}
		}
		return null;
	}

    public void paintComponent(Graphics g){

        super.paintComponent(g);
        Graphics2D g2=(Graphics2D)g;

        board.draw(g2);

        //ToDo:make label relative to board
        
		if(labelvar){
			board.drawLabel(g2);
		}

        for(Piece p:pieces){

			if(p==activeP){continue;}
            p.draw(g2);
        }

		if(isPieceVisible && activeP!=null){
			activeP.draw(g2);
		}

		else if(isPieceVisible==false){
			g2.setColor(Color.BLACK);
			g2.fillRect(970,870,10,10);
		}

		if(activeP!=null){

			if(canMove){
				g2.setColor(Color.white);
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
				g2.fillRect((activeP.col*100), (activeP.row*100),Board.SQUARE_SIZE,Board.SQUARE_SIZE);
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
			}
		}

		//Displaying Moves
		/*if(tempString!=null){
			g2.setFont(new Font("Helvetica",Font.PLAIN,30));
			g2.drawString(tempString,1200,500);
		}*/
		
		if(moveCount>1){
			g2.setFont(new Font("Helvetica",Font.PLAIN,30));
			g2.drawString(Moves[moveCount-2],1200,500);
		}
		/*if(Moves[moveCount-1]!=null){
			g2.setFont(new Font("Helvetica",Font.PLAIN,30));

			System.out.println(Moves[moveCount-1]);
			g2.drawString(Moves[moveCount-1],1200,500);
		}*/
		//g2.drawString(Moves[moveCount-1],1200,500);

		//check messages make this a red sq later
		/*if(checkingP!=null)
		g2.setColor(Color.RED);*/


		//status messages
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setFont(new Font("Helvetica",Font.PLAIN,30));
        g2.setColor(Color.white);

		if(promotion){
			for(Piece pro:promoPieces){
				pro.draw(g2);
			}
		}
 
        if(currentColor==WHITE){
            g2.drawString("White's turn",950,750);
			if(checkingP!=null && checkingP.color==BLACK){
				g2.setColor(Color.red);
				g2.drawString("King in check",950 , 700);
			}
        }
        else{
            g2.drawString("Black's turn",950,250);
			if(checkingP!=null && checkingP.color==WHITE){
				g2.setColor(Color.red);
				g2.drawString("King in check",950 , 200);
			}
		}
		if(gameOver){
			g2.setColor(Color.red);
			g2.drawString("wijwiwiwiwn",500,500);
		}
		if(staleMate){
			g2.setColor(Color.red);
			g2.drawString("drawdrawdarw",500,500);
		}
    }

	public void setPieces() {

		pieces.add(new King(WHITE, 5, 8));
		pieces.add(new King(BLACK, 5, 1));

		pieces.add(new Pawn(WHITE, 1, 7));
		pieces.add(new Pawn(WHITE, 2, 7));
		pieces.add(new Pawn(WHITE, 3, 7));
		pieces.add(new Pawn(WHITE, 4, 7));
		pieces.add(new Pawn(WHITE, 5, 7));
		pieces.add(new Pawn(WHITE, 6, 7));
		pieces.add(new Pawn(WHITE, 7, 7));
		pieces.add(new Pawn(WHITE, 8, 7));

		pieces.add(new Rook(WHITE, 1, 8));
		pieces.add(new Rook(WHITE, 8, 8));

		pieces.add(new Knight(WHITE, 2, 8));
		pieces.add(new Knight(WHITE, 7, 8));

		pieces.add(new Bishop(WHITE, 3, 8));
		pieces.add(new Bishop(WHITE, 6, 8));

		pieces.add(new Queen(WHITE, 4, 8));

		pieces.add(new Pawn(BLACK, 1, 2));
		pieces.add(new Pawn(BLACK, 2, 2));
		pieces.add(new Pawn(BLACK, 3, 2));
		pieces.add(new Pawn(BLACK, 4, 2));
		pieces.add(new Pawn(BLACK, 5, 2));
		pieces.add(new Pawn(BLACK, 6, 2));
		pieces.add(new Pawn(BLACK, 7, 2));
		pieces.add(new Pawn(BLACK, 8, 2));

		pieces.add(new Rook(BLACK, 1, 1));
		pieces.add(new Rook(BLACK, 8, 1));

		pieces.add(new Knight(BLACK, 2, 1));
		pieces.add(new Knight(BLACK, 7, 1));

		pieces.add(new Bishop(BLACK, 3, 1));
		pieces.add(new Bishop(BLACK, 6, 1));

		pieces.add(new Queen(BLACK, 4, 1));
	}

	public void setPieces2(){
		
		pieces.add(new King(WHITE, 4, 4));
		pieces.add(new King(BLACK, 5, 1));

		pieces.add(new Pawn(WHITE, 1, 7));
	}
}
