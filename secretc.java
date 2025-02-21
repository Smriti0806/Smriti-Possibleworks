import java.io.*;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.util.*;

public class Secretc {

    public static void main(String[] args) {
        String file1 = "testcase1.json";
        String file2 = "testcase2.json";

        try {
            List<double[]> testCase1 = parseJson(file1);
            List<double[]> testCase2 = parseJson(file2);
            
            solveAndPrintConstant(testCase1);
            solveAndPrintConstant(testCase2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<double[]> parseJson(String filename) throws Exception {
        List<double[]> points = new ArrayList<>();
        
        InputStream is = new FileInputStream(filename);
        JSONTokener tokener = new JSONTokener(is);
        JSONObject jsonObject = new JSONObject(tokener);
        
        for (String key : jsonObject.keySet()) {
            if (!key.equals("keys")) {
                JSONObject obj = jsonObject.getJSONObject(key);
                int x = Integer.parseInt(key);
                String valueStr = obj.getString("value");
                int base = obj.getInt("base");
                long y = Long.parseLong(valueStr, base);
                points.add(new double[]{x, y});
            }
        }
        return points;
    }

    private static void solveAndPrintConstant(List<double[]> points) {
        int n = points.size();
        double[][] matrix = new double[n][n + 1];
        
        for (int i = 0; i < n; i++) {
            double x = points.get(i)[0];
            double y = points.get(i)[1];
            for (int j = 0; j < n; j++) {
                matrix[i][j] = Math.pow(x, j);
            }
            matrix[i][n] = y;
        }
        
        double[] coefficients = gaussElimination(matrix, n);
        System.out.println("Constant term: " + coefficients[0]);
    }

    private static double[] gaussElimination(double[][] matrix, int n) {
        for (int i = 0; i < n; i++) {
            int maxRow = i;
            for (int k = i + 1; k < n; k++) {
                if (Math.abs(matrix[k][i]) > Math.abs(matrix[maxRow][i])) {
                    maxRow = k;
                }
            }
            double[] temp = matrix[i];
            matrix[i] = matrix[maxRow];
            matrix[maxRow] = temp;

            for (int k = i + 1; k < n; k++) {
                double factor = matrix[k][i] / matrix[i][i];
                for (int j = i; j <= n; j++) {
                    matrix[k][j] -= factor * matrix[i][j];
                }
            }
        }
        
        double[] result = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            result[i] = matrix[i][n] / matrix[i][i];
            for (int k = i - 1; k >= 0; k--) {
                matrix[k][n] -= matrix[k][i] * result[i];
            }
        }
        return result;
    }
}
