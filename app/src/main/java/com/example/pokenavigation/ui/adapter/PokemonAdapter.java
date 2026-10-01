package com.example.pokenavigation.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pokenavigation.R;
import com.example.pokenavigation.data.model.Pokemon;
import java.util.ArrayList;
import java.util.List;

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder> {

    public interface OnPokemonClickListener {
        void onPokemonClick(Pokemon pokemon);
    }

    private final List<Pokemon> pokemonList = new ArrayList<>();
    private final OnPokemonClickListener listener;

    public PokemonAdapter(OnPokemonClickListener listener) {
        this.listener = listener;
    }

    public void actualizarDatos(List<Pokemon> nuevosPokemon) {
        pokemonList.clear();
        if (nuevosPokemon != null) {
            pokemonList.addAll(nuevosPokemon);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PokemonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pokemon, parent, false);
        return new PokemonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PokemonViewHolder holder, int position) {
        holder.bind(pokemonList.get(position));
    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    class PokemonViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvPokemonName;
        private final TextView tvPokemonUrl;

        public PokemonViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPokemonName = itemView.findViewById(R.id.tvPokemonName);
            tvPokemonUrl = itemView.findViewById(R.id.tvPokemonUrl);
        }

        public void bind(Pokemon pokemon) {
            tvPokemonName.setText(pokemon.getName());
            tvPokemonUrl.setText(pokemon.getUrl());
            itemView.setOnClickListener(v -> listener.onPokemonClick(pokemon));
        }
    }
}
