package edu.uga.cs.roomateshoppingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddShoppingItemActivity extends AppCompatActivity {
    public static final String DEBUG_TAG = "NewJobLeadActivity";

    private EditText itemNameView;
    private EditText itemQuantityView;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shopping_item);

        itemNameView = (EditText) findViewById(R.id.itemName);
        itemQuantityView = (EditText) findViewById(R.id.addItemQuantity);
        addButton = (Button) findViewById(R.id.add);

        addButton.setOnClickListener( new ButtonClickListener()) ;
    }

    private class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String itemName = itemNameView.getText().toString();
            String itemQuantity = itemQuantityView.getText().toString();
            Double itemPrice = 0.00;

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String whoAdded = user.getEmail();
            String whoPurchased = "User";

            if (!itemName.equals("")) {
                final Item item = new Item(itemName, itemQuantity, itemPrice, whoAdded, whoPurchased);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("shoppingitems");

                myRef.push().setValue(item)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Item created for " + item.getItemName(),
                                        Toast.LENGTH_SHORT).show();
                                itemNameView.setText("");
                                itemQuantityView.setText("");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(getApplicationContext(), "Failed to create a Item for " + item.getItemName(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(getApplicationContext(), "Please Input a Name",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}