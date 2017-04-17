package com.kyview.testdemo;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.TextView;

import com.kyview.InitConfiguration;
import com.kyview.interfaces.AdViewNativeListener;
import com.kyview.manager.AdViewNativeManager;
import com.kyview.natives.NativeAdInfo;
import com.kyview.util.AdViewUtil;

public class AdNativeActivity extends Activity implements AdViewNativeListener,MyItemClickListener{
	public static String HTML = "<meta charset='utf-8'><style type='text/css'>* { padding: 0px; margin: 0px;}a:link { text-decoration: none;}</style><div  style='width: 100%; height: 100%;'><img src=\"image_path\" width=\"100%\" height=\"100%\" ></div>";
	private NativeAdAdapter adAdapter;
	private ArrayList<Data> list;
	InitConfiguration initConfiguration;

	public final static int STREAM = 1;
	public final static int STREAM_AD = 2;
	private RecyclerView recyclerView;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_adnative);


		//RecyclerView for Native ad
		recyclerView = (RecyclerView) findViewById(R.id.list);
		LayoutManager layoutManager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(layoutManager);

//		recyclerView.addItemDecoration(new DividerItemDecoration(this));

		//Data Class Type Array List for RecyclerView Data
		list = new ArrayList<AdNativeActivity.Data>();

		//RecyclerView Data items
		//user defined data
		for (int i = 0; i < 10; i++) {
			Data data = new Data();
			data.setTitle("AdView, a mobile Exchange platform");
			data.setIcon("http://www.adview.cn/static/images/logo_1.png");
			data.setImage("");
			data.setDescript("AdView is the largest third-party mobile Ad Exchange in China）");
			data.setType(STREAM);
			data.setAd(false);
			list.add(data);
		}

		adAdapter = new NativeAdAdapter(list);
		recyclerView.setAdapter(adAdapter);

		adAdapter.setOnItemClickListener(this);


		//Intialization for Native advertisement
		AdViewNativeManager.getInstance(this).init(MainActivity.initConfiguration,new String[]{MainActivity.SDK_KEY});

		//Requesting Native Advertisement
		AdViewNativeManager.getInstance(this).requestAd(this, MainActivity.SDK_KEY, 1, this);
	}

	@Override
	public void onAdFailed(String arg0) {

	}


	// Ad successfully Received
	@Override
	public void onAdRecieved(String arg1, ArrayList arg0) {
		for (int i = 0; i < arg0.size(); i++) {
			Data data = new Data();
			NativeAdInfo nativeAdInfo = (NativeAdInfo) arg0.get(i);
			data.title = ((NativeAdInfo) arg0.get(i)).getTitle();
			data.descript = nativeAdInfo.getDescription();
			data.icon = nativeAdInfo.getIconUrl();
			data.image= nativeAdInfo.getImageUrl();
			data.adInfo = (NativeAdInfo) arg0.get(i);
			((NativeAdInfo) arg0.get(i)).getIconHeight();
			data.setAd(true);
			data.setType(STREAM_AD);
			Log.i("Native info：", "data.title:" + data.title + "\ndata.descript: " + data.descript + "\ndata.icon: "
					+ data.icon + "\ndata.image: " + data.image );
			list.add(3, data);
			((NativeAdInfo) arg0.get(i)).onDisplay(new View(
					AdNativeActivity.this));
		}
		adAdapter.notifyDataSetChanged();

	}

	@Override
	public void onAdStatusChanged(String arg0, int arg1) {

	}


	/**
	 * NativeAdAdapter for RecyclerView Adapter
	 */



	class NativeAdAdapter extends RecyclerView.Adapter<ViewHolder>{
		private ArrayList<Data> list;
		private MyItemClickListener mListener=null;

		public NativeAdAdapter(ArrayList<Data> list) {
			this.list = list;
		}

		public class StreamHolder extends RecyclerView.ViewHolder {

			private TextView stream_adTitle;
			private TextView stream_adSubTitle;
			private WebView stream_adIcon;

			public StreamHolder(View v) {
				super(v);
				stream_adTitle = (TextView) v
						.findViewById(R.id.title);
				stream_adSubTitle = (TextView) v
						.findViewById(R.id.desc);
				stream_adIcon = (WebView) v
						.findViewById(R.id.icon);
			}
		}

		public class StreamHolderAd extends RecyclerView.ViewHolder implements OnClickListener{

			private TextView stream_adTitle;
			private TextView stream_adSubTitle;
			private WebView stream_adIcon;
			private WebView stream_adImage;
			private TextView stream_adLogo;
			private MyItemClickListener mListener;

			public StreamHolderAd(View v, MyItemClickListener listener) {
				super(v);
				stream_adTitle = (TextView) v
						.findViewById(R.id.title);
				stream_adSubTitle = (TextView) v
						.findViewById(R.id.desc);
				stream_adIcon = (WebView) v
						.findViewById(R.id.icon);
				stream_adImage = (WebView) v
						.findViewById(R.id.image);
				stream_adLogo=(TextView) v.findViewById(R.id.logo);
				mListener = listener;
				v.setOnClickListener(this);

			}

			@Override
			public void onClick(View v) {
				if(mListener != null){
					mListener.onItemClick(v,getPosition());
				}

			}


		}

		@Override
		public int getItemCount() {
			return list.size();
		}

		@Override
		public int getItemViewType(int position) {
			return list.get(position).getType();
		}


		@Override
		public void onBindViewHolder(ViewHolder holder, final int position) {
			switch (getItemViewType(position)) {
			case STREAM:
				StreamHolder streamHolder = (StreamHolder) holder;
				streamHolder.stream_adTitle.setText((String) list.get(position).getTitle());
				streamHolder.stream_adSubTitle.setText((String) list.get(position).getDescript());
				streamHolder.stream_adIcon.loadData((new String(HTML)).replace("image_path",
						list.get(position).getIcon()), "text/html; charset=UTF-8", null);
				streamHolder.itemView.setTag(position);
				break;
			case STREAM_AD:
//				if(list.get(position).getType()==STREAM_AD && null!=list.get(position).getImage()){
				StreamHolderAd streamHolderAd = (StreamHolderAd) holder;
				streamHolderAd.stream_adTitle.setText((String) list.get(position).getTitle());
				streamHolderAd.stream_adSubTitle.setText((String) list.get(position).getDescript());
				if(null!=list.get(position).getIcon())
					streamHolderAd.stream_adIcon.loadData((new String(HTML)).replace("image_path",
						list.get(position).getIcon()), "text/html; charset=UTF-8", null);

				if(null!=list.get(position).getImage()){
					streamHolderAd.stream_adImage.loadData((new String(HTML)).replace("image_path",
						list.get(position).getImage()), "text/html; charset=UTF-8", null);
				}else{
					streamHolderAd.stream_adImage.loadData((new String(HTML)).replace("image_path",
							"http://img1.imgtn.bdimg.com/it/u=1568907497,643537910&fm=21&gp=0.jpg"), "text/html; charset=UTF-8", null);
				}
//				streamHolderAd.stream_adLogo.setText("推广");
				streamHolderAd.stream_adLogo.setVisibility(View.VISIBLE);
				break;
			}
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			View v = null;
			ViewHolder vh = null;
			switch (viewType) {

			case STREAM:
				v = inflater.inflate(R.layout.native_ad_item, parent, false);
				vh = new StreamHolder(v);
				break;
			case STREAM_AD:
				v = inflater.inflate(R.layout.native_ad_item1, parent, false);
				vh = new StreamHolderAd(v,mListener);
				break;
			}
			return vh;

		}

	    public void setOnItemClickListener(MyItemClickListener listener){
	    	mListener = listener;
	    }

	}


	class Data {
		private String icon;
		private String image;
		private String title;
		private String descript;
		public boolean isAd;
		public int type;
		public NativeAdInfo adInfo;

		public String getImage() {
			return image;
		}
		public void setImage(String image) {
			this.image = image;
		}
		public String getIcon() {
			return icon;
		}
		public void setIcon(String icon) {
			this.icon = icon;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getDescript() {
			return descript;
		}
		public void setDescript(String descript) {
			this.descript = descript;
		}
		public boolean isAd() {
			return isAd;
		}
		public void setAd(boolean isAd) {
			this.isAd = isAd;
		}
		public int getType() {
			return type;
		}
		public void setType(int type) {
			this.type = type;
		}
		public NativeAdInfo getAdInfo() {
			return adInfo;
		}
		public void setAdInfo(NativeAdInfo adInfo) {
			this.adInfo = adInfo;
		}

	}


	@Override
	public void onItemClick(View view, int position) {
		AdViewUtil.logDebug("position:"+position);
		Data data=list.get(position);

		if (data.isAd) {
			data.adInfo.onClick(view);
		}

	}

}

