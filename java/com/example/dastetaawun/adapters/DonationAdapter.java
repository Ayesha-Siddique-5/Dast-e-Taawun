package com.example.dastetaawun.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dastetaawun.R;
import com.example.dastetaawun.models.Donation;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class DonationAdapter extends RecyclerView.Adapter<DonationAdapter.DonationViewHolder> {
    private Context context;
    private List<Donation> donationList;
    private SimpleDateFormat dateFormat;

    public DonationAdapter(Context context, List<Donation> donationList) {
        this.context = context;
        this.donationList = donationList;
        this.dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
    }

    @NonNull
    @Override
    public DonationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_donation, parent, false);
        return new DonationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonationViewHolder holder, int position) {
        Donation donation = donationList.get(position);

        // Format amount
        String formattedAmount = String.format(Locale.getDefault(), "PKR %.0f", donation.getAmount());
        holder.amountTextView.setText(formattedAmount);

        // Set recipient name
        holder.recipientTextView.setText(donation.getRecipientName() != null ?
                donation.getRecipientName() : "General Fund");

        // Format and set date
        if (donation.getDonationDate() != null) {
            holder.dateTextView.setText(dateFormat.format(donation.getDonationDate().toDate()));
        } else {
            holder.dateTextView.setText("N/A");
        }

        // Set payment method
        holder.paymentMethodTextView.setText(donation.getPaymentMethod() != null ?
                donation.getPaymentMethod() : "Not specified");
    }

    @Override
    public int getItemCount() {
        return donationList.size();
    }

    public static class DonationViewHolder extends RecyclerView.ViewHolder {
        TextView amountTextView, recipientTextView, dateTextView, paymentMethodTextView;

        public DonationViewHolder(@NonNull View itemView) {
            super(itemView);
            amountTextView = itemView.findViewById(R.id.amountTextView);
            recipientTextView = itemView.findViewById(R.id.recipientTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            paymentMethodTextView = itemView.findViewById(R.id.paymentMethodTextView);
        }
    }
}