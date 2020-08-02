package com.example.matrimonyapp.modal;



import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by deepshikha on 3/3/17.
 */

public class GalleryFolderModel  {
    String folderName;
    ArrayList<GalleryImageModel> galleryImageModelArrayList ;

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public ArrayList<GalleryImageModel> getGalleryImageModelArrayList() {
        return galleryImageModelArrayList;
    }

    public void setGalleryImageModelArrayList(ArrayList<GalleryImageModel> galleryImageModelArrayList) {
        this.galleryImageModelArrayList = galleryImageModelArrayList;
    }

    public static Comparator<GalleryFolderModel> folderModelComparator = new Comparator<GalleryFolderModel>() {
        @Override
        public int compare(GalleryFolderModel o1, GalleryFolderModel o2) {

            String folderName1 = o1.getFolderName().toUpperCase();
            String folderName2 = o2.getFolderName().toUpperCase();

            // Ascending order
            return folderName1.compareTo(folderName2);

/*
            // Descending order
            return str_folder2.compareTo(str_folder1);
*/

        }
    };

}
