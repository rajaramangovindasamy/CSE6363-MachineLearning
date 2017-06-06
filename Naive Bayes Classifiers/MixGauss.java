//RAJARAMAN GOVINDASAMY
import java.util.List;

public class MixGauss {
	List<Gaussian> mixture;
	List<Double> weights;
	List<List<Double>> probij;

	public List<List<Double>> getProbij() {
		return probij;
	}

	public void setMixture(List<Gaussian> mixture) {
		this.mixture = mixture;
	}
	public List<Double> getWeights() {
		return weights;
	}

	public void setWeights(List<Double> weights) {
		this.weights = weights;
	}

	public List<Gaussian> getMixture() {
		return mixture;
	}
	public void setProbij(List<List<Double>> probij) {
		this.probij = probij;
	}

	@Override
	public String toString() {
		return "MixtureOfGaussians \n[mixture=\n" + mixture + "\nweights=" + weights + "]\n";
	}

}