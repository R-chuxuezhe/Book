package com.coderman.api.book.service.impl;

import com.coderman.api.book.converter.BookConverter;
import com.coderman.api.book.mapper.BookMapper;
import com.coderman.api.book.service.BookService;
import com.coderman.api.book.vo.BookVo;
import com.coderman.api.common.pojo.book.Book;
import com.coderman.api.system.vo.PageVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookMapper bookMapper;

    @Override
    public Book add(BookVo bookVo) {
        Book book=new Book();
        BeanUtils.copyProperties(bookVo,book);
        book.setCreateTime(new Date());
        book.setModifiedTime(new Date());
        bookMapper.insert(book);
        return book;
    }

    @Override
    public void edit(BookVo bookVo) {
        Book book=new Book();
        BeanUtils.copyProperties(bookVo,book);
        book.setModifiedTime(new Date());
        bookMapper.updateByPrimaryKeySelective(book);
    }

    @Override
    public void delete(Long id) {
        bookMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Book getInfo(Long id) {
        Book book=bookMapper.selectByPrimaryKey(id);
        return book;
    }

    @Override
    public PageVO<BookVo> findBookList(Integer pageNum, Integer pageSize, BookVo bookVo) {
        PageHelper.startPage(pageNum, pageSize);
        Example o = new Example(Book.class);
        Example.Criteria criteria = o.createCriteria();
        if (bookVo.getBookName() != null && !"".equals(bookVo.getBookName())) {
            criteria.andLike("bookName", "%" + bookVo.getBookName() + "%");
        }
        if (bookVo.getAuthor() !=null && !"".equals(bookVo.getAuthor())){
            criteria.andLike("author", "%" + bookVo.getAuthor() + "%");
        }
        if (bookVo.getUpDown() !=null){
            criteria.andLike("upDown", "%" + bookVo.getUpDown() + "%");
        }
        if (bookVo.getPress() !=null && !"".equals(bookVo.getPress())){
            criteria.andLike("press", "%" + bookVo.getPress() + "%");
        }
        List<Book> books = bookMapper.selectByExample(o);
        List<BookVo> bookVos= BookConverter.converterToVOList(books);
        PageInfo<Book> info = new PageInfo<>(books);
        return new PageVO<>(info.getTotal(), bookVos);
    }

}
