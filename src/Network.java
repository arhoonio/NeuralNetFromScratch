public class Network {
    protected Layer[] layers;
    protected double[] output;

    public Network(int[] neuronsInLayer) {
        layers = new Layer[neuronsInLayer.length];
        for(int i = 0; i < neuronsInLayer.length; i++) { // number of levels
            Neuron[] neurons = new Neuron[neuronsInLayer[i]];
            for(int j = 0; j < neuronsInLayer[i]; j++) { // number of neurons
                if (i != 0) {
                    Edge[] edgesEntering = new Edge[neuronsInLayer[i - 1]];
                    neurons[j] = new Neuron(edgesEntering);
                    for (int k = 0; k < edgesEntering.length; k++) { // wizardry
                        edgesEntering[k] = new Edge(layers[i - 1].neurons[k], neurons[j]);
                    }
                } else {
                    neurons[j] = new Neuron(0);
                }

            }
            layers[i] = new Layer(neurons);
        }
    }

    public double[] calculateNetwork(double input[]) {
        for(int i = 0; i < layers[0].neurons.length; i++) {
            layers[0].neurons[i].updateOutput(input[i]);
        }
        for (int i = 1; i < layers.length; i++) {
            layers[i].calculateLayer();
        }
        
        double[] output = new double[layers[layers.length - 1].neurons.length];
        for (int i = 0; i < output.length; i++){
            output[i] = layers[layers.length - 1].neurons[i].output;
        }
        this.output = output;
        return output;
    }

    private double computeWeightDerivs(int i, int j, int k, boolean derivWeight){
        Edge edge = layers[i].neurons[j].edgesEntering[k];
        double dZdW;
        if(derivWeight) {
            dZdW = edge.source.output;
        } else {
            dZdW = edge.weight;
        }
        double dAdZ = (Math.pow(Math.E, edge.edgeOutput))/(Math.pow((Math.pow(Math.E, edge.edgeOutput) + 1), 2));
        return dZdW * dAdZ;
    }

     private double computeBiasDerivs(int i, int j, int k, boolean derivBias){
        Edge edge = layers[i].neurons[j].edgesEntering[k];
        double dZdB;
        if (derivBias) {
            dZdB = 1;
            
        } else {
            dZdB = edge.weight;
        }
        double dAdZ = (Math.pow(Math.E, edge.edgeOutput))/(Math.pow((Math.pow(Math.E, edge.edgeOutput) + 1), 2));
        return dZdB * dAdZ;
     }

    public void implementChanges() {
        for(int i = layers.length - 1; i > 0; i--) { // number of layers
            for(int j = 0; j < layers[i].neurons.length; j++) { // number of neurons
                for(int k = 0; k < layers[i].neurons[j].edgesEntering.length; k++) { // number of edges entering neuron
                    layers[i].neurons[j].edgesEntering[k].implementChanges();
                }
            }
        }
    }

    public void backpropagation(double expected) {
        for(int i = layers.length - 1; i > 0; i--) { // number of layers
            for(int j = 0; j < layers[i].neurons.length; j++) { // number of neurons
                if (layers[i].neurons[j].prevdCdA == 0) { // if we are at the final neuron,
                    for(int k = 0; k < layers[i].neurons[j].edgesEntering.length; k++) { // number of edges entering
                        double dCdW = 2 * (output[0] - expected) * computeWeightDerivs(i, j, k, true);
                        double dCdB = 2 * (output[0] - expected) * computeBiasDerivs(i, j, k, true);
                        layers[i].neurons[j].edgesEntering[k].updateChanges(dCdW, dCdB);
                        layers[i - 1].neurons[k].prevdCdA += computeWeightDerivs(i, j, k, false);
                    } 
                } else {
                    for(int k = 0; k < layers[i].neurons[j].edgesEntering.length; k++) { // number of edges entering
                        double dCdW = computeWeightDerivs(i, j, k, true) * layers[i].neurons[j].prevdCdA;
                        double dCdB = computeBiasDerivs(i, j, k, true) * layers[i].neurons[j].prevdCdA;
                        layers[i].neurons[j].edgesEntering[k].updateChanges(dCdW, dCdB);
                        layers[i - 1].neurons[k].prevdCdA += computeWeightDerivs(i, j, k, false) * 
                            layers[i].neurons[j].prevdCdA;
                    }
                }
                
            }
        }
    }
}
