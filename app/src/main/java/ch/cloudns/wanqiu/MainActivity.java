package ch.cloudns.wanqiu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

public class MainActivity extends AppCompatActivity {

  private static final int INDICATOR_SIZE = 20;
  private static final int INDICATOR_MARGIN = 8;
  private final int[] guideImages = {R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};
  private ViewPager2 viewPager;
  private LinearLayout indicatorLayout;
  private Button skipButton, nextButton, startButton;

  private boolean isFirstLaunch() {
    SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
    return prefs.getBoolean("is_first_launch", true);
  }

  private void setFirstLaunchFalse() {
    SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
    prefs.edit().putBoolean("is_first_launch", false).apply();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // 如果不是第一次启动，直接跳转 RecyclerActivity
    if (!isFirstLaunch()) {
      startActivity(new Intent(this, RecyclerActivity.class));
      finish(); // 结束当前引导页面
      return;
    }

    // 第一次启动，继续加载引导界面
    setContentView(R.layout.activity_main);

    enableImmersiveMode(); // 设置沉浸式 + 刘海屏适配
    initViews(); // 初始化控件
    initViewPager(); // 初始化 ViewPager
    setupIndicators(); // 设置底部指示器
    setCurrentIndicator(0); // 默认选中第一个指示器
  }

  /** 初始化控件 */
  private void initViews() {
    viewPager = findViewById(R.id.viewPager);
    indicatorLayout = findViewById(R.id.indicatorLayout);
    skipButton = findViewById(R.id.btnSkip);
    nextButton = findViewById(R.id.btnNext);
    startButton = findViewById(R.id.btnStart);

    skipButton.setOnClickListener(v -> viewPager.setCurrentItem(guideImages.length - 1, true));

    nextButton.setOnClickListener(
        v -> {
          int nextIndex = viewPager.getCurrentItem() + 1;
          if (nextIndex < guideImages.length) {
            viewPager.setCurrentItem(nextIndex, true);
          }
        });

    startButton.setOnClickListener(
        v -> {
          // 创建 EditText 并设置参数
          final EditText input = new EditText(this);
          input.setInputType(InputType.TYPE_CLASS_TEXT); // 或 InputType.TYPE_CLASS_NUMBER
          input.setHint("请输入您的昵称或编号");
          input.setSingleLine(true);
          input.setMaxLines(1);
          input.setPadding(50, 30, 50, 30);
          input.setLayoutParams(
              new LinearLayout.LayoutParams(
                  ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

          // 创建 AlertDialog 并显示
          AlertDialog dialog =
              new AlertDialog.Builder(this)
                  .setTitle("输入信息")
                  .setMessage("请输入您的昵称或编号：")
                  .setView(input)
                  .setPositiveButton("确认", null) // 稍后设置点击事件，避免自动关闭
                  .setNegativeButton("取消", (d, w) -> d.dismiss())
                  .create();

          dialog.setOnShowListener(
              d -> {
                Button confirmButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                confirmButton.setOnClickListener(
                    view -> {
                      String userInput = input.getText().toString().trim();
                      if (!userInput.isEmpty()) {
                        setFirstLaunchFalse(); // 设置不再是第一次启动
                        Intent intent = new Intent(this, RecyclerActivity.class);
                        intent.putExtra("userInput", userInput);
                        startActivity(intent);
                        dialog.dismiss(); // 关闭对话框
                        finish(); // 关闭引导页
                      } else {
                        input.setError("输入不能为空");
                      }
                    });
              });

          dialog.show();
        });
  }

  /** 初始化 ViewPager2 和切换回调 */
  private void initViewPager() {
    GuideAdapter adapter = new GuideAdapter(guideImages);
    viewPager.setAdapter(adapter);

    viewPager.registerOnPageChangeCallback(
        new ViewPager2.OnPageChangeCallback() {
          @Override
          public void onPageSelected(int position) {
            setCurrentIndicator(position);
            updateButtonVisibility(position);
          }
        });
  }

  /** 设置按钮可见性 */
  private void updateButtonVisibility(int position) {
    boolean isLastPage = position == guideImages.length - 1;

    skipButton.setVisibility(isLastPage ? View.GONE : View.VISIBLE);
    nextButton.setVisibility(isLastPage ? View.GONE : View.VISIBLE);
    startButton.setVisibility(isLastPage ? View.VISIBLE : View.GONE);
  }

  /** 设置指示器小圆点 */
  private void setupIndicators() {
    indicatorLayout.removeAllViews();

    for (int i = 0; i < guideImages.length; i++) {
      View dot = new View(this);
      dot.setBackgroundResource(R.drawable.indicator_unselected);

      LinearLayout.LayoutParams params =
          new LinearLayout.LayoutParams(INDICATOR_SIZE, INDICATOR_SIZE);
      params.setMargins(INDICATOR_MARGIN, INDICATOR_MARGIN, INDICATOR_MARGIN, INDICATOR_MARGIN);

      indicatorLayout.addView(dot, params);
    }
  }

  /** 高亮当前选中指示器 */
  private void setCurrentIndicator(int position) {
    for (int i = 0; i < indicatorLayout.getChildCount(); i++) {
      View dot = indicatorLayout.getChildAt(i);
      dot.setBackgroundResource(
          i == position ? R.drawable.indicator_selected : R.drawable.indicator_unselected);
    }
  }

  /** 启用沉浸式状态栏 + 刘海屏适配 */
  private void enableImmersiveMode() {
    Window window = getWindow();
    window.setDecorFitsSystemWindows(false);

    // 刘海屏适配（API 28+）
    WindowManager.LayoutParams lp = window.getAttributes();
    lp.layoutInDisplayCutoutMode =
        WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
    window.setAttributes(lp);

    // 沉浸式状态栏/导航栏控制（API 30+）
    WindowInsetsController controller = window.getInsetsController();
    if (controller != null) {
      controller.hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
      controller.setSystemBarsBehavior(
          WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
    }
  }
}
