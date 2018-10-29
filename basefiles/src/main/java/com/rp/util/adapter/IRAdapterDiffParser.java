package com.rp.util.adapter;

public interface IRAdapterDiffParser {

    int getOldListSize();
    int getNewListSize();

    boolean areItemsTheSame(int oldPos, int newPos);
    boolean areContentsTheSame(int oldPos, int newPos);

}