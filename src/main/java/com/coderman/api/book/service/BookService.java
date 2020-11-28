package com.coderman.api.book.service;

import com.coderman.api.book.vo.*;
import com.coderman.api.common.pojo.book.Book;
import com.coderman.api.common.pojo.book.BookFindings;
import com.coderman.api.common.pojo.book.Record;
import com.coderman.api.system.vo.PageVO;

import java.util.List;
import java.util.Map;

public interface BookService {

    Book add(BookVo bookVo);

    PageVO<BookVo> findBookList(Integer pageNum, Integer pageSize, BookVo bookVo);

    void edit(BookVo bookVo);

    void delete(Long id);

    Book getInfo(Long id);

    void examine(BookFindingsEditVo bookFindingsEditVo);

    void upDown(BookUpDownVo bookVo);

    BookFindings getBookFindings(Long id);

    List<BookFindings> getBookFindingsAll(Long id);

    List<CategoryListVo> getCategory();

    PageVO<BookVo> findBookRecondList(Integer pageNum, Integer pageSize, Book book, Record record);

    int bookCount(Record record);

    List<BookRankVo> findRanking(Record record);
}
