package com.example.mediatracker20.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediatracker20.R;
import com.example.mediatracker20.adapters.sources.MediaSources;

import java.util.List;

public class SearchSourcesAdapter extends RecyclerView.Adapter<SearchSourcesAdapter.SearchSourcesViewHolder> {

    private List<MediaSources> allSources;

    public SearchSourcesAdapter(List<MediaSources> mediaSources) {
        this.allSources = mediaSources;
    }

    @NonNull
    @Override
    public SearchSourcesAdapter.SearchSourcesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.media_source_card, parent, false);
        SearchSourcesViewHolder vh = new SearchSourcesViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchSourcesAdapter.SearchSourcesViewHolder holder, int position) {
        MediaSources mediaSource = allSources.get(position);
        holder.sourceTitle.setText(mediaSource.getTitle());
        holder.sourceTitle.setCompoundDrawablesWithIntrinsicBounds(0, mediaSource.getImageId(), 0, 0);
        holder.cardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int actionId;
                switch (mediaSource.getType()) {
                    case "general":
                        //actionId =
                        break;
                    case "anime":
                        //actionId =
                        break;
                    case "manga":
                        //actionId =
                        break;
                    case "drama":
                        //actionId =
                        break;
                }
                //Navigation.findNavController(holder.itemView).navigate();
            }
        });
    }

    @Override
    public int getItemCount() {
        return allSources.size();
    }

    public class SearchSourcesViewHolder extends RecyclerView.ViewHolder {

        private TextView sourceTitle;
        private ConstraintLayout cardLayout;

        public SearchSourcesViewHolder(@NonNull View itemView) {
            super(itemView);
            sourceTitle = itemView.findViewById(R.id.textView2);
            cardLayout = itemView.findViewById(R.id.card_layout);
        }
    }
}
