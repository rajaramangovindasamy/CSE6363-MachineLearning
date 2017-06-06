import java.util.Random;

public class frequentist_estimate2 {

	public static double RandomSortedfn() {
		Random r = new Random();
		double Result = 0;
		String RandomS = "";
		for (int i = 1; i <= 3100; i++) {
			Result = r.nextFloat();
			if (Result <= 0.1) {
				RandomS += "a";
			} else if (Result > 0.1) {
				RandomS += "b";
			}

		}
		int count = 0;
		for (int j = 0; j < RandomS.length(); j++) {
			if (RandomS.charAt(j) == 'a') {
				count++;
			}
		}
		double prob = 0;
		prob = (double) (count) / (double) (RandomS.length());
		double str = Double.parseDouble(String.format("%.4f", prob));
		return str;
	}

	public static void main(String[] args) {

		int a = 0, b = 0, c = 0, d = 0, e = 0;
		for (int i = 1; i <= 10000; i++) {
			double s = RandomSortedfn();
			if (s < 0.08) {
				a++;
			} else if (s < 0.09) {
				b++;
			} else if (s >= 0.09 && s <= 0.11) {
				c++;
			} else if (s > 0.11) {
				d++;
			} else if (s > 0.12) {
				e++;
			}

		}
		System.out.println("In " + a + " of the simulations p(c = 'a') < 0.08 ");
		System.out.println("In " + b + " of the simulations p(c = 'a') < 0.09");
		System.out.println("In " + c + " of the simulations p(c = 'a') is in the interval [0.09, 0.11] ");
		System.out.println("In " + d + " of the simulations p(c = 'a') > 0.11");
		System.out.println("In " + e + " of the simulations p(c = 'a') > 0.12 ");
	}
}