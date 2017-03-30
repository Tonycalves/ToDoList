package com.example.jatal.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public final class NotesAdapter extends ArrayAdapter<Note> {
    public NotesAdapter(Context context, ArrayList<Note> notes) {
        super(context, 0, notes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Note note = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_note, parent, false);
        }
        // Lookup view for data population
        TextView noteN = (TextView) convertView.findViewById(R.id.noteName);
        TextView noteC = (TextView) convertView.findViewById(R.id.noteContent);
        TextView noteD = (TextView) convertView.findViewById(R.id.noteDate);
        TextView noteS = (TextView) convertView.findViewById(R.id.noteDateS);
        ImageView imgTdo = (ImageView) convertView.findViewById(R.id.todo);
        
        imgTdo.setImageResource(R.drawable.ic_todo);
        // Populate the data into the template view using the data object
        if (note != null) {
            noteN.setText(note.getName());
            noteC.setText(note.getContent());
            if (note.getState() == 0)
                imgTdo.setImageResource(R.drawable.ic_doing);
            else if (note.getState() == 1)
                imgTdo.setImageResource(R.drawable.ic_done);
            if (note.getRemind()) {
                noteS.setText(" - ");
                noteD.setText(note.getDay() + "/" + note.getMonth());
                noteS.setVisibility(View.VISIBLE);
                noteD.setVisibility(View.VISIBLE);
            }
            else {
                noteS.setVisibility(View.INVISIBLE);
                noteD.setVisibility(View.INVISIBLE);
            }
        }
        // Return the completed view to render on screen
        return convertView;
    }
}
