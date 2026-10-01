package com.example.pokenavigation.data.remote;

import com.example.pokenavigation.data.model.PokemonDetail;
import com.example.pokenavigation.data.model.PokemonResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PokeApiService {
    @GET("pokemon")
    Call<PokemonResponse> getPokemon(
            @Query("limit") int limit,
            @Query("offset") int offset
    );

    @GET("pokemon/{name}")
    Call<PokemonDetail> getPokemonDetail(@Path("name") String name);
}