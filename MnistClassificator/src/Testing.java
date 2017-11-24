
public class Testing {

	public static void main(String[] args) {
		// Training phase
		MnistFetch mnist = new MnistFetch("train-labels.idx1-ubyte","train-images.idx3-ubyte");
		mnist.loadData();
		double[][] images = mnist.getImages(); 
		double[] labels = mnist.getLabels(); 
		
		Classifier linearClassifier = new Classifier();
		linearClassifier.loadTrainingData(images, labels);
		linearClassifier.train(10, 0.1, 16);	
		
		// Testing phase
		MnistFetch mnistTest = new MnistFetch("t10k-labels.idx1-ubyte", "t10k-images.idx3-ubyte");
		mnistTest.loadData();

		int failedToGuess = 0;
		for (int i = 0; i < mnistTest.getNumImages(); i++) {
			double label = mnistTest.getLabel(i);
			double predicted = linearClassifier.prediction(mnistTest.getImage(i));
			if (label != predicted) 
				failedToGuess++;	
		}
		System.out.println("Total images: " + mnistTest.getNumImages() + ". Wrong Predictions: " + failedToGuess + " or "
				+ (((double)failedToGuess/mnistTest.getNumImages() * 100.0) / 100.0) * 100.0 + "%");

		
	}

}
