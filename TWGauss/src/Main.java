import java.util.Arrays;

public class Main {

    private static void swapIfCurrentCellIsZero(Matrix matrix, int row){
        if(matrix.getRow(row)[row] == 0){
            for(int i=row;i<matrix.getSize();i++){
                if(matrix.getRow(i)[row] != 0){
                    // swap rows
                    swapRows(matrix, row, i);
                }
            }
        }
    }

    private static void swapIfCurrentCellIsZeroReversed(Matrix matrix, int row) {
        if (matrix.getRow(row)[row] == 0) {
            for(int i=row;i>=0;i--){
                if(matrix.getRow(i)[row] != 0){
                    // swap rows
                    swapRows(matrix, row, i);
                }
            }
        }
    }

    private static void swapRows(Matrix matrix, int a, int b){
        double[] nonZeroRow = matrix.getRow(a);
        double[] currentRow = matrix.getRow(b);

        matrix.setRow(nonZeroRow, b);
        matrix.setRow(currentRow, a);
    }

    private static double[] calculateRatioForRows(Matrix matrix, int row) throws InterruptedException {
        Thread[] threads = new Thread[matrix.getSize()-row-1];
        double[] ratios = new double[matrix.getSize()];

        int index = 0;
        for(int i=row+1;i<matrix.getSize();i++){
            threadRoute(ratios, threads, matrix, row, i, index);
            index++;
        }

        for(int i=0;i<index;i++) {
            threads[i].join();
        }

        return ratios;
    }

    private static double[] calculateRatioForRowsReversed(Matrix matrix, int row) throws InterruptedException {
        Thread[] threads = new Thread[row+1];
        double[] ratios = new double[matrix.getSize()];

        for(int i=row-1;i>=0;i--){
            threadRoute(ratios, threads, matrix, row, i,i);
        }

        for(int i=row-1;i>=0;i--) {
            threads[i].join();
        }

        return ratios;
    }

    private static void threadRoute(double[] ratios, Thread[] threads, Matrix matrix, int row, int i, int threadIndex){
        final int f_i = i;

        threads[threadIndex] = new Thread(() -> {
            ratios[f_i] = calculateRatio(matrix.getRow(row), matrix.getRow(f_i), row);
        });

        threads[threadIndex].start();
    }

    private static double calculateRatio(double[] from, double[] to, int column){
        return -1*to[column]/from[column];
    }

    private static Matrix substract(Matrix matrix, int row, double[] ratios) throws InterruptedException {
        Thread[][] threads = new Thread[matrix.getSize()-row-1][matrix.getSize()+1];

        for(int i=row+1;i<matrix.getSize();i++){
            for(int j=0;j<matrix.getSize()+1;j++){
                final int final_i = i;
                final int final_j = j;

                threads[i-row-1][j] = new Thread(() -> {
                    double value = matrix.getMatrix()[final_i][final_j]+ratios[final_i]*matrix.getRow(row)[final_j];
                    matrix.setValue(value, final_i, final_j);
                });

                threads[i-row-1][j].start();
            }
        }

        for(int i=row+1;i<matrix.getSize();i++){
            for(int j=0;j<matrix.getSize()+1;j++){
                threads[i-row-1][j].join();
            }
        }

        return matrix;
    }

    private static Matrix reduceDiagonal(Matrix matrix) throws InterruptedException {
        Thread[] threads = new Thread[matrix.getSize()];

        for(int i=0;i<matrix.getSize();i++){
            final int i_f = i;

            threads[i] = new Thread(() -> {
                double value = matrix.getMatrix()[i_f][i_f];
                matrix.setValue(1,i_f,i_f);

                matrix.setValue(matrix.getMatrix()[i_f][matrix.getSize()]/value, i_f, matrix.getSize());
            });

            threads[i].start();
        }

        for(int i=0;i<matrix.getSize();i++) threads[i].join();

        return matrix;
    }

    private static Matrix substractReversed(Matrix matrix, int row, double[] ratios) throws InterruptedException {
        Thread[][] threads = new Thread[row][matrix.getSize()+1];

        int index = 0;
        for(int i=row-1;i>=0;i--){
            for(int j = 0;j<matrix.getSize()+1;j++){
                final int f_i = i;
                final int f_j = j;

                threads[index][j] = new Thread(() -> {
                    double value = matrix.getRow(f_i)[f_j] + ratios[f_i]*matrix.getRow(row)[f_j];

                   matrix.setValue(value, f_i,f_j);
                });

                threads[index][j].start();
            }
            index++;
        }

        for(int i=index-1;i>=0;i--){
            for(int j = 0;j<matrix.getSize()+1;j++){
                threads[i][j].join();
            }
        }

        return matrix;
    }

        public static void main(String[] args) throws InterruptedException {
        Matrix matrix = Parser.parseFile().addResultToMatrix();

        System.out.println(matrix);

        for(int i=0;i<matrix.getSize();i++){
            //swap rows if index cell is 0
            swapIfCurrentCellIsZero(matrix,i);

            //calculate ratios for every row that is lower than current
            double[] ratios = calculateRatioForRows(matrix, i);

            // add/substract lower rows
            matrix = substract(matrix, i, ratios);
        }
        System.out.println("After reducing lower part:");

        System.out.println(matrix);

        System.out.println("After reducing upper part:");

        for(int i = matrix.getSize()-1;i>=0;i--){
            //basically same thing as above

            //Swap rows
            swapIfCurrentCellIsZeroReversed(matrix, i);

            //calculate ratios
            double[] ratios = calculateRatioForRowsReversed(matrix, i);

            matrix = substractReversed(matrix, i, ratios);
        }

        System.out.println(matrix);

        matrix = reduceDiagonal(matrix);

        System.out.println(matrix);

        System.out.println("Result:");
        System.out.println(Arrays.toString(matrix.resultsColumn()));
    }
}
