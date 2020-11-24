package com.coderman.api.book.converter;

import com.coderman.api.book.vo.BookVo;
import com.coderman.api.common.pojo.book.Book;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class BookConverter {

     /**
     * 转voList
     * @param books
     * @return
     */
    public static List<BookVo> converterToVOList(List<Book> books) {
        List<BookVo> bookVos=new ArrayList<>();
        if(!CollectionUtils.isEmpty(books)){
            for (Book book : books) {
                BookVo BookVo = converterToBookVo(book);
                bookVos.add(BookVo);
            }
        }
        return bookVos;
    }

    /***
     * 转VO
     * @param book
     * @return
     */
    public static BookVo converterToBookVo(Book book) {
        BookVo bookVo = new BookVo();
        BeanUtils.copyProperties(book,bookVo);
        return bookVo;
    }
}
