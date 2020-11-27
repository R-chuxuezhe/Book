package com.coderman.api.book.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.Date;

@Data
public class BookRankVo {

    private Long id;

    @NotBlank(message = "图书名称不能为空")
    private String bookName;

    @NotBlank(message = "图书地址不能为空")
    private String bookUrl;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    @NotBlank(message = "作者不能为空")
    private String author;

    @NotBlank(message = "出版社不能为空")
    private String press;

    private Integer upDown;

    private Integer status;

    private Long createUser;

    @NotNull(message = "图书检索ID不能为空")
    private Long categoryId;

    @NotBlank(message = "备注信息不能为空")
    private String remark;

    private Integer delStatus;

    @NotBlank(message = "图书封面不能为空")
    private String cover;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date recordTime;

    private Integer OCC;

    /*
     * 排序,根据OCC排序  出现次数
     */
    public static Comparator<BookRankVo> occ(){
        Comparator<BookRankVo> comparator = (o2, o1) -> {  //o2 在前 降序排列   o1在前升序排列
            if(o1.getOCC() != o2.getOCC()){
                return (int) (o1.getOCC() - o2.getOCC());
            }
            return 0;
        };
        return comparator;
    }
}
