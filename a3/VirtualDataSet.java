// You are allowed to use LinkedList or other Collection classes in A2 and A3
import java.util.LinkedList;

/**
 * This class is used for representing a virtual dataset, that is, a dataset
 * that is a view over an actual dataset. A virtual dataset has no data matrix
 * of its own.
 * 
 * @author Mehrdad Sabetzadeh, University of Ottawa
 * @author Guy-Vincent Jourdan, University of Ottawa
 *
 */
public class VirtualDataSet extends DataSet {

	/**
	 * reference to the source dataset (instance of ActualDataSet)
	 */
	private ActualDataSet source;

	/**
	 * array of integers mapping the rows of this virtual dataset to the rows of its
	 * source (actual) dataset
	 */
	private int[] map;

	/**
	 * Constructor for VirtualDataSet. There are two important considerations here:
	 * (1) Make sure that you keep COPIES of the "rows" and "attributes" passed as
	 * formal parameters. Do not, for example, say this.map = rows. Instead, create
	 * a copy of rows before assigning that copy to this.map. (2) Prune the value
	 * sets of the attributes. Since a virtual dataset is only a subset of an actual
	 * dataset, it is likely that some or all of its attributes may have smaller
	 * value sets.
	 * 
	 * @param source     is the source dataset (always an instance of ActualDataSet)
	 * @param rows       is the set of rows from the source dataset that belong to
	 *                   this virtual dataset
	 * @param attributes is the set of attributes belonging to this virtual dataset.
	 *                   IMPORTANT: you need to recalculate the unique value sets
	 *                   for these attributes according to the rows. Why? Because
	 *                   this virtual set is only a subset of the source dataset and
	 *                   its attributes potentially have fewer unique values.
	 */

	public VirtualDataSet(ActualDataSet source, int[] rows, Attribute[] attributes) { 
		this.source = source;																		//Sets the reference of VirtualDataSet's source to the ActualDataSet 
		this.numAttributes = attributes.length;														//Sets the variable numAttributes to be the number of attributes being passed in
		this.numRows = rows.length;																	//Sets the variable numRows to be the number of indexes being passed in the parameter
		this.attributes = attributes;																//Sets the variable attributes to reference the attributes array being passed in the parameters
		int[] copyOfRows = new int[rows.length];													//Creates a new copy of int array rows and stores it within variable copyOfRows
		for(int i =0; i<copyOfRows.length; i++){
			copyOfRows[i] = rows[i];
		}
		map = copyOfRows;																			//Sets the variable map to reference rows

		Attribute[] copyOfAttributes = new Attribute[attributes.length];							//Creates a deep copy of attributes array and stores it within variable copyOfAttributes
		for(int i =0; i<attributes.length; i++){
			copyOfAttributes[i] = attributes[i].clone();
		}
		for(int j = 0; j<attributes.length; j++){													//Loops through each attribute individually
			String []newValues = new String[rows.length];											//Declares a new String array called newValues
			for(int i =0; i<rows.length; i++){														//Loops through the rows array and stores the attributes corresponding to the indexes in rows array into String array newValues
				newValues[i] = source.getValueAt(rows[i],copyOfAttributes[j].getAbsoluteIndex());	
			}
			copyOfAttributes[j].replaceValues(newValues);											//Replaces the values stored within attributes array copyOfAttributes with newValues				
			copyOfAttributes[j].replaceValues(getUniqueAttributeValues(copyOfAttributes[j].getAbsoluteIndex()));	//Replaces the values of attribute copyOfAttribute with the non-duplicated String array newValues
		}

		this.attributes = copyOfAttributes;															//Sets the reference of attributes to the newly edited attributes array copyOfAttributes

	}

	/**
	 * String representation of the virtual dataset.
	 */
	public String toString() {
		// WRITE YOUR CODE HERE!
		StringBuffer buffer = new StringBuffer();
		buffer.append("Virtual dataset with " +attributes.length+" attribute(s) and "+map.length+" row(s)"+ System.lineSeparator());
		buffer.append("Dataset is a view over weather-nominal.csv"+System.lineSeparator());
		buffer.append("Row indices in this dataset (w.r.t its source dataset) [");

		for(int i = 0; i < map.length; i++){
			buffer.append(map[i]);
			if(i!=map.length-1)
				buffer.append(",");
		}
		buffer.append("]").append(System.lineSeparator());
		for(int i =0; i<attributes.length; i++){
			buffer.append(attributes[i]);
			buffer.append(System.lineSeparator());
		}
		return buffer.toString();
	}

	/**
	 * Implementation of DataSet's getValueAt abstract method for virtual datasets.
	 * Hint: You need to call source.getValueAt(...). What you need to figure out is
	 * with what parameter values that method needs to be called.
	 */
	public String getValueAt(int row, int attributeIndex) {
		return source.getValueAt(map[row],attributeIndex);											//returns the value at located in matrix at row map[row] and column attributeIndex
	}

	/**
	 * @return reference to source dataset
	 */
	public ActualDataSet getSourceDataSet() {
		// WRITE YOUR CODE HERE!
		return source;																				//returns the ActualDataSet
	}


	/**
	 * This method splits the virtual dataset over a nominal attribute. This process
	 * has been discussed and exemplified in detail in the assignment description.
	 * 
	 * @param attributeIndex is the index of the nominal attribute over which we
	 *                       want to split.
	 * @return a set (array) of partitions resulting from the split. The partitions
	 *         will no longer contain the attribute over which we performed the
	 *         split.
	 */
	public VirtualDataSet[] partitionByNominallAttribute(int attributeIndex) {
		// WRITE YOUR CODE HERE!
		String [] category = source.getUniqueAttributeValues(attributeIndex); 						//find unique element to split 
		VirtualDataSet [] partitions = new VirtualDataSet[category.length ]; 						// determine number of split path 
		Attribute [] subset = new Attribute[source.numAttributes-1]; 

		for(int i =0; i<attributeIndex; i++){
            subset[i] = source.getAttribute(i); 													// create a subset to eliminate the split attribute
        } 
        for(int i = attributeIndex+1; i<source.numAttributes; i++){
            subset[i-1] = source.getAttribute(i);
        }



		for(int i = 0; i < partitions.length;i++){ 
			LinkedList<Integer> rows = new LinkedList<Integer> (); 									// using linkedlist to do collection of split rows  
			int count = 0; 
			for(int j = 0; j < source.numRows; j++){ 
				if(category[i].equals(source.getValueAt(j, attributeIndex))){
					rows.add(j); 																	// add rows that correspond with the current attribute of spliting 
					count++; 																		// determine the size of partition rows array 
				}
			}
			int[] partitionRows = new int[count]; 
			for(int k = 0; k < count; k++){
				partitionRows[k] = rows.poll(); 													// transform from linkedlist to array 
			}

			partitions[i] = new VirtualDataSet(source,partitionRows,subset);  						// send partition to VirtualDataSet constructor 

		}
		return partitions;

		// for each partition we need 1. orginal rows index[] 2. unique attribute 
		// Where to split 
		// for each path, i have to know the original position of row (1 partition at time)
		// I need to know rest attribute value and record the unique (this need to be recaculate)
	}

	/**
	 * This method splits the virtual dataset over a given numeric attribute at a
	 * specific value from the value set of that attribute. This process has been
	 * discussed and exemplified in detail in the assignment description.
	 * 
	 * @param attributeIndex is the index of the numeric attribute over which we
	 *                       want to split.
	 * @param valueIndex     is the index of the value (in the value set of the
	 *                       attribute of interest) to use for splitting
	 * @return a pair of partitions (VirtualDataSet array of length two) resulting
	 *         from the two-way split. Note that the partitions will retain the
	 *         attribute over which we perform the split. This is in contrast to
	 *         splitting over a nominal, where the split attribute disappears from
	 *         the partitions.
	 */
	public VirtualDataSet[] partitionByNumericAttribute(int attributeIndex, int valueIndex) {
		// WRITE YOUR CODE HERE!

		 if(attributes[attributeIndex].getType()==AttributeType.NOMINAL){ 							// aviod passing through nominal value to current method
            return null; // 
        }
			VirtualDataSet [] partitions = new VirtualDataSet[2]; 									// creates two split path 

			int split = Integer.parseInt(source.getValueAt(valueIndex , attributeIndex));			// determine the middle number in order to split 
			
			LinkedList<Integer> rowsLess = new LinkedList<Integer> ();  							// using linkedlist to do collection of split rows that less than valueIndex value
			
			LinkedList<Integer> rowsMore = new LinkedList<Integer> ();								// using linkedlist to do collection of split rows that less than valueIndex value
			
			int countLess = 0;
			int countMore = 0; 

			String []arrayOfNums = attributes[attributeIndex].getValues();

			for(int j = 0; j < source.numRows; j++){ 
				
				if( Integer.parseInt(source.getValueAt(j,attributeIndex)) <= split){ 				// transform from string to integer in order to compare the number smaller than middle number
					
					rowsLess.add(j);																// add rows that correspond with the current attribute of spliting 
					countLess++;																	// determine the size of partition rows array 
				}else if(Integer.parseInt(source.getValueAt(j,attributeIndex)) > split){			// transform from string to integer in order to compare the number bigger than middle number
					
					rowsMore.add(j); 																// add rows that correspond with the current attribute of spliting 
					countMore++;																	// determine the size of partition rows array 
				}
			}

			int [] partitionRowsLess = new int [countLess]; 										// the collection of rows represents all number that smaller than middle number
			int [] partitionRowsMore = new int [countMore]; 										// the collection of rows represents all number that bigger than middle number
			for(int k = 0; k < countLess; k++){
				partitionRowsLess[k] = rowsLess.poll(); 											// transform from linkedlist to array 
			}
			for(int k = 0; k < countMore; k++){
				partitionRowsMore[k] = rowsMore.poll(); 											// transform from linkedlist to array 
			}
			
			partitions[0] = new VirtualDataSet(source,partitionRowsLess, attributes); 				// send partition to VirtualDataSet constructor 
			partitions[1] = new VirtualDataSet(source,partitionRowsMore, attributes); 
			


			return partitions;
		
	}


	public static void main(String[] args) throws Exception {

		StudentInfo.display();

		System.out.println("============================================");
		System.out.println("THE WEATHER-NOMINAL DATASET:");
		System.out.println();

		ActualDataSet figure5Actual = new ActualDataSet(new CSVReader("weather-nominal.csv"));

		System.out.println(figure5Actual);

		VirtualDataSet figure5Virtual = figure5Actual.toVirtual();

		System.out.println("JAVA IMPLEMENTATION OF THE SPLIT IN FIGURE 5:");
		System.out.println();

		VirtualDataSet[] figure5Partitions = figure5Virtual
				.partitionByNominallAttribute(figure5Virtual.getAttributeIndex("outlook"));

		for (int i = 0; i < figure5Partitions.length; i++)
			System.out.println("Partition " + i + ": " + figure5Partitions[i]);

		System.out.println("============================================");
		System.out.println("THE WEATHER-NUMERIC DATASET:");
		System.out.println();

		ActualDataSet figure9Actual = new ActualDataSet(new CSVReader("weather-numeric.csv"));

		System.out.println(figure9Actual);

		VirtualDataSet figure9Virtual = figure9Actual.toVirtual();

		// Now let's figure out what is the index for humidity in figure9Virtual and
		// what is the index for "80" in the value set of humidity!

		int indexForHumidity = figure9Virtual.getAttributeIndex("humidity");

		Attribute humidity = figure9Virtual.getAttribute(indexForHumidity);

		String[] values = humidity.getValues();

		int indexFor80 = -1;

		for (int i = 0; i < values.length; i++) {
			if (values[i].equals("80")) {
				indexFor80 = i;
				break;
			}
		}

		if (indexFor80 == -1) {
			System.out.println("Houston, we have a problem!");
			return;
		}

		VirtualDataSet[] figure9Partitions = figure9Virtual.partitionByNumericAttribute(indexForHumidity, indexFor80);

		System.out.println("JAVA IMPLEMENTATION OF THE SPLIT IN FIGURE 9:");
		System.out.println();

		for (int i = 0; i < figure9Partitions.length; i++)
			System.out.println("Partition " + i + ": " + figure9Partitions[i]);

	}
}