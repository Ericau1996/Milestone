import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.*;

public class GUI extends JFrame{

    public boolean resetter = false;

    int spacing = 1;

    int neighs = 0;

    String wMes = "Nice job!";

    public int mx = -100;
    public int my = -100;

    public int reStartX = 0;
    public int reStartY = 5;
    public int MessageCenterX = reStartX +35;
    public int MessageCenterY = reStartY +35;

    public int timeX = 590;
    public int timeY = 5;

    public int wMesX = 200;
    public int wMesY = -50;

    public boolean flagger = false;

    public int flaggerX = 100;
    public int flaggerY = 5;

    public int flaggerCenterX = flaggerX+35;
    public int flaggerCenterY = flaggerY+35;

    public int sec = 0;
    public String restartButton = "R";

    public boolean message = true;

    public boolean win = false;
    public boolean lose = false;

    Random rand = new Random();

    Date startDate = new Date();
    Date endDate;

    int[][] mines = new int[10][10];
    int[][] neighbours = new int[10][10];
    boolean[][] revealed = new boolean[10][10];
    boolean[][] flagged = new boolean[10][10];

    public GUI(){
        this.setTitle("Minesweeper");
        this.setSize(726,770);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);

        for(int i = 0; i< 10; i++){
            for(int j = 0; j < 10; j++){
                if(rand.nextInt(100)<15){
                    mines[i][j]=1;
                }else{
                    mines[i][j]=0;
                }
                revealed[i][j] = false;
            }
        }

        for(int i = 0; i< 10; i++){
            for(int j = 0; j < 10; j++){
                neighs = 0;
                for(int m = 0; m< 10; m++){
                    for(int n = 0; n < 10; n++){
                        if(!(m == i && n ==j)) {
                            if (isN(i, j, m, n) == true)
                                neighs++;
                        }
                    }
                }
                neighbours[i][j] = neighs;
            }
        }

        Board board = new Board();
        this.setContentPane(board);

        Move move = new Move();
        this.addMouseMotionListener(move);

        Click click = new Click();
        this.addMouseListener(click);
    }

    public class Board extends JPanel{
        public void paintComponent(Graphics g){
            g.setColor(Color.GRAY);
            g.fillRect(0,0,720,720);
            g.setColor(Color.DARK_GRAY);
            for(int i = 0; i< 10; i++){
                for(int j = 0; j < 10; j++){
                    g.setColor(Color.DARK_GRAY);

                    if(revealed[i][j] == true){
                        g.setColor(Color.WHITE);
                        if (mines[i][j]==1){
                            g.setColor(Color.red);
                        }
                    }

                    if(mx >= spacing+i*72 && mx < spacing+i*72+72-2*spacing && my >= spacing+j*72+72+26 && my < spacing+j*72+26+72+72-2*spacing){
                        g.setColor(Color.lightGray);
                    }
                    g.fillRect(spacing+i*72,spacing+j*72+72, 72-2*spacing, 72-2*spacing);
                    if(revealed[i][j] == true){
                        g.setColor(Color.BLACK);
                        if(mines[i][j]==0 && neighbours[i][j] !=0) {
                            if(neighbours[i][j] == 1){
                                g.setColor(new Color(195,0,128));
                            }else if (neighbours[i][j] == 2){
                                g.setColor(new Color(90,80,38));
                            }else if (neighbours[i][j] == 3){
                                g.setColor(new Color(80,80,128));
                            }else if (neighbours[i][j] == 4){
                                g.setColor(new Color(30,77,110));
                            }else if (neighbours[i][j] == 5){
                                g.setColor(new Color(56,0,128));
                            }else if (neighbours[i][j] == 6){
                                g.setColor(new Color(0,72,128));
                            }else if (neighbours[i][j] == 7){
                                g.setColor(new Color(0,128,128));
                            }
                            g.setFont(new Font("Tahoma", Font.BOLD, 40));

                            g.drawString(Integer.toString(neighbours[i][j]), i * 72 + 23, j * 72 + 72 + 50);
                        }else if(mines[i][j]==1){
                            g.fillRect(spacing+i*72+7+20,spacing+j*72+72+15, 20,40);
                            g.fillRect(spacing+i*72+17,spacing+j*72+72+20+5, 40,20);
                            g.fillRect(spacing+i*72+2+20,spacing+j*72+72+5+15, 30,30);
                        }
                    }

                    if (flagged[i][j] == true){
                        g.setColor(Color.BLACK);
                        g.fillRect(i*72+30,j*72+72+10,5,45);
                        g.fillRect(i*72+17,j*72+72+45,30,10);
                        g.setColor(Color.RED);
                        g.fillRect(i*72+8,j*72+72+10,25,20);
                        g.setColor(Color.BLACK);
                        g.drawRect(i*72+8,j*72+72+10,25,20);
                    }
                }
            }
            //Restart point
            g.setColor(Color.red);
            g.fillOval(reStartX,reStartY,60,60);

            //flagger
            g.setColor(Color.BLACK);
            g.fillRect(flaggerX+30,flaggerY+10,5,45);
            g.fillRect(flaggerX+17,flaggerY+45,30,10);
            g.setColor(Color.RED);
            g.fillRect(flaggerX+8,flaggerY+10,25,20);
            g.setColor(Color.BLACK);
            g.drawRect(flaggerX+8,flaggerY+10,25,20);

            if (flagger == true){
                g.setColor(Color.RED);
            }
            g.drawOval(flaggerX,flaggerY,60,60);
            g.drawOval(flaggerX+1,flaggerY+1,58,58);

            //time counter painting
            g.setColor(Color.BLACK);
            g.fillRect(timeX,timeY,120,60);
            if (lose == false && win ==false) {
                sec = (int) ((new Date().getTime() - startDate.getTime()) / 1000);
            }
            if(sec > 999){
                sec = 999;
            }
            g.setColor(Color.WHITE);
            if (win==true){
                g.setColor(Color.green);
            }else if (lose == true){
                g.setColor(Color.red);
            }
            g.setFont(new Font("Tahoma", Font.PLAIN, 70));
            if (sec <10){
                g.drawString("00"+Integer.toString(sec), timeX, timeY+55);
            }
            else if (sec < 100){
                g.drawString("0"+Integer.toString(sec), timeX, timeY+55);
            }
            else {
                g.drawString(Integer.toString(sec), timeX, timeY+55);
            }
           if (win == true){
               g.setColor(Color.green);
               wMes = "YOU WIN";
           }else if (lose == true){
               g.setColor(Color.red);
               wMes = "YOU LOSE";
           }
           if (win ==true || lose==true){
               wMesY = -50 +(int)(new Date().getTime()-endDate.getTime())/10;
               if (wMesY > 60){
                   wMesY = 60;
               }
               g.setFont(new Font("Tahoma",Font.PLAIN,60));
               g.drawString(wMes, wMesX, wMesY);
           }
        }
    }

    public class Move implements MouseMotionListener{

        @Override
        public void mouseDragged(MouseEvent arg0){

        }
        @Override
        public void mouseMoved(MouseEvent e){
            mx = e.getX();
            my = e.getY();
            /*
            System.out.println("The mouse was moved!");
            System.out.println("X:" + mx + "Y:" + my);
            */
        }
    }

    public class Click implements MouseListener{

        @Override
        public void mouseClicked(MouseEvent e){

            mx = e.getX();
            my = e.getY();

            if (inBoxX() != -1 && inBoxY() != -1){
                System.out.println("The mouse is in the "+inBoxX()+ ","+inBoxY()+ "Number of neighs "+neighbours[inBoxX()][inBoxY()]);
                if (flagger == true && revealed[inBoxX()][inBoxY()] == false){
                    if( flagged[inBoxX()][inBoxY()] == false){
                        flagged[inBoxX()][inBoxY()]=true;
                    }else{
                        flagged[inBoxX()][inBoxY()]=false;
                    }
                }else{
                    if(flagged[inBoxX()][inBoxY()] == false){
                        revealed[inBoxX()][inBoxY()] = true;
                    }
                }
            } else {
                System.out.println("The pointer is not inside of any box");
            }
            if (inGame()==true){
                resetAll();
                System.out.println("In game = true");
            }

            if (inFlagger() == true){
                if (flagger == false){
                    flagger = true;
                    System.out.println("In flag = true");
                }else{
                    flagger = false;
                    System.out.println("In flag = false");
                }
            }
        }
        @Override
        public void mouseEntered(MouseEvent arg0){

        }
        @Override
        public void mouseExited(MouseEvent arg0){

        }
        @Override
        public void mousePressed(MouseEvent arg0){

        }
        @Override
        public void mouseReleased(MouseEvent arg0){

        }

    }

    public  int inBoxX(){
        for(int i = 0; i< 10; i++){
            for(int j = 0; j < 10; j++){
                if(mx >= spacing+i*72 && mx < spacing+i*72+72-2*spacing && my >= spacing+j*72+72+26 && my < spacing+j*72+26+72+72-2*spacing){
                    return i;
                }
           }
        }
        return -1;
    }

    public  int inBoxY(){
        for(int i = 0; i< 10; i++){
            for(int j = 0; j < 10; j++){
                if(mx >= spacing+i*72 && mx < spacing+i*72+72-2*spacing && my >= spacing+j*72+72+26 && my < spacing+j*72+26+72+72-2*spacing){
                    return j;
                }
            }
        }
        return -1;
    }

    public boolean isN(int mX, int mY, int cX, int cY){
        if( mX - cX < 2 && mX - cX > -2 && mY - cY < 2 && mY - cY > -2 && mines[cX][cY]==1){
            return  true;
        }
        return false;
    }

    public void resetAll(){

        resetter = true;

        flagger = false;

        startDate = new Date();

        wMesY = -50;

        wMes = "Nice job!";

        message = true;
        win = false;
        lose = false;
        for(int i = 0; i< 10; i++){
            for(int j = 0; j < 10; j++){
                if(rand.nextInt(100)<15){
                    mines[i][j]=1;
                }else{
                    mines[i][j]=0;
                }
                revealed[i][j] = false;
                flagged[i][j]=false;
            }
        }

        for(int i = 0; i< 10; i++){
            for(int j = 0; j < 10; j++){
                neighs = 0;
                for(int m = 0; m< 10; m++){
                    for(int n = 0; n < 10; n++){
                        if(!(m == i && n ==j)) {
                            if (isN(i, j, m, n) == true)
                                neighs++;
                        }
                    }
                }
                neighbours[i][j] = neighs;
            }
        }
        resetter = false;
    }

    public boolean inGame(){
        int dif = (int)Math.sqrt(Math.abs(mx-MessageCenterX)*Math.abs(mx-MessageCenterX)+Math.abs(my-MessageCenterY)*Math.abs(my-MessageCenterY));
        if(dif<60){
            return true;
        }
        return false;
    }

    public boolean inFlagger(){
        int dif = (int)Math.sqrt(Math.abs(mx-flaggerCenterX)*Math.abs(mx-flaggerCenterX)+Math.abs(my-flaggerCenterY)*Math.abs(my-flaggerCenterY));
        if(dif<60){
            return true;
        }
        return false;
    }

    public void checkStatus(){
        if(lose==false){
            for(int i = 0; i< 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (revealed[i][j] == true && mines[i][j] == 1) {
                        lose = true;
                        message = false;
                        endDate = new Date();
                    }
                }
            }
        }
        if(totalBoxesRevealed() >= 100 - totalMines() && win ==false){
            win = true;
            endDate = new Date();
        }
    }

    public  int totalMines(){
        int total = 0;
        for(int i = 0; i< 10; i++){
            for(int j = 0; j < 10; j++){
                if(mines[i][j] == 1){
                    total++;
                }
            }
        }
        return total;
    }

    public int totalBoxesRevealed(){
        int total = 0;
        for(int i = 0; i< 10; i++){
            for(int j = 0; j < 10; j++){
                if(revealed[i][j] == true){
                    total++;
                }
            }
        }
        return total;
    }
}
