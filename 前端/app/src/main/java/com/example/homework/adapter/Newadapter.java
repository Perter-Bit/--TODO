package com.example.homework.adapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.homework.R;
import com.example.homework.entity.News;
import com.example.homework.logic.LoginActivity;
import com.example.homework.logic.RegisterActivity;
import com.example.homework.logic.Web;

import java.util.List;
import java.util.Timer;
public class Newadapter extends RecyclerView.Adapter<Newadapter.ViewHolder>
{
    List<News> NewsList;
    private static Timer timer = new Timer();
    private Context context;
    private Newadapter.OnItemClickListener itemClickListener;
    private Newadapter.OnEditButtonClickListener editButtonClickListener;
    @NonNull
    @Override
    public Newadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_news_layout, parent, false);
        return new Newadapter.ViewHolder(view, itemClickListener, editButtonClickListener);
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    // Edit button click event listener interface
    public interface OnEditButtonClickListener {
        void onEditButtonClick(int position);
    }
    public Newadapter(Context context, List<News> NewList) {
        this.context = context;
        this.NewsList = NewList;
    }
    public void setOnItemClickListener(Newadapter.OnItemClickListener listener) {
        this.itemClickListener = listener;
    }
    // Setter for edit button click listener
    public void setOnEditButtonClickListener(Newadapter.OnEditButtonClickListener listener) {
        this.editButtonClickListener = listener;
    }
    @Override
    public int getItemCount() {
        if (NewsList == null) {
            return 0; // 或者根据实际情况返回一个默认值
        }
        return NewsList.size();
    }
    @Override
    public void onBindViewHolder(@NonNull Newadapter.ViewHolder holder, int position)
    {
        News n = NewsList.get(position);
        holder.img.setTag(n.img_url);
        holder.title.setText(n.title);
        holder.textView_content.setText(n.describe);
        //通过集合中的图片地址获得图片并且设置到view上
        getImage(this.context, n.img_url, holder.img);
        holder.textView_content.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Web.class);
                intent.putExtra("url",n.url);
                context.startActivity(intent);
            }
        });
    }
    public void getImage(Context context, String imgUrl,
                         final ImageView imageView) {
        if (imageView.getTag().toString().equals(imgUrl)) {
            RequestQueue mQueue = Volley.newRequestQueue(context);
            ImageRequest imageRequest = new ImageRequest(imgUrl,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            imageView.setImageBitmap(response);
                        }
                    }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            mQueue.add(imageRequest);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView_content,title;
        ImageView img;
        public ViewHolder(@NonNull View itemView, Newadapter.OnItemClickListener itemClickListener, Newadapter.OnEditButtonClickListener editButtonClickListener)
        {
            super(itemView);
            title = itemView.findViewById(R.id.new_title);
            textView_content = itemView.findViewById(R.id.new_des);
            img = itemView.findViewById(R.id.new_img);
        }
    }
}
