package edu.uga.cs.roomateshoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PurchasedListActivity extends AppCompatActivity {

    private Button settleCostButton;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter recyclerAdapter;

    private List<Item> purchasedItemsList;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_purchased_list );

        settleCostButton = findViewById(R.id.settleCostButton);
        settleCostButton.setOnClickListener(new SettleCostButtonClickListener());

        recyclerView = (RecyclerView) findViewById(R.id.purchasedListRecyclerView);

        layoutManager = new LinearLayoutManager(this );
        recyclerView.setLayoutManager(layoutManager);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("purchaseditems");
        purchasedItemsList = new ArrayList<Item>();


        myRef.addListenerForSingleValueEvent( new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for( DataSnapshot postSnapshot: snapshot.getChildren() ) {
                    Item shoppingItem = postSnapshot.getValue(Item.class);
                    purchasedItemsList.add(shoppingItem);
                }
                recyclerAdapter = new PurchasedListRecyclerAdapter(purchasedItemsList);
                recyclerView.setAdapter( recyclerAdapter );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        } );
    }

    private class SettleCostButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), SettleCostActivity.class);
            view.getContext().startActivity( intent );
        }
    }
}