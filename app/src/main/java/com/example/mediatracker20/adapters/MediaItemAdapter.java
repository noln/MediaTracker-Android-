package com.example.mediatracker20.adapters;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.mediatracker20.R;

import java.util.List;

import model.exceptions.ItemNotFoundException;
import model.model.ListManager;
import model.model.MediaItem;
import model.model.MediaList;

public class MediaItemAdapter extends RecyclerView.Adapter<MediaItemAdapter.MediaItemViewHolder> {
    private static List<MediaItem> itemsList;
    private static ListManager listManager;
    private MediaList selectedList;

    public class MediaItemViewHolder extends RecyclerView.ViewHolder {

        public TextView itemTitle;
        public TextView itemRating;
        public TextView itemStatus;
        public ImageButton itemOptions;
        public ImageView itemBanner;


        public MediaItemViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public MediaItemAdapter.MediaItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       // View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
       // MediaItemAdapter.MediaItemViewHolder evh = new MediaItemAdapter.MediaItemViewHolder(v);
        return null; //evh;
    }

    @Override
    public void onBindViewHolder(@NonNull MediaItemAdapter.MediaItemViewHolder holder, int position) {
        MediaItem item = itemsList.get(position);
        holder.itemRating.setText( item.getItemInfo("Rating"));
        holder.itemStatus.setText(item.getItemInfo("Status"));
        holder.itemTitle.setText(item.getItemInfo("Title"));
        //holder.itemBanner. load with picasso later
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public MediaItemAdapter(List<MediaItem> itemsList, MediaList mediaList) {
        this.itemsList = itemsList;
        listManager = ListManager.getInstance();
        this.selectedList = mediaList;
    }

}
