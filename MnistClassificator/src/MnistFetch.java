import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class MnistFetch {

	private String labelFileName;
	private String imageFileName;

	private double[] labels;
	private double[][] images;

	public MnistFetch(String labelFileName, String imageFileName) {
		this.labelFileName = labelFileName;
		this.imageFileName = imageFileName;

	}

	public void loadData() {

		ByteArrayOutputStream labelBuffer = new ByteArrayOutputStream();
		ByteArrayOutputStream imageBuffer = new ByteArrayOutputStream();

		InputStream labelInputStream = this.getClass().getResourceAsStream(this.labelFileName);
		InputStream imageInputStream = this.getClass().getResourceAsStream(this.imageFileName);

		int read;
		byte[] buffer = new byte[16384];

		try {
			while ((read = labelInputStream.read(buffer, 0, buffer.length)) != -1)
				labelBuffer.write(buffer, 0, read);

			labelBuffer.flush();

			while ((read = imageInputStream.read(buffer, 0, buffer.length)) != -1)
				imageBuffer.write(buffer, 0, read);

			imageBuffer.flush();

		} catch (IOException e) {

		}

		byte[] labelBytes = labelBuffer.toByteArray();
		byte[] imageBytes = imageBuffer.toByteArray();

		byte[] labelMagic = Arrays.copyOfRange(labelBytes, 0, 4);
		byte[] imageMagic = Arrays.copyOfRange(imageBytes, 0, 4);

		if (ByteBuffer.wrap(labelMagic).getInt() != 2049)
			throw new RuntimeException("Bad Magic number in label file name");

		if (ByteBuffer.wrap(imageMagic).getInt() != 2051)
			throw new RuntimeException("Bad Magic number in image file name");

		int numberOfLabels = ByteBuffer.wrap(Arrays.copyOfRange(labelBytes, 4, 8)).getInt();
		int numberOfImages = ByteBuffer.wrap(Arrays.copyOfRange(imageBytes, 4, 8)).getInt();

		if (numberOfImages != numberOfLabels)
			throw new RuntimeException("The number of labels and images do not match!");

		int numRows = ByteBuffer.wrap(Arrays.copyOfRange(imageBytes, 8, 12)).getInt();

		if (numRows != 28 && numRows != 28)
			throw new RuntimeException("Bad image. Rows and columns do not equal " + 28 + "x" + 28);

		images = new double[numberOfLabels][784];
		labels = new double[numberOfLabels];

		for (int i = 0; i < numberOfLabels; i++) {
			int label = labelBytes[8 + i];
			byte[] imageData = Arrays.copyOfRange(imageBytes, (i * 784) + 16, (i * 784) + 800);
			double[] row = new double[784];
			for (int j = 0; j < imageData.length; j++) {
				row[j] = imageData[j] & 0xFF;
			}
			images[i] = Arrays.copyOf(normalize(row), 784);
			labels[i] = (double) label;

		}

	}

	public double[][] get2DImage(int imageNo) {
		double[][] image = new double[28][28];
		for (int i = 0 , k = 0; i < 28; i++) {
			for (int j = 0; j < 28; j++, k++) {
				image[i][j] = images[imageNo][k];
			}
		}
		return image;
	}

	private double[] normalize(double[] data) {
		for (int i = 0; i < data.length; i++) {
			data[i] = data[i] / 255.0;
		}
		return data;
	}

	public double[][] getImages() {
		return images;
	}

	public double[] getLabels() {
		return labels;
	}

	public double[] getImage(int index) {
		return images[index];
	}

	public double getLabel(int index) {
		return labels[index];
	}
	public int getNumImages() {
		return images.length;
	}
	
}
