package com.app.chatanim;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    RecyclerView rvList;
    EditText etMsg;
    private ArrayList<String> mListMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListMsg = new ArrayList<>();
        rvList = findViewById(R.id.rvlist);
        etMsg = findViewById(R.id.et_message_field);
        initializeRecyclerView();
        initializeMessageListener();
    }


    private void initializeMessageListener() {
        etMsg.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (etMsg.length() > 0) {
                    mListMsg.add(etMsg.getText().toString().trim());
                    Objects.requireNonNull(rvList.getAdapter())
                            .notifyItemInserted(mListMsg.size() - 1);
                    etMsg.getText().clear();
                } else {
                    InputMethodManager imm = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    Objects.requireNonNull(imm).hideSoftInputFromWindow(etMsg.getWindowToken(), 0);
                }
            }
            return false;
        });
    }

    private void initializeRecyclerView() {
        rvList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rvList.setAdapter(new TextAdapter(this, mListMsg,
                position -> {
                    if (position == 0) return;
                    Objects.requireNonNull(rvList.getAdapter())
                            .notifyItemChanged(position - 1, true);
                }));
        Objects.requireNonNull(rvList.getAdapter()).notifyDataSetChanged();
    }
}
