package net.csdn.mvvm.ui.adapter;

import java.util.List;

public interface AdapterMethod<T> {

    /**
     * 获取所有数据
     */
    List<T> getDatas();

    /**
     * 设置所有数据
     */
    void setDatas(List<T> datas);

    /**
     * 在末尾添加批量数据
     */
    void addDatas(List<T> datas);

    /**
     * 在某个位置添加批量数据
     */
    void addDatas(int position, List<T> datas);

    /**
     * 在末尾添加单个数据
     */
    void addData(T data);

    /**
     * 在某个位置添加单个数据
     */
    void addData(int position, T data);

    /**
     * 移除某个位置数据
     */
    void removeData(int position);
}
