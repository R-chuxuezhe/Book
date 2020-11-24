package com.coderman.api.book.controller;

import com.coderman.api.biz.vo.ConsumerVO;
import com.coderman.api.biz.vo.SupplierVO;
import com.coderman.api.book.service.BookService;
import com.coderman.api.book.vo.BookVo;
import com.coderman.api.common.annotation.ControllerEndpoint;
import com.coderman.api.common.bean.ActiveUser;
import com.coderman.api.common.bean.ResponseBean;
import com.coderman.api.common.pojo.book.Book;
import com.coderman.api.system.vo.PageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    @ControllerEndpoint(exceptionMessage = "书籍添加失败", operation = "书籍添加")
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
    @ControllerEndpoint(exceptionMessage = "书籍修改失败", operation = "书籍修改")
    @ApiOperation(value = "修改书籍")
    @PostMapping("/edit")
    public ResponseBean edit(@RequestBody @Validated BookVo bookVo) {
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        bookVo.setCreateUser(activeUser.getUser().getId());
        bookService.edit(bookVo);
        return ResponseBean.success("成功");
    }

    /**
     * 删除书籍
     * @param id
     * @return
     */
    @ControllerEndpoint(exceptionMessage = "书籍删除失败", operation = "书籍删除")
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
    @ControllerEndpoint(exceptionMessage = "获取单个书籍失败", operation = "获取单个书籍")
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

}
