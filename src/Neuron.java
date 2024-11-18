import java.lang.Math;

public class Neuron {
    protected double output;
    protected Edge[] edgesEntering;
    protected double prevdCdA = 0;

    public Neuron(Edge[] edgesEntering) {
        this.edgesEntering = edgesEntering;
        this.output = 0;
    }

    // for the first layer
    public Neuron(double output){
        this.output = output;
    }

    public void calculateNeuron() {
        if (edgesEntering != null && edgesEntering[0] != null) {
            for (Edge edge : edgesEntering) {
                output += edge.calculateOuput();
            }
            output = 1.0 / (1.0 + (Math.pow(Math.E,-output)));
        }
    }

    public void updateOutput(double output) {
        this.output = output;
    }
}
