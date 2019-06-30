package com.litesnap.open.elements;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.litesnap.open.elements.bean.HolderBean;
import com.litesnap.open.elements.manager.DataManager;
import com.litesnap.open.elements.utils.BindImage;

import java.util.List;

public class ViewFragment extends Fragment {
    public static final String INDEX = "index";

    private RecyclerView mRecyclerView;
    private View mTargetView;
    private int mTargetIndex;
    public static ViewFragment newInstance(int index) {

        Bundle args = new Bundle();
        args.putInt(INDEX, index);
        ViewFragment fragment = new ViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTargetIndex = getArguments().getInt(INDEX);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmnet_view, container, false);
        mRecyclerView = view.findViewById(R.id.recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        new PagerSnapHelper().attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(new ViewAdpater(inflater, DataManager.getDataList()));

        mRecyclerView.scrollToPosition(mTargetIndex);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                mTargetView = mRecyclerView.getChildAt(0).findViewById(R.id.image);
                mTargetIndex = ((LinearLayoutManager)mRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
            }
        });

        return view;
    }

    public View getTargetView() {
        return mTargetView;
    }

    public int getTargetIndex() {
        return mTargetIndex;
    }

    private class ViewAdpater extends RecyclerView.Adapter<ViewAdpater.Holder>{
        private List<HolderBean> mDataList;
        private LayoutInflater mInflater;

        public ViewAdpater(LayoutInflater inflater, List<HolderBean> list){
            mDataList = list;
            mInflater = inflater;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = mInflater.inflate(R.layout.holder_detial_item, viewGroup, false);
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

            public void bindHolder(HolderBean bean){
                BindImage.bindImage(mImageView, bean.getResId(), getActivity());
                ViewCompat.setTransitionName(mImageView, String.valueOf(bean.getUUID()));
            }
        }
    }
}
