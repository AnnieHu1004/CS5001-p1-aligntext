import java.util.ArrayList;

/**
 * Practical[01]:CS5001-p1-aligntext.
 * @author Anli Hu <ah373@st-andrews.ac.uk>
 * @version 2.0
 */
public class AlignText {
    private static final int MAX_LENGTH_OF_ARGS = 3; // max length of String array Args that is allowed
    private static final int LENGTH_OF_AD_STR = 4; // the string length of signpost's side: " |" and " |"

    /**
     * method for confirming align_mode.
     * @param  option introduce args[2], the align_mode input by users
     * @return certain align_mode
     *         if args[2] is invalid align_mode, return to default align_mode "L"
     */
    public static Boolean options(String option) {
        switch (option) {
            case "R":
            case "C":
            case "L":
            case "S":
                break;
            default:
                return false;
        }
        return true;
    }

    /**
     * method for preprocessing the *.txt into an ArrayList.
     * @param paragraphs an array of String that contain certain paragraph in each element
     * @param lineLength the maximum number of words possible within the specified limit
     * @return an arrayList that contains information for each line of the txt document
     */
    public static ArrayList<ArrayList<String>> alignInLine(String[] paragraphs, int lineLength) {
        ArrayList<ArrayList<String>> lines = new ArrayList<>();
        int row = 0; // variable row refers to the specific line number of preprocessed text

        for (int i = 0; i < paragraphs.length; i++) {
            lines.add(new ArrayList<>()); // initialize "lines"
            // according to the number of paragraphs
            // split the "paragraphs" into different single String[] "paragraph"
            String[] paragraph = paragraphs[i].split(" ");
            // create an equal-length array to record the length of strings within each element
            int[] lengthOfChar = new int[paragraph.length];

            for (int j = 0; j < paragraph.length; j++) {
                lengthOfChar[j] = paragraph[j].length();
            }
            // for each paragraph, add words and whitespace to ArrayList according to the given line_length
            // each element in "lines" contains information for each row
            lines.get(row).add(paragraph[0]);
            lines.get(row).add(" ");

            int sum = 0;
            for (int j = 1; j < paragraph.length; j++) {
                // use "lengthOfChar" to decide whether program should add words and space
                // to the current row(current ArrayList) or create a new row(new ArrayList) to add words
                sum = sum + lengthOfChar[j - 1];
                sum++;
                // if sum  exceeds the line_length, creating a new row(ArrayList)
                if (sum - 1 > lineLength || sum + lengthOfChar[j] > lineLength) {
                    lines.add(new ArrayList<>());
                    row++; // update the number of row
                    sum = 0;
                }
                // add words and spaces to each row(ArrayList)
                lines.get(row).add(paragraph[j]);
                lines.get(row).add(" ");
            }
            row++; //update row number for next new paragraph
        }
        // remove all spaces at the end of each line
        for (ArrayList<String> line : lines) {
            line.remove(line.size() - 1);
        }
        return lines;
    }

    /**
     * method for choosing certain method to align text.
     * @param lines      an ArrayList contains text that has been preprocessed per line
     * @param option     align_mode
     * @param lineLength the maximum number of words possible within the specified limit
     */
    public static void chooseAlignType(ArrayList<ArrayList<String>> lines, String option, int lineLength) {
        switch (option) {
            case "R":rightAligned(lines, lineLength);
                break;
            case "C":centerAligned(lines, lineLength);
                break;
            case "S":signPostAligned(lines, lineLength);
                break;
            case "L":leftAligned(lines);
                break;
            default:
        }
    }

    /**
     * method for recording the total number of space need to output for each line.
     * @param lines      an ArrayList contains text that has been preprocessed per line
     * @param lineLength the maximum number of words possible within the specified limit
     * @return an ArrayList contains number of space need to output
     */
    public static ArrayList<Integer> numOfSpace(ArrayList<ArrayList<String>> lines, int lineLength) {
        ArrayList<Integer> testSpace = new ArrayList<>();
        // calculate the number of characters line by line
        for (ArrayList<String> line : lines) {
            int num = 0;
            // calculate the number of characters of current line
            for (String wordOrSpace : line) {
                num = num + wordOrSpace.length();
            }
            // calculate the number of space need to be printed
            // if num > lineLength, no space needs to be printed
            testSpace.add(Math.max(lineLength - num, 0));
        }
        return testSpace;
    }

    /**
     * method for output left-aligned text.
     * @param lines an ArrayList contains text that has been preprocessed per line
     */
    public static void leftAligned(ArrayList<ArrayList<String>> lines) {
        print(null, null, lines, null, null);
    }

    /**
     * method for output right-aligned text.
     * @param lines      an ArrayList contains text that has been preprocessed per line
     * @param lineLength the maximum number of words possible within the specified limit
     */
    public static void rightAligned(ArrayList<ArrayList<String>> lines, int lineLength) {
        ArrayList<Integer> numsOfSpace = numOfSpace(lines, lineLength);
        print(null, numsOfSpace, lines, null, null);
    }

    /**
     * method for output center-aligned text.
     * @param lines      an ArrayList contains text that has been preprocessed per line
     * @param lineLength the maximum number of words possible within the specified limit
     */
    public static void centerAligned(ArrayList<ArrayList<String>> lines, int lineLength) {
        ArrayList<Integer> testSpace = numOfSpace(lines, lineLength);
        // only need to output half number of needed spaces
        for (int i = 0; i < testSpace.size(); i++) {
            if (testSpace.get(i) % 2 != 0) {
                testSpace.set(i, testSpace.get(i) / 2 + 1);
            } else {
                testSpace.set(i, testSpace.get(i) / 2);
            }
        }
        print(null, testSpace, lines, null, null);
    }

    /**
     * method for output signpost-aligned text.
     * @param lines      an ArrayList contains text that has been preprocessed per line
     * @param lineLength the maximum number of words possible within the specified limit
     */
    public static void signPostAligned(ArrayList<ArrayList<String>> lines, int lineLength) {
        // if certain word overflows,the lineLength change
        for (ArrayList<String> line : lines) {
            if (line.size() == 1) {
                lineLength = Math.max(lineLength, line.get(0).length());
            }
        }
        ArrayList<Integer> testSpace = numOfSpace(lines, lineLength);

        //print the top of signpost
        printTheTopOfSignpost(lineLength);
        //print the body
        String left = "| ";
        String right  = " |";
        print(left, null, lines, testSpace, right);
        //print the second half of board
        secondHalfOfSignpost(lineLength);
    }

    /**
     * method for print text.
     * @param left         the String needed to be printed on the left side
     * @param lNumsOfSpace an ArrayList contains the number of spaces needed to be print on the left per line
     * @param lines        an ArrayList contains text that has been preprocessed per line
     * @param rNumsOfSpace an ArrayList contains the number of spaces needed to be print on the right per line
     * @param right        the String needed to be printed on the right side
     */
    public static void print(String left, ArrayList<Integer> lNumsOfSpace,
                             ArrayList<ArrayList<String>> lines,
                             ArrayList<Integer> rNumsOfSpace, String right) {
        for (int i = 0; i < lines.size(); i++) {
            // print string on the left
            if (left != null) {
                System.out.print(left);
            }
            // print spaces on the left
            if (lNumsOfSpace != null) {
                if (lNumsOfSpace.get(i) > 0) {
                    System.out.printf("%" + lNumsOfSpace.get(i) + "s", " ");
                }
            }
            // print string
            for (int j = 0; j < lines.get(i).size(); j++) {
                System.out.print(lines.get(i).get(j));
            }
            // print spaces on the right
            if (rNumsOfSpace != null) {
                if (rNumsOfSpace.get(i) > 0) {
                    System.out.printf("%" + rNumsOfSpace.get(i) + "s", " ");
                }
            }
            // print string on the right
            if (right != null) {
                System.out.print(right);
            }
            System.out.println();
        }
    }

    /**
     * method for printing the top of signpost.
     * @param lineLength the maximum number of words possible within the specified limit
     */
    public static void printTheTopOfSignpost(int lineLength) {
        // print the first line   " ___...___ "
        System.out.print(" ");
        for (int i = 0; i < lineLength + 2; i++) {
            //(+ 2) means add the number of the whitespace on each side of the text
            System.out.print("_");
        }
        System.out.println(" ");
        // print the second line "/    ...    \"
        String m = String.format("%" + (lineLength + 2) + "s", " ");
        System.out.println("/" + m + "\\");
    }

    /**
     * method for printing the second half of signpost.
     * @param lineLength the maximum number of words possible within the specified limit
     */
    public static void secondHalfOfSignpost(int lineLength) {
        //print "\___...___/"
        System.out.print("\\");
        for (int i = 0; i < lineLength + 2; i++) {
            System.out.print("_");
        }
        System.out.println("/");
        //print the hand bar
        System.out.println("        |  |");
        System.out.println("        |  |");
        System.out.println("        L_ |");
        System.out.println("       / _)|");
        System.out.println("      / /__L");
        System.out.println("_____/ (____)");
        System.out.println("       (____)");
        System.out.println("_____  (____)");
        System.out.println("     \\_(____)");
        System.out.println("        |  |");
        System.out.println("        |  |");
        System.out.println("        \\__/");
    }

    /**
     * method for testing and running program.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        try {
            String[] paragraphs = FileUtil.readFile(args[0]);
            int lineLength = Integer.parseInt(args[1]);
            //if args[1] is an invalid line length, an exception can be caught
            if (lineLength < 1) {
                lineLength = Integer.parseInt("");
            }
            //default align_mode is left_align
            String alignType = "L";

            if (args.length == MAX_LENGTH_OF_ARGS) {
                if (options(args[2])) {
                    alignType = args[2];
                } else {
                    System.out.println("usage: java AlignText file_name line_length [align_mode]");
                }
                // minus length of additional strings "| "and " |" to avoid overflowing
                if (args[2].equals("S")) {
                    lineLength -= LENGTH_OF_AD_STR;
                }
            }
            ArrayList<ArrayList<String>> lines = alignInLine(paragraphs, lineLength);
            chooseAlignType(lines, alignType, lineLength);
        } catch (Exception e) {
            System.out.println("usage: java AlignText file_name line_length");
        }
    }
}
