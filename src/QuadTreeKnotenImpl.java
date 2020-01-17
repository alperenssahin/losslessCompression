import java.util.NoSuchElementException;
import java.util.Optional;

public class QuadTreeKnotenImpl implements QuadTreeKnoten {
    private int dim;
    private int color;
    public QuadTreeKnotenImpl topLeft;
    public QuadTreeKnotenImpl topRight;
    public QuadTreeKnotenImpl bottomLeft;
    public QuadTreeKnotenImpl bottomRight;
    private int size = 1;


    public QuadTreeKnotenImpl(int dim, int color) {
        this.dim = dim;
        this.color = color;
    }

    public static QuadTreeKnoten buildFromIntArray(int[][] image) {
//        for(int i = 0; i<image.length;i++){
//            for(int y = 0; y<image[i].length; y++){
//                System.out.println(image[i][y]);
//            }
//        }
        QuadTreeKnotenImpl root = new QuadTreeKnotenImpl(image.length, image[0][0]);
        return recursiveParser(root, image);
    }

    public static QuadTreeKnotenImpl recursiveParser(QuadTreeKnotenImpl root, int[][] image) {
        int[][][] arr = arrayParser(image);
        for (int i = 0; i < 4; i++) {
            QuadTreeKnotenImpl tmp;
            if (colorIsSame(arr[i]) || arr[i].length == 1) {
                tmp = new QuadTreeKnotenImpl(arr[i].length, arr[i][0][0]);
                tmp.size = 1;

            } else {
                tmp = new QuadTreeKnotenImpl(arr[i].length, arr[i][0][0]);
                tmp = recursiveParser(tmp, arr[i]);
                tmp.size = 5;
            }
            if (i == 0) {
                root.setTopLeft(tmp);
            }
            if (i == 1) {
                root.setTopRight(tmp);
            }
            if (i == 2) {
                root.setBottomLeft(tmp);
            }
            if (i == 3) {
                root.setBottomRight(tmp);
            }
        }
        return root;
    }

    public static int[][][] arrayParser(int[][] image) {
        int[][][] arr = new int[4][image.length / 2][image.length / 2];
        for (int i = 0; i < image.length / 2; i++) {
            for (int y = 0; y < image[i].length / 2; y++) {
                arr[0][i][y] = image[i][y];
            }
        }
        for (int i = image.length / 2; i < image.length; i++) {
            for (int y = 0; y < image[i].length / 2; y++) {
                arr[1][i - image.length / 2][y] = image[i][y];
            }
        }
        for (int i = 0; i < image.length / 2; i++) {
            for (int y = image[i].length / 2; y < image[i].length; y++) {
                arr[2][i][y - image[i].length / 2] = image[i][y];
            }
        }
        for (int i = image.length / 2; i < image.length; i++) {
            for (int y = image[i].length / 2; y < image[i].length; y++) {
                arr[3][i - image.length / 2][y - image[i].length / 2] = image[i][y];
            }
        }
        return arr;
    }

    public static boolean colorIsSame(int[][] image) {
        boolean state = true;
        int tmp = image[0][0];
        for (int i = 0; i < image.length; i++) {
            for (int y = 0; y < image[i].length; y++) {
                if (tmp != image[i][y]) {
                    state = false;
                }
            }
        }
        return state;
    }

    public void setTopLeft(QuadTreeKnotenImpl topLeft) {
        this.topLeft = topLeft;
    }

    public void setTopRight(QuadTreeKnotenImpl topRight) {
        this.topRight = topRight;
    }

    public void setBottomLeft(QuadTreeKnotenImpl bottomLeft) {
        this.bottomLeft = bottomLeft;
    }

    public void setBottomRight(QuadTreeKnotenImpl bottomRight) {
        this.bottomRight = bottomRight;
    }

    @Override
    public QuadTreeKnoten getTopLeft() {
        if(this.isLeaf()){
            throw new NoSuchElementException("node is leaf");
        }else{
            return this.topLeft;
        }
    }

    @Override
    public QuadTreeKnoten getTopRight() {
        if(this.isLeaf()){
            throw new NoSuchElementException("node is leaf");
        }else{
            return this.topRight;
        }
    }

    @Override
    public QuadTreeKnoten getBottomLeft() {
        if(this.isLeaf()){
            throw new NoSuchElementException("node is leaf");
        }else{
            return this.bottomLeft;
        }
    }

    @Override
    public QuadTreeKnoten getBottomRight() {
        if(this.isLeaf()){
            throw new NoSuchElementException("node is leaf");
        }else{
            return this.bottomRight;
        }
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public int getRelativeColor(int x, int y) {
        if (x > this.getDimension() - 1 || y > this.getDimension() - 1) {
            throw new IllegalArgumentException("x or y greater then node dimension");
        }
        int color = 0;
        try{
            if (x < this.getDimension() / 2 && y < this.getDimension() / 2) {
                if (this.isLeaf()) {
                    color = this.getColor();
                } else {
                    color = getTopLeft().getRelativeColor(x, y);
                }
            }
            if (x > this.getDimension() / 2 && y < this.getDimension() / 2) {
                if (this.isLeaf()) {
                    color = this.getColor();
                } else {
                    color = getTopRight().getRelativeColor(x - this.getDimension() / 2, y);
                }
            }
            if (x < this.getDimension() / 2 && y > this.getDimension() / 2) {
                if (this.isLeaf()) {
                    color = this.getColor();
                } else {
                    color = getBottomLeft().getRelativeColor(x, y - this.getDimension() / 2);
                }
            }
            if (x > this.getDimension() / 2 && y > this.getDimension() / 2) {
                if (this.isLeaf()) {
                    color = this.getColor();
                } else {
                    color = getBottomRight().getRelativeColor(x - this.getDimension() / 2, y - this.getDimension() / 2);
                }
            }
        }catch (Exception ex){
            color = this.getColor();
        }
        return color;
    }

    @Override
    public void setRelativeColor(int x, int y, int color) {
        if (x > getDimension() - 1 || y > getDimension() - 1) {
            throw new IllegalArgumentException("x or y greater then node dimension");
        }
        if (x < getDimension() / 2 && y < getDimension() / 2) {
            if (this.isLeaf()) {
                 this.setColor(color);
            } else {
                getTopLeft().setRelativeColor(x, y,color);
            }
        }
        if (x > getDimension() / 2 && y < getDimension() / 2) {
            if (this.isLeaf()) {
                this.setColor(color);
            } else {
                 getTopRight().setRelativeColor(x - getDimension() / 2, y,color);
            }
        }
        if (x < getDimension() / 2 && y > getDimension() / 2) {
            if (this.isLeaf()) {
               this.setColor(color);
            } else {
                getBottomLeft().setRelativeColor(x, y - getDimension() / 2,color);
            }
        }
        if (x > getDimension() / 2 && y > getDimension() / 2) {
            if (this.isLeaf()) {
               this.setColor(color);
            } else {
               getBottomRight().setRelativeColor(x - getDimension() / 2, y - getDimension() / 2,color);
            }
        }
    }

    @Override
    public int getDimension() {
        return this.dim;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public boolean isLeaf() {
        return this.size == 1;
    }

    @Override
    public int[][] toArray() {
        int[][] arr = new int[getDimension()][getDimension()];
        for (int i = 0; i < getDimension(); i++) {
            for (int y = 0; y < getDimension(); y++) {
                arr[i][y] = getRelativeColor(i, y);
            }
        }
        return arr;
    }

    // TODO Implement other methods
}
