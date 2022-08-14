/**
 * @(#)FinalProject.java
 *
 *
 * @author 
 * @version 1.00 2021/5/23
 */
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.imageio.*;
import java.util.ArrayList;

public class FinalProject {
	public static void main(String args[]){
		JFrame j = new JFrame();
		MyPanel panel = new MyPanel();
		j.setSize(1300, 1020);
		j.add(panel);
		
		j.addKeyListener(panel);
		j.addMouseListener(panel);
		j.setVisible(true);
		
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
class MyPanel extends JPanel implements ActionListener, MouseListener, KeyListener {
	private Timer time;
	private int moveX, moveY;
	private Snake player;
	private Thing apple;
	private Thing gun;
	private Thing bullet;
	private boolean bulletCreated = false;
	private String bulletDir = "";
	private int bulletTimer = 0;
	private Particles blocks;
	private int max = 32, min = 0;
	private boolean going = true;
	
	public MyPanel(){
		setVisible(true);
		time = new Timer(100, this);
		time.start();
		moveX = 30;
		moveY = 0;
		player = new Snake(510, 510);
		apple = new Thing((int)(Math.random()*(max-min+1)+min)*30, (int)(Math.random()*(max-min+1-5)+min+5)*30);
		blocks = new Particles();
	}
	
	public void drawGrid(Graphics g){
		g.setColor(new Color(255,255,255));
		g.fillRect(0, 0, 1200, 1020);
		g.setColor(Color.BLACK);
		
		g.drawString("Use arrow keys to move.", 1030, 30);
		g.drawString("Click to shoot the asteroids.", 1030, 50);
		g.drawString("Score: " + player.getSize(), 1030, 70);
		for(int i = 0; i <= 1020; i+= 30)
			g.drawLine(i, 0, i, 1020);
		for(int i = 0; i < 1020; i+= 30)
			g.drawLine(0, i, 1020, i);
	}
	
	public void drawSnake(Graphics g){
		for(int i = 0; i < player.getSize(); i ++){
			int tempx = player.getPart(i).getX();
			int tempy = player.getPart(i).getY();
			g.setColor(new Color(0, 0, 0));
			g.drawRect(tempx, tempy, 30, 30);
			
			g.setColor(new Color(0, 204, 0));
			g.fillRect(tempx, tempy, 30, 30);
		}
		int tempx2 = player.getPart(player.getSize()).getX();
		int tempy2 = player.getPart(player.getSize()).getY();
		g.setColor(new Color(255,255,255));
		g.fillRect(tempx2, tempy2, 30, 30);
		g.setColor(new Color(0, 0, 0));
		g.drawRect(tempx2, tempy2, 30, 30);	
	}
	
	public void drawApple(Graphics g){
		g.setColor(new Color(255, 0, 0));
		g.fillRect(apple.getX(), apple.getY(), 30, 30);
	}
	
	public void drawBlock(Graphics g){
		for(int i= 0; i < blocks.getSize(); i++){
			g.setColor(new Color(128, 128, 128));
			g.fillRect(blocks.getPart(i).getX(), blocks.getPart(i).getY(), 30, 30);
		}
	}
	
	public void drawGun(Graphics g) {
		g.setColor(new Color(255, 255, 0));
		int i= 0;
		gun = new Thing(player.getPart(i).getX(), player.getPart(i).getY());
		if(moveX == 30){
			g.fillRect(gun.getX()+30, gun.getY()+10, 30,10);
			gun.setPos(gun.getX()+30, gun.getY());
		}
		
		if(moveX == -30){
			g.fillRect(gun.getX()-30, gun.getY()+10, 30,10);
			gun.setPos(gun.getX()-30, gun.getY());
		}
		
		if(moveY == 30){
			g.fillRect(gun.getX()+10, gun.getY()+30, 10, 30);
			gun.setPos(gun.getX(), gun.getY()+30);
		}
		if(moveY == -30){
			g.fillRect(gun.getX()+10, gun.getY()-30, 10, 30);
			gun.setPos(gun.getX(), gun.getY()-30);
		}
	}
	
	public void drawBullet(Graphics g) {
		if(bulletCreated && !(bullet.getX() > 1020 || bullet.getX() < 0 || bullet.getY() > 1020 || bullet.getY() < 0)){
			g.setColor(new Color(0, 0, 255));
			g.fillRect(bullet.getX(), bullet.getY(), 30, 30);
		}
	}
	
	public void bulletIncrementer() {
		if(bulletCreated){
			bulletCheck();
			bulletTimer += 100;
			if(bulletDir == "UP"){
				bullet.setPos(bullet.getX(), bullet.getY() - 60);
			}
			if(bulletDir == "DOWN"){
				bullet.setPos(bullet.getX(), bullet.getY() + 60);
			}
			if(bulletDir == "RIGHT"){
				bullet.setPos(bullet.getX()+60, bullet.getY());
			}
			if(bulletDir == "LEFT"){
				bullet.setPos(bullet.getX()-60, bullet.getY());
			}
			bulletCheck();
			if(bulletTimer >= 1000){
				bulletCreated = false;
				bulletTimer = 0;
			}
		}
		
		
	}
	
	public void blockIncrementer(){
		for(int i = 0; i < 33; i++){
			int check = (int)(Math.random()*100);
			if(check == 0){
				blocks.addWall(i * 30, 0);
			}
		}
		blocks.inc();
		bulletCheck();
		dieCheck();
	}
	
	public void appleCheck(){
		if(apple.getX() == player.getPart(0).getX() && apple.getY() == player.getPart(0).getY()){
			player.incSize();
			apple.setPos((int)(Math.random()*(max-min+1)+min)*30, (int)(Math.random()*(max-min+1-5)+min+5)*30);
		}
	}
	
	public void dieCheck(){
		bulletCheck();
		if(player.getPart(0).getX() > 1020 || player.getPart(0).getX() < 0 || player.getPart(0).getY() > 1020 || player.getPart(0).getY() < 0)
			going = false;
		for(int i = 1; i < player.getSize(); i++){
			if(player.getPart(i).getX() == player.getPart(0).getX() && player.getPart(i).getY() == player.getPart(0).getY()){
				going = false;
				break;
			}
		}
		for(int i = 0; i < blocks.getSize(); i++){
			if(player.getPart(0).getX() == blocks.getPart(i).getX() && player.getPart(0).getY() == blocks.getPart(i).getY()){
				going = false;
				break;
			}
		}
	}
	
	public void bulletCheck(){
		if(bulletCreated){
			for(int i = 1; i < player.getSize(); i++){
				if(player.getPart(i).getX() == bullet.getX() && player.getPart(i).getY() == bullet.getY()){
					player.setSize(i);
				}else if(bulletDir == "RIGHT" && (player.getPart(i).getX() == bullet.getX()-30 && player.getPart(i).getY() == bullet.getY()))
					player.setSize(i);
				else if(bulletDir == "LEFT" && (player.getPart(i).getX() == bullet.getX()+30 && player.getPart(i).getY() == bullet.getY()))
					player.setSize(i);
				else if(bulletDir == "DOWN" && (player.getPart(i).getX() == bullet.getX() && player.getPart(i).getY() == bullet.getY()-30))
					player.setSize(i);
				else if(bulletDir == "UP" && (player.getPart(i).getX() == bullet.getX() && player.getPart(i).getY() == bullet.getY()+30))
					player.setSize(i);
				
			}
			
			for(int i = 1; i < blocks.getSize(); i++){
				if(blocks.getPart(i).getX() == bullet.getX() && blocks.getPart(i).getY() == bullet.getY()){
					blocks.removeWall(i);
					i--;
				}else if(bulletDir == "RIGHT" && (blocks.getPart(i).getX() == bullet.getX()-30 && blocks.getPart(i).getY() == bullet.getY())){
					blocks.removeWall(i);
					i--;
				}else if(bulletDir == "LEFT" && (blocks.getPart(i).getX() == bullet.getX()+30 && blocks.getPart(i).getY() == bullet.getY())){
					blocks.removeWall(i);
					i--;
				}else if(bulletDir == "DOWN" && (blocks.getPart(i).getX() == bullet.getX() && blocks.getPart(i).getY() == bullet.getY()-30)){
					blocks.removeWall(i);
					i--;
				}else if(bulletDir == "UP" && (blocks.getPart(i).getX() == bullet.getX() && blocks.getPart(i).getY() == bullet.getY()+30)){
					blocks.removeWall(i);
					i--;
				}
				
			}
			
		}
	}
	
	public void paintComponent(Graphics g){
		//use
		if(going){
			dieCheck();
			drawGrid(g);
			drawSnake(g);
			drawBlock(g);
			drawApple(g);
			drawGun(g);
			drawBullet(g);
		}
		
	}
	
	public void actionPerformed(ActionEvent e){
		if(going){
			dieCheck();
			player.addCoord(moveX, moveY);
			dieCheck();
			blockIncrementer();
			bulletIncrementer();
			bulletCheck();
			appleCheck();
			repaint();
		}
		
	}
	
	public void mouseClicked(MouseEvent e){
		//use
		if(!bulletCreated){
			bulletTimer = 0;
			bullet = new Thing(gun.getX(), gun.getY());
			if(moveX == 30)
				bulletDir = "RIGHT";
			if(moveX == -30)
				bulletDir = "LEFT";
			if(moveY == -30)
				bulletDir = "UP";
			if(moveY == 30)
				bulletDir = "DOWN";
			bulletCreated = true;
		}
	}
	public void mousePressed(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	
	public void keyPressed(KeyEvent e){
		//use
		int code = e.getKeyCode();
		if(code == KeyEvent.VK_RIGHT){
			moveX = 30;
			moveY = 0;
		}
		if(code == KeyEvent.VK_LEFT){
			moveX = -30;
			moveY = 0;
		}
		if(code == KeyEvent.VK_DOWN){
			moveY = 30;
			moveX = 0;
		}
		if(code == KeyEvent.VK_UP){
			moveY = -30;
			moveX = 0;
		}
	}
	public void keyReleased(KeyEvent e)	{
		//use
		
	}
	public void keyTyped(KeyEvent e){}
}

class Particles {
	
	private ArrayList<Thing> walls;
	
	public Particles(){
		walls = new ArrayList<Thing>();
	}
	
	public void inc(){
		for(int i = 0; i < getSize(); i++){
			getPart(i).setPos(getPart(i).getX(), getPart(i).getY()+30);
			if(getPart(i).getY() > 1020)
				walls.remove(i);
			
		}
	}
	
	public Thing getPart(int x){
		return walls.get(x);
	}
	
	public void addWall(int x, int y){
		walls.add(new Thing(x, y));
	}
	
	public void removeWall(int x){
		walls.remove(x);
	}
	
	public int getSize(){
		return walls.size();
	}
	
}

class Snake {
	private int size;
	private ArrayList<Thing> bod;
	
	public Snake(int x, int y){
		bod = new ArrayList<Thing>();
		addBod(x,y);
		addBod(x-30,y-30);
		size = 1;
	}
	
	public int getSize(){
		return size;
	}
	
	public void incSize(){
		size++;
	}
	
	public void setSize(int size){
		size = size;
	}
	
	public Thing getPart(int x){
		return bod.get(x);
	}
	
	public void addBod(int x, int y){
		Thing temp = new Thing(x, y);
		bod.add(0,temp);
	}
	
	public void incHead(int amt, int amt2){
		int headx = bod.get(0).getX();
		int heady = bod.get(0).getY();
		headx += amt;
		heady += amt2;
		addBod(headx, heady);
	}
	
	public void addCoord(int moveX, int moveY){
		addBod(getPart(0).getX() + moveX, getPart(0).getY() + moveY);
	}
	
}

class Thing {
	private int x, y;
	
	public Thing(int x1, int y1){
		x = x1;
		y = y1;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public void setPos(int x, int y){
		this.x = x;
		this.y = y;
	}
	
}