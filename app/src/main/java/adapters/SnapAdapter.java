package adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps.argenis.snapnotes.R;

import java.util.List;

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

    public SnapAdapter(List<Snapnote> notes){
        this.notes = notes;
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
        snapnoteViewHolder.notePhoto.setImageBitmap(notes.get(i).getNoteImg());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


}
