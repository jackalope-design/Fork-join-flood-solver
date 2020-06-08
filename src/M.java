import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

public class M implements Flood{

    Node root = new Node(this);
    //root node for tree
    Node solution = new Node(this);
    //solution node
    ArrayList<char[][]> displaySequence = new ArrayList<>();
    //an arrary of char[][]'s that are placed in order for playback


    void makeTree(){
        //initalizes a new fork/join pool
        ForkJoinPool f = new ForkJoinPool(numThreads);
        //invokes this onto the root node to reqursivelly grow a tree
        solution  = (Node) f.invoke(root);
        f.shutdown();
	if(solution != null) {
            getSequence();
        }else{
            System.out.println("could not find solution");
        }
    }

    public void getSequence(){
        //backtracks through a solution node adding the char[][] elemts
        //that is has gound in the desplay sequence array in reverse order
        Node n = solution;
        while(n.parrent != null){
            displaySequence.add(0, n.state);
            n = n.parrent;
        }
    }
}
