package adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps.argenis.snapnotes.R;

import java.lang.ref.WeakReference;
import java.util.List;

import clases.AsyncDrawable;
import clases.Snapnote;

/**
 * Created by Argenis on 6/16/15.
 */
public class SnapAdapter extends RecyclerView.Adapter<SnapAdapter.SnapnoteViewHolder>  {

    //the viewholder class
    public static class SnapnoteViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView noteDate;
        TextView dueDate;
        ImageView notePhoto;

        public SnapnoteViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            noteDate = (TextView)itemView.findViewById(R.id.note_due_date);
            dueDate = (TextView)itemView.findViewById(R.id.note_date);
            notePhoto = (ImageView)itemView.findViewById(R.id.note_photo);
        }
    }



    List<Snapnote> notes;

    Context context;

    //cache variables
    private LruCache<String, Bitmap> mMemoryCache;


    public SnapAdapter(List<Snapnote> notes,Context context){

        this.notes = notes;
        this.context = context;

        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @Override
    public SnapnoteViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.note_list_item, viewGroup, false);
        SnapnoteViewHolder snh = new SnapnoteViewHolder(v);
        return snh;
    }

    @Override
    public void onBindViewHolder(SnapAdapter.SnapnoteViewHolder snapnoteViewHolder, int i) {
        snapnoteViewHolder.noteDate.setText(notes.get(i).getDate());
        snapnoteViewHolder.dueDate.setText(notes.get(i).getDuedate());

         loadBitmap(notes.get(i).getPhotoUrl(),snapnoteViewHolder.notePhoto);

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void addItemsToList(Snapnote note) {
        notes.add(note);
    }

    public void loadBitmap(String url, ImageView imageView) {
        if (cancelPotentialWork(url, imageView)) {

            final String imageKey = url;

            final Bitmap bitmap = getBitmapFromMemCache(imageKey);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {

                Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);

                final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
                final AsyncDrawable asyncDrawable =
                        new AsyncDrawable(context.getResources(), bm, task);
                imageView.setImageDrawable(asyncDrawable);
                task.execute(url);

            }

        }
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    public  boolean cancelPotentialWork(String data, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
            final String bitmapData = bitmapWorkerTask.url;
            // If bitmapData is not yet set or it differs from the new data
            if (bitmapData == "" || !bitmapData.equals(data)) {
                // Cancel previous task
                bitmapWorkerTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }

    public  BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    public class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {

        private final WeakReference<ImageView> imageViewReference;
        public String url = "";



        public BitmapWorkerTask(ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<ImageView>(imageView);
        }


        // Decode image in background.
        @Override
        protected Bitmap doInBackground(String... params) {
            url = params[0];
            final int THUMBSIZEW = 800;
            final int THUMBSIZEH = 400;

            Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(url),
                    THUMBSIZEW, THUMBSIZEH);
            /*Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());*/
            addBitmapToMemoryCache(String.valueOf(params[0]), ThumbImage);

            return ThumbImage;

        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                final BitmapWorkerTask bitmapWorkerTask =
                        getBitmapWorkerTask(imageView);
                if (this == bitmapWorkerTask && imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }

        }

    }











}
