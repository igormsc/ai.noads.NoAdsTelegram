package ai.noads.telegram.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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

    private View createPreferenceView() {
        try {
            ScrollView scrollView = new ScrollView(this);
            LinearLayout mainLayout = new LinearLayout(this);
            mainLayout.setOrientation(LinearLayout.VERTICAL);
            mainLayout.setPadding(dpToPx(20), dpToPx(20), dpToPx(20), dpToPx(20));

            for (HookConfig hook : HookConfig.getAll()) {
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
                spacer.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(10)));
                mainLayout.addView(spacer);
            }

            TextView restartNote = new TextView(this);
            restartNote.setText("⚠️ Changes will apply after restarting Telegram");
            restartNote.setTextSize(14);
            restartNote.setTextColor(0xFFFF4444);
            restartNote.setPadding(0, dpToPx(20), 0, 0);
            restartNote.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            mainLayout.addView(restartNote);

            scrollView.addView(mainLayout);
            return scrollView;
        } catch (Exception e) {
            TextView errorView = new TextView(this);
            errorView.setText("Error loading settings: " + e.getMessage());
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