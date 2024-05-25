import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.Arrays;
import java.util.Scanner;

/**
 * This class is meant to serve ITP 265 students as a help for getting input and error checking on input, but
 * may also be used as a general purpose class to store methods which may be needed across lots of projects.
 *
 * @author Kendra Walther and Matteus Schmidt
 * @version Fall 2023
 */
public class BFF {
    private Scanner sc; //declare

    public BFF(){
        sc = new Scanner(System.in); // initialize
    }
    /**
     * Prompt the user and read one word of text as a String
     * @param prompt: the question to ask the user
     * @return: a one word String - if the user enters multiple words, all other input until the return will be discarded.
     */
    public String inputWord(String prompt) {
        System.out.print(prompt);
        String word = sc.next();
        sc.nextLine(); // remove any "garbage" (like extra whitespace or the return key) after the one word that is read in
        return word;
    }
    /**
     * Prompt the user and read one line of text as a String
     * @param prompt: the question to ask the user
     * @return: a line of user input (including spaces, until they hit enter)
     */
    public String input(String prompt) {
        System.out.print(prompt);
        return sc.nextLine();
    }


//    public LocalDate inputDate(String prompt) {
//        System.out.println(prompt); // what kind of date is the program looking for
//        int year = inputInt("Year ", 1900, 2555); // arbitrary future date.
//        int month = inputInt("Month ", 1, 12);
//        int numDays = Month.of(month).length(Year.isLeap(year)); // find out the number of actual days in the month.
//        int day = inputInt("Day ", 1, numDays);
//
//        // create LocalDate object
//        return LocalDate.of(year, month, day);
//    }

    public LocalDate inputFutureDate(String prompt, LocalDate date) {
        System.out.println(prompt); // what kind of date is the program looking for
        int yearNow = date.getYear();
        int monthNow = date.getMonthValue();
        int year = inputRedInt("\tYear: ", yearNow, 2555); // arbitrary future date.
        int month;
        if (year > yearNow) month = inputRedInt("\tMonth: ", 1, 12);
        else month = inputRedInt("\tMonth: ", date.getMonthValue(), 12);
        int numDays = Month.of(month).length(Year.isLeap(year)); // find out the number of actual days in the month.
        int day;
        if (year == yearNow && month == monthNow) day = inputRedInt("\tDay: ", date.getDayOfMonth(), numDays);
        else day = inputRedInt("\tDay: ", 1, numDays);
        // create LocalDate object
        return LocalDate.of(year, month, day);
    }

    /**
     * Prompt the user and read one word of text as a String, returns a String that matches
     * one allowed matching words passed in as parameters (case sensitive)
     * @param prompt: the question to ask the user
     * @param match1: a word the input is allowed to be
     * @param match2: a word the input is allowed to be
     * @param match3: a word the input is allowed to be
     * @return: a one word  String that matches one of the three allowed words (case-sensitive)
     */
    public String input(String prompt, String match1, String match2, String match3) {
        String answer = input(prompt);  // DRY
        answer = answer.trim(); // remove extra white space
        //TODO: checking for matching words
        while(! (answer.equalsIgnoreCase(match1) ||answer.equalsIgnoreCase(match2)
                || answer.equalsIgnoreCase(match3) ) ){
            System.out.printf("%s is bad. Must match one of the allowed strings %s, %s, or %s%n",
                    answer, match1, match2, match3);
            answer = input(prompt);  // get new answer from the user
        }
        return answer;
    }

    /**
     * Prompt the user and read one word of text as a String, returns a String that matches
     * one allowed matching words passed in as parameters (case sensitive)
     * @param prompt: the question to ask the user
     * @param matches
     * @return: a one word  String that matches one of the  allowed words (case-sensitive)
     */
    public String input(String prompt, String... matches) {
        String answer = input(prompt);  // DRY
        answer = answer.trim(); // remove extra white space
        //TODO: checking for matching words
        // is answer INSIDE the array String[] matches
        boolean found = false;

        while(!found){
            int i = 0;
            while(!found && i < matches.length){
                if(answer.equalsIgnoreCase(matches[i])){ // the user input is an ALLOWED match.
                    found = true;
                }
                i++;
            }
            if(!found){ // bad input
                System.out.printf("%s is bad. Must match one of the allowed strings %s%n",
                        answer, Arrays.toString(matches));
                answer = input(prompt); // get a new try
            }
        }

        return answer;
    }


    /**
     * Prompt the user and read an int, clearing whitespace or the enter after the number
     * @param prompt: the question to ask the user
     * @return: an int
     */
    public int inputInt(String prompt) {
        System.out.print(prompt);
        while(!sc.hasNextInt()){
            String garbage = sc.nextLine(); // grab the "bad data"
            System.out.println(garbage + " was not an int.");
            System.out.print(prompt);
        } //here, we know an int is waiting on System.in
        int num = sc.nextInt(); // grab the number
        String extra =  sc.nextLine();
        return num;
    }

    /**
     * Prompt the user and read an int between (inclusive) of both min and max,
     * clearing whitespace or the enter after the number
     * @param prompt: the question to ask the user
     * @return: an int between min and max
     */
    public int inputRedInt(String prompt, int min, int max) {
        int number = inputInt(prompt); //get a number (DRY)
        // check the number is in range
        while(! (number >= min && number <= max)){
            try {
                System.out.flush();
                Thread.sleep(100);
                System.err.println(number + " is not in the allowed range, ["
                        + min + " - " + max + "]");
                System.err.flush();
                Thread.sleep(100);
            }
            catch(Exception e){
                //IGNORE
            }
            number = inputInt(prompt);
        }

        return number;
    }
    /**
     * Prompt the user and read a floating point number, clearing whitespace or the enter after the number
     * @param prompt: the question to ask the user
     * @return: a double value
     */
    public double inputDouble(String prompt) {
        System.out.print(prompt);
        while(!sc.hasNextDouble()){
            String garbage = sc.nextLine(); // grab the "bad data"
            System.out.println(garbage + " was not a double.");
            System.out.print(prompt);
        } //here, we know a double is waiting on System.in
        double num = sc.nextDouble(); // grab the number
        sc.nextLine(); //clear the inputStream
        return num;
    }

    /**
     * Prompt the user and read a floating point number between (inclusive) of min and max,
     * clearing whitespace or the enter after the number
     * @param prompt: the question to ask the user
     * @return: a double value
     */
    public double inputDouble(String prompt, double min, double max) {
        //get a number
        double number = inputDouble(prompt);
        // check the number is in range
        while(! (number >= min && number <= max)){
            System.out.println(number + " is not in the allowed range, ["
                    + min + " - " + max + "]");
            number = inputDouble(prompt);
        }

        return number;
    }

    /**
     * Prompt the user and read a boolean value, clearing whitespace or the enter after the number
     * @param prompt: the question to ask the user
     * @return: a boolean value
     */
    public boolean inputBoolean(String prompt) {
        System.out.print(prompt);
        while(!sc.hasNextBoolean()){
            String garbage = sc.nextLine(); // grab the "bad data"
            System.out.println(garbage + " was not a boolean Allowed values are:"
                    + "\"true\" or \"false\"");
            System.out.print(prompt);
        } //here, we know a boolean is waiting on System.in
        boolean answer = sc.nextBoolean(); // grab the cboolean
        sc.nextLine();
        return answer;
    }

    /**
     * Prompt the user enter yes or no (will match y/yes and n/no any case) and return true for yes and false for no.
     * @param prompt: the question to ask the user
     * @return: a boolean value
     */
    public boolean inputYesNo(String prompt) {
        String answer = input(prompt);
        while(! (isYes(answer) || isNo(answer))){
            System.out.println("Expecting a yes or no answer. ");
            answer = input(prompt);
        } // at this point answer should be y/yes or n/no

        return isYes(answer); // (answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("yes") );

    }

    private boolean isNo(String a){
        return  (a.equalsIgnoreCase("n") || a.equalsIgnoreCase("no") || (a.equalsIgnoreCase("non")) );
    }
    private boolean isYes(String a){
        return  (a.equalsIgnoreCase("y") || a.equalsIgnoreCase("yes") || a.equalsIgnoreCase("si"));
    }

    public void print(String msg){
        System.out.print(msg);
    }

    /**
     * A shortcut to System.out.println
     * @param msg: the line to be output
     * @return: none
     */
    public void println(String msg){
        System.out.println(msg);
    }
    /**
     * A shortcut to System.out.println which will surround the message with some stars to make it stand out.
     * @param msg: the line to be output
     * @return: none
     */
    public void printFancy(String msg){
        System.out.println("********************************");
        System.out.println(msg);
        System.out.println("********************************");
    }

    public void printRed(String s) {
        try {
            System.out.flush();
            Thread.sleep(100);
            System.err.println(s);
            System.err.flush();
            Thread.sleep(100);
        }
        catch(Exception e){
            //IGNORE
        }
    }

}