package westore.rainbow.cn.pulltorefreshdemo.utils;

import java.util.ArrayList;
import java.util.List;

import westore.rainbow.cn.pulltorefreshdemo.bean.ItemEntity;

/**
 * Created by LinZaixiong on 2016/10/29.
 */

public class DataSource {

    private int index = 0;
    private int pageSize = 10;
    private int totalPage = 10;

    public boolean isEndPage(){
        return index == totalPage;
    }
    public List<ItemEntity> getDataList(int index){

        List<ItemEntity> list = new ArrayList<ItemEntity>();

        this.index = index;

        if(index < totalPage){
            ItemEntity item = new ItemEntity();
            list.add(item);
            item = new ItemEntity();
            list.add(item);
            item = new ItemEntity();
            list.add(item);
            item = new ItemEntity();
            list.add(item);
            item = new ItemEntity();
            list.add(item);
            item = new ItemEntity();
            list.add(item);
            item = new ItemEntity();
            list.add(item);
            item = new ItemEntity();
            list.add(item);
            item = new ItemEntity();
            list.add(item);
            item = new ItemEntity();
            list.add(item);
        }

        return list;
    }
}
