//RAJARAMAN GOVINDASAMY
public class Gaussian {
	private double mean = 0;
	private double std = 0;

	public double getMean() {
		return mean;
	}

	public void setMean(double mean) {
		this.mean = mean;
	}

	public double getStd() {
		return std;
	}

	public void setStd(double std) {
		this.std = std;
	}

	@Override
	public String toString() {
		return "Gaussian [mean=" + mean + " std=" + std + "]\n";
	}

}