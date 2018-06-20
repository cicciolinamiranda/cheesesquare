/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.support.android.designlibdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CheeseListFragment extends Fragment {

    private static final String ARG_IS_LIST_EMPTY = "com.support.android.designlibdemo.empty_list";
    private TextView mErrorMessage;
    private RecyclerView mRecyclerView;

    public static CheeseListFragment newEmptyInstance() {
        CheeseListFragment fragment = new CheeseListFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(ARG_IS_LIST_EMPTY, true);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_cheese_list, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mErrorMessage = (TextView) view.findViewById(android.R.id.empty);
        setupRecyclerView(mRecyclerView);
        return view;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        boolean isEmpty = getArguments() == null ? false : getArguments().getBoolean(ARG_IS_LIST_EMPTY, false);
        if(isEmpty) {
            recyclerView.setVisibility(View.GONE);
            mErrorMessage.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(getActivity(),
                    getRandomSublist(Cheeses.sCheeseStrings, 30)));

            recyclerView.setVisibility(View.VISIBLE);
            mErrorMessage.setVisibility(View.GONE);
        }

    }

    private List<String> getRandomSublist(String[] array, int amount) {
        ArrayList<String> list = new ArrayList<>(amount);
        Random random = new Random();
        while (list.size() < amount) {
            list.add(array[random.nextInt(array.length)]);
        }
        return list;
    }

    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int TYPE_HEADER = 0;
        private static final int TYPE_FOOTER = 1;
        private static final int TYPE_ITEM = 2;

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private List<String> mValues;

        public static class ItemViewHolder extends RecyclerView.ViewHolder {
            public String mBoundString;

            public final View mView;
            public final ImageView mImageView;
            public final TextView mTextView;

            public ItemViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.avatar);
                mTextView = (TextView) view.findViewById(android.R.id.text1);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTextView.getText();
            }
        }

        private class HeaderViewHolder extends RecyclerView.ViewHolder {
            TextView headerTitle;

            public HeaderViewHolder(View view) {
                super(view);
                headerTitle = (TextView) view.findViewById(R.id.header_text);
            }
        }

        private class FooterViewHolder extends RecyclerView.ViewHolder {
            TextView footerText;

            public FooterViewHolder(View view) {
                super(view);
                footerText = (TextView) view.findViewById(R.id.footer_text);
            }
        }

        public String getValueAt(int position) {
            return mValues.get(position);
        }

        public SimpleStringRecyclerViewAdapter(Context context, List<String> items) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mValues = items;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return TYPE_HEADER;
            } else if (position == mValues.size() + 1) {
                return TYPE_FOOTER;
            }
            return TYPE_ITEM;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_ITEM) {
                //Inflating recycle view item layout
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
                return new ItemViewHolder(itemView);
            } else if (viewType == TYPE_HEADER) {
                //Inflating header view
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_header, parent, false);
                return new HeaderViewHolder(itemView);
            } else if (viewType == TYPE_FOOTER) {
                //Inflating footer view
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_footer, parent, false);
                return new FooterViewHolder(itemView);
            } else return null;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {


            if (holder instanceof HeaderViewHolder) {
                HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
                headerHolder.headerTitle.setText("Header View");
                headerHolder.headerTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(CheeseListFragment.newEmptyInstance().getActivity(), "You clicked at Header View!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else if (holder instanceof FooterViewHolder) {
                FooterViewHolder footerHolder = (FooterViewHolder) holder;
                footerHolder.footerText.setText("Footer View");
                footerHolder.footerText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(CheeseListFragment.newEmptyInstance().getActivity(), "You clicked at Footer View", Toast.LENGTH_SHORT).show();
                    }
                });
            } else if(holder instanceof ItemViewHolder) {

                int itemPosition = position - 1;

                final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                itemViewHolder.mBoundString = mValues.get(itemPosition);
                itemViewHolder.mTextView.setText(mValues.get(itemPosition));

                itemViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, CheeseDetailActivity.class);
                        intent.putExtra(CheeseDetailActivity.EXTRA_NAME, itemViewHolder.mBoundString);

                        context.startActivity(intent);
                    }
                });

                Glide.with(itemViewHolder.mImageView.getContext())
                        .load(Cheeses.getRandomCheeseDrawable())
                        .fitCenter()
                        .into(itemViewHolder.mImageView);
            }
        }

        @Override
        public int getItemCount() {
            return mValues.size() + 2;
        }
    }
}
