package edu.uga.cs.roomateshoppingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateShoppingItemActivity extends AppCompatActivity {

    private EditText itemNameView;
    private EditText itemQuantityView;
    private Button saveButton;
    Item item;
    private String itemName;
    private String itemQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_shopping_item);
        item = (Item)getIntent().getSerializableExtra("obj");
        itemNameView = (EditText) findViewById(R.id.updateItemName);
        itemQuantityView = (EditText) findViewById(R.id.updateItemQuantity);
        saveButton = (Button) findViewById(R.id.update2);

        saveButton.setOnClickListener(new UpdateShoppingItemActivity.ButtonClickListener()) ;
    }

    class ButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick (View v){
            itemName = itemNameView.getText().toString();
            itemQuantity = itemQuantityView.getText().toString();

            if (!itemName.equals("")) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("shoppingitems");
                myRef.orderByChild("itemName").equalTo(item.getItemName()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            if (item.getItemName() != itemName) {
                                postSnapshot.getRef().child("itemName").setValue(itemName);
                            }
                            if (item.getItemQuantity() != itemQuantity) {
                                postSnapshot.getRef().child("itemQuantity").setValue(itemQuantity);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                Toast.makeText(getApplicationContext(), "Updated " + item.getItemName(),
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), ShoppingListActivity.class);
                v.getContext().startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Enter a New Name/Quantity for " + item.getItemName(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}