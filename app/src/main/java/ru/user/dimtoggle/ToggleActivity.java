package ru.user.dimtoggle;

import android.app.Activity;
import android.content.ContentResolver;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

/**
 * Переключает подсветку матрицы ТВ между LOW и HIGH и сразу закрывается.
 * Без интерфейса, без фоновых служб: в памяти не задерживается.
 */
public class ToggleActivity extends Activity {

    /** Настройка подсветки в ТВ Grundig (MediaTek): Settings.Global, 0-100. */
    private static final String KEY = "picture_backlight";

    /** Затемнённое значение. Хотите другую яркость: поменяйте число (0-100). */
    private static final int LOW = 10;

    /** Обычное значение. */
    private static final int HIGH = 100;

    /** Всё, что выше порога, считается «обычной» яркостью, и нажатие затемняет. */
    private static final int THRESHOLD = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            ContentResolver resolver = getContentResolver();
            int current = Settings.Global.getInt(resolver, KEY, HIGH);
            int next = current > THRESHOLD ? LOW : HIGH;
            boolean saved = Settings.Global.putInt(resolver, KEY, next);
            if (saved) {
                Toast.makeText(this,
                        next == LOW ? R.string.toast_dim : R.string.toast_bright,
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.toast_failed, Toast.LENGTH_LONG).show();
            }
        } catch (SecurityException e) {
            Toast.makeText(this, R.string.toast_no_permission, Toast.LENGTH_LONG).show();
        }
        finish();
    }
}
