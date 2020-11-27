package com.coderman.api.book.mapper;



import com.coderman.api.common.pojo.book.Book;
import com.coderman.api.common.pojo.book.Record;
import tk.mybatis.mapper.common.BaseMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;


public interface RankMapper extends Mapper<Book> {

    List<Map> findRanking(Record record);
}
