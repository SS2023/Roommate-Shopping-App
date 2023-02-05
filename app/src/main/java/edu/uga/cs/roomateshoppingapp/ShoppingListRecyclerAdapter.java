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

public class ShoppingListRecyclerAdapter extends RecyclerView.Adapter<ShoppingListRecyclerAdapter.ShoppingItemHolder> {

    private List<Item> shoppingItemList;

    public ShoppingListRecyclerAdapter( List<Item> shoppingItemList ) {
        this.shoppingItemList = shoppingItemList;
    }

    class ShoppingItemHolder extends RecyclerView.ViewHolder {

        TextView itemNameView;
        TextView itemQuantityView;
        Button editButton;

        public ShoppingItemHolder(View itemView ) {
            super(itemView);
            itemNameView = (TextView) itemView.findViewById( R.id.itemName);
            itemQuantityView = (TextView) itemView.findViewById( R.id.itemQuantity );
            editButton = (Button) itemView.findViewById(R.id.edit);
        }
    }

    @Override
    public ShoppingItemHolder onCreateViewHolder(ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.shopping_item, parent, false );
        return new ShoppingItemHolder( view );
    }

    @Override
    public void onBindViewHolder(ShoppingItemHolder holder, final int position ) {
        final Item item = shoppingItemList.get( position );

        holder.itemNameView.setText(item.getItemName());
        holder.itemQuantityView.setText(item.getItemQuantity());

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), UpdateShoppingListActivity.class);
                intent.putExtra("obj", item);
                view.getContext().startActivity( intent );
            }
        });
    }

    @Override
    public int getItemCount() {
        return shoppingItemList.size();
    }
}
