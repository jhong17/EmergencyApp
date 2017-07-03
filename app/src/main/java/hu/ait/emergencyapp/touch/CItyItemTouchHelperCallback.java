package hu.ait.emergencyapp.touch;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by Sooyoung Kim on 6/27/2017.
 */

public class CItyItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private CityTouchHelperAdapter cityTouchHelperAdapter;

    public CItyItemTouchHelperCallback(CityTouchHelperAdapter cityTouchHelperAdapter) {

        this.cityTouchHelperAdapter = cityTouchHelperAdapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;

        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

        cityTouchHelperAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        cityTouchHelperAdapter.onItemDismiss(viewHolder.getAdapterPosition());

    }

}
