package com.memolease.realmtoy.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.memolease.realmtoy.R;
import com.memolease.realmtoy.activity.MemoEditActivity;
import com.memolease.realmtoy.event.deleteMemo;
import com.memolease.realmtoy.model.Memo;
import com.memolease.realmtoy.util.BusProvider;


import java.util.List;

/**
 * Created by bro on 2017-09-21.
 */

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.MemoViewHolder> {
    public List<Memo> items;
    public Context context;

    public MemoAdapter(Context context, List<Memo> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public MemoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = View.inflate(parent.getContext(), R.layout.memo_listview, null);
        MemoViewHolder memoViewHolder = new MemoViewHolder(rootView);
        return memoViewHolder;
    }

    @Override
    public void onBindViewHolder(MemoViewHolder holder, int position) {
        Memo memo = items.get(position);
        holder.context = context;
        holder.mAdapter = this;
        holder.setmAdapter(this);

        holder.mTitle.setText(memo.getTitle());
        holder.mBody.setText(memo.getContent());
        holder.create_date.setText(memo.getUpdatedAt());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class MemoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        int id;
        TextView mTitle;
        TextView mBody;
        TextView create_date;
        Button mShare;
        Button mDelete;
        MemoAdapter mAdapter;
        Context context;

        public MemoViewHolder(View v) {
            super(v);
            mTitle = (TextView) v.findViewById(R.id.titleTextView);
            mTitle.setTextColor(Color.parseColor("#333333"));

            mBody = (TextView) v.findViewById(R.id.bodyTextView);
            mBody.setTextColor(Color.parseColor("#427ED9"));

            create_date = (TextView) v.findViewById(R.id.create_date);

            mDelete = (Button) v.findViewById(R.id.deleteButton);
            mShare = (Button) v.findViewById(R.id.shareButton);

            v.setOnClickListener(this);
            mDelete.setOnClickListener(this);
            mShare.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Memo memo = items.get(getAdapterPosition());
            switch (v.getId()) {
                case R.id.deleteButton :
                    deleteMemo deleteMemo = new deleteMemo();
                    deleteMemo.setId(memo.getId());
                    deleteMemo.setPosition(getAdapterPosition());
                    BusProvider.getInstance().post(deleteMemo);
                    break;
                case R.id.shareButton :
                    Intent memoIntent = new Intent(context, MemoEditActivity.class);
                    memoIntent.putExtra("id", memo.getId());
                    memoIntent.putExtra("position", getAdapterPosition());
                    context.startActivity(memoIntent);
                    break;
            }

        }

        public void setId(int id) {
            this.id = id;
        }
        public void setmAdapter(MemoAdapter mAdapter) {
            this.mAdapter = mAdapter;
        }
        public void setContext(Context context) { this.context = context; }

    }
}
