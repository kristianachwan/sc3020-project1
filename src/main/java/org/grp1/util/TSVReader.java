package org.grp1.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TSVReader {
    /**
     * The function to read the TSV file
     *
     * @param filePath The file path
     * @return The list of unparsed strings from the records file
     */

    public static List<String> ReadTSVFile(String filePath) throws IOException {

        List<String> result = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        reader.readLine();
        String line;

        while ((line = reader.readLine()) != null) {
            result.add(line);
        }

        reader.close();

        return result;
    }

}
