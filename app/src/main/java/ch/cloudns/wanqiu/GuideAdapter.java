package ch.cloudns.wanqiu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GuideAdapter extends RecyclerView.Adapter<GuideAdapter.GuideViewHolder> {

  private final int[] imageResources;

  public GuideAdapter(int[] imageResources) {
    this.imageResources = imageResources;
  }

  @NonNull
  @Override
  public GuideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_guide, parent, false);
    return new GuideViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull GuideViewHolder holder, int position) {
    holder.imageView.setImageResource(imageResources[position]);
  }

  @Override
  public int getItemCount() {
    return imageResources.length;
  }

  static class GuideViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;

    public GuideViewHolder(@NonNull View itemView) {
      super(itemView);
      imageView = itemView.findViewById(R.id.imageView);
    }
  }
}
