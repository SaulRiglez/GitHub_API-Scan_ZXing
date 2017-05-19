package com.yoprogramo.github_app.utilities;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yoprogramo.github_app.R;
import com.yoprogramo.github_app.entities.RepoUser;

import java.util.List;

/**
 * Created by User on 5/17/2017.
 */

public class ReposAdapter extends RecyclerView.Adapter<ReposAdapter.RepoViewHolder>{

    Context context;
    List<RepoUser> listRepo;

    public ReposAdapter( List<RepoUser> listRepo,Context context) {
        this.context = context;
        this.listRepo = listRepo;
    }

    @Override
    public ReposAdapter.RepoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_repo_data, parent, false);
        return new RepoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReposAdapter.RepoViewHolder holder, int position) {

        RepoUser repo = this.listRepo.get(position);
        holder.textView.setText(repo.getName());

        Log.d("viewHolder", "onBindViewHolder: " + repo.getName() );

    }

    @Override
    public int getItemCount() {
        return listRepo.size();
    }

    public class RepoViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public RepoViewHolder(View itemView) {
            super(itemView);

            textView = ((TextView) itemView.findViewById(R.id.text_view));
        }
    }
}
