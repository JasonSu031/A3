/**
 * This class enables calculating (weighted-average) entropy values for a set of
 * datasets
 * 
 * @author Mehrdad Sabetzadeh, University of Ottawa
 * @author Guy-Vincent Jourdan, University of Ottawa
 *
 */
public class EntropyEvaluator {

	/**
	 * A static method that calculates the weighted-average entropy of a given set
	 * (array) of datasets. The assignment description provides a detailed
	 * explanation of this calculation. In particular, note that all logarithms are
	 * to base 2. For your convenience, we provide a log2 method. You can use this
	 * method wherever you need to take logarithms in this assignment.
	 * 
	 * @param partitions is the array of datasets to compute the entropy of
	 * @return Shannon's logarithmic entropy (to base 2) for the partitions
	 */
	public static double evaluate(DataSet[] partitions) {
		double totalRows = 0;
		double gain = 0;
		double sum =0;
		double entropyP = 0;
		double entropyD = 0;

		int lastIndex = (partitions[0].attributes.length-1);
		int classIndex = partitions[0].attributes[lastIndex].getAbsoluteIndex();

		String stateOne = partitions[0].getValueAt(0, classIndex);
		double stateOneNum = 0;

		for(int i =0; i<partitions.length; i++){
			int numRows = partitions[i].numRows;
			totalRows+=numRows;
			for(int j = 0; j<numRows; j++){
				String compareStateOne = partitions[i].getValueAt(j,classIndex);
				if(compareStateOne.equals(stateOne)){
					stateOneNum++;
				}
			}
		}
		double entropyDFraction = (stateOneNum/totalRows);
		entropyD = (-1)*entropyDFraction*log2(entropyDFraction)-(1-entropyDFraction)*log2(1-entropyDFraction);
		
		for(int i =0; i<partitions.length; i++){
			double numRows = partitions[i].numRows;
			if(numRows==0){
				entropyP = 0;
			}
			else{
				String firstState = partitions[i].getValueAt(0,classIndex);
				double numFirstState =1;
				for(int j =1; j<numRows; j++){
					if(firstState.equals(partitions[i].getValueAt(j, classIndex))){
						numFirstState++;
					}
				}
				double entropyPFraction = numFirstState/numRows;
				entropyP = (-1)*(entropyPFraction)*log2(entropyPFraction)-(1-entropyPFraction)*log2(1-entropyPFraction);	
				entropyP = entropyP*numRows/totalRows;
				if(numFirstState==numRows) entropyP=0;
			}
			sum+=entropyP;
		}
		gain = entropyD-sum;
		return gain;
	}
	
	/**
	 * Calculate base-2 logarithm for a given number
	 * 
	 * @param x is the number to take the logarithm of
	 * @return base-2 logarithm for x
	 */
	public static double log2(double x) {
		return (Math.log(x) / Math.log(2));
	}
}
