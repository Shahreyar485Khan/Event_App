package com.example.eventapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.eventapp.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventListAdapterViewHolder> {

    public static List<String> namesList;
    public static List<String> emailList;
    public static List<String> idList;
    final private EventListAdapter.EventListAdapterOnClickHandler mClickHandler;
    private final EventListAdapter.AdapterListener onClickListener;

    public interface EventListAdapterOnClickHandler {
        void onClick(String bookmarksStr);
    }

    //region Interface adapter listener
    public interface AdapterListener {

        void btnOnClick(View v, int position);
    }

    public EventListAdapter(EventListAdapterOnClickHandler handler, AdapterListener listener, Context context) {
        mClickHandler = handler;
        this.onClickListener = listener;
    }

    @NonNull
    @Override
    public EventListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutId = R.layout.search_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutId, parent, shouldAttachToParentImmediately);

        return new EventListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventListAdapterViewHolder holder, final int position) {

        String nameStr = namesList.get(position);
        //String emailStr = emailList.get(position);
      //  String idStr = emailList.get(position);

        holder.nameTextView.setText(nameStr);

      //  holder.sendButton.setTag(R.string.email, emailStr);
        holder.sendButton.setTag(R.string.name, nameStr);
//        holder.sendButton.setTag(idStr);

        holder.sendButton.setOnClickListener(new View.OnClickListener() {
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

    public void setNamesList(List<String> mNamesList) {
        namesList = mNamesList;
        notifyDataSetChanged();
    }

    public void setEmailList(List<String> mEmailList) {
        emailList = mEmailList;
        notifyDataSetChanged();
    }

    public void setIdList(List<String> mIdList) {
        emailList = mIdList;
        notifyDataSetChanged();
    }

    public class EventListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView nameTextView;
        final Button sendButton;

        EventListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.name_tv);
            sendButton = itemView.findViewById(R.id.send_btn);

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
