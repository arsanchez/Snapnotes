package clases;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

/**
 * Created by Argenis on 6/16/15.
 */
public class Snapnote {

     int id;
     String date;
     private String duedate;
     private String photoUrl;

    //emmpty constructor
    public Snapnote(){

    }
    //constructor
    public Snapnote(int id,String date, String duedate, String photoUrl)
    {
        this.id = id;
        this.date = date;
        this.setDuedate(duedate);
        this.setPhotoUrl(photoUrl);
    }

    //constructor
    public Snapnote(String date, String duedate, String photoUrl)
    {
        this.date = date;
        this.setDuedate(duedate);
        this.setPhotoUrl(photoUrl);
    }

    public int getID(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getDate(){
        return this.date;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getDuedate() {
        return duedate;
    }

    public void setDuedate(String duedate) {
        this.duedate = duedate;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Bitmap getNoteImg()
    {
        File imgFile = new  File(this.photoUrl);

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            return myBitmap;
        }

        return null;
    }
}
