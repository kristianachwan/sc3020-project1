package org.grp1.model;

import org.grp1.index.NodeChild;

public class Record implements NodeChild {
    private String tconst;
    private float averageRating;
    private int numVotes;

    public Record(String tconst, float averageRating, int numVotes) {
        this.tconst = tconst;
        this.averageRating = averageRating;
        this.numVotes = numVotes;
    }

    public int getKey() {
        return this.getNumVotes();
    }

    public int getNumVotes() {
        return this.numVotes;
    }

    public void setNumVotes(int numVotes) {
        this.numVotes = numVotes;
    }

    public float getAverageRating() {
        return this.averageRating;
    }

    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
    }

    public String getTconst() {
        return this.tconst;
    }

    public void setTconst(String tconst) {
        this.tconst = tconst;
    }
}
