import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import static java.lang.Thread.sleep;

class Main implements Flood{

    Window w = new Window();
    //jframe for window
    GridBagConstraints grid = new GridBagConstraints();
    //Gridbag for jframe

    JFrame window = new JFrame("P3");




    public static void main(String[] args) throws ExecutionException, InterruptedException {

        M m = new M();
        m.makeTree();
        Main rc = new Main(m.root.state);
        Scanner kb = new Scanner(System.in);

        for(int k = 0; k < m.displaySequence.size(); k++){
            sleep(1000);
            //kb.nextLine();
            rc.paint(m.displaySequence.get(k));
            //"paints" a given state to the game board
        }

    }

    public Main(char[][] state) {
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }
            //error handeling

            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            w.paintItt(state);
            //paints an entire board
            window.add(w);
            window.pack();
            window.setLocationRelativeTo(null);
            window.setVisible(true);
            //options for the window
            //adding options to jframe
        });
    }

    public synchronized void paint(char[][] c) {
        w.paintItt(c); //calles paint Itt method for each char
    }

    public class Window extends JPanel {

        GridBagConstraints grid;

        public Window() {
            setLayout(new GridBagLayout());
            grid = new GridBagConstraints();
            //makes a grid
            grid.gridy = 0;
            for (int i = 0; i < degree; i++) {
                grid.gridx = 0;
                for (int j = 0; j < degree; j++) {
                    JPanel cell = new JPanel() {
                        @Override
                        public Dimension getPreferredSize() {
                            return new Dimension(30, 30);
                        }

                    };
                    //creates new jpanels for each cell of the grid
                    add(cell, grid);
                    grid.gridx++;
                }
                grid.gridy++;
            }
        }

        public void paintItt(char[][] boi) {
            for (int i = 0; i < degree; i++) {
                for (int j = 0; j < degree; j++) {
                    this.getComponent(i + (j * degree)).setBackground(getColor(boi[i][j]));
                    //finds position in the current grid and updates it to the
                    //current color
                }
            }
        }
    }


    private Color getColor(char c) {
        //finds color object from a char
        switch (c) {
            case 'r':
                return Color.RED;
            case 'b':
                return Color.BLUE;
            case 'g':
                return Color.GREEN;
            case 'y':
                return Color.YELLOW;
            case 'o':
                return Color.ORANGE;
            case 'p':
                return Color.MAGENTA;
        }
        return Color.BLACK;
    }
}