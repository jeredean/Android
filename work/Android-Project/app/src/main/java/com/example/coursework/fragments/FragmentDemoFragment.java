package com.example.coursework.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.coursework.R;
import com.google.android.material.button.MaterialButton;

/** Task 2: a host screen that swaps independently managed child fragments. */
public final class FragmentDemoFragment extends Fragment {
    private static final String CHILD_TAG = "fragment_demo_child";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_demo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        MaterialButton firstButton = view.findViewById(R.id.show_first_fragment);
        MaterialButton secondButton = view.findViewById(R.id.show_second_fragment);
        firstButton.setOnClickListener(ignored -> showChild(new FirstContentFragment()));
        secondButton.setOnClickListener(ignored -> showChild(new SecondContentFragment()));

        if (savedInstanceState == null) {
            showChild(new FirstContentFragment());
        }
    }

    private void showChild(Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction()
                .replace(R.id.child_fragment_container, fragment, CHILD_TAG);
        transaction.commit();
    }
}
