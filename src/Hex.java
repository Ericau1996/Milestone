import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.*;

public class Hex extends JFrame{

        public int boxSize = 60;
        public int box = 10;

        public int timeX = 590;
        public int timeY = 600;

        String wMes = "Nice job!";
        public int wMesX = 200;
        public int wMesY = 600;
        public int sec = 0;

        Date startDate = new Date();
        Date endDate;

        public boolean inGame = true;

        public int leftClicked = 1;
        public int mines = 10;
        Random rand = new Random();
        public int totalBoxesRevealed;

        int[] neighboursColor = {0xFF0000, 0x00FF00, 0x0000FF, 0xFFFF00, 0xFF00FF, 0x00FFFF, 0x0F0F0F, 0xF0F0F0};
        Cell[][] field = new Cell[box][box];

        public boolean won, mine;


        public Hex() {
            this.setTitle("Hex Minesweeper");
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setSize(726,770);
            this.setVisible(true);
            this.setResizable(false);

            Board board = new Board();
            board.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    int x1 = e.getX() / (boxSize + 2);
                    int y1 = e.getY() / (boxSize / 4 * 3 + 2);
                    int x = -1, y = -1;
                    if(y1 % 2 == 0) {
                        if(x1 < box && y1 < box && field[y1][x1].hexagon.contains(e.getX(), e.getY())){x = x1; y = y1;}
                        if(y1 - 1 > -1 && y1 < box && field[y1 - 1][x1].hexagon.contains(e.getX(), e.getY())){x = x1; y = y1 - 1;}
                        if(x1 - 1 > -1 && y1 - 1 > -1 && field[y1 - 1][x1 - 1].hexagon.contains(e.getX(), e.getY())){x = x1 - 1; y = y1 - 1;}
                    }
                    else {
                        if(x1 < box && y1 < box && field[y1][x1].hexagon.contains(e.getX(), e.getY())){x = x1; y = y1;}
                        if(y1 - 1 > -1 && x1 + 1 < box && field[y1 - 1][x1 + 1].hexagon.contains(e.getX(), e.getY())){x = x1 + 1; y = y1 - 1;}
                        if(y1 - 1 > -1 && x1 < box && field[y1 - 1][x1].hexagon.contains(e.getX(), e.getY())){x = x1; y = y1 - 1;}
                    }
                    if (!mine && !won && x > -1 && y > -1) {
                        if (e.getButton() == leftClicked)
                            if (field[y][x].isHidden()) {
                                cellsRevealed(x, y);
                                if(mine){
                                    inGame=false;
                                }
                            }
                            board.repaint();
                    }
                }
            });
            add(BorderLayout.CENTER, board);
            setVisible(true);
            gameStatus();
        }

        private void cellsRevealed(int x, int y) {
            if (x < 0 || x > box - 1 || y < 0 || y > box - 1) return;
            if (!field[y][x].isHidden()) return;
            field[y][x].revealed();
            if (field[y][x].getNeighs() > 0 || mine) return;
        }

        private void gameStatus() {
            int x, y, countMines = 0;
            for (x = 0; x < box; x++)
                for (y = 0; y < box; y++)
                    field[y][x] = new Cell(x, y);
            while (countMines < mines) {
                do {
                    x = rand.nextInt(box);
                    y = rand.nextInt(box);
                } while (field[y][x].isMined());

                field[y][x].mine();
                countMines++;
            }
            for (x = 0; x < box; x++)
                for (y = 0; y < box; y++)
                    if (!field[y][x].isMined()) {
                        int count = 0;
                        for (int dx = -1; dx < 1; dx++)
                            for (int dy = -1; dy < 2; dy++) {
                                int nX = x + dx + y % 2;
                                int nY = y + dy;
                                if (nX < 0 || nY < 0 || nX > box - 1 || nY > box - 1) {
                                    nX = x;
                                    nY = y;
                                }
                                count += (field[nY][nX].isMined()) ? 1 : 0;
                            }
                        if(y % 2 == 0 && x + 1 < box) {count += (field[y][x + 1].isMined()) ? 1 : 0;}
                        if(y % 2 > 0 && x - 1 > -1){count += (field[y][x - 1].isMined()) ? 1 : 0;}
                        field[y][x].totalMines(count);
                    }

        }

        public class Cell {
            public int neighbours;
            public boolean safe, lose;
            public Polygon hexagon;
            public Polygon hexagonOut;

            public Cell(int x, int y) {
                int px[] = new int[6];
                int py[] = new int[6];
                int dx;
                if (y % 2 == 0) {
                    dx = 0;
                }
                else {
                    dx = 60 / 2 + 1;
                }
                px[0] = 10*x + x * 60 + dx;
                py[0] = 10*y + y * 60 / 4 * 3 + 60 / 4;
                px[1] = 10*x + x * 60 + 60 / 2 + dx;
                py[1] = 10*y + y * 60 / 4 * 3;
                px[2] = 10*x + x * 60 + 60 + dx;
                py[2] = 10*y + y * 60 / 4 * 3 + 60 / 4;
                px[3] = 10*x + x * 60 + 60 + dx;
                py[3] = 10*y + y * 60 / 4 * 3 + 60 / 4 * 3;
                px[4] = 10*x + x * 60 + 60 / 2 + dx;
                py[4] = 10*y + y * 60 / 4 * 3 + 60;
                px[5] = 10*x + x * 60 + dx;
                py[5] = 10*y + y * 45 + 45;
                hexagon = new Polygon(px, py, 6);

                //左上
                px[0] = 10*x + x * 60 + dx-1;
                py[0] = 10*y + y * 60 / 4 * 3 + 60 / 4-1;
                //頂點
                px[1] = 10*x + x * 60 + 60 / 2 + dx;
                py[1] = 10*y + y * 60 / 4 * 3-1;
                //右上
                px[2] = 10*x + x * 60 + 60 + dx+1;
                py[2] = 10*y + y * 60 / 4 * 3 + 60 / 4-1;
                //右下
                px[3] = 10*x + x * 60 + 60 + dx+1;
                py[3] = 10*y + y * 60 / 4 * 3 + 60 / 4 * 3+1;
                //下點
                px[4] = 10*x + x * 60 + 60 / 2 + dx;
                py[4] = 10*y + y * 60 / 4 * 3 + 60+1;
                //左下角
                px[5] = 10*x + x * 60 + dx -1;
                py[5] = 10*y + y * 45 + 45+1;
                hexagonOut = new Polygon(px, py, 6);
            }

            public void revealed() {
                safe = true;
                mine = lose;
                if (!lose) totalBoxesRevealed++;
            }

            public void mine() { lose = true; }

            public void totalMines(int count) { neighbours = count; }

            public int getNeighs() { return neighbours; }

            public boolean isHidden() { return !safe; }

            public boolean isMined() { return lose; }

            public void paintMine(Graphics g, int x, int y, Color color) {
                g.setColor(Color.BLACK);
                int dx;
                if (y % 2 == 0) {
                    dx = 0;
                }
                else {
                    dx = boxSize / 2 + 1;
                }
                int xcenter = 10*x + x * boxSize + boxSize / 2 + dx;
                int ycenter = 10*y + y * boxSize / 4 * 3 + boxSize / 2;
                g.fillRect(xcenter - boxSize / 8, ycenter - boxSize / 4, boxSize / 4, boxSize / 2);
                g.fillRect(xcenter - boxSize / 4, ycenter - boxSize / 8, boxSize / 2, boxSize / 4);
                g.fillRect(xcenter - boxSize / 8 - boxSize / 12, ycenter - boxSize / 8 - boxSize / 12,
                        boxSize / 8 * 7/2, boxSize / 8 * 7/2);
            }

            public void paintNumber(Graphics g, String str, int x, int y, Color color) {
                g.setColor(color);
                int dx;
                if (y % 2 == 0) {
                    dx = 0;
                }
                else {
                    dx = boxSize / 2 + 1;
                }
                int xcenter = 10*x + x * boxSize + boxSize / 2 + dx;
                int ycenter = 10*y + y * boxSize / 4 * 3 + boxSize / 2;
                g.setFont(new Font("Tahoma", Font.BOLD, boxSize / 2));
                g.drawString(str, xcenter - boxSize / 8, ycenter + boxSize / 4);
            }

            public void paintComponent(Graphics g, int x, int y) {
                g.setColor(Color.YELLOW);
                g.drawPolygon(hexagon);
                g.setColor(Color.GREEN);
                g.drawPolygon(hexagonOut);
                if (!safe) {
                    if ((mine || won) && lose) paintMine(g, x, y, Color.black);
                    else {
                        g.setColor(Color.DARK_GRAY);
                        g.fillPolygon(hexagon);
                        g.setColor(Color.WHITE);
                    }
                }else
                if (lose) paintMine(g, x, y, mine? Color.red : Color.black);
                else
                if (neighbours > 0)
                    paintNumber(g, Integer.toString(neighbours), x, y, new Color(neighboursColor[neighbours - 1]));
            }
        }

        public class Board extends JPanel {}

}