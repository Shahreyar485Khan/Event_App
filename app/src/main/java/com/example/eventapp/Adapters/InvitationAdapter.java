package com.example.eventapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.eventapp.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class InvitationAdapter extends RecyclerView.Adapter<InvitationAdapter.InvitationAdapterViewHolder> {

    public static List<String> namesList;
    public static List<String> emailList;
    public static List<String> idList;
    final private InvitationAdapter.InvitationAdapterOnClickHandler mClickHandler;
    private final InvitationAdapter.AdapterListener onClickListener;

    public interface InvitationAdapterOnClickHandler {
        void onClick(String bookmarksStr);
    }

    //region Interface adapter listener
    public interface AdapterListener {

        void btnOnClick(View v, int position);
    }

    public
    InvitationAdapter(InvitationAdapter.InvitationAdapterOnClickHandler handler, InvitationAdapter.AdapterListener listener, Context context) {
        mClickHandler = handler;
        this.onClickListener = listener;
    }

    @NonNull
    @Override
    public InvitationAdapter.InvitationAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutId = R.layout.invitation_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutId, parent, shouldAttachToParentImmediately);

        return new InvitationAdapter.InvitationAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InvitationAdapter.InvitationAdapterViewHolder holder, final int position) {

        String nameStr = namesList.get(position);
        String emailStr = emailList.get(position);
        String idStr = idList.get(position);

        Log.d("invitation_Adapter","name   "+nameStr+" email "+emailStr+" id  " + idStr);

        holder.nameTextView.setText(nameStr);

//        holder.sendButton.setTag(R.string.email, emailStr);
//        holder.sendButton.setTag(R.string.name, nameStr);
//        holder.sendButton.setTag(idStr);

        holder.confirmButton.setTag(R.string.sender_name , nameStr);
        holder.confirmButton.setTag(R.string.sender_id , idStr);
        holder.confirmButton.setTag(R.string.sender_email , emailStr);

        holder.rejectButton.setTag(R.string.sender_name , nameStr);
        holder.rejectButton.setTag(R.string.sender_id , idStr);
        holder.rejectButton.setTag(R.string.sender_email , emailStr);

        holder.confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickListener.btnOnClick(v, position);
            }
        });


        holder.rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickListener.btnOnClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (namesList == null)
            return 0;
        return namesList.size();
    }

    public void setSenderList(List<String> mNamesList) {
        namesList = mNamesList;
        notifyDataSetChanged();
    }

    public void setRecipientList(List<String> mEmailList) {
        emailList = mEmailList;
        notifyDataSetChanged();
    }

    public void setIdList(List<String> mIdList) {
        idList = mIdList;
        notifyDataSetChanged();
    }

    public void setEmailList(List<String> mIdList) {
        emailList = mIdList;
        notifyDataSetChanged();
    }

    public class InvitationAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView nameTextView;
        final Button confirmButton;
        final Button rejectButton;

        InvitationAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.request_name_tv);
            confirmButton = itemView.findViewById(R.id.join_btn);
            rejectButton = itemView.findViewById(R.id.not_interested_btn);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int adapterPosition = getAdapterPosition();
            String clickedFileName = namesList.get(adapterPosition);

            mClickHandler.onClick(clickedFileName);
        }
    }
}
