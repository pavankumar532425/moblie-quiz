package mark1.mobiquiz.harsha.com.mobiquiz;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.StringTokenizer;
import java.util.Vector;

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    Context context;
    Vector items;
    public Adapter(Context context,Vector items)
    {
        this.context=context;
        this.items=items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.custom_row,parent,false);
        Item item = new Item(row);
        return item;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {

        StringTokenizer str=new StringTokenizer(items.get(position).toString());

        ((Item)holder).rank.setText(str.nextToken("~"));
        ((Item)holder).name.setText(str.nextToken("~"));
        ((Item)holder).points.setText(str.nextToken("~"));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Item extends RecyclerView.ViewHolder
    {
        TextView rank,name,points;
        public Item(View itemView) {
            super(itemView);
            rank = (TextView) itemView.findViewById(R.id.rhrank);
            name = (TextView) itemView.findViewById(R.id.rhname);
            points = (TextView) itemView.findViewById(R.id.rhpoints);
        }
    }

}

