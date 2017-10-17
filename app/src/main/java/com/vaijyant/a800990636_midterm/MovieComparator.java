package com.vaijyant.a800990636_midterm;

import java.util.Comparator;

/**
 * Assignment   : Midterm
 * File         : MovieComparator.java
 * Author       : Vaijyant Tomar
 * */

public class MovieComparator {

    static class MovieRatingComparator implements Comparator<Movie> {
        public int compare(Movie movie1, Movie movie2) {
            double rating1 = Double.parseDouble(movie1.getVote_average().toString());
            double rating2 = Double.parseDouble(movie2.getVote_average().toString());
            return rating1 == rating2 ? 0 : rating1 > rating2 ? -1 : 1;
        }
    }

    static class MoviePopularityComparator implements Comparator<Movie> {
        public int compare(Movie movie1, Movie movie2) {
            double popularity1 = Double.parseDouble(movie1.getPopularity().toString());
            double popularity2 = Double.parseDouble(movie2.getPopularity().toString());
            return popularity1 == popularity2 ? 0 : popularity1 > popularity2 ? -1 : 1;
        }
    }
}
