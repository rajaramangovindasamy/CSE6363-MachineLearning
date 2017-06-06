//RAJARAMAN GOVINDASAMY
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class naive_bayes {
	public static final String HISTOGRAMS = "histograms";
	public static final String GAUSSIANS = "gaussians";
	public static final String MIXTURES = "mixtures";
	private static int noOfAttributes = -1;
	private static int noOfClasses = -1;
	private static int noOfBins = -1;
	private static int noOfGaussians = -1;
	private static List<Double> minList = null;
	private static List<Double> maxList = null;
	private static List<List<Gaussian>> gaussians = null;
	private static List<List<MixGauss>> mixtures = null;
	private static List<Hist> histograms = null;
	private static List<Double> probDistOfClass = null;
	private static String trainingData;
	private static String testData;
	private static String ClassificationMethod;

	private static List<List<MixGauss>> trainingMixtures(List<List<Double>> data1) {
		List<Integer> classDistributionVar = generateClassDist(data1);
		classProbDistFn(classDistributionVar, data1.size());
		List<List<MixGauss>> mixtures = new ArrayList<List<MixGauss>>();
		List<List<List<Double>>> data = new ArrayList<List<List<Double>>>();
		minList = new ArrayList<Double>();
		maxList = new ArrayList<Double>();
		for (int i = 0; i < noOfAttributes; i++) {
			List<List<Double>> classList = new ArrayList<List<Double>>();
			for (int j = 0; j < noOfClasses; j++) {
				List<Double> listOfVals = new ArrayList<Double>();
				classList.add(listOfVals);
			}
			data.add(classList);
		}
		for (int i = 0; i < data1.size(); i++) {
			List<Double> patternVar = getPattern(data1.get(i));
			int currClass = getClass(data1.get(i));
			for (int attribute = 0; attribute < noOfAttributes; attribute++) {
				double value = patternVar.get(attribute);
				data.get(attribute).get(currClass).add(value);
			}
		}

		for (int attribute = 0; attribute < noOfAttributes; attribute++) {
			List<MixGauss> mixGaussList = new ArrayList<MixGauss>();
			for (int currClass = 0; currClass < noOfClasses; currClass++) {
				MixGauss MixGauss = mixGaussInit(data.get(attribute).get(currClass));
				EM(MixGauss, data.get(attribute).get(currClass));
				mixGaussList.add(MixGauss);
			}
			mixtures.add(mixGaussList);
		}
		DisplayMixtureOfGaussians(mixtures);
		return mixtures;
	}

	private static void DisplayMixtureOfGaussians(List<List<MixGauss>> mixtures) {
		MixGauss MixGauss;
		for (int currClass = 1; currClass < noOfClasses; currClass++) {
			for (int attribute = 0; attribute < noOfAttributes; attribute++) {
				MixGauss = mixtures.get(attribute).get(currClass);
				for (int gaussianNum = 0; gaussianNum < MixGauss.getMixture().size(); gaussianNum++) {
					Gaussian gaussian1 = MixGauss.getMixture().get(gaussianNum);
					System.out.printf("Class %d, attribute %d, Gaussian = %d, mean = %.2f, std = %.2f\n", currClass,
							attribute, gaussianNum, gaussian1.getMean(), gaussian1.getStd());
				}
			}
		}
	}

	private static double detWeight(List<Double> probdist, List<List<Double>> probij) {
		double weight;
		double num = 0, den = 0;
		for (int j = 0; j < probdist.size(); j++) {
			num = num + probdist.get(j);

			for (int k = 0; k < probij.size(); k++) {
				for (int l = 0; l < probij.get(k).size(); l++) {
					den = den + probij.get(k).get(l);
				}
			}
		}
		weight = num / den;
		return weight;
	}

	private static void mStep(MixGauss MixGauss, List<Double> listOfValues) {
		for (int i = 0; i < noOfGaussians; i++) {
			Gaussian gaussian = MixGauss.getMixture().get(i);
			List<Double> probDistribution = MixGauss.getProbij().get(i);
			double mean, std, weight;
			mean = getMean(listOfValues, probDistribution);
			std = getStd(listOfValues, mean, probDistribution);
			if (std < 0.01)
				std = 0.01;
			weight = detWeight(probDistribution, MixGauss.getProbij());
			gaussian.setStd(std);
			gaussian.setMean(mean);
			MixGauss.getWeights().set(i, weight);
		}
	}
	private static void EM(MixGauss MixGauss, List<Double> listOfValues) {
		if (listOfValues.size() == 0)
			return;
		for (int i = 0; i < 50; i++) {
			MixGauss.setProbij(eMS(MixGauss, listOfValues));
			mStep(MixGauss, listOfValues);
		}
	}
	private static double getMean(List<Double> listOfValues, List<Double> probdist) {
		double mean;
		double num = 0, den = 0;
		for (int j = 0; j < listOfValues.size(); j++) {
			num = num + probdist.get(j) * listOfValues.get(j);
			den = den + probdist.get(j);
		}
		mean = num / den;
		return mean;
	}

	private static double getStd(List<Double> listOfValues, double mean, List<Double> probdist) {
		double std;
		double num = 0, den = 0;
		for (int j = 0; j < listOfValues.size(); j++) {
			num = num + probdist.get(j) * Math.pow((listOfValues.get(j) - mean), 2);
			den = den + probdist.get(j);
		}
		std = Math.sqrt(num / den);
		if (std < 0.01)
			std = 0.01;
		return std;
	}

	private static List<List<Double>> eMS(MixGauss MixGauss, List<Double> listOfValues) {
		List<List<Double>> probij = new ArrayList<List<Double>>();
		for (int i = 0; i < noOfGaussians; i++) {
			List<Double> probDistribution = new ArrayList<Double>();
			for (int j = 0; j < listOfValues.size(); j++) {
				Gaussian gaussian = MixGauss.getMixture().get(i);
				double value = listOfValues.get(j);
				double weight = MixGauss.getWeights().get(i);
				double prob = (getClassValuefromGaussProb(value, gaussian) * weight)
						/ getValuefromMixProb(value, MixGauss);
				probDistribution.add(prob);
			}
			probij.add(probDistribution);
		}
		return probij;
	}

	private static double getValuefromMixProb(double value, MixGauss MixGauss) {
		double valGivenClassProb = 0;
		for (int i = 0; i < noOfGaussians; i++) {
			Gaussian gaussian1 = MixGauss.getMixture().get(i);
			// double weight = MixGauss.getWeights().get(i);
			valGivenClassProb = valGivenClassProb + getClassValuefromGaussProb(value, gaussian1);
		}
		return valGivenClassProb;
	}

	private static double getClassValuefromGaussProb(double value, Gaussian gaussiana) {

		double mean = gaussiana.getMean();
		double std = gaussiana.getStd();
		double valGivenClassProb;
		if (std > 0)
			valGivenClassProb = (1 / (std * Math.sqrt(2 * Math.PI)))
					* (Math.pow(Math.E, -(Math.pow(value - mean, 2) / (2 * std * std))));
		else
			valGivenClassProb = 0.0;
		return valGivenClassProb;
	}

	private static MixGauss mixGaussInit(List<Double> listOfValues) {
		double min, max;
		if (listOfValues.size() == 0) {
			min = 0;
			max = 0;
		} else {
			min = getMinList(listOfValues);
			max = getMaxList(listOfValues);
		}
		double g = (max - min) / noOfGaussians;
		MixGauss MixGauss = new MixGauss();
		List<Double> weights = new ArrayList<Double>();
		List<Gaussian> mixture = new ArrayList<Gaussian>();

		for (int k = 0; k < noOfGaussians; k++) {
			Gaussian gaussian1 = new Gaussian();
			gaussian1.setMean(min + (k * g) + (g / 2));
			gaussian1.setStd(1);

			mixture.add(gaussian1);

			weights.add((double) 1 / noOfGaussians);
		}
		MixGauss.setMixture(mixture);
		MixGauss.setWeights(weights);

		return MixGauss;
	}

	private static double getMinList(List<Double> listOfValues) {
		double min = listOfValues.get(0);
		for (int i = 0; i < listOfValues.size(); i++) {
			if (listOfValues.get(i) < min)
				min = listOfValues.get(i);
		}
		return min;
	}

	private static double getMaxList(List<Double> listOfValues) {
		double max = listOfValues.get(0);
		for (int i = 0; i < listOfValues.size(); i++) {
			if (listOfValues.get(i) > max)
				max = listOfValues.get(i);
		}
		return max;
	}

	private static List<List<Gaussian>> trainingGaussians(List<List<Double>> data1) {
		List<List<Gaussian>> gaussians = new ArrayList<List<Gaussian>>();
		List<List<List<Double>>> data = new ArrayList<List<List<Double>>>();
		List<Integer> classDistributionVar = generateClassDist(data1);
		classProbDistFn(classDistributionVar, data1.size());

		for (int i = 0; i < noOfAttributes; i++) {
			List<List<Double>> classList = new ArrayList<List<Double>>();
			for (int j = 0; j < noOfClasses; j++) {
				List<Double> listOfVals = new ArrayList<Double>();
				classList.add(listOfVals);
			}
			data.add(classList);
		}
		for (int i = 0; i < data1.size(); i++) {
			List<Double> patternVar = getPattern(data1.get(i));
			int currClass = getClass(data1.get(i));
			for (int attribute = 0; attribute < noOfAttributes; attribute++) {
				double value = patternVar.get(attribute);
				data.get(attribute).get(currClass).add(value);
			}
		}

		for (int attribute = 0; attribute < noOfAttributes; attribute++) {
			List<Gaussian> gaussiansForAttribute = new ArrayList<Gaussian>();
			for (int currClass = 0; currClass < noOfClasses; currClass++) {
				List<Double> values = new ArrayList<Double>();
				values = data.get(attribute).get(currClass);
				double mean = getMean(data.get(attribute).get(currClass));
				double std = getStd(mean, values);
				Gaussian gaussian1 = new Gaussian();
				gaussian1.setStd(std);
				gaussian1.setMean(mean);
				gaussiansForAttribute.add(gaussian1);
			}
			gaussians.add(gaussiansForAttribute);
		}

		displayGaussians(gaussians);
		return gaussians;
	}

	private static double getMean(List<Double> list) {
		double sum = 0;
		double mean;
		for (int i = 0; i < list.size(); i++) {
			sum += list.get(i);
		}
		if (list.size() != 0)
			mean = sum / list.size();
		else
			mean = 0;
		return mean;
	}

	private static double getStd(double mean, List<Double> list) {
		double var = 0;
		double std;
		for (int i = 0; i < list.size(); i++) {
			var += Math.pow((list.get(i) - mean), 2);
		}
		if (list.size() >= 2)
			var = var / (list.size() - 1);
		else
			var = 0;
		std = Math.sqrt(var);
		if (std < 0.01)
			std = 0.01;
		return std;
	}

	private static void displayGaussians(List<List<Gaussian>> gaussians) {
		Gaussian gaussian;
		for (int currClass = 1; currClass < noOfClasses; currClass++) {
			for (int attribute = 0; attribute < noOfAttributes; attribute++) {
				gaussian = gaussians.get(attribute).get(currClass);
				System.out.printf("Class %d, attribute %d, mean = %.2f, std = %.2f\n", currClass, attribute,
						gaussian.getMean(), gaussian.getStd());
			}
		}

	}

	private static void classificationPhase(List<List<Double>> data1) {
		int exampleId = 0;
		double accuracy;
		double accuracySum = 0.0;
		int predictedClass, trueClass;
		double probability;

		for (int i = 0; i < data1.size(); i++) {
			List<Double> patternVar = getPattern(data1.get(i));
			trueClass = getClass(data1.get(i));
			List<Double> classGivenPatternProb = null;
			if (ClassificationMethod.equals(HISTOGRAMS)) {
				classGivenPatternProb = getProbDistrOfClassGivenPatternFrmH(histograms, data1, patternVar);
			} else if (ClassificationMethod.equals(GAUSSIANS)) {
				classGivenPatternProb = getClassGivenPatternFrmGaussProb(gaussians, data1, patternVar);
			} else if (ClassificationMethod.equals(MIXTURES)) {
				classGivenPatternProb = getClassGivenPatternFrmMixtureProb(mixtures, data1, patternVar);
			}
			predictedClass = classPrediction(classGivenPatternProb);
			probability = classGivenPatternProb.get(predictedClass);
			accuracy = findAccuracy(predictedClass, trueClass, classGivenPatternProb);
			accuracySum = accuracySum + accuracy;
			System.out.printf("ID=%5d, predicted=%3d, probability = %.4f, true=%3d, accuracy=%4.2f\n", exampleId,
					predictedClass, probability, trueClass, accuracy);
			exampleId++;
		}
		System.out.printf("classification accuracy=%6.4f\n", accuracySum / data1.size());

	}

	private static double findAccuracy(int predictedClass, int trueClass, List<Double> dist) {
		int numOfTies = 0;
		if ((double) dist.get(predictedClass) != (double) dist.get(trueClass))
			return 0.0;
		else {
			numOfTies = getNoOfTies(dist);
			if (numOfTies > 0)
				return (1.0 / (double) (numOfTies + 1));
			else
				return 1.0;
		}
	}

	private static List<Double> getClassGivenPatternFrmMixtureProb(List<List<MixGauss>> mixtures,
			List<List<Double>> data1, List<Double> patternVar) {
		List<Double> classGivenPatternProb = new ArrayList<Double>();

		for (int i = 0; i < noOfClasses; i++) {
			classGivenPatternProb.add(getClassGivenPatternFrmMixProb(i, patternVar, mixtures));
		}
		return classGivenPatternProb;
	}

	private static Double getClassGivenPatternFrmMixProb(int currClass, List<Double> patternVar,
			List<List<MixGauss>> mixtures) {

		double patternGivenClassProb = getPatternfromMixProb(patternVar, currClass, mixtures);
		double classProb = probDistOfClass.get(currClass);
		double patternProb = 0;
		double classGivenPatternProb;
		for (int i = 0; i < noOfClasses; i++) {
			patternProb = patternProb + getPatternfromMixProb(patternVar, i, mixtures) * probDistOfClass.get(i);
		}

		if (patternProb > 0.0)
			classGivenPatternProb = patternGivenClassProb * classProb / patternProb;
		else
			classGivenPatternProb = 0.0;
		return classGivenPatternProb;
	}

	private static double getPatternfromMixProb(List<Double> patternVar, int currClass, List<List<MixGauss>> mixtures) {
		double patternGivenClassProb = 1;

		for (int attribute = 0; attribute < patternVar.size(); attribute++) {
			double value = patternVar.get(attribute);
			double valGivenClassProb = getValuefromMixProb(value, mixtures.get(attribute).get(currClass));
			patternGivenClassProb = patternGivenClassProb * valGivenClassProb;
		}

		return patternGivenClassProb;
	}

	private static int getNoOfTies(List<Double> dist) {
		double predictedClassProb = dist.get(classPrediction(dist));
		int numOfTies = -1;
		for (int i = 0; i < dist.size(); i++) {
			if (dist.get(i) == predictedClassProb)
				numOfTies++;
		}
		return numOfTies;
	}

	private static int classPrediction(List<Double> dist) {
		double maxProb = -1;
		Random rand = new Random();
		List<Integer> maxProbClass = new ArrayList<Integer>();
		int predictedClass = -1;
		for (int i = 0; i < dist.size(); i++) {
			if (dist.get(i) > maxProb) {
				maxProb = dist.get(i);
			}
		}
		for (int i = 0; i < dist.size(); i++) {
			if (dist.get(i) == maxProb) {
				maxProbClass.add(i);
			}
		}
		predictedClass = maxProbClass.get(rand.nextInt(maxProbClass.size()));
		return predictedClass;
	}

	private static List<Double> getClassGivenPatternFrmGaussProb(List<List<Gaussian>> gaussians,
			List<List<Double>> data1, List<Double> patternVar) {
		List<Double> classGivenPatternProb = new ArrayList<Double>();

		for (int i = 0; i < noOfClasses; i++) {
			classGivenPatternProb.add(getClassGivenPatternFrmGaussProb(i, patternVar, gaussians));
		}
		return classGivenPatternProb;
	}

	private static Double getClassGivenPatternFrmGaussProb(int currClass, List<Double> patternVar,
			List<List<Gaussian>> gaussians) {
		double patternGivenClassProb = getPatternGivenClassProb(patternVar, currClass, gaussians);
		double classProb = probDistOfClass.get(currClass);
		double patternProb = 0;
		double classGivenPatternProb;
		for (int i = 0; i < noOfClasses; i++) {
			patternProb = patternProb + getPatternGivenClassProb(patternVar, i, gaussians) * probDistOfClass.get(i);
		}

		if (patternProb > 0.0)
			classGivenPatternProb = patternGivenClassProb * classProb / patternProb;
		else
			classGivenPatternProb = 0.0;
		return classGivenPatternProb;
	}

	private static double getPatternGivenClassProb(List<Double> patternVar, int currClass,
			List<List<Gaussian>> gaussians) {
		double patternGivenClassProb = 1;
		for (int attribute = 0; attribute < patternVar.size(); attribute++) {
			double value = patternVar.get(attribute);
			double valGivenClassProb = getProbOfValueGivenClassFrmG(attribute, value, currClass, gaussians);
			patternGivenClassProb = patternGivenClassProb * valGivenClassProb;
		}
		return patternGivenClassProb;
	}

	private static double getProbOfValueGivenClassFrmG(int attribute, double value, int currClass,
			List<List<Gaussian>> gaussians) {
		double mean = gaussians.get(attribute).get(currClass).getMean();
		double std = gaussians.get(attribute).get(currClass).getStd();
		double valGivenClassProb;
		if (std > 0)
			valGivenClassProb = (1 / (std * Math.sqrt(2 * Math.PI)))
					* (Math.pow(Math.E, -(Math.pow(value - mean, 2) / (2 * std * std))));

		else
			valGivenClassProb = 0.0;
		return valGivenClassProb;
	}

	private static List<Double> getProbDistrOfClassGivenPatternFrmH(List<Hist> histograms, List<List<Double>> data1,
			List<Double> patternVar) {
		List<Double> classGivenPatternProb = new ArrayList<Double>();

		for (int i = 0; i < noOfClasses; i++) {
			classGivenPatternProb.add(getProbOfClassGivenPatternFrmH(i, patternVar, histograms));
		}
		return classGivenPatternProb;
	}

	private static Double getProbOfClassGivenPatternFrmH(int currClass, List<Double> patternVar,
			List<Hist> histograms) {
		double patternGivenClassProb = patternGivenClassfromHistProb(patternVar, currClass, histograms);
		double classProb = probDistOfClass.get(currClass);
		double patternProb = 0;
		double classGivenPatternProb;
		for (int i = 0; i < noOfClasses; i++) {
			patternProb = patternProb
					+ patternGivenClassfromHistProb(patternVar, i, histograms) * probDistOfClass.get(i);
		}

		if (patternProb > 0.0)
			classGivenPatternProb = patternGivenClassProb * classProb / patternProb;
		else
			classGivenPatternProb = 0.0;
		return classGivenPatternProb;
	}

	private static double patternGivenClassfromHistProb(List<Double> patternVar, int currClass, List<Hist> histograms) {
		double patternGivenClassProb = 1;
		for (int attribute = 0; attribute < patternVar.size(); attribute++) {
			double value = patternVar.get(attribute);
			int bin = BinNum(value, minList.get(attribute), maxList.get(attribute));
			double probOfBinGivenClass = getBinGivenClassfromHistProb(attribute, bin, currClass, histograms);
			patternGivenClassProb = patternGivenClassProb * probOfBinGivenClass;
		}
		return patternGivenClassProb;
	}

	private static double getBinGivenClassfromHistProb(int attribute, int bin, int currClass, List<Hist> histograms) {
		return histograms.get(attribute).getBins().get(bin).getDistribution().get(currClass);
	}

	private static List<Hist> trainingHistograms(List<List<Double>> data1) {
		List<Hist> histograms = generateHist(data1);
		displayHistogram(histograms);
		return histograms;
	}

	private static void displayHistogram(List<Hist> hist) {
		double probOfBinGivenClass;
		for (int currClass = 1; currClass < noOfClasses; currClass++) {
			for (int attribute = 0; attribute < noOfAttributes; attribute++) {
				for (int bin = 0; bin < noOfBins; bin++) {
					probOfBinGivenClass = getBinGivenClassfromHistProb(attribute, bin, currClass, hist);
					System.out.printf("Class %d, attribute %d, bin %d, P(bin | class) = %.2f\n", currClass, attribute,
							bin, probOfBinGivenClass);
				}
			}
		}
	}

	private static List<Hist> generateHist(List<List<Double>> data1) {
		List<Integer> classDistributionVar = generateClassDist(data1);
		classProbDistFn(classDistributionVar, data1.size());
		List<Hist> histograms = new ArrayList<Hist>();
		minList = new ArrayList<Double>();
		maxList = new ArrayList<Double>();
		for (int attribute = 0; attribute < noOfAttributes; attribute++) {
			double min = getMinimum(data1, attribute);
			double max = getMaximum(data1, attribute);
			minList.add(min);
			maxList.add(max);
			Hist Hist = new Hist();
			List<Bintype> bins = Hist.getBins();
			for (int i = 0; i < noOfBins; i++) {
				Bintype bin = new Bintype();
				initialize(bin.getDistribution(), noOfClasses, 0);
				bins.add(bin);
			}
			for (int i = 0; i < data1.size(); i++) {
				List<Double> patternVar = getPattern(data1.get(i));
				int currClass = getClass(data1.get(i));
				double value = patternVar.get(attribute);
				int bin = BinNum(value, min, max);
				List<Double> classDistributionForBin = bins.get(bin).getDistribution();
				classDistributionForBin.set(currClass, classDistributionForBin.get(currClass) + 1);
			}
			for (int i = 0; i < noOfBins; i++) {
				List<Double> classDistributionForBin = bins.get(i).getDistribution();
				for (int currClass = 0; currClass < noOfClasses; currClass++) {
					double countOfMyClass = classDistributionForBin.get(currClass);
					int totalCountOfMyClass = classDistributionVar.get(currClass);
					double probOfBinGivenClass;
					if (totalCountOfMyClass != 0.0)
						probOfBinGivenClass = countOfMyClass / totalCountOfMyClass;
					else
						probOfBinGivenClass = 0.0;
					classDistributionForBin.set(currClass, probOfBinGivenClass);
				}
			}
			Hist.setBins(bins);
			histograms.add(Hist);
		}
		return histograms;
	}

	private static void classProbDistFn(List<Integer> classDistributionVar, int totalNumOfExamples) {
		probDistOfClass = new ArrayList<Double>();
		for (int i = 0; i < classDistributionVar.size(); i++)
			probDistOfClass.add((double) classDistributionVar.get(i) / totalNumOfExamples);
	}

	private static double getMinimum(List<List<Double>> data1, int attribute) {
		double temp;
		double min = data1.get(0).get(attribute);
		;
		for (int i = 0; i < data1.size(); i++) {
			temp = data1.get(i).get(attribute);
			if (temp < min) {
				min = temp;
			}
		}
		return min;
	}

	private static double getMaximum(List<List<Double>> data1, int attribute) {
		double temp;
		double max = data1.get(0).get(attribute);

		for (int i = 0; i < data1.size(); i++) {
			temp = data1.get(i).get(attribute);
			if (temp > max) {
				max = temp;
			}
		}
		return max;
	}

	private static List<Integer> generateClassDist(List<List<Double>> data1) {
		List<Integer> classDistributionVar = new ArrayList<Integer>();

		initialize(classDistributionVar, noOfClasses, 0);

		for (int i = 0; i < data1.size(); i++) {
			int currClass = getClass(data1.get(i));
			classDistributionVar.set(currClass, classDistributionVar.get(currClass) + 1);
		}
		return classDistributionVar;
	}

	private static int BinNum(double value, double min, double max) {
		int bin;
		double binSize = (max - min) / (noOfBins - 3);
		if (value < min)
			bin = 0;
		else if (value >= max) {
			bin = noOfBins - 1;
		} else {
			bin = (int) Math.floor((value - min) / binSize);
		}
		return bin;
	}

	private static void initialize(List<Integer> arrayList, int size, int initializationValue) {
		for (int i = 0; i < size; i++)
			arrayList.add(i, initializationValue);
	}

	private static void initialize(List<Double> arrayList, int size, double initializationValue) {
		for (int i = 0; i < size; i++)
			arrayList.add(i, initializationValue);
	}

	private static int getNumberOfClasses(List<List<Double>> data1) {
		int maxClass = -1;
		for (int i = 0; i < data1.size(); i++) {
			if (getClass(data1.get(i)) > maxClass)
				maxClass = getClass(data1.get(i));
		}
		return maxClass + 1;
	}

	private static int getClass(List<Double> data3) {
		return data3.get(data3.size() - 1).intValue();
	}

	private static List<Double> getPattern(List<Double> data4) {
		return data4.subList(0, data4.size() - 1);
	}

	private static List<List<Double>> fileReader(String test) {
		List<List<Double>> data1 = new ArrayList<List<Double>>();
		String fileName = test;
		String line = null;
		String[] values = null;
		List<Double> dummy = null;

		try {
			FileReader fileReader = new FileReader(fileName);

			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				dummy = new ArrayList<Double>();
				line = line.trim();
				values = line.split("\\s+");
				for (int i = 0; i < values.length; i++) {
					dummy.add(Double.parseDouble(values[i]));
				}
				data1.add(dummy);
			}
			noOfAttributes = dummy.size() - 1;
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Cannot open specified file '" + fileName + "'");
			System.exit(1);
		} catch (IOException ex) {
			System.out.println("Cannot read specified file '" + fileName + "'");
			System.exit(1);
		}
		return data1;
	}

	public static void main(String[] args) {
		InputCheck(args);
		List<List<Double>> trainingSet = fileReader(trainingData);
		List<List<Double>> testSet = fileReader(testData);
		noOfClasses = getNumberOfClasses(trainingSet);
		if (ClassificationMethod.equals(HISTOGRAMS)) {
			histograms = trainingHistograms(trainingSet);
			classificationPhase(testSet);
		} else if (ClassificationMethod.equals(GAUSSIANS)) {
			gaussians = trainingGaussians(trainingSet);
			classificationPhase(testSet);
		} else if (ClassificationMethod.equals(MIXTURES)) {
			mixtures = trainingMixtures(trainingSet);
			classificationPhase(testSet);
		} else {
			System.out.println("Please enter valid method in the input!");
			System.exit(1);
		}
	}

	private static void InputCheck(String[] args) {
		if (args.length != 3 && args.length != 4) {
			System.out.println(
					"Please enter arguments in proper format naive_bayes <training_file> <test_file> <method> <number>");
			System.exit(1);
		}
		trainingData = args[0];
		testData = args[1];
		ClassificationMethod = args[2];
		if (args.length == 4) {
			if (ClassificationMethod.equals(HISTOGRAMS))
				noOfBins = Integer.parseInt(args[3]);
			else if (ClassificationMethod.equals(MIXTURES))
				noOfGaussians = Integer.parseInt(args[3]);
		}
	}

}