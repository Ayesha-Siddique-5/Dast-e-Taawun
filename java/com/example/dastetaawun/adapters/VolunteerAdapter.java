package com.example.dastetaawun.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dastetaawun.R;
import com.example.dastetaawun.models.Volunteer;
import java.util.List;

public class VolunteerAdapter extends RecyclerView.Adapter<VolunteerAdapter.VolunteerViewHolder> {
    private Context context;
    private List<Volunteer> volunteerList;

    public VolunteerAdapter(Context context, List<Volunteer> volunteerList) {
        this.context = context;
        this.volunteerList = volunteerList;
    }

    @NonNull
    @Override
    public VolunteerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_volunteer, parent, false);
        return new VolunteerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VolunteerViewHolder holder, int position) {
        Volunteer volunteer = volunteerList.get(position);

        holder.nameTextView.setText(volunteer.getName());
        holder.skillsTextView.setText(volunteer.getSkillsAsString());
        holder.locationTextView.setText(volunteer.getLocation() != null ? volunteer.getLocation() : "Not specified");
        holder.availabilityTextView.setText(volunteer.getAvailability() != null ? volunteer.getAvailability() : "Not specified");
    }

    @Override
    public int getItemCount() {
        return volunteerList.size();
    }

    public static class VolunteerViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, skillsTextView, locationTextView, availabilityTextView;

        public VolunteerViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            skillsTextView = itemView.findViewById(R.id.skillsTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            availabilityTextView = itemView.findViewById(R.id.availabilityTextView);
        }
    }
}