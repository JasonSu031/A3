/**
 * This class enables the calculation and sorting of information gain values
 * 
 * @author Mehrdad Sabetzadeh, University of Ottawa
 * @author Guy-Vincent Jourdan, University of Ottawa
 *
 */
public class InformationGainCalculator {

	/**
	 * @param dataset is the dataset whose attributes we want to analyze and sort
	 *                according to information gain
	 * @return an array of GainInfoItem instances sorted in descending order of gain
	 *         value
	 */
	public static GainInfoItem[] calculateAndSortInformationGains(VirtualDataSet dataset) {
		GainInfoItem info[] = new GainInfoItem[dataset.numAttributes-1];
		double gain = 0;
		for(int i = 0; i<dataset.attributes.length-1; i++){ 
			Attribute a = dataset.attributes[i];
			double maxGain = 0;
			if(a.getType().equals(AttributeType.NOMINAL)){
				gain = EntropyEvaluator.evaluate(dataset.partitionByNominallAttribute(i));
			}else{
				String [] numbers = a.getValues();
				for(int j =0; j<numbers.length; j++){
					System.out.print(dataset.getValueAt(j,i)+" ");
					gain = EntropyEvaluator.evaluate(dataset.partitionByNumericAttribute(i,j));
					if(maxGain<gain)maxGain = gain;
				}
				gain = maxGain;
			}
			//info[i] = new GainInfoItem(a.getName(), a.getType() , gain, "80"); 
		}
		//GainInfoItem.reverseSort(info);
		//return info;
		return null;
	}

	public static void main(String[] args) throws Exception {

		StudentInfo.display();

		if (args == null || args.length == 0) {
			System.out.println("Expected a file name as argument!");
			System.out.println("Usage: java InformationGainCalculator <file name>");
			return;
		}

		String strFilename = args[0];

		ActualDataSet actual = new ActualDataSet(new CSVReader(strFilename));

		VirtualDataSet virtual = actual.toVirtual();

		GainInfoItem[] items = calculateAndSortInformationGains(virtual);

		//Print out the output
		// System.out.println(
		// 		" *** items represent (attribute name, information gain) in descending order of gain value ***");
		// System.out.println();

		// for (int i = 0; i < items.length; i++) {
		// 	System.out.println(items[i]);
		// }
	}
}
