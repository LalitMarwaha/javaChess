package mypack;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Mouse extends MouseAdapter{

	public int mx,my;
	public int locx,locy;
	public boolean clicked;
	boolean pressed;

	//PrintStream ps = new PrintStream(new FileOutputStream("log.txt"));

	@Override
	public void mouseClicked(MouseEvent e){
		locx=e.getX();
		locy=e.getY();
		clicked=true;
		//System.out.println("mouselickd");
	}

	@Override
	public void mousePressed(MouseEvent e){
		pressed=true;
	}

	@Override
	public void mouseReleased(MouseEvent e){
		pressed=false;
	}

	@Override
	public void mouseDragged(MouseEvent e){
		/*mx=e.getX();
		my=e.getY();*/
	}

	@Override
	public void mouseMoved(MouseEvent e){
		mx=e.getX();
		my=e.getY();
	}
}
