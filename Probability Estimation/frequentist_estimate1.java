import java.util.Random;

public class frequentist_estimate1 {

	public static String RandomStringfn() {
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
		return RandomS;
	}

	public static void main(String[] args) {
		String RandS = RandomStringfn();
		int count = 0;
		for (int j = 0; j < RandS.length(); j++) {
			if (RandS.charAt(j) == 'a') {
				count++;
			}
		}
		double prob = 0;
		prob = (double) (count) / (double) (RandS.length());
		String str = String.format("%.4f", prob);
		System.out.println("\nP(c=\"a\")= " + str);
	}
}