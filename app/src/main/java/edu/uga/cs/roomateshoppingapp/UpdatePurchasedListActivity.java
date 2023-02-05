package edu.uga.cs.roomateshoppingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdatePurchasedListActivity extends AppCompatActivity {
    private TextView itemNameView;
    private TextView itemQuantityView;
    private TextView itemPriceView;

    Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_purchased_list);
        item = (Item) getIntent().getSerializableExtra("obj");

        Button editButton = findViewById(R.id.purchasedEditButton);
        Button undoButton = findViewById(R.id.purchasedUndoButton);

        itemNameView = (TextView) findViewById(R.id.itemName3);
        itemQuantityView = (TextView) findViewById(R.id.itemQuantity3);
        itemPriceView = (TextView) findViewById(R.id.price3);

        editButton.setOnClickListener(new UpdatePurchasedListActivity.editButtonClickListener());
        undoButton.setOnClickListener(new UpdatePurchasedListActivity.undoButtonClickListener());

        itemNameView.setText(item.getItemName());
        itemQuantityView.setText(item.getItemQuantity());
        itemPriceView.setText(item.getPrice().toString());

    }

    private class editButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), UpdatePurchasedItemActivity.class);
            intent.putExtra("obj", item);
            view.getContext().startActivity(intent);
        }
    }

    private class undoButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {


            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("purchaseditems");

            myRef.orderByChild("itemName").equalTo(item.getItemName()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        postSnapshot.getRef().removeValue();

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            DatabaseReference myRef2 = database.getReference("shoppingitems");

            myRef2.push().setValue(item)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Moved " + item.getItemName() + " to Shopping List",
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed to create an Item for " + item.getItemName(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

            Intent intent = new Intent(view.getContext(), PurchasedListActivity.class);
            view.getContext().startActivity(intent);
        }
    }
}