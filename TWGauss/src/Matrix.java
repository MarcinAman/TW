import java.util.Arrays;

public class Matrix {
    private double[][] matrix;
    private int size;
    private Integer columns;
    private double[] result;

    public Matrix setRow(double[] row, int rowNo){
        this.matrix[rowNo] = row;
        return this;
    }

    public double[] getRow(int rowNo){
        return this.matrix[rowNo];
    }

    public double[][] getMatrix() {
        return matrix;
    }

    public Matrix setMatrix(double[][] matrix) {
        this.matrix = matrix;
        return this;
    }

    public int getSize() {
        return size;
    }

    public Matrix setSize(int size) {
        this.size = size;
        return this;
    }

    public double[] getResult() {
        return result;
    }

    public Matrix setResult(double[] result) {
        this.result = result;
        return this;
    }

    public Matrix addResultToMatrix(){
        double[][] m = new double[size][size+1];

        for(int i=0;i<size;i++){
            for(int j = 0;j<size; j++){
                m[i][j] = this.matrix[i][j];
            }
        }

        for(int i=0;i<size;i++){
            m[i][size] = result[i];
        }

        this.columns = size+1;

        this.matrix = m;

        return this;
    }

    public void setValue(double value, int i, int j){
        this.matrix[i][j] = value;
    }

    public double[] resultsColumn(){
        double[] column = new double[this.size];

        for(int i=0;i<this.size;i++){
            column[i] = this.matrix[i][size];
        }

        return column;
    }

    private String printArray(double[][] array, int size){
        final StringBuilder sb = new StringBuilder("Content{\n");
        for(int i=0;i<size;i++){
            for(int j=0;j<size+1;j++){
                sb.append(array[i][j]);
                sb.append(",");
            }
            sb.append("\n");
        }

        sb.append("}");

        return sb.toString();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Matrix{");
        sb.append("matrix=").append(this.printArray(this.matrix, this.size));
        sb.append(", size=").append(size);
        sb.append(", result=").append(Arrays.toString(result));
        sb.append('}');
        return sb.toString();
    }
}
