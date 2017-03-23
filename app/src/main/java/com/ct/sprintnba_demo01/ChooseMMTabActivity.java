package com.ct.sprintnba_demo01;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;

import com.ct.sprintnba_demo01.base.BaseActivity;
import com.ct.sprintnba_demo01.base.adapter.listener.OnRvItemTouchListener;
import com.ct.sprintnba_demo01.base.adapter.listener.OnRvItemTouchListenerAdapter;
import com.ct.sprintnba_demo01.base.utils.ECache;
import com.ct.sprintnba_demo01.constant.Column_MM;
import com.ct.sprintnba_demo01.madapter.ChooseAdapter;
import com.ct.sprintnba_demo01.madapter.callback.ItemHelperCallback;
import com.ct.sprintnba_demo01.mentity.TabEntity;
import com.ct.sprintnba_demo01.mutils.SpaceItemDecoration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * mm界面tab选择界面
 */
public class ChooseMMTabActivity extends BaseActivity {

    @BindView(R.id.recycler_choose_mmtab)
    RecyclerView recyclerView_mmtab;
    @BindView(R.id.recycler_choose_mmmore)
    RecyclerView recyclerView_mmmore;

    private ChooseAdapter adapter;
    private List mList;

    private ECache eCache;
    private Gson gson = new Gson();

    @Override
    public int getLayoutId() {
        return R.layout.activity_choose_mmtab;
    }

    @Override
    public void initViewsAndEvents(Bundle savedInstanceState) {
        setTitle("频道选择界面");

        mList = new ArrayList<TabEntity<Column_MM>>();
        eCache = ECache.get(this, "Tab", false);

        adapter = new ChooseAdapter(this, mList);
        recyclerView_mmtab.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView_mmtab.setAdapter(adapter);
        recyclerView_mmtab.addItemDecoration(new SpaceItemDecoration(this, 5));

        ItemTouchHelper.Callback callback = new ItemHelperCallback(adapter);
        final ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView_mmtab);

        recyclerView_mmtab.addOnItemTouchListener(new OnRvItemTouchListener(recyclerView_mmtab, new OnRvItemTouchListenerAdapter() {
            @Override
            public void onItemLongClick(RecyclerView.ViewHolder holder) {

                if (holder.getAdapterPosition() != 0)
                    helper.startDrag(holder);
                
            }
        }));

    }

    @Override
    protected void onResume() {
        super.onResume();
        List<TabEntity<Column_MM>> list = getSelectedTab();
        if (list == null || list.size() == 0)
            list = getTab();
        mList.clear();
        mList.addAll(list);


    }

    private List<TabEntity<Column_MM>> getSelectedTab() {
        List<TabEntity<Column_MM>> cacheList = null;
        String json = eCache.getAsString("tab_json");
        if (!TextUtils.isEmpty(json)) {
            cacheList = gson.fromJson(json, new TypeToken<List<TabEntity<Column_MM>>>() {
            }.getType());

        }
        return cacheList;
    }


    private List<TabEntity<Column_MM>> getTab() {
        String[] titles = {"校花", "清纯", "气质", "萌女", "壁纸", "非主流", "明星", "美术", "家居", "型男", "萌宠"};
        Column_MM[] columnMms = {
                Column_MM.SCHOOL_BABE,
                Column_MM.PURE,
                Column_MM.TEMPERAMENT,
                Column_MM.LOLITA,
                Column_MM.WALLPAPER,
                Column_MM.ALTERNATIVE,
                Column_MM.STAR,
                Column_MM.ART,
                Column_MM.HOME,
                Column_MM.MAN,
                Column_MM.PET
        };

        List<TabEntity<Column_MM>> list = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            TabEntity<Column_MM> entity = new TabEntity<>();
            entity.title = titles[i];
            entity.tag = columnMms[i];
            list.add(entity);

        }
        eCache.put("tab_json", gson.toJson(list));
        return list;

    }


    private List<TabEntity<Column_MM>> getNotSelectTab() {
        List<TabEntity<Column_MM>> cacheList = null;
        String json = eCache.getAsString("tab_json_no");
        if (!TextUtils.isEmpty(json)) {
            cacheList = gson.fromJson(json, new TypeToken<List<TabEntity<Column_MM>>>() {
            }.getType());

        }
        return cacheList;
    }

    private List<TabEntity<Column_MM>> getNoTab() {
        String[] titles = {"校花", "清纯", "气质", "萌女", "壁纸", "非主流", "明星", "美术", "家居", "型男", "萌宠"};
        Column_MM[] columnMms = {
                Column_MM.SCHOOL_BABE,
                Column_MM.PURE,
                Column_MM.TEMPERAMENT,
                Column_MM.LOLITA,
                Column_MM.WALLPAPER,
                Column_MM.ALTERNATIVE,
                Column_MM.STAR,
                Column_MM.ART,
                Column_MM.HOME,
                Column_MM.MAN,
                Column_MM.PET
        };

        List<TabEntity<Column_MM>> list = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            TabEntity<Column_MM> entity = new TabEntity<>();
            entity.title = titles[i];
            entity.tag = columnMms[i];
            list.add(entity);

        }
        eCache.put("tab_json", gson.toJson(list));
        return list;

    }

}
