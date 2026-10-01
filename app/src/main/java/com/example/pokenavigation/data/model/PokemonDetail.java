package com.example.pokenavigation.data.model;

import com.google.gson.annotations.SerializedName;

public class PokemonDetail {
    private int id;
    private String name;

    @SerializedName("base_experience")
    private int baseExperience;

    private int height;

    @SerializedName("is_default")
    private boolean isDefault;

    private int order;
    private int weight;

    public int getId() {
        return id;
    }

    public String getName() {

        return name;
    }

    public int getBaseExperience() {
        return baseExperience;
    }

    public int getHeight() {
        return height;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public int getOrder() {
        return order;
    }

    public int getWeight() {
        return weight;
    }
}
