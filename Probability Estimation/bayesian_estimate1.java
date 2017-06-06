import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class bayesian_estimate1 
{
	public static void main(String args[]) throws IOException
	{	
		
		int counter=0;
		double [] x ={0.1,0.3,0.5,0.7,0.9};
		double [] y ={0.9,0.04,0.03,0.02,0.01};
		double [] prior = new double[5];
		double prior_of_a=0,prior_of_b=0,post_of_a=0,post_of_b;
		System.out.print("Enter String ");
		String name = null;
		int f=1;
		BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
		name=br.readLine();
		int len= name.length();
		for(int i=0;i<name.length();i++)
		{
			if(name.charAt(i)=='a')
			{
				counter++;
			}
		}
		if(counter==0)
		{
			System.out.print("p(m = 0.1 | data) = 0.9\np(m = 0.3 | data) = 0.04\np(m = 0.5 | data) = 0.03\np(m = 0.7 | data) = 0.02\np(m = 0.9 | data) = 0.01\np(c = 'a' | data) = 0.14");
		}
		for(int j=0;j<counter;j++)
		{
			for(int i=0;i<len-1;i++)
			{
				if(name.charAt(i)!='a'&& name.charAt(i)!='b'){
					f=0;
					break;
				}
			}

			if (f==0){
				System.out.print("\nPlease enter valid string\n");
			}
			else
			{
				for(int k=0;k<x.length;k++)
				{
					prior_of_a=prior_of_a+(x[k]*y[k]);
				}
				prior_of_b=1-prior_of_a;
				for(int i=0;i<x.length;i++)
				{
					prior[i]=(x[i]*y[i])/prior_of_a;
					String str = String.format("%.4f", prior[i]);
					System.out.println(str);
				}
			}
		}

	}
}