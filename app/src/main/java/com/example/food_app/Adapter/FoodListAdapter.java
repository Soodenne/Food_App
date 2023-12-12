//package com.example.food_app.Adapter;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
//import com.example.food_app.Activity.ConnectionHelper;
//import com.example.food_app.Activity.DetailActivity;
//import com.example.food_app.Domain.FoodDomain;
//import com.example.food_app.R;
//
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
//
//public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.ViewHolder>{
//    ArrayList<FoodDomain> items;
//    Connection connect;
//    public FoodListAdapter(ArrayList<FoodDomain> items) {
//        this.items = items;
//    }
//
//    Context context;
//    @NonNull
//    @Override
//    public FoodListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_food_list,parent,false);
//        context = parent.getContext();
//        return new ViewHolder(inflate);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        try{
//            ConnectionHelper connectionHelper = new ConnectionHelper();
//            connect = connectionHelper.connectionclass();
//            if(connect!= null){
//                String query = "Select * from [dbo].[Food]";
//                Statement st = connect.createStatement();
//                ResultSet rs = st.executeQuery(query);
//                while (rs.next()){
//                    holder.title_txt.setText(rs.getString(1));
//                    holder.time_txt.setText(rs.getString(5) + " min");
//                    holder.score_txt.setText(""+rs.getString(7));
//                    int drawableResourceId = holder.itemView.getResources().getIdentifier(rs.getString(3),"drawable", holder.itemView.getContext().getPackageName());
//
//                    Glide.with(holder.itemView.getContext())
//                            .load(drawableResourceId)
//                            .transform(new GranularRoundedCorners(30,30,0,0))
//                            .into(holder.pic);
//                    holder.itemView.setOnClickListener(new View.OnClickListener(){
//                        @Override
//                        public void onClick(View v){
//                            Intent intent =  new Intent(holder.itemView.getContext(), DetailActivity.class);
//                            intent.putExtra("object", items.get(position));
//                            holder.itemView.getContext().startActivity(intent);
//                        }
//                    });
//                }
//            }
//            else{
//
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return items.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder{
//    TextView title_txt, time_txt, score_txt;
//    ImageView pic;
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            title_txt = itemView.findViewById(R.id.title_txt);
//            time_txt = itemView.findViewById(R.id.time_txt);
//            score_txt = itemView.findViewById(R.id.scrore_txt);
//            pic = itemView.findViewById(R.id.pic);
//        }
//    }
//}
package com.example.food_app.Adapter;

        import android.content.Context;
        import android.content.Intent;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

        import com.bumptech.glide.Glide;
        import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
        import com.example.food_app.Activity.DetailActivity;
        import com.example.food_app.Model.FoodDomain;
        import com.example.food_app.R;

        import java.util.ArrayList;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.ViewHolder>{
    ArrayList<FoodDomain> items;

    public FoodListAdapter(ArrayList<FoodDomain> items) {
        this.items = items;
    }

    Context context;
    @NonNull
    @Override
    public FoodListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_food_list,parent,false);
        context = parent.getContext();
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title_txt.setText(items.get(position).getTitle());
        holder.time_txt.setText(items.get(position).getTime() + " min");
        holder.score_txt.setText(""+items.get(position).getScore());
        int drawableResourceId = holder.itemView.getResources().getIdentifier(items.get(position).getPicUrl(),"drawable", holder.itemView.getContext().getPackageName());

        Glide.with(holder.itemView.getContext())
                .load(drawableResourceId)
                .transform(new GranularRoundedCorners(30,30,0,0))
                .into(holder.pic);
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent =  new Intent(holder.itemView.getContext(), DetailActivity.class);
                intent.putExtra("object", items.get(position));
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title_txt, time_txt, score_txt;
        ImageView pic;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title_txt = itemView.findViewById(R.id.title_txt);
            time_txt = itemView.findViewById(R.id.time_txt);
            score_txt = itemView.findViewById(R.id.scrore_txt);
            pic = itemView.findViewById(R.id.pic);
        }
    }
}

