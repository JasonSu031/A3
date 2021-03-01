/**
 * This class is used for representing an actual dataset, that is, a dataset
 * that holds a data matrix
 * 
 * @author Mehrdad Sabetzadeh, University of Ottawa
 * @author Guy-Vincent Jourdan, University of Ottawa
 *
 */
public class ActualDataSet extends DataSet {
	/**
	 * The data matrix
	 */
	private String[][] matrix;

	/**
	 * The source identifier for the data. When the data source is a file, sourceId
	 * will be the name and location of the source file
	 */
	private String dataSourceId;

	/**
	 * Constructor for ActualDataSet. In addition to initializing dataSourceId,
	 * numAttributes, numRows and matrix, the constructor needs to create an array of
	 * attributes (instance of the Attribute class) and initialize the "attributes"
	 * instance variable of DataSet.
	 * 
	 * 
	 * @param reader is the DataReader instance to read data from.
	 */
	public ActualDataSet(DataReader reader) {
		// WRITE YOUR CODE HERE!
		numAttributes = reader.getNumberOfColumns();										//Sets the variable numAttributes to the number of columns taken from DataReader reader
		numRows = reader.getNumberOfDataRows();												//Sets the variable numRows to be the number of rows taken from DataReader reader
		matrix = reader.getData();															//Sets the 2D matrix to reference the matrix read from DataReader reader
		attributes = new Attribute[numAttributes];											//Declares a new attributes array called attributes that will store information of all attributes
		String [] allAttributes = reader.getAttributeNames();								//Creates a String array allAttributes that stores the names of all the Attributes
		for(int j =0; j<numAttributes; j++){												//Loops throught all attributes and determines whether its type is NOMINAL or NUMERIC
			AttributeType t = AttributeType.NOMINAL;										//by checking if its first value is NOMINAL or NUMERIC
			if (Util.isNumeric(matrix[0][j])){
				t = AttributeType.NUMERIC;
			}
			String att = allAttributes[j];													//Stores the name of the current attribute to String variable att
			attributes[j] = new Attribute(att, j, t, super.getUniqueAttributeValues(j));	//Stores each attribute to the attributes array one at a time
		}																					//getUniqueAttributeValues is called to make sure that the values of each attribute will be unique
		dataSourceId = reader.getSourceId();												//Sets the variable dataSourceId to the filename/filepath taken from DataReader reader
	}

	/**
	 * Implementation of DataSet's abstract getValueAt method for an actual dataset
	 */
	public String getValueAt(int row, int attributeIndex) {										
		if((row>=0 && row<matrix.length )&& (attributeIndex>=0 &&attributeIndex<matrix[0].length)){ //Checks if the values of row and attributeIndex entered are out of bounds
			return matrix[row][attributeIndex];														//Returns the matrix values located at row: row and column: attributeIndex
		}			
		return null;
	}

	/**
	 * @return the sourceId of the dataset.
	 */
	public String getSourceId() {
		return dataSourceId;																//Returns the filepath/filename
	}

	/**
	 * Returns a virtual dataset over this (actual) dataset
	 * 
	 * @return a virtual dataset spanning the entire data in this (actual) dataset
	 */
	public VirtualDataSet toVirtual() {
		int rows[] = new int[numRows];														//Creates a rows array
		for(int i =0; i<numRows; i++){														//Stores every index of the data set to int array rows
			rows[i] = i;
		}
		VirtualDataSet entireDataSet = new VirtualDataSet(this, rows, attributes);			//Sends the ActualDataSet, and variables rows and attributes to VirtualDataSetClass
		return entireDataSet;																//Returns the VirtualDataSet
	}

	/**
	 * Override of toString() in DataSet
	 * 
	 * @return a string representation of this (actual) dataset.
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Actual dataset (" + getSourceId() + ") with " + getNumberOfAttributes() + " attribute(s) " + getNumberOfDatapoints() + " row(s)").append(System.lineSeparator());
		buffer.append(super.toString());

		return buffer.toString();
	}
}