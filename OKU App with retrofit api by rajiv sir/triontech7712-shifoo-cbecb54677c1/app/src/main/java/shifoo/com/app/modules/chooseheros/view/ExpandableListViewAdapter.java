package shifoo.com.app.modules.chooseheros.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import oku.app.R;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {

    private Context context;

    // group titles
    private List<String> listDataGroup;

    // child data in format of header title, child title
    private HashMap<String, List<PersonalityItemDetail>> listDataChild;

    public ExpandableListViewAdapter(Context context, List<String> listDataGroup,
                                     HashMap<String, List<PersonalityItemDetail>> listDataChild) {
        this.context = context;
        this.listDataGroup = listDataGroup;
        this.listDataChild = listDataChild;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listDataChild.get(this.listDataGroup.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final PersonalityItemDetail personalityItemDetail = (PersonalityItemDetail) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.row_list_child, null);
        }


        TextView textViewChild = convertView
                .findViewById(R.id.textViewChild);
        TextView textView = convertView
                .findViewById(R.id.textView);
        Typeface Celebritychild = Typeface.createFromAsset(context.getAssets(), "fonts/JosefinSans_Bold.ttf");
        textViewChild.setTypeface(Celebritychild);
        textView.setTypeface(Celebritychild);

        if (personalityItemDetail.getHeader() != null) {
            textViewChild.setText(personalityItemDetail.getHeader());
            textViewChild.setVisibility(View.VISIBLE);

        } else {
            textViewChild.setVisibility(View.GONE);
        }

        textView.setText(personalityItemDetail.getValue());


        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild.get(this.listDataGroup.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataGroup.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataGroup.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.row_list_group, null);
        }

        TextView textViewGroup = convertView
                .findViewById(R.id.textViewGroup);
        Typeface Celebritygroup = Typeface.createFromAsset(context.getAssets(), "fonts/JosefinSans_Bold.ttf");
        textViewGroup.setTypeface(Celebritygroup);

       // textViewGroup.setTypeface(null, Typeface.BOLD);
        textViewGroup.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}