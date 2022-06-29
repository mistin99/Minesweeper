import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Date;
import java.util.Random;

public class GUI extends JFrame {

        public boolean reseter = false;

        Date startDate = new Date();
        Date endDate = new Date();
        public int sec = 0;
        int spacing = 5;
        int neighs;

        public boolean victory = false;
        public boolean defeat = false;

        int smileyX = 605;
        int smileyY = 5;

        int smileyCenterX = smileyX +35;
        int smileyCenterY = smileyY +35;

        public boolean flagger = false;
        public int flaggerX = 445;
        public int flaggerY = 5;

        public int flaggerCenterX = flaggerX + 35;
        public int flaggerCenterY = flaggerY+35;

        public  int vicMesX = 800;
        public int vicMesY = -50;
        boolean happy = true;

       public int mouseX = -100;
       public int mouseY = -100;

       Random rand = new Random();

       String vicMes = "nothing";

       int[][] mines = new int[16][9];
       int[][] neighbours = new int[16][9];
       boolean[][] revealed = new boolean[16][9];
       boolean[][] flagged = new boolean[16][9];
    public GUI() {
        this.setTitle("Minesweeper");
        this.setSize(1286,829);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // making window visible
        this.setVisible(true);
        this.setResizable(false);

        createMine();
        createNeighbours();


        Board board = new Board();
        this.setContentPane(board);

        Move move = new Move();
        this.addMouseMotionListener(move);

        Click click = new Click();
        this.addMouseListener(click);
    }

    public class Board extends JPanel {
        public void paintComponent(Graphics g) {
             g.setColor(Color.darkGray);
             g.fillRect(0,0,1280,800);

             for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 9;j++) {
                    g.setColor(Color.gray);
                    // show mines
                    //if (mines[i][j] == 1) {
                       // g.setColor(Color.yellow);
                   // }
                    revealTiles(g, i, j);
                    mouseHovering(g, i, j);

                    spacingBetweenTiles(g, i, j);

                    gameLogic(g, i, j);


                    markFlag(g, i, j);

                }
             }
             // smiley emoticon
            drawSmiley(g);

            //time counter drawing
            drawTimer(g);

            // victory message painting

            vicMessage(g);


            //Paint flag
            drawFlag(g);


        }

        private void drawFlag(Graphics g) {
            g.setColor(Color.black);
            g.fillRect(flaggerX + 31,flaggerY +15,7,40);
            g.fillRect(flaggerX + 20,flaggerY + 50,30,10);
            g.setColor(Color.red);
            g.fillRect(flaggerX + 16,flaggerY +15,20,15);
            g.setColor(Color.black);
            g.drawRect(flaggerX + 16,flaggerY +15,20,15);
            if (flagger) {
                g.setColor(Color.red);
            }

            g.drawOval(flaggerX,flaggerY,70,70);
            g.drawOval(flaggerX+1,flaggerY+1,68,68);
            g.drawOval(flaggerX+2,flaggerY+2,66,66);
        }

        private void drawTimer(Graphics g) {
            g.setColor(Color.black);
            g.fillRect(1120,5,150,70);
            if (!defeat && !victory) {
                sec = (int) ((new Date().getTime() - startDate.getTime()) / 1000);
            }
            if (sec > 999) {
                sec = 999;
            }
            g.setColor(Color.white);
            if (victory) {
                g.setColor(Color.green);
            }
            else if (defeat) {
                g.setColor(Color.red);
            }
            g.setFont(new Font("Tahoma",Font.PLAIN,80));
            if (sec < 10) {
                g.drawString("00" +sec,1120,75);
            }
            else if(sec <99) {
                g.drawString("0" +sec,1120,75);
            }
            else {
                g.drawString(Integer.toString(sec),1120,75);
            }
        }

        private void drawSmiley(Graphics g) {
            g.setColor(Color.yellow);
            g.fillOval(smileyX,smileyY,70,70);
            g.setColor(Color.black);
            g.fillOval(smileyX+15,smileyY+20,10,10);
            g.fillOval(smileyX+45,smileyY+20,10,10);
            if (happy) {
                g.fillRect(smileyX+20,smileyY+50,30,5);
                g.fillRect(smileyX+17,smileyY+45,5,5);
                g.fillRect(smileyX+48,smileyY+45,5,5);

            }
            else {
                g.fillRect(smileyX+20,smileyY+50,30,5);
            }
        }

        private void spacingBetweenTiles(Graphics g, int i, int j) {
            g.fillRect(spacing + i *80,spacing + j *80+80,80 - 2*spacing,80 - 2*spacing);
        }

        private void mouseHovering(Graphics g, int i, int j) {
            if (mouseX >= spacing + i *80 && mouseX <spacing + i *80+80-2*spacing && mouseY >= spacing + j *80+80+26 && mouseY < spacing+ j *80+80+80-2*spacing) {
                g.setColor(Color.green);

            }
        }

        private void revealTiles(Graphics g, int i, int j) {
            if (revealed[i][j]) {
                g.setColor(Color.white);
                if (mines[i][j] == 1) {
                    g.setColor(Color.RED);
                }
            }
        }

        private void markFlag(Graphics g, int i, int j) {
            if (flagged[i][j]) {
                g.setColor(Color.black);
                g.fillRect(i *80 + 35, j *80+80 +20,7,40);
                g.fillRect(i *80 + 20, j *80+80 + 50,30,10);
                g.setColor(Color.red);
                g.fillRect(i *80 + 16, j *80+80 +20,20,15);
                g.setColor(Color.black);
                g.drawRect(i *80 + 16, j *80+80 +20,20,15);
                if (flagger) {
                    g.setColor(Color.red);
                }


            }
        }

        private void gameLogic(Graphics g, int i, int j) {
            if (revealed[i][j]) {
                g.setColor(Color.black);
                revealPlusRows(i, j);
                revealMinusRows(i, j);
                revealMinusColumn(i, j);
                revealPlusColumn(i, j);
                revealMinusRowPlusColumn(i, j);
                revealMinusRowMinusColumn(i, j);
                revealPlusRowPlusColumn(i, j);
                revealPlusRowMinusColumn(i, j);
                if (mines[i][j] == 0 && neighbours[i][j] !=0) {
                    g.setFont(new Font("Tahoma", Font.BOLD, 40));
                    g.drawString(Integer.toString(neighbours[i][j]), i * 80 + 27, j * 80 + 80 + 55);
                }
                else  if (mines[i][j] == 1){
                    // draw mine
                    drawMine(g, i, j);
                }


            }
        }

        private void drawMine(Graphics g, int i, int j) {
            g.fillRect(i *80+10+20, j *80+80+20,20,40);
            g.fillRect(i *80+20, j *80+80+10+20,40,20);
            g.fillRect(i *80+5+20, i *80+80+5+20,30,30);
            g.fillRect(i *80+38, j *80+80+15,4,50);
            g.fillRect(i *80+15, j *80+80+38,50,4);
        }
    }

    public void checkVictoryStatus() {

        if (!defeat)
            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 9; j++) {
                    if (revealed[i][j] && mines[i][j] == 1) {
                        defeat = true;
                        happy = false;
                        endDate = new Date();
                        break;
                    }
                }

            }

        if (totalBoxesRevealed() >= 144 - totalMines() && !victory) {
            victory = true;
            endDate = new Date();
        }
        else {
        }
    }
    public void resetGame(){
        flagger = false;
        reseter = true;
        happy = true;
        victory=false;
        defeat=false;
        vicMesY = -50;
        startDate = new Date();
        vicMes = "nothing";

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 9; j++){
                // create mine
                if (rand.nextInt(100) < 20) {
                    mines[i][j] = 1;
                }
                else {
                    mines[i][j] = 0;
                }
                revealed[i][j] = false;
                flagged[i][j] = false;
            }
        }
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 9; j++) {
                neighs = 0;
                for (int m = 0; m < 16; m++) {
                    for (int n = 0; n < 9; n++) {
                        if (!((m == i) && (n ==j)))
                            if (checkIfNeighbor(i, j, m, n)) {
                                neighs++;
                            }
                    }
                    neighbours[i][j] = neighs;
                }
            }
        }
        reseter = false;
    }
    private void createMine() {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 9; j++){
                // create mine
                if (rand.nextInt(100) < 20) {
                    mines[i][j] = 1;
                }
                else {
                    mines[i][j] = 0;
                }
                revealed[i][j] =false;
            }
        }
    }
    public int totalMines(){
        int total = 0;
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 9; j++){
                if (mines[i][j] == 1) {
                    total++;

                }
            }
        }
        return  total;
    }
    private void createNeighbours() {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 9; j++) {
                neighs = 0;
                for (int m = 0; m < 16; m++) {
                    for (int n = 0; n < 9; n++) {
                        if (!((m == i) && (n ==j)))
                            if (checkIfNeighbor(i,j,m,n)) {
                                neighs++;
                            }
                    }
                    neighbours[i][j] = neighs;
                }
            }
        }
    }
    public boolean checkIfNeighbor(int mX,int mY,int cX,int cY) {
        return (mX - cX) < 2 && (mX - cX) > -2 && (mY - cY) < 2 && (mY - cY) > -2 && mines[cX][cY] == 1;
    }


    private void vicMessage(Graphics g) {
        if (victory) {
            g.setColor(Color.green);
            vicMes = "You Win";
        }
        else if (defeat) {
            g.setColor(Color.red);
            vicMes = "You Loose";
        }
        if (victory) {
            vicMesY = (int) (-50 + (new Date().getTime() - endDate.getTime()) /10);
            if (vicMesY > 70) {
                vicMesY = 70;
            }
            g.drawString(vicMes,vicMesX,vicMesY);
        }
        if (defeat) {
            vicMesY = (int) (-50 + (new Date().getTime() - endDate.getTime()) /10);
            if (vicMesY > 70) {
                vicMesY = 70;
            }
            g.drawString(vicMes,vicMesX-50,vicMesY);
        }
    }

    public class Move implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent e) {

        }

        @Override
        public void mouseMoved(MouseEvent e) {
           mouseX =  e.getX();
           mouseY =   e.getY();
        }
    }

    public class Click implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            mouseX =  e.getX();
            mouseY =   e.getY();
            if (!defeat && !victory) {
                if (inBoxX() != -1 && inBoxY() != -1) {
                    if (SwingUtilities.isRightMouseButton(e) && !revealed[inBoxX()][inBoxY()]) {
                        if (!flagged[inBoxX()][inBoxY()]) {

                            flagged[inBoxX()][inBoxY()] = true;
                        } else {
                            flagged[inBoxX()][inBoxY()] = false;
                        }

                    } else {
                        if (!flagged[inBoxX()][inBoxY()]) {
                            revealed[inBoxX()][inBoxY()] = true;
                        }
                    }
                }
            }

            if (inSmiley()) {
                resetGame();
            }
            if (inFlag()) {
                if (!flagger) {
                    flagger = true;
                }
            }
            else {
                flagger = false;
            }


        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }


    }

    public  int totalBoxesRevealed() {
        int total =0;
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 9; j++){
                if (revealed[i][j]) {
                    total++;

                }
            }
        }
        return total;
    }

    public boolean inSmiley() {
        int dif = (int) Math.sqrt(Math.abs(mouseX-smileyCenterX)*Math.abs(mouseX-smileyCenterX) +
                Math.abs(mouseY-smileyCenterY)*Math.abs(mouseY-smileyCenterY));
        if (dif < 35) {
            return true;
        }
        return false;
    }

    public boolean inFlag() {
        int dif = (int) Math.sqrt(Math.abs(mouseX-flaggerCenterX)*Math.abs(mouseX-flaggerCenterX) +
                Math.abs(mouseY-flaggerCenterY)*Math.abs(mouseY-smileyCenterY));
        if (dif < 35) {
            return true;
        }
        return false;
    }

    public int inBoxX() {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 9;j++) {

                if (mouseX >= spacing + i*80 && mouseX <i*80+80-spacing && mouseY >= spacing +j*80+106 && mouseY < spacing+j*80+186-spacing) {
                    return i;
                }

            }
        }
        return -1;
    }

    public int inBoxY() {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 9;j++) {

                if (mouseX >= spacing + i*80 && mouseX <i*80+80-spacing && mouseY >= spacing +j*80+106 && mouseY < spacing+j*80+186-spacing) {
                    return j;
                }

            }
        }
        return -1;
    }

    public void revealPlusRows(int x, int y) {


        revealed[x][y] = true;
        if(neighbours[x][y] == 0) {
            if (x < 15) {
                revealPlusRows(x + 1, y);
            }
        }
    }
    public void revealMinusRows(int x, int y) {


        revealed[x][y] = true;
        if(neighbours[x][y] == 0) {
            if (x > 0) {
                revealMinusRows(x - 1, y);
            }
        }
    }
    public void revealPlusColumn(int x, int y) {


        revealed[x][y] = true;
        if(neighbours[x][y] == 0) {
            if (y < 8) {
                revealPlusColumn(x, y + 1);
            }
            }
    }
    public void revealMinusColumn(int x, int y) {


        revealed[x][y] = true;
        if(neighbours[x][y] == 0) {
            if (y > 0 ) {
                revealMinusColumn(x, y - 1);
            }
        }
    }

    public void revealMinusRowMinusColumn(int x, int y) {


        revealed[x][y] = true;
        if(neighbours[x][y] == 0) {
            if (y > 0 && x > 0 ) {
                revealMinusRowMinusColumn(x-1, y - 1);
            }
        }
    }
    public void revealPlusRowPlusColumn(int x, int y) {


        revealed[x][y] = true;
        if(neighbours[x][y] == 0) {
            if (y < 8 && x < 15 ) {
                revealPlusRowPlusColumn(x + 1, y + 1);
            }
        }
    }
    public void revealPlusRowMinusColumn(int x, int y) {


        revealed[x][y] = true;
        if(neighbours[x][y] == 0) {
            if (y > 0 && x < 15 ) {
                revealPlusRowPlusColumn(x + 1, y - 1);
            }
        }
    }
    public void revealMinusRowPlusColumn(int x, int y) {


        revealed[x][y] = true;
        if(neighbours[x][y] == 0) {
            if (y < 8 && x > 0 ) {
                revealPlusRowPlusColumn(x - 1, y + 1);
            }
        }
    }


}
