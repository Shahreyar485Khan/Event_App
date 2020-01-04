package com.example.eventapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventapp.R;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestAdapterViewHolder> {

    public static List<String> namesList;
    public static List<String> emailList;
    public static List<String> idList;
    final private RequestAdapter.RequestAdapterOnClickHandler mClickHandler;
    private final RequestAdapter.AdapterListener onClickListener;

    public interface RequestAdapterOnClickHandler {
        void onClick(String bookmarksStr);
    }

    //region Interface adapter listener
    public interface AdapterListener {

        void btnOnClick(View v, int position);
    }

    public RequestAdapter(RequestAdapter.RequestAdapterOnClickHandler handler, RequestAdapter.AdapterListener listener, Context context) {
        mClickHandler = handler;
        this.onClickListener = listener;
    }

    @NonNull
    @Override
    public RequestAdapter.RequestAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutId = R.layout.request_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutId, parent, shouldAttachToParentImmediately);

        return new RequestAdapter.RequestAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestAdapter.RequestAdapterViewHolder holder, final int position) {

        String nameStr = namesList.get(position);
        String emailStr = emailList.get(position);
        String idStr = emailList.get(position);

        holder.nameTextView.setText(emailStr);

//        holder.sendButton.setTag(R.string.email, emailStr);
//        holder.sendButton.setTag(R.string.name, nameStr);
//        holder.sendButton.setTag(idStr);

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
        emailList = mIdList;
        notifyDataSetChanged();
    }

    public class RequestAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView nameTextView;
        final Button confirmButton;
        final Button rejectButton;

        RequestAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.request_name_tv);
            confirmButton = itemView.findViewById(R.id.confirm_btn);
            rejectButton = itemView.findViewById(R.id.reject_btn);

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
