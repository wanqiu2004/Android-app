package ch.cloudns.wanqiu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.InnerViewHolder> {
  private final List<String> assetImagePaths;
  private final Context context;
  private final int layout;
  private Toast lastToast = null;

  public MyRecyclerAdapter(Context context, int layout, List<String> assetImagePaths) {
    this.context = context;
    this.layout = layout;
    this.assetImagePaths = assetImagePaths;
  }

  @NonNull
  @Override
  public InnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(layout, parent, false);
    return new InnerViewHolder(view);
  }

  // 显示 Toast 的方法，接收 Context 作为参数
  public void showToast(Context context, String message) {
    if (lastToast != null) {
      lastToast.cancel();
    }
    lastToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
    lastToast.show();
  }

  // onBindViewHolder 中设置点击事件
  public void onBindViewHolder(@NonNull InnerViewHolder innerViewHolder, int position) {
    String assetPath = assetImagePaths.get(position);
    String fileName = assetPath.substring(assetPath.lastIndexOf("/") + 1);

    try {
      InputStream is = context.getAssets().open(assetPath);
      Bitmap bitmap = BitmapFactory.decodeStream(is);
      innerViewHolder.imageView.setImageBitmap(bitmap);
      innerViewHolder.imageName.setText(fileName);
      is.close();
    } catch (IOException e) {
      innerViewHolder.imageName.setText("加载失败");
    }

    // 设置点击事件，直接通过 imageName 获取文本
    innerViewHolder.imageView.setOnClickListener(
        v -> {
          String imageNameText = innerViewHolder.imageName.getText().toString(); // 获取文本
          showToast(v.getContext(), "第 " + (position + 1) + " 张图片是：" + imageNameText); // 传递上下文
        });
  }

  @Override
  public int getItemCount() {
    return assetImagePaths.size();
  }

  public static class InnerViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView imageName;

    public InnerViewHolder(@NonNull View itemView) {
      super(itemView);
      imageView = itemView.findViewById(R.id.image_view);
      imageName = itemView.findViewById(R.id.image_name);
    }
  }
}
