package com.litesnap.open.elements;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.List;
import java.util.Map;

public class ViewActivity extends SingleFragmentActivity {
    public static final String TAG = "ViewActivity";
    public static final String FINAL_INDEX = "final_index";
    private ViewFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        postponeEnterTransition();

        setEnterSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                names.clear();
                sharedElements.clear();

                View view = mFragment.getTargetView();

                if (view != null){
                    String name = ViewCompat.getTransitionName(view);

                    names.add(name);
                    sharedElements.put(name, view);
                }
                super.onMapSharedElements(names, sharedElements);
            }
        });

        getWindow().getDecorView().getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onPreDraw() {
                getWindow().getDecorView().getViewTreeObserver().removeOnPreDrawListener(this);
                startPostponedEnterTransition();
                return true;
            }
        });

        mFragment = ViewFragment.newInstance(getIntent().getIntExtra(ViewFragment.INDEX, 0));

        super.onCreate(savedInstanceState);
    }

    public void finishAfterTransition() {
        Intent intent = new Intent();
        intent.putExtra(FINAL_INDEX, mFragment.getTargetIndex());
        setResult(Activity.RESULT_OK, intent);
        super.finishAfterTransition();
    }

    @Override
    protected Fragment createFragment() {
        return mFragment;
    }
}
