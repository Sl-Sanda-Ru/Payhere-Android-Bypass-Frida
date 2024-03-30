package lk.payhere.androidsdk.util;

import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

/* loaded from: classes7.dex */
public class ListItemDecore extends RecyclerView.ItemDecoration {
    private int space;

    public ListItemDecore(int i) {
        this.space = i;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
    public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
        int i = this.space;
        rect.left = i;
        rect.right = i;
        rect.bottom = i;
        if (recyclerView.getChildLayoutPosition(view) == 0) {
            rect.left = 0;
        }
    }
}
