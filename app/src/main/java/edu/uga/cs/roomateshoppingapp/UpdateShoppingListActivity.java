package edu.uga.cs.roomateshoppingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateShoppingListActivity extends AppCompatActivity {

    private TextView itemName;
    private TextView itemQuantity;

    Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_shopping_list);
        item = (Item)getIntent().getSerializableExtra("obj");


        Button updateButton = findViewById( R.id.update );
        Button purchaseButton = findViewById( R.id.purchase);
        Button deleteButton = findViewById( R.id.delete);

        itemName = (TextView) findViewById(R.id.itemName2);
        itemQuantity = (TextView) findViewById(R.id.itemQuantity2);

        updateButton.setOnClickListener( new UpdateShoppingListActivity.UpdateButtonClickListener() );
        purchaseButton.setOnClickListener( new UpdateShoppingListActivity.PurchaseButtonClickListener() );
        deleteButton.setOnClickListener( new UpdateShoppingListActivity.DeleteButtonClickListener() );

        itemName.setText(item.getItemName());
        itemQuantity.setText(item.getItemQuantity());

    }

    private class UpdateButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick( View view ) {
            Intent intent = new Intent(view.getContext(), UpdateShoppingItemActivity.class);
            intent.putExtra("obj", item);
            view.getContext().startActivity( intent );
        }
    }

    private class PurchaseButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), PurchaseItemActivity.class);
            intent.putExtra("obj", item);
            view.getContext().startActivity( intent );
        }
    }

    private class DeleteButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("shoppingitems");
            myRef.orderByChild("itemName").equalTo(item.getItemName()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        postSnapshot.getRef().removeValue();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            Toast.makeText(getApplicationContext(), "Deleted " + item.getItemName(),
                    Toast.LENGTH_SHORT).show();
        }
    }
}