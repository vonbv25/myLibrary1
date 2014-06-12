import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorView;
/**
 * Created by von on 6/10/14.
 */
public class sample {


    public static void main(String[] args) {
    double[] x = new double[] {1,2,3,4,5};


       Vector X = new DenseVector(x);

        for (Vector.Element _x: X.all())
            System.out.println(_x);

        System.out.print(X.get(0));

    }


}
