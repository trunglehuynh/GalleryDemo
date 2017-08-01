package trunghuynh.samsungcodingchallenge;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by trunghuynh on 7/31/17.
 */

public class MyListViewAdapter extends ArrayAdapter<RowObject> {

    final String TAG ="MyListViewAdapter";
    int layoutId, imageId, textId;
    RowObject itemData;
    //    int HeightDp;
    public MyListViewAdapter(Context context, int layoutId, int imageId, int textId, RowObject[] objects) {
        super(context, layoutId, objects);
        this.layoutId = layoutId;
        this.imageId = imageId;
        this.textId = textId;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.d(TAG, "getView "+ position);

        MyViewHolder holder = null;

        if(convertView == null)
        {

            LayoutInflater itemInflater =  LayoutInflater.from(getContext());
            convertView = itemInflater.inflate(layoutId,parent,false);
            holder = new MyViewHolder(convertView, imageId, textId);

            convertView.setTag(holder);
        }else{

            holder = (MyViewHolder)convertView.getTag();
//            Log.d(TAG, "getView reusing"+ position);
        }
        itemData = getItem(position);
        holder.textView.setText(itemData.getImageName());
        holder.imageView.setImageBitmap(itemData.getImageBitmap());
        return convertView;
    }





}
class MyViewHolder{
    ImageView imageView;
    TextView textView;
    MyViewHolder(View view, int imageId, int textId){
        imageView = (ImageView)view.findViewById(imageId);
        textView = (TextView)view.findViewById(textId);
    }
}

