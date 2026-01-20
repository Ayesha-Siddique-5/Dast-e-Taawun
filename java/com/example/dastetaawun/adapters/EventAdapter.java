package com.example.dastetaawun.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dastetaawun.R;
import com.example.dastetaawun.models.Event;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private Context context;
    private List<Event> eventList;
    private SimpleDateFormat dateFormat;

    public EventAdapter(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
        this.dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);

        holder.titleTextView.setText(event.getTitle());
        holder.descriptionTextView.setText(event.getDescription());
        holder.locationTextView.setText(event.getLocation());

        if (event.getDate() != null) {
            holder.dateTextView.setText(dateFormat.format(event.getDate().toDate()));
        }

        int progress = event.getProgressPercentage();
        holder.progressBar.setProgress(progress);
        holder.progressTextView.setText(progress + "% Complete");

        holder.amountTextView.setText("PKR " + String.format(Locale.getDefault(), "%.0f", event.getCurrentAmount()) +
                " / " + String.format(Locale.getDefault(), "%.0f", event.getTargetAmount()));
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, descriptionTextView, locationTextView, dateTextView;
        TextView progressTextView, amountTextView;
        ProgressBar progressBar;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            progressTextView = itemView.findViewById(R.id.progressTextView);
            progressBar = itemView.findViewById(R.id.progressBar);
            amountTextView = itemView.findViewById(R.id.amountTextView);
        }
    }
}


