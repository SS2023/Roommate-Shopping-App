package edu.uga.cs.roomateshoppingapp;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PurchasedListRecyclerAdapter extends RecyclerView.Adapter<PurchasedListRecyclerAdapter.PurchasedItemHolder> {

    private List<Item> purchasedItemList;

    public PurchasedListRecyclerAdapter( List<Item> shoppingItemList ) {
        this.purchasedItemList = shoppingItemList;
    }

    class PurchasedItemHolder extends RecyclerView.ViewHolder {

        TextView itemNameView;
        TextView itemQuantityView;
        TextView itemPriceView;
        Button editButton;

        public PurchasedItemHolder(View itemView ) {
            super(itemView);

            itemNameView = (TextView) itemView.findViewById( R.id.purchasedItemName );
            itemQuantityView = (TextView) itemView.findViewById( R.id.purchasedItemQuantity );
            itemPriceView = (TextView) itemView.findViewById( R.id.purchasedPrice);
            editButton = (Button) itemView.findViewById( R.id.purchasedEdit);
        }
    }

    @Override
    public PurchasedItemHolder onCreateViewHolder(ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.purchased_item, parent, false );
        return new PurchasedItemHolder( view );
    }

    @Override
    public void onBindViewHolder(PurchasedItemHolder holder, final int position ) {
        final Item item = purchasedItemList.get( position );

        holder.itemNameView.setText(item.getItemName());
        holder.itemQuantityView.setText(item.getItemQuantity());

        holder.itemPriceView.setText( item.getPrice().toString());

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), UpdatePurchasedListActivity.class);
                intent.putExtra("obj", item);
                view.getContext().startActivity( intent );
            }
        });
    }

    @Override
    public int getItemCount() {
        return purchasedItemList.size();
    }
}