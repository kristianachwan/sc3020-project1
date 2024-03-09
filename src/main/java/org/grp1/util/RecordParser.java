package org.grp1.util;

import org.grp1.model.Record;

public class RecordParser {

    public static Record ParseRecordStr(String recordStr) throws NumberFormatException {
        
        String[] recordValues = recordStr.split("\\s+");

        String tconst = recordValues[0];
        float averageRating = Float.parseFloat(recordValues[1]);
        int numVotes = Integer.parseInt(recordValues[2]);

        return new Record(tconst, averageRating, numVotes);
    }
    
}
