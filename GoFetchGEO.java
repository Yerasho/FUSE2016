// NAME:  Grogan W. Huff
// NU ID:  #####810
//
// Partners: NONE
//
// Connects to the ncbi.nlm.nih.gov/geo to retrive series matrix files.
// Valid series name should follow GEO format. Example: "GSE8542"
// Files retrieved are saved to current directory and must be unzipped
// to restore them to a series_matrix.txt file using the following format
// $	gunzip GSE8542_series_matrix.txt.gz
// from command line.
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.io.IOException;
import java.util.Scanner;

public class GoFetchGEO
{
    public static void main(String args[])
    {
        // Variables
        boolean tryAgain = true;    // Series of Interest Validation
        String letters = null,      // Letters of Series of Interest
               digits = null;       // Digits of Series of Interest

        // Obtain a series of interest from user
        do
        {
            // Variables
            Scanner in = new Scanner(System.in);    // Scanner

            // Variable initialization
            letters = new String("");               // Reset letters
            digits = new String("");                // Reset digits

            // Prompt
            System.out.print("Enter the series of interest " +
                    "(e.g. \"GSE8542\"): ");
            String orgIn = in.next();

            // Interpret user input
            for (int i = 0; i < orgIn.length(); i++)
            {
                char next = orgIn.charAt(i);
                if (Character.isLetter(next))
                    letters += next;
                if (Character.isDigit(next))
                    digits += next;
            }
            // if either letters or digits not entered, try again.
            if (letters.equals("") || digits.equals(""))
                continue;

            // Confirm interpretation
            char choice;
            do
            {
                System.out.printf("\tIs this correct? : %s%s",
                        letters, digits);
                System.out.print("\n(y / n): ");
                choice = in.next().toLowerCase().charAt(0);
            } while (!(choice == 'n' || choice == 'y'));

            if (choice == 'y')
                tryAgain = false;
        } while (tryAgain);

        // Build the ftp url
        String address = new String("ftp://ftp.ncbi.nlm.nih.gov/geo/series/");
        address += letters + Integer.parseInt(digits)/1000 + "nnn/";
        address += letters + digits;
        address += "/matrix/";
        address += letters + digits + "_series_matrix.txt.gz";

        // Output full address
        System.out.println("Fetching:\n" + address);

        // Make a connection and establish input stream
        try
        {
            // Varibles
            URL url = new URL(address);     // url to file
            URLConnection c =               // connection to url
                url.openConnection();
            InputStream in =                // input stream from connection
                c.getInputStream();
            FileOutputStream out =          // File being written
                new FileOutputStream(
                    letters + digits + "_series_matrix.txt.gz");

            // Read through the input stream and write to file
            try
            {
                byte[] buffer = new byte[4096];
                int bytesRead = -1;
                while ((bytesRead = in.read(buffer)) != -1)
                {
                    out.write(buffer, 0, bytesRead);
                }

                // Confirmation message
                System.out.println("File downloaded");
            }
            catch (IOException IOE)
            {
                // Unforseen connection issues print the stack trace
                IOE.printStackTrace();
            }
            finally
            {
                // In any case, close both out and in streams
                out.close();
                in.close();
            }
        }
        catch (IOException IOE)
        {
            // 404 exception
            System.out.println(
                    "\nNo such series exists. Please try again.");
        }
    } // end main
} // end class
