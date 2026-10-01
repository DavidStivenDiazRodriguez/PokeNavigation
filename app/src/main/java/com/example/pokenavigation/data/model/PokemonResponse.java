package com.example.pokenavigation.data.model;

import java.util.List;

public class PokemonResponse {
    private int count;
    private String next;
    private String previous;
    private List<Pokemon> results;

    public int getCount() {
        return count;
    }

    public String getNext() {
        return next;
    }

    public String getPrevious() {
        return previous;
    }

    public List<Pokemon> getResults() {
        return results;
    }
}
