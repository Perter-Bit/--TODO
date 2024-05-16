package com.example.homework.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.homework.R;
import com.example.homework.entity.Affair;
import com.example.homework.logic.CustomDialog;
import com.example.homework.logic.SmsFragment;
import com.example.homework.logic.TimerDialog;
import java.util.List;
public class AffairAdapter extends RecyclerView.Adapter<AffairAdapter.ViewHolder>
{
    private List<Affair> affairList;
    private Context context;
    private OnItemClickListener itemClickListener;
    private OnEditButtonClickListener editButtonClickListener;
    // Item click event listener interface
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    // Edit button click event listener interface
    public interface OnEditButtonClickListener {
        void onEditButtonClick(int position);
    }

    //构造器
    public AffairAdapter(Context context, List<Affair> affairList) {
        this.context = context;
        this.affairList = affairList;
    }

    // Setter for item click listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }
    // Setter for edit button click listener
    public void setOnEditButtonClickListener(OnEditButtonClickListener listener) {
        this.editButtonClickListener = listener;
    }
    // ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView textView2;
        ImageView backgroundImageView;
        Button editButton;
        public ViewHolder(@NonNull View itemView, OnItemClickListener itemClickListener, OnEditButtonClickListener editButtonClickListener) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            backgroundImageView = itemView.findViewById(R.id.backgroundImageView);
            textView2 = itemView.findViewById(R.id.textView2);
            editButton = itemView.findViewById(R.id.editButton);
            // Set click listeners
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && itemClickListener != null) {
                        itemClickListener.onItemClick(position);
                        CustomDialog.affair = SmsFragment.affairList.get(position);
                        CustomDialog.showDialog(v.getContext());
                    }
                }
            });
            // Set click listener for edit button
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && editButtonClickListener != null)
                    {
                        editButtonClickListener.onEditButtonClick(position);
                        // 创建对话框实例并显示
                        TimerDialog.affair = SmsFragment.affairList.get(position);
                        TimerDialog timerDialog = new TimerDialog(itemView.getContext());
                        timerDialog.show();
                    }
                }
            });
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout, parent, false);
        return new ViewHolder(view, itemClickListener, editButtonClickListener);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Affair affair = affairList.get(position);
        holder.textView.setText(affair.message);
        if (affair.type == 1)
        {
            holder.textView2.setText("计时");
        } else {
            holder.textView2.setText(affair.time + "分钟");
        }
        switch (affair.imageId) {
            case 1:
                holder.backgroundImageView.setImageResource(R.drawable.bp1);
                break;
            case 2:
                holder.backgroundImageView.setImageResource(R.drawable.bp2);
                break;
            case 3:
                holder.backgroundImageView.setImageResource(R.drawable.bp3);
                break;
            case 4:
                holder.backgroundImageView.setImageResource(R.drawable.bp4);
                break;
            case 5:
                holder.backgroundImageView.setImageResource(R.drawable.bp5);
                break;
            case 6:
                holder.backgroundImageView.setImageResource(R.drawable.bp6);
                break;
            case 7:
                holder.backgroundImageView.setImageResource(R.drawable.bp7);
                break;
            case 8:
                holder.backgroundImageView.setImageResource(R.drawable.bp8);
                break;
            case 9:
                holder.backgroundImageView.setImageResource(R.drawable.bp9);
                break;
            case 10:
                holder.backgroundImageView.setImageResource(R.drawable.bp10);
                break;
        }
    }
    @Override
    public int getItemCount() {
        return affairList.size();
    }
}
