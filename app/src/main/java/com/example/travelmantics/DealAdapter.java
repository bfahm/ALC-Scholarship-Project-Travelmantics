package com.example.travelmantics;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/* First step in creating RecylcerView Classes is designing the ViewHolder,
*  THEN extending the main outer class with a recycler view of
*  GENERIC type: "just designed viewholder" */

public class DealAdapter extends RecyclerView.Adapter<DealAdapter.DealViewHolder>{

    private static final String TAG = "DealAdapter";

    ArrayList<TravelDeal> deals;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;

    DealAdapter(){
        //We will open the reference in the caller activity instead to be able to pass the activity context.
        //FirebaseUtils.openFbReference("traveldeals");
        mFirebaseDatabase = FirebaseUtils.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtils.mDatabaseReference;

        deals = FirebaseUtils.mDeals; //Get the current list in FirebaseUtils

        //When an adapter is initialized, a listener will be created an will always listen for any changes.
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                /* From the snapshot taken from the database, get the Added child of type TravelDeal
                   -- This line gives the effect of serializing the return object to an array
                      of bytes and adding it to the class object deal. */
                TravelDeal deal = dataSnapshot.getValue(TravelDeal.class);
                Log.d(TAG, deal.getTitle());
                deal.setId(dataSnapshot.getKey());
                deals.add(deal);
                notifyItemChanged(deals.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mDatabaseReference.addChildEventListener(mChildEventListener);
    }


    @NonNull
    @Override
    public DealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Called when the RecyclerView needs A NEW viewHolder
        // Point to the ITEM.xml layout file here, and inflate it.

        View itemView = LayoutInflater.from(parent.getContext())
                                      .inflate(R.layout.rv_row, parent, false);

        return new DealViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DealViewHolder holder, int position) {
        // Called to populate the just created viewHolder
        // Get the list item (CLASS) at the current position and BIND it;
        TravelDeal deal = deals.get(position);
        holder.bind(deal);
    }

    @Override
    public int getItemCount() {
        return deals.size();
    }



    public class DealViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        /*View holder is what manages what is currently appearing to the user, via showing
        * current views and cashing scrolled views.
        * It needs to know WHERE it would bind and HOW..*/
        TextView tvTitle;
        TextView tvDescription;
        TextView tvPrice;
        ImageView imageDeal;
        public DealViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            imageDeal = itemView.findViewById(R.id.imageDeal);

            itemView.setOnClickListener(this);

        }
        public void bind(TravelDeal deal){
            tvTitle.setText(deal.getTitle());
            tvDescription.setText(deal.getDescription());
            tvPrice.setText(deal.getPrice());
            showImage(deal.getImageUrl());
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Log.d(TAG, "onClick: "+ String.valueOf(position));
            TravelDeal deal = deals.get(position);
            Intent intent = new Intent(v.getContext(), DealActivity.class);
            intent.putExtra("Deal", deal);

            //TODO: There is a bug here that needs attention.
            //Item should only be clickable if current user is an admin.
            //if(FirebaseUtils.isAdmin){
                v.getContext().startActivity(intent);
            //}

        }

        private void showImage(String url){
            if(url != null && url.isEmpty()==false){
                Picasso.get()
                        .load(url)
                        .resize(160, 160)
                        .centerCrop()
                        .into(imageDeal);
            }
        }
    }

}
