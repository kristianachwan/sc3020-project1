package org.grp1.model;

public class Record {
    /**
     * Represents the tconst of this record
     */
    private String tconst;
    /**
     * Represents the number of average rating in this record
     */
    private float averageRating;
    /**
     * Represents the number of votes for this record
     */
    private int numVotes;

    /**
     * Constructor to instantiate record
     *
     * @param tconst        The tconst of the record
     * @param averageRating The average rating of this record
     * @param numVotes      The number of votes of this record
     */
    public Record(String tconst, float averageRating, int numVotes) {
        this.tconst = tconst;
        this.averageRating = averageRating;
        this.numVotes = numVotes;
    }

    /**
     * The function to return the key of the record
     *
     * @return The key of the record
     */
    public int getKey() {
        return this.getNumVotes();
    }

    /**
     * The function to return the number of votes
     *
     * @return The number of votes of the record
     */
    public int getNumVotes() {
        return this.numVotes;
    }

    /**
     * The function to set the number of votes
     *
     * @param numVotes The number of votes of the record
     */
    public void setNumVotes(int numVotes) {
        this.numVotes = numVotes;
    }

    /**
     * The function to return the average rating of the record
     *
     * @return The average rating of the record
     */
    public float getAverageRating() {
        return this.averageRating;
    }

    /**
     * The function to set the average rating of the record
     *
     * @param averageRating The average rating of the record
     */
    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
    }

    /**
     * The function to get the tconst of the record
     *
     * @return The tconst of the record
     */
    public String getTconst() {
        return this.tconst;
    }

    /**
     * The function to set the tconst of the record
     *
     * @param tconst The tconst of the record
     */
    public void setTconst(String tconst) {
        this.tconst = tconst;
    }

}
