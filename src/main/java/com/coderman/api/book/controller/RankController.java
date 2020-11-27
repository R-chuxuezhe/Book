package com.coderman.api.book.controller;

import com.coderman.api.book.service.BookService;
import com.coderman.api.book.vo.BookRankVo;
import com.coderman.api.book.vo.BookVo;
import com.coderman.api.common.bean.ResponseBean;
import com.coderman.api.common.pojo.book.Book;
import com.coderman.api.common.pojo.book.Record;
import com.coderman.api.system.vo.PageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "排行接口")
@RestController
@RequestMapping("/rank")
public class RankController {


    @Autowired
    private BookService bookService;

    /**
     * 排行
     *
     * @return
     */
    @ApiOperation(value = "排行", notes = "排行")
    @GetMapping("/findRanking")
    public ResponseBean findRanking(Record record) {
        List<BookRankVo> bookRankVos = bookService.findRanking(record);
        return ResponseBean.success(bookRankVos);
    }


}
