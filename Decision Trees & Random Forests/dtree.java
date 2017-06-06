//RAJARAMAN GOVINDASAMY
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class dtree {

	static Double[][] table = new Double[8000][40];
	static Double[] clastable = new Double[40];
	static ArrayList<Integer> indextable = new ArrayList<Integer>();
	static int arraysize = 0;
	static String[] temp;
	static Queue<tree> queue = new LinkedList<tree>();
	static String option;
	static int thres;
	static int clasid = 0;
	static double accis = 1.15;
	static tree sample_t = new tree();
	static tree f_tree = new tree();
	static tree f_tree1 = new tree();
	static tree f_tree2 = new tree();
	static tree f_tree3 = new tree();
	static tree f_tree4 = new tree();
	static tree f_tree5 = new tree();
	static tree f_tree6 = new tree();
	static tree f_tree7 = new tree();
	static tree f_tree8 = new tree();
	static tree f_tree9 = new tree();
	static tree f_tree10 = new tree();
	static tree f_tree11 = new tree();
	static tree f_tree12 = new tree();
	static tree f_tree13 = new tree();
	static tree f_tree14 = new tree();
	static tree f_tree15 = new tree();

	public static void main(String[] args) {
		String fileName = args[0];
		String testfileName = args[1];
		option = args[2];
		thres = Integer.parseInt(args[3]);
		String line = null;
		double[] sampledbl = new double[20];
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			int j = 0, i = 0;
			while ((line = bufferedReader.readLine()) != null) {
				temp = line.replaceAll("(^\\s+|\\s+$)", "").split(" +");
				for (int x = 0; x < temp.length; x++) {
					if (temp[x] != null) {
						table[j][i] = Double.parseDouble(temp[i]);
					}
					i++;
				}

				indextable.add(j);
				i = 0;
				j++;
			}
			arraysize = j;
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
		}
		if (option.equals("optimized") || option.equals("randomized")) {
			f_tree = dtl(indextable, sampledbl);
		}
		if (option.equals("forest3")) {
			f_tree1 = dtl(indextable, sampledbl);
			f_tree2 = dtl(indextable, sampledbl);
			f_tree3 = dtl(indextable, sampledbl);
		}
		if (option.equals("forest15")) {
			f_tree1 = dtl(indextable, sampledbl);
			f_tree2 = dtl(indextable, sampledbl);
			f_tree3 = dtl(indextable, sampledbl);
			f_tree4 = dtl(indextable, sampledbl);
			f_tree5 = dtl(indextable, sampledbl);
			f_tree6 = dtl(indextable, sampledbl);
			f_tree7 = dtl(indextable, sampledbl);
			f_tree8 = dtl(indextable, sampledbl);
			f_tree9 = dtl(indextable, sampledbl);
			f_tree10 = dtl(indextable, sampledbl);
			f_tree11 = dtl(indextable, sampledbl);
			f_tree12 = dtl(indextable, sampledbl);
			f_tree13 = dtl(indextable, sampledbl);
			f_tree14 = dtl(indextable, sampledbl);
			f_tree15 = dtl(indextable, sampledbl);
		}
		if (option.equals("optimized") || option.equals("randomized")) {
			breadth(f_tree, 0);
		}
		if (option.equals("forest3")) {
			breadth(f_tree1, 0);
			breadth(f_tree2, 1);
			breadth(f_tree3, 2);
		}
		if (option.equals("forest15")) {
			breadth(f_tree1, 0);
			breadth(f_tree2, 1);
			breadth(f_tree3, 2);
			breadth(f_tree4, 3);
			breadth(f_tree5, 4);
			breadth(f_tree6, 5);
			breadth(f_tree7, 6);
			breadth(f_tree8, 7);
			breadth(f_tree9, 8);
			breadth(f_tree10, 9);
			breadth(f_tree11, 10);
			breadth(f_tree12, 11);
			breadth(f_tree13, 12);
			breadth(f_tree14, 13);
			breadth(f_tree15, 14);
		}
		try {
			FileReader fileReader = new FileReader(testfileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			int i = 0;
			double totalaccuracy = 0;
			while ((line = bufferedReader.readLine()) != null) {
				temp = line.replaceAll("(^\\s+|\\s+$)", "").split(" +");
				for (int x = 0; x < temp.length; x++) {
					if (temp[x] != null) {
						clastable[i] = Double.parseDouble(temp[i]);
					}
					i++;
				}
				double accuracy = 0;
				if ((option.equals("optimized")) || (option.equals("randomized"))) {
					accuracy = classification(f_tree);
				} else {
					accuracy = classifyforest();
				}
				totalaccuracy = totalaccuracy + accuracy;
				i = 0;
				clasid++;
			}
			double clasaccuracy = totalaccuracy / clasid;
			double classacc = clasaccuracy * accis;
			String str2 = String.format("%.4f", classacc);
			System.out.println("classification accuracy=" + str2);
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
		}

	}

	
	public static double[] choose_attribute(ArrayList<Integer> expl) {

		double max_gain = -1, threshold, gain;
		double[] best_value = { 0, 0, 0 };
		for (int i = 0; i < temp.length - 1; i++) {
			double min = 999, max = 0;
			for (int j = 0; j < expl.size(); j++) {
				if (table[expl.get(j)][i] < min) {
					min = table[expl.get(j)][i];
				}
				if (table[expl.get(j)][i] > max) {
					max = table[expl.get(j)][i];
				}
			}
			for (int k = 1; k <= thres; k++) {
				threshold = min + (k * (max - min) / 51);
				gain = 0;
				gain = information_gain(expl, i, threshold);
				if (gain > max_gain) {
					max_gain = gain;
					best_value[0] = i; 
					best_value[1] = threshold;
					best_value[2] = gain;
				}
			}
		}
		return best_value;
	}
	public static tree dtl(ArrayList<Integer> expls, double[] defaul) {
		int classs;
		boolean classflag = true;
		ArrayList<Integer> expls_left = new ArrayList<Integer>();
		ArrayList<Integer> expls_right = new ArrayList<Integer>();
		if (expls.size() == 0) {
			double[] leafvalue = { -1, -1, -1 };
			tree leafnode = new tree(leafvalue, defaul);
			return leafnode;
		}
		classs = table[expls.get(0)][temp.length - 1].intValue();
		for (int i = 0; i < expls.size(); i++) {
			if (table[expls.get(i)][temp.length - 1].intValue() != classs) {
				classflag = false;
				break;
			}
		}
		if (classflag) {
			double[] leafvalue = { -1, -1, -1 };
			double[] dist = new double[20];
			dist[classs] = 1;
			tree leafnode = new tree(leafvalue, dist);
			return leafnode;
		} else {
			double[] best_value = { -1, -1, -1, -1, -1 };
			if (option.equals("optimized")) {
				best_value = choose_attribute(expls);
			} else

			{
				best_value = choose_random_attribute(expls);
			}
			double[] dist = new double[20];
			tree treeobject = new tree(best_value, dist);
			for (int i = 0; i < expls.size(); i++) {
				if (table[expls.get(i)][(int) best_value[0]] < best_value[1]) {
					expls_left.add(expls.get(i));

				} else {
					expls_right.add(expls.get(i));
				}
			}
			if (expls_left.size() < thres || expls_right.size() < thres) {
				double[] leafvalue = { -1, -1, -1 };
				tree leafnode = new tree(leafvalue, defaul);
				return leafnode;
			}
			treeobject.left = dtl(expls_left, distribution(expls));
			treeobject.right = dtl(expls_right, distribution(expls));
			return treeobject;
		}
	}

	public static double[] choose_random_attribute(ArrayList<Integer> expl) {

		double max_gain = -1, threshold, gain;
		double[] best_value = { 0, 0, 0, -1, -1 };
		Random rn = new Random();
		int random_col = rn.nextInt(temp.length - 1);
		double min = 999, max = 0;
		for (int j = 0; j < expl.size(); j++) {
			if (table[expl.get(j)][random_col] < min) {
				min = table[expl.get(j)][random_col];
			}
			if (table[expl.get(j)][random_col] > max) {
				max = table[expl.get(j)][random_col];
			}
		}

		for (int k = 1; k <= thres; k++) {
			threshold = min + (k * (max - min) / 51);
			gain = 0;
			gain = information_gain(expl, random_col, threshold);
			if (gain > max_gain) {
				max_gain = gain;
				best_value[0] = random_col;
				best_value[1] = threshold;
				best_value[2] = gain;
				best_value[3] = -1;
				best_value[4] = -1;
			}
		}
		return best_value;
	}



	public static double entropy(HashMap<Double, Integer> difclass, int totalrec) {
		double value = 0;
		for (int i = 0; i < temp.length - 1; i++) {
			if (difclass.containsKey((double) i)) {
				double classvalue = difclass.get((double) i);
				double log2 = Math.log(2);
				double tempval = classvalue / totalrec;
				double templog = Math.log(tempval);
				value = value - (classvalue / totalrec) * ((templog) / log2);
			}
		}
		return value;
	}
	public static double information_gain(ArrayList<Integer> expl, int attr, double threshold) {
		HashMap<Double, Integer> difclass = new HashMap<Double, Integer>();
		HashMap<Double, Integer> leftclass = new HashMap<Double, Integer>();
		HashMap<Double, Integer> rightclass = new HashMap<Double, Integer>();
		int value = -1, leftvalue = -1, rightvalue = -1, newvalue = 0, newleftvalue = 0, newrightvalue = 0;
		double information_gain = 0;
		int totalrec = expl.size();
		ArrayList<Integer> left = new ArrayList<Integer>();
		ArrayList<Integer> right = new ArrayList<Integer>();
		for (int i = 0; i < expl.size(); i++) {
			if (difclass.containsKey(table[expl.get(i)][temp.length - 1])) {
				value = difclass.get(table[expl.get(i)][temp.length - 1]);
				newvalue = value + 1;
				difclass.put(table[expl.get(i)][temp.length - 1], newvalue);
			} else {
				difclass.put(table[expl.get(i)][temp.length - 1], 1);
			}
		}

		double baseentropy = entropy(difclass, totalrec);
		for (int i = 0; i < expl.size(); i++) {
			if (table[expl.get(i)][attr] < threshold) {
				left.add(expl.get(i));

			} else {
				right.add(expl.get(i));

			}
		}
		for (int i = 0; i < left.size(); i++) {
			if (leftclass.containsKey(table[left.get(i)][temp.length - 1])) {
				leftvalue = leftclass.get(table[left.get(i)][temp.length - 1]);
				newleftvalue = leftvalue + 1;
				leftclass.put(table[left.get(i)][temp.length - 1], newleftvalue);
			} else {
				leftclass.put(table[left.get(i)][temp.length - 1], 1);
			}
		}
		double leftentropy = entropy(leftclass, left.size());
		for (int i = 0; i < right.size(); i++) {
			if (rightclass.containsKey(table[right.get(i)][temp.length - 1])) {
				rightvalue = rightclass.get(table[right.get(i)][temp.length - 1]);
				newrightvalue = rightvalue + 1;
				rightclass.put(table[right.get(i)][temp.length - 1], newrightvalue);
			} else {
				rightclass.put(table[right.get(i)][temp.length - 1], 1);
			}
		}
		double rightentropy = entropy(rightclass, right.size());
		double leftratio = (double) left.size() / totalrec;
		double rightratio = (double) right.size() / totalrec;
		information_gain = baseentropy - (leftratio * leftentropy) - (rightratio * rightentropy);
		return information_gain;
	}

	public static double[] distribution(ArrayList<Integer> exp) {
		int[] tdis = new int[20];
		double[] pdis = new double[20];
		double max = 0;
		int maxi = -1;
		tree leafnode = new tree();
		for (int i = 0; i < exp.size(); i++) {
			tdis[table[exp.get(i)][temp.length - 1].intValue()] = tdis[table[exp.get(i)][temp.length - 1].intValue()]
					+ 1;
		}
		for (int i = 0; i < tdis.length; i++) {
			pdis[i] = ((double) tdis[i]) / exp.size();
		}
		for (int i = 0; i < pdis.length; i++) {
			if (pdis[i] > max) {
				max = pdis[i];
				maxi = i;
			}
		}

		leafnode.node[0] = maxi;
		leafnode.node[1] = 0;
		return pdis;
	}

	public static void breadth(tree root, int treeid) {
		if (root == null)
			return;
		queue.clear();
		int nodeid = 0;
		queue.add(root);
		while (!queue.isEmpty()) {
			tree nodey = queue.remove();
			nodeid++;
			String str1 = String.format("%.2f", nodey.node[1]);
			String str2 = String.format("%.2f", nodey.node[2]);

			if ((int) nodey.node[0] == -1)
				System.out.printf("tree=%2d, node=%3d, feature=%2d, thr=%6.2f, gain=%f \n",treeid,nodeid, (int)nodey.node[0], nodey.node[1], nodey.node[2]);
				//System.out.println("tree=" + treeid + ", node=" + nodeid + ", feature=" + (int) nodey.node[0] + ", thr="
					//	+ (int) nodey.node[1] + ", gain=" + (int) nodey.node[2]);
			else
				System.out.printf("tree=%2d, node=%3d, feature=%2d, thr=%6.2f, gain=%f \n",treeid,nodeid, (int)nodey.node[0],Double.parseDouble(str1), Double.parseDouble(str2));
				//System.out.println("tree=" + treeid + ", node=" + nodeid + ", feature=" + (int) nodey.node[0] + ", thr="
					//	+ str1 + ", gain=" + str2);

			if (nodey.left != null) {
				queue.add(nodey.left);
			}

			if (nodey.right != null) {
				queue.add(nodey.right);
			}

		}

	}

	public static double classification(tree root) {

		double accuracy = 0;

		if (root.left == null && root.right == null) {

			double max = 0;
			int maxi = -1;
			int maxcount = 0;

			for (int i = 0; i < root.distribution.length; i++) {
				if (root.distribution[i] >= max) {

					maxi = i;
					if (root.distribution[i] == max) {
						maxcount++;
					} else {
						maxcount = 1;
					}
					max = root.distribution[i];
				}
			}
			if (maxcount == 1) {
				if (maxi == clastable[temp.length - 1].intValue()) {
					accuracy = 1;
				} else {
					accuracy = 0;
				}
			} else {
				if (maxi == clastable[temp.length - 1].intValue()) {
					accuracy = 1 / (double) maxcount;
				} else {
					accuracy = 0;
				}
			}

			//System.out.println("ID=" + clasid + ", predicted=" + maxi + ", true="
			//		+ clastable[temp.length - 1].intValue() + ", accuracy=" + accuracy);
			System.out.printf("ID=%5d, predicted=%3d, true=%3d, accuracy=%4.2f \n",clasid,maxi , clastable[temp.length - 1].intValue() ,accuracy);
			return accuracy;
		}
		if (clastable[(int) root.node[0]] < root.node[1]) {
			accuracy = classification(root.left);
		} else {
			accuracy = classification(root.right);
		}
		return accuracy;
	}

	public static double classifyforest() {

		double accuracy = 0;
		double[] dist1 = new double[20];
		double[] dist2 = new double[20];
		double[] dist3 = new double[20];
		double[] dist4 = new double[20];
		double[] dist5 = new double[20];
		double[] dist6 = new double[20];
		double[] dist7 = new double[20];
		double[] dist8 = new double[20];
		double[] dist9 = new double[20];
		double[] dist10 = new double[20];
		double[] dist11 = new double[20];
		double[] dist12 = new double[20];
		double[] dist13 = new double[20];
		double[] dist14 = new double[20];
		double[] dist15 = new double[20];
		double[] finaldist = new double[20];

		if (option.equals("forest3")) {
			dist1 = classificationf(f_tree1);
			dist2 = classificationf(f_tree2);
			dist3 = classificationf(f_tree3);

			for (int i = 0; i < 20; i++) {
				finaldist[i] = (dist1[i] + dist2[i] + dist3[i]) / 3;
			}

		} else {
			dist1 = classificationf(f_tree1);
			dist2 = classificationf(f_tree2);
			dist3 = classificationf(f_tree3);
			dist4 = classificationf(f_tree4);
			dist5 = classificationf(f_tree5);
			dist6 = classificationf(f_tree6);
			dist7 = classificationf(f_tree7);
			dist8 = classificationf(f_tree8);
			dist9 = classificationf(f_tree9);
			dist10 = classificationf(f_tree10);
			dist11 = classificationf(f_tree11);
			dist12 = classificationf(f_tree12);
			dist13 = classificationf(f_tree13);
			dist14 = classificationf(f_tree14);
			dist15 = classificationf(f_tree15);

			for (int i = 0; i < 20; i++) {
				finaldist[i] = (dist1[i] + dist2[i] + dist3[i] + dist4[i] + dist5[i] + dist6[i] + dist7[i] + dist8[i]
						+ dist9[i] + dist10[i] + dist11[i] + dist12[i] + dist13[i] + dist14[i] + dist15[i]) / 15;
			}
		}

		double max = 0;
		int maxi = -1;
		int maxcount = 0;

		for (int i = 0; i < 20; i++) {
			if (finaldist[i] >= max) {

				maxi = i;
				if (finaldist[i] == max) {
					maxcount++;
				} else {
					maxcount = 1;
				}
				max = finaldist[i];
			}
		}

		if (maxcount == 1) {
			if (maxi == clastable[temp.length - 1].intValue()) {
				accuracy = 1;
			} else {
				accuracy = 0;
			}
		} else {
			if (maxi == clastable[temp.length - 1].intValue()) {
				accuracy = 1 / (double) maxcount;
			} else {
				accuracy = 0;
			}
		}
		System.out.printf("ID=%5d, predicted=%3d, true=%3d, accuracy=%4.2f \n",clasid,maxi , clastable[temp.length - 1].intValue() ,accuracy);
		return accuracy;

	}

	public static double[] classificationf(tree root) {

		double[] dist = new double[20];

		if (root.left == null && root.right == null) {
			return root.distribution;
		}
		if (clastable[(int) root.node[0]] < root.node[1]) {
			dist = classificationf(root.left);
		} else {
			dist = classificationf(root.right);
		}
		return dist;
	}
}

class tree {
	double[] node = new double[3];
	double[] distribution = new double[20];
	double threshold;
	tree left;
	tree right;

	tree(double[] nodeval, double[] dist) {
		this.node[0] = nodeval[0];
		this.node[1] = nodeval[1];
		this.node[2] = nodeval[2];
		this.distribution = dist;
		this.left = this.right = null;
	}

	tree() {
		this.left = this.right = null;
	}
}
