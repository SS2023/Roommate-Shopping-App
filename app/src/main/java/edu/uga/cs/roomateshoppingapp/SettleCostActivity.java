package edu.uga.cs.roomateshoppingapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SettleCostActivity extends AppCompatActivity {

    private Button clearButton;
    private TextView totalCost;
    private TextView avgCost;
    private TextView userTotals;

    private List<Item> purchasedItemsList;
    private int userCount;
    private double finalTotal;
    private final HashMap<String, Double> map = new HashMap<>();;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        userCount();
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_settle_cost );

        clearButton = findViewById( R.id.clear );
        clearButton.setOnClickListener( new clearButtonClickListener() );

        totalCost = findViewById( R.id.total);
        avgCost = findViewById( R.id.avg);
        userTotals = findViewById( R.id.userTotal);

        // get a Firebase DB instance reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("purchaseditems");
        purchasedItemsList = new ArrayList<Item>();

        // Set up a listener (event handler) to receive a value for the database reference, but only one time.
        // This type of listener is called by Firebase once by immediately executing its onDataChange method.
        // We can use this listener to retrieve the current list of JobLeads.
        // Other types of Firebase listeners may be set to listen for any and every change in the database
        // i.e., receive notifications about changes in the data in real time (hence the name, Realtime database).
        // This listener will be invoked asynchronously, as no need for an AsyncTask, as in the previous apps
        // to maintain job leads.
        myRef.addListenerForSingleValueEvent( new ValueEventListener() {

            @Override
            public void onDataChange( DataSnapshot snapshot ) {
                // Once we have a DataSnapshot object, knowing that this is a list,
                // we need to iterate over the elements and place them on a List.
                for( DataSnapshot postSnapshot: snapshot.getChildren() ) {
                    Item shoppingItem = postSnapshot.getValue(Item.class);
                    purchasedItemsList.add(shoppingItem);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        } );

        FirebaseDatabase.getInstance().getReference().child("purchaseditems")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Item item = snapshot.getValue(Item.class);
                            if (!map.containsKey(item.getWhoPurchased())) {
                                map.put(item.getWhoPurchased(), item.getPrice());
                                finalTotal += item.getPrice();
                            } else {
                                map.put(item.getWhoPurchased(), map.get(item.getWhoPurchased()) + item.getPrice());
                                finalTotal += item.getPrice();
                            }
                        }
                        settle();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

    }

    private class clearButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            // delete all of it
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("purchaseditems");
            myRef.removeValue();

            Toast.makeText(getApplicationContext(), "Deleted Purchased List",
                    Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(view.getContext(), HomePageActivity.class);
            view.getContext().startActivity( intent );
        }
    }

    private void userCount() {
        FirebaseDatabase.getInstance().getReference().child("users")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int count=0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            count++;
                        }
                        userCount = count;
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }


    private void settle() {
        finalTotal = (int)(finalTotal * 100);
        finalTotal = finalTotal / 100;
        totalCost.setText("Total Cost: $" + finalTotal);

        double avCost = (finalTotal / userCount);

        avCost = (int)(avCost * 100);
        avCost = avCost / 100;
        avgCost.setText("Average Cost: $" + avCost);

        String perPerson = "";

        Iterator mapIterator = map.entrySet().iterator();

        while (mapIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry)mapIterator.next();
            double marks = ((double)mapElement.getValue());
            marks = (int)(marks * 100);
            marks = marks / 100;
            perPerson = perPerson + (mapElement.getKey() + " spent $" + marks) + "\n";
        }
        userTotals.setText(perPerson);
    }
}