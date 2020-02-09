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


    private List<String> locationList;
    private List<String> event_idList;
    private List<String> titleList;
    private List<String> descList;
    private List<String> st_timeList;
    private List<String> end_timeList;
    private List<String> st_dateList;
    private List<String> end_dateList;


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

        String title = titleList.get(position);
        String location = locationList.get(position);
        String event_idStr = event_idList.get(position);
        String desc = descList.get(position);
        String start_time = st_timeList.get(position);
        String end_time = end_timeList.get(position);
        String start_date = st_dateList.get(position);
        String end_date = end_dateList.get(position);


        Log.d("invitation_Adapter","name   "+nameStr+" email "+emailStr+" id  " + idStr);

        holder.txtTitle.setText(title);
        holder.txtLocation.setText(location);
        holder.txtDesc.setText(desc);
        holder.txtStTime.setText(start_time);
        holder.txtEndTime.setText(end_time);
        holder.txtStDate.setText(start_date);
        holder.txtEndDate.setText(end_date);

        holder.txtInviteFrom.setText(nameStr);

//        holder.sendButton.setTag(R.string.email, emailStr);
//        holder.sendButton.setTag(R.string.name, nameStr);
//        holder.sendButton.setTag(idStr);

        holder.confirmButton.setTag(R.string.sender_name , nameStr);
        holder.confirmButton.setTag(R.string.sender_id , idStr);
        holder.confirmButton.setTag(R.string.sender_email , emailStr);
        holder.confirmButton.setTag(R.string.event_id , event_idStr);

        holder.rejectButton.setTag(R.string.sender_name , nameStr);
        holder.rejectButton.setTag(R.string.sender_id , idStr);
        holder.rejectButton.setTag(R.string.sender_email , emailStr);
        holder.rejectButton.setTag(R.string.event_id , event_idStr);

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

    public void setEventIdList(List<String> mIdList) {
        event_idList = mIdList;
        notifyDataSetChanged();
    }

    public void setTitleList(List<String> mTitleList) {
        titleList = mTitleList;
        notifyDataSetChanged();
    }

    public void setLocationList(List<String> mEmailList) {
        locationList = mEmailList;
        notifyDataSetChanged();
    }

    public void setDescList(List<String> mDescList) {
        descList = mDescList;
        notifyDataSetChanged();
    }


    public void setSt_timeList(List<String> mSt_timeList) {
        st_timeList = mSt_timeList;
        notifyDataSetChanged();
    }
    public void setEnd_timeList(List<String> mEnd_timeList) {
        end_timeList = mEnd_timeList;
        notifyDataSetChanged();
    }

    public void setSt_dateList(List<String> mSt_dateList) {
        st_dateList = mSt_dateList;
        notifyDataSetChanged();
    }
    public void setEnd_dateList(List<String> mEnd_dateList) {
        end_dateList = mEnd_dateList;
        notifyDataSetChanged();
    }




    public class InvitationAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //final TextView nameTextView;
        final Button confirmButton;
        final Button rejectButton;


        TextView txtInviteFrom;
        TextView txtTitle;
        TextView txtLocation;
        TextView txtDesc;
        TextView txtStTime;
        TextView txtStDate;
        TextView txtEndTime;
        TextView txtEndDate;


        InvitationAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

           // nameTextView = itemView.findViewById(R.id.request_name_tv);

            txtInviteFrom = itemView.findViewById(R.id.event_invite_from);
            txtTitle = itemView.findViewById(R.id.event_title);
            txtLocation = itemView.findViewById(R.id.event_loc);
            txtDesc = itemView.findViewById(R.id.event_desc);
            txtStDate = itemView.findViewById(R.id.event_st_date);
            txtStTime = itemView.findViewById(R.id.event_st_time);
            txtEndDate = itemView.findViewById(R.id.event_end_date);
            txtEndTime = itemView.findViewById(R.id.event_end_time);

            confirmButton = itemView.findViewById(R.id.event_btn_join);
            rejectButton = itemView.findViewById(R.id.event_btn_reject);

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
