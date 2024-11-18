public class Layer {
    protected Neuron[] neurons;
    Layer(Neuron[] neurons) {
        this.neurons = neurons;
    }

    void calculateLayer() {
        for(Neuron neuron : neurons) {
            neuron.calculateNeuron();
        }
    }
}