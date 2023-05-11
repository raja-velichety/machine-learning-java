import java.io.*;
import java.util.*;

//Decision Tree class
public class DecisionTree{
    /******************************************************************************************/

    //Training record class
    private class Record{
        private int[] attributes;  //attributes of record
        private int className;     //class of record

        //constructor of record
        private Record(int[] attributes, int className){
            this.attributes = attributes; //set attributes
            this.className = className;   //set class name
        }
    }

    /******************************************************************************************/

    //Decision tree node class
    private class Node{

        private String nodeType;  //node - type - internal or leaf
        private int condition; // condition  if node is internal
        private int className; //class name if node is leaf
        private Node left; //left branch
        private Node right; //right branch

        //Constructor of node
        private Node(String nodeType, int value, Node left, Node right){

            this.nodeType = nodeType;  //set node type
            this.left = left;       //set left branch
            this.right = right; //set right branch

            if(nodeType.equals("internal")){
                condition = value;      //if node is internal set condition to value 
                className = -1; //node has no class name
            }
            else{
                className = value; //if node is leaf set class to value
                condition=-1; //node has no condition
            }
        }
    }

     /******************************************************************************************/

      private Node root; // root of decision tree
      private ArrayList<Record> records; //list of training records
      private ArrayList<Integer> attributes; // list of attributes
      private int numberRecords; //number of training records
      private int numberAttributes; //number of attributes
      private int numberClasses; //number of classes

    /******************************************************************************************/

    //Constructor of Decision Tree
    public DecisionTree(int numberAttributes, int numberClasses){

        //set parameters
        this.numberAttributes = numberAttributes;
        this.numberClasses = numberClasses;

        //other data is empty
        this.numberRecords=0;
        this.records=null;
        this.attributes=null;
        this.root=null;
    }
    //Empty Constructor of Decision Tree
    public DecisionTree(){

        //set parameters
        this.numberAttributes = 0;
        this.numberClasses = 0;

        //other data is empty
        this.numberRecords=0;
        this.records=null;
        this.attributes=null;
        this.root=null;
    }

    /******************************************************************************************/

    // Method loads training records from training file
    public void loadTrainingData(String trainingFile) throws IOException{

        Scanner inFile =new Scanner(new File(trainingFile));

        //read number of records
        numberRecords=inFile.nextInt();

        numberAttributes=inFile.nextInt();

        numberClasses=inFile.nextInt();

        //create empty list of records
        records = new ArrayList<Record>();

        //for each record
        for(int i=0; i <numberRecords;i++){
            //create attribute array
            int[] attributeArray = new int[numberAttributes];

            //read attributes
            for(int j=0;j<numberAttributes;j++){
                attributeArray[j]= inFile.nextInt();
            }

            //read class nane
            int className = inFile.nextInt();

            //create record
            Record record = new Record(attributeArray,className);

            //add records to list
            records.add(record);
        }

        //create list of attributes
        attributes= new ArrayList<Integer>();
        for(int i=0;i<numberAttributes;i++){
            attributes.add(i+1);
        }

        inFile.close();
    }
      
    /******************************************************************************************/
    //Method builds decision tree for the whole training data
    public void buildTree(){
        root=build(records,attributes); //initial call to build method
    }

    /******************************************************************************************/

    //Method builds decision tree from given records and attributes, returns the root of the tree that is built
    private Node build(ArrayList<Record> records, ArrayList<Integer> attributes){

        //root node is initiallly empty
        Node node =null;
        //if all the records have same claass
        if(sameClass(records)){
            //find class name
            int className = records.get(0).className;

            //node is leaf with that class
            node =new Node("leaf",className,null,null);
        }

        //if there are no attributes
        else if(attributes.isEmpty()){

            //find majority class of records
            int className= majorityClass(records);

            //node is leaf with that class
            node = new Node("leaf",className,null,null);
        }

        else{
            // find the best condition for current records and attributes
            int condition =bestCondition(records,attributes);

            //collect all the records which have 0 for condition
            ArrayList<Record> leftRecords= collect(records,condition,0);

            //collect all the records which have 1 for condition
            ArrayList<Record> rightRecords= collect(records,condition,1);

            //if either right or left records is empty
            if(leftRecords.isEmpty() || rightRecords.isEmpty()){
                //find majority class of records
                int className =majorityClass(records);

                //node is leaf with that class
                node = new Node("leaf",className,null,null);
            }
            else{

                //create copies of current attributes
                ArrayList<Integer> leftAttributes= copyAttributes(attributes);
                ArrayList<Integer> rightAttributes= copyAttributes(attributes);

                ///remove the best condition form current attributes
                leftAttributes.remove(new Integer(condition));
                rightAttributes.remove(new Integer(condition));

                //create internal node with best condition
                node = new Node("internal",condition,null,null);

                //create left subtree recursively
                node.left= build(leftRecords,leftAttributes);

                 //create right  subtree recursively
                 node.right= build(rightRecords,rightAttributes);
            }

        }
        //return root of the node that is built
        return node;
    }

    /***********************************************************************************************************/

    //Method decides whether all the records have same class
    private boolean sameClass(AbstractList<Record> records){
        //compare class of each record with class of first record
for(int i = 0; i < records.size(); i++)
if(records.get(i).className != records.get(0).className)
    return false;

return true;
}


/*************************************************************************************/

private int majorityClass(ArrayList<Record> records)
{
    int[] frequency = new int[numberClasses];           //frequency array

    for (int i = 0; i < numberClasses; i++)             //initialize frequencies
         frequency[i] = 0;

    for(int i = 0; i < records.size(); i++)            // find frequencies of classes
        frequency[records.get(i).className - 1] += 1;

    int maxIndex = 0;                                   // find class with maximum
    for(int i = 0; i < numberClasses; i++)               //frequency
        if (frequency[i] > frequency[maxIndex])
            maxIndex = i;

    return maxIndex + 1;                                 //return majority class
}

//Method cllects records that have a given value for a given attribute
private ArrayList<Record> collect(ArrayList<Record> records, int condition, int value)
{
    //initialize collection
    ArrayList<Record> result = new ArrayList<Record>();

    //go through records and collect those that have given value
    //for given attribute
    for(int i = 0; i < records.size(); i++)
        if (records.get(i).attributes[condition-1] == value)
            result.add(records.get(i));

    //return collection
    return result;
}

/*************************************************************************************/

//Method makes copy of list of attributes
private ArrayList<Integer> copyAttributes(ArrayList<Integer> attributes)
{
    //initialize copy list
    ArrayList<Integer> result = new ArrayList<Integer>();

    //insert all attrubtes into copy list
    for(int i = 0; i < attributes.size(); i++)
        result.add(attributes.get(i));

    //return copy list
    return result;
}

//Method finds best condition for given records and attributes
private int bestCondition(ArrayList<Record> records, ArrayList<Integer> attributes)
{
    //evaluate first attribute
    double minValue = evaluate(records, attributes.get(0));
    int minIndex=0;

    //go through all aatributes
    for(int i = 0; i < attributes.size(); i++)
    {
        double value = evaluate(records, attributes.get(i));
                                         // evaluate attribute
        if(value < minValue)
        {
            minValue = value;
            minIndex = i;

        }
    }
    return attributes.get(minIndex);
}

/*************************************************************************************/

//method evalutes an attribute using weighted average entropy
private double evaluate(ArrayList<Record> records, int attribute)
{
    //collect records that have attribute value 0
    ArrayList<Record> leftRecords = collect(records , attribute, 0);

    //collect records that have attribute value 1
    ArrayList<Record> rightRecords = collect(records , attribute, 1);

    //find entropy of left records
    double entropyLeft = entropy(leftRecords);

    //find entropy of right records
    double entropyRight = entropy(rightRecords);

    //find weighted average entropy
    double average = entropyLeft*leftRecords.size()/records.size()+
                     entropyRight*rightRecords.size()/records.size();

    //return weighted average entropy
    return average;
}

/*************************************************************************************/

//Method finds entropy of records using gini measure
private double entropy(ArrayList<Record> records)
{
    double[] frequency = new double[numberClasses];   //frequency array

    for(int i = 0; i< numberClasses ; i++) {         //initializes frequencies
    frequency[i] = 0;}

    for(int i = 0; i < records.size(); i++)  {        //find class frequencies
    frequency[records.get(i).className - 1] += 1;}


    double sum = 0;                                 //find sum of frequencies
    for(int i = 0; i < numberClasses; i++)
    sum = sum + frequency[i];

    for(int i = 0 ;i<numberClasses; i++)             //normalize frequencies
    frequency[i] = frequency[i]/sum;
    double max=frequency[0];
    for(int i=0; i<numberClasses; i++)
    {
        if(max < frequency[i])
            max = frequency[i];
    }

    //sum = 0;
    //for(int i = 0; i< numberClasses; i++)            //find sum of squares
    //sum = sum + frequency[i]*frequency[i];

    return 1 - sum;                                  // gini measure
}

/****************************************************************************************************************/
    //Method finds class of given attributes
    private int classify(int[] attributes)
    {
        //start at root node
        Node current = root;

        //go down the tree
        while (current.nodeType.equals("internal"))
        {
            if(attributes[current.condition - 1] == 0)  //if attribute value of condition is 0 go to left
                current = current.left;
            else
                current = current.right;                //else go to right
        }
        //return class name when reaching leaf
        return current.className;
    }
    /****************************************************************************************************************/
    //Methods to read test records from test file and writes classes to classified file
    public void classifyData(String testFile,String classifiedFile)
            throws IOException
    {

        Scanner inFile=new Scanner(new File(testFile));
        PrintWriter outFile=new PrintWriter(new FileWriter(classifiedFile));

        //read number of records
        int numberRecords=inFile.nextInt();

        //write number of records
        outFile.println(numberRecords);

        //for each record
        for(int i=0;i<numberRecords;i++)
        {
            //create attribute array
            int[] attributeArray=new int[numberAttributes];
            //read attribute values
            for(int j=0;j<numberAttributes;j++)
                attributeArray[j]=inFile.nextInt();
            //find class of attributes
            int className=classify(attributeArray);
            //write class name to output file
            outFile.println(className);
        }
        inFile.close();
        outFile.close();
    }

    /****************************************************************************************************************/
    //Method validates classifier using validation file and displays error rate
    public double validate(String validationFile) throws IOException
    {
        Scanner inFile= new Scanner(new File(validationFile));

        //read number of records
        int numberRecords = inFile.nextInt();

        //initially zero errors
        int numberErrors = 0 ;

        //for each record
        for(int i=0; i < numberRecords; i++)
        {
            int[] attributeArray = new int[numberAttributes];

            // read attributes
            for (int j=0; j< numberAttributes;j++)
                attributeArray[j] = inFile.nextInt();

            //read actual class
            int actualClass =inFile.nextInt();

            //find class predicted by classifier
            int predictedClass = classify(attributeArray);

            //error if predicted and actual classes do not match
            if(predictedClass != actualClass)
                numberErrors +=1;

        }
        //find and print error rate
        double errorRate = 100.0 * numberErrors/ numberRecords;
       // System.out.println("validation error:" + errorRate);

        inFile.close();

        return errorRate;
    }

 /****************************************************************************************************************/
     //Method to find the training error of the classifier
     public void trainingError(String trainingfile) throws IOException
    {
        Scanner inFile = new Scanner(new File(trainingfile));
        
        //read number of records, attributes, classes, neighbors, majority rule
        numberRecords = inFile.nextInt();
        numberAttributes = inFile.nextInt();
        numberClasses = inFile.nextInt();
        
        //initialize sum to 0
        int sum = 0;
        
        //for each record
        for(int i = 0; i < numberRecords; i++)
        {
            //create attribute array
            int[] attributeArray = new int[numberAttributes];
            
            //read attributes and convert them to numerical form
            for(int j = 0; j < numberAttributes; j++)
                attributeArray[j]=inFile.nextInt();

            //read class name
            int className=inFile.nextInt();
 
            //find class of attribute
            int guessClassName = classify(attributeArray);
            
            //compare class names and add 1 to total sum
            if(className != guessClassName)
                sum++;
        }
        //find and print error rate
        double errorRate = 100.0 * sum / numberRecords;
        System.out.println("Training error:" + errorRate);
        inFile.close();
        
    }

    public void leaveoneout() throws IOException{
        double totalerrorrate=0.0;
        int total_records_length=records.size();

        for(int i=0;i<total_records_length;i++){
            loadTrainingData("trainingfile.txt");
            
            PrintWriter trainingoutFile=new PrintWriter(new FileWriter("leaveonetraining.txt",false));
            PrintWriter testingoutFile=new PrintWriter(new FileWriter("leaveonetesting.txt",false));
            trainingoutFile.println((total_records_length-1)+" "+6+" "+2);
            for(int j=0;j<records.size();j++){
                if(j==i){
                    testingoutFile.println(1);
                    testingoutFile.print(records.get(j).attributes[0]+" "+records.get(j).attributes[1]+" "+records.get(j).attributes[2]+" "+records.get(j).attributes[3]+" "+records.get(j).attributes[4]+" "+records.get(j).attributes[5]+" "+records.get(j).className);
                }
                else if(j==(records.size()-1)){
                    trainingoutFile.print(records.get(j).attributes[0]+" "+records.get(j).attributes[1]+" "+records.get(j).attributes[2]+" "+records.get(j).attributes[3]+" "+records.get(j).attributes[4]+" "+records.get(j).attributes[5]+" "+records.get(j).className);
                }
                else{

                    trainingoutFile.println(records.get(j).attributes[0]+" "+records.get(j).attributes[1]+" "+records.get(j).attributes[2]+" "+records.get(j).attributes[3]+" "+records.get(j).attributes[4]+" "+records.get(j).attributes[5]+" "+records.get(j).className);

                }
            }
            
            trainingoutFile.close();
            testingoutFile.close();
            loadTrainingData("leaveonetraining.txt");
            buildTree();
            
            totalerrorrate=totalerrorrate+validate("leaveonetesting.txt");
            System.out.println(;
        }
        double leaveoneouterrorrate=(totalerrorrate/Double.valueOf(total_records_length));
        System.out.println("leave one out error rate: "+leaveoneouterrorrate);
       
    }
}