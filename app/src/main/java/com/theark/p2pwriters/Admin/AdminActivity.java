package com.theark.p2pwriters.Admin;

// Your package declaration here

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.theark.p2pwriters.R;

import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

public class AdminActivity extends AppCompatActivity {

    private Toolbar topToolbar;
    private BottomNavigationView bottomNavigationView;
    private TextView FragmentTitle;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        topToolbar = findViewById(R.id.topToolbar);
        setSupportActionBar(topToolbar);
        getSupportActionBar().setTitle("Admin Control");
        mAuth= FirebaseAuth.getInstance();
        FragmentTitle =(TextView)findViewById(R.id.FragmentTitle);
        bottomNavigationView= (BottomNavigationView)findViewById(R.id.bottomNavigationView);

        //default fragment is product (awl ma y sign in go to products fragment)
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new BooksFragment()).commit();
        FragmentTitle.setText("All Books");
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment SelectedFragment=null;
            int id = item.getItemId();
            if(id==R.id.action_books){
                SelectedFragment = new BooksFragment();
                FragmentTitle.setText("All Books");
            }
            else if(id==R.id.action_reports){
                SelectedFragment = new ReportsFragment();
                FragmentTitle.setText("All Reports");
            }
            else if(id==R.id.action_sales){
                SelectedFragment = new SalesFragment();
                FragmentTitle.setText("All Sales");
            }
            else if(id==R.id.action_category){
                SelectedFragment = new CategoryFragment();
                FragmentTitle.setText("Add Category");
            }
            else if(id==R.id.action_salesperson){
                SelectedFragment = new SalespersonFragment();
                FragmentTitle.setText("All SalesMen");
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.container,SelectedFragment).commit();
            return true;
        });

        // Load the default fragment
        loadFragment(new BooksFragment());
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }
}
