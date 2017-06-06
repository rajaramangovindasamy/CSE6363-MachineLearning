//RAJARAMAN GOVINDASAMY
import java.util.ArrayList;
import java.util.List;

public class Hist {
	private List<Bintype> bins = null;

	public Hist() {
		this.bins = new ArrayList<Bintype>();
	}
	public void setBins(List<Bintype> bins) {
		this.bins = bins;
	}
	public List<Bintype> getBins() {
		return bins;
	}

	

	@Override
	public String toString() {
		return "Histogram [bins=" + bins + "]";
	}

}