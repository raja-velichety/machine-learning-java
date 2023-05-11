
import java.io.*;
import java.util.*;

//Nearest neighbor classifier class
public class NearestNeighbor {
    /****************************************************************************************************************/

    //Training the record class
    private  class Record
    {
        private double[] attributes;  //attributes of record
        private int className;          //class of record

        //Constructor of record
        private Record(double[] attributes, int className){
            this.attributes = attributes;       //assign attributes
            this.className = className;         //assign class
        }
    }

    /****************************************************************************************************************/

    private ArrayList<Record> records;          //list of training records
    private int numberRecords;              //number of training records
    private int numberAttributes;           //number of attributes
    private int numberClasses;              //number of classes
    private int numberNeighbors;            //number of nearest neighbors
    private String majorityRule;            //majority rule used

    /****************************************************************************************************************/

    //Constructor of classifier
    public NearestNeighbor()
    {
        records = null;         //list of records is empty
        numberRecords= 0;       //number of records and attributes
        numberAttributes= 0;
        numberClasses=0;        //number of classes, neighbors is zero
        numberNeighbors = 0;
        majorityRule = "";      //majority rule has no value
    }

    /****************************************************************************************************************/

    //Method loads training records from training file
    public void loadTrainingData(String trainingFile) throws IOException{

        Scanner inFile =new Scanner(new File(trainingFile));

        //read number of records,attributes,classes,neighbors,majority rule
        numberRecords= inFile.nextInt();
        numberAttributes= inFile.nextInt();
        numberClasses=inFile.nextInt();
        numberNeighbors=inFile.nextInt();
        majorityRule=inFile.next();

        //empty list of records
        records= new ArrayList<Record>();

        //for each record
        for(int i=0;i<numberRecords;i++){
            //create attribute array
            double[] attributeArray =new double[numberAttributes];
            //read attribute values
            for(int j=0;j<(numberAttributes-1);j++){
                attributeArray[j]=inFile.nextDouble();
                //System.out.println(attributeArray[j]);
            }

                //read class name
                int className=inFile.nextInt();
                //System.out.println(className);

                //System.out.println("#################");


                //create record
                Record record=new Record(attributeArray,className);

                 //add record to list of records
                records.add(record);

        }

        inFile.close();

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
            double[] attributeArray=new double[numberAttributes];
            //read attribute values
            for(int j=0;j<numberAttributes-1;j++)
                attributeArray[j]=inFile.nextDouble();
            //find class of attributes
            int className=classify(attributeArray);
            //write class name to output file
            outFile.println(className);
        }
        inFile.close();
        outFile.close();
    }

    /****************************************************************************************************************/
    //Method finds class of given attributes
    private int classify(double[] attributes)
    {
        double[] distance=new double[numberRecords];
        int[] id=new int[numberRecords];

        //find distances between attributes and all records
        for(int i=0;i<numberRecords;i++)
        {
            distance[i]=distance(attributes,records.get(i).attributes);
            id[i]=i;
        }
        //find the nearest neighbors
        nearestNeighbor(distance, id);
        //find majority class of neighbors
        int className = majority(id, attributes);
        //return class
        return className;
    }
    /****************************************************************************************************************/
    //Method finds the nearest neighbors
    private void nearestNeighbor(double[] distance, int[] id)
    {
        //sort the records by their distances and choose the closest neighbors
        for (int i = 0; i < numberNeighbors; i++)
            for(int j=i; j< numberRecords; j++)
            if(distance[i] > distance[j])
            {
                double tempDistance = distance[i];
                distance[i]=distance[j];
                distance[j]=tempDistance;

                int tempId = id[i];
                id[i]=id[j];
                id[j]=tempId;
            }
    }
    /****************************************************************************************************************/
    //Method finds the majority class of nearest neighbors
    private int majority(int[] id, double[] attributes)
    {
        double[] frequency = new double[numberClasses];
        //class frequencies are zero initially
        for(int i=0;i< numberClasses;i++)
            frequency[i]=0;

        //if unweighted majority rule is used
        if(majorityRule.equals("unweighted"))
        {
            //each neighbor contributes 1 to its class
            for(int i=0;i<numberNeighbors;i++)
                frequency[records.get(id[i]).className-1]+=1;
        }
        //if weighted majority rule is used
        else
        {
            //each neighbor contributes 1/distances to its class
            for(int i=0;i<numberNeighbors;i++)
            {
                double d= distance(records.get(id[i]).attributes,attributes);
                frequency[records.get(id[i]).className-1]+=1/(d+0.000001);
            }
        }
        //find majority class
        int maxIndex=0;
        for(int i=0; i<numberClasses;i++)
            if(frequency[i] > frequency[maxIndex])
                maxIndex = i;
        return maxIndex+1;
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
            double[] attributeArray = new double[numberAttributes];

            // read attributes
            for (int j=0; j< numberAttributes-1;j++)
                attributeArray[j] = inFile.nextDouble();

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
        //System.out.println("validation error:" + errorRate);

        inFile.close();
        return errorRate;
    }


    //method finds distance between two points
    //Euclidean distance is used in this application
    private double distance(double[] u, double[] v)
    {
        double distance=0;

        for(int i=0 ; i<u.length; i++)
            distance= distance + (u[i] -v[i]) * (u[i]-v[i]);
        distance = Math.sqrt(distance);

        return distance;
    }

    public void trainingerror(String trainingFile) throws IOException{
        Scanner inFile= new Scanner(new File(trainingFile));

        //read number of records
        int numberRecords= inFile.nextInt();
        int numberAttributes= inFile.nextInt();
        int numberClasses=inFile.nextInt();
        int numberNeighbors=inFile.nextInt();
        String majorityRule=inFile.next();


        //initially zero errors
        int numberErrors = 0 ;

        //for each record
        for(int i=0; i < numberRecords; i++)
        {
            double[] attributeArray = new double[numberAttributes];

            // read attributes
            for (int j=0; j< numberAttributes-1;j++)
                attributeArray[j] = inFile.nextDouble();

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
        System.out.println("training error:" + errorRate);

        inFile.close();
        

    }

    public void leaveoneout() throws IOException{
        double totalerrorrate=0.0;
        int total_records_length=records.size();

        for(int i=0;i<total_records_length;i++){
            loadTrainingData("trainingfile.txt");
            PrintWriter trainingoutFile=new PrintWriter(new FileWriter("leaveonetraining.txt",false));
            PrintWriter testingoutFile=new PrintWriter(new FileWriter("leaveonetesting.txt",false));
            trainingoutFile.println((total_records_length-1)+" "+4+" "+3+" "+5+" "+"weighted");
            for(int j=0;j<records.size();j++){
                if(j==i){
                    testingoutFile.println(1);
                    testingoutFile.print(records.get(j).attributes[0]+" "+records.get(j).attributes[1]+" "+records.get(j).attributes[2]+" "+records.get(j).className);
                }
                else if(j==(records.size()-1)){
                    trainingoutFile.print(records.get(j).attributes[0]+" "+records.get(j).attributes[1]+" "+records.get(j).attributes[2]+" "+records.get(j).className);
                }
                else{

                    trainingoutFile.println(records.get(j).attributes[0]+" "+records.get(j).attributes[1]+" "+records.get(j).attributes[2]+" "+records.get(j).className);

                }
            }
            
            trainingoutFile.close();
            testingoutFile.close();
            loadTrainingData("leaveonetraining.txt");
            totalerrorrate=totalerrorrate+validate("leaveonetesting.txt");
        }
        System.out.println("leave one out error rate: "+(totalerrorrate/Double.valueOf(total_records_length)));
       
    }


}
