import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Controller {
    public static double[] expected;
    public static double[][] inputs;

    public static void initData() throws NumberFormatException, IOException {
        String fileName = "src/titanic.csv";
        FileReader fis = new FileReader(fileName);
        BufferedReader myInput = new BufferedReader(fis); 
        String thisLine; 

        // CSV manipulation
        List<double[]> lines = new ArrayList<double[]>();
        List<Double> expected = new ArrayList<Double>();
        while ((thisLine = myInput.readLine()) != null) {
            String[] thisLineArray = new String[7];
            double[] thisLineDouble = new double[6];
            thisLineArray = thisLine.split(",");
            for (int i = 0; i < thisLineArray.length; i++){
                if (i != 0) {
                    thisLineDouble[i - 1] = Double.parseDouble(thisLineArray[i]);
                } else {
                    expected.add(Double.parseDouble(thisLineArray[i]));
                }
            }
            lines.add(thisLineDouble);
        }
        myInput.close();

        Controller.inputs = new double[lines.size()][0];
        
        Double[] expectedValuesTemp = new Double[expected.size()];
        lines.toArray(Controller.inputs);
        expected.toArray(expectedValuesTemp);

        Controller.expected = Stream.of(expectedValuesTemp).mapToDouble(Double::doubleValue).toArray();
    }


    
    public static void main(String[] args) throws NumberFormatException, IOException {
        initData();

        // initialization
        int[] neuronsInLayer = {6,10,20,1};
        Network network = new Network(neuronsInLayer);
        
        for (int i = 0; i < inputs.length - 20; i++) {
            network.calculateNetwork(inputs[i]);
            network.backpropagation(expected[i]);
            if ((i + 1) % 100 == 0) {
                System.out.println("Actual: " + network.output[0]);
                System.out.println("Expected: " + expected[i]);
                System.out.println("-----------------");
                network.implementChanges();
            }
            
        }
        network.implementChanges();
        for (int j = inputs.length - 20; j < inputs.length; j++) {
            double[] output = network.calculateNetwork(inputs[j]);
            System.out.println("Actual: " + output[0]);
            System.out.println("Expected: " + expected[j]);
            System.out.println("-----------------");
        }
    }
}