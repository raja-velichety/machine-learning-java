import java.io.*;
import java.util.*;

import javax.lang.model.util.ElementScanner6;
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
    private static final int ATTRIBUTES = 5;
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
        //load training error
        classifier.trainingError("trainingfile.txt");
        //validating leaveoneout training error
        classifier.leaveoneout();
        //classify test data
        classifier.classifyData("testfile.txt", "classifiedfile.txt");
        //validate classfier
        //classifier.validate("validationfile.txt");
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
        else if(fileType == CLASSIFY_FILE)
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
    //column 0 - convert score to normalised
        if(column == 0)
        {
            value = Double.valueOf(label);
            value = Math.round((value-500))/400.0;
        }
        //column 1 - convert gpa to normalized gpa
        else if (column == 1)
        {
            value = Double.valueOf(label);
            value = Math.round((value-30))/60.0;
        }
        //column 2 - convert grade to number
        else if (column == 2)
        {
            value = Double.valueOf(label);
            value = Math.round((value-30))/50.0;
        }
        //column 3 - convert class to number
        else if(column == 3)
        {
            if (label.equals("male"))
                value = 0.5;
            else
                value = 1.5;
        }
        else if(column == 4)
        {
            if (label.equals("single"))
                value = 1.0;
            else if(label.equals("married"))
                value = 1.5;
            else if(label.equals("divorced"))
                value = 2.0;

        }
        else if(column == 5)
        {
            if (label.equals("low"))
                value = 1;
            else if(label.equals("medium"))
                value = 2;
            else if(label.equals("high"))
                value = 3;
            else if(label.equals("undetermined"))
                value = 4;
        }
        
        
        return value;
    }
    //Method converts class integer to class label
    private static String convert(int value)
    {
        String label="";
        if (value == 1)
            label = "low";
        else if (value == 2)
            label = "medium";
        else if(value == 3)
            label = "high";
        else if(value == 4){
            label="undetermined";
        }
        return label;
    }
}
