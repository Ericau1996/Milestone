import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.*;

public class Main extends JFrame {
    public Main() {
        JButton btn = new JButton("Start");
        JButton hexbtn = new JButton("Hex");
        btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Thread thread1 = new Thread(new Runnable() {
                    public void run() {
                        Graphics g = getGraphics();
                        GUI gui = new GUI();
                        while (true){
                            gui.repaint();
                            if (gui.resetter == false){
                                gui.checkStatus();
                            }
                        }
                    }
                });

                thread1.start();
            }
        });
            hexbtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Thread thread2 = new Thread(new Runnable() {
                        public void run() {
                            Graphics g = getGraphics();
                            Hex gui = new Hex();
                            while (true){
                                gui.repaint();
                            }
                        }
                    });
                    thread2.start();
                }
            });
        getContentPane().add(btn, BorderLayout.NORTH);
        getContentPane().add(hexbtn, BorderLayout.SOUTH);
        setDefaultCloseOperation(
                WindowConstants.EXIT_ON_CLOSE);
        setSize(320, 200);

        setVisible(true);
    }

    public static void main(String[] args) {
        new Main();
    }
}