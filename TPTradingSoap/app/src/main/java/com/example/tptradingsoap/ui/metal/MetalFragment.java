package com.example.tptradingsoap.ui.metal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tptradingsoap.MainActivity;
import com.example.tptradingsoap.databinding.FragmentMetalBinding;
import com.example.tptradingsoap.ui.metal.metalhelper.MetalDto;

import java.util.ArrayList;

public class MetalFragment extends Fragment {

    private FragmentMetalBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MetalViewModel metalViewModel =
                new ViewModelProvider(this, new ViewModelProvider.Factory() {
                    @NonNull
                    @Override
                    public <T extends ViewModel> T create(Class<T> modelClass) {
                        if (modelClass.isAssignableFrom(MetalViewModel.class)) {
                            return (T) new MetalViewModel(getContext());
                        }
                        throw new IllegalArgumentException("Unknown ViewModel class");
                    }
                }).get(MetalViewModel.class);

        binding = FragmentMetalBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final RecyclerView listeMetal = binding.listeMetal;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listeMetal.setLayoutManager(linearLayoutManager);
        metalViewModel.setRecyclerAdapter().observe(getViewLifecycleOwner(),listeMetal::setAdapter);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}