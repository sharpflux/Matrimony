package com.example.matrimonyapp.adapter;

import android.content.Context;
import android.content.ContextWrapper;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.customViews.CustomDialogAddSibling;
import com.example.matrimonyapp.customViews.CustomDialogDotMenuEditDelete;
import com.example.matrimonyapp.customViews.CustomDialogViewFarm;
import com.example.matrimonyapp.customViews.CustomDialogViewLanguageKnown;
import com.example.matrimonyapp.customViews.CustomDialogViewMama;
import com.example.matrimonyapp.customViews.CustomDialogViewProperty;
import com.example.matrimonyapp.customViews.CustomDialogViewSibling;
import com.example.matrimonyapp.modal.AddPersonModel;
import com.example.matrimonyapp.modal.ChatModel;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ViewMultipleDetailsAdapter extends RecyclerView.Adapter<ViewMultipleDetailsAdapter.ViewHolder> {


    Context context;
    private int pos;
    private LayoutInflater layoutInflater;
    ArrayList<AddPersonModel> list;
    String viewDialog;


    public ViewMultipleDetailsAdapter(Context context, ArrayList<AddPersonModel> list, String viewDialog)
    {
        this.context = context;
        this.list = list;
        this.viewDialog = viewDialog;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.recycler_view_view_multiple_details, parent, false);
        ViewMultipleDetailsAdapter.ViewHolder viewHolder = new ViewMultipleDetailsAdapter.ViewHolder(listItem);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final AddPersonModel model = list.get(position);

        holder.textView_id.setText(model.getId());
        holder.textView_name.setText(model.getName());
        holder.textView_address.setText(model.getAddress());
        //holder.textView_qualification.setText(model.getQualification());

        if(viewDialog.equals("Language"))
        {
            holder.textView_view.setVisibility(View.GONE);
        }


        holder.textView_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(viewDialog.equals("Sibling"))
                {
                    CustomDialogViewSibling customDialogViewSibling =
                        new CustomDialogViewSibling(context, model.getId(),  model.getDetails_id(),
                                ViewMultipleDetailsAdapter.this, list, position);
                    customDialogViewSibling.show();

                }
                else if(viewDialog.equals("Language"))
                {
                    /*CustomDialogViewLanguageKnown customDialogViewLanguageKnown =
                            new CustomDialogViewLanguageKnown(context, model.getId(),  model.getDetails_id(),
                                    ViewMultipleDetailsAdapter.this, list, position);
                    customDialogViewLanguageKnown.show();*/
                }
                else if(viewDialog.equals("Property"))
                {
                    CustomDialogViewProperty customDialogViewProperty =
                            new CustomDialogViewProperty(context, model.getId(),  model.getDetails_id(),
                                    ViewMultipleDetailsAdapter.this, list, position);
                    customDialogViewProperty.show();
                }
                else if(viewDialog.equals("Mama"))
                {
                    CustomDialogViewMama customDialogViewMama =
                            new CustomDialogViewMama(context, model.getId(),  model.getDetails_id(),
                                    ViewMultipleDetailsAdapter.this, list, position);
                    customDialogViewMama.show();
                }
                else if(viewDialog.equals("Farm"))
                {
                    CustomDialogViewFarm customDialogViewFarm =
                            new CustomDialogViewFarm(context, model.getId(),  model.getDetails_id(),
                                    ViewMultipleDetailsAdapter.this, list, position);
                    customDialogViewFarm.show();
                }



            }
        });




    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{


        public  TextView textView_id, textView_name, textView_address;
        TextView textView_view; //, textView_dotMenu


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.textView_id = itemView.findViewById(R.id.textView_id);
            this.textView_name = itemView.findViewById(R.id.textView_name);
            this.textView_address= itemView.findViewById(R.id.textView_address);
            this.textView_view= itemView.findViewById(R.id.textView_view);
            //this.textView_qualification = itemView.findViewById(R.id.textView_qualification);
            //this.textView_dotMenu= itemView.findViewById(R.id.textView_dotMenu);


        }
    }
}
