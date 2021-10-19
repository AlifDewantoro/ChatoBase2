package com.gamatechno.chato.sdk.utils.ChatUtils;

import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;

public abstract class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
    private int visibleThreshold = 5;
    private int currentPage = 0;
    private int previousTotalItemCount = 0;

    int visibleItemCount = 0;
    int totalItemCount = 5;
    int pastVisiblesItems = 0;

    private boolean loading = true;
    private final int startingPageIndex = 0;
    private SpeedyLinearLayoutManager layoutManager;

    private final int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        int i = 0;

        for(int var4 = lastVisibleItemPositions.length; i < var4; ++i) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i];
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];
            }
        }

        return maxSize;
    }

    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {
        visibleItemCount = this.layoutManager.getChildCount();
        totalItemCount = this.layoutManager.getItemCount();
        pastVisiblesItems = this.layoutManager.findFirstVisibleItemPosition();
        if (dy < 0) //check for scroll down
        {
//            visibleItemCount = this.layoutManager.getChildCount();
//            totalItemCount = this.layoutManager.getItemCount();
//            pastVisiblesItems = this.layoutManager.findFirstVisibleItemPosition();

            if (loading) {
                if (pastVisiblesItems == 0) {
                    this.loading = false;
                    System.out.println("Last Item Bro " + totalItemCount);
                    this.onLoadMore(dx, dy, view);
                }
            }
        }

        Log.d("scroll lv", "onScrolled: "+visibleItemCount+" total item : "+totalItemCount+" pastVisiblesItems : "+pastVisiblesItems);

    }

    public final void resetState() {
        this.loading = true;
    }

    public abstract void onLoadMore(int var1, int var2, RecyclerView var3);

    public EndlessRecyclerViewScrollListener(SpeedyLinearLayoutManager layoutManager) {
        super();
        this.visibleThreshold = 8;
        this.loading = true;
        this.layoutManager = layoutManager;
    }
}
