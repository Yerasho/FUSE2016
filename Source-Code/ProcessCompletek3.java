// NAME:  Grogan Huff
// NU ID:  #####810
//
// Partners: NONE
//
// Splits csv files of FASTA data in the format:
//   >name1,SEQUENCE1,>name2,SEQUENCE2,name3,SEQUENCE3
//   >name1,SEQUENCE1,>name2,SEQUENCE2,name3,SEQUENCE3
//   ...
// where each row is the contents of a 3 sequence
// FASTA file into seperate files.
// The naming convention is
//   0file.csv
//   1file.csv
//   ...
// Individual file contents will appear as:
//   >name1
//   SEQUENCE1
//   >name2
//   SEQUENCE2
//   >name3
//   SEQUENCE3
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ProcessCompletek3
{
    public static void main(String[] args)
    {
        // Assert Format
        if (args.length != 1)
            System.out.println(
                    "Please Format to Example:\n\t" +
                        "java ProcessCompletek3 GSE8542k3FASTA.csv");
        // Assert file type
        else if (!args[0].endsWith(".csv"))
        {
            System.out.println("Not a valid .csv file." +
                               " Please try again.");
        }
        // Continue
        else
        {
            // Read in file
            try
            {
                // Variables
                Scanner in =                        // File Scanner
                    new Scanner(new File(args[0]));
                String line = in.nextLine();        // Line from file
                String[] col = line.split(",");
                // Check that csv is 6 columns. Program does not handle
                // FASTA files of different sizes at this time.
                if (col.length != 6)
                {
                    System.out.println(args[0] + " may not be formatted correctly. Please ensure:");
                    System.out.println(">Name1,SEQUENCE1,>Name2,SEQUENCE2,>Name3,SEQUENCE3$");
                    System.out.println("...");
                }
                else
                {
                // Begin writing files
                    for (int i = 0; true; i++)
                    {
                        try
                        {
                            // Do not overwrite files, in case user
                            // already has "0file.csv" or similar
                            if (new File("./" + i + "file.csv").exists())
                            {
                                throw new FileNotFoundException("Exists");
                            }
                            // Variables
                            PrintWriter output = new PrintWriter(i + "file.csv");

                            // for each file
                            for (int j = 0; j < col.length; j++)
                            {
                                // Append the first col to each file
                                output.write(col[j] + "\n");
                            }

                            output.close();

                        }
                        catch (FileNotFoundException FNFE)
                        {
                            System.out.printf("An exception occured while writing %s.\n", i + "file.csv");
                            System.out.println("A file with this name may already exist or some other error occured.");
                            System.out.println("The file was not written, please isolate and run seperately.\n");
                        }
                        catch (Exception E)
                        {
                            System.out.printf("An exception occured while writing %s.\n", i + "file.csv");
                            System.out.println("Please check all requirements and try again.");
                            System.out.println("The file was not written, please isolate and run seperately.\n");
                        }
                        // Move to new line
                        if (in.hasNextLine())
                        {
                            line = in.nextLine();
                            col = line.split(",", 6);
                        }
                        else
                        {
                            break;
                        }
                    }
                }

                // close input stream
                in.close();
            }
            catch (FileNotFoundException FNFE)
            {
                // No such file
                System.out.println("No such file exists. Please try again.");
            }
            catch (NoSuchElementException NSEE)
            {
                // File empty
                System.out.println("That file may be empty. Please try again.");
            }
        }
    } // end main
} // end class
