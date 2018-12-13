package tago.timetrackerapp.ui.util;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * A simple wrapper for TextChangedListener to hide unnecessary listeners.
 * @param <T>
 */
public abstract class TextChangedListener<T> implements TextWatcher {
    private T target;

    public TextChangedListener(T target) {
        this.target = target;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Hidden
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Hidden
    }

    @Override
    public void afterTextChanged(Editable s) {
        this.onTextChanged(target, s);
    }

    public abstract void onTextChanged(T target, Editable s);
}
