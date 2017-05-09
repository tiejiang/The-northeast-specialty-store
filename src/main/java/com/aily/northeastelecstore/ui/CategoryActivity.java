package com.aily.northeastelecstore.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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
				Toast.makeText(CategoryActivity.this, "你点击了第"+id+"项"+ "parent= " + parent, Toast.LENGTH_SHORT).show();
				
			}
		});
	}
	//加入购物车的dialog
	public void addToCartCar(int cart_index){

		final AlertDialog.Builder builder = new AlertDialog.Builder(CategoryActivity.this);
		builder.setTitle("提示");
		builder.setMessage("是否加入购物车？");
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {

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
	private Integer[] mImageIds = {R.drawable.catergory_appliance,R.drawable.catergory_book,R.drawable.catergory_cloth,R.drawable.catergory_deskbook,
			R.drawable.catergory_digtcamer,R.drawable.catergory_furnitrue,R.drawable.catergory_mobile,R.drawable.catergory_skincare
			 };
	//给照片添加文字显示(Title)
//				private String[] mTitleValues = { "家电", "图书", "衣服", "笔记本", "数码",
//						"家具", "手机", "护肤" };
	private String[] mTitleValues = { "山珍", "农产品", "熟食", "养生", "家禽",
			"家居", "装饰", "民俗" };

	private String[] mContentValues={"山珍", "农产品","熟食", "养生",
			"家禽", "家居", "装饰", "民俗"};
			

	 public static class ViewHolder {
			ImageView image;
			TextView title;
			TextView content;
		}
	
	

}
