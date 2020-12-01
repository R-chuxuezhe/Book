package com.coderman.api.book.controller;

import com.coderman.api.book.service.BookService;
import com.coderman.api.book.vo.*;
import com.coderman.api.common.bean.ActiveUser;
import com.coderman.api.common.bean.ResponseBean;
import com.coderman.api.common.pojo.book.Book;
import com.coderman.api.common.pojo.book.BookFindings;
import com.coderman.api.common.pojo.book.Record;
import com.coderman.api.system.vo.PageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "书籍接口")
@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;


    /**
     * 书籍列表
     *
     * @return
     */
    @ApiOperation(value = "书籍列表", notes = "书籍列表,根据书籍名模糊查询")
    @GetMapping("/findBookList")
    public ResponseBean findBookList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                         @RequestParam(value = "pageSize") Integer pageSize,
                                         BookVo bookVo) {
        PageVO<BookVo> bookVoPageVO = bookService.findBookList(pageNum, pageSize, bookVo);
        return ResponseBean.success(bookVoPageVO);
    }


    /**
     * 添加书籍
     *
     * @return
     */
    @ApiOperation(value = "添加书籍")
    @PostMapping("/add")
    public ResponseBean add(@RequestBody @Validated BookVo bookVo) {
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        bookVo.setCreateUser(activeUser.getUser().getId());
        bookService.add(bookVo);
        return ResponseBean.success("成功");
    }


    /**
     * 修改书籍
     *
     * @return
     */
    @ApiOperation(value = "修改书籍")
    @PostMapping("/edit")
    public ResponseBean edit(@RequestBody @Validated BookVo bookVo) {
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        bookVo.setCreateUser(activeUser.getUser().getId());
        bookService.edit(bookVo);
        return ResponseBean.success("成功");
    }


    /**
     * 上下架书籍
     *
     * @return
     */
    @ApiOperation(value = "上下架书籍")
    @PostMapping("/upDown")
    public ResponseBean upDown(@RequestBody @Validated BookUpDownVo upDownVo) {
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        upDownVo.setCreateUser(activeUser.getUser().getId());
        bookService.upDown(upDownVo);
        return ResponseBean.success("成功");
    }


    /**
     * 删除书籍
     * @param id
     * @return
     */
    @ApiOperation(value = "删除书籍", notes = "删除书籍")
    @DeleteMapping("/delete/{id}")
    public ResponseBean delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseBean.success("删除书籍成功");
    }

    /**
     * 获取单个书籍信息
     * @param id
     * @return
     */
    @ApiOperation(value = "获取单个书籍", notes = "获取单个书籍")
    @GetMapping("/getInfo/{id}")
    public ResponseBean getInfo(@PathVariable Long id){
        Book book=bookService.getInfo(id);
        if (book!=null){
            return ResponseBean.success(book);
        }else {
            return ResponseBean.error("获取单个书籍失败！");
        }
    }


    /**
     * 获取书籍的全部审核记录
     * @param id  //书籍ID
     * @return
     */
    @ApiOperation(value = "获取书籍的全部审核记录", notes = "获取书籍的全部审核记录")
    @GetMapping("/getBookFindingsAll/{id}")
    public ResponseBean getBookFindingsAll(@PathVariable Long id){
        List<BookFindings> bookFindingsList=bookService.getBookFindingsAll(id);
        if (bookFindingsList.size()>0){
            return ResponseBean.success(bookFindingsList);
        }else {
            return ResponseBean.error("获取审核记录失败！");
        }
    }


    /**
     * 审核书籍
     *
     * @return
     */
    @ApiOperation(value = "审核书籍")
    @PostMapping("/examine")
    public ResponseBean examine(@RequestBody @Validated BookFindingsEditVo bookFindingsEditVo) {
        bookService.examine(bookFindingsEditVo);
        return ResponseBean.success("成功");
    }

    /**
     * 获取图书检索失败！
     * @param
     * @return
     */
    @ApiOperation(value = "获取图书检索失败！", notes = "获取图书检索失败！")
    @GetMapping("/getCategory")
    public ResponseBean getCategory(){
        List<CategoryListVo> categoryListVo=bookService.getCategory();
        if(categoryListVo!=null){
            return ResponseBean.success(categoryListVo);
        }else {
            return ResponseBean.error("获取图书检索失败！");
        }
    }


    /**
     * 个人借阅及下载列表
     *
     * @return
     */
    @ApiOperation(value = "个人借阅及下载列表", notes = "个人借阅及下载列表")
    @GetMapping("/findBookRecondList")
    public ResponseBean findBookRecondList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                           @RequestParam(value = "pageSize") Integer pageSize,
                                           Book book, Record Record) {
        PageVO<BookVo> bookVoPageVO = bookService.findBookRecondList(pageNum, pageSize, book,Record);
        return ResponseBean.success(bookVoPageVO);
    }

    /**
     * 阅读或下载量查询
     *
     * @return
     */
    @ApiOperation(value = "阅读或下载量查询")
    @GetMapping("/bookCount")
    public ResponseBean bookCount(Record record) {
        int count=bookService.bookCount(record);
        return ResponseBean.success(count);
    }


    /**
     * 上传
     *
     * @return
     */
    @ApiOperation(value = "上传")
    @GetMapping("/bookUpCount")
    public ResponseBean bookUpCount() {
        int count=bookService.bookUpCount();
        return ResponseBean.success(count);
    }


    /**
     * 全部上传、下载、阅读
     *
     * @return
     */
    @ApiOperation(value = "全部上传、下载、阅读")
    @GetMapping("/bookAllCount")
    public ResponseBean bookAllCount() {
        NumberVo numberVo=bookService.bookAllCount();
        return ResponseBean.success(numberVo);
    }

    /**
     *
     *添加阅读或下载记录
     * @return
     */
    @ApiOperation(value = "添加阅读或下载记录")
    @PostMapping("/addRecord")
    public ResponseBean addOrDelRecord(@RequestBody @Validated RecordVo recordVo) {
        bookService.addRecord(recordVo);
        return ResponseBean.success("成功");
    }


    /**
     *
     *删除阅读或下载记录
     * @return
     */
    @ApiOperation(value = "删除阅读或下载记录")
    @PostMapping("/delRecord")
    public ResponseBean delRecord(@RequestBody @Validated RecordDelVo recordDelVo) {
        bookService.delRecord(recordDelVo);
        return ResponseBean.success("成功");
    }


    /**
     * 分类的阅读或下载量查询
     *
     * @return
     */
    @ApiOperation(value = "分类的阅读或下载量查询")
    @GetMapping("/bookCategoryCount")
    public ResponseBean bookCategoryCount() {
        List<CategoryIndexVo> categoryIndexVos=bookService.bookCategoryCount();
        return ResponseBean.success(categoryIndexVos);
    }

}

