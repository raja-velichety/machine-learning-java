
import java.io.*;
import java.util.*;
//Program tests nearest neighbor classifier in specific application
public class Tester
{
    /*************************************************************************/
//constants for file conversions
    private static final int TRAIN_FILE = 1;
    private static final int VALIDATE_FILE = 2;
    private static final int TEST_FILE = 3;
    private static final int CLASSIFY_FILE = 4;
    //number of attributes
    private static final int ATTRIBUTES = 3;
    /*************************************************************************/
    //Main method
    public static void main(String[] args) throws IOException
    {
    //preprocess files
        convert("originaltrainingfile.txt", "trainingfile.txt", TRAIN_FILE);
        convert("originalvalidationfile.txt", "validationfile.txt", VALIDATE_FILE);
        convert("originaltestfile.txt", "testfile.txt", TEST_FILE);
        //construct nearest neighbor classifier
        NearestNeighbor classifier = new NearestNeighbor();
        //load training data
        classifier.loadTrainingData("trainingfile.txt");
        //classify test data
        classifier.classifyData("testfile.txt", "classifiedfile.txt");
        //validate classfier
        classifier.trainingerror("trainingfile.txt");
        //classifier.validate("validationfile.txt");

        classifier.leaveoneout();
        //postprocess files
        convert("classifiedfile.txt", "originalclassifiedfile.txt", CLASSIFY_FILE);
    }
    /*************************************************************************/
    //Method converts one file to another file
    private static void convert(String inputFile, String outputFile, int fileType)
            throws IOException
    {
    //input and output files
        Scanner inFile = new Scanner(new File(inputFile));
        PrintWriter outFile = new PrintWriter(new FileWriter(outputFile));
    //convert original training file to training file
        if (fileType == TRAIN_FILE)
        {
    //read number of records, attributes, classes, neighbors, majority rule
            int numberRecords = inFile.nextInt();
            int numberAttributes = inFile.nextInt();
            int numberClasses = inFile.nextInt();
            int numberNeighbors = inFile.nextInt();
            String majorityRule = inFile.next();
    //write number of records, attributes, classes, neighbors, majority rule
            outFile.println(numberRecords + " " + numberAttributes + " " + numberClasses
                    + " " + numberNeighbors + " " + majorityRule);
    //for each record

            for (int i = 0; i < numberRecords; i++)
            {
    //read attributes/classes and convert them to numbers
                for (int j = 0; j < ATTRIBUTES+1; j++)
                {
    //read attribute/class label
                    String label = inFile.next();
    //convert label to number
                    double value = convert(label, j);
    //print attribute number
                    if (j < ATTRIBUTES)
                        outFile.print(value + " ");
    //print class number
                    else
                        outFile.print((int)value + " ");
                }
                outFile.println();
            }
        }
    //convert original validation file to validation file
        else if (fileType == VALIDATE_FILE)
        {
    //read number of records
            int numberRecords = inFile.nextInt();
    //write number of records
            outFile.println(numberRecords);
    //for each record
            for (int i = 0; i < numberRecords; i++)
            {
    //read attributes/classes and convert them to numbers
                for (int j = 0; j < ATTRIBUTES+1; j++)
                {
    //read attribute/class label
                    String label = inFile.next();
    //convert label to number
                    double value = convert(label, j);
    //print attribute number
                    if (j < ATTRIBUTES)
                        outFile.print(value + " ");
    //print class number
                    else
                        outFile.print((int)value + " ");
                }
                outFile.println();
            }
        }
    //convert original test file to test file
        else if (fileType == TEST_FILE)
        {
    //read number of records
            int numberRecords = inFile.nextInt();
    //write number of records
            outFile.println(numberRecords);
    //for each record
            for (int i = 0; i < numberRecords; i++)
            {

    //read attributes and convert them to numbers
                for (int j = 0; j < ATTRIBUTES; j++)
                {
    //read attribute label
                    String label = inFile.next();
    //convert label to number
                    double value = convert(label, j);
    //print number
                    outFile.print(value + " ");
                }
                outFile.println();
            }
        }
    //convert classified file to original classified file
        else
        {
    //read number of records
            int numberRecords = inFile.nextInt();
    //write number of records
            outFile.println(numberRecords);
    //for each record
            for (int i = 0; i < numberRecords; i++)
            {
    //read class number
                int value = inFile.nextInt();
    //convert number to label
                String label = convert(value);
    //print label
                outFile.println(label);
            }
        }
        inFile.close();
        outFile.close();
    }
    //Method converts attribute/class label located at a column
    //to numerical value, column index starts at 0
    private static double convert(String label, int column)
    {
        double value=0.0;
    //column 0 - convert grade to number
        if (column == 0)
        {
            value = Double.valueOf(label);
            value = value/100.0;
        }
    //column 1 - convert gpa to normalized gpa
        else if (column == 1)
        {
            value = Double.valueOf(label);
            value = value/4.0;
        }

        else if(column == 2){
            if (label.equals("A"))
                value = 1.00;
            else if (label.equals("B"))
                value = 1.50;
            else if(label.equals("C"))
                value = 2.00;
        }
        //column 2 - convert class to number
        else if(column == 3)
        {
            if (label.equals("good"))
                value = 1;
            else if(label.equals("bad"))
                value = 2;
            else if(label.equals("average")){
                value=3;
            }
        }
        return value;
    }
    //Method converts class integer to class label
    private static String convert(int value)
    {
        String label;
        if (value == 1)
            label = "good";
        else if(value == 2 )
            label = "bad";
        else
            label ="average";
        return label;
    }
}
