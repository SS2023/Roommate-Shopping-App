package edu.uga.cs.roomateshoppingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
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

public class UpdatePurchasedItemActivity extends AppCompatActivity {

    private EditText itemNameView;
    private EditText itemTotalView;
    private Button updateButton;
    Item item;
    private String itemName;
    private Double itemTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_shopping_item);
        item = (Item)getIntent().getSerializableExtra("obj");
        itemNameView = (EditText) findViewById(R.id.updateItemName);
        itemTotalView = (EditText) findViewById(R.id.updateItemQuantity);
        itemTotalView.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        itemTotalView.setHint("Enter Updated Total Price");

        updateButton = (Button) findViewById( R.id.update2 );

        updateButton.setOnClickListener( new UpdatePurchasedItemActivity.ButtonClickListener()) ;
    }

    class ButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick (View v){

            if (!itemTotalView.getText().toString().matches("")) {
                itemTotal = Double.parseDouble(itemTotalView.getText().toString());
                itemName = itemNameView.getText().toString();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("purchaseditems");
                myRef.orderByChild("itemName").equalTo(item.getItemName()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            if (item.getItemName() != itemName && !itemName.equals("")) {
                                postSnapshot.getRef().child("itemName").setValue(itemName);
                            }
                            if (item.getPrice() != itemTotal) {
                                postSnapshot.getRef().child("price").setValue(itemTotal);
                            }
                            break;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                Toast.makeText(getApplicationContext(), "Updated " + item.getItemName(),
                        Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(v.getContext(), PurchasedListActivity.class);
                v.getContext().startActivity(intent);

            } else {
                Toast.makeText(getApplicationContext(), "Enter at least Price for " + item.getItemName(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}