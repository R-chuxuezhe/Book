package com.coderman.api.book.service.impl;

import com.coderman.api.book.converter.BookConverter;
import com.coderman.api.book.converter.CategoryListConverter;
import com.coderman.api.book.mapper.*;
import com.coderman.api.book.service.BookService;
import com.coderman.api.book.vo.*;
import com.coderman.api.common.exception.ServiceException;
import com.coderman.api.common.pojo.book.Book;
import com.coderman.api.common.pojo.book.BookFindings;
import com.coderman.api.common.pojo.book.Category;
import com.coderman.api.common.pojo.book.Record;
import com.coderman.api.common.utils.CategoryTreeBuilder;
import com.coderman.api.system.vo.PageVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private BookFindingsMapper bookFindingsMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private RecordMapper recordMapper;

    @Autowired
    private RankMapper rankMapper;

    @Override
    public Book add(BookVo bookVo) {
        //新增图书
        Book book=new Book();
        BeanUtils.copyProperties(bookVo,book);
        book.setCreateTime(new Date());
        book.setModifiedTime(new Date());
        book.setDelStatus(1);
        book.setUpDown(1);
        bookMapper.insert(book);
        //新增审核记录
        BookFindings bookFindings=new BookFindings();
        bookFindings.setBookId(book.getId());
        bookFindings.setCreateTime(new Date());
        bookFindings.setCreateUser(book.getCreateUser());
        bookFindings.setStatus(1);
        bookFindingsMapper.insert(bookFindings);
        return book;
    }


    @Override
    public void edit(BookVo bookVo) {
        Book book1=bookMapper.selectByPrimaryKey(bookVo.getId());
        if (book1==null){
            throw new ServiceException("该图书不存在!");
        }
        Book book=new Book();
        BeanUtils.copyProperties(bookVo,book);
        book.setModifiedTime(new Date());
        book.setStatus(1);
        bookMapper.updateByPrimaryKeySelective(book);
        BookFindings bookFindings=new BookFindings();
        Example o = new Example(BookFindings.class);
        Example.Criteria criteria = o.createCriteria();
        criteria.andEqualTo("bookId",book.getId());
        criteria.andEqualTo("createUser",book.getCreateUser());
        criteria.andEqualTo("status",1);
        BookFindings bookFindings1=bookFindingsMapper.selectOneByExample(o);
        if (bookFindings1==null){
            bookFindings.setBookId(book.getId());
            bookFindings.setCreateTime(new Date());
            bookFindings.setCreateUser(book.getCreateUser());
            bookFindings.setStatus(1);
            //新增审核记录
            bookFindingsMapper.insert(bookFindings);
        }
    }

    @Override
    public void delete(Long id) {
        Book book=new Book();
        book.setModifiedTime(new Date());
        book.setDelStatus(2);
        book.setId(id);
        bookMapper.updateByPrimaryKeySelective(book);
    }

    @Override
    public Book getInfo(Long id) {
        Book book=bookMapper.selectByPrimaryKey(id);
        return book;
    }

    @Override
    public void examine(BookFindingsEditVo bookFindingsEditVo) {
        if(2==bookFindingsEditVo.getStatus()){
            bookFindingsEditVo.setFindings("通过");
        }
        BookFindings bookFindings=new BookFindings();
        BeanUtils.copyProperties(bookFindingsEditVo,bookFindings);
        bookFindingsMapper.updateByPrimaryKeySelective(bookFindings);
        Book book=new Book();
        book.setId(bookFindings.getBookId());
        book.setStatus(bookFindings.getStatus());
        bookMapper.updateByPrimaryKeySelective(book);
    }

    @Override
    public void upDown(BookVo bookVo) {
        Book book1=bookMapper.selectByPrimaryKey(bookVo.getId());
        if (book1==null){
            throw new ServiceException("该图书不存在!");
        }
        Book book=new Book();
        BeanUtils.copyProperties(bookVo,book);
        book.setModifiedTime(new Date());
        bookMapper.updateByPrimaryKeySelective(book);
    }

    @Override
    public BookFindings getBookFindings(Long id) {
        Example o = new Example(BookFindings.class);
        Example.Criteria criteria = o.createCriteria();
        criteria.andEqualTo("status",1);
        criteria.andEqualTo("bookId",id);
        return bookFindingsMapper.selectOneByExample(o);
    }

    @Override
    public  List<BookFindings> getBookFindingsAll(Long id) {
        Example o = new Example(BookFindings.class);
        Example.Criteria criteria = o.createCriteria();
        o.setOrderByClause("create_time desc");
        criteria.andEqualTo("bookId",id);
        List<BookFindings>  bookFindingsList =bookFindingsMapper.selectByExample(o);
        return bookFindingsList;
    }

    @Override
    public List<CategoryListVo> getCategory() {
        List<Category> categories=categoryMapper.selectAll();
        List<CategoryListVo> converterToVOList= CategoryListConverter.converterToVOList(categories);
        return CategoryTreeBuilder.build(converterToVOList);
    }


    @Override
    public PageVO<BookVo> findBookList(Integer pageNum, Integer pageSize, BookVo bookVo) {
        PageHelper.startPage(pageNum, pageSize);
        Example o = new Example(Book.class);
        o.setOrderByClause("create_time desc");
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
        if(bookVo.getStatus() !=null){
            criteria.andEqualTo("status",bookVo.getStatus());
        }
        if(bookVo.getCreateUser() !=null){
            criteria.andEqualTo("createUser",bookVo.getCreateUser());
        }
        if(bookVo.getCategoryId() !=null){
            criteria.andEqualTo("categoryId",bookVo.getCategoryId());
        }
        criteria.andEqualTo("delStatus",1);
        List<Book> books = bookMapper.selectByExample(o);
        List<BookVo> bookVos= BookConverter.converterToVOList(books);
        PageInfo<Book> info = new PageInfo<>(books);
        return new PageVO<>(info.getTotal(), bookVos);
    }


    @Override
    public PageVO<BookVo> findBookRecondList(Integer pageNum, Integer pageSize, Book book, Record record) {
        List<Record> recordList=new ArrayList<>();
        List<BookVo> bookVos=new ArrayList<>();
        if((book.getBookName() == null && "".equals(book.getBookName())) || book.getCategoryId() ==null){
            Example o = new Example(Record.class);
            o.setOrderByClause("create_time desc");
            Example.Criteria criteria = o.createCriteria();
            criteria.andEqualTo("type",record.getType());
            criteria.andEqualTo("createUser",record.getCreateUser());
            recordList=recordMapper.selectByExample(o);
            for(Record records:recordList){
                Example o2 = new Example(Book.class);
                Example.Criteria criteria2 = o2.createCriteria();
                criteria2.andEqualTo("id",records.getBookId());
                Book book2 = bookMapper.selectOneByExample(o2);
                BookVo bookVo =new BookVo();
                BeanUtils.copyProperties(book2,bookVo);
                bookVo.setRecordTime(records.getCreateTime());
                bookVos.add(bookVo);
            }
        }else {
            Example o2 = new Example(Book.class);
            o2.setOrderByClause("create_time desc");
            Example.Criteria criteria2 = o2.createCriteria();
            if (book.getBookName() != null && !"".equals(book.getBookName())) {
                criteria2.andLike("bookName", "%" + book.getBookName() + "%");
            }
            if(book.getCategoryId() !=null){
                criteria2.andEqualTo("categoryId",book.getCategoryId());
            }
            List<Book> books = bookMapper.selectByExample(o2);
            for (Book book1:books){
                Example o = new Example(Record.class);
                o.setOrderByClause("create_time desc");
                Example.Criteria criteria = o.createCriteria();
                criteria.andEqualTo("bookId",book1.getId());
                criteria.andEqualTo("createUser",record.getCreateUser());
                List<Record> recordList1=recordMapper.selectByExample(o);
                for (Record record1:recordList1){
                    BookVo bookVo=new BookVo();
                    BeanUtils.copyProperties(book1,bookVo);
                    bookVo.setRecordTime(record1.getCreateTime());
                    bookVos.add(bookVo);
                }
            }
        }
        PageInfo<BookVo> info = new PageInfo<>(bookVos);
        return new PageVO<>(info.getTotal(), bookVos);
    }

    @Override
    public int bookCount(Record record) {
        Example o = new Example(Record.class);
        Example.Criteria criteria = o.createCriteria();
        criteria.andEqualTo("type",record.getType());
        criteria.andEqualTo("createUser",record.getCreateUser());
        int count=recordMapper.selectCountByExample(o);
        return count;
    }

    @Override
    public List<BookRankVo> findRanking(Record record) {
        List<BookRankVo> bookRankVos=new ArrayList<>();
        List<Book> bookList=bookMapper.selectAll();
        for (Book book:bookList){
            Example o = new Example(Record.class);
            Example.Criteria criteria = o.createCriteria();
            criteria.andEqualTo("type",record.getType());
            criteria.andEqualTo("bookId",book.getId());
            int count=recordMapper.selectCountByExample(o);
            BookRankVo bookRankVo=new BookRankVo();
            BeanUtils.copyProperties(book,bookRankVo);
            bookRankVo.setOCC(count);
            bookRankVos.add(bookRankVo);
        }
        Collections.sort(bookRankVos,BookRankVo.occ());//降序
        return bookRankVos;
    }


}
