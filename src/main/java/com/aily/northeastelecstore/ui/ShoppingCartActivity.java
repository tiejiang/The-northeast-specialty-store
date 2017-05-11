package com.aily.northeastelecstore.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.aily.northeastelecstore.R;
import com.aily.northeastelecstore.adapter.ShopAdapter;
import com.aily.northeastelecstore.bean.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiejiang on 17-5-10.
 */

public class ShoppingCartActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private List<Product> datas; //数据源
    private ShopAdapter adapter; //自定义适配器
    private ListView listView;   //ListView控件
//    private Vector mCartVector = new Vector();
    private ArrayList mArrayList = new ArrayList();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        listView = (ListView) findViewById(R.id.listView);

        //获得商品的Vector数据
        Intent mIntent = getIntent();
        mArrayList = (ArrayList) (mIntent.getSerializableExtra("cartVector"));
        // test
        for (int i = 0; i < mArrayList.size(); i ++){
            Log.d( "TIEJIANG", "ShoppingCartActivity---mCartVector[i]= " + mArrayList.get(i));
        }
        // 向list写入数据
        datas = new ArrayList<Product>();
        Product product = null;
        for (int i = 0; i < mArrayList.size(); i ++){
            product = new Product();
            String cartName = CategoryActivity.mTitleValues[Integer.valueOf((String)(mArrayList.get(i)))];
            product.setName("商品："+ cartName +":单价:");
            product.setNum(1);
            product.setPrice(i);
            datas.add(product);
        }

        // 模拟数据
//        datas = new ArrayList<Product>();
//        Product product = null;
//        for (int i = 0; i < 8; i++) {
//            product = new Product();
//            product.setName("商品："+i+":单价:"+i);
//            product.setNum(1);
//            product.setPrice(i);
//            datas.add(product);
//        }
        adapter = new ShopAdapter(datas,this);
        listView.setAdapter(adapter);

        //以上就是我们常用的自定义适配器ListView展示数据的方法了
        //解决问题：在哪里处理按钮的点击响应事件，是适配器 还是 Activity或者Fragment，这里是在Activity本身处理接口
        //执行添加商品数量，减少商品数量的按钮点击事件接口回调
        adapter.setOnAddNum(this);
        adapter.setOnSubNum(this);
        listView.setOnItemClickListener(this);
    }
    //点击某个按钮的时候，如果列表项所需的数据改变了，如何更新UI
    @Override
    public void onClick(View view) {
        Object tag = view.getTag();
        switch (view.getId()){
            case R.id.item_btn_add: //点击添加数量按钮，执行相应的处理
                // 获取 Adapter 中设置的 Tag
                if (tag != null && tag instanceof Integer) { //解决问题：如何知道你点击的按钮是哪一个列表项中的，通过Tag的position
                    int position = (Integer) tag;
                    //更改集合的数据
                    int num = datas.get(position).getNum();
                    num++;
                    datas.get(position).setNum(num); //修改集合中商品数量
                    datas.get(position).setPrice(position*num); //修改集合中该商品总价 数量*单价
                    //解决问题：点击某个按钮的时候，如果列表项所需的数据改变了，如何更新UI
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.item_btn_sub: //点击减少数量按钮 ，执行相应的处理
                // 获取 Adapter 中设置的 Tag
                if (tag != null && tag instanceof Integer) {
                    int position = (Integer) tag;
                    //更改集合的数据
                    int num = datas.get(position).getNum();
                    if (num>0) {
                        num--;
                        datas.get(position).setNum(num); //修改集合中商品数量
                        datas.get(position).setPrice(position * num); //修改集合中该商品总价 数量*单价
                        adapter.notifyDataSetChanged();
                    }
                }
                break;
        }
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        addToCartCar();
//        Toast.makeText(ShoppingCartActivity.this,"点击了第"+i+"个列表项",Toast.LENGTH_SHORT).show();
    }

    /**
     * 下单的 dialog
     * */
    public void addToCartCar(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(ShoppingCartActivity.this);
        builder.setTitle("提示");
        builder.setMessage("是否下单？");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(ShoppingCartActivity.this, "在线结算SDK需付费，尚未开发", Toast.LENGTH_SHORT).show();

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }
}
