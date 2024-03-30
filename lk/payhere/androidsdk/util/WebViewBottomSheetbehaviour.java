package lk.payhere.androidsdk.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

/* loaded from: classes7.dex */
public class WebViewBottomSheetbehaviour<V extends View> extends BottomSheetBehavior<V> {
    public static boolean scrollEnabled = false;

    public WebViewBottomSheetbehaviour() {
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onInterceptTouchEvent(CoordinatorLayout coordinatorLayout, V v, MotionEvent motionEvent) {
        return scrollEnabled && super.onInterceptTouchEvent(coordinatorLayout, v, motionEvent);
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onNestedPreFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V v, @NonNull View view, float f, float f2) {
        return scrollEnabled && super.onNestedPreFling(coordinatorLayout, v, view, f, f2);
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V v, @NonNull View view, int i, int i2, @NonNull int[] iArr, int i3) {
        if (scrollEnabled) {
            super.onNestedPreScroll(coordinatorLayout, v, view, i, i2, iArr, i3);
        }
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V v, @NonNull View view, @NonNull View view2, int i, int i2) {
        return scrollEnabled && super.onStartNestedScroll(coordinatorLayout, v, view, view2, i, i2);
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V v, @NonNull View view, int i) {
        if (scrollEnabled) {
            super.onStopNestedScroll(coordinatorLayout, v, view, i);
        }
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onTouchEvent(CoordinatorLayout coordinatorLayout, V v, MotionEvent motionEvent) {
        return scrollEnabled && super.onTouchEvent(coordinatorLayout, v, motionEvent);
    }

    public WebViewBottomSheetbehaviour(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }
}
