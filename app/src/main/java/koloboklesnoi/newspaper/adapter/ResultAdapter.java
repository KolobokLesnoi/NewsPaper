package koloboklesnoi.newspaper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import koloboklesnoi.newspaper.R;
import koloboklesnoi.newspaper.model.Result;

import java.util.List;

public class ResultAdapter extends ArrayAdapter<Result>{

    List<Result> resultList;
    Context context;
    private LayoutInflater layoutInflater;

    public ResultAdapter(Context context, List<Result> resultList){
        super(context, 0, resultList);
        this.resultList = resultList;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public Result getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if(convertView == null){
            View view = layoutInflater.inflate(R.layout.article_title_view, parent, false);
            viewHolder = ViewHolder.create((LinearLayout) view);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Result result = getItem(position);
        viewHolder.titleView.setText(result.getTitle());


        return viewHolder.rootView;

    }

    private static class ViewHolder{
        public final LinearLayout rootView;
        public final TextView titleView;

        public ViewHolder(LinearLayout rootView, TextView textView) {
            this.rootView = rootView;
            this.titleView = textView;
        }

        public static ViewHolder create(LinearLayout rootView){
            TextView titleVIew = (TextView)rootView.findViewById(R.id.titleView);
            return new ViewHolder(rootView,titleVIew);
        }
    }

    public List<Result> getResultList() {
        return resultList;
    }
}
