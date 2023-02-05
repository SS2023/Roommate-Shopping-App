package edu.uga.cs.roomateshoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomePageActivity extends AppCompatActivity {

    private Button shoppingListButton;
    private Button purchasedListButton;
    private Button logoutButton;
    private TextView signedInTextView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        shoppingListButton = findViewById(R.id.shoppingList);
        purchasedListButton = findViewById(R.id.purchasedList);
        logoutButton = findViewById(R.id.logout);
        signedInTextView = findViewById(R.id.user);

        shoppingListButton.setOnClickListener(new ShoppingListButtonClickListener());
        purchasedListButton.setOnClickListener(new PurchasedListButtonClickListener());
        logoutButton.setOnClickListener(new LogoutButtonClickListener());

        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                if( currentUser != null ) {
                    // User is signed in
                    String userEmail = currentUser.getEmail();
                    signedInTextView.setText( "Signed in as: " + userEmail );
                } else {
                    // User is signed out
                    signedInTextView.setText( "Signed in as: not signed in" );
                }
            }
        });
    }

    private class ShoppingListButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), ShoppingListActivity.class);
            view.getContext().startActivity(intent);
        }
    }

    private class PurchasedListButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), PurchasedListActivity.class);
            view.getContext().startActivity(intent);
        }
    }

    private class LogoutButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
            Intent intent = new Intent(view.getContext(), MainActivity.class);
            view.getContext().startActivity(intent);
        }
    }
}
