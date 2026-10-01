package com.example.pokenavigation.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pokenavigation.MainActivity;
import com.example.pokenavigation.R;
import com.example.pokenavigation.data.model.Pokemon;
import com.example.pokenavigation.data.model.PokemonResponse;
import com.example.pokenavigation.data.repository.PokemonRepository;
import com.example.pokenavigation.ui.adapter.PokemonAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerPokemon;
    private CircularProgressIndicator progressIndicator;
    private LinearLayout errorContainer;
    private TextView tvError;
    private MaterialButton btnRetry;
    private PokemonAdapter adapter;
    private PokemonRepository repository;
    private Call<PokemonResponse> currentCall;

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerPokemon = view.findViewById(R.id.recyclerPokemon);
        progressIndicator = view.findViewById(R.id.progressIndicator);
        errorContainer = view.findViewById(R.id.errorContainer);
        tvError = view.findViewById(R.id.tvError);
        btnRetry = view.findViewById(R.id.btnRetry);

        adapter = new PokemonAdapter(this::mostrarPokemonSeleccionado);
        repository = new PokemonRepository();

        recyclerPokemon.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerPokemon.setHasFixedSize(true);
        recyclerPokemon.setAdapter(adapter);

        btnRetry.setOnClickListener(v -> cargarPokemon());
        cargarPokemon();
    }

    private void cargarPokemon() {
        mostrarCargando();
        currentCall = repository.obtenerPokemon(30, 0);

        currentCall.enqueue(new Callback<PokemonResponse>() {
            @Override
            public void onResponse(@NonNull Call<PokemonResponse> call,
                                   @NonNull Response<PokemonResponse> response) {
                if (!isAdded()) return;

                PokemonResponse body = response.body();
                if (response.isSuccessful() && body != null && body.getResults() != null) {
                    adapter.actualizarDatos(body.getResults());
                    mostrarContenido();
                } else {
                    mostrarError("No fue posible obtener los Pokémon. Código HTTP: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<PokemonResponse> call, @NonNull Throwable throwable) {
                if (call.isCanceled() || !isAdded()) return;
                mostrarError("Error de conexión. Verifique internet e intente nuevamente.");
            }
        });
    }

    private void mostrarPokemonSeleccionado(Pokemon pokemon) {
        InfoFragment infoFragment = InfoFragment.newInstance(pokemon.getName());

        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, infoFragment)
                .addToBackStack(null)
                .commit();

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).marcarMenuInfo();
        }
    }

    private void mostrarCargando() {
        progressIndicator.setVisibility(View.VISIBLE);
        recyclerPokemon.setVisibility(View.GONE);
        errorContainer.setVisibility(View.GONE);
    }

    private void mostrarContenido() {
        progressIndicator.setVisibility(View.GONE);
        recyclerPokemon.setVisibility(View.VISIBLE);
        errorContainer.setVisibility(View.GONE);
    }

    private void mostrarError(String mensaje) {
        progressIndicator.setVisibility(View.GONE);
        recyclerPokemon.setVisibility(View.GONE);
        errorContainer.setVisibility(View.VISIBLE);
        tvError.setText(mensaje);
    }

    @Override
    public void onDestroyView() {
        if (currentCall != null) currentCall.cancel();
        if (recyclerPokemon != null) recyclerPokemon.setAdapter(null);
        super.onDestroyView();
    }
}
