package com.example.pokenavigation.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pokenavigation.R;
import com.example.pokenavigation.data.model.PokemonDetail;
import com.example.pokenavigation.data.repository.PokemonRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoFragment extends Fragment {
    private static final String ARG_POKEMON_NAME = "pokemon_name";

    private CircularProgressIndicator progressIndicator;
    private LinearLayout errorContainer;
    private TextView tvError;
    private MaterialButton btnRetry;
    private MaterialCardView cardDetail;
    private TextView tvEmptyState;

    private TextView tvPokemonName;
    private TextView tvPokemonId;
    private TextView tvBaseExperience;
    private TextView tvHeight;
    private TextView tvWeight;
    private TextView tvOrder;
    private TextView tvIsDefault;

    private PokemonRepository repository;
    private Call<PokemonDetail> currentCall;
    private String pokemonName;

    public InfoFragment() {
        super(R.layout.fragment_info);
    }

    public static InfoFragment newInstance(String pokemonName) {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_POKEMON_NAME, pokemonName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressIndicator = view.findViewById(R.id.progressIndicator);
        errorContainer = view.findViewById(R.id.errorContainer);
        tvError = view.findViewById(R.id.tvError);
        btnRetry = view.findViewById(R.id.btnRetry);
        cardDetail = view.findViewById(R.id.cardDetail);
        tvEmptyState = view.findViewById(R.id.tvEmptyState);

        tvPokemonName = view.findViewById(R.id.tvPokemonName);
        tvPokemonId = view.findViewById(R.id.tvPokemonId);
        tvBaseExperience = view.findViewById(R.id.tvBaseExperience);
        tvHeight = view.findViewById(R.id.tvHeight);
        tvWeight = view.findViewById(R.id.tvWeight);
        tvOrder = view.findViewById(R.id.tvOrder);
        tvIsDefault = view.findViewById(R.id.tvIsDefault);

        repository = new PokemonRepository();

        if (getArguments() != null) {
            pokemonName = getArguments().getString(ARG_POKEMON_NAME);
        }

        btnRetry.setOnClickListener(v -> cargarDetallePokemon());

        if (pokemonName != null && !pokemonName.isEmpty()) {
            cargarDetallePokemon();
        } else {
            mostrarEstadoVacio();
        }
    }

    private void cargarDetallePokemon() {
        mostrarCargando();
        currentCall = repository.obtenerDetallePokemon(pokemonName);

        currentCall.enqueue(new Callback<PokemonDetail>() {
            @Override
            public void onResponse(@NonNull Call<PokemonDetail> call,
                                   @NonNull Response<PokemonDetail> response) {
                if (!isAdded()) return;

                PokemonDetail detail = response.body();
                if (response.isSuccessful() && detail != null) {
                    mostrarDetalle(detail);
                } else {
                    mostrarError("No se pudo obtener la información de " + pokemonName + ". Código HTTP: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<PokemonDetail> call, @NonNull Throwable throwable) {
                if (call.isCanceled() || !isAdded()) return;
                mostrarError("Error de conexión. Verifique internet e intente nuevamente.");
            }
        });
    }

    private void mostrarDetalle(PokemonDetail detail) {
        progressIndicator.setVisibility(View.GONE);
        errorContainer.setVisibility(View.GONE);
        tvEmptyState.setVisibility(View.GONE);
        cardDetail.setVisibility(View.VISIBLE);

        tvPokemonName.setText(capitalize(detail.getName()));
        tvPokemonId.setText("#" + detail.getId());
        tvBaseExperience.setText(String.valueOf(detail.getBaseExperience()));
        tvHeight.setText(detail.getHeight() + " dm");
        tvWeight.setText(detail.getWeight() + " hg");
        tvOrder.setText(String.valueOf(detail.getOrder()));
        tvIsDefault.setText(detail.isDefault() ? "Sí" : "No");
    }

    private void mostrarCargando() {
        progressIndicator.setVisibility(View.VISIBLE);
        errorContainer.setVisibility(View.GONE);
        cardDetail.setVisibility(View.GONE);
        tvEmptyState.setVisibility(View.GONE);
    }

    private void mostrarEstadoVacio() {
        progressIndicator.setVisibility(View.GONE);
        errorContainer.setVisibility(View.GONE);
        cardDetail.setVisibility(View.GONE);
        tvEmptyState.setVisibility(View.VISIBLE);
    }

    private void mostrarError(String mensaje) {
        progressIndicator.setVisibility(View.GONE);
        cardDetail.setVisibility(View.GONE);
        tvEmptyState.setVisibility(View.GONE);
        errorContainer.setVisibility(View.VISIBLE);
        tvError.setText(mensaje);
    }

    private String capitalize(String text) {
        if (text == null || text.isEmpty()) return "";
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    @Override
    public void onDestroyView() {
        if (currentCall != null) currentCall.cancel();
        super.onDestroyView();
    }
}
