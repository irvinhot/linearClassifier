/**
 * Simple linear classificator using MNIST database
 * @author Irvin Hot <irwinhot@gmail.com>
 * @version 1.0
 */
public class Classifier {
	private double[][] inputs;
	private double[] outputs;
	private double[] binarized;
	private double[] params;
	private double[][] trainedParams;

	private int examples;
	private int features;
	private int numClasses;
	
	private double learningRate;
	private int batchSize;

	public void loadTrainingData(double[][] data, double[] labels) {
		inputs = new double[data.length][data[0].length+1];
		for (int i = 0; i < data.length; i++) {
			inputs[i][0] = 1.0;
			System.arraycopy(data[i], 0, inputs[i], 1, data[i].length);
		}

		outputs = new double[labels.length];
		System.arraycopy(labels, 0, outputs, 0, labels.length);
		
		features = inputs[0].length;
		examples = inputs.length;
		
		this.params = new double[features];
	}
	
	public void train(int numClasses, double learningRate, int batchSize) {
		this.numClasses = numClasses;
		this.learningRate = learningRate;
		this.batchSize = batchSize;
		trainedParams = new double[numClasses][features];
		binarized = new double[examples];
		for(int i=0; i<numClasses; i++) {
			System.out.println("Training class: " + i);
			binarizeOutputs(i);
			gradientDescent(i);
			}
	}
	
	public double hypothesis(double[] row) {
		double hypothesis = 0.0;
		for (int i = 0; i < row.length; i++)
			hypothesis += row[i] * params[i];
		if (hypothesis > 0)
			return 1.0 / (1.0 + Math.exp(-hypothesis));
		else {
			double z = Math.exp(hypothesis);
			return z / (1 + z);
		}
	}
	
	public void gradientDescent(int numClass) {
		int i = 0;
		double[] h_theta_label = new double[examples];
		while (i < examples-batchSize) {

			for (int k = i; k < i+batchSize; k++) {
				h_theta_label[k] = hypothesis(inputs[k]) - binarized[k];
			}

			double gradSum = 0.0;
			for (int z = 0; z < features; z++) {
				gradSum = 0.0;
				for (int t = i; t < i+batchSize; t++) {
					gradSum += inputs[t][z] * h_theta_label[t];
				}
				params[z] = params[z] - (learningRate / (double) batchSize) * gradSum;
				
			}
			i+=batchSize;
		}
		System.arraycopy(params, 0, trainedParams[numClass], 0, params.length);
	}
	
	private void binarizeOutputs(int numClass) {
			for (int i = 0; i < outputs.length; i++) {
				if (outputs[i] == (double) numClass)
					binarized[i] = 1.0;
				else
					binarized[i] = 0.0;
			}
	}
	
	public double[] getPredictions(double[] input) {
		double[] prediction = new double[trainedParams.length];

		double[] input_data = new double[input.length + 1];
		input_data[0] = 1.0;
		System.arraycopy(input, 0, input_data, 1, input.length);

		
		params = new double[input_data.length];
		for (int i = 0; i < trainedParams.length; i++) {
			System.arraycopy(trainedParams[i], 0, params, 0, trainedParams[i].length);
			prediction[i] = hypothesis(input_data);
		}
		return prediction;
	}
	
	public int prediction(double[] input) {
		double[] predicted = getPredictions(input);
		double maxValue = 0.0;
		int indexOfMax = 0;
		for (int i = 0; i < predicted.length; i++) {
			if (predicted[i] > maxValue) {
				maxValue = predicted[i];
				indexOfMax = i;
			}
		}
		return indexOfMax;
	}

}
