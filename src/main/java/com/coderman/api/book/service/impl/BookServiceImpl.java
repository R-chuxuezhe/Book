package com.coderman.api.book.service.impl;

import com.coderman.api.book.converter.BookConverter;
import com.coderman.api.book.converter.CategoryListConverter;
import com.coderman.api.book.mapper.*;
import com.coderman.api.book.service.BookService;
import com.coderman.api.book.vo.*;
import com.coderman.api.common.bean.ActiveUser;
import com.coderman.api.common.exception.ServiceException;
import com.coderman.api.common.pojo.book.Book;
import com.coderman.api.common.pojo.book.BookFindings;
import com.coderman.api.common.pojo.book.Category;
import com.coderman.api.common.pojo.book.Record;
import com.coderman.api.common.pojo.system.User;
import com.coderman.api.common.utils.CategoryTreeBuilder;
import com.coderman.api.system.mapper.UserMapper;
import com.coderman.api.system.vo.PageVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
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
    private UserMapper userMapper;

    @Override
    public Book add(BookVo bookVo) {
        //新增图书
        Book book=new Book();
        BeanUtils.copyProperties(bookVo,book);
        book.setCreateTime(new Date());
        book.setModifiedTime(new Date());
        book.setDelStatus(1);
        book.setUpDown(1);
        book.setStatus(1);
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
        BookFindings bookFindings=new BookFindings();
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        Long id=activeUser.getUser().getId();
        bookFindings.setBookId(bookFindingsEditVo.getId());
        bookFindings.setCreateTime(new Date());
        bookFindings.setCreateUser(id);
        bookFindings.setFindings(bookFindingsEditVo.getFindings());
        bookFindings.setStatus(bookFindingsEditVo.getStatus());
        //新增审核记录
        bookFindingsMapper.insert(bookFindings);
        Book book=new Book();
        book.setId(bookFindingsEditVo.getBookId());
        book.setStatus(bookFindingsEditVo.getStatus());
        bookMapper.updateByPrimaryKeySelective(book);
    }

    @Override
    public void upDown(BookUpDownVo bookVo) {
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
        if (bookVo.getUpDown() !=null){
            criteria.andEqualTo("upDown",bookVo.getUpDown());
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

        if (bookVo.getKeyword() != null && !"".equals(bookVo.getKeyword())) {
            criteria.andLike("bookName","%"+ bookVo.getKeyword() +"%" );
        }
        List<Book> books = bookMapper.selectByExample(o);
        List<BookVo> bookVos= BookConverter.converterToVOList(books);
        List<BookVo> bookVoList=getBookUserName(bookVos);
        PageInfo<Book> info = new PageInfo<>(books);
        return new PageVO<>(info.getTotal(), bookVoList);
    }





    public  List<BookVo> getBookUserName(List<BookVo> bookVos){
        for(BookVo bookVo:bookVos){
            User user= userMapper.selectByPrimaryKey(bookVo.getCreateUser());
            Category category=categoryMapper.selectByPrimaryKey(bookVo.getCategoryId());
           bookVo.setCreateUserName(user.getNickname());
           bookVo.setCategoryName(category.getName());
        }
        return bookVos;
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
        List<BookVo> bookVoList=getBookUserName(bookVos);
        PageInfo<BookVo> info = new PageInfo<>(bookVoList);
        return new PageVO<>(info.getTotal(), bookVoList);
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
        List<Book> bookList=new ArrayList<>();
        List<BookRankVo> bookRankVos=new ArrayList<>();
        if(record.getCreateUser()!=null){
            Example o2 = new Example(Book.class);
            Example.Criteria criteria2 = o2.createCriteria();
            criteria2.andEqualTo("createUser",record.getCreateUser());
            bookList=bookMapper.selectByExample(o2);
        }else {
             bookList=bookMapper.selectAll();
        }
        for (Book book:bookList){
            Example o = new Example(Record.class);
            Example.Criteria criteria = o.createCriteria();
            criteria.andEqualTo("type",record.getType());
            criteria.andEqualTo("bookId",book.getId());
            int count=recordMapper.selectCountByExample(o);
            BookRankVo bookRankVo=new BookRankVo();
            BeanUtils.copyProperties(book,bookRankVo);
            User user= userMapper.selectByPrimaryKey(book.getCreateUser());
            Category category=categoryMapper.selectByPrimaryKey(book.getCategoryId());
            bookRankVo.setCreateUserName(user.getNickname());
            bookRankVo.setCategoryName(category.getName());
            bookRankVo.setOCC(count);
            bookRankVos.add(bookRankVo);
        }
        Collections.sort(bookRankVos,BookRankVo.occ());//降序
        return bookRankVos;
    }

    @Override
    public int bookUpCount() {
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        Long id=activeUser.getUser().getId();
        Example o2 = new Example(Book.class);
        Example.Criteria criteria2 = o2.createCriteria();
        criteria2.andEqualTo("createUser",id);
        int count = bookMapper.selectCountByExample(o2);
        return count;
    }

    @Override
    public List<RecordCountVo> coverReadCount(RecordCountVo recordCountVo) {
        List<RecordCountVo> recordCountVoList=new ArrayList<>();
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        Long id=activeUser.getUser().getId();
        Example o = new Example(Book.class);
        Example.Criteria criteria2 = o.createCriteria();
        criteria2.andEqualTo("createUser",id);
        List<Book> books = bookMapper.selectByExample(o);
        int day=recordCountVo.getDay()-1;
        for (int i=day;i>=0;i--){
            Calendar now = Calendar.getInstance();
            now.add(Calendar.DAY_OF_MONTH, -i);
            String endDate = new SimpleDateFormat("yyyy-MM-dd").format(now.getTime());
            RecordCountVo recordCountVo1=new RecordCountVo();
            int dayCount=0;
            for(int b=0;b<books.size();b++){
                Example o2= new Example(Record.class);
                Example.Criteria criteria = o2.createCriteria();
                criteria.andEqualTo("type",recordCountVo.getType());
                criteria.andEqualTo("bookId",books.get(b).getId());
                criteria.andGreaterThanOrEqualTo("createTime",endDate+" 00:00:00");
                criteria.andLessThanOrEqualTo("createTime",endDate+" 23:59:59");
                int count=recordMapper.selectCountByExample(o2);
                dayCount+=count;
            }
            recordCountVo1.setSdate(endDate);
            recordCountVo1.setCoverRead(dayCount);
            recordCountVoList.add(recordCountVo1);
        }
        return recordCountVoList;
    }


}
