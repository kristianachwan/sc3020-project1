package org.grp1.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TSVReader {

    public static List<String> ReadTSVFile(String filePath) throws FileNotFoundException, IOException {

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
