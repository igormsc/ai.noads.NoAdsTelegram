package ai.noads.telegram.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ai.noads.telegram.config.HookConfig;

public class SettingsActivity extends Activity {

    private AlertDialog mainDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showPreferenceDialog();
    }

    private void showPreferenceDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("NoAdsTelegram Settings");
            builder.setView(createPreferenceView());
            builder.setPositiveButton("OK", (dialog, which) -> {});
            builder.setOnDismissListener(dialog -> { if (!isFinishing()) finish(); });
            mainDialog = builder.create();
            mainDialog.show();
        } catch (Exception e) {
            Toast.makeText(this, "Error showing settings", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @SuppressLint("SetTextI18n")
    private View createPreferenceView() {
        try {
            ScrollView scrollView = new ScrollView(this);
            LinearLayout mainLayout = new LinearLayout(this);
            mainLayout.setOrientation(LinearLayout.VERTICAL);
            mainLayout.setPadding(dpToPx(20), dpToPx(20), dpToPx(20), dpToPx(20));

            Map<HookConfig.Category, List<HookConfig>> grouped = new LinkedHashMap<>();
            for (HookConfig hook : HookConfig.getAll()) {
                HookConfig.Category cat = hook.category;
                if (!grouped.containsKey(cat)) grouped.put(cat, new ArrayList<>());
                Objects.requireNonNull(grouped.get(cat)).add(hook);
            }

            for (HookConfig.Category category : HookConfig.Category.values()) {
                List<HookConfig> hooks = grouped.get(category);
                if (hooks == null || hooks.isEmpty()) continue;

                TextView header = new TextView(this);
                header.setText(category.displayName);
                header.setTextSize(18);
                header.setTypeface(null, android.graphics.Typeface.BOLD);
                header.setPadding(0, dpToPx(16), 0, dpToPx(8));
                mainLayout.addView(header);

                for (HookConfig hook : hooks) {
                    LinearLayout switchLayout = new LinearLayout(this);
                    switchLayout.setOrientation(LinearLayout.HORIZONTAL);
                    switchLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                    TextView textView = new TextView(this);
                    textView.setText(hook.title);
                    textView.setTextSize(16);
                    textView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));

                    Switch switchView = new Switch(this);
                    boolean isEnabled = hook.isEnabledInActivity(this);
                    switchView.setChecked(isEnabled);
                    switchView.setTag(hook.key);
                    switchView.setOnCheckedChangeListener((buttonView, checked) -> hook.setEnabled(checked, SettingsActivity.this));

                    switchLayout.addView(textView);
                    switchLayout.addView(switchView);
                    mainLayout.addView(switchLayout);

                    View spacer = new View(this);
                    spacer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(8)));
                    mainLayout.addView(spacer);
                }
            }

            TextView restartNote = new TextView(this);
            restartNote.setText("⚠️ Changes will apply after restarting Telegram");
            restartNote.setTextSize(14);
            restartNote.setTextColor(0xFFFF4444);
            restartNote.setPadding(0, dpToPx(20), 0, 0);
            mainLayout.addView(restartNote);

            scrollView.addView(mainLayout);
            return scrollView;
        } catch (Exception e) {
            TextView errorView = new TextView(this);
            errorView.setText("Error loading settings.");
            errorView.setPadding(dpToPx(20), dpToPx(20), dpToPx(20), dpToPx(20));
            return errorView;
        }
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mainDialog != null && mainDialog.isShowing()) {
            mainDialog.dismiss();
        }
    }
}