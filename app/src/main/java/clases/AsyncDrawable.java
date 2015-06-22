package clases;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import java.lang.ref.WeakReference;

import adapters.SnapAdapter;

/**
 * Created by Argenis on 6/20/15.
 */
public  class AsyncDrawable extends BitmapDrawable {
    private final WeakReference<SnapAdapter.BitmapWorkerTask> bitmapWorkerTaskReference;

    public AsyncDrawable(Resources res, Bitmap bitmap,
                         SnapAdapter.BitmapWorkerTask bitmapWorkerTask) {
        super(res, bitmap);
        bitmapWorkerTaskReference =
                new WeakReference<SnapAdapter.BitmapWorkerTask>(bitmapWorkerTask);
    }

    public SnapAdapter.BitmapWorkerTask getBitmapWorkerTask() {
        return bitmapWorkerTaskReference.get();
    }
}
