//RAJARAMAN GOVINDASAMY
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

public class gaussian_1d {
	
		public static void main(String [] args){
		
			//ArrayList to hold Input, mean, variance
		Map<Integer, ArrayList<Float>> raw = new HashMap<Integer, ArrayList<Float>>();
		Map<Integer, ArrayList<Float>> means = new HashMap<Integer, ArrayList<Float>>();
		Map<Integer, ArrayList<Float>> variance = new HashMap<Integer, ArrayList<Float>>();
		int [] size = new int[20];
		System.out.println("Enter file name\n");
		Scanner inputfile = new Scanner(System.in);
		String fileName =inputfile.nextLine();
		int i=0,j=0;int stack=0;
		for(i=0; i<20; i++)
			size[i]=0;
		
	//MEAN & VARIANCE
		try{
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			for(String line; (line=reader.readLine())!= null;){
				//Convert string to int
				String[] records = line.split(" ");
				List<String> ListRecords = Arrays.asList(records);
				ArrayList<Float> RecordAsInt= ModifyArrayInput(ListRecords);
				
				int temp=0, key;
				key = Math.round(RecordAsInt.remove(RecordAsInt.size()-1));
				size[key] = size[key]+1;
				if(stack==0) stack = RecordAsInt.size();
				
			if(raw.get(key) == null)
				raw.put(key, RecordAsInt);
			
			else
			{
				ArrayList<Float> tempArr = new ArrayList<Float>();
				for(j=0;j<stack;j++){
					tempArr.add(raw.get(key).get(j) + RecordAsInt.get(j));
					
				}
				raw.put(key, tempArr);
			}
		}
		
		//Calculate Mean
		Iterator<Map.Entry<Integer, ArrayList<Float>>> it = raw.entrySet().iterator();
		while(it.hasNext()){
			Entry<Integer, ArrayList<Float>> calMean = it.next();
			ArrayList<Float> meanList = new ArrayList<Float>();
			int tempKey = calMean.getKey();
			for(int k=0;k< stack;k++){
				float Mean = (float)raw.get(tempKey).get(k) / size[tempKey];
				meanList.add(Mean);
			}
			means.put(tempKey, meanList);
		}
		
		
		
		//Calculate variance
				Iterator<Map.Entry<Integer, ArrayList<Float>>> it1 = raw.entrySet().iterator();
				while(it1.hasNext()){
					Entry<Integer, ArrayList<Float>> CalVar = it1.next();
					float var1;
					ArrayList<Float> VarList = new ArrayList<Float>();
					int tempKey = CalVar.getKey();
					for(int k=0;k< stack;k++){
						float var2 = (float)raw.get(tempKey).get(k) / size[tempKey];
						var1 = ((CalVar.getKey() - var2)*(CalVar.getKey() - var2)/(size[tempKey]-1)); 
						VarList.add(var1);
					}
					variance.put(tempKey, VarList);
				}
				
		Iterator<Map.Entry<Integer, ArrayList<Float>>> ii = means.entrySet().iterator();
		Iterator<Map.Entry<Integer, ArrayList<Float>>> ij = variance.entrySet().iterator();		
		while(ii.hasNext()){
			Entry<Integer, ArrayList<Float>> mean = ii.next();
			Entry<Integer, ArrayList<Float>> vare = ij.next();
			int mtKey = mean.getKey();
			for(int k=0;k<stack;k++){
				System.out.printf("Class %d, dimension %d, mean = %.2f, variance = %.2f\n", mtKey, k, mean.getValue().get(k),vare.getValue().get(k));
				}
			
		}
	}
		catch(FileNotFoundException ex){
			ex.printStackTrace();
		}
		catch(IOException ei){
			ei.printStackTrace();
		}
		catch(NullPointerException en){
			en.printStackTrace();
		}
		
	}
		public static ArrayList<Float> ModifyArrayInput(List<String> inputData) {
	        ArrayList<Float> result = new ArrayList<Float>();
	        for(String stringValue : inputData) {
	            try {
	                result.add(Float.parseFloat(stringValue));
	            	} catch(NumberFormatException nfe) {
	            } 
	        }       
	        return result;
	    }
	
	
}
