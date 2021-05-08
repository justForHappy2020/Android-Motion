package com.example.motion.Widget;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.motion.Entity.CourseTagGroup;
import com.example.motion.R;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class SelectionTagsAdapter extends BaseQuickAdapter<CourseTagGroup, BaseViewHolder>{

    public SelectionTagsAdapter(@LayoutRes int layoutResId, @Nullable List<CourseTagGroup> courseTagGroups) {
        super(layoutResId, courseTagGroups);
    }

    @Override
    protected void convert(BaseViewHolder helper, CourseTagGroup tagGroup) {
        helper.setText(R.id.tv_tag_menu_name, tagGroup.getGroupName());

        TabLayout tabLayout = helper.findView(R.id.tl_tags);

        for(int i=0;i<tagGroup.getCourseTagList().size();i++){
            tabLayout.addTab(tabLayout.newTab().setText(tagGroup.getCourseTagList().get(i).getTagName()));
            /*
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            Log.d("SelectionTagsAdapter","tagGroup.getCourseTagList().get(i).getTagName()="+tagGroup.getCourseTagList().get(i).getTagName());
            if (tab != null) {
                tab.setText(tagGroup.getCourseTagList().get(i).getTagName());
            }

             */
        }

    }
}