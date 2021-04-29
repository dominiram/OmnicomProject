package app.appworks.myapplication.views;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavAction;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.Navigator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import app.appworks.myapplication.MainActivity;
import app.appworks.myapplication.R;
import app.appworks.myapplication.viewmodels.MainViewModel;

public class MainFragment extends Fragment {

    private View rootView;
    private NavController navController;
    private MainViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.main_fragment, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        rootView.findViewById(R.id.btn).setOnClickListener(v -> checkPassword());
    }

    private void checkPassword() {
        final EditText etPassword = rootView.findViewById(R.id.et_password);
        final String password = etPassword.getText().toString();

        if(password.length() > 0 &&
                Character.isUpperCase(password.charAt(0)) &&
                Character.isUpperCase(password.charAt(password.length() - 1))) {

            final NavDirections direction =
                    MainFragmentDirections
                            .actionMainFragmentToMapsFragment(password);
            navController.navigate(direction);
        }
        else {
            Toast.makeText(
                    requireContext(),
                    "Password is not in the right format!",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }

}