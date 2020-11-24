package com.coderman.api.book.service;

import com.coderman.api.book.vo.BookVo;
import com.coderman.api.common.pojo.book.Book;
import com.coderman.api.system.vo.PageVO;

public interface BookService {

    Book add(BookVo bookVo);

    PageVO<BookVo> findBookList(Integer pageNum, Integer pageSize, BookVo bookVo);

    void edit(BookVo bookVo);

    void delete(Long id);

    Book getInfo(Long id);
}
