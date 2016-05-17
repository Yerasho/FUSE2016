// NAME:  Grogan W. Huff
// NU ID:  #####810
//
// Partners: NONE
//
// Splits series matrix files from ncbi.nlm.nih.gov/geo into even
// sample datasets. Valid files should end with "_series_matrix.txt"
// Files generated are save to the current directory.
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ProcessSeriesGEO
{
    public static void main(String[] args)
    {
        // Assert "java ProcesSeriesGEO File_series_matrix.txt"
        if (args.length != 1)
            System.out.println(
                    "Please Format to Example:\n\t" +
                    "java ProcessSeriesGEO GSE8542_series_matrix.txt");
        // Assert file type
        else if (!args[0].endsWith("_series_matrix.txt"))
        {
            System.out.println("Not a valid _series_matrix.txt file." +
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
                Scanner get =                       // User Scanner
                    new Scanner(System.in);
                String line = null;                 // Line from file
                int num = 0;                        // Sample count
                char choice = 'a';                  // Choice validation

                // Throw out input stream until series matrix table
                while (in.hasNext())
                {
                    line = in.nextLine();
                    if (line.equals("!series_matrix_table_begin"))
                    {
                        line = in.nextLine();
                        break;
                    }
                }

                // Print current line
                System.out.println(line);

                // Variables
                String[] col = line.split("\t");    // Headers
                int header = col.length;            // Count of headers

                // Prompt for split
                do
                {
                    System.out.printf("%d samples:" + 
                            "How many samples per group?: ",
                            header - 1);
                    try
                    {
                        num = get.nextInt();
                    }
                    catch (Exception E)
                    {
                        get.nextLine();
                        num = 0;
                    }

                    // Ensure split produces even datasets
                    if ((header - 1) % num != 0)
                    {
                        System.out.println("Choice does not divide evenly.");
                        num = 0;
                        continue;
                    }

                    // Confirm split
                    do
                    {
                        System.out.printf("Split into %d groups of %d? " +
                                "(y / n):", (header - 1)/num, num);
                        get.nextLine();
                        choice = get.nextLine().charAt(0);
                    } while (!(choice == 'y' || choice == 'n'));
                } while (num < 1 || choice == 'n');

                // Begin writing files
                try
                {
                    // Variables
                    PrintWriter[] output =  // an output stream for each file
                        new PrintWriter[(col.length - 1)/num];

                    for (int i = 0; i < output.length; i++)
                    {
                        output[i] = new PrintWriter(i + args[0]);
                    }

                    // while we work through the series matrix table
                    while (!line.equals("!series_matrix_table_end"))
                    {
                        // for each file
                        for (int i = 0; i < output.length; i++)
                        {
                            // Append the first col to each file
                            output[i].write(col[0] + "\t");

                            // Split the remaining samples among the files
                            for (int j = i * num + 1;
                                    j < i * num + (num + 1); j++)
                            {
                                output[i].write(col[j] + "\t");
                            }

                            // End each row of each file
                            output[i].write("\n");
                        }

                        // Move to new line
                        line = in.nextLine();
                        col = line.split("\t", header);
                    }

                    // close each output stream
                    for (int i = 0; i < output.length; i++)
                    {
                        output[i].close();
                    }
                }
                catch (IOException IOE)
                {
                }

                // close input stream
                in.close();
            }
            catch (FileNotFoundException FNFE)
            {
                // No such file
                System.out.println("No such file exists. Please try again.");
            }
        }
    } // end main
} // end class
