package com.litesnap.open.elements;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.litesnap.open.elements.bean.HolderBean;
import com.litesnap.open.elements.manager.DataManager;
import com.litesnap.open.elements.utils.BindImage;

import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE = 1;
    private RecyclerView mRecyclerView;

    private View mTargetView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setExitSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                names.clear();
                sharedElements.clear();

                View view = mTargetView;
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
                return false;
            }
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(new MyAdapter(DataManager.getDataList()));
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        postponeEnterTransition();

        final int index = data.getIntExtra(ViewActivity.FINAL_INDEX, 0);
        mRecyclerView.scrollToPosition(index);

        getWindow().getDecorView().getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onPreDraw() {
                getWindow().getDecorView().getViewTreeObserver().removeOnPreDrawListener(this);
                mTargetView = ((MyAdapter.Holder)mRecyclerView.findViewHolderForAdapterPosition(index)).getImageView();
                startPostponedEnterTransition();
                return false;
            }
        });
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.Holder>{
        private List<HolderBean> mDataList;
        private LayoutInflater mInflater;

        public MyAdapter(List<HolderBean> list){
            mDataList = list;
            mInflater = LayoutInflater.from(MainActivity.this);
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = mInflater.inflate(R.layout.holder_item, viewGroup, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int i) {
            holder.bindHolder(mDataList.get(i));
        }

        @Override
        public int getItemCount() {
            return DataManager.getDataList().size();
        }

        public class Holder extends RecyclerView.ViewHolder {
            private ImageView mImageView;
            public Holder(@NonNull View itemView) {
                super(itemView);
                mImageView = itemView.findViewById(R.id.image);
            }

            public ImageView getImageView() {
                return mImageView;
            }

            public void bindHolder(final HolderBean bean){
                BindImage.bindImage(mImageView, bean.getResId(), MainActivity.this);
                ViewCompat.setTransitionName(mImageView, String.valueOf(bean.getUUID()));

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mTargetView = mImageView;
                        Intent intent = new Intent(MainActivity.this, ViewActivity.class);
                        intent.putExtra(ViewFragment.INDEX, mDataList.indexOf(bean));
                        Pair<View, String> pair = new Pair<View, String>(mImageView, String.valueOf(bean.getUUID()));
                        ActivityOptionsCompat optionsCompat =
                                ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, pair);
                        startActivityForResult(intent, REQUEST_CODE, optionsCompat.toBundle());
                    }
                });
            }
        }
    }
}
