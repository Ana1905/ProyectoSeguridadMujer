package com.example.proyectoseguridadmujer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<ListElement> mData;
    private LayoutInflater mInflater;
    private Context context;

    public ListAdapter(List<ListElement> itemList, Context context){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData =itemList;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    @Override
    public ListAdapter.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_post,null);
        return new ListAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ListAdapter.ViewHolder holder, final int position) {
       holder.bindData(mData.get(position));
    }

    public void setItems (List<ListElement> items ){
        mData=items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView mTextViewNameUser;
        TextView mTextViewCategory;
        TextView mTextViewPostContent;
        Button mButtonReport;
        Button mButtonComment;
        Button mButtonSavePost;

        ViewHolder(View itemView){
            super(itemView);
            mTextViewNameUser = itemView.findViewById(R.id.user_name_comunity);
            mTextViewCategory = itemView.findViewById(R.id.post_category_label_comunity);
            mTextViewPostContent = itemView.findViewById(R.id.post_content_label_comunity);
            mButtonReport = itemView.findViewById(R.id.button_report_post_comunity);
            mButtonComment = itemView.findViewById(R.id.button_comment_post_comunity);
            mButtonSavePost = itemView.findViewById(R.id.button_save_post_comunity);
        }

        void bindData(final ListElement item){
            mTextViewNameUser.setText(item.getUser_name());
            mTextViewCategory.setText(item.getCategory());
            mTextViewPostContent.setText(item.getPost_content());

        }

    }
}
