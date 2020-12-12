package com.g10.locomobile.ui.main;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.g10.locomobile.R;
import com.g10.locomobile.models.OnListFragmentInteractionListener;
import com.g10.locomobile.models.Train;

import java.util.List;

/**
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class TrainManagementRecyclerViewAdapter extends RecyclerView.Adapter<TrainManagementRecyclerViewAdapter.ViewHolder> {

    private final List<Train> mValues;
    private final OnListFragmentInteractionListener mListener;

    public TrainManagementRecyclerViewAdapter(List<Train> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_train_management, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.train = mValues.get(position);
        holder.mIdView.setText("" + (position + 1));
        holder.mContentView.setText(mValues.get(position).getname());

        holder.rowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.train);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final LinearLayout rowLayout;
        public Train train;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
            rowLayout = (LinearLayout) view.findViewById(R.id.theRow);

        }


        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
