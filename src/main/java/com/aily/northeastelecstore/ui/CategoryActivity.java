package com.aily.northeastelecstore.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aily.northeastelecstore.R;
import com.aily.northeastelecstore.ui.base.BaseActivity;


public class CategoryActivity extends BaseActivity {

	private ListView catergory_listview;
	private LayoutInflater layoutInflater;
	public static Handler mHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_category);
		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
		catergory_listview=(ListView)this.findViewById(R.id.catergory_listview);

		catergory_listview.setAdapter(new CatergorAdapter());
		catergory_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterview, View view, int parent,
					long id) {
				addToCartCar(parent);
//				Toast.makeText(CategoryActivity.this, "你点击了第"+id+"项"+ "parent= " + parent, Toast.LENGTH_SHORT).show();

			}
		});
	}

	//为购物车准备资源
	public void returnMessageFromServer(String chat){

        if (chat!=null) {
			Message msg = new Message();
			msg.obj = chat;
			mHandler.sendMessage(msg);

		}
	}


	/**
	 * 加入购物车的dialog
	 * @param cart_index  所选择的商品索引
	 * */
	public void addToCartCar(final int cart_index){

		View mDialogView = getLayoutInflater().inflate(R.layout.dialog_view, null);
		TextView editText = (TextView)mDialogView.findViewById(R.id.cart_discribe);
		editText.setText(mContentValues[cart_index]);
		final AlertDialog.Builder builder = new AlertDialog.Builder(CategoryActivity.this);
		builder.setTitle("是否加入购物车？");
		builder.setMessage(mContentValues[cart_index]);
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				returnMessageFromServer(String.valueOf(cart_index));
				Toast.makeText(CategoryActivity.this, "已经加入购物车", Toast.LENGTH_SHORT).show();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {

			}
		});
		builder.create().show();
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

	}
	
	private class CatergorAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mImageIds.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@SuppressWarnings("null")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = new ViewHolder();
			layoutInflater=LayoutInflater.from(CategoryActivity.this);
			
			//组装数据
			if(convertView==null){
				convertView=layoutInflater.inflate(R.layout.activity_category_item, null);
				holder.image=(ImageView) convertView.findViewById(R.id.catergory_image);
				holder.title=(TextView) convertView.findViewById(R.id.catergoryitem_title);
				holder.content=(TextView) convertView.findViewById(R.id.catergoryitem_content);
				//使用tag存储数据
				convertView.setTag(holder);
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			holder.image.setImageResource(mImageIds[position]);
			holder.title.setText(mTitleValues[position]);
			holder.content.setText(mContentValues[position]);
		//	holder.title.setText(array[position]);
			
			return convertView;
		
		}
		
	}
	
	
	// 适配显示的图片数组
	public static Integer[] mImageIds = {R.drawable.catergory_appliance,R.drawable.catergory_book,R.drawable.catergory_cloth,R.drawable.catergory_deskbook,
			R.drawable.catergory_digtcamer,R.drawable.catergory_furnitrue,R.drawable.catergory_mobile,R.drawable.catergory_skincare
			 };
	//给照片添加文字显示(Title)
//				private String[] mTitleValues = { "家电", "图书", "衣服", "笔记本", "数码",
//						"家具", "手机", "护肤" };
	public static String[] mTitleValues = { "野山菌", "我也不清楚啥玩意儿", "水漏～子", "东北山参", "木耳",
			"东北稻米", "鹿茸", "人参" };

	public static String[] mContentValues={ "野山菌-非常好吃", "我也不清楚啥玩意儿-非常好吃", "水漏～子-非常好吃",
			"东北山参-非常好吃", "木耳-非常好吃", "东北稻米-非常好吃", "鹿茸-非常好吃", "人参-非常好吃" };
			

	 public static class ViewHolder {
			ImageView image;
			TextView title;
			TextView content;
		}
	
	

}
