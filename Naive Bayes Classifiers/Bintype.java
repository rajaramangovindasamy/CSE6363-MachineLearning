//RAJARAMAN GOVINDASAMY
import java.util.ArrayList;
import java.util.List;

public class Bintype {
	private List<Double> distribution = null;

	public Bintype() {
		this.distribution = new ArrayList<Double>();
	}
	public void setDistribution(List<Double> distribution) {
		this.distribution = distribution;
	}
	public List<Double> getDistribution() {
		return distribution;
	}
	@Override
	public String toString() {
		return "\nBin [distribution=" + distribution + "]";
	}

}