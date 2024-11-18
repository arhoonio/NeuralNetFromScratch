import java.util.Random;

public class Edge {
    protected double weight;
    protected double bias;
    protected double weightChange;
    protected double biasChange;
    protected Neuron target;
    protected Neuron source;
    protected double edgeOutput;

    Random rand = new Random();

    Edge(Neuron source, Neuron target) {
        this.source = source;
        this.target = target;
        weight = rand.nextGaussian();
        bias = rand.nextGaussian();
    }

    double calculateOuput() {
        return (weight * source.output) + bias;
    }

    void updateChanges(double weightChange, double biasChange) {
        this.weightChange += weightChange;
        this.biasChange += biasChange;
    }

    void implementChanges(){
        this.weight += weightChange;
        this.bias += biasChange;
    }
}