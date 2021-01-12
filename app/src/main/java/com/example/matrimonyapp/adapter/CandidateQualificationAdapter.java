package com.example.matrimonyapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.customViews.CustomDialogDotMenuEditDelete;
import com.example.matrimonyapp.modal.AddPersonModel;
import com.example.matrimonyapp.modal.CandidateEducationModel;

import java.util.ArrayList;

public class CandidateQualificationAdapter extends RecyclerView.Adapter<CandidateQualificationAdapter.ViewHolder> {

    Context context;
    private int pos;
    private LayoutInflater layoutInflater;
    ArrayList<CandidateEducationModel> list;
    String relation;


    public CandidateQualificationAdapter(Context context, ArrayList<CandidateEducationModel> list)
    {
        this.context = context;
        this.list = list;
        this.relation = relation;
    }

    @NonNull
    @Override
    public CandidateQualificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.recycler_view_add_person, parent, false);
        CandidateQualificationAdapter.ViewHolder viewHolder = new CandidateQualificationAdapter.ViewHolder(listItem);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull final CandidateQualificationAdapter.ViewHolder holder, final int position) {

        final CandidateEducationModel model = list.get(position);

        holder.textView_id.setText(model.getQualificationLevelId());
        holder.textView_name.setText(model.getQualification());
        holder.textView_address.setText(model.getQualificationLevelName());
        //holder.textView_qualification.setText(model.getQualification());



        holder.textView_dotMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             /*   CustomDialogDotMenuEditDelete customDialogDotMenuEditDelete =
                        new CustomDialogDotMenuEditDelete(context, model.getQualificationLevelId(), model.getQualificationLevelId(), CandidateQualificationAdapter.this,
                                list, position, relation);
                customDialogDotMenuEditDelete.show();*/

                /*Context wrapper = new ContextThemeWrapper(context, R.style.popupDotMenu);

                PopupMenu popupMenu = new PopupMenu(wrapper, holder.textView_dotMenu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        switch (menuItem.getItemId())
                        {

                            case R.id.edit :
                                CustomDialogAddSibling customDialogAddSibling = new CustomDialogAddSibling(context, model.getId());
                                customDialogAddSibling.show();
                                return true;

                            case R.id.delete :

                                return true;

                            default:
                                return false;
                        }


                    }
                });

                popupMenu.inflate(R.menu.menu_edit_delete);
                popupMenu.show();
*/



            }
        });


    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{


        public TextView textView_id, textView_name, textView_address, textView_dotMenu;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.textView_id = itemView.findViewById(R.id.textView_id);
            this.textView_name = itemView.findViewById(R.id.textView_name);
            this.textView_address= itemView.findViewById(R.id.textView_address);
            //this.textView_qualification = itemView.findViewById(R.id.textView_qualification);
            this.textView_dotMenu= itemView.findViewById(R.id.textView_dotMenu);


        }
    }

}
