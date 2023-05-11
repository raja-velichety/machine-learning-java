import java.io.*;
import java.util.*;
//Program tests decision tree classifier in specific application
public class Tester
{
    /*************************************************************************/
//constants for file conversions
    private static final int TRAIN_FILE = 1;
    private static final int VALIDATE_FILE = 2;
    private static final int TEST_FILE = 3;
    private static final int CLASSIFY_FILE = 4;
    //number of attributes
    private static final int ATTRIBUTES = 6;
    
    /*************************************************************************/
//Main method
    public static void main(String[] args) throws IOException
    {
        //preprocess files
        convert("originaltrainingfile.txt", "trainingfile.txt", TRAIN_FILE);
        // convert("originalvalidationfile", "validationfile", VALIDATE_FILE);
        convert("originaltestfile.txt", "testfile.txt", TEST_FILE);
        //construct decision tree classifier
        //DecisionTree classifier = new DecisionTree(ATTRIBUTES, CLASSES);
        DecisionTree classifier = new DecisionTree();
        //load training data
        classifier.loadTrainingData("trainingfile.txt");

        //build decision tree
        classifier.buildTree();
        //classify test data
        classifier.classifyData("testfile.txt", "classifiedfile.txt");
        classifier.trainingError("trainingfile.txt");
        classifier.leaveoneout();
        //validate classfier
        //classifier.validate("validationfile.txt");
        //postprocess files
        convert("classifiedfile.txt", "originalclassifiedfile.txt", CLASSIFY_FILE);
    }
    /************************************************************************************************************************************/
    //Method converts one file to another file
    private static void convert(String inputFile, String outputFile, int fileType)
            throws IOException
    {
        //input and output files
        Scanner inFile = new Scanner(new File(inputFile));
        PrintWriter outFile = new PrintWriter(new FileWriter(outputFile));
        //convert original training file to training file
        if (fileType == TRAIN_FILE || fileType == VALIDATE_FILE)
        {
        //read number of records
            int numberRecords = inFile.nextInt();
            int numberAttributes = inFile.nextInt();
            int numberClasses = inFile.nextInt();
            
        //write number of records, attributes, classes, neighbors, majority rule
            outFile.println(numberRecords);
            outFile.println(numberAttributes);
            outFile.println(numberClasses);
        //for each record

            for (int i = 0; i < numberRecords; i++)
            {
                //read attributes/classes and convert them to numbers
                for (int j = 0; j < ATTRIBUTES+1; j++)
                {
                    //read attribute/class label
                    String label = inFile.next();
                    //convert label to number
                    int value = convert(label, j);
                    //print attribute/class number
                    outFile.print(value + " ");
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
                    int value = convert(label, j);
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
    /************************************************************************************************************************************/
    //Method converts attribute/class label located at a column
    //to numerical value, column index starts at 0
    private static int convert(String label, int column)
    {
        int value=0;
        //column 0 - convert education to binary
        if (column == 0)
        {
            if (label.equals("cs"))
                value = 1;
            else if(label.equals("other"))
                value = 0;
        }
        //column 1 - convert smoking to binary
        else if (column == 1)
        {
            if (label.equals("java"))
                value = 1;
            else if (label.equals("no"))
                value = 0;
        }
        //column 2 - convert marital status to binary
        else if (column == 2)

        {
            if (label.equals("c/c++"))
                value = 1;
            else if (label.equals("no"))
                value = 0;
        }
        //column 3 - convert sex to binary
        else if (column == 3)

        {
            if (label.equals("gpa>3"))
                value = 1;
            else if(label.equals("gpa<3"))
                value = 0;
        }
        //column 4 - convert work to binary
        else if (column == 4)

        {
            if (label.equals("large"))
                value = 1;
            else if (label.equals("small"))
                value = 0;
        }
        //column 5 - convert class to number
        else if (column == 5)

        {
            if (label.equals("years<5"))
                value = 1;
            else if (label.equals("years>5"))
                value = 0;
        }
        else 
        {
            if (label.equals("hire"))
                value = 2;
            else if (label.equals("no"))
                value = 1;
        }

        return value;
    }
    //Method converts class integer to class label
    private static String convert(int value)
    {
        String label="";
        if (value == 2)
            label = "hire";
        else if (value == 1)
            label = "no";
        return label;
    }
}