package com.example.mediatracker20.adapters;

import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.navigation.Navigation;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediatracker20.R;
import com.example.mediatracker20.listselectors.ListItemDetail;

import java.util.ArrayList;
import java.util.List;

import model.model.ListManager;
import model.model.MediaList;

//adapter for MediaList Recyclerview
public class MediaListAdapter extends RecyclerView.Adapter<MediaListAdapter.MediaListViewHolder> {

    private static List<MediaList> displayList; //all active lists
    private static List<MediaList> allLists;
    private static ListManager listManager;
    private SelectionTracker selectionTracker;
    private ActionMode actionMode;

    public class MediaListViewHolder extends RecyclerView.ViewHolder {

        public TextView listTitle;
        public TextView numberOfItems;
        public TextView listCreationDate;
        public LinearLayout layout;

        //find and assign different views
        public MediaListViewHolder(@NonNull View itemView) {
            super(itemView);
            listTitle = itemView.findViewById(R.id.list_card_title);
            numberOfItems = itemView.findViewById(R.id.list_card_items);
            listCreationDate = itemView.findViewById(R.id.list_card_date);
            layout = itemView.findViewById(R.id.media_source_layout);
        }

        //provide item details lookup
        public ItemDetailsLookup.ItemDetails getItemDetails() {
            return new ListItemDetail(getAdapterPosition(), displayList.get(getAdapterPosition()));
        }

        //set binding
        public final void bind(MediaList list, boolean isActive) {
            layout.setActivated(isActive);
        }
    }

    //set the Selection Tracker
    public void setSelectionTracker(SelectionTracker selectionTracker) {
        this.selectionTracker = selectionTracker;
    }

    //filter characters and adjust list displayed
    public void filter(String charText) {
        charText = charText.toLowerCase();
        displayList.clear();
        System.out.println(charText.length());
        if (charText.length() == 0) {
            displayList.addAll(allLists);
        } else {
            for (MediaList wp : allLists) {
                if (wp.getName().toLowerCase().contains(charText)) {
                    displayList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    //sync display list and all lists
    public void syncLists() {
        allLists.clear();
        allLists.addAll(displayList);
    }


    //create cards
    @NonNull
    @Override
    public MediaListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_card, parent, false);
        MediaListViewHolder evh = new MediaListViewHolder(card);
        return evh;
    }

    //set content of the cards
    @Override
    public void onBindViewHolder(@NonNull MediaListViewHolder holder, int position) {
        MediaList list = displayList.get(position);
        holder.listTitle.setText(list.getName());
        holder.listCreationDate.setText("Created: " + list.getDateCreated());
        int num = listManager.getListOfMedia(list).size();
        holder.numberOfItems.setText("Items: " + num);
        holder.bind(list, selectionTracker.isSelected(list));

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!selectionTracker.hasSelection()) {
                    Bundle bundle = new Bundle();
                    bundle.putString("LIST_NAME", list.getName());
                    Navigation.findNavController(holder.itemView).navigate(R.id.action_nav_home_to_itemListFragment, bundle);

                } else {
                    MediaList mediaList = displayList.get(position);
                    if(selectionTracker.isSelected(mediaList)) {
                        selectionTracker.deselect(mediaList);
                    } else {
                        selectionTracker.select(mediaList);
                    }
                }
                Log.d("clicked", Integer.toString(position));
            }
        });
    }

    //get # of active lists
    @Override
    public int getItemCount() {
        return displayList.size();
    }

    //creates an adapter
    public MediaListAdapter(List<MediaList> exampleList, ActionMode actionMode) {
        displayList = exampleList;
        allLists = new ArrayList<>();
        allLists.addAll(displayList);
        listManager = ListManager.getInstance();
        this.actionMode = actionMode;
    }

    //get the position of item
    @Override
    public long getItemId(int position) {
        return position;
    }

}
