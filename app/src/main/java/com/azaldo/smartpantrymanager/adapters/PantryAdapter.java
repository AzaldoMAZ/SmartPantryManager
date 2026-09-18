package com.azaldo.smartpantrymanager.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.azaldo.smartpantrymanager.R;
import com.azaldo.smartpantrymanager.models.PantryItem;

import java.util.List;

/**
 * Binds a List<PantryItem> to the Pantry List RecyclerView. Click handling
 * is delegated back to the hosting Activity through the OnPantryItemClickListener
 * interface, keeping this class free of navigation/database concerns.
 */
public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    /** Callback interface so MainActivity decides what edit/delete taps do. */
    public interface OnPantryItemClickListener {
        void onEditClicked(PantryItem item);

        void onDeleteClicked(PantryItem item);
    }

    private final List<PantryItem> items;
    private final OnPantryItemClickListener listener;

    public PantryAdapter(List<PantryItem> items, OnPantryItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);
        return new PantryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        PantryItem item = items.get(position);

        holder.textItemName.setText(item.getName());

        // Quantity is stored as a double but whole numbers should display
        // without a trailing ".0" (e.g. "500 g", not "500.0 g").
        String quantityText;
        if (item.getQuantity() == Math.floor(item.getQuantity())) {
            quantityText = ((long) item.getQuantity()) + " " + item.getUnit();
        } else {
            quantityText = item.getQuantity() + " " + item.getUnit();
        }
        holder.textItemQuantity.setText(quantityText);

        if (item.getExpiryDate() != null && !item.getExpiryDate().isEmpty()) {
            holder.textItemExpiry.setVisibility(View.VISIBLE);
            holder.textItemExpiry.setText("Expires " + item.getExpiryDate());
        } else {
            holder.textItemExpiry.setVisibility(View.GONE);
        }

        holder.buttonEditItem.setOnClickListener(v -> listener.onEditClicked(item));
        holder.buttonDeleteItem.setOnClickListener(v -> listener.onDeleteClicked(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /** Replaces the adapter's data (e.g. after a CRUD operation) and refreshes the list. */
    public void updateData(List<PantryItem> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    static class PantryViewHolder extends RecyclerView.ViewHolder {
        TextView textItemName;
        TextView textItemQuantity;
        TextView textItemExpiry;
        View buttonEditItem;
        View buttonDeleteItem;

        PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            textItemName = itemView.findViewById(R.id.textItemName);
            textItemQuantity = itemView.findViewById(R.id.textItemQuantity);
            textItemExpiry = itemView.findViewById(R.id.textItemExpiry);
            buttonEditItem = itemView.findViewById(R.id.buttonEditItem);
            buttonDeleteItem = itemView.findViewById(R.id.buttonDeleteItem);
        }
    }
}
