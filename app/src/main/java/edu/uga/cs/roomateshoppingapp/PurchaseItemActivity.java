package edu.uga.cs.roomateshoppingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PurchaseItemActivity extends AppCompatActivity {

    private TextView itemNameView;
    private TextView itemQuantityView;
    private EditText itemPriceView;

    Item item;

    private Button purchaseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_item);
        item = (Item)getIntent().getSerializableExtra("obj");

        itemNameView = (TextView) findViewById(R.id.purchaseItemName);
        itemQuantityView = (TextView) findViewById(R.id.purchaseItemQuantity);

        itemNameView.setText("Purchase " + item.getItemName() + "?");
        itemQuantityView.setText(item.getItemQuantity());

        itemPriceView = (EditText) findViewById(R.id.purchasePrice);
        purchaseButton = (Button) findViewById(R.id.purchaseButton);


        purchaseButton.setOnClickListener( new PurchaseItemActivity.ButtonClickListener()) ;
    }


    class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("shoppingitems");

            myRef.orderByChild("itemName").equalTo(item.getItemName()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        if (!itemPriceView.getText().toString().matches("")) {
                            postSnapshot.getRef().removeValue();
                        } else {
                            Toast.makeText(getApplicationContext(), "Please Input a Price for " + item.getItemName(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            if (!itemPriceView.getText().toString().matches("")) {
                DatabaseReference myRef2 = database.getReference("purchaseditems");
                item.setPrice(Double.parseDouble(itemPriceView.getText().toString()));
                item.setItemName(item.getItemName() + " ");

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                item.setWhoPurchased(user.getEmail());

                myRef2.push().setValue(item)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Purchased Item created for " + item.getItemName(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(getApplicationContext(), "Failed to create a Job lead for " + item.getItemName(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

                Intent intent = new Intent(v.getContext(), ShoppingListActivity.class);
                v.getContext().startActivity(intent);
            }
        }
    }
}