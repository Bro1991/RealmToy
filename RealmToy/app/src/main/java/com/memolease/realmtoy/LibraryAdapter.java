package com.memolease.realmtoy;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.memolease.realmtoy.model.Library;
import com.memolease.realmtoy.util.BusProvider;
import com.squareup.otto.Bus;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by bro on 2017-10-13.
 */

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.LibraryViewHolder> {
    List<Library> items;
    Context context;
    Bus mBus = BusProvider.getInstance();

    @Override
    public LibraryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = View.inflate(parent.getContext(), R.layout.library_item, null);
        LibraryViewHolder libraryViewHolder = new LibraryViewHolder(rootView);
        return libraryViewHolder;
    }

    @Override
    public void onBindViewHolder(LibraryViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class LibraryViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        Button editLibraryListButton, deleteLibraryListButton;

        public LibraryViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            editLibraryListButton = (Button) itemView.findViewById(R.id.editLibraryListButton);
            deleteLibraryListButton = (Button) itemView.findViewById(R.id.deleteLibraryListButton);
        }
    }

}

