package ch.cloudns.wanqiu;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RecyclerActivity extends AppCompatActivity {

  List<String> assetImagePaths;
  RecyclerView recyclerView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.recycler_activity);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    ImageView backButton = findViewById(R.id.back_button);
    backButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

    recyclerView = findViewById(R.id.recyclerView);
    assetImagePaths = loadAssetImagePaths();

    showGridView(2);
  }

  private void showGridView(int spanCount) {
    GridLayoutManager recyclerLayoutManager = new GridLayoutManager(this, spanCount);

    MyRecyclerAdapter myRecyclerAdapter =
        new MyRecyclerAdapter(this, R.layout.item_image_grid, assetImagePaths);

    recyclerView.setAdapter(myRecyclerAdapter);
    recyclerView.setLayoutManager(recyclerLayoutManager);
  }

  private void showListView(boolean isVertical, boolean isReverse) {
    LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(this);
    recyclerLayoutManager.setOrientation(
        isVertical ? LinearLayoutManager.VERTICAL : LinearLayoutManager.HORIZONTAL);
    recyclerLayoutManager.setReverseLayout(isReverse);

    MyRecyclerAdapter myRecyclerAdapter =
        new MyRecyclerAdapter(this, R.layout.item_image, assetImagePaths);

    recyclerView.setAdapter(myRecyclerAdapter);
    recyclerView.setLayoutManager(recyclerLayoutManager);
  }

  private List<String> loadAssetImagePaths() {
    List<String> assetImagePaths = new ArrayList<>();
    try {
      String[] files = getAssets().list("gallery");
      if (files != null) {

        assetImagePaths =
            Arrays.stream(files).map(file -> "gallery/" + file).collect(Collectors.toList());
      }
    } catch (IOException e) {
      Toast.makeText(this, "IO错误", Toast.LENGTH_SHORT).show();
    }
    return assetImagePaths;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.ListVertical) {
      showListView(true, false);
      Toast.makeText(this, "垂直显示", Toast.LENGTH_SHORT).show();

    } else if (id == R.id.ListVerticalReverse) {
      showListView(true, true);
      Toast.makeText(this, "垂直反转", Toast.LENGTH_SHORT).show();

    } else if (id == R.id.ListLevel) {
      showListView(false, false);
      Toast.makeText(this, "水平显示", Toast.LENGTH_SHORT).show();

    } else if (id == R.id.ListLevelReverse) {
      showListView(false, true);
      Toast.makeText(this, "水平反转", Toast.LENGTH_SHORT).show();

    } else if (id == R.id.GridTwoColumns) {
      showGridView(2);
      Toast.makeText(this, "两列布局", Toast.LENGTH_SHORT).show();

    } else if (id == R.id.GridThreeColumns) {
      showGridView(3);
      Toast.makeText(this, "三列布局", Toast.LENGTH_SHORT).show();

    } else if (id == R.id.GridFourColumns) {
      showGridView(4);
      Toast.makeText(this, "四列布局", Toast.LENGTH_SHORT).show();

    } else if (id == R.id.GridAutoFit) {
      Toast.makeText(this, "自适应列数", Toast.LENGTH_SHORT).show();

    } else if (id == R.id.StaggerVertical) {
      Toast.makeText(this, "瀑布流垂直", Toast.LENGTH_SHORT).show();

    } else if (id == R.id.StaggerVerticalReverse) {
      Toast.makeText(this, "瀑布流垂直反转", Toast.LENGTH_SHORT).show();

    } else if (id == R.id.StaggerHorizontal) {
      Toast.makeText(this, "瀑布流水平", Toast.LENGTH_SHORT).show();

    } else if (id == R.id.StaggerHorizontalReverse) {
      Toast.makeText(this, "瀑布流水平反转", Toast.LENGTH_SHORT).show();

    } else {
      return super.onOptionsItemSelected(item);
    }
    return true;
  }
}
