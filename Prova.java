import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.mg4j.util.*;
import cern.colt.Timer;
import java.io.*;

public class Prova {

    static public void main( String arg[] ) throws Exception {
	final int refArray[] = new int[2* Integer.parseInt(arg[0])];
	final int inv[] = new int[refArray.length];
	final int heap[] = new int[refArray.length/2];
	
	java.util.Random r = new java.util.Random();
	int i = refArray.length;
	while(i--!=0) refArray[ i ] = r.nextInt();
	i = heap.length;
	final int length = heap.length;
	while(i--!=0) heap[ i ] = i;
	IntSemiIndirectHeaps.makeHeap( refArray, heap, length, null );

	Timer t = new Timer();

	int rep = 20, tot=0;

	while(rep-- != 0) {
	
	t.reset();
	t.start();
	i = 1000000;
	while(i-- != 0) {
		heap[ 0 ] = r.nextInt( length * 2 );
		IntSemiIndirectHeaps.downHeap( refArray, heap, length, 0, null );
	}

	t.stop();
	if ( rep < 15 ) tot += t.millis();
	System.out.println( t.millis() );
	
    }
	System.out.println( tot/15.0);
	}
}
