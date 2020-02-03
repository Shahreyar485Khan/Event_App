package com.example.eventapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.eventapp.R;
import com.example.eventapp.Utils.PhpMethodsUtils;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class JoinedEventListAdapter extends RecyclerView.Adapter<JoinedEventListAdapter.ViewHolders>{


    private List<String> mData;

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

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private final JoinedEventListAdapter.AdapterListener onClickListener;


    // data is passed into the constructor
    public JoinedEventListAdapter(Context context, List<String> data, JoinedEventListAdapter.AdapterListener listener) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.onClickListener = listener;
    }
    public interface AdapterListener {

        void btnOnClick(View v, int position);
    }


    // inflates the row layout from xml when needed
    @Override
    public ViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.joined_event_list_item, parent, false);
        return new ViewHolders(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolders holder, int position) {

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


        holder.txtInviteFrom.setText(nameStr);
        holder.txtTitle.setText(title);
        holder.txtLocation.setText(location);
        holder.txtDesc.setText(desc);
        holder.txtStTime.setText(start_time);
        holder.txtEndTime.setText(end_time);
        holder.txtStDate.setText(start_date);
        holder.txtEndDate.setText(end_date);


     //   holder.btnSendInvites.setTag(R.string.email, title);
     //   holder.btnSendInvites.setTag(R.string.name, nameStr);
        holder.btnLeaveEvent.setTag(R.string.event_id, event_idStr);
        holder.btnLeaveEvent.setTag(R.string.user_id, PhpMethodsUtils.currentDeviceId);

        holder.btnLeaveEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.btnOnClick(view, position);
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
       // return mData.size();

        if (titleList == null)
            return 0;
        return titleList.size();
    }


    public void setSenderList(List<String> mNamesList) {
        namesList = mNamesList;
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





    // stores and recycles views as they are scrolled off screen
    public class ViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtTitle;
        TextView txtInviteFrom;
        TextView txtLocation;
        TextView txtDesc;
        TextView txtStTime;
        TextView txtStDate;
        TextView txtEndTime;
        TextView txtEndDate;

        Button btnLeaveEvent;
        Button btnEventUpdate;
        Button btnEventDelete;

        ViewHolders(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.event_title);
            txtInviteFrom = itemView.findViewById(R.id.event_invite_from);
            txtLocation = itemView.findViewById(R.id.event_loc);
            txtDesc = itemView.findViewById(R.id.event_desc);
            txtStDate = itemView.findViewById(R.id.event_st_date);
            txtStTime = itemView.findViewById(R.id.event_st_time);
            txtEndDate = itemView.findViewById(R.id.event_end_date);
            txtEndTime = itemView.findViewById(R.id.event_end_time);


            btnLeaveEvent = itemView.findViewById(R.id.event_btn_leave);
           // btnEventUpdate = itemView.findViewById(R.id.event_btn_update);
           // btnEventDelete = itemView.findViewById(R.id.event_btn_delete);
           //  btnSendInvites.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null){
                mClickListener.onItemClick(view, getAdapterPosition());
            }

        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
   public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }


}
