package com.hunter.appinfomonitor.yodo1page;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hunter.appinfomonitor.BaseActvity;
import com.hunter.appinfomonitor.R;
import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Error;
import com.tonyodev.fetch2.FetchListener;
import com.tonyodev.fetch2core.DownloadBlock;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @author yodo1
 */
public class DownloadListActivity extends BaseActvity implements FetchListener {

    private ListView lv;
    private DownloadAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_downloadlist);
        lv = findViewById(R.id.listview);

        initData();

        if (DownloadServerice.getDownloadingList().size() == 0) {
            Toast.makeText(this, "没有下载任务", Toast.LENGTH_SHORT).show();
        }
    }

    private void initData() {
        HashMap<Integer, Integer> downloadingList = DownloadServerice.getDownloadingList();
        List<DB> data = new ArrayList<>();
        if (!downloadingList.isEmpty()) {
            Set<Integer> integers = downloadingList.keySet();
            for (Integer i : integers) {
                DB d = new DB();
                d.downlaodId = i;
                d.progress = downloadingList.get(i);
                data.add(d);
            }
        }

        adapter = new DownloadAdapter(this);
        adapter.setData(data);
        lv.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        DownloadServerice.getInstance().getFetch().addListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        DownloadServerice.getInstance().getFetch().removeListener(this);
    }

    @Override
    public void onAdded(@NotNull Download download) {

    }

    @Override
    public void onCancelled(@NotNull Download download) {

    }

    @Override
    public void onCompleted(@NotNull Download download) {
        initData();
    }

    @Override
    public void onDeleted(@NotNull Download download) {

    }

    @Override
    public void onDownloadBlockUpdated(@NotNull Download download, @NotNull DownloadBlock downloadBlock, int i) {

    }

    @Override
    public void onError(@NotNull Download download, @NotNull Error error, @org.jetbrains.annotations.Nullable Throwable throwable) {

    }

    @Override
    public void onPaused(@NotNull Download download) {

    }

    @Override
    public void onProgress(@NotNull Download download, long l, long l1) {
        List<DB> dbs = adapter.getmData();
        for (DB d : dbs) {
            if (d.downlaodId == download.getId()) {
                d.download = download;
                break;
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onQueued(@NotNull Download download, boolean b) {

    }

    @Override
    public void onRemoved(@NotNull Download download) {

    }

    @Override
    public void onResumed(@NotNull Download download) {

    }

    @Override
    public void onStarted(@NotNull Download download, @NotNull List<? extends DownloadBlock> list, int i) {

    }

    @Override
    public void onWaitingNetwork(@NotNull Download download) {

    }

    class DB {
        Integer downlaodId;
        Integer progress;
        Download download;
    }

    private class DownloadAdapter extends BaseAdapter {

        private List<DB> mData;
        private Context context;

        public DownloadAdapter(DownloadListActivity downloadListActivity) {
            super();
            context = downloadListActivity;
            mData = new ArrayList<>();
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            VH vh = null;
            if (convertView == null) {
                convertView = View.inflate(DownloadListActivity.this, R.layout.download_item, null);
                vh = new VH();
                vh.name = convertView.findViewById(R.id.downloaditem_fileName);
                vh.desp = convertView.findViewById(R.id.downloaditem_filepath);
                vh.progressBar = convertView.findViewById(R.id.donwloaditem_progress);
                convertView.setTag(vh);
            } else {
                vh = (VH) convertView.getTag();
            }
            DB db = mData.get(position);
            String s = "DownloadId:" + db.downlaodId;
            if (db.download != null) {
                s += "     已下载:" + Formatter.formatFileSize(context, db.download.getDownloaded()) + "  总大小:" + Formatter.formatFileSize(context, db.download.getTotal());
            }
            vh.name.setText(s);
            Integer progress = db.progress;
            if (db.download != null) {
                long l = ((db.download.getDownloaded() * 100) / db.download.getTotal());
                progress = (int) l;
            }
            String ss = null;
            if (db.download != null) {
                ss = "path:" + db.download.getFile();
            }
            vh.desp.setText(ss);
            vh.progressBar.setProgress(progress);
            return convertView;
        }

        public void setData(List<DB> data) {
            mData.clear();
            mData.addAll(data);
            notifyDataSetChanged();
        }

        public List<DB> getmData() {
            return mData;
        }

        class VH {
            TextView name, desp;
            ProgressBar progressBar;
        }
    }
}
