package com.example.pokenavigation.data.repository;

import com.example.pokenavigation.data.model.PokemonDetail;
import com.example.pokenavigation.data.model.PokemonResponse;
import com.example.pokenavigation.data.remote.PokeApiService;
import com.example.pokenavigation.data.remote.RetrofitClient;
import retrofit2.Call;

public class PokemonRepository {
    private final PokeApiService apiService;

    public PokemonRepository() {
        apiService = RetrofitClient.getService();
    }

    public Call<PokemonResponse> obtenerPokemon(int limit, int offset) {
        return apiService.getPokemon(limit, offset);
    }

    public Call<PokemonDetail> obtenerDetallePokemon(String name) {
        return apiService.getPokemonDetail(name);
    }
}