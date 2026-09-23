package com.azaldo.smartpantrymanager.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.azaldo.smartpantrymanager.R;
import com.azaldo.smartpantrymanager.models.PantryItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Binds a List<PantryItem> to the Pantry List RecyclerView. Click handling
 * is delegated back to the hosting Activity through the OnPantryItemClickListener
 * interface, keeping this class free of navigation/database concerns.
 *
 * Also implements the "expiry alerts" Settings toggle: when enabled, items
 * expiring within EXPIRY_SOON_THRESHOLD_DAYS are highlighted. When
 * disabled, expiry dates still show but with no alert styling - this is
 * what makes that Settings toggle genuinely functional rather than a
 * stored value nothing else reads.
 */
public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    private static final int EXPIRY_SOON_THRESHOLD_DAYS = 3;

    /** Callback interface so MainActivity decides what edit/delete taps do. */
    public interface OnPantryItemClickListener {
        void onEditClicked(PantryItem item);

        void onDeleteClicked(PantryItem item);
    }

    private final List<PantryItem> items;
    private final OnPantryItemClickListener listener;
    private boolean expiryAlertsEnabled = true;

    public PantryAdapter(List<PantryItem> items, OnPantryItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    /** Set from MainActivity's SettingsRepository before each refresh. */
    public void setExpiryAlertsEnabled(boolean expiryAlertsEnabled) {
        this.expiryAlertsEnabled = expiryAlertsEnabled;
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

        bindExpiryText(holder, item);

        holder.buttonEditItem.setOnClickListener(v -> listener.onEditClicked(item));
        holder.buttonDeleteItem.setOnClickListener(v -> listener.onDeleteClicked(item));
    }

    private void bindExpiryText(PantryViewHolder holder, PantryItem item) {
        if (item.getExpiryDate() == null || item.getExpiryDate().isEmpty()) {
            holder.textItemExpiry.setVisibility(View.GONE);
            return;
        }

        holder.textItemExpiry.setVisibility(View.VISIBLE);
        boolean soon = expiryAlertsEnabled && isExpiringSoon(item.getExpiryDate());

        if (soon) {
            holder.textItemExpiry.setText("Expiring soon: " + item.getExpiryDate());
            holder.textItemExpiry.setTextColor(
                    ContextCompat.getColor(holder.itemView.getContext(), R.color.error_red));
        } else {
            holder.textItemExpiry.setText("Expires " + item.getExpiryDate());
            holder.textItemExpiry.setTextColor(
                    ContextCompat.getColor(holder.itemView.getContext(), R.color.pantry_orange));
        }
    }

    private boolean isExpiringSoon(String expiryDate) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date expiry = format.parse(expiryDate);
            if (expiry == null) {
                return false;
            }
            long diffMillis = expiry.getTime() - System.currentTimeMillis();
            long diffDays = diffMillis / (1000L * 60 * 60 * 24);
            return diffDays <= EXPIRY_SOON_THRESHOLD_DAYS;
        } catch (ParseException e) {
            return false;
        }
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

