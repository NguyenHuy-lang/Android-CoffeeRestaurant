package dajava.pro.pcoffee.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;
import dajava.pro.pcoffee.model.Comment;
import dajava.pro.ushouldbuy.R;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder>{
    private Context mContext;
    private List<Comment> mCommentList;
    static Picasso picasso = Picasso.get();

    public CommentAdapter(List<Comment> commentList, Context context) {
        mContext = context;
        mCommentList = commentList;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public CommentAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item,
                parent, false);
        return new CommentAdapter.CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.CommentViewHolder holder, int position) {
        Comment comment = mCommentList.get(position);
        picasso.load(comment.getUser().getImage()).into(holder.imageCusView);
        holder.nameCusTextView.setText(comment.getUser().getFullname());
        holder.ratingComment.setRating(comment.getRating());
        holder.contentCommentTextView.setText(comment.getContent());
        holder.commentDateTimeTextView.setText(comment.getDateTime());
    }

    @Override
    public int getItemCount() {
        return mCommentList.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageCusView;
        public TextView nameCusTextView;
        public TextView contentCommentTextView;
        public RatingBar ratingComment;
        public TextView commentDateTimeTextView;
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCusView = itemView.findViewById(R.id.item_image_customer_view);
            nameCusTextView = itemView.findViewById(R.id.name_customer_text_view);
            contentCommentTextView = itemView.findViewById(R.id.content_comment_text_view);
            ratingComment = itemView.findViewById(R.id.vote_rating_bar);
            commentDateTimeTextView = itemView.findViewById(R.id.time_comment_text_view);
        }
    }

}
