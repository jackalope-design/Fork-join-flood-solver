import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

public class Node extends RecursiveTask implements Flood {

    char[][] state = new char[degree][degree];
    //state grid for current node
    ArrayList<Node> children = new ArrayList<>();
    //array of all children nodes
    char move;
    //char that represents the current transformation of the state
    int floodSize;
    //int that represents the current fitness of the state
    Node parrent = null;
    //parent for backtracking after finding a solution
    int futureFloodSize;
    //temp varible used in children creation logic
    int layer = 0;
    //this current layer in the tree
    char[][] temp = new char[degree][degree];
    //temp varible used in children creation
    M m = null;
    //a refrence to the "M" or manager of the program
    Random r = new Random();
    //random used to init. the game
    boolean computed = false;

    Node(M m) {
        this.m = m;
        //init manager
        initGame();
        //makes a new game
        this.move = state[0][0];
        //init move
        voidTempArrayCopy();
        //copyies the state to the temp arrays
        char c = temp[0][0];
        futureFloodSize = plauge(0, 0, c);
        //finds the champian flood size;


    }

    /**
     * Node(M m, Node parrent, char[][] state, int layer, char move, int futureFloodSize) {
     * this.m = m;
     * this.parrent = parrent;
     * this.state = state;
     * this.layer = layer + 1;
     * this.move = move;
     * this.floodSize = futureFloodSize;
     * //initalizes nessisary class varibles
     * }
     **/

    Node(M m, Node parrent, char[][] state, int layer, char move, int floodSize) {
        this.m = m;
        this.parrent = parrent;
        this.state = state;
        this.layer = layer + 1;
        this.move = move;
        this.floodSize = floodSize;
        //initalizes nessisary class varibles
    }

    public boolean isEnd() {
        //checks to see if this node is a solution
        //by itterating through all the chars
        //and returning flase if any differ from the origen
        char c = state[0][0];
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length; j++) {
                if (c != state[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    void initGame() {
        //init the state array with random colors
        for (int i = 0; i < degree; i++) {
            for (int j = 0; j < degree; j++) {
                state[i][j] = colors[r.nextInt(colors.length)];
            }
        }
    }

    char[][] next(char c) {
        //copys temp array
        tempArrayCopy();
        //finds all occurences of a given color c
        newPlauge();
        for (int i = 0; i < degree; i++) {
            for (int j = 0; j < degree; j++) {
                if (temp[i][j] == 'h') {
                    //replaces all found occurences with a char c
                    temp[i][j] = c;
                }
            }
        }
        return temp;
    }

    //returns a copyed array of state
    char[][] tempArrayCopy() {
        char[][] temp = new char[degree][degree];
        for (int i = 0; i < degree; i++) {
            for (int j = 0; j < degree; j++) {
                temp[i][j] = state[i][j];
            }
        }
        return temp;
    }

    //copyies this nodes state array to this nodes "temp" without retrning anything
    void voidTempArrayCopy() {
        this.temp = new char[degree][degree];
        for (int i = 0; i < degree; i++) {
            for (int j = 0; j < degree; j++) {
                temp[i][j] = state[i][j];
            }
        }
    }


    void newPlauge() {
        //This method works in conjunction with plauge
        voidTempArrayCopy();
        //copys temp array to ensure that state isent being copyed over.
        char c = temp[0][0];
        //makes a char c equal to the origen in the grid
        futureFloodSize = plauge(0, 0, c);
        //plauge
    }

    int plauge(int x, int y, char c) {
        //recursive algorithm used to find all the connecting
        //colors in a grid from an origen 0, 0
        temp[x][y] = 'h';
        int right = 0, left = 0, up = 0, down = 0;
        //varables used to count, usefull for finding the flood size

        if (!(x <= 0)) {
            if (c == temp[x - 1][y]) {
                left = plauge(x - 1, y, c);
            }
        }
        //all these if statements check the bound for a move
        if (!(x >= temp.length - 1)) {
            //these if statements reqursivelly call the method
            //incrementing that side varible by one
            if (c == temp[x + 1][y]) {
                right = plauge(x + 1, y, c);
            }
        }
        if (!(y <= 0)) {
            if (c == temp[x][y - 1]) {
                down = plauge(x, y - 1, c);
            }
        }
        if (!(y >= temp.length - 1)) {
            if (c == temp[x][y + 1]) {
                up = plauge(x, y + 1, c);
            }
        }
        return 1 + left + right + up + down;
    }

    protected void findChildren() {
        //compute is imported mehod for fork/join tasks
            //checks current state if it is an end state, or if there are any moves left
            for (int i = 0; i < colors.length; i++) {
                //itterates through all options of next nodes
                char[][] nState = next(colors[i]);
                //initilizes next node from a color transformaton
                if (colors[i] != this.move) {
                    //checks if the next move is the current move
                    if (futureFloodSize > floodSize) {
                        //checks to see if it will make any progrss
                        Node n = new Node(m, this, nState, layer, colors[i], futureFloodSize);
                        //new node is made
                        this.children.add(n);
                        //node is added to this nodes children
                    }
                }
            }
    }

    @Override
    protected Node compute() {
        //checks to see if there are any moves left
        if (size - this.layer > 0) {
            this.findChildren();
            //calculates children of the root node

            Node temp = null;
            //used to find best solution of the children

            for (int i = 0; i < children.size(); i++) {
                if (!children.get(i).isEnd()) {
                    //checks chldren to see if they are an ending state
                    children.get(i).fork();
                    //makes all this nodes children into subtasks because this task was
                    //not a solution
                } else {
                    if(temp == null){
                        //sees if a solution has been found already
                        temp = children.get(i);
                    } else if (temp.layer > children.get(i).layer){
                        //checks to see if the current solution is better than the previous solution
                        temp = children.get(i);
                    }
                }
            }

            if(temp != null){
                //if a solution is found, it is proudly returned
                return temp;
            }

            temp = null;
            //reusing temp for second part for the same purpose

            for(int i = children.size() - 1; i > 0; i--){
                Node f = (Node) children.get(i).join();
                //this sees if a solution was found on a lower level
                if(f != null){
                    //a solution was found on a lower level
                    if(temp != null){
                        if(temp.layer > f.layer){
                            //finds better solution
                            temp = f;
                        }
                    } else {
                        //a solution for this node has not been found yet
                        temp = f;
                    }
                }
            }
            //returns a joined solution
            return temp;
        }
        //nothing has been found
        return null;
    }
}
